import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.List;

public class Simulation implements Runnable
{
	// User specific
	int periodsPerMillisecond = 0000;
	boolean useGUI = false;

	// General
	private static Simulation singleton = null;
	private int time = 1;
	private double cash = 0;
	private Orderbook orders;
	List<Actor> actors;
	List<Source> sources = new ArrayList<Source>();
	List<Stock> stocks = new ArrayList<Stock>();
	List<Maschine> machines = new ArrayList<Maschine>();
	List<Sink> sinks = new ArrayList<Sink>();
	boolean backorderFeeConsidered = false;
	Integer maxTime;

	// Graphical Attributes
	GUI gui;
	Screen screen;
	BufferStrategy bs;
	Graphics g;
	int SCREEN_WIDTH = 640, SCREEN_HEIGHT = 640;
	Level level;

	// Constructor Singleton Pattern
	private Simulation()
	{
		actors = new ArrayList<Actor>();
		orders = new Orderbook(this);
	}

	@Override
	public void run()
	{

		resetSimSetting();

		// GUI TESTING
		if (useGUI)
		{
			gui = new GUI(this);
			screen = new Screen("JobShop", 640, 640, this);
			TileSet tileSet = new TileSet("/rpg.png", 12, 12);
			level = new Level("/Level1.txt", tileSet);
			while (true)
				render();
		}
		else
			calculateTillMax();
	}

	void resetSimSetting()
	{
		time = 1;
		cash = 0;
		orders = new Orderbook(this);
		actors = new ArrayList<Actor>();
		Item.itemId = 0;

		Source src = new Source(2, true, FindNextTargetPolicy.evenDistributed, FindNextItemPolicy.FIFO);
		Stock st = new Stock(100, 0, FindNextTargetPolicy.evenDistributed, FindNextItemPolicy.MinSetupTime);
		Maschine m = new Maschine(2);
		Maschine m2 = new Maschine(2);
		Sink snk = new Sink();

		src.assignTarget(st);
		st.assignTarget(m);
		st.assignTarget(m2);
		m.assignTarget(snk);
		m2.assignTarget(snk);

		maxTime = 20;

	}

	void calculatePeriod()
	{
		// Spawn with List Source
		for (Source s : sources)
			s.work();

		// Transportation Phase with all Actors
		transportationPhase();

		// Working phase with Machines
		for (Maschine m : machines)
			m.work();

		// Statistic phase with all Actors
		for (Actor a : actors)
		{
			a.calculateStatistics();
		}

		if (backorderFeeConsidered)
		{
			orders.checkBackorders();
		}

		printPeriod();
		time++;
	}

	void calculateTillMax()
	{
		while (true)
		{

			// At the end, print ItemData
			if (maxTime != null && time > maxTime)
			{
				Item.itemData.forEach(x -> System.out.println(x.toString()));
				break;
			}

			calculatePeriod();

			try
			{
				Thread.sleep(periodsPerMillisecond);
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}
	}

	void transportationPhase()
	{

		boolean sentItems = true;
		while (sentItems)
		{
			sentItems = false;
			for (Actor a : actors)
			{
				if (a.hasTargetThatCanReceiveItem() && !a.isEmpty())
				{
					a.sendItems();
					sentItems = true;
				}
			}

		}

	}

	void printPeriod()
	{
		System.out.println("End of period: " + (time) + " Cash: " + cash);
		orders.printOrderbook();
		for (Actor a : actors)
		{
			System.out.println(a.toString());
		}
		System.out.println();

	}

	void render()
	{
		Canvas c = screen.getCanvas();
		bs = c.getBufferStrategy();
		if (bs == null)
		{
			screen.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		// Clear Screen
		g.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		level.renderMap(g);
		bs.show();
		g.dispose();
	}

	public double getCash()
	{
		return cash;
	}

	public Orderbook getOrderbook()
	{
		return orders;
	}

	public void addCash(double change)
	{
		cash += change;
	}

	public void remCash(double change)
	{
		cash -= change;
	}

	public int getCurTime()
	{
		return time;
	}

	public static Simulation getSimulation()
	{
		if (singleton == null)
		{
			singleton = new Simulation();
			return singleton;
		}
		else
			return singleton;
	}

}
