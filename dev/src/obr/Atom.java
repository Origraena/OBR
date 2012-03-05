package obr;

import java.lang.String;
import java.lang.Iterable;
import java.util.NoSuchElementException;

public interface Atom extends Iterable<Term> {

	public Predicate getPredicate();
	public void setPredicate(Predicate value);
	public int getArity();
	public Term get(int i) throws NoSuchElementException;
	public void set(int i, Term t) throws NoSuchElementException;
	public void set(int i, String s) throws NoSuchElementException;
	
	public void fromString(String str) throws UnrecognizedStringFormatException;
	public String toString();


	public static final String PREDICATE_OPEN = "<";
	public static final String PREDICATE_CLOSE = ">";
	public static final String TERM_SEPARATOR = ",";


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


