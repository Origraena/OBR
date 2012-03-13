import moca.graphs.*;
import moca.graphs.vertices.*;
import moca.graphs.edges.*;
import java.lang.Exception;
import java.util.Iterator;
import obr.*;

public class Demo20120314 {
	public static void main(String args[]) {
		try {
			GraphRuleDependencies grd = new GraphRuleDependencies();

			grd.add(new AtomicRule("p<x,y>;q<y>-->r<x,y,z>"));
			grd.add(new AtomicRule("r<x,y,z>;r<x,y,x>-->p<x,x>"));
			grd.add(new AtomicRule("r<'a','b',x>-->r<x,x,y>"));
			grd.add(new AtomicRule("p<x,y>;p<'a',y>-->p<'a',z>"));

			System.out.println(grd);

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
};

