package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.IllegalEdgeException;
import moca.graphs.edges.Edge;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.visu.AcyclicGraphLocalizer;
import moca.graphs.visu.PolygonLocalizer;
import moca.graphs.visu.PostScriptConverter;
import moca.graphs.visu.SCCIdentityFunction;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.File;
//import java.io.OutputStreamWriter;
//import java.io.BufferedOutputStream;
//import java.io.FileOutputStream;


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
			stringBuilder.append(":\t");
			stringBuilder.append(rule);
			stringBuilder.append('\n');
			i++;
		}
		stringBuilder.append('\n');
		Vertex<AtomicRule> rule = null;
		boolean newelement = false;			// TODO
		for (Iterator<Vertex<AtomicRule> > ruleIterator = vertexIterator() ; ruleIterator.hasNext() ; ) {
			newelement = false;
			rule = ruleIterator.next();
				for (Iterator<NeighbourEdge<Boolean> > neighbour = neighbourIterator(rule.getID()) ; neighbour.hasNext() ; ) {
				if (!newelement) {
					stringBuilder.append("R");
					stringBuilder.append(rule.getID());
					stringBuilder.append("\t-->\t");
				}	
				stringBuilder.append("R");
				stringBuilder.append(neighbour.next().getIDV());
				stringBuilder.append(',');
				newelement = true;
			}
			if (newelement == true)
				stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}

	public String stronglyConnectedComponentsToString() {
		DirectedSimpleGraph<ArrayList<Vertex<AtomicRule> >, Boolean> sccg = getStronglyConnectedComponentsGraph();
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0 ; i < sccg.getNbVertices() ; i++) {
			stringBuilder.append('C');
			stringBuilder.append(i);
			stringBuilder.append(":\t");
			for (int j = 0 ; j < sccg.getVertex(i).getValue().size() ; j++) {
				stringBuilder.append(sccg.getVertex(i).getValue().get(j).getID());
				if (j != sccg.getVertex(i).getValue().size()-1)
					stringBuilder.append(',');
			}
			stringBuilder.append('\n');
		}
		boolean newelement = false;
		for (int i = 0 ; i < sccg.getNbVertices() ; i++) {
			newelement = false;
			for (Iterator<NeighbourEdge<Boolean> > neighbour = sccg.neighbourIterator(i) ; neighbour.hasNext() ; ) {
				if (!newelement) {
					stringBuilder.append("C");
					stringBuilder.append(i);
					stringBuilder.append("\t-->\t");
				}
				stringBuilder.append("C");
				stringBuilder.append(neighbour.next().getIDV());
				stringBuilder.append(',');
				newelement = true;
			}
			if (newelement)
				stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}

	public void sccToPostScript(String filePath) {
		PostScriptConverter psOutput = new PostScriptConverter(
			getStronglyConnectedComponentsGraph(),
			480,640,
			new AcyclicGraphLocalizer(480,640),
			SCCIdentityFunction.instance(),
			"Strongly Connected Components");
		psOutput.writeToFile(filePath);
	}

	public void toPostScript(String filePath) {
		PostScriptConverter psOutput = new PostScriptConverter(
			this,
			480,640,
			new PolygonLocalizer(480,640),
			RuleIdentityFunction.instance(),
			"Graph of Rule Dependencies");
		psOutput.writeToFile(filePath);
	}
};


