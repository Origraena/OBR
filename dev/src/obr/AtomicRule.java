package obr;

import moca.graphs.BipartedGraph;
import moca.graphs.vertices.Vertex;
import moca.graphs.vertices.VertexArrayList;
import moca.graphs.edges.NeighbourEdge;
import moca.graphs.edges.UndirectedNeighboursLists;
import moca.graphs.edges.IllegalEdgeException;

import java.lang.String;
import java.lang.Integer;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;

public class AtomicRule extends GraphAtomConjonction {

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
		if (v.getID() >= getNbAtoms())
			return true;
		return false;
	}

	public boolean isBody(Vertex<Object> v) throws NoSuchElementException {
		if (v == null)
			throw new NoSuchElementException();
		if (v == _head)
			return false;
		if (v.getID() < getNbAtoms())
			return true;
		for (Iterator<NeighbourEdge<Integer> > iterator = _graph.neighbourIterator(v.getID()) ; iterator.hasNext() ;)
			if (!(_graph.getVertex(iterator.next().getIDV()) == v))
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

	public AtomicRule cloneIn(AtomicRule copy) {
		super.cloneIn(copy);
		copy._head = copy.getVertex(_head.getID());
		return copy;
	}

	public AtomicRule clone() {
		AtomicRule copy = new AtomicRule();
		return cloneIn(copy);
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
/*	public static boolean existUnification(GraphAtomConjonction H1, AtomicRule R) {

			/** init */
/*		GraphAtomConjonction H2 = R.getBody();								// rule body
		Atom C2 = R.getHead();											// rule head
		boolean isLocallyUnifiable[] = new boolean[H1.getNbAtoms()];	// valuing true while atom is supposed locally unifiable
		int E[] = R.getExistentialIndex();								// positions of existential variables in the rule
		Vertex<Atom> current = null;
		AtomConjonction Q = null;

			/** preprocessing */
/*		for (Iterator<Vertex<Atom> > iterator = H1.firstSetIterator() ; iterator.hasNext() ; ) {
			current = iterator.next();
			if (localUnification(new GraphAtomConjonction(current.getValue().toString()), R) == true)
				isLocallyUnifiable[current.getID()] = true;
			else
				isLocallyUnifiable[current.getID()] = false;
		}

			/** extension */
/*		for (Iterator<Vertex<Atom> > iterator = H1.firstSetIterator() ; iterator.hasNext() ; ) {
			current = iterator.next();
			if (isLocallyUnifiable[current.getID()] == true) {
				try {
					Q = extension(H1, current, isLocallyUnifiable, E);
					if (localUnification(Q,R) == true)
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
*/
	public static boolean localUnification(GraphAtomConjonction H1, AtomicRule R, int[] existentialIndex) {
		
		System.out.println("localUnification");
		System.out.println("H1 = "+H1);
		System.out.println("R = "+R);

			/** predicate check */
		for (Iterator<Vertex<Object> > iterator = H1._graph.firstVertexIterator() ; iterator.hasNext() ;)
			if ((((Predicate)(iterator.next().getValue())).compareTo((Predicate)(R.getHead().getValue()))) != 0)
				return false;
	
	//	System.out.println("predicate ok!");

			/** graph generation */
		GraphAtomConjonction unification = H1.clone();	// TODO not necessary if method subgraph
		unification._graph.addInFirstSet(((Predicate)(R.getHead().getValue())).clone());	
		for (Iterator<NeighbourEdge<Integer> > iterator = R._graph.neighbourIterator(R.getHead().getID()) ; iterator.hasNext() ;) {
			NeighbourEdge<Integer> edge = iterator.next();
			Term t = (Term)(R.get(edge.getIDV()));
			if ((t.getLabel().length() >= 2) && (t.getLabel().charAt(t.getLabel().length()-1) != 'R') && (t.getLabel().charAt(t.getLabel().length()-2) != '_'))
				t.setLabel(t.getLabel()+"_R");
			int idV = unification.addTerm(t);
			try {
				unification._graph.addEdge(unification.getNbAtoms()-1,idV,new Integer(edge.getValue()));
			}
			catch (IllegalEdgeException e) { }
		}

		System.out.println("Graph generated!");
		System.out.println(unification);

			/** iterators init */
		ArrayList<Iterator<NeighbourEdge<Integer> > > iterators = new ArrayList<Iterator<NeighbourEdge<Integer> > >(H1.getNbAtoms());
		Iterator<NeighbourEdge<Integer> > headIterator = unification._graph.neighbourIterator(unification.getNbAtoms()-1);
		for (int i = 0 ; i < unification.getNbAtoms()-1 ; i++) {
			iterators.add(unification._graph.neighbourIterator(i));
		}

		NeighbourEdge<Integer> headEdge = null;
		NeighbourEdge<Integer> bodyEdge = null;
		Vertex<Object> headVertex = null;
		Vertex<Object> bodyVertex = null;
		Term headTerm = null;
		Term bodyTerm = null;

		boolean[] isexistential = new boolean[unification.getNbTerms()];
		for (int i = 0 ; i < unification.getNbTerms() ; i++)
			isexistential[i] = false;
		for (int i = 0 ; i < existentialIndex.length ; i++)
			isexistential[existentialIndex[i]-unification.getNbAtoms()] = true;

	//	System.out.println("Init finished!");

		int debug = 0;
			/** algorithm */
		while ((headIterator!=null)&&(headIterator.hasNext())) {
	//		System.out.println("iteration = "+debug);
			try {
				headEdge = headIterator.next();
			}
			catch (ConcurrentModificationException e) {
	//			System.out.println("ConcurrentModificationException from head iterator : "+e);
				headIterator = null;
			}
	//		System.out.println("headEdge = "+headEdge);
			headVertex = unification._graph.getVertex(headEdge.getIDV());
	//		System.out.println("headVertex = "+headVertex);
			headTerm = (Term)(headVertex.getValue());
	//		System.out.println("headTerm = "+headTerm);
			int debug2 = 0;
			for (Iterator<NeighbourEdge<Integer> > bodyIterator : iterators) {
				try {
	//			System.out.println("for : "+debug2);
				bodyEdge = bodyIterator.next();	// cannot fail because the predicates are checked
	//			System.out.println("bodyEdge = "+bodyEdge);
				bodyVertex = unification._graph.getVertex(bodyEdge.getIDV());
	//			System.out.println("bodyVertex = "+bodyVertex);
				bodyTerm = (Term)(bodyVertex.getValue());
	//			System.out.println("bodyTerm = "+bodyTerm);
				if (headVertex.getID() != bodyVertex.getID()) {
				//if (headTerm != bodyTerm) {
					if ((headTerm.isConstant() || isexistential[headVertex.getID()-unification.getNbAtoms()])
					&&  (bodyTerm.isConstant() || isexistential[bodyVertex.getID()-unification.getNbAtoms()]))
						return false;
					else if ((bodyTerm.isVariable()) && (!isexistential[bodyVertex.getID()-unification.getNbAtoms()])) {
						unification._graph.contract(bodyVertex.getID(),headVertex.getID());
	//					System.out.println("case2");
					}
					else {
						unification._graph.contract(headVertex.getID(),bodyVertex.getID());
						headVertex = bodyVertex;
						headTerm = bodyTerm;
	//					System.out.println("case1");
	//					System.out.println("headVertex = "+headVertex);
	//					System.out.println("headTerm = "+headTerm);
					}
				}
				}
				catch (ConcurrentModificationException e) { 
	//				System.out.println("+++"+e);
				}
	//			System.out.println("graph updated :");
				System.out.println(unification);
				debug2++;
			}
			debug++;
		}
		return true;

	}

};

