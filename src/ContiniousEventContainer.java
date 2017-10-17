
public class ContiniousEventContainer
{

	double[] eventData;
	int index = 0;
	boolean containerIsFull = false;

	ContiniousEventContainer(int numberObservedPeriods)
	{
		eventData = new double[numberObservedPeriods];
	}

	public void addData(double newData)
	{
		eventData[index] = newData;
		index++;
		if (index == eventData.length)
		{
			index = 0;
			containerIsFull = true;
		}

	}

	public double getAvg()
	{
		double sum = 0;
		if (containerIsFull)
		{
			for (double d : eventData)
				sum += d;
			return sum/(double)eventData.length;
		}
		else
		{
			for(int i=0; i<index; i++)
				sum += eventData[i];
			return sum/(double)index;
		}

	}
}
