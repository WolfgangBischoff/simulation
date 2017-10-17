import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUI extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Simulation sim;
	
	JButton button1;
	JButton button2;
	JButton button3;
	JLabel label;
	JPanel panel;

	public GUI(Simulation sim)
	{
		this.sim = sim;
		
		this.setTitle("WorkShop Control");
		this.setSize(400, 200);
		panel = new JPanel();

		// Leeres JLabel-Objekt wird erzeugt
		label = new JLabel();

		// Drei Buttons werden erstellt
		button1 = new JButton("Next Period");
		button2 = new JButton("Calculate till max Period");
		button3 = new JButton("Reset");

		// Buttons werden dem Listener zugeordnet
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);

		// Buttons werden dem JPanel hinzugefügt
		panel.add(button1);
		panel.add(button2);
		panel.add(button3);

		// JLabel wird dem Panel hinzugefügt
		panel.add(label);
		this.add(panel);

		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent ae)
	{
		// Die Quelle wird mit getSource() abgefragt und mit den
		// Buttons abgeglichen. Wenn die Quelle des ActionEvents einer
		// der Buttons ist, wird der Text des JLabels entsprechend geändert
		if (ae.getSource() == this.button1)
		{
			label.setText("Next Period");
			sim.calculatePeriod();
		}
		else if (ae.getSource() == this.button2)
		{
			label.setText("Run");
			sim.calculateTillMax();
		}
		else if (ae.getSource() == this.button3)
		{
			label.setText(("Reseted"));
			sim.resetSimSetting();
		}
	}

}
