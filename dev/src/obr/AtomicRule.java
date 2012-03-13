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

public class AtomicRule extends GraphAtomConjunction {

	/* Constants */
	public static final String HEAD_SEPARATOR	=	new String("-->");

	public AtomicRule() {
		super();
	}

	/** @see .fromString(String) */
	public AtomicRule(String stringRepresentation) {
		super();
		try {
			fromString(stringRepresentation);
		}
		catch (UnrecognizedStringFormatException e) { }
	}

	public Vertex<Object> getHead() {
		return _head;
	}

	public Atom getHeadAtom() {
		return getAtom(_head.getID());
	}

	public boolean isBody(Vertex<Object> v) throws NoSuchElementException {
		if (v == null)
			throw new NoSuchElementException();
		if (v == _head)
			return false;
		if (v.getID() < getNbAtoms())
			return true;
		for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(v.getID()) ; iterator.hasNext() ;)
			if (!(_graph.getVertex(iterator.next().getIDV()) == _head))
				return true;
		return false;
	}

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
	 * @throws NoSuchElementException whenever an atom vertex is passed as parameter (only a term can be in the frontier)
	 */
	public boolean isFrontier(Vertex<Object> v) throws NoSuchElementException {
		if ((v == null) || /*(*/(v.getID() < getNbAtoms()))/* || (v.getID() > getNbVertices())) */
			throw new NoSuchElementException();
		return isBody(v) && isHead(v);
	}

	public boolean isExistential(Vertex<Object> v) {
		if ((v == null) || (v.getID() < getNbAtoms()))
			return false;
		return !((((Term)(v.getValue())).isConstant()) || isBody(v));
	}

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

	@Override
	public void fromString(String str) throws UnrecognizedStringFormatException {
		String[] sub1 = str.split(HEAD_SEPARATOR);
		if (sub1.length != 2)
			throw new UnrecognizedStringFormatException();
		sub1[0]+=ATOM_SEPARATOR;
		sub1[0]+=sub1[1];
		super.fromString(sub1[0]);
		_head = getVertex(getNbAtoms()-1);
	}

	@Override
	public String toString() {
		return super.toString(getNbAtoms()-1)+HEAD_SEPARATOR+getHeadAtom();
	}

	public GraphAtomConjunction getBody() {
		return subAtomConjunction(0, getNbAtoms()-1);
	}

	public AtomicRule cloneIn(AtomicRule copy) {
		super.cloneIn(copy);
		copy._head = copy.getVertex(_head.getID());
		return copy;
	}

	public AtomicRule clone() {
		AtomicRule copy = new AtomicRule();
		return cloneIn(copy);
	}

	public boolean mayImply(AtomicRule R) {
		return existUnification(R.getBody(), this);
	}

	private Vertex<Object> _head = null;


	/* ALGORITHMS */

	/**
	 * Unification
	 */

	/**
	 * @param H1 The atom conjonction to be unified.
	 * @param R The rule whose head will be unified.
	 * @return True if success, false otherwise.
	 */
	public static boolean existUnification(GraphAtomConjunction H1, AtomicRule R) {
			/** init */
		boolean isLocallyUnifiable[] = new boolean[H1.getNbAtoms()];	// valuing true while atom is supposed locally unifiable
		int E[] = R.existentialIndex();								// positions of existential variables in the rule
		Vertex<Object> head = null;
		Vertex<Object> current = null;
		GraphAtomConjunction Q = null;

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

	public static GraphAtomConjunction extension(GraphAtomConjunction H1, int atomRootID, boolean isLocallyUnifiable[], int E[]) throws ExtensionFailureException {
		GraphAtomConjunction result = new GraphAtomConjunction();
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

	public static boolean localUnification(GraphAtomConjunction H1, AtomicRule R, int[] existentialIndex) {
		
			/** predicate check */
		for (Iterator<Vertex<Object> > iterator = H1._graph.firstVertexIterator() ; iterator.hasNext() ;) 
			if ((((Predicate)(iterator.next().getValue())).compareTo((Predicate)(R.getHead().getValue()))) != 0) 
				return false;
	

			/** graph generation */
		GraphAtomConjunction unification = H1;//.clone();	// TODO not necessary if method subgraph
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
					else if ((bodyTerm.isVariable()) && (!isexistential[bodyVertex.getID()-unification.getNbAtoms()]))
						unification._graph.contract(bodyVertex.getID(),headVertex.getID());
					else {
						unification._graph.contract(headVertex.getID(),bodyVertex.getID());
						headVertex = bodyVertex;
						headTerm = bodyTerm;
					}
				}
			}
			headIndex++;
		}
		return true;

	}

};

