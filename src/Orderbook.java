import java.util.ArrayList;
import java.util.List;

public class Orderbook
{

	List<Item> orders = new ArrayList<Item>();
	Simulation sim;
	
	public Orderbook(Simulation sim)
	{
		this.sim = sim;
	}
	
	public void printOrderbook()
	{
		System.out.print("Orderbook: ");
		for(Item i : orders)
			System.out.print(i);
		System.out.println();
	}
	
	public void printOrderbook(int numberPositions)
	{
		System.out.print("Orderbook: ");
		if(numberPositions > orders.size())
			numberPositions = orders.size();
		
		for(int i=0; i<numberPositions; i++)
		{
			System.out.print(orders.get(i));
		}
		System.out.println();
	}
	
	public boolean removeItem(Item i)
	{
		return orders.remove(i);
	}
	
	public boolean addItem(Item i)
	{
		return orders.add(i);
	}
	
	public void checkBackorders()
	{
		for(Item i : orders)
		{
			if(sim.getCurTime() > i.dueDate)
			{
				sim.remCash(i.getBackorderFee());
			}
		}
	}
	
}
