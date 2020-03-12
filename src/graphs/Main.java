package graphs;

public class Main
{
	public static void main(String[] args)  
	{
		Graph g1 = Graph.loadFromASCIIFile("graphs/flat1000_76_0.col");
		Graph g = Graph.loadFromBinFile("graphs/flat1000_76_0.col.b");
		//System.out.println(g);
		//System.out.println("");
		//System.out.println(g1);
		System.out.println(g.getEdgeCount());
		System.out.println(g1.getEdgeCount());
		System.out.println(g.equals(g1));
		
		//JUNGWindowManager jwm = new JUNGWindowManager();
		//jwm.load(g1);
		//jwm.run();
	}
}