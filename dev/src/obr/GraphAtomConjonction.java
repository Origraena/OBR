package obr;

import moca.graphs.BipartedGraph;
import moca.graphs.vertices.Vertex;
import moca.graphs.vertices.VertexArrayList;
import moca.graphs.edges.UndirectedNeighboursLists;

public class GraphAtomConjonction implements AtomConjonction {

	public GraphAtomConjonction() {

	}

	public Atom getAtom(int i) throws NoSuchElementException {
		Atom atom = new Atom((Predicate)(getVertex(i).getValue()));
		for (Iterator<NeighbourEdge<Integer> > iterator = neighbourIterator() ; iterator.hasNext() ; ) {
			NeighbourEdge<Integer> edge = iterator.next();
			atom.set(edge.getValue(),(Term)(getVertex(edge.getIDV()).getValue()));
		}
		return atom;
	}

	public void fromString(String str) throws UnrecognizedStringFormatException {
		String[] sub1;
		String[] sub2;
		sub1 = str.split(";");
//		int termID;
//		for (int i = 0 ; i < sub1.length ; i++)
//			if 
	}

	public int addVertex(Vertex<Term> vertex) {
		for (int i = getNbAtoms() ; i < _graph.getNbVertices() ; i++) {
			if ((Term)(getVertex(i).getValue()) == vertex.getValue())
				return i;
		}
		_graph.addVertex(vertex);
		return _graph.getNbVertices() - 1;
	}


	/** global id */
	public Vertex<Object> getVertex(int i) throws NoSuchElementException {
		return _graph.getVertex(i);
	}

	/** local id */
	public Vertex<Term> getVertexTerm(int i) throws NoSuchElementException {
		return (Vertex<Term>)_graph.getInFirstSet(i);
	}
	
	/** local id */
	public Vertex<Predicate> getVertexAtom(int i) throws NoSuchElementException {
		return (Vertex<Predicate>)_graph.getInSecondSet(i);
	}
	
	/** local id */
	public Term getTerm(int i) throws NoSuchElementException {
		return getVertexTerm(i).getValue();
	}

	private BipartedGraph<Object,Integer> _graph = new BipartedGraph<Object,Integer>(new VertexArrayList<Predicate>(),new VertexArrayList<Term>(), new UndirectedNeighboursLists<Integer>());

};

