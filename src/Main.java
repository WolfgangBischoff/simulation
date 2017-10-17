
public class Main
{

	public static void main(String[] args)
	{
		
		Simulation sim = Simulation.getSimulation();
		new Thread(sim).start();
	}

}
