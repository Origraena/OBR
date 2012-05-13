package obr;

/**
 * Label for a decidable class check.
 * @see GRDAnalyser
 */
public class DecidableClassLabel {

	static final int FES = 1;
	static final int GBTS = 2;
	static final int FUS = 3;
	static final String FES_STR = "fes";
	static final String GBTS_STR = "gbts";
	static final String FUS_STR = "fus";
	static final String FES_STRING = "Finite Expansion Set";
	static final String GBTS_STRING = "Greedy Bounded Treewidth Set";
	static final String FUS_STRING = "Finite Unification Set";

	static final String shortName(int abstractClassID) {
		switch (abstractClassID) {
			case FUS :
				return FUS_STR;
			case FES :
				return FES_STR;
			case GBTS :
				return GBTS_STR;
			default :
				return "";
		}
	}

	static final String longName(int abstractClassID) {
		switch (abstractClassID) {
			case FUS :
				return FUS_STRING;
			case FES :
				return FES_STRING;
			case GBTS :
				return GBTS_STRING;
			default :
				return "";
		}
	}

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

	public boolean isFUS() {
		return _fus;
	}

	public boolean isFES() {
		return _fes;
	}

	public boolean isGBTS() {
		return _gbts;
	}

	/**
	 * Converts the label into a string.
	 * @return A string representation of the concrete class label.
	 */
	@Override
	public String toString() {
		String result = _label;
		result += "( ";
		if (_fus)
			result+=FUS_STR+" ";
		if (_fes)
			result+=FES_STR + " ";
		if (_gbts)
			result+=GBTS_STR + " ";
		result += ")";
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

