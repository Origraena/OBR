package obr;

import moca.graphs.vertices.Vertex;

import java.util.ArrayList;

public interface DecidableClassCheck {

	/**
	 * Checks a graph of rule dependencies.
	 * @return the decidable class label if the grd belongs to this decidable class, null otherwise
	 */
	DecidableClassLabel grdCheck(GraphRuleDependencies grd);

	/**
	 * Checks only a strongly connected component of the graph of rule dependencies.
	 * @param grd The graph of rule dependencies.
	 * @param sccID The id of the strongly connected component to be checked.
	 * @return the decidable class label if the strongly connected component belongs to this decidable class, null otherwise
	 */
	DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID);

};

