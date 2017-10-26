import java.io.FileReader;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Source extends Actor
{
	int spawnTime = 3;
	int spawnType = 0;
	int residualTime;
	int numSpawnedItems = 0;

	boolean isDeterministic = false;
	Random rand = new Random();
	double[] percentageBorder;

	// Constructor
	public Source(int spawnTime, boolean isDeterministic)
	{
		this.sim = Simulation.getSimulation();
		sim.sources.add(this);
		sim.actors.add(this);

		this.spawnTime = spawnTime;
		residualTime = spawnTime;// The first item spawns after the spawn
									// time,not at 0
		this.isDeterministic = isDeterministic;
	}
	
	public Source(int spawnTime, boolean isDeterministic, FindNextTargetPolicy nextTargetPolicy, FindNextItemPolicy nextItemPolicy)
	{
		this(spawnTime, isDeterministic);
		this.nextItemPolicy = nextItemPolicy;
		this.nextTargetPolicy = nextTargetPolicy;
	}



	private void spawnDeterministic(String filePath, int period)
	{
		try (Scanner sc = new Scanner(new FileReader(filePath)))
		{
			while (sc.hasNextLine())
			{
				String line = sc.nextLine();
				if (line.contains("#") || line.isEmpty())
					continue;

				String[] splittedLine = line.split(",");

				for (int i = 0; i < splittedLine.length; i++)
				{
					splittedLine[i] = splittedLine[i].trim();
				}

				if (Utils.tryParseInt(splittedLine[0]) == period)// Period
					for (int i = 0; i < Integer.parseInt(splittedLine[2]); i++) // Quantity
					{
						// Spawn the order
						numSpawnedItems++;
						storedItems.add(new Item(sim.getCurTime(), Integer.parseInt(splittedLine[1]),
								Integer.parseInt(splittedLine[3])));
					}

				// TODO Implement something that remembers the last used line to
				// avoid iterating the file again and again
			}
		}
		catch (IOException e)
		{
			System.out.println("File not found");
			e.printStackTrace();
		}

	}

	public void setProportions(Integer[] proportions)
	{
		// TODO Check if all product Types have a probability

		// Get sum of all proportions
		int proportionSum = 0;
		for (Integer d : proportions)
		{
			proportionSum += d;
		}

		// Compute Percentage
		percentageBorder = new double[proportions.length];
		for (int i = 0; i < percentageBorder.length; i++)
		{
			percentageBorder[i] = (double) proportions[i] / (double) proportionSum;
		}

	}



	private int chooseTypeRandom()
	{
		float base = rand.nextFloat();
		for (int i = 0; i < percentageBorder.length; i++)
		{
			base -= percentageBorder[i];
			if (base <= 0)
				return i;
		}

		return 0;
	}

	private void trySpawn()
	{
		if (residualTime > 0)// We have to wait for an incoming order
			residualTime--;
		else if (storedItems.size() == 0)
		{
			storedItems.add(new Item(sim.getCurTime(), chooseTypeRandom(), sim.getCurTime() + 3));
			numSpawnedItems++;
			residualTime = spawnTime - 1;

			if (spawnType == 0) // Change type
				spawnType = 1;
			else
				spawnType = 0;
		}
		else
			System.out.println("Source blocked order, no room!");
	}

	public String toString()
	{
		if (storedItems.size() > 0)
			System.out.println("No room for Order, " + storedItems.size() + " order(s) remain in source: "
					+ storedItems.toString());
		
		if (isDeterministic)
			return "SOURCE: " + " Total spawned: " + numSpawnedItems;
		return "SOURCE:" + " Total spawned: " + numSpawnedItems + " Residual time: " + residualTime;
	}

	@Override
	public boolean isFull()
	{
		return true;
	}

	@Override
	public boolean addItem(Item i)
	{
		return false;
	}

	@Override
	public boolean remItem(Item i)
	{
		storedItems.remove(i);
		return true;

	}


	@Override
	public void work()
	{
		if (isDeterministic)
			spawnDeterministic("/home/wolfgang/Documents/simulation/simulation/src/res/schedule.csv", sim.getCurTime());//works with ubuntu
		else
			trySpawn();

	}

	@Override
	public void calculateStatistics()
	{
		// TODO Auto-generated method stub

	}

}
