package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Checks if a set of rules satisfies the range-restricted property.<br />
 * I.e., if there is no existential variables.
 */
public class RangeRestrictedCheck implements DecidableClassCheck {

	/** Associated label. */
	public static final DecidableClassLabel LABEL = new DecidableClassLabel("range-restricted",true,false,true);

	/**
	 * Checks a graph of rule dependencies.
	 * @return The decidable class label if the grd belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel grdCheck(GraphRuleDependencies grd) {
		if (check(grd.getVertexCollection()))
			return LABEL;
		return null;
	}

	/**
	 * Checks only a strongly connected component of the graph of rule dependencies.
	 * @param grd The graph of rule dependencies.
	 * @param sccID The id of the strongly connected component to be checked.
	 * @return The decidable class label if the strongly connected component belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID) {
		if (check(grd.getComponent(sccID)))
			return LABEL;
		return null;
	}

	/**
	 * Internal check.
	 * @param rules The set of rule to check.
	 * @return True if there is no existential variables, false otherwise.
	 */
	protected boolean check(Iterable<Vertex<AtomicRule> > rules) {
		for (Vertex<AtomicRule> vrule : rules) {
			for (Iterator<Vertex<Object> > termIterator = vrule.getValue().vertexTermIterator() ; termIterator.hasNext() ;) {
				if (vrule.getValue().isExistential(termIterator.next()))
					return false;
			}
		}
		return true;
	}

};

