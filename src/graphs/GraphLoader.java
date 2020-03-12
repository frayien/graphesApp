package graphs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import graphs.Graph.ReadException;

public class GraphLoader 
{
	private final static int MAX_NR_VERTICES = 5000;
	private final static int MAX_NR_VERTICESdiv8 = 625;

	private int Nr_vert = 0;
	private int Nr_edges = 0;
	private char bitmap[][] = new char[MAX_NR_VERTICES][MAX_NR_VERTICESdiv8];
	
	private final static int MAX_PREAMBLE = 10000;
	
	private char premable[] = new char[MAX_PREAMBLE];
	
	private int length = 0;
	
	private final static char masks[] = { 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80 };
	
	public void loadFromBinFile(String path)
	{
		BufferedReader br = null;
        try 
        {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(path))));
            String line = br.readLine();
            length = Integer.parseInt(line);
            if(length > MAX_PREAMBLE) throw new ReadException("ERROR: Too long preamble.");
            br.read(premable,0,length);
            
            loadPremable();
            
            for (int i = 0; i < Nr_vert; i++ ) 
            	br.read(bitmap[i], 0, (int)((i + 8)/8));      
        } 
        catch (ReadException e) { System.err.println(e.toString()); }
        catch (FileNotFoundException e) { e.printStackTrace(); } 
        catch (IOException e) {  e.printStackTrace(); }
        catch(NumberFormatException e) { System.err.println("ERROR: Corrupted preamble."); e.printStackTrace(); }
        finally { try { br.close(); } catch (IOException e) { } catch (NullPointerException e) { } }
	}
	
	private void loadPremable()
	{
		// lecture du premable, extraction de Nr_Vert et Nr_edges
        int i = 0;
        boolean stop = false;
        while(i < length && !stop)
        {
        	switch(premable[i])
        	{
        	case 'c':
        		while(premable[i++] != '\n');
        		break;
        	case 'p':
        		while(premable[i++] != ' ');
        		while(premable[i++] != ' ');
        		StringBuffer sb = new StringBuffer();
        		while(premable[i] != ' ')
        		{
        			sb.append(premable[i]);
        			i++;
        		}
        		i++;
        		Nr_vert = Integer.parseInt(sb.toString());
        		sb = new StringBuffer();
        		while(premable[i] != '\n')
        		{
        			sb.append(premable[i]);
        			i++;
        		}
        		Nr_edges = Integer.parseInt(sb.toString());
        		stop = true;
        		break;
    		default:
    			break;
        	}
        }
	}
	
	private boolean get_edge(int i, int j)
	{
		int oct, bit;
		char mask;
		
		bit  = 7-(j & 0x00000007);
		oct = j >> 3;
		
		mask = masks[bit];
		return (bitmap[i][oct] & mask) == mask;
	}
	
	public Graph getGraph()
	{
		Graph graph = new Graph(Nr_vert);		
		for (int i = 0; i<Nr_vert; i++ )
		{
			for (int j = 0; j <= i; j++ )
			{
				if ( get_edge(i,j) )
				{
					graph.addEdge(i, j);
					graph.addEdge(j, i);
				}
			}
				
		}
		
		return graph;
	}
}
