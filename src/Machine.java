

public class Machine extends Actor
{
	// Statics
	static int nextMachineNumber = 1;

	// General
	int machineNumber;

	// Processing
	double processingTime = 0;
	double resProcTime = 0;
	private Item wip;
	private MachineState state;
	int numProcItems = 0;

	// Machine Setups
	Integer lastProcessedItemType = null;
	int setupTime = 2;
	int remainingSetupTime = 0;

	// Statistical
	DiscreteEventContainer stat = new DiscreteEventContainer(Utils.getEnumNames(MachineState.class), 10);

	// KONSTRUKTOR
	public Machine(int procTime)
	{
		this.sim = Simulation.getSimulation();
		sim.machines.add(this);
		sim.actors.add(this);
		machineNumber = nextMachineNumber++;

		processingTime = procTime;

	}


	private void actualizeState()
	{

		if (isFull())
		{
			if (remainingSetupTime > 0)
				state = MachineState.SETUP;
			else if (resProcTime > 0)
				state = MachineState.PROCESSING;
			else
				state = MachineState.WAITINGSTORAGE;
		}
		else
			state = MachineState.IDLE;
	}

	private void handleState()
	{
		// set state
		switch (state)
		{
		case PROCESSING:
			process();
			break;
		case SETUP:
			setup();
			break;
		case IDLE:
			break;
		case WAITINGSTORAGE:
			break;
		default:
			break;
		}

	}

	private void process()
	{
		resProcTime--;

		if (resProcTime == 0)
		{
			numProcItems++;
			wip.setTimeFinished(sim.getCurTime());
		}

	}

	private void setup()
	{
		remainingSetupTime--;
	}



	public String toString()
	{

		return "MASCHINE " + machineNumber + ": Residual Time: " + resProcTime + " Res Setup time: "
				+ remainingSetupTime + " ItemType: " + lastProcessedItemType + " State: " + state
		// + "----- Times: " + stat.toString()

		;
	}

	@Override
	public boolean isFull()
	{
		return wip != null;
	}

	@Override
	public boolean isEmpty()
	{
		return !isFull();
	}

	@Override
	public boolean addItem(Item i)
	{
		wip = i;
		resProcTime = processingTime;
		/* If the Itemtype changed, add setup time */
		if (lastProcessedItemType == null || lastProcessedItemType != i.type)
		{
			lastProcessedItemType = i.type;
			remainingSetupTime = setupTime;
		}
		return true;
	}

	@Override
	public boolean remItem(Item i)
	{
		wip = null;
		return true;
	}

	public Item getWip()
	{
		return wip;
	}

	public Integer getlastProcessedItemType()
	{
		return lastProcessedItemType;
	}

	@Override
	public boolean hasTargetThatCanReceiveItem()
	{
		if (resProcTime > 0)
			return false;

		for (Actor a : targets)
			if (canReceiveItem(a))
				return true;
		return false;
	}

	@Override
	public void sendItems()
	{
		if (resProcTime <= 0 && targets != null && !targets.get(0).isFull() && !isEmpty())
		{
			transferItem(targets.get(0), wip);
			wip = null;
		}

	}

	@Override
	public void work()
	{
		actualizeState();
		handleState();
	}

	@Override
	public void calculateStatistics()
	{
		stat.addData(state.ordinal());

	}

}
