package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Checks if all rules in the GRD or in one of its strongly connected components satisfy the atomic-hypothesis property.
 * I.e., if all rules contain only one atom in their body.
 */
public class AtomicHypothesisCheck implements DecidableClassCheck {

	/** 
	 * Atomic-hypothesis property label. 
	 */
	public static final DecidableClassLabel LABEL = new DecidableClassLabel("atomic-hypothesis",true,false,true);

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
	 * @return The decidable class label if the strongly connected component belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID) {
		if (check(grd.getComponent(sccID)))
			return LABEL;
		return null;
	}
	
	/**
	 * Internal check.
	 * @return True if the set of rules satisfies the atomic-hypothesis property, false otherwise.
	 */
	protected boolean check(Iterable<Vertex<AtomicRule> > rules) {
		for (Vertex<AtomicRule> vrule : rules) {
			if (vrule.getValue().getNbAtoms() > 2)	// one for the head, the other for the body
				return false;
		}
		return true;
	}

};

