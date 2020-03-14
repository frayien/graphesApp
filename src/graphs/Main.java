package graphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main
{
	public static void main(String[] args)  
	{
		//Graph g = Graph.loadFromASCIIFile("graphs/myciel6.col");
		Graph g = Graph.loadFromBinFile("graphs/flat1000_76_0.col.b");
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
		
		
		//JUNGWindowManager jwm = new JUNGWindowManager();
		//jwm.load(g, coloration);
		//jwm.run();
	}
	
	public static int getLowestNextColor(Graph g, int[] coloration, int node)
	{
		int color = 0;
		int acolor = 0;
		do
		{
			acolor = color;
			for(int j : g.getEdgesFrom(node))
			{
				if(coloration[j] == color) color++;
			}
		}
		while(acolor != color);
		
		return color;
	}
	
	public static int[] greedy_algorithm(Graph g)
	{
		int[] coloration = new int[g.getNodeCount()];
		Arrays.fill(coloration, -1);
		
		for(int i = 0; i < g.getNodeCount(); i++)
		{
			coloration[i] = getLowestNextColor(g, coloration, i);
		}
		
		return coloration;
	}
	
	public static Random generator = new Random();
	
	public static int[] random_algorithm(Graph g)
	{
		int[] coloration = new int[g.getNodeCount()];
		Arrays.fill(coloration, -1);
		
		Integer[] order = new Integer[g.getNodeCount()];
		Arrays.fill(order, -1);
		for(int i = 0; i<order.length; i++)
		{
			int rng = generator.nextInt(g.getNodeCount()-i);
			int n = 0;
			while(rng != 0)
			{
				if(order[n] == -1) rng--;  
				n++;
			}
			order[n] = i;
		}
		
		for(int i = 0; i < g.getNodeCount(); i++)
		{
			coloration[i] = getLowestNextColor(g, coloration, i);
		}
		
		return coloration;
	}
	
	public static int[] welsh_powell_algorithm(Graph g)
	{
		int[] coloration = new int[g.getNodeCount()];
		Arrays.fill(coloration, -1);
		
		Integer[] order = new Integer[g.getNodeCount()];
		Arrays.setAll(order,p -> p);
		Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
		
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
		Arrays.fill(coloration, -1);
		
		int[] dsat = new int[g.getNodeCount()];
		Arrays.fill(dsat, 0);
		
		Integer[] order = new Integer[g.getNodeCount()];
		Arrays.setAll(order,p -> p);
		Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
		
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
			
			coloration[next] = getLowestNextColor(g, coloration, next);
			nb_colored++;
			for(int i : g.getEdgesFrom(next)) dsat[i]++;
		}
		
		return coloration;
	}
}