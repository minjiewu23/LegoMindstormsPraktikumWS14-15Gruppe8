import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class Parcour implements ButtonListener
{
	public boolean ready = false;
	
	public static void main(String[] args) throws InterruptedException {
		Parcour parcour = new Parcour();
		Button.ENTER.addButtonListener(parcour);
		while (!parcour.ready) {
			Thread.sleep(100);
		}
		ContinuousPilot pilot = new ContinuousPilot(17.5, Consts.LEFT_MOTOR, Consts.RIGHT_MOTOR);
		
		DefaultBehaviour defaultBehaviour = new DefaultBehaviour(pilot);
		ParcourStartBehaviour parcourStart = new ParcourStartBehaviour();
		FindLineAfterStartBehaviour findLineAfterStart = new FindLineAfterStartBehaviour();
		FollowLineBehaviour findLine = new FollowLineBehaviour(pilot, FollowLineBehaviour.LinePart.FIRST);
		
		/**
		 * store all the behaviours in an array
		 * the first behaviour gets the highest priority 
		 * => when this one thinks it is done it just always sends false in take action
		 * and the next one will be called
		 */
		Behavior behaviours[] = {findLine};
		Arbitrator parcourArbitrator = new Arbitrator(behaviours, false);
		LCD.drawString("Starting Parcour", 0, 0);
		parcourArbitrator.start();
	}

	@Override
	public void buttonPressed(Button b) {
	}

	@Override
	public void buttonReleased(Button b) {
		if (b == Button.ENTER) {
			this.ready = true;
		}
	}
}
