package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class WeaklyStickyCheck extends StickyCheck implements DecidableClassCheck {

	public static final DecidableClassLabel LABEL = new DecidableClassLabel("weakly-sticky");

	/**
	 * Checks a graph of rule dependencies.
	 * @return the decidable class label if the grd belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel grdCheck(GraphRuleDependencies grd) {
		try { _graphPosDep = new GraphPositionDependencies(grd.getVertexCollection()); }
		catch (IllegalConstructionException e) { }
		if (check(grd.getVertexCollection()))
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
		try { _graphPosDep = new GraphPositionDependencies(grd.getComponent(sccID)); }
		catch (IllegalConstructionException e) { }
		if (check(grd.getComponent(sccID)))
			return LABEL;
		return null;
	}

	protected void mark(Vertex<Object> term, AtomicRule rule) throws MarkFailureException {
		NeighbourEdge<Integer> edge = null;
		int cpt = 0;
		for (Iterator<NeighbourEdge<Integer> > iterator = rule.neighbourIterator(term.getID()) ; iterator.hasNext() ; ) {
			edge = iterator.next();
			if (rule.isBody(edge.getIDV())) {
				cpt++;
				if ((cpt >= 2) && (!_graphPosDep.finiteRank(rule.getPredicate(edge.getIDV()),edge.getValue())))
					throw new MarkFailureException();
				if (!_positions.contains(edge.getValue()))
					_positions.add(edge.getValue());
			}
		}
	}

	private GraphPositionDependencies _graphPosDep;

};

