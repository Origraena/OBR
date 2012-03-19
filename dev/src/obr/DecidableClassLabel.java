package obr;

public class DecidableClassLabel {

	public DecidableClassLabel(String label) {
		_label = label;
	}

	public DecidableClassLabel(String label, boolean fus, boolean fes, boolean gbts) {
		_label = label;
		_fus = fus;
		_fes = fes;
		_gbts = gbts;
	}

	public String getLabel() {
		return _label;
	}

	public String toString() {
		String result = _label;
		result += "(";
		if (_fus)
			result+=" fus";
		if (_fes)
			result+=" fes";
		if (_gbts)
			result+=" gbts";
		result += " )";
		return result;
	}

	private String _label;
	private boolean _fus = false;
	private boolean _fes = false;
	private boolean _gbts = false;
	
};

