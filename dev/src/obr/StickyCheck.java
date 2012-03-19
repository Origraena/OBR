package obr;

import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.IllegalEdgeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class StickyCheck implements DecidableClassCheck {

	public static final DecidableClassLabel LABEL = new DecidableClassLabel("sticky",true,false,false);

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
		_positions = new ArrayList<Integer>();
		Vertex<Object> vertexTerm = null;
		NeighbourEdge<Integer> edge = null;
		AtomicRule rule = null;

		// first walk
		for (Vertex<AtomicRule> vrule : rules) {
			rule = vrule.getValue();
			for (Iterator<Vertex<Object> > termIterator = rule.vertexTermIterator() ; termIterator.hasNext() ; ) {
				vertexTerm = termIterator.next();
				if ((((Term)vertexTerm.getValue()).isVariable()) && (!rule.isHead(vertexTerm))) {
					try { mark(vertexTerm,rule); }
					catch (MarkFailureException e) { return false; }
				}
			}
		}

		// second walk
		for (int i = 0 ; i < _positions.size() ; i++) {
			for (Vertex<AtomicRule> vrule : rules) {
				rule = vrule.getValue();
				try {
					vertexTerm = rule.getVertexTermFromAtom(rule.getHead().getID(),_positions.get(i));
					try { mark(vertexTerm,rule); }
					catch (MarkFailureException e) { return false; }
				}
				catch (NoSuchElementException e) { /* nothing to be done */ }
			}
		}

		return true;
	}

	protected void mark(Vertex<Object> term, AtomicRule rule) throws MarkFailureException {
		NeighbourEdge<Integer> edge = null;
		int cpt = 0;
		for (Iterator<NeighbourEdge<Integer> > iterator = rule.neighbourIterator(term.getID()) ; iterator.hasNext() ; ) {
			edge = iterator.next();
			if (rule.isBody(edge.getIDV())) {
				cpt++;
				if (cpt >= 2)
					throw new MarkFailureException();
				if (!_positions.contains(edge.getValue()))
					_positions.add(edge.getValue());
			}
		}
	}

	protected ArrayList<Integer> _positions;

};

