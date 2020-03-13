package graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class Graph 
{
	public static class ReadException extends Exception { public ReadException(String str) { super(str); } }
	
	private List<Integer> adj_list[];
	private int node_count;
	
	public Graph(int node_count)
	{
		this.node_count = node_count;
		adj_list = new LinkedList[node_count];
		
		for(int i = 0; i<node_count; i++)
			adj_list[i] = new LinkedList<>();
	}
	
	public static Graph loadFromASCIIFile(String path)
	{
		Graph graph = null;
        BufferedReader br = null;
        try 
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            for (String line = br.readLine(); line != null; line = br.readLine()) 
            {
            	line.toLowerCase();
            	String[] cmd = line.split("\\s");
            	switch(cmd[0])
            	{
            	case "p":
            		if(cmd[1].equals("edge"))
            			graph = new Graph(Integer.parseInt(cmd[2]));
            		break;
            	case "e":
            		graph.addEdge(Integer.parseInt(cmd[1])-1,Integer.parseInt(cmd[2])-1);
            		graph.addEdge(Integer.parseInt(cmd[2])-1,Integer.parseInt(cmd[1])-1);
            		break;
            	default: break;
            	}
            }
        } 
        catch (FileNotFoundException e) { e.printStackTrace(); } 
        catch (IOException e) {  e.printStackTrace(); } 
        catch(NumberFormatException e) { e.printStackTrace(); }
        finally { try { br.close(); } catch (IOException e) { } catch (NullPointerException e) { } }
        
		return graph;
	}
	
	public static Graph loadFromBinFile(String path)
	{
		GraphLoader gl = new GraphLoader();
		gl.loadFromBinFile(path);
		return gl.getGraph();
	}
	
	public int getNodeCount()
	{
		return node_count;
	}
	
	public int getDegreeOf(int node)
	{
		return adj_list[node].size();
	}
	
	public List<Integer> getEdgesFrom(int node)
	{
		return adj_list[node];
	}
	
	public int getEdgeCount()
	{
		int tot = 0;
		
		for(List<Integer> l : adj_list)
		{
			tot += l.size();
		}
		
		return tot;
	}
	
	public void addEdge(int from, int to)
	{
		if(!adj_list[from].contains(to))
			adj_list[from].add(to);
	}
	
	@Override
	public String toString()
	{
		StringBuffer str = new StringBuffer();
		str.append("nodes " + node_count + "\n");
		for(int i = 0; i<node_count; i++)
		{
			for(int j : adj_list[i])
			{
				str.append(i + " " + j + "\n");
			}
		}
		return str.toString();
	}
	
	public boolean equals(Graph other)
	{
		if(getNodeCount() != other.getNodeCount()) return false;
		if(getEdgeCount() != other.getEdgeCount()) return false;
		for(int i = 0; i<node_count; i++)
		{
			if(! (getEdgesFrom(i).containsAll(other.getEdgesFrom(i)) && other.getEdgesFrom(i).containsAll(getEdgesFrom(i)))) return false;
		}
		return true;
	}
}
