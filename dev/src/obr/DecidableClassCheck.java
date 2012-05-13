package obr;

/**
 * Interface for functions checking if a graph of rule dependencies or a subset of this graph belongs to a concrete 
 * decidable class. <br />
 * It provides two methods, the first one is used to check the full set of rules, and the second one to check only
 * a strongly connected component of this set.
 * The classes which implement this interface may be used as a check function in the GRD Analyser.
 * @see GRDAnalyser
 */
public interface DecidableClassCheck {

	/**
	 * Checks a graph of rule dependencies.
	 * @return The associated decidable class label if the grd belongs to this decidable class, 
	 * null otherwise.
	 */
	DecidableClassLabel grdCheck(GraphRuleDependencies grd);

	/**
	 * Checks only a strongly connected component of the graph of rule dependencies.
	 * @param grd The graph of rule dependencies.
	 * @param sccID The id of the strongly connected component to be checked.
	 * @return The associated decidable class label if the strongly connected component belongs to this decidable class,
	 * null otherwise.
	 */
	DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID);

};

