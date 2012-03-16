package obr;

import java.util.ArrayList;

/**
 * Analyse a graph of rule dependencies to determine if it belongs to decidable class.
 * Furthermore, the GRDAnalyser will check the strongly connected components of the GRD.
 */
public class GRDAnalyser {

	/**
	 * Constructor.
	 * @param grd The graph of rule dependencies to be analysed.
	 */
	public GRDAnalyser(GraphRuleDependencies grd) {
		_grd = grd;
		_checkFunctions = new ArrayList<DecidableClassCheck>();
		_grdLabels = new ArrayList<DecidableClassLabel>();
		_sccLabels = new ArrayList<ArrayList<DecidableClassLabel> >(grd.getNbComponents());
		for (int i = 0 ; i < grd.getNbComponents() ; i++)
			_sccLabels.add(new ArrayList<DecidableClassLabel>());
	}

	/**
	 * Adds a new decidable class check function into the list.
	 * If the function already exists, it will not be added.
	 * @param checkFunction The new function to add.
	 */
	public void addDecidableClassCheck(DecidableClassCheck checkFunction) {
		if (!_checkFunctions.contains(checkFunction))
			_checkFunctions.add(checkFunction);
	}

	/**
	 * <p>Process all checks in a specific order :
	 * <ul>
	 * <li>check all functions onto the complete graph of rule dependencies ;</li>
	 * <li>check if the graph is cyclic ;</li>
	 * <li>if is is, check all functions onto each strongly connected component of the GRD.</li>
	 * </p>
	 */
	public void process() {
		DecidableClassLabel l = null;
		for (DecidableClassCheck function : _checkFunctions) {
			try {
				l = function.grdCheck(_grd);
				if (l != null)
					_grdLabels.add(l);
			}
			catch (UnsupportedOperationException e) { /* this check function cannot be used against the full grd */ }
		}
		if (_grd.isCyclic()) {
			for (int i = 0 ; i < _grd.getNbComponents() ; i++) {
				for (DecidableClassCheck function : _checkFunctions) {
					try {
						l = function.sccCheck(_grd,_grd.getComponent(i));
						if (l != null)
							_sccLabels.get(i).add(l);
					}
					catch (UnsupportedOperationException e) { /* ... against a single scc */ }
				}
			}
		}
	}

	/** The graph of rule dependencies to be analysed. */
	private GraphRuleDependencies _grd;
	/** The check functions to be used. */
	private ArrayList<DecidableClassCheck> _checkFunctions;
	/** Contains all determined decidable class labels for the GRD. */
	private ArrayList<DecidableClassLabel> _grdLabels;
	/** Contains all determined decidable class labels for each strongly connected component of the GRD. */
	private ArrayList<ArrayList<DecidableClassLabel> > _sccLabels;


};

