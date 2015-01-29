import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

public class Consts {
	
	/**
	 * general Constants
	 */
	
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;

	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	
	public static final NXTRegulatedMotor SENSOR_MOTOR = Motor.C;
	
	
	public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S1;

	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S2;

	public static final SensorPort LEFT_TOUCH_SENSOR_PORT = SensorPort.S3;

	public static final SensorPort RIGHT_TOUCH_SENSOR_PORT = SensorPort.S4;
	
	/**
	 * LineFollower Constants
	 */
	
	public static final int LINE_SEPERATION_VALUE = 40;
	
	public static final int LINE_SEARCH_ARC = 2;

	
	/**
	 * BridgeRider Constants
	 */
	
	public static final int BRIDGE_SEPERATION_VALUE = 30;
	
	public static final int BRIDGE_SEARCH_ARC = 4;

	private Consts() {
		throw new AssertionError();
	}
}
