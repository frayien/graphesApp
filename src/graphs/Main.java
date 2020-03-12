package graphs;

public class Main
{
	public static void main(String[] args)  
	{
		Graph g = Graph.loadFromFile("graphs/myciel3.col");
		
		JUNGWindowManager jwm = new JUNGWindowManager();
		jwm.load(g);
		jwm.run();
	}
}