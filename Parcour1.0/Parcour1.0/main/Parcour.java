import lejos.nxt.Motor;
import lejos.robotics.subsumption.Arbitrator;
import lejos.robotics.subsumption.Behavior;


public class Parcour {
	public static void main(String[] args) {
		ContinuousPilot pilot = new ContinuousPilot(17.5, Motor.A, Motor.B);
		DefaultBehaviour defaultBehaviour = new DefaultBehaviour(pilot);
		ParcourStartBehaviour parcourStart = new ParcourStartBehaviour();
		FindLineAfterStartBehaviour findLineAfterStart = new FindLineAfterStartBehaviour();
		FollowLineBehaviour findLine = new FollowLineBehaviour();
		
		/**
		 * store all the behaviours in an array
		 * the first behaviour gets the highest priority 
		 * => when this one thinks it is done it just always sends false in take action
		 * and the next one will be called
		 */
		Behavior behaviours[] = {defaultBehaviour, findLine, findLineAfterStart, parcourStart};
		Arbitrator parcourArbitrator = new Arbitrator(behaviours, false);
	}
}
