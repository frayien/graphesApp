package graphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main
{
	enum Algo
	{
		GREEDY,
		RANDOM,
		WELSH_POWELL,
		DSATUR
	}
	
	public static void main(String[] args)  
	{
		boolean no_gui = false;
		boolean is_bin = false;
		String file_name = "graphs/myciel6.col";
		Algo algo = Algo.GREEDY;
		
		for(String str : args)
		{
			if(str.toLowerCase().equals("-no-gui")) no_gui = true;
			else if(str.toLowerCase().equals("-gui")) no_gui = false;
			else if(str.toLowerCase().equals("-bin")) is_bin = true;
			else if(str.toLowerCase().equals("-raw")) is_bin = false;
			
			else if(str.toLowerCase().equals("-greedy")) algo = Algo.GREEDY;
			else if(str.toLowerCase().equals("-random")) algo = Algo.RANDOM;
			else if(str.toLowerCase().equals("-rnd")) algo = Algo.RANDOM;
			else if(str.toLowerCase().equals("-welsh-powell")) algo = Algo.WELSH_POWELL;
			else if(str.toLowerCase().equals("-wp")) algo = Algo.WELSH_POWELL;
			else if(str.toLowerCase().equals("-dsatur")) algo = Algo.DSATUR;
			else file_name = str;
		}
		
		Graph g = null;
		if(is_bin) g = Graph.loadFromBinFile(file_name);
		else g = Graph.loadFromASCIIFile(file_name);
		
		//Graph g = Graph.loadFromASCIIFile("graphs/myciel6.col");
		//Graph g = Graph.loadFromBinFile("graphs/flat1000_76_0.col.b");
		//System.out.println(g);
		//System.out.println("");
		//System.out.println(g1);
		
		if(g == null) return;
		
		long time = System.currentTimeMillis();
		
		int[] coloration = null;
		
		switch(algo)
		{
		case GREEDY:
			coloration = greedy_algorithm(g);
			break;
		case RANDOM:
			coloration = random_algorithm(g);
			break;
		case WELSH_POWELL:
			coloration = welsh_powell_algorithm(g);
			break;
		case DSATUR:
			coloration = DSATUR_algorithm(g);
			break;
		}
		
		//greedy_algorithm
		//random_algorithm
		//welsh_powell_algorithm
		//DSATUR_algorithm
		System.out.println("Temps d'execution : " + (System.currentTimeMillis() - time) + "ms");
		System.out.println("Algorithme : " + algo.toString().toLowerCase());
		System.out.println("Fichier : " + file_name);
		System.out.println("Nombre de noeuds : " + g.getNodeCount());
		System.out.println("Nombre de liens : " + g.getEdgeCount());
		int max = -1;
		for(int c : coloration) if(c>max) max = c;
		System.out.println("Coloration : " + (max+1) +" couleurs");
		
		if(!no_gui)
		{
			if(max < JUNGWindowManager.colorcode.length)
			{
				JUNGWindowManager jwm = new JUNGWindowManager();
				jwm.load(g, coloration);
				jwm.run();
			}
			else
			{
				System.out.println("Il y a trop de couleurs pour afficher le graphe");
			}
		}
		
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