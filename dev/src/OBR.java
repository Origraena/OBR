import moca.graphs.*;
import moca.graphs.vertices.*;
import moca.graphs.edges.*;
import java.lang.Exception;
import java.util.Iterator;
import obr.*;

public class OBR {
	public static void main(String args[]) {
		try {
			Predicate p = new Predicate("p/2");
			Predicate q = new Predicate("q",1);
			Predicate p2 = new Predicate(p);
			Predicate q2 = new Predicate(q.toString());
			System.out.println(p);
			System.out.println(p2);
			System.out.println(q);
			System.out.println(q2);

			GraphAtomConjunction gac = new GraphAtomConjunction("p<x,y>;q<z>;q<x>");

			AtomicRule r = new AtomicRule("p<x,y>;q<z>-->r<x,y,x>");

			
			System.out.println("nb atoms = " +gac.getNbAtoms()+" nb terms = "+gac.getNbTerms());
			System.out.println(gac);
			System.out.println(r);
			System.out.println("nb atoms = " +r.getNbAtoms()+" nb terms = "+r.getNbTerms());

			for (int i = r.getNbAtoms() ; i < 6 ; i++)
			System.out.println("vertex["+i+"]"+r.isExistential(r.getVertex(i))+"  "+r.isHead(r.getVertex(i)));

			GraphAtomConjunction gac2 = gac.clone();
			gac2.getVertex(0).setValue(new Object());
			System.out.println(gac.getVertex(0));
			System.out.println(gac2.getVertex(0));
			
			AtomicRule r2 = new AtomicRule("r<a,b,c>-->p<x,y>");
			GraphAtomConjunction h1 = new GraphAtomConjunction("p<x,y>;q<z>");
			GraphAtomConjunction h2 = new GraphAtomConjunction("r<a,b,c>");
			GraphAtomConjunction h3 = new GraphAtomConjunction("p<x,'a'>;p<y,'b'>");

			System.out.println("R1 = "+r);
			System.out.println("R2 = "+r2);
			System.out.println("H3 = "+h3);
			System.out.println("R1 --> R2 ? " + AtomicRule.localUnification(h2,r,r.existentialIndex()));
			System.out.println("R2 --> R1 ? " + AtomicRule.localUnification(h1,r2,r2.existentialIndex()));
			System.out.println("R2 --> R3 ? " + AtomicRule.localUnification(h3,r2,r2.existentialIndex()));

			//GraphRuleDependencies grd = new GraphRuleDependencies("../basetest");
			System.out.println(" ");
			AtomicRule R1 = new AtomicRule("p<x,y>;q<y>-->r<x,y,z>");
			AtomicRule R2 = new AtomicRule("r<x,y,z>;r<x,y,x>-->p<x,x>");
			AtomicRule R3 = new AtomicRule("r<'a','b',x>-->r<x,x,y>");
			AtomicRule R4 = new AtomicRule("p<x,y>;p<'a',y>-->p<'a',z>");
/*			System.out.println("R1 : "+R1);
			System.out.println("R2 : "+R2);
			System.out.println("R3 : "+R3);
			System.out.println("R4 : "+R4);

			System.out.println("R1");
			System.out.println("R1 --> R1 ? "+R1.mayImply(R1));
			System.out.println("R1 --> R2 ? "+R1.mayImply(R2));
			System.out.println("R1 --> R3 ? "+R1.mayImply(R3));
			System.out.println("R1 --> R4 ? "+R1.mayImply(R4));
			System.out.println("");

			System.out.println("R2");
			System.out.println("R2 --> R1 ? "+R2.mayImply(R1));
			System.out.println("R2 --> R2 ? "+R2.mayImply(R2));
			System.out.println("R2 --> R3 ? "+R2.mayImply(R3));
			System.out.println("R2 --> R4 ? "+R2.mayImply(R4));
			System.out.println("");
			
			System.out.println("R3");
			System.out.println("R3 --> R1 ? "+R3.mayImply(R1));
			System.out.println("R3 --> R2 ? "+R3.mayImply(R2));
			System.out.println("R3 --> R3 ? "+R3.mayImply(R3));
			System.out.println("R3 --> R4 ? "+R3.mayImply(R4));
			System.out.println("");

			System.out.println("R4");
			System.out.println("R4 --> R1 ? "+R4.mayImply(R1));
			System.out.println("R4 --> R2 ? "+R4.mayImply(R2));
			System.out.println("R4 --> R3 ? "+R4.mayImply(R3));
			System.out.println("R4 --> R4 ? "+R4.mayImply(R4));
*/
			GraphRuleDependencies grd = new GraphRuleDependencies();
			grd.add(R1);
			grd.add(R2);
			grd.add(R3);
			grd.add(R4);
			System.out.println(grd);

			GraphRuleDependencies grd2 = new GraphRuleDependencies();
			grd2.fromFile("basetest");
			System.out.println(grd2);

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
};

