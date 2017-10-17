import java.util.ArrayList;

public class Stock extends Actor
{
	//Static
	static int nextStorageNumber = 1;
	
	// General
	int storageNumber;
	int storageCap;

	// Statistics
	int numberObservedPeriods = 10;
	ContiniousEventContainer stat = new ContiniousEventContainer(numberObservedPeriods);

	// Constructor
	public Stock(int capacity)
	{
		this.sim = Simulation.getSimulation();
		sim.stocks.add(this);
		sim.actors.add(this);
		storageNumber = nextStorageNumber++;
		
		this.storageCap = capacity;
		storedItems = new ArrayList<Item>();
	}

	public Stock(int capacity, int initStock)
	{
		this(capacity);
		for (int i = 0; i < initStock; i++)
		{
			storedItems.add(new Item(1, 0, sim.getCurTime() + 4));
		}
	}

	public Stock(int capacity, int initStock, FindNextTargetPolicy nextTargetPolicy, FindNextItemPolicy nextItemPolicy)
	{
		this(capacity, initStock);
		this.nextItemPolicy = nextItemPolicy;
		this.nextTargetPolicy = nextTargetPolicy;
	}



	public String toString()
	{
		return "STORAGE " + storageNumber + ": " + storedItems.size() + "/" + storageCap + " Items stored" + " Avg Stock: "
				+ Utils.round(stat.getAvg(), 2);
	}

	public boolean isFull()
	{
		if (storedItems.size() >= storageCap)
			return true;
		else
			return false;
	}

	public void computeAvgStock()
	{
		stat.addData(storedItems.size());
	}

	@Override
	public boolean addItem(Item i)
	{
		if (!isFull())
			return storedItems.add(i);
		else
			return false;
	}

	@Override
	public boolean remItem(Item i)
	{
		return storedItems.remove(i);
	}


	@Override
	public void work()
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void calculateStatistics()
	{
		computeAvgStock();
	}

}
