package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class WeaklyAcyclicCheck implements DecidableClassCheck {

	public static final DecidableClassLabel LABEL = new DecidableClassLabel("wa");

	/**
	 * Checks a graph of rule dependencies.
	 * @return the decidable class label if the grd belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel grdCheck(GraphRuleDependencies grd) {
		init(grd,-1);
//		System.out.println(_graphPosDep);
		if (_graphPosDep.finiteRank())
			return LABEL;
		return null;
	}

	/**
	 * Checks only a strongly connected component of the graph of rule dependencies.
	 * @param grd The graph of rule dependencies.
	 * @param scc The strongly connected component to be checked.
	 * @return the decidable class label if the strongly connected component belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID) {
		init(grd,sccID);
//		System.out.println(_graphPosDep);
		if (_graphPosDep.finiteRank())
			return LABEL;
		return null;
	}

	protected void init(GraphRuleDependencies grd, int sccID) {
		try {_graphPosDep = new GraphPositionDependencies();}
		catch (IllegalConstructionException e) { }
		Iterable<Vertex<AtomicRule> > scc;
		
		if (sccID < 0)
			scc = grd.getVertexCollection();
		else 
			scc = grd.getComponent(sccID);

		// vertices
		for (Vertex<AtomicRule> rule : scc) {
			for (int i = 0 ; i < rule.getValue().getNbAtoms() ; i++)
				_graphPosDep.addPredicate(rule.getValue().getPredicate(i));
		}

		// edges
		Vertex<Object> headVertex = null;
		AtomicRule rule = null;
		Term neighbour = null;
		Vertex<Object> vertex = null;
		NeighbourEdge<Integer> edge = null;
		NeighbourEdge<Integer> edge2 = null;
		NeighbourEdge<Integer> edge3 = null;
		Predicate p,q;
		Vertex<Predicate> ri = null;
		Vertex<Predicate> sj = null;
		int positionP, positionQ;
		for (Vertex<AtomicRule> vertexRule : scc) {
			rule = vertexRule.getValue();
			headVertex = rule.getHead();
			for (Iterator<NeighbourEdge<Integer> > neighbourIterator = rule.neighbourIterator(headVertex.getID()) ;
				neighbourIterator.hasNext() ;) {
				edge = neighbourIterator.next();
				neighbour = rule.getTerm(edge.getIDV()-rule.getNbAtoms());
				if (rule.isUniversal(rule.getVertex(edge.getIDV()))) {
					for (Iterator<NeighbourEdge<Integer> > neighbourIterator2 = rule.neighbourIterator(edge.getIDV()) ;
						neighbourIterator2.hasNext() ;) {
						edge2 = neighbourIterator2.next();
						vertex = rule.getVertex(edge2.getIDV());
						if (rule.isBody(vertex)) {
							p = (Predicate)(vertex.getValue());
							positionP = edge2.getValue();
							ri = _graphPosDep.getVertex(p,positionP);
							for (Iterator<NeighbourEdge<Integer> > neighbourIterator3 = rule.neighbourIterator(edge.getIDV()) ; neighbourIterator3.hasNext() ; ) {
								edge3 = neighbourIterator3.next();
								if (rule.isHead(rule.getVertex(edge3.getIDV()))) {
									q = rule.getPredicate(edge3.getIDV());
									positionQ = edge3.getValue();
									try { _graphPosDep.addEdge(p,positionP,q,positionQ); }
									catch (IllegalEdgeException e) { /* the edge already exists : nothing to be done */ }
								}
							}
							for (Iterator<NeighbourEdge<Integer> > neighbourIterator3 = rule.neighbourIterator(headVertex.getID()) ; neighbourIterator3.hasNext() ;) {	
								edge3 = neighbourIterator3.next();
								if (rule.isExistential(rule.getVertex(edge3.getIDV()))) {
									q = (Predicate)(headVertex.getValue());
									positionQ = edge3.getValue();
									sj = _graphPosDep.getVertex(q,positionQ);
									if (_graphPosDep.isEdge(ri.getID(),sj.getID()))
										_graphPosDep.removeEdge(ri.getID(),sj.getID());
									try { _graphPosDep.addEdge(p,positionP,q,positionQ,true); }
									catch (IllegalEdgeException e) { /* can never happen */ }
								}
							}
						}
					}
				}
			}
		}
	}

	/** The graph of position dependencies generated from the graph of rule dependencies. */
	private GraphPositionDependencies _graphPosDep; 

};

