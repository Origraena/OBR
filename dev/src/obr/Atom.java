package obr;

import java.lang.String;
import java.lang.Iterable;
import java.util.NoSuchElementException;
import java.util.ArrayList;

/**
 * Represents an atom.<br />
 * Note that instances of this class can be returned by the atom conjunction class, but this will imply
 * the atom creation. I.e., this class is not used to store atom information, but to get a convenient
 * instance.
 */
public class Atom implements Iterable<Term> {

	/** Represents the begin of a term list when the atom is under a String format. */
	public static final String BEGIN_TERM_LIST = "\\(";
	public static final String BEGIN_TERM_LIST_W = "(";
	/** Represents the end of a term list when the atom is under a String format. */
	public static final String END_TERM_LIST = "\\)";
	public static final String END_TERM_LIST_W = ")";
	/** Represents the term separator when the atom is under a String format. */
	public static final String TERM_SEPARATOR = ",";

	/**
	 * Constructor. <br />
	 * Note that there is no default constructor since this class should not be used directly but be get by an
	 * atom conjunction method.
	 * @param predicate The predicate of the atom to be instantiated.
	 */
	public Atom(Predicate predicate) {
		_predicate = predicate;
		_terms = new ArrayList<Term>(_predicate.getArity());
		for (int i = 0 ; i < getArity() ; i++)
			_terms.add(null);
	}

	/**
	 * Predicate getter.
	 * @return The atom predicate.
	 */
	public Predicate getPredicate() {
		return _predicate;
	}

	/**
	 * Predicate setter.
	 * @param value The new predicate to set.
	 */
	public void setPredicate(Predicate value) {
		_predicate = value;
		_terms.clear();
		for (int i = 0 ; i < getArity() ; i++)
			_terms.add(null);
	}

	/**
	 * Convenient method to get the predicate arity.
	 * @return The atom arity.
	 */
	public int getArity() {
		return _predicate.getArity();
	}

	/**
	 * Term getter.
	 * @param i The term index inside the atom.
	 * @return The term corresponding to this index.
	 * @throws NoSuchElementException If i is out of range.
	 */
	public Term get(int i) throws NoSuchElementException {
		if ((i >= getArity()) || (i < 0))
			throw new NoSuchElementException();
		return _terms.get(i);
	}

	public boolean contains(Term term) {
		for (Term thisterm : this) {
			if (term.getLabel().compareTo(thisterm.getLabel()) == 0)
				return true;
		}
		return false;
	}

	public int getNbConstants() {
		int result = 0;
		for (Term term : this)
			if (term.isConstant())
				result++;
		return result;
	}

	public ArrayList<Term> constants() {
		ArrayList<Term> result = new ArrayList<Term>();
		for (Term term : this)
			if (term.isConstant())
				result.add(term);
		return result;
	}
	public int getNbVariables() {
		int result = 0;
		for (Term term : this)
			if (term.isVariable())
				result++;
		return result;
	}

	public ArrayList<Term> variables() {
		ArrayList<Term> result = new ArrayList<Term>();
		for (Term term : this)
			if (term.isVariable())
				result.add(term);
		return result;
	}

	/**
	 * Term setter.
	 * @param i The term index inside the atom.
	 * @param t The new term to be set instead of the old one.
	 * @throws NoSuchElementException If i is out of range.
	 */
	public void set(int i, Term t) throws NoSuchElementException {
		if ((i >= getArity()) || (i < 0))
			throw new NoSuchElementException();
		_terms.set(i,t);
	}

	/**
	 * Converts the atom into a String.
	 */
	@Override
	public String toString() {
		StringBuilder string = new StringBuilder(getPredicate().getLabel());
		string.append(BEGIN_TERM_LIST_W);
		for (int i = 0 ; i < getArity()-1 ; i++) {
			string.append(get(i));
			string.append(TERM_SEPARATOR);
		}
		if (getArity() >= 1)
			string.append(get(getArity()-1));
		string.append(END_TERM_LIST_W);
		return string.toString();
	}

	/**
	 * Access to the nested Iterator over terms instance.
	 * @return The iterator over terms corresponding to this atom instance.
	 */
	public java.util.Iterator<Term> iterator() {
		return new Iterator();
	}

	/** Predicate of the atom. */
	private Predicate _predicate;
	/** List of terms. */
	private ArrayList<Term> _terms;

	/**
	 * Nested class used to iterate over the terms.<br />
	 * Note that the remove() operation is not supported, and will always throw an UnsupportedOperationException.
	 */
	public class Iterator implements java.util.Iterator<Term> {
		/**
		 * Allows to know if there is still element to be iterated.
		 * @return True if the next call to next() method will not throw a NoSuchElementException, false otherwise.
		 */
		@Override
		public boolean hasNext() {
			return (_current < getArity());
		}
		/**
		 * Access to the next element.
		 * @return The next term of the atom.
		 * @throws NoSuchElementException If there is no more term to be iterated.
		 */
		@Override
		public Term next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException();
			Term t = get(_current);
			_current++;
			return t;
		}
		/** 
		 * Unsupported operation.
		 * @throws UnsupportedOperationException Whenever this method is called.
		 */
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
		/** Current term index. */
		private int _current = 0;
	};

};


