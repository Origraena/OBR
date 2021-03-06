package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Checks if a set of rules satisfies the frontier-1 property.<br />
 * I.e., if all rules have a frontier of size 1.
 */
public class FrontierOneCheck implements DecidableClassCheck {

	/** Associated label. */
	public static final DecidableClassLabel LABEL = new DecidableClassLabel("frontier-1",false,false,true);

	/**
	 * Checks a graph of rule dependencies.
	 * @return the decidable class label if the grd belongs to this decidable class, null otherwise.
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
	 * @return the decidable class label if the strongly connected component belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID) {
		if (check(grd.getComponent(sccID)))
			return LABEL;
		return null;
	}

	protected boolean check(Iterable<Vertex<AtomicRule> > rules) {
		for (Vertex<AtomicRule> vrule : rules) {
			if (vrule.getValue().frontier().size() != 1){
				return false;
			}
		}
		return true;
	}

};

