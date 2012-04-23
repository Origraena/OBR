package obr;

import java.util.Scanner;
import java.util.regex.Pattern;
import java.io.File;

/** 
 * This class provides static functions to parse a single rule from its string
 * representation, or a full set of rules from a file.
 */
public class DTGParser {

	public static final String HEAD_SEPARATOR = " :- ";
	public static final String BEGIN_TERM_LIST = "\\(";		// TODO
	public static final String END_TERM_LIST = "\\)";
	public static final String TERM_SEPARATOR = ", ";
	/** Not marker constant. */
	public static final char NOT_MARK = '!';
	/** End of line marker. */
	public static final char END_LINE = '.';
	/** 
	 * Absurd predicate used to convert <code>body --&gt; !head</code> to
	 * <code>body;head--&gt;ABSURD</code>.
	 */
	public static final Predicate ABSURD = new Predicate("ABSURD",0);

	/**
	 * Parses a rule under a DTG format.
	 * @param str The rule string representation.
	 * @throws UnrecognizedStringFormatException If the string format is not ok.
	 */
	public static AtomicRule parseRule(String str) throws UnrecognizedStringFormatException {
		String[] subs = str.split(HEAD_SEPARATOR);
		if (subs.length != 2)
			throw new UnrecognizedStringFormatException();
		if (subs[1].charAt(subs[1].length()-1) != END_LINE)
			throw new UnrecognizedStringFormatException();
		AtomicRule rule = new AtomicRule();
		
		String[] terms;
		String[] atomSubs;
		int termID, atomID;
		boolean not = false;

		if (subs[0].charAt(0) == NOT_MARK) {
			subs[0] = subs[0].substring(1,subs[0].length());
			not = true;
		}

		/* HEAD */
		atomSubs = subs[0].split(BEGIN_TERM_LIST);
		if (atomSubs.length != 2)
			throw new UnrecognizedStringFormatException();
		atomSubs[1] = atomSubs[1].substring(0,atomSubs[1].length()-1);
		terms = atomSubs[1].split(TERM_SEPARATOR);
		atomID = rule.addAtom(new Predicate(atomSubs[0],terms.length));
		for (int i = 0 ; i < terms.length ; i++) {
			termID = rule.addTerm(terms[i]);
			rule.addEdge(atomID,termID,i);
		}
		rule.setHead(atomID);

		/* BODY */
		subs[1] = subs[1].substring(0,subs[1].length()-1);	// last char is a dot
		atomSubs = subs[0].split(BEGIN_TERM_LIST);
		if (atomSubs.length != 2)
			throw new UnrecognizedStringFormatException();
		atomSubs[1] = atomSubs[1].substring(0,atomSubs[1].length()-1);
		terms = atomSubs[1].split(TERM_SEPARATOR);
		atomID = rule.addAtom(new Predicate(atomSubs[0],terms.length));
		for (int i = 0 ; i < terms.length ; i++) {
			termID = rule.addTerm(terms[i]);
			rule.addEdge(atomID,termID,i);
		}

		if (not) {
			atomID = rule.addAtom(ABSURD);
			rule.setHead(atomID);
		}

		return rule;

	}

	/**
	 * Parses a file and returns the graphe of rule dependencies generated from the rule
	 * set.
	 * @param filePath The path to the file to read.
	 * @return The graph of rule dependencies corresponding, or null if an error occured.
	 */
	public static GraphRuleDependencies parseRules(String filePath) {
		try {
			GraphRuleDependencies grd = new GraphRuleDependencies();
			String line = null;
			Scanner scan = new Scanner(new File(filePath));
			scan.useDelimiter(Pattern.compile("\n"));
			line = scan.next();
			while (scan.hasNext()) {
				if ((line.length() >= 2) && ((line.charAt(0) != '/') || (line.charAt(1) != '/'))) {
					try {
						grd.addVertex(parseRule(line));
					}
					catch (UnrecognizedStringFormatException exception) { }
				}
				line = scan.next();
			}
			scan.close();
			return grd;
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
			return null;
		}
	}

	/** This class cannot be instantiated. */
	protected DTGParser() { }

};

