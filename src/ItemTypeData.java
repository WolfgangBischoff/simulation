
public class ItemTypeData
{

	int numConsideredPeriods = 10;
	ContiniousEventContainer avgLeadTime = new ContiniousEventContainer(numConsideredPeriods);
	int numItems = 0;
	int numFinishedItems = 0;
	
	
	public void addDataNewItem()
	{
		numItems++;
	}
	
	public void addDataFinishedItem(Item i)
	{
		numFinishedItems++;
		avgLeadTime.addData(i.getLeadTime());
	}
	
	public String toString()
	{
		return "Spawned: " + numItems + "Finished: " + numFinishedItems + "Avg LT: " + avgLeadTime.getAvg();
	}

}
