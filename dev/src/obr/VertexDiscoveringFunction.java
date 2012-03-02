package obr;

public interface VertexDiscoveringFunction<V> {

	public void exec(Vertex<V> current, Vertex<V> neighbour);

};


