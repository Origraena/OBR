package obr;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class AtomArrayList extends AbstractAtom implements Atom {

	public AtomArrayList(Predicate predicate) {
		super(predicate);
	}

	public AtomArrayList(String str) throws UnrecognizedStringFormatException {
		super(str);
	}

	public AtomArrayList(Atom a) {
		super(a);
	}

	public Predicate getPredicate() {
		return _predicate;
	}

	public void setPredicate(Predicate value) {
		_predicate = value;
		_terms = new ArrayList<Term>(getArity());
		for (int i = 0 ; i < getArity() ; i++)
			_terms.add(new Term(""));
	}

	public Term get(int i) throws NoSuchElementException {
		if ((i < 0) || (i >= getArity()))
			throw new NoSuchElementException();
		return _terms.get(i);
	}

	public void set(int i, Term t) throws NoSuchElementException {
		if ((i < 0) || (i >= getArity()))
			throw new NoSuchElementException();
		if (_terms.get(i).toString() == "") {	// first set
			for (int j = 0 ; j < getArity() ; j++) {
				if (i != j) {
					if (_terms.get(j).toString() == t.toString()) {
						_terms.set(i,_terms.get(j));
						break;
					}
				}
			}
			_terms.set(i,t);
		}
		else
			_terms.get(i).fromString(t.toString());
	}

	private Predicate _predicate;
	private ArrayList<Term> _terms;

};

