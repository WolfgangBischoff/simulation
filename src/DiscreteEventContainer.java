
public class DiscreteEventContainer
{
	//Saves discrete events over the last n events
	//For instance the number of idle/processing/setup periods in the last n timeperiods
	//Return data of proportions
	
	String eventNames[];
	int eventCounter[];
	int eventData[];
	int index = 0;
	boolean notJetFilled = true;
	
	public DiscreteEventContainer(String[] eventNames, int numberObservedEvents)
	{
		eventData = new int[numberObservedEvents];
		this.eventNames = eventNames;
		eventCounter = new int[eventNames.length];
	}
	
	
	
	public void addData(int newData)
	{
		//Count the event
		eventCounter[newData]++;
		
		//Erase the oldest event
		if(notJetFilled == false)
			eventCounter[eventData[index]]--;
		//Remember the newest event
		eventData[index] = newData;
		index++;
		if(index == eventData.length)
		{
			index=0;
			notJetFilled = false;
		}
		
	}
		
	public double getProportion(int eventIndex)
	{
		 if(notJetFilled)
		 {
			 int totalNumberEvents = 0;
			 for(int e : eventCounter)
				 totalNumberEvents += e;
			 return (double)eventCounter[eventIndex] / totalNumberEvents;			 
		 }
		 else
			 return (double)eventCounter[eventIndex] / eventData.length;
		
	}
	
	public String toString()
	{
		StringBuilder ret = new StringBuilder();
		for(int i=0; i<eventNames.length; i++)
		{
			ret.append(eventNames[i] + ": " + Utils.round(getProportion(i), 2) + " ");
		}
		return ret.toString();
	}
	
}
