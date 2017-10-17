
public class Sink extends Actor
{
	Maschine assignedMaschine;
	int numItems;
	
	public Sink()
	{
		this.sim = Simulation.getSimulation();
		sim.sinks.add(this);
		sim.actors.add(this);
	}


	@Override
	public boolean isFull()
	{
		return false;
	}

	@Override
	public boolean isEmpty()
	{
		return true;
	}

	@Override
	public boolean addItem(Item i)
	{
		numItems++;
		sim.addCash(i.getValue());
		
		Item.itemData.get(i.type).addDataFinishedItem(i);
		
		sim.getOrderbook().removeItem(i);
		i = null;
		return true;
	}

	@Override
	public boolean remItem(Item i)
	{
		return false;
	}
	
	@Override
	public String toString()
	{
		return "SINK: Received items: " + numItems;
	}

	@Override
	public void sendItems()
	{
		throw new Error("Sinks does not send Items");
		
	}

	@Override
	public void work()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateStatistics()
	{
		// TODO Auto-generated method stub
		
	}


	
}
