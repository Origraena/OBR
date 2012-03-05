package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.vertices.Vertex;

import java.util.Iterator;

/**
 * Unsecure :
 * 	if a vertex already in the graph is added once again, it may produce strange behaviour
 */
public class GraphRuleDependencies extends DirectedSimpleGraph<Rule,Boolean> {

	public GraphRuleDependencies() {
		super();
	}

	public GraphRuleDependencies(GraphRuleDependencies g) {
		super(g);
	}

	/**
	 * Whenever a vertex is added, the rule is checked for unification against all others.
	 * This provides an always updated graph.
	 */
	public void addVertex(Vertex<Rule> rule) {
		super.addVertex(rule);
		Vertex<Rule> r = null;
		for (Iterator<Vertex<Rule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			r = ruleIterator.next();
			if (rule.getValue().mayImply(r.getValue()))
				addEdge(rule.getID(),r.getID(),true);
			if ((rule != r) && (r.getValue().mayImply(rule.getValue())))
				addEdge(r.getID(),rule.getID(),true);

		}
	}

};


