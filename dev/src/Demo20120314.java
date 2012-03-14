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
//			grd.add(new AtomicRule("p<x,y>;q<y>-->r<x,y,z>"));
//			grd.add(new AtomicRule("r<x,y,z>;r<x,y,x>-->p<x,x>"));
//			grd.add(new AtomicRule("r<'a','b',x>-->r<x,x,y>"));
//			grd.add(new AtomicRule("p<x,y>;p<'a',y>-->p<'a',z>"));

			System.out.println(grd);
		
/*			for (int i = 0 ; i < grd.getNbComponents() ; i++) {
				System.out.println("i = "+i);
				System.out.println(grd.getComponent(i));
			}
*/

//			DirectedSimpleGraph<ArrayList<Vertex<AtomicRule> >,Boolean> grd_scc = grd.getStronglyConnectedComponentsGraph();
			System.out.println(grd.stronglyConnectedComponentsToString());

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
};

