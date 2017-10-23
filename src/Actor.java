import java.util.ArrayList;
import java.util.List;

public abstract class Actor
{
	Simulation sim;
	List<Actor> targets = new ArrayList<Actor>();
	List<Item> storedItems = new ArrayList<Item>();
	FindNextTargetPolicy nextTargetPolicy = FindNextTargetPolicy.sendToFirstPossibility;
	FindNextItemPolicy nextItemPolicy = FindNextItemPolicy.FIFO;

	// Abstracts
	public abstract void work();

	public abstract void calculateStatistics();

	public abstract boolean isFull();

	public abstract boolean addItem(Item i);

	public abstract boolean remItem(Item i);

	void sendItems()
	{
		switch (nextTargetPolicy)
		{
		case evenDistributed:
			sendItemsEvenDistributed();
			break;
		case sendToFirstPossibility:
			sendItemsToFirstPossibility();
			break;
		case lowestStock:
			sendToLowestStock();
			break;
		case accordingToTable:
			accordingToTable();
			break;
		default:
			break;
		}
	}

	private void sendToLowestStock()
	{
		//In case of one target
		if(targets.size() == 1)
			sendItemsToFirstPossibility();

		boolean sentItem = true;
		while(sentItem) {
			sentItem = false;
			//In case of lowest/second are same we need the third lowest stock
			//check the lowest/second stock niveau
			Integer lowest = null, secLowest = null;
			for (Actor a : targets) {
			    //System.out.println(a + "Stock"  + a.storedItems.size());
				if (lowest == null || a.storedItems.size() < lowest)
                {
                    lowest = a.storedItems.size();continue;
                }
                //System.out.println("Lowest: " + lowest + " Second" + secLowest);
				if (secLowest == null || (a.storedItems.size() < secLowest && a.storedItems.size() > lowest))
					secLowest = a.storedItems.size();

			}
            System.out.println("Lowest: " + lowest + " Second" + secLowest);


			//if we found no second lowest, all stock have the same stock level
			if (secLowest == null)
				sendItemsEvenDistributed();
			else {
				int numberItemsAdded = secLowest - lowest;

				List<Actor> lowestStock = new ArrayList<>();
				//Add all Actors that have the same and lowest stock level
				for (Actor a : targets) {
					if (a.storedItems.size() == lowest)
						lowestStock.add(a);
				}

				//Fill Actors till second lowest
				for (int i = 0; i <= numberItemsAdded; i++) {
					for (Actor a : lowestStock) {
						if (canReceiveItem(a))
						{
							transferItem(a, storedItems.get(findIndexAccordingToStockPolicy(a)));
							sentItem = true;
						}

					}
				}


			}
		}

	}

	private void accordingToTable()
	{
		throw new Error("Not implemented");
	}

	private void sendItemsEvenDistributed()
	{
		// Should evenly distribute spawned Items to all targets
		boolean sentItem = true;
		while (sentItem)
		{
			sentItem = false;

			for (Actor a : targets)
			{
				if (canReceiveItem(a) && !isEmpty())
				{
					sentItem = true;
					//System.out.println(this + " " + a);
					transferItem(a, storedItems.get(findIndexAccordingToStockPolicy(a)));
				}
			}
		}
	}

	private void sendItemsToFirstPossibility()
	{
		for (Actor a : targets)
		{
			while (a != null && !a.isFull() && !isEmpty())
			{
				transferItem(a, storedItems.get(findIndexAccordingToStockPolicy(a)));
			}
		}
	}

	private int findIndexAccordingToStockPolicy(Actor a)
	{
		switch (nextItemPolicy)
		{
		case FIFO:
			return chooseIndexFIFO();
		case LIFO:
			return chooseIndexLIFO();
		case MinSetupTime:
			return chooseIndexWhichMinimizesSetupTime(a);
		default:
			return chooseIndexFIFO();
		}
	}

	int chooseIndexWhichMinimizesSetupTime(Actor a)
	{
		if (!(a instanceof Machine))
			return 0;

		Machine ma = (Machine) a;
		int lastProcessedType;
		if (ma.getlastProcessedItemType() == null)
			return 0;
		else
			lastProcessedType = ma.getlastProcessedItemType();

		// Look if we have an order of the same item type as before processed
		for (int i = 0; i < storedItems.size(); i++)
		{
			if (storedItems.get(i).type == lastProcessedType)
				return i;
		}
		return 0;
	}

	private int chooseIndexFIFO()
	{
		return 0;
	}

	private int chooseIndexLIFO()
	{
		return storedItems.size() - 1;
	}

	public boolean canReceiveItem(Actor target)
	{
		if (target != null && !target.isFull())
			return true;
		else
			return false;
	}

	public boolean hasTargetThatCanReceiveItem()
	{
		for (Actor a : targets)
			if (canReceiveItem(a))
				return true;
		return false;
	}

	// Currently double occurances can occur
	public void assignTarget(Actor t)
	{
		targets.add(t);
	}


	public boolean transferItem(Actor receiver, Item i)
	{
		if (receiver.addItem(i) && remItem(i))
		{
			return true;
		}
		else
			return false;
	}

	public boolean isEmpty()
	{
		if (storedItems.size() == 0)
			return true;
		else
			return false;
	}

}
