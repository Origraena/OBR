package obr;

import moca.graphs.BipartedGraph;
import moca.graphs.vertices.Vertex;
import moca.graphs.vertices.VertexArrayList;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.UndirectedNeighboursLists;
import moca.graphs.edges.IllegalEdgeException;
import moca.lists.Fifo;

import java.lang.String;
import java.lang.Integer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

/**
 * Represents an atomic rule.<br />
 * A rule is an atom conjunction which adds the notions of "body" and "head".<br />
 * An atomic rule contains only one atom in its head.
 * The structure of this class is the same as a classical atom conjunction but its head pointer.
 * @see AtomConjunction
 */
public class AtomicRule extends AtomConjunction {

	/** Represents the separator between the body and the head when the rule is under a String format. */
	public static final String HEAD_SEPARATOR	=	new String("-->");

	/**
	 * Default constructor.
	 * It creates an empty rule.
	 */
	public AtomicRule() {
		super();
	}

	/** 
	 * Constructor from a string representation.
	 * @param stringRepresentation The rule under a string format.
	 * @see #fromString(String) 
	 */
	public AtomicRule(String stringRepresentation) {
		super();
		try {
			fromString(stringRepresentation);
		}
		catch (UnrecognizedStringFormatException e) { }
	}

	/**
	 * Head vertex getter.
	 * @return The head vertex.
	 */
	public Vertex<Object> getHead() {
		return _head;
	}

	/**
	 * Converts the head into an atom instance.
	 * @see AtomConjunction#getAtom(int)
	 */
	public Atom getHeadAtom() {
		return getAtom(_head.getID());
	}

