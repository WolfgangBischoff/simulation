import java.util.ArrayList;
import java.util.List;

class Item
{
	static int itemId = 0;
	static List<ItemTypeData> itemData = new ArrayList<ItemTypeData>();

	//private Simulation sim;
	int id;
	int type;
	private int timeSpawned;
	private int timeFinished;
	private int leadTime;
	int dueDate;
	private double value = 10;
	private double backorderFee = 2;

	//public Item(Simulation sim, int curTime, int itemType, int dueDate)
	public Item(int curTime, int itemType, int dueDate)
	{
		//this.sim = sim;
		this.id = getNextId();
		timeSpawned = curTime;
		type = itemType;
		this.dueDate = dueDate;
		Simulation.getSimulation().getOrderbook().addItem(this);
		
		feedItemData(this);

	}

	

	private void createItemDataContainer(Item item)
	{
		for(int i = itemData.size() -1; i < item.type;  i++)
		{
			itemData.add(new ItemTypeData());
		}

	}

	private void feedItemData(Item item)
	{
		if (isItemTypeDataExisting(item.type))
			itemData.get(item.type).addDataNewItem();
		else
		{
			createItemDataContainer(item);
			itemData.get(item.type).addDataNewItem();
		}
		//System.out.println("Type: " + type + "Number: " + itemData.get(item.type).numItems);
	}

	private boolean isItemTypeDataExisting(int type)
	{
		return type < itemData.size() && type >= 0;
	}

	public double getBackorderFee()
	{
		return backorderFee;
	}

	public static int getNextId()
	{
		itemId++;
		return itemId;
	}

	public double getValue()
	{
		return value;
	}

	public void setTimeFinished(int timeFinished)
	{
		leadTime = timeFinished - timeSpawned + 1;
		this.timeFinished = timeFinished;
	}

	public String toString()
	{
		return "[ID: " + id + " Type: " + type + " Spawned/Finished " + timeSpawned + "/" + timeFinished + " LeadTime: "
				+ leadTime + "]";
	}
	
	public int getLeadTime()
	{
		return leadTime;
	}

}