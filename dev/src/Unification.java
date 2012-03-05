package obr;

public class Unification {

	/**
	 * @param H1 The atom conjonction to be unified.
	 * @param R The rule whose head will be unified.
	 * @return True if success, false otherwise.
	 */
	public static boolean existUnification(AtomConjonction H1, AtomicRule R) {

			/** init */
		AtomConjonction H2 = R.getBody();								// rule body
		Atom C2 = R.getHead();											// rule head
		boolean isLocallyUnifiable[] = new boolean[H1.getNbAtoms()];	// valuing true while atom is supposed locally unifiable
		int E[] = R.getExistentialIndex();								// positions of existential variables in the rule
		Vertex<Atom> current = null;
		AtomConjonction Q = null;

			/** preprocessing */
		for (Iterator<Vertex<Atom> > iterator = H1.firstSetIterator() ; iterator.hasNext() ; ) {
			current = iterator.next();
			if (localUnification(new AtomConjonction(current.getValue()), new AtomicRule(R)) == true)
				isLocallyUnifiable[current.getID()] = true;
			else
				isLocallyUnifiable[current.getID()] = false;
		}

			/** extension */
		for (Iterator<Vertex<Atom> > iterator = H1.firstSetIterator() ; iterator.hasNext() ; ) {
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
				continue {	// the continue block is only read after the catch block, or if the localUnification(Q,R) returns a failure
					isLocallyUnifiable[current.getID()] = false;	
				}
			}
		}
	
		// no set have been found => failure
		return false;
	}

	protected static boolean localUnification(AtomConjonction H1, AtomicRule R) {
		
			/** predicate check */
		for (Atom a : H1)
			if (a.getPredicate() != R.getHead().getPredicate())
				return false;
		
			/** graph generation */
		AtomConjonction unification = H1;
		unification.addAtom(R.getHead());
		for (Iterator<NeighbourEdge<Integer> > headIterator = R.neighbourIterator(R.getHead().getID()) ; headIterator.hasNext() ; ) {
			NeighbourEdge<Integer> headEdge = headIterator.next();
			Vertex<Object> headObject = R.getVertex(headEdge.getIDV());
			Term headTerm = (Term)headObject.getValue();
			if (headTerm.isConstant()) {
				// 
			}
			// else
			unification.addTerm(headTerm);
			unification.addEdge(unification.getNbAtoms()-1,unification.getNbTerms()-1);
		}

			/** init */
		ArrayList<Iterator<NeighbourEdge<Integer> > > iterators = new ArrayList<Iterator<NeighbourEdge<Integer> > >(H1.getNbAtoms());


	}

};

