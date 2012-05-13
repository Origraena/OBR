package obr;

import java.lang.Comparable;

/**
 * Represents a predicate with its label and its arity.<br />
 */
public class Predicate implements Comparable<Predicate> {

	/**
	 * Constructor.
	 * @param label The predicate label.
	 * @param arity The predicate arity.
	 */
	public Predicate(String label, int arity) {
		_label = label;
		_arity = arity;
	}

	/**
	 * Copy constructor.
	 * @param p The predicate to be copied.
	 */
	public Predicate(Predicate p) {
		_label = new String(p.getLabel());
		_arity = p.getArity();
	}

	/**
	 * Constructor from a string representation.
	 * @param str The string representation.
	 * @throws UnrecognizedStringFormatException If the string format is not correct.
	 */
	public Predicate(String str) throws UnrecognizedStringFormatException {
		fromString(str);
	}

	/**
	 * Label getter.
	 * @return The predicate label.
	 */
	public String getLabel() {
		return _label;
	}

	/**
	 * Arity getter.
	 * @return The predicate arity.
	 */
	public int getArity() {
		return _arity;
	}

	/**
	 * Label setter.
	 * @param value The new label to set.
	 */
	public void setLabel(String value) {
		_label = value;
	}
	
	/**
	 * Arity setter.
	 * @param value The new arity to set.
	 */
	public void setArity(int value) {
		_arity = value;
	}

	/**
	 * Converts the predicate into a string.
	 * @return A string representation of the predicate.
	 */
	@Override
	public String toString() {
		return _label + '/' + _arity;
	}

	/**
	 * Converts from a string.<br />
	 * A well-formated string is under the form of : &lt;predicate label&gt;/&lt;arity&gt;.
	 * @param str The string representation of a predicate.
	 * @throws UnrecognizedStringFormatException If the string is not well-formated.
	 */
	protected void fromString(String str) throws UnrecognizedStringFormatException {
		String[] substr = str.split("/");
		if (substr.length != 2)
			throw new UnrecognizedStringFormatException();
		_label = substr[0];
		_arity = new Integer(substr[1]).intValue();
	}

	/**
	 * Clones the predicate.
	 * @return A clone.
	 */
	public Predicate clone() {
		return new Predicate(new String(_label),_arity);
	}
	
	/**
	 * Compares two predicates.<br />
	 * Both label and arity are used.
	 * @return <ul><li>-1 if this &lt; p</li>
	 * <li>0 if this = p</li>
	 * <li>1 if this > p</li></ul>
	 */
	@Override
	public int compareTo(Predicate p) {
		if (getArity() < p.getArity())
			return -1;
		else if (getArity() > p.getArity())
			return 1;
		else
			return _label.compareTo(p.getLabel());
	}

	/**
	 * Overriden to provide an equals-consistent class.
	 * @param o The object to check against.
	 * @return True if this equals to o, false otherwise.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Predicate))
			return false;
		return compareTo((Predicate)o) == 0;
	}
	
	/**
	 * Hashcode of the predicate.<br />
	 * Hash the label and adds the arity.
	 * @return The hash code of the predicate.
	 */
	@Override
	public int hashCode() {
		return _label.hashCode()+_arity;
	}

	/** The label of the predicate. */
	private String _label;

	/** The arity of the predicate. */
	private int _arity = 1;

};

