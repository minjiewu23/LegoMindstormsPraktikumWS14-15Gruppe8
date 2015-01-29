import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;

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

	public static final LightSensor LIGHT_SENSOR = new LightSensor(LIGHT_SENSOR_PORT);

	public static final UltrasonicSensor ULTRASONIC_SENSOR = new UltrasonicSensor(ULTRASONIC_SENSOR_PORT);
	
	public static final TouchSensor TOUCH_SENSOR_LEFT = new TouchSensor(LEFT_TOUCH_SENSOR_PORT);

	public static final TouchSensor TOUCH_SENSOR_RIGHT = new TouchSensor(RIGHT_TOUCH_SENSOR_PORT);
	
	/**
	 * Sensors Define
	 */
	
	public static final DifferentialPilot PILOT = new DifferentialPilot(20, 100, Motor.A, Motor.B, true);
	
	/**
	 * LineFollower Constants
	 */
	
	public static final int LINE_SEPERATION_VALUE = 40;
	
	public static final int LINE_SEARCH_ARC = 2;
	
	public static final int LINE_BORDER_OFFSET = 5;
	
	public static final int LINE_MAX_PANS_WHILE_FOLLOWING = 3;

	
	/**
	 * BridgeRider Constants
	 */
	
	public static final int BRIDGE_SEPERATION_VALUE = 30;
	
	public static final int BRIDGE_SEARCH_ARC = 4;

	private Consts() {
		throw new AssertionError();
	}
}
