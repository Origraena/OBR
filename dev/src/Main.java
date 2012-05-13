import moca.graphs.*;
import moca.graphs.vertices.*;
import moca.graphs.edges.*;
import java.lang.Exception;
import java.util.Iterator;
import java.util.ArrayList;
import obr.*;

public class Main {
	public static void main(String args[]) {
		try {
			System.out.println("Reading file : "+args[0]);
			GraphRuleDependencies grd = null;
			String[] filePathSubs = args[0].split("\\.");
			if (filePathSubs.length == 1)
				grd = new GraphRuleDependencies(args[0]);
			else if (filePathSubs[filePathSubs.length-1].equals("dtg")) {
				System.out.println("Invoking DTG parser...");
				grd = DTGParser.parseRules(args[0]);
			}

			System.out.println("\n[1] GRAPH OF RULE DEPENDENCIES\n\n"+grd);
			System.out.println("\n\n[2] STRONGLY CONNECTED COMPONENTS\n");
			System.out.println(grd.stronglyConnectedComponentsToString());

			GRDAnalyser analyser = new GRDAnalyser(grd);
			analyser.addDecidableClassCheck(new DisconnectedCheck());
			analyser.addDecidableClassCheck(new RangeRestrictedCheck());
			analyser.addDecidableClassCheck(new AtomicHypothesisCheck());
			analyser.addDecidableClassCheck(new WeaklyAcyclicCheck());
			analyser.addDecidableClassCheck(new FrontierOneCheck());
			analyser.addDecidableClassCheck(new GuardedCheck());
			analyser.addDecidableClassCheck(new FrontierGuardedCheck());
			analyser.addDecidableClassCheck(new DomainRestrictedCheck());
			analyser.addDecidableClassCheck(new StickyCheck());
			analyser.addDecidableClassCheck(new WeaklyStickyCheck());

			System.out.println("\n\n[3] DIAGNOSTIC\n");
			analyser.process();
			System.out.println(analyser.diagnostic());

			if (args.length >= 2)
				grd.toPostScript(args[1]);
			if (args.length >= 3)
				grd.sccToPostScript(args[2]);
		}
		catch (Exception e) {
			System.out.println(e);
			e.printStackTrace();
		}
	}
};

