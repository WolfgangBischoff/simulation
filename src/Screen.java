import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

// Class for a general Screen

public class Screen
{
	String title;
	int height;
	int width;
	Simulation sim;
	
	private JFrame frame;
	private Canvas canvas;
	
	public Screen(String name, int height, int width, Simulation sim)
	{
		this.title = name;
		this.height = height;
		this.width = width;
		this.sim = sim;
		
		//Das Fenster
		frame = new JFrame(title);
	    frame.setSize(width, height);
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);
	    frame.setLocationRelativeTo(null);
	    frame.setVisible(true);
		
	    //The picture in the Screen
	    canvas = new Canvas();
	    canvas.setPreferredSize(new Dimension(width, height));
	    canvas.setMaximumSize(new Dimension(width, height));
	    canvas.setMinimumSize(new Dimension(width, height));
	    canvas.setFocusable(false);

	    frame.add(canvas);
	    frame.pack();
	  }

	  public Canvas getCanvas(){
	    return canvas;
	  }

	  public JFrame getFrame(){
	    return frame;
	  }
	}
	


