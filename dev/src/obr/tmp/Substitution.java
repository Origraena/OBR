package obr;

public interface Substitution<T1,T2> extends Iterable<Pair<T1,T2> > {

	public int size();
	public Pair<T1,T2> get(int i) throws NoSuchElementException;
	public T1 getFirst(int i) throws NoSuchElementException;
	public T2 getSecond(int i) throws NoSuchElementException;
	public void add(Pair<T1,T2> pair) throws IllegalOperationException;
	public void add(T1 t1, T2 t2) throws IllegalOperationException;
	public void addAll(Iterable<Pair<T1,T2> > collection);
	public void remove(int i) throws NoSuchElementException;
	public void remove(T1 t1) throws NoSuchElementException;

//	public Collection<T2> apply(Collection<T1> firstSet);

};


