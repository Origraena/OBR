package obr;

import moca.graphs.vertices.Vertex;
import moca.graphs.vertices.VertexIdentityFunction;

public class RuleIdentityFunction extends VertexIdentityFunction<AtomicRule> {
	public String exec(Vertex<AtomicRule> v) {
		return "R"+v.getID();
	}
	public static RuleIdentityFunction instance() {
		if (_instance == null)
			_instance = new RuleIdentityFunction();
		return _instance;
	}
	private static RuleIdentityFunction _instance = null;
	protected RuleIdentityFunction() { }
};

