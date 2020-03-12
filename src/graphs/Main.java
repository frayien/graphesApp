package graphs;

public class Main
{
	public static void main(String[] args)  
	{
		//Graph g = Graph.loadFromASCIIFile("graphs/myciel3.col");
		Graph g = Graph.loadFromBinFile("graphs/DSJC125.1.col.b");
		
		JUNGWindowManager jwm = new JUNGWindowManager();
		jwm.load(g);
		jwm.run();
	}
}