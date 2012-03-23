package obr;

import moca.graphs.DirectedSimpleGraph;
import moca.graphs.IllegalConstructionException;
import moca.graphs.vertices.Vertex;
import moca.graphs.edges.IllegalEdgeException;
import moca.graphs.edges.NeighbourEdge;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;
import java.util.NoSuchElementException;


/**
 * Represents the graph of position dependencies corresponding to a GRD or to one of its strongly connected components.<br />
 * The graph maintains a hash map to find quickly the vertex corresponding to a predicate and its position.
 * It is used to check the weakly-acyclic and the weakly-sticky properties.
 */
public class GraphPositionDependencies extends DirectedSimpleGraph<Predicate,Boolean> {

	/**
	 * Default constructor.
	 */
	public GraphPositionDependencies() throws IllegalConstructionException {
		super();
	}

	/**
	 * Constructor from a set of rules.
	 * @param rules The set of rule to convert.
	 */
	public GraphPositionDependencies(Iterable<Vertex<AtomicRule> > rules) throws IllegalConstructionException {
		super();
		init(rules);
	}
	
	/**
	 * Adds a predicate into the graph if not already in.
	 * Note that the number of created vertices is equals to the predicate arity.
	 * @param p The predicate to be added.
	 */
	public void addPredicate(Predicate p) {
		if (!_predicateIndex.containsKey(p)) {
			_predicateIndex.put(p,getNbVertices());
			for (int i = 0 ; i < p.getArity() ; i++)
				addVertex(p);
		}
	}
	
	/**
	 * Convenient method to add a non special edge between two predicate positions.
	 * (from the first to the second)
	 * @param p The first predicate.
	 * @param positionP The first position (it must be lesser than p arity).
	 * @param q The second predicate.
	 * @param positionQ The second position (it mus be lesser than q arity).
	 */
public void addEdge(Predicate p, int positionP, Predicate q, int positionQ) {
		addEdge(p,positionP,q,positionQ,false);
	}

	/**
	 * Convenient method to add a non special edge between two predicate positions.
	 * (from the first to the second)
	 * @param p The first predicate.
	 * @param positionP The first position (it must be lesser than p arity).
	 * @param q The second predicate.
	 * @param positionQ The second position (it mus be lesser than q arity).
	 * @param special The edge value, if true it will be a special edge.
	 */
	public void addEdge(Predicate p, int positionP, Predicate q, int positionQ, Boolean special) {
		try {
			addEdge(getVertex(p,positionP),getVertex(q,positionQ),special);
		}
		catch (IllegalEdgeException e) {
			// if an edge already exists its value should be checked against the special parameter.
		}
	}

	/**
	 * Returns the vertex associated to a predicate position.
	 * Uses an internal hash map to provide fast access.
	 * @param p The predicate to be found.
	 * @param position The position inside the predicate.
	 * @throws NoSuchElementException Whenever a predicate does not belong to the graph, or if the position is greater than predicate arity.
	 */
	public Vertex<Predicate> getVertex(Predicate p, int position) throws NoSuchElementException {
		if (position >= p.getArity())
			throw new NoSuchElementException();
		Integer pIndex = _predicateIndex.get(p);
		if (pIndex == null) 
			throw new NoSuchElementException();
		return getVertex(pIndex+position);
	}

	/**
	 * Used to check the weakly acyclic property.
	 * @return True if there is no position with infinite rank, i.e. if it is weakly acyclic.
	 */
	public boolean finiteRank() {
		if (_finiteRanks != null) {
			for (int i = 0 ; i < getNbComponents() ; i++)
				if (!_finiteRanks[i])
					return false;
		}
		if (isAcyclic())
			return true;
		boolean success = true;
		ArrayList<Vertex<Predicate> > scc = null;
		for (int i = 0 ; (i < getNbComponents()) && success ; i++) {
			scc = getComponent(i);
			// NOT OPTIMIZED TODO
			for (int j = 0 ; (j < scc.size()) && success ; j++) {
				for (int k = 0 ; (k < scc.size()) && success ; k++) {
					try {
						if (getEdgeValue(scc.get(j).getID(),scc.get(k).getID()) == true)
							success = false;
					}
					catch (NoSuchElementException e) {
						// the edge does not exist, 
						// in particular there is no special edge
					}
				}
			}
		}
		return success;
	}

	/**
	 * Used to check the weakly sticky property.
	 * @param vertexID The vertex to be checked.
	 * @return True if the position corresponding to the vertex has a finite rank.
	 */
	public boolean finiteRank(int vertexID) {
		if (_finiteRanks == null) {
			if (isAcyclic())
				return true;
			processFiniteRanks();
		}
		return _finiteRanks[vertexID];
	}

	/**
	 * Convenient method for finiteRank(vertexID).
	 * @see #finiteRank(int)
	 */
	public boolean finiteRank(Predicate p, int position) {
		return finiteRank(getComponentID(getVertex(p,position).getID()));
	}

	/**
	 * Internal method which process the predicate positions ranks.
	 * Will only be called by the finiteRank(int vertexID) method.
	 */
	protected void processFiniteRanks() {
		_finiteRanks = new boolean[getNbComponents()];
		ArrayList<Vertex<Predicate> > scc = null;
		for (int i = 0 ; i < getNbComponents() ; i++) {
			scc = getComponent(i);
			_finiteRanks[i] = true;
			// NOT OPTIMIZED TODO
			for (int j = 0 ; (j < scc.size()) && _finiteRanks[i] ; j++) {
				for (int k = 0 ; (k < scc.size()) && _finiteRanks[i] ; k++) {
					try {
						if (getEdgeValue(scc.get(j).getID(),scc.get(k).getID()) == true)
							_finiteRanks[i] = false;
					}
					catch (NoSuchElementException e) { }
				}
			}
		}
	}

