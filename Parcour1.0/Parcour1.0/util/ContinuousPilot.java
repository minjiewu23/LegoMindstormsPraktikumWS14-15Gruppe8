import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.robotics.navigation.Pose;


public class ContinuousPilot {
	
	private double timeFor100Right;
	
	private double timeFor100Left;

	private int speedLeft;
	
	private int speedRight;
	
	private static int defaultSpeed = 200;
	
	private double wheelDiameter = 3;
	private double wheelDistance = 8.6;
	
	NXTRegulatedMotor leftMotor;
	
	NXTRegulatedMotor rightMotor;
	
	private Pose pose;
	
	/**
	 * performs as many steps fprward as specified
	 * 1 step is one wheel turn
	 */
	public void forwardStep(double d) {
		int turn = (int) (d * 360);
		LCD.drawInt(turn, 5, 5);
		leftMotor.rotate(turn, true);
		rightMotor.rotate(turn);
	}
	
	/**
	 * Constructs a ContinuousPilot with given Values
	 * @param timeFor100 in seconds
	 * @param timeFor360 in seconds
	 * @param left left motor
	 * @param right right motor
	 */
	public ContinuousPilot(double timeFor100, NXTRegulatedMotor left, NXTRegulatedMotor right) {
		// TODO Auto-generated constructor stub
		this.timeFor100Left = timeFor100;
		this.timeFor100Right = timeFor100;
		this.leftMotor = left;
		this.rightMotor = right;
		pose = new Pose();
	}
	
	public float getHeadingSinceLastPoseReset() {
		return this.pose.getHeading();
	}
	
	public void resetPose() {
		this.pose = new Pose();
	}

	/**
	 * Constructs a ContinuousPilot with given Values
	 * different speed values for left and right weel are possible (if robot is faster on left or right side)
	 * @param timeFor100 in seconds
	 * @param timeFor360 in seconds
	 * @param left left motor
	 * @param right right motor
	 */
	public ContinuousPilot(double timeFor100Left, double timeFor100Right, NXTRegulatedMotor left, NXTRegulatedMotor right) {
		// TODO Auto-generated constructor stub
		this.timeFor100Left = timeFor100Left;
		this.timeFor100Right = timeFor100Right;
		this.leftMotor = left;
		this.rightMotor = right;
	}
	
	/**
	 * speed means the time needed for 100 units (same as in timeFor100)
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speedLeft = (int) ((timeFor100Left * defaultSpeed) / speed);
		this.speedRight = (int) ((timeFor100Right * defaultSpeed) / speed);
	}
	
	public int getSpeedLeft() {
		return speedLeft;
	}
	
	public int getSpeedRight() {
		return speedRight;
	}
	
	public void forward() {
		leftMotor.setSpeed(this.speedLeft);
		rightMotor.setSpeed(this.speedRight);
		leftMotor.forward();
		rightMotor.forward();
	}
	
	public void backward() {
		leftMotor.setSpeed(this.speedLeft);
		rightMotor.setSpeed(this.speedRight);
		leftMotor.backward();
		rightMotor.backward();
	}
	
	public void stop() {
		leftMotor.stop();
		rightMotor.stop();
	}
	
	public int motorRotation(double degree) {
		double drehKreisUmfang = Math.PI * wheelDistance;
		double radumfang = Math.PI * wheelDiameter;
		double radDrehungProGrad = radumfang / 360;
		double gradProMotor = (drehKreisUmfang / radDrehungProGrad) * 2;
		
		gradProMotor *= degree / 360;
		
		return (int) gradProMotor;
	}
	
	public void rotateLeftBy(int degree) {
		leftMotor.stop();
		rightMotor.stop();
		int deg = motorRotation(degree);
		this.pose.rotateUpdate(degree);
		rightMotor.rotate((int) deg, true);
		leftMotor.rotate((int) -deg);
	}
	
	public void rotateRightBy(int degree) {
		leftMotor.stop();
		rightMotor.stop();
		int deg = motorRotation(degree);
		this.pose.rotateUpdate(-degree);
		rightMotor.rotate((int) -deg, true);
		leftMotor.rotate((int) deg);
	}
	
	public void rotateLeft90() {
		this.rotateLeftBy(90);
	}
	
	public void rotateRight90() {
		this.rotateRightBy(90);
	}
	
	public void smallRotateLeft() {
		this.rotateLeftBy(5);
	}
	
	public void smallRotateRight() {
		this.rotateRightBy(5);
	}
	
	public void smallPanLeft() {
		this.forward();
		leftMotor.stop();
		try {
			Thread.sleep((int) (150));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stop();
	}
	
	public void smallPanRight() {
		this.forward();
		rightMotor.stop();
		try {
			Thread.sleep((int) (150));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.stop();
	}
	
	public static void testTimeFor100(NXTRegulatedMotor left, NXTRegulatedMotor right) {
		left.setSpeed(defaultSpeed);
		right.setSpeed(defaultSpeed);
		left.backward();
		right.backward();
		while (true){}
	}
	
	public static void testTimeFor360(NXTRegulatedMotor left, NXTRegulatedMotor right) {
		left.setSpeed(defaultSpeed);
		right.setSpeed(defaultSpeed);
		left.forward();
		right.backward();
		while (true){}
	}
}
