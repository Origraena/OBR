package obr;

import java.lang.String;
import java.lang.Iterable;
import java.util.NoSuchElementException;
import java.util.ArrayList;

public class Atom implements Iterable<Term> {

	public static final String BEGIN_TERM_LIST = "<";
	public static final String END_TERM_LIST = ">";
	public static final String TERM_SEPARATOR = ",";

	public Atom(Predicate predicate) {
		_predicate = predicate;
		_terms = new ArrayList<Term>(_predicate.getArity());
		for (int i = 0 ; i < getArity() ; i++)
			_terms.add(null);
	}

	public Predicate getPredicate() {
		return _predicate;
	}

	public void setPredicate(Predicate value) {
		_predicate = value;
		_terms.clear();
		for (int i = 0 ; i < getArity() ; i++)
			_terms.add(null);
	}

	public int getArity() {
		return _predicate.getArity();
	}

	public Term get(int i) throws NoSuchElementException {
		if ((i >= getArity()) || (i < 0))
			throw new NoSuchElementException();
		return _terms.get(i);
	}

	public void set(int i, Term t) throws NoSuchElementException {
		if ((i >= getArity()) || (i < 0))
			throw new NoSuchElementException();
		_terms.set(i,t);
	}

//	public void fromString(String str) throws UnrecognizedStringFormatException;
	public String toString() {
		StringBuilder string = new StringBuilder(getPredicate().getLabel());
		string.append(BEGIN_TERM_LIST);
		for (int i = 0 ; i < getArity()-1 ; i++) {
			string.append(get(i));
			string.append(TERM_SEPARATOR);
		}
		if (getArity() >= 1)
			string.append(get(getArity()-1));
		string.append(END_TERM_LIST);
		return string.toString();
	}

	public java.util.Iterator<Term> iterator() {
		return _terms.iterator();
	}

	private Predicate _predicate;
	private ArrayList<Term> _terms;


	public class Iterator implements java.util.Iterator<Term> {
		public Iterator(Atom source) {
			_source = source;
		}
		public boolean hasNext() {
			return (_current < _source.getArity());
		}
		public Term next() throws NoSuchElementException {
			if (!hasNext())
				throw new NoSuchElementException();
			Term t = _source.get(_current);
			_current++;
			return t;
		}
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException();
		}
		private Atom _source;
		private int _current = 0;
	};

};


