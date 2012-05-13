package obr;

import java.util.ArrayList;
import java.util.Iterator;

import moca.graphs.vertices.Vertex;

public class GuardedCheck implements DecidableClassCheck{

	public static final DecidableClassLabel LABEL = new DecidableClassLabel("guarded",false,false,true);
	
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
	 * @param scc The strongly connected component to be checked.
	 * @return the decidable class label if the strongly connected component belongs to this decidable class, null otherwise.
	 */
	public DecidableClassLabel sccCheck(GraphRuleDependencies grd, int sccID) {
		if (check(grd.getComponent(sccID)))
			return LABEL;
		return null;
	}

	protected boolean check(Iterable<Vertex<AtomicRule> > rules) {
		//System.out.println("-------------GUARDED?---------------");
		AtomicRule rule;
		int i,nbAtoms,nbVars;
		boolean guarded;
		for (Vertex<AtomicRule> vrule : rules) {
			rule = vrule.getValue();
			nbAtoms = rule.getNbAtoms();
			nbVars = rule.getNbUniversalVariables();
			guarded = false;
			//System.out.println("rule = "+rule+"\nnbAtoms = "+nbAtoms+"\nnbVars = "+nbVars);
			for (i = 0 ; (i < nbAtoms) && !guarded ; i++) {
				//System.out.println("i = "+i+"\nisHead()? "+rule.isHead(i)+"\nvars = "+rule.variables(i).size());
				if ((!rule.isHead(i))
				&&  (nbVars == rule.variables(i).size()))
					guarded = true;
			}
			if (!guarded)
				return false;
		}
		return true;
	}

}