	/**
	 * Converts a vertex to the predicate position string.
	 * @param vertexID The id of the vertex to be converted.
	 * @return A string of form "p[i]"
	 */
	public String predicatePositionToString(int vertexID) {
		Predicate p = get(vertexID);
		int position = 1;
		boolean end = false;
		while ((vertexID - position >= 0) && (!end)) {
			if (p.compareTo(get(vertexID-position)) == 0)
				position++;
			else
				end = true;
		}
		position--;
		return p.getLabel() + "[" + position + "]";
	}

	/**
	 * Converts the full graph into a string.<br />
	 * It will first contain the vertices values, and then the edges.
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		NeighbourEdge<Boolean> edge = null;

		// vertices
		for (int i = 0 ; i < getNbVertices() ; i++) {
			result.append(i);
			result.append(" : ");
			result.append(predicatePositionToString(i));
			result.append('\n');
		}
		result.append('\n');
		
		/*while (predicateIndex < getNbVertices()) {
			positionIndex = 0;
			while (positionIndex < get(predicateIndex).getArity()) {
				result.append(predicateIndex + positionIndex);
				result.append(" : ");
				result.append(get(predicateIndex).getLabel());
				result.append("[");
				result.append(positionIndex);
				result.append("]");
				result.append('\n');
				positionIndex++;
			}
			predicateIndex += positionIndex;
		}*/
	
		// edges
		for (int i = 0 ; i < getNbVertices() ; i++) {
			for (Iterator<NeighbourEdge<Boolean> > iterator = neighbourIterator(i) ; iterator.hasNext() ;) {
				edge = iterator.next();
				result.append(predicatePositionToString(i));
				if (edge.getValue() == true)
					result.append("\tx->\t");
				else
					result.append("\t-->\t");
				result.append(predicatePositionToString(edge.getIDV()));
				result.append('\n');
			}
		}
		return result.toString();
	}

	/**
	 * Fullfills the graph from the set of rules.
	 * @param rules The set of rule.
	 */
	protected void init(Iterable<Vertex<AtomicRule> > rules) {

		// vertices
		for (Vertex<AtomicRule> rule : rules) {
			for (int i = 0 ; i < rule.getValue().getNbAtoms() ; i++)
				addPredicate(rule.getValue().getPredicate(i));
		}

		// edges
		Vertex<Object> headVertex = null;
		AtomicRule rule = null;
		Term neighbour = null;
		Vertex<Object> vertex = null;
		NeighbourEdge<Integer> edge = null;
		NeighbourEdge<Integer> edge2 = null;
		NeighbourEdge<Integer> edge3 = null;
		Predicate p,q;
		Vertex<Predicate> ri = null;
		Vertex<Predicate> sj = null;
		int positionP, positionQ;
		for (Vertex<AtomicRule> vertexRule : rules) {
			rule = vertexRule.getValue();
			headVertex = rule.getHead();
			for (Iterator<NeighbourEdge<Integer> > neighbourIterator = rule.neighbourIterator(headVertex.getID()) ;
				neighbourIterator.hasNext() ;) {
				edge = neighbourIterator.next();
				neighbour = rule.getTerm(edge.getIDV()-rule.getNbAtoms());
				if (rule.isUniversal(rule.getVertex(edge.getIDV()))) {
					for (Iterator<NeighbourEdge<Integer> > neighbourIterator2 = rule.neighbourIterator(edge.getIDV()) ;
						neighbourIterator2.hasNext() ;) {
						edge2 = neighbourIterator2.next();
						vertex = rule.getVertex(edge2.getIDV());
						if (rule.isBody(vertex)) {
							p = (Predicate)(vertex.getValue());
							positionP = edge2.getValue();
							ri = getVertex(p,positionP);
							for (Iterator<NeighbourEdge<Integer> > neighbourIterator3 = rule.neighbourIterator(edge.getIDV()) ; neighbourIterator3.hasNext() ; ) {
								edge3 = neighbourIterator3.next();
								if (rule.isHead(rule.getVertex(edge3.getIDV()))) {
									q = rule.getPredicate(edge3.getIDV());
									positionQ = edge3.getValue();
									addEdge(p,positionP,q,positionQ);
								}
							}
							for (Iterator<NeighbourEdge<Integer> > neighbourIterator3 = rule.neighbourIterator(headVertex.getID()) ; neighbourIterator3.hasNext() ;) {	
								edge3 = neighbourIterator3.next();
								if (rule.isExistential(rule.getVertex(edge3.getIDV()))) {
									q = (Predicate)(headVertex.getValue());
									positionQ = edge3.getValue();
									sj = getVertex(q,positionQ);
									if (isEdge(ri.getID(),sj.getID()))
										removeEdge(ri.getID(),sj.getID());
									addEdge(p,positionP,q,positionQ,true);
								}
							}
						}
					}
				}
			}
		}
	}


	/** Internal hash map used to access to the first vertex corresponding to the predicate. */
	private HashMap<Predicate,Integer> _predicateIndex = new HashMap<Predicate,Integer>();
	/** Array of same length as the number of components, and valuing true if the scc has a finite rank. */
	private boolean _finiteRanks[] = null;

};

