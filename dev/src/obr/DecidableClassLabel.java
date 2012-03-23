package obr;

/**
 * Label for a decidable class check.
 * @see GRDAnalyser
 */
public class DecidableClassLabel {

	/**
	 * Constructor which let all boolean attributes to false.
	 * @param label The label of the decidable class.
	 */
	public DecidableClassLabel(String label) {
		_label = label;
	}

	/**
	 * Complete constructor.
	 * @param label The label of the decidable class.
	 * @param fus True if the concrete class belongs to fus abstract class.
	 * @param fes True if the concrete class belongs to fes abstract class.
	 * @param gbts True if the concrete class belongs to gbts abstract class.
	 */
	public DecidableClassLabel(String label, boolean fus, boolean fes, boolean gbts) {
		_label = label;
		_fus = fus;
		_fes = fes;
		_gbts = gbts;
	}

	/**
	 * Label getter.
	 * @return The label of the concrete class.
	 */
	public String getLabel() {
		return _label;
	}

	/**
	 * Converts the label into a string.
	 * @return A string representation of the concrete class label.
	 */
	@Override
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

	/** The real label of the concrete class. */
	private String _label;

	/** True if the concrete class belongs to fus abstract class. */
	private boolean _fus = false;

	/** True if the concrete class belongs to fes abstract class. */
	private boolean _fes = false;
	
	/** True if the concrete class belongs to gbts abstract class. */
	private boolean _gbts = false;
	
};

