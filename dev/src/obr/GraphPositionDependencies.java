package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.IllegalEdgeException;
import moca.graphs.edges.NeighbourEdge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.NoSuchElementException;


public class GraphPositionDependencies extends DirectedSimpleGraph<Predicate,Boolean> {

	public GraphPositionDependencies() throws IllegalConstructionException {
		super();
	}

	public void addPredicate(Predicate p) {
		if (!_predicateIndex.containsKey(p)) {
			_predicateIndex.put(p,getNbVertices());
			for (int i = 0 ; i < p.getArity() ; i++)
				addVertex(p);
		}
	}
	
	public void addEdge(Predicate p, int positionP, Predicate q, int positionQ) throws IllegalEdgeException {
		addEdge(p,positionP,q,positionQ,false);
	}

	public void addEdge(Predicate p, int positionP, Predicate q, int positionQ, Boolean special) throws IllegalEdgeException {
		addEdge(getVertex(p,positionP),getVertex(q,positionQ),special);
	}

	public Vertex<Predicate> getVertex(Predicate p, int position) throws NoSuchElementException {
		if (position >= p.getArity())
			throw new NoSuchElementException();
		Integer pIndex = _predicateIndex.get(p);
		if (pIndex == null) 
			throw new NoSuchElementException();
		return getVertex(pIndex+position);
	}

	public boolean finiteRank() {
		if (isAcyclic()) {
			System.out.println("acyclic");
			return true;
		}
		ArrayList<Vertex<Predicate> > scc = null;
		boolean success = true;
		for (int i = 0 ; (i < getNbComponents()) && success ; i++) {
			scc = getComponent(i);
			System.out.println("scc = "+scc);
			for (int j = 0 ; (j < scc.size()) && success ; j++) {
				for (int k = 0 ; (k < scc.size()) && success ; k++) {
					try {
						if (getEdgeValue(scc.get(j).getID(),scc.get(k).getID()) == true) {
							System.out.println("success = false");
							success = false;
						}
					}
					catch (NoSuchElementException e) {
						// the edge does not exist, 
						// in particular there is no special edge
					}
				}
			}
		}
		return success;
	}

	private HashMap<Predicate,Integer> _predicateIndex = new HashMap<Predicate,Integer>();

};


