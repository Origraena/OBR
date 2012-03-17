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
//			System.out.println("acyclic");
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
//							System.out.println("success = false");
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

	public String predicatePositionToString(int vertexID) {
		Predicate p = get(vertexID);
		int position = 1;
		boolean end = false;
		while ((vertexID - position >= 0) && (!end)) {
			if (p.compareTo(get(vertexID-position)) == 0)
				position++;
			else
				end = true;
		}
		position--;
		return p.getLabel() + "[" + position + "]";
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		NeighbourEdge<Boolean> edge = null;

		// vertices
		for (int i = 0 ; i < getNbVertices() ; i++) {
			result.append(i);
			result.append(" : ");
			result.append(predicatePositionToString(i));
			result.append('\n');
		}
		result.append('\n');
		
		/*while (predicateIndex < getNbVertices()) {
			positionIndex = 0;
			while (positionIndex < get(predicateIndex).getArity()) {
				result.append(predicateIndex + positionIndex);
				result.append(" : ");
				result.append(get(predicateIndex).getLabel());
				result.append("[");
				result.append(positionIndex);
				result.append("]");
				result.append('\n');
				positionIndex++;
			}
			predicateIndex += positionIndex;
		}*/
	
		// edges
		for (int i = 0 ; i < getNbVertices() ; i++) {
			for (Iterator<NeighbourEdge<Boolean> > iterator = neighbourIterator(i) ; iterator.hasNext() ;) {
				edge = iterator.next();
				result.append(predicatePositionToString(i));
				if (edge.getValue() == true)
					result.append("\tx->\t");
				else
					result.append("\t-->\t");
				result.append(predicatePositionToString(edge.getIDV()));
				result.append('\n');
			}
		}
		return result.toString();
	}

	private HashMap<Predicate,Integer> _predicateIndex = new HashMap<Predicate,Integer>();

};


