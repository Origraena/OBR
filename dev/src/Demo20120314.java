import moca.graphs.*;
import moca.graphs.vertices.*;
import moca.graphs.edges.*;
import java.lang.Exception;
import java.util.Iterator;
import java.util.ArrayList;
import obr.*;

public class Demo20120314 {
	public static void main(String args[]) {
		try {
			System.out.println("Reading file : "+args[0]);
			GraphRuleDependencies grd = new GraphRuleDependencies(args[0]);

			System.out.println(grd);
		
			System.out.println(grd.stronglyConnectedComponentsToString());

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
};

