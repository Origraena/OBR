package obr;

public abstract class AbstractSubstitution<T1,T2> implements Substitution<T1,T2> {

	public T2 get(T1 t1) throws NoSuchElementException {
		for (Pair<T1,T2> pair : this)
			if (t1 == pair.getFirst())
				return pair.getSecond();
		throw new NoSuchElementException();
	}

	public T1 getFirst(int i) throws NoSuchElementException {
		return get(i).getFirst();
	}

	public T2 getSecond(int i) throws NoSuchElementException {
		return get(i).getSecond();
	}

	public void add(Pair<T1,T2> pair) throws IllegalOperationException {
		if (contains(t1))
			throw new IllegalOperationException();
		internalAdd(pair);
	}

	public void add(T1 t1, T2 t2) throws IllegalOperationException {
		add(new Pair<T1,T2>(t1,t2));
	}

	public void addAll(Iterable<Pair<T1,T2> > collection) {
		for (Pair<T1,T2> pair : collection)
			add(pair);
	}

	public void remove(T1 t1) throws NoSuchElementException {
		boolean success = false;
		for (int i = 0 ; (i < size()) && !success ; i++) {
			if (getFirst(i) == t1) {
				remove(i);
				success = true;
			}
		}
		if (!success) 
			throw new NoSuchElementException();
	}

	/*public Collection<T2> apply(Collection<T1> firstSet, Collection<T2> secondSet) {
		secondSet.clear();
		for (T1 t1 : firstSet) {
			secondSet.add(get(t1));
		}
		return secondSet;
	}*/


	protected abstract void internalAdd(Pair<T1,T2> pair);

};


