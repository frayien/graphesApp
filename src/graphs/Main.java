package graphs;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class Main
{
	public static Random RANDOM_GENERATOR = new Random();
	
	enum Algo
	{
		GREEDY,
		WELSH_POWELL,
		DSATUR
	}
	
	enum Ordre
	{
		ASC,
		DESC,
		RND
	}
	
	private static Ordre ordre = Ordre.DESC;
	
	public static void main(String[] args)  
	{
		boolean no_gui = false;
		boolean is_bin = false;
		String file_name = "graphs/myciel6.col";
		Algo algo = Algo.GREEDY;
		
		for(String str : args)
		{
			if(str.toLowerCase().equals("-nogui")) no_gui = true;
			else if(str.toLowerCase().equals("-gui")) no_gui = false;
			else if(str.toLowerCase().equals("-bin")) is_bin = true;
			else if(str.toLowerCase().equals("-raw")) is_bin = false;
			
			else if(str.toLowerCase().equals("-asc")) ordre = Ordre.ASC;
			else if(str.toLowerCase().equals("-desc")) ordre = Ordre.DESC;
			else if(str.toLowerCase().equals("-random")) ordre = Ordre.RND;
			else if(str.toLowerCase().equals("-rnd")) ordre = Ordre.RND;
			
			else if(str.toLowerCase().equals("-greedy")) algo = Algo.GREEDY;
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
		System.out.println("Nombre de sommets : " + g.getNodeCount());
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
		//init
		int[] coloration = new int[g.getNodeCount()];
		Arrays.fill(coloration, -1);
		
		//ordre
		Integer[] order = new Integer[g.getNodeCount()];
		Arrays.setAll(order, p -> p);
		//ordonne les noeuds dans l'odre defini par ordre
		switch(ordre)
		{
		case ASC:
			Arrays.sort(order, new Comparator<Integer>() { /*ASC*/ @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o1) - g.getDegreeOf(o2); } });
			break;
		case DESC:
			Arrays.sort(order, new Comparator<Integer>() { /*DESC*/ @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
			break;
		case RND:
			Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  { return RANDOM_GENERATOR.nextInt(2)*2 -1; } });
			break;
		}
		
		for(int i : order)
		{
			//System.out.println(g.getDegreeOf(i));
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
		//ordonne les noeuds dans l'odre defini par ordre
		switch(ordre)
		{
		case ASC:
			Arrays.sort(order, new Comparator<Integer>() { /*ASC*/ @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o1) - g.getDegreeOf(o2); } });
			break;
		case DESC:
			Arrays.sort(order, new Comparator<Integer>() { /*DESC*/ @Override public int compare(Integer o1, Integer o2)  { return g.getDegreeOf(o2) - g.getDegreeOf(o1); } });
			break;
		case RND:
			Arrays.sort(order, new Comparator<Integer>() { @Override public int compare(Integer o1, Integer o2)  { return RANDOM_GENERATOR.nextInt(2)*2 -1; } });
			break;
		}
		
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
		//init
		int[] coloration = new int[g.getNodeCount()];
		Arrays.fill(coloration, -1);
		
		//init desat
		int[] dsat = new int[g.getNodeCount()];
		Arrays.fill(dsat, 0);
		
		//init order dans l'ordre descroissant
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