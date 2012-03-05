package obr;

import java.lang.String;
import java.lang.Iterable;
import java.util.NoSuchElementException;

public abstract class AbstractAtom implements Atom {

	public AbstractAtom(Predicate predicate) {
		setPredicate(predicate);
	}

	public AbstractAtom(String str) throws UnrecognizedStringFormatException {
		fromString(str);
	}
	
	public AbstractAtom(Atom a) {
		setPredicate(a.getPredicate());
		for (int i = 0 ; i < getArity() ; i++)
			set(i,a.get(i));
	}

	public int getArity() {
		return getPredicate().getArity();
	}

	public void set(int i, String s) throws NoSuchElementException {
		set(i, new Term(s));
	}

	public void fromString(String str) throws UnrecognizedStringFormatException {
		String[] sub1;
		String[] sub2;
		Predicate p;
		sub1 = str.split(PREDICATE_OPEN);
		if (sub1.length != 2)
			throw new UnrecognizedStringFormatException();
		sub1[1] = sub1[1].substring(0,sub1[1].length()-1);
		sub2 = sub1[1].split(TERM_SEPARATOR);
		p = new Predicate(sub1[0],sub2.length);
		setPredicate(p);
		for (int i = 0 ; i < getArity() ; i++)
			set(i,sub2[i]);
	}

	public String toString() {
		StringBuilder str = new StringBuilder(getPredicate().getLabel());
		str.append(PREDICATE_OPEN);
		for (int i = 0 ; i < getArity() - 1 ; i++) {
			str.append(get(i).toString());
			str.append(TERM_SEPARATOR);
		}
		str.append(get(getArity() - 1).toString());
		str.append(PREDICATE_CLOSE);
		return str.toString();
	}

	public java.util.Iterator<Term> iterator() {
		return new Atom.Iterator(this);
	}

};


