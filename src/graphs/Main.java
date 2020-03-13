package graphs;

import java.util.Arrays;
import java.util.Comparator;

public class Main
{
	public static void main(String[] args)  
	{
		Graph g = Graph.loadFromASCIIFile("graphs/myciel6.col");
		//Graph g = Graph.loadFromBinFile("graphs/flat1000_76_0.col.b");
		//System.out.println(g);
		//System.out.println("");
		//System.out.println(g1);
		
		long time = System.currentTimeMillis();
		
		int[] coloration = DSATUR_algorithm(g);
		//int[] coloration = welsh_powell_algorithm(g);
		
		System.out.println(System.currentTimeMillis() - time + "ms");
		int max = -1;
		for(int c : coloration) if(c>max) max = c;
		System.out.println((max+1) +" couleurs");
		
		
		JUNGWindowManager jwm = new JUNGWindowManager();
		jwm.load(g, coloration);
		jwm.run();
	}
	
	public static int[] welsh_powell_algorithm(Graph g)
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
	
	public static int[] DSATUR_algorithm(Graph g)
	{
		int[] coloration = new int[g.getNodeCount()];
		for(int i = 0; i<coloration.length; i++) coloration[i] = -1;
		
		int[] dsat = new int[g.getNodeCount()];
		for(int i = 0; i<dsat.length; i++) dsat[i] = 0;
		
		Integer[] order = new Integer[g.getNodeCount()];
		for(int i = 0; i<order.length; i++) order[i] = i;
		Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  {
				return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
		
		coloration[order[0]] = 0;
		int nb_colored = 1;
		
		for(int i : g.getEdgesFrom(order[0])) dsat[i]++;
		
		while(nb_colored<order.length)
		{
			int next = -1;
			int ndsat = -1;
			for(int i : order)
			{
				if(coloration[i] == -1)
				{
					if(dsat[i] > ndsat) 
					{
						next = i; 
						ndsat = dsat[i]; 
					} 
				}
			}
			
			int color = 0;
			boolean verif = true;
			while(verif)
			{
				verif = false;
				for(int i : g.getEdgesFrom(next)) 
				{
					if(color == coloration[i])
					{
						color++;
						verif = true;
					}
				}
			}
			
			coloration[next] = color;
			nb_colored++;
			for(int i : g.getEdgesFrom(next)) dsat[i]++;
		}
		
		return coloration;
	}
}