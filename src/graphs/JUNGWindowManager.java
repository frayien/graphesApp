package graphs;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Stroke;

import javax.swing.JFrame;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class JUNGWindowManager 
{
	private Graph<Integer, Integer> g = new SparseMultigraph<>();
	
	public JUNGWindowManager()
	{
		
	}
	
	public void run()
	{ 
		Layout<Integer, Integer> layout = new KKLayout<>(g);
		
		layout.setSize(new Dimension(1000,550)); 
		
		BasicVisualizationServer<Integer,Integer> bvs = new BasicVisualizationServer<>(layout); 
		bvs.setPreferredSize(new Dimension(1050,600));
		bvs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		bvs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
		
		bvs.getRenderContext().setEdgeStrokeTransformer(
				new Transformer<Integer,Stroke>(){  @Override public Stroke transform(Integer i) {
					return new BasicStroke(0.5f); } });
		
		JFrame frame = new JFrame("Simple Graph View");     
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
		frame.getContentPane().add(bvs);      
		frame.pack();     
		frame.setVisible(true); 
	}
	
	private void loadOnlyNodes(graphs.Graph gr)
	{
		for(int i = 0; i<gr.getNodeCount(); i++)
		{
			g.addVertex(i);
		}
	}
	
	private void loadOnlyLinks(graphs.Graph gr)
	{
		int i = 1;
		for(int j = 0; j<gr.getNodeCount(); j++)
		{
			for(int k : gr.getEdgesFrom(j))
			{
				if(j<k) g.addEdge(i++, j, k);
			}
			
		}
	}
	
	public void load(graphs.Graph gr)
	{
		loadOnlyNodes(gr);
		loadOnlyLinks(gr);
	}
}
