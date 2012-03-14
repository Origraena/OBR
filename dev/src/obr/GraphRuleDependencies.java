package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.IllegalEdgeException;
import moca.graphs.edges.NeighbourEdge;

import java.util.Scanner;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.File;


/**
 * Unsecure :
 * 	if a vertex already in the graph is added once again, it may produce strange behaviour
 * 	if an edge is added manually it may produce errors
 */
public class GraphRuleDependencies extends DirectedSimpleGraph<AtomicRule,Boolean> {

	public GraphRuleDependencies() throws IllegalConstructionException {
		super();
	}

	public GraphRuleDependencies(String filePath) throws IllegalConstructionException {
		super();
		fromFile(filePath);
	}

	public GraphRuleDependencies(GraphRuleDependencies g) throws IllegalConstructionException {
		super(g);
	}

	/**
	 * Whenever a vertex is added, the rule is checked for unification against all others.
	 * This provides an always updated graph.
	 */
	public void addVertex(AtomicRule rule) {
		super.addVertex(rule);
		Vertex<AtomicRule> r = getVertex(getNbVertices()-1);
		Vertex<AtomicRule> r2 = null;
		for (Iterator<Vertex<AtomicRule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			r2 = ruleIterator.next();
			try {
				if (r.getValue().mayImply(r2.getValue()))
					addEdge(r.getID(),r2.getID(),true);
			}
			catch (IllegalEdgeException e) {
				// the edge already exists
			}
			try {
				if ((r2 != r) && (r2.getValue().mayImply(r.getValue())))
					addEdge(r2.getID(),r.getID(),true);
			}
			catch (IllegalEdgeException e) {
				// the edge already exists
			}
		}
	}

	// TODO
	// toString() : String (pseudo visu) 
	// toFile(String filename) : boolean (tostring dans le fichier)
	// methode affichage composantes fortement connexes qui retourne une String

	public void fromFile(String filePath) {
		try {
			String line = null;
			Scanner scan = new Scanner(new File(filePath));
			scan.useDelimiter(Pattern.compile("\n"));
			while (scan.hasNext())
				addVertex(new AtomicRule(scan.next()));
			scan.close();
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
	
	public String toString(){
		StringBuilder stringBuilder = new StringBuilder();
		int i = 0;
		for (AtomicRule rule : this) {
			stringBuilder.append("R");
			stringBuilder.append(i);
			stringBuilder.append("\t:");
			stringBuilder.append(rule);
			stringBuilder.append('\n');
			i++;
		}
		stringBuilder.append('\n');
		Vertex<AtomicRule> rule = null;
		for (Iterator<Vertex<AtomicRule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			rule = ruleIterator.next();
			for (Iterator<NeighbourEdge<Boolean> > neighbour = neighbourIterator(rule.getID()) ; neighbour.hasNext() ; ) {
				stringBuilder.append("R");
				stringBuilder.append(rule.getID());
				stringBuilder.append("\t-->\t");
				stringBuilder.append("R");
				stringBuilder.append(neighbour.next().getIDV());
				stringBuilder.append('\n');
			}
			stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}

};


