package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.vertices.Vertex;
import java.util.Scanner;


import java.util.Iterator;

/**
 * Unsecure :
 * 	if a vertex already in the graph is added once again, it may produce strange behaviour
 * 	if an edge is added manually it may produce errors
 */
public class GraphRuleDependencies extends DirectedSimpleGraph<AtomicRule,Boolean> {

	public GraphRuleDependencies(String filePath) {
		fromFile(filePath);
		System.out.println(this);
	}

	public GraphRuleDependencies(GraphRuleDependencies g) {
		super(g);
	}

	/**
	 * Whenever a vertex is added, the rule is checked for unification against all others.
	 * This provides an always updated graph.
	 */
	public void addVertex(Vertex<AtomicRule> rule) {
		super.addVertex(rule);
		Vertex<AtomicRule> r = null;
		for (Iterator<Vertex<AtomicRule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			r = ruleIterator.next();
			try {
				if (rule.getValue().mayImply(r.getValue()))
					addEdge(rule.getID(),r.getID(),true);
			}
			catch (IllegalEdgeException e) {
				// the edge already exists
			}
			try {
				if ((rule != r) && (r.getValue().mayImply(rule.getValue())))
					addEdge(r.getID(),rule.getID(),true);
			}
			catch (IllegalEdgeException e) {
				// the edge already exists
			}
		}
	}

	private DirectedSimpleGraph<StronglyConnectedComponent,Boolean> _stronglyConnectedComponents;

	// TODO
	// toString() : String (pseudo visu) 
	// toFile(String filename) : boolean (tostring dans le fichier)
	// methode affichage composantes fortement connexes qui retourne une String

	public void fromFile(String filePath) throws FileNotFoundException{

		try {
			Scanner scanner=new Scanner(new File(filePath));
			while(scanner.hasNext())
			      addVertex(new Vertex<AtomicRule>(new AtomicRule(scanner.nextLine())));
			} catch (FileNotFoundException e) {
					      e.printStackTrace();
			}
		scanner.close();
	}
	
	public String toString(){
		String result = new String("");
		Vertex<AtomicRule> r = null;
		for (Iterator<Vertex<AtomicRule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			r = ruleIterator.next();
			result += r;
			}
			
		return result;
	
	}

};


