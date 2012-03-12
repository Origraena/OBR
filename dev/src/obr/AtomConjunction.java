package obr;

import moca.graphs.vertices.Vertex;

import java.lang.Iterable;
import java.util.Iterator;
import java.util.NoSuchElementException;


public interface AtomConjunction /*extends Iterable<Atom>*/ {

//	public Atom get(int i) throws NoSuchElementException;

//	public Atom getAtom(int i) throws NoSuchElementException;

	public Term getTerm(int i) throws NoSuchElementException;

	public Term getTermFromAtom(int atom, int i) throws NoSuchElementException;

//	public Iterator<Atom> iterator();
//	public Iterator<Vertex<Atom> > atomIterator();
//	public Iterator<Vertex<Term> > termIterator();
	
	public String toString();
	public void fromString(String str) throws UnrecognizedStringFormatException;

};

