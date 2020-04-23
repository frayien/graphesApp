package graphs;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.ArrayList;

import org.jsfml.graphics.Color;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.PrimitiveType;
import org.jsfml.graphics.RenderWindow;
import org.jsfml.graphics.Text;
import org.jsfml.graphics.VertexArray;
import org.jsfml.window.ContextActivationException;
import org.jsfml.window.VideoMode;
import org.jsfml.window.event.Event;

/**
 * 
 * Ancienne classe de rendu utilisant JSFML
 * 
 * @author garbani
 *
 */

@Deprecated
public class SFMLWindowManager
{
	private RenderWindow window;
	private Thread thread;
	
	private Font font;
	private ArrayList<Text> texts = new ArrayList<>();
	private VertexArray plot = new VertexArray(PrimitiveType.LINE_STRIP);
	
	public SFMLWindowManager()
	{
		font = new Font();
		try { font.loadFromFile(FileSystems.getDefault().getPath("Roboto-Medium.ttf")); } catch (IOException e1) { e1.printStackTrace(); }
		
		window = new RenderWindow(new VideoMode(1200,800), "Graph");
		window.setFramerateLimit(60);
		try { window.setActive(false); } catch (ContextActivationException e) { e.printStackTrace(); }
	}
	
	public void run()
	{
		thread = new Thread(() -> displayThread());
		thread.setDaemon(true);
		thread.start();
		
		eventThread();
	}
	
	public void eventThread()
	{
		while(window.isOpen())
		{
			Event e = window.waitEvent();
			switch(e.type) 
			{
			case CLOSED:
				window.close();
				break;
			default:
				break;
			}
		}
	}
	
	public void displayThread()
	{
		try { window.setActive(true); } catch (ContextActivationException e) { e.printStackTrace(); }
		
		while(window.isOpen())
		{
			window.clear(Color.WHITE);

			window.draw(plot);
			for(Text t : texts)
				window.draw(t);
			
			window.display();
		}
	}
	
	public void load()
	{
		
	}
}
