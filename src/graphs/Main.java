package graphs;

import java.util.Arrays;
import java.util.Comparator;

public class Main
{
	public static void main(String[] args)  
	{
		Graph g = Graph.loadFromASCIIFile("graphs/myciel3.col");
		//Graph g = Graph.loadFromBinFile("graphs/flat1000_76_0.col.b");
		//System.out.println(g);
		//System.out.println("");
		//System.out.println(g1);
		
		int[] coloration = welshPowellAlgorithm(g);
		
		
		JUNGWindowManager jwm = new JUNGWindowManager();
		jwm.load(g, coloration);
		jwm.run();
	}
	
	public static int[] welshPowellAlgorithm(Graph g)
	{
		int[] coloration = new int[g.getNodeCount()];
		for(int i = 0; i<coloration.length; i++) coloration[i] = -1;
		Integer[] order = new Integer[g.getNodeCount()];
		for(int i = 0; i<order.length; i++) order[i] = i;
		Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  {
				return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
		int nb_colored = 0;
		int color = 0;
		while(nb_colored < order.length)
		{
			for(int i : order)
			{
				if(coloration[i] == -1)
				{
					coloration[i] = color;
					for(int adj : g.getEdgesFrom(i))
					{
						if(coloration[adj] == color) coloration[i] = -1;
					}
					if(coloration[i] != -1) nb_colored++;
				}
			}
			color++;
		}
		
		return coloration;
	}
}