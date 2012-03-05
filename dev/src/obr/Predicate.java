package obr;

public class Predicate {

	public Predicate(String label, int arity) {
		_label = label;
		_arity = arity;
	}

	public Predicate(Predicate p) {
		_label = new String(p.getLabel());
		_arity = p.getArity();
	}

	public Predicate(String str) throws UnrecognizedStringFormatException {
		fromString(str);
	}

	public String getLabel() {
		return _label;
	}

	public int getArity() {
		return _arity;
	}

	public void setLabel(String value) {
		_label = value;
	}

	public void setArity(int value) {
		_arity = value;
	}

	public String toString() {
		return _label + '/' + _arity;
	}

	public void fromString(String str) throws UnrecognizedStringFormatException {
		String[] substr = str.split("/");
		if (substr.length != 2)
			throw new UnrecognizedStringFormatException();
		_label = substr[0];
		_arity = new Integer(substr[1]).intValue();
	}
	
	private String _label;
	private int _arity = 1;

};

