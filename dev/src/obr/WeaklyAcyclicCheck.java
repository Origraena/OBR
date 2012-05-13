package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Checks if a set of rules satisfies the weakly-acyclic property.
 * @see GraphPositionDependencies
 */
public class WeaklyAcyclicCheck implements DecidableClassCheck {

	/** Associated label. */
	public static final DecidableClassLabel LABEL = new DecidableClassLabel("weakly-acyclic",false,true,false);

	/**
	 * Checks a graph of rule dependencies.
	 * @param grd The graph of rule dependencies.
	 * @return The decidable class label if the grd belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel grdCheck(GraphRuleDependencies grd) {
		try { _graphPosDep = new GraphPositionDependencies(grd.getVertexCollection()); }
		catch (IllegalConstructionException e) { return null; }
		if (_graphPosDep.finiteRank())
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
		try { _graphPosDep = new GraphPositionDependencies(grd.getComponent(sccID)); }
		catch (IllegalConstructionException e) { return null; }
		if (_graphPosDep.finiteRank())
			return LABEL;
		return null;
	}

	/** The graph of position dependencies generated from the set of rules. */
	private GraphPositionDependencies _graphPosDep; 

};

