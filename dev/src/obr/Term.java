package obr;

import java.lang.String;

public class Term {

	public Term(String label) {
		_label = label;
	}

	boolean isConstant() {
		return (_label.charAt(0) == '\'');
	}

	boolean isVariable() {
		return !isConstant();
	}

	boolean isExistential() {
		return (_label.charAt(0)=='_');
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

	private String _label;

}