	/**
	 * Allows to know if a vertex is in the body.
	 * @param v The vertex to be checked.
	 * @return True if v belongs to the body, false otherwise.
	 * @throws NoSuchElementException If v is null or if its id does not match in the rule.
	 */
	public boolean isBody(Vertex<Object> v) throws NoSuchElementException {
		if (v == null)
			throw new NoSuchElementException();
		if (v == _head)
			return false;
		if (v.getID() < getNbAtoms())
			return true;
		for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(v.getID()) ; iterator.hasNext() ;)
			if (_graph.getVertex(iterator.next().getIDV()) != _head)
				return true;
		return false;
	}

	/**
	 * Convenient method for isBody(Vertex&lt;Object&gt;).
	 * @param vertexID The id of the vertex to be checked.
	 * @return True if the vertex belongs to the body, false otherwise.
	 * @throws NoSuchElementException If the id does not match in the rule.
	 * @see #isBody(Vertex)
	 */
	public boolean isBody(int vertexID) throws NoSuchElementException {
		return isBody(getVertex(vertexID));
	}

	/**
	 * Allows to know if a vertex is in the head.
	 * @param v The vertex to be checked.
	 * @return True if v belongs to the head, false otherwise.
	 * @throws NoSuchElementException If v is null or if its id does not match in the rule.
	 */
	public boolean isHead(Vertex<Object> v) throws NoSuchElementException {
		if (v == null)
			throw new NoSuchElementException();
		if (v == _head)
			return true;
		for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(_head.getID()) ; iterator.hasNext() ; )
			if (_graph.getVertex(iterator.next().getIDV()) == v)
				return true;
		return false;
	}

	/**
	 * Convenient method for isHead(Vertex&lt;Object&gt;).
	 * @param vertexID the id of the vertex to be checked.
	 * @return True if the vertex belongs to the head, false otherwise.
	 * @throws NoSuchElementException If the id does not match in the rule.
	 * @see #isHead(Vertex)
	 */
	public boolean isHead(int vertexID) throws NoSuchElementException {
		return isHead(getVertex(vertexID));
	}

	/**
	 * Creates a new array list containing all vertices which belong to the frontier of the rule.
	 * A term is said to be in the frontier iff it belongs to the body <b>and</b> to the head of the rule.
	 * @return An array list of Vertex<Object> which contains all terms in the frontier.
	 */
	public ArrayList<Vertex<Object> > frontier() {
		NeighbourEdge<Integer> edge = null;
		ArrayList<Vertex<Object> > result = new ArrayList<Vertex<Object> >();
		for (Iterator<NeighbourEdge<Integer> > iterator = neighbourIterator(_head.getID()) ; iterator.hasNext() ; ) {
			edge = iterator.next();
			if ((isBody(edge.getIDV())) && (!result.contains(getVertex(edge.getIDV()))))
				result.add(getVertex(edge.getIDV()));
		}
		return result;
	}

	/**
	 * Allows to know if a vertex belongs to the frontier of a rule.
	 * A term is said to be in the frontier iff it belongs to the body <b>and</b> to the head of the rule.
	 * @return True if the vertex is in the frontier, false otherwise.
	 * @throws NoSuchElementException If the vertex is null, or its id does not match to the rule, or it is an atom vertex.
	 */
	public boolean isFrontier(Vertex<Object> v) throws NoSuchElementException {
		if ((v == null) || /*(*/(v.getID() < getNbAtoms()))/* || (v.getID() > getNbVertices())) */
			throw new NoSuchElementException();
		return isBody(v) && isHead(v);
	}

	/**
	 * Checks if a vertex is an existential variable.
	 * An existential variable is a variable which appears only in the head of the rule.
	 * @param v The vertex to be checked.
	 * @return True if the vertex is an existential variable, false otherwise.
	 */
	public boolean isExistential(Vertex<Object> v) {
		if ((v == null) || (v.getID() < getNbAtoms()))
			return false;
		return !((((Term)(v.getValue())).isConstant()) || isBody(v));
	}

	/**
	 * Checks if a vertex is a universal variable.
	 * A universal variable is a variable which appears at least in the body of the rule.
	 * @param v The vertex to be checked.
	 * @return True if the vertex is a universal variable, false otherwise.
	 */
	public boolean isUniversal(Vertex<Object> v) {
		if ((v == null) || (v.getID() < getNbAtoms()))
			return false;
		return (!(((Term)(v.getValue())).isConstant()) && isBody(v));
	}

	/**
	 * Creates an array containing all positions of existential variables.<br />
	 * The length of the array matches the number of existential variables.
	 * This method is used by the unification process.
	 * @return An array of int containing the positions of all existential variables in the rule.
	 */
	public int[] existentialIndex() {
		int cpt = 0;
		int arity = ((Predicate)(_head.getValue())).getArity(); 
		for (int i = 0 ; i < arity ; i++) {
			if (isExistential(getVertexTermFromAtom(_head.getID(),i)))
				cpt++;
		}
		int result[] = new int[cpt];
		cpt = 0;
		for (int i = 0 ; i < arity ; i++) {
			if (isExistential(getVertexTermFromAtom(_head.getID(),i))) {
				result[cpt] = i;
				cpt++;
			}
		}
		return result;
	}

	/**
	 * Fullfills the rule from its string representation.
	 * @param str The string representation of the rule.
	 * @throws UnrecognizedStringFormatException If the string cannot be converted.
	 */
	@Override
	protected void fromString(String str) throws UnrecognizedStringFormatException {
		String[] sub1 = str.split(HEAD_SEPARATOR);
		if (sub1.length != 2)
			throw new UnrecognizedStringFormatException();
		sub1[0]+=ATOM_SEPARATOR;
		sub1[0]+=sub1[1];
		super.fromString(sub1[0]);
		_head = getVertex(getNbAtoms()-1);
	}

	/**
	 * Converts the rule into a String.
	 * @return The string representation of the rule.
	 */
	@Override
	public String toString() {
		return super.toString(getNbAtoms()-1)+HEAD_SEPARATOR+getHeadAtom();
	}

	/**
	 * Creates a new atom conjunction corresponding to the body of the rule.
	 * @return The atom conjunction of the body.
	 */
	public AtomConjunction getBody() {
		return subAtomConjunction(0, getNbAtoms()-1);
	}

	/**
	 * Clone the rule into a copy.
	 * @param copy The instance where to copied the rule.
	 * @return The copy of the rule.
	 */
	public AtomicRule cloneIn(AtomicRule copy) {
		super.cloneIn(copy);
		copy._head = copy.getVertex(_head.getID());
		return copy;
	}

	/**
	 * Clone the rule.
	 * @return A clone of the rule.
	 */
	@Override
	public AtomicRule clone() {
		AtomicRule copy = new AtomicRule();
		return cloneIn(copy);
	}

	/**
	 * Allows to know if the rule may imply the other one passed as parameter.
	 * I.e., if there exist a unificator between this rule head a subset of the other rule body.
	 * @param R The rule to be checked against.
	 * @return True if this rule may imply R, false otherwise.
	 * @see #existUnification(AtomConjunction,AtomicRule)
	 */
	public boolean mayImply(AtomicRule R) {
		return existUnification(R.getBody(), this);
	}
	
	/** Pointer to the head of the rule. */
	private Vertex<Object> _head = null;


	/* ALGORITHMS */

	/*
	 * Unification
	 */

	/**
	 * Static method used to know if there exists a unification between the atom conjunction and the rule
	 * passed as parameters.
	 * @param H1 The atom conjonction to be unified.
	 * @param R The rule whose head will be unified.
	 * @return True if success, false otherwise.
	 */
	public static boolean existUnification(AtomConjunction H1, AtomicRule R) {
			/** init */
		boolean isLocallyUnifiable[] = new boolean[H1.getNbAtoms()];	// valuing true while atom is supposed locally unifiable
		int E[] = R.existentialIndex();								// positions of existential variables in the rule
		Vertex<Object> head = null;
		Vertex<Object> current = null;
		AtomConjunction Q = null;

			/** preprocessing */
		for (int i = 0 ; i < H1.getNbAtoms() ; i++) {
			Q = H1.subAtomConjunction(i,i+1);
			if (localUnification(Q,R,E) == true)
				isLocallyUnifiable[i] = true;
			else
				isLocallyUnifiable[i] = false;
		}

			/** extension */
		for (int i = 0 ; i < H1.getNbAtoms() ; i++) {
			current = H1.getVertex(i);
			if (isLocallyUnifiable[i] == true) {
				try {
					Q = extension(H1, i, isLocallyUnifiable, E);
					if (localUnification(Q,R,E) == true)
						return true;
				}
				catch (ExtensionFailureException e) {
					// the extended set cannot be unified with R
				}
				isLocallyUnifiable[current.getID()] = false;	
			}
		}
	
		// no set have been found => failure
		return false;
	}

	/**
	 * Extends an atom conjunction for the unification algorithm. <br />
	 * This methods returns the atom conjunction corresponding to a minimal good unification set rooted in the atom ID from
	 * the complete atom conjunction H1.
	 * @param H1 The complete atom conjunction.
	 * @param atomRootID The id of the atom which will be the root of the returned atom conjunction.
	 * @param isLocallyUnifiable An array of booleans such as isLocallyUnifiable[i] = true iff atom i is still known as a
	 * locally unifiable one.
	 * @param E An array containing all existential positions of the rule whose head will be checked against 
	 * the returned atom conjunction.
	 * @return A minimal good unification set rooted in a specific atom.
	 * @throws ExtensionFailureException If there exists no good unification set rooted in atomID from H1.
	 */
	protected static AtomConjunction extension(AtomConjunction H1, int atomRootID, boolean isLocallyUnifiable[], int E[]) throws ExtensionFailureException {
		AtomConjunction result = new AtomConjunction();
		boolean isColored[] = new boolean[H1.getNbAtoms()+H1.getNbTerms()];
		Fifo<Vertex<Object> > waiting = new Fifo<Vertex<Object> >();
		Vertex<Object> current = H1.getVertex(atomRootID);
		Vertex<Object> neighbour = null;
		for (int i = 0 ; i < H1.getNbAtoms() ; i++) {
			if (isLocallyUnifiable[i]) {
				isColored[i] = false;
				for (NeighbourIterator neighbourIterator = H1.vertexTermIteratorFromAtom(i) ; neighbourIterator.hasNext() ; )
					isColored[neighbourIterator.next().getID()] = false;
			}
		}
		isColored[atomRootID] = true;
		
		result.addAtom(current,H1);
		waiting.put(current);
		
		while (!waiting.isEmpty()) {
			current = waiting.pop();
			if (H1.isAtom(current)) {
				for (int i = 0 ; i < E.length ; i++) {
					neighbour = H1.getVertexTermFromAtom(current.getID(),E[i]);
					if (((Term)(neighbour.getValue())).isConstant())
						throw new ExtensionFailureException();
					if (!isColored[neighbour.getID()]) {
						isColored[neighbour.getID()] = true;
						waiting.put(neighbour);
					}
				}
			}
			else {		// current is a term
				for (NeighbourIterator neighbourIterator = H1.vertexAtomIteratorFromTerm(current.getID()) ; neighbourIterator.hasNext() ;) {
					neighbour = neighbourIterator.next();
					if (!isLocallyUnifiable[neighbour.getID()])
						throw new ExtensionFailureException();
					if (!isColored[neighbour.getID()]) {
						isColored[neighbour.getID()] = true;
						waiting.put(neighbour);
						result.addAtom(neighbour,H1);	
					}
				}
			}
		}
		return result;
	}

	/**
	 * Checks if there is a unification between the head of the rule and the full atom conjunction passed as parameters.
	 * @param H1 The atom conjunction to be unified.
	 * @param R The rule whose head is to be unified.
	 * @param existentialIndex The position of all existential variables of R. This parameter is here for optimization only.
	 * @return True if there exists a unification, false otherwise.
	 */
	protected static boolean localUnification(AtomConjunction H1, AtomicRule R, int[] existentialIndex) {
		
			/** predicate check */
		for (Iterator<Vertex<Object> > iterator = H1._graph.firstVertexIterator() ; iterator.hasNext() ;) 
			if ((((Predicate)(iterator.next().getValue())).compareTo((Predicate)(R.getHead().getValue()))) != 0) 
				return false;
	

			/** graph generation */
		AtomConjunction unification = H1;//.clone();	// TODO not necessary if method subgraph
		unification._graph.addInFirstSet(((Predicate)(R.getHead().getValue())).clone());	
		int termID = -1;
		int firstHeadTermID = -1;
		for (Iterator<NeighbourEdge<Integer> > iterator = R._graph.neighbourIterator(R.getHead().getID()) ; iterator.hasNext() ;) {
			NeighbourEdge<Integer> edge = iterator.next();
			Term t = (Term)(R.get(edge.getIDV()));
			if (t.isConstant())
				termID = unification.addTerm(t);
			else {
				termID = -1;
				if (firstHeadTermID > 0) {
					for (int i = firstHeadTermID ; i < unification._graph.getNbVertices() ; i++) {
						if (unification.getTerm(i-unification.getNbAtoms()).getLabel().compareTo(t.getLabel()) == 0)
							termID = i;
					}
				}
				if (termID < 0) {
					unification._graph.addInSecondSet(t);
					termID = unification._graph.getNbVertices()-1;
				}
				if (firstHeadTermID < 0) 
					firstHeadTermID = termID;
			}
			try {
				unification._graph.addEdge(unification.getNbAtoms()-1,termID,new Integer(edge.getValue()));
			}
			catch (IllegalEdgeException e) { }
		}

		
			/** index */
		int headIndex = 0;

		Vertex<Object> headVertex = null;
		Vertex<Object> bodyVertex = null;
		Term headTerm = null;
		Term bodyTerm = null;
		int arity = ((Predicate)(R.getHead().getValue())).getArity();

		boolean[] isexistential = new boolean[unification.getNbTerms()];
		for (int i = 0 ; i < unification.getNbTerms() ; i++)
			isexistential[i] = false;
		for (int i = 0 ; i < existentialIndex.length ; i++)
			isexistential[unification.getVertexTermFromAtom(unification.getNbAtoms()-1,existentialIndex[i]).getID()-unification.getNbAtoms()] = true;


			/** algorithm */
		while (headIndex < arity) {
			headVertex = unification.getVertexTermFromAtom(unification.getNbAtoms()-1,headIndex);
			headTerm = (Term)(headVertex.getValue());
			for (int i = 0 ; i < unification.getNbAtoms() - 1 ; i++) {
				bodyVertex = unification.getVertexTermFromAtom(i,headIndex);
				bodyTerm = (Term)(bodyVertex.getValue());
				if (headVertex.getID() != bodyVertex.getID()) {
					if ((headTerm.isConstant() || isexistential[headVertex.getID()-unification.getNbAtoms()])
					&&  (bodyTerm.isConstant() || isexistential[bodyVertex.getID()-unification.getNbAtoms()]))
						return false;
					else if ((bodyTerm.isVariable()) && (!isexistential[bodyVertex.getID()-unification.getNbAtoms()])) {
						try {
							unification._graph.contract(bodyVertex.getID(),headVertex.getID());
						}
						catch (Exception e) {
							//e.printStackTrace();
							// TODO ConcurrentModificationException ???
						}
					}
					else {
						try {
							unification._graph.contract(headVertex.getID(),bodyVertex.getID());
							headVertex = bodyVertex;
							headTerm = bodyTerm;
						}
						catch (Exception e) {
							//e.printStackTrace();
							// TODO ConcurrentModificationException ???
						}
					}
				}
			}
			headIndex++;
		}
		return true;

	}

};

