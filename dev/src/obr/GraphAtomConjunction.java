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


public class GraphAtomConjunction implements AtomConjunction {

	/* Constants */
	public static final String ATOM_SEPARATOR	=	new String(";");
	public static final String TERM_SEPARATOR	=	Atom.TERM_SEPARATOR;	
	public static final String BEGIN_TERM_LIST	=	Atom.BEGIN_TERM_LIST;
	public static final char END_TERM_LIST		=	Atom.END_TERM_LIST.charAt(Atom.END_TERM_LIST.length()-1);

	public GraphAtomConjunction() {
		try {
			_graph = new BipartedGraph<Object,Integer>(
				new VertexArrayList<Object>(),
				new VertexArrayList<Object>(), 
				new UndirectedNeighboursLists<Integer>());
		}
		catch (Exception e) { }
	}

	/** @see .fromString(String) */
	public GraphAtomConjunction(String stringRepresentation) {
		try {
			_graph = new BipartedGraph<Object,Integer>(
				new VertexArrayList<Object>(),
				new VertexArrayList<Object>(), 
				new UndirectedNeighboursLists<Integer>());
			fromString(stringRepresentation);
		}
		catch (Exception e) { }
	}

	public GraphAtomConjunction(GraphAtomConjunction toCopy) {
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

	public boolean isAtom(Vertex<Object> v) {
		if (v == null)
			return false;
		if (v.getID() < getNbAtoms())
			return true;
		return false;
	}

	public boolean isTerm(Vertex<Object> v) {
		if (v == null)
			return false;
		if (v.getID() < getNbAtoms())
			return false;
		return true;
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
	
	public boolean addEdge(int atomID, int termID, Integer position) {
		try {
			_graph.addEdge(atomID,termID,position);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	/**
	 * Add an atom from another graph atom conjunction.
	 * Unsecure :
	 * 	no check against the vertex to be sure it is an atom Vertex
	 */
	public void addAtom(Vertex<Object> atom, GraphAtomConjunction source) {
		Iterator<NeighbourEdge<Integer> > termIterator = source.neighbourIterator(atom.getID());
		NeighbourEdge<Integer> edge = null;
		int termID = -1;
		addAtom((Predicate)(atom.getValue()));
		while (termIterator.hasNext()) {
			edge = termIterator.next();
			termID = addTerm(source.getTerm(edge.getIDV()-source.getNbAtoms()));
			addEdge(getNbAtoms()-1,termID,new Integer(edge.getValue()));
		}
	}

	/** global id */
	public Object get(int i) throws NoSuchElementException {
		return _graph.get(i);
	}

	public Predicate getPredicate(int i) throws NoSuchElementException {
		return (Predicate)(getVertexAtom(i).getValue());
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

	public GraphAtomConjunction subAtomConjunction(int beginAtomID, int endAtomID) {
		if ((beginAtomID > endAtomID) || (endAtomID > getNbAtoms()))
			return null;
		GraphAtomConjunction result = new GraphAtomConjunction();
		NeighbourEdge<Integer> edge = null;
		int termID = -1;
		for (int i = beginAtomID ; i < endAtomID ; i++) {
			result._graph.addInFirstSet(getPredicate(i).clone());
		}
		for (int i = beginAtomID ; i < endAtomID ; i++) {
			for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(i) ; iterator.hasNext() ; ) {
				edge = iterator.next();
				termID = result.addTerm(getTerm(edge.getIDV()-getNbAtoms()).clone());
				try { result._graph.addEdge(i-beginAtomID,termID,new Integer(edge.getValue())); }
				catch (IllegalEdgeException e) { }
			}
		}
		return result;
	}

	public GraphAtomConjunction cloneIn(GraphAtomConjunction copy) {
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

	public GraphAtomConjunction clone() {
		GraphAtomConjunction copy = new GraphAtomConjunction();
		return cloneIn(copy);
	}

	public Iterator<Vertex<Object> > vertexAtomIterator() {
		return _graph.firstVertexIterator();
	}

	public Iterator<Vertex<Object> > vertexTermIterator() {
		return _graph.secondVertexIterator();
	}

	public NeighbourIterator vertexTermIteratorFromAtom(int atomID) {
		return new NeighbourIterator(this,_graph.neighbourIterator(atomID));
	}

	public NeighbourIterator vertexAtomIteratorFromTerm(int termID) {
		return new NeighbourIterator(this,_graph.neighbourIterator(termID));
	}

	public Iterator<NeighbourEdge<Integer> > neighbourIterator(int vertexID) {
		return _graph.neighbourIterator(vertexID);
	}

	protected BipartedGraph<Object,Integer> _graph = null;


	public class NeighbourIterator implements Iterator<Vertex<Object> > {
		public NeighbourIterator(GraphAtomConjunction source, Iterator<NeighbourEdge<Integer> > iterator) {
			_source = source;
			_iterator = iterator;
		}
		public boolean hasNext() {
			return _iterator.hasNext();
		}
		public Vertex<Object> next() throws NoSuchElementException {
			return _source.getVertex(_iterator.next().getIDV());
		}
		public void remove() throws UnsupportedOperationException { throw new UnsupportedOperationException(); }

		private GraphAtomConjunction _source;
		private Iterator<NeighbourEdge<Integer> > _iterator;
	};


};

