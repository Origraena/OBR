package obr;

import moca.graphs.BipartedGraph;
import moca.graphs.vertices.Vertex;
import moca.graphs.vertices.VertexArrayList;
import moca.graphs.edges.Edge;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.UndirectedNeighboursLists;
import moca.graphs.edges.IllegalEdgeException;

import java.lang.String;
import java.lang.Integer;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class GraphAtomConjonction implements AtomConjonction {

	/* Constants */
	public static final String ATOM_SEPARATOR	=	new String(";");
	public static final String TERM_SEPARATOR	=	Atom.TERM_SEPARATOR;	
	public static final String BEGIN_TERM_LIST	=	Atom.BEGIN_TERM_LIST;
	public static final char END_TERM_LIST		=	Atom.END_TERM_LIST.charAt(Atom.END_TERM_LIST.length()-1);

	public GraphAtomConjonction() {
		try {
			_graph = new BipartedGraph<Object,Integer>(
				new VertexArrayList<Object>(),
				new VertexArrayList<Object>(), 
				new UndirectedNeighboursLists<Integer>());
		}
		catch (Exception e) { }
	}

	/** @see .fromString(String) */
	public GraphAtomConjonction(String stringRepresentation) {
		try {
			_graph = new BipartedGraph<Object,Integer>(
				new VertexArrayList<Object>(),
				new VertexArrayList<Object>(), 
				new UndirectedNeighboursLists<Integer>());
			fromString(stringRepresentation);
		}
		catch (Exception e) { }
	}

	public GraphAtomConjonction(GraphAtomConjonction toCopy) {
		try {
			_graph = new BipartedGraph<Object,Integer>(toCopy._graph);
		}
		catch (Exception e) { }
		
	}

	public int getNbAtoms() {
		return _graph.getNbVerticesInFirstSet();
	}

	public int getNbTerms() {
		return _graph.getNbVerticesInSecondSet();
	}

	public Atom getAtom(int i) throws NoSuchElementException {
		if ((i >= getNbAtoms()) || (i < 0))
			throw new NoSuchElementException();
		Atom atom = new Atom((Predicate)(_graph.getVertex(i).getValue()));
		for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(i) ; iterator.hasNext() ; ) {
			NeighbourEdge<Integer> edge = iterator.next();
			atom.set(edge.getValue(),(Term)(_graph.getVertex(edge.getIDV()).getValue()));
		}
		return atom;
	}

	public void fromString(String str) throws UnrecognizedStringFormatException {
		String[] sub1 = null;
		String[] sub2 = null;
		String[] sub3 = null;
		sub1 = str.split(ATOM_SEPARATOR);
		Predicate predicate = null;
		int arity = 0;
		int atomID = -1;
		int termID = -1;
//		int termID;
		for (int i = 0 ; i < sub1.length ; i++) {
			sub2 = sub1[i].split(BEGIN_TERM_LIST);
			switch (sub2.length) {
				case 1 :	// if there is no begin term list char we suppose the predicate arity to be null
					arity = 0;
					break;
				case 2 :
					if (sub2[1].charAt(sub2[1].length()-1) != END_TERM_LIST)
						throw new UnrecognizedStringFormatException();
					sub2[1] = sub2[1].substring(0,sub2[1].length()-1);	// the last char is to be removed
					sub3 = sub2[1].split(TERM_SEPARATOR);
					arity = sub3.length;
					break;
				default :
					throw new UnrecognizedStringFormatException();
			}
			atomID = addAtom(new Predicate(sub2[0],arity));
			for (int j = 0 ; j < arity ; j++) {
				termID = addTerm(sub3[j]);
				try {
					_graph.addEdge(atomID,termID,new Integer(j));
				}
				catch (Exception e) {
					throw new UnrecognizedStringFormatException();
				}
			}
		}
	}

	public String toString() {
		return toString(getNbAtoms());
	}

	public String toString(int nbAtoms) {
		StringBuilder string = new StringBuilder();
		if (nbAtoms > getNbAtoms())
			nbAtoms = getNbAtoms();
		for (int i = 0 ; i < nbAtoms -1; i++) {
			string.append(getAtom(i));
			string.append(ATOM_SEPARATOR);
		}
		string.append(getAtom(nbAtoms-1));
		return string.toString();
	
	}

	public int addAtom(Predicate predicate) {
		_graph.addInFirstSet(predicate);
		return _graph.getNbVerticesInFirstSet() - 1;
	}

	public int addTerm(Term term) {
		for (int i = getNbAtoms() ; i < _graph.getNbVertices() ; i++) {
			if (((Term)(getVertex(i).getValue())).getLabel().compareTo(term.getLabel()) == 0) {
				return i;
			}
		}
		_graph.addInSecondSet(term);
		return _graph.getNbVertices() - 1;
	}

	public int addTerm(String termLabel) {
		for (int i = getNbAtoms() ; i < _graph.getNbVertices() ; i++) {
			if (((Term)(getVertex(i).getValue())).getLabel().compareTo(termLabel) == 0) {
				return i;
			}
		}
		_graph.addInSecondSet(new Term(termLabel));
		return _graph.getNbVertices() - 1;
	}


	/** global id */
	public Object get(int i) throws NoSuchElementException {
		return _graph.get(i);
	}

	/** global id */
	public Vertex<Object> getVertex(int i) throws NoSuchElementException {
		return _graph.getVertex(i);
	}

	/** local id */
	public Vertex<Object> getVertexTerm(int i) throws NoSuchElementException {
		return _graph.getInSecondSet(i);
	}
	
	/** local id */
	public Vertex<Object> getVertexAtom(int i) throws NoSuchElementException {
		return _graph.getInFirstSet(i);
	}
	
	/** local id */
	public Term getTerm(int i) throws NoSuchElementException {
		return (Term)getVertexTerm(i).getValue();
	}

	/** local id */
	public Term getTermFromAtom(int atomID, int termIndex) throws NoSuchElementException {
		return (Term)_graph.getNeighbourValue(atomID,termIndex);
	}

	public Vertex<Object> getVertexTermFromAtom(int atomID, int termIndex) throws NoSuchElementException {
		return _graph.getNeighbour(atomID,termIndex);
	}


	public GraphAtomConjonction cloneIn(GraphAtomConjonction copy) {
		for (Iterator<Object> iterator = _graph.firstIterator() ; iterator.hasNext() ; ) {
			copy.addAtom(((Predicate)(iterator.next())).clone());
		}
		for (Iterator<Object> iterator = _graph.secondIterator() ; iterator.hasNext() ; ) {
			copy.addTerm(((Term)(iterator.next())).clone());
		}
		Edge<Integer> edge = null;
		for (Iterator<Edge<Integer> > iterator = _graph.edgeIterator() ; iterator.hasNext() ; ) {
			edge = iterator.next();
			try {copy._graph.addEdge(edge.getIDU(),edge.getIDV(),new Integer(edge.getValue()));}
			catch (IllegalEdgeException e) { /*no such exception can be thrown*/ }
		}
		return copy;
	}

	public GraphAtomConjonction clone() {
		GraphAtomConjonction copy = new GraphAtomConjonction();
		return cloneIn(copy);
	}

	protected BipartedGraph<Object,Integer> _graph = null;

};

