import lejos.robotics.subsumption.Behavior;


public class DefaultBehaviour implements Behavior {
	
	private ContinuousPilot pilot;

	public DefaultBehaviour(ContinuousPilot pilot) {
		// TODO Auto-generated constructor stub
		this.pilot = pilot;
	}
	
	/**
	 * this Method determines whether or not to take control 
	 * the Arbitrator periodically asks every Behaviour whether or not it wants to take control 
	 * and the one with the highest Rank that says yes will be run
	 * 
	 * The Rank is determined on Arbitrary Construction
	 * the first Element in the array [0] is the one with the lowest rank
	 */
	@Override
	public boolean takeControl() {
		return true;
	}

	/**
	 * will be called by the Arbitrator as long as this Class is active
	 */
	@Override
	public void action() {
		// TODO Auto-generated method stub
		pilot.resetPose();
		if ((Math.random() - 0.5) < 0) {
			pilot.rotateRight90();
		} else {
			pilot.rotateLeft90();
		}
	}

	/**
	 *will be called by the Arbitrator when this class no longer will be active 
	 */
	@Override
	public void suppress() {
		pilot.stop();
	}

}
