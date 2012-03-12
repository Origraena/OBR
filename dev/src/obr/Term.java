package obr;

import java.lang.String;

public class Term {

	public Term(String label) {
		_label = label;
	}

	public String getLabel() {
		return _label;
	}

	public void setLabel(String value) {
		_label = value;
	}

	boolean isConstant() {
		return (_label.charAt(0) == '\'');
	}

	boolean isVariable() {
		return !isConstant();
	}

	/**
	 * check if the term is a constant generated from a rule application
	 */
	boolean isExistential() {
		return (isConstant() && (_label.charAt(1)=='_'));
	}

	boolean isUniversal() {
		return (isVariable() && !isExistential());
	}

	public String toString() {
		return _label;
	}

	public void fromString(String str) {
		_label = str;
	}

	public Term clone() {
		return new Term(new String(_label));
	}

	private String _label;

}


