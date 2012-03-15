package obr;

public class DecidableClassLabel {

	public DecidableClassLabel(String label) {
		_label = label;
	}

	public DecidableClassLabel(String label, boolean allowFC, boolean allowBC) {
		_label = label;
		_allowFC = allowFC;
		_allowBC = allowBC;
	}

	public String getLabel() {
		return _label;
	}

	public boolean allowFC() {
		return _allowFC;
	}

	public boolean allowBC() {
		return _allowBC;
	}

	private String _label;
	private boolean _allowFC = false;
	private boolean _allowBC = false;
	
};

