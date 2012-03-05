package obr;

import java.lang.Iterable;

public interface AtomConjonction extends Iterable<Atom> {

	public Atom get(int i);

	public Atom getAtomAt(int i);

	public Term getTermAt(int i);

	public Term getTermFromAtom(int atom, int i);

	public Iterator<Atom> iterator();
	public Iterator<Vertex<Atom> > atomIterator();
	public Iterator<Vertex<Term> > termIterator();
	
	public String toString();
	public void fromString(String str);

};

