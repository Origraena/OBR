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

			String s = "salut";
			String[] s2 = s.split("<");
			String[] s3 = s.split("v");
			System.out.println(s2.length);
			System.out.println(s3.length);
			for (String sss : s2)
				System.out.println(sss);
			for (String sss : s3)
				System.out.println(sss);


			Atom a1 = new AtomArrayList(p);
			a1.set(0,"x");
			a1.set(1,"y");
			Atom a2 = new AtomArrayList("q<'a'>");
			Atom a3 = new AtomArrayList(a1);
			Atom a4 = new AtomArrayList("p<x,y>");
			System.out.println(a1);
			System.out.println(a2);
			System.out.println(a3);
			System.out.println(a4);

		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
};

