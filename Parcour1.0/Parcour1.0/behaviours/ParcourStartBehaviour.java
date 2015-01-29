import lejos.robotics.subsumption.Behavior;


public class ParcourStartBehaviour implements Behavior {
	
	private int rotateSpeed = 200;
	private int rotateRobotRight = -113;
	private int rotateRobotLeft = 100;
	private int lineNumber = 0;
	
	@Override
	public boolean takeControl() {
		if(lineNumber >= 3)	return false;
		else return true;
	}

	@Override
	public void action() {
		while(Consts.ULTRASONIC_SENSOR.getDistance() > 35){
			Consts.PILOT.forward();
			if(Consts.LIGHT_SENSOR.getLightValue() < Consts.LINE_SEPERATION_VALUE){
				lineNumber++;
			}
			lookWall();
		}
		Consts.PILOT.stop();
		Consts.SENSOR_MOTOR.rotateTo(-90); //Rotate Ultrasonic Left
		int distanceLeft = Consts.ULTRASONIC_SENSOR.getDistance(); //Left Wall
		Consts.SENSOR_MOTOR.rotateTo(90); //Rotate Ultrasonic Right 90
		int distanceRight = Consts.ULTRASONIC_SENSOR.getDistance(); //Right Wall
		Consts.SENSOR_MOTOR.rotateTo(-90); //Rotate Ultrasonic null
		
		Consts.PILOT.setRotateSpeed(rotateSpeed); //Rotate Robot speed
		//Go to the direction where is more distance to the next wall
		if(distanceRight < distanceLeft) Consts.PILOT.rotate(rotateRobotRight);
		else Consts.PILOT.rotate(rotateRobotLeft);
		Consts.PILOT.forward();
	}
	
	private static void lookWall() {
			
		Consts.SENSOR_MOTOR.rotateTo(-90); //Rotate Ultrasonic Left
		if(Consts.ULTRASONIC_SENSOR.getDistance() < 30){
			Consts.PILOT.rotate(10); //Rotate Robot Right 10º
		}
		
		Consts.SENSOR_MOTOR.rotateTo(90); //Rotate Ultrasonic null + Right (90 + 90)
		if(Consts.ULTRASONIC_SENSOR.getDistance() < 30){ //Right Wall
			Consts.PILOT.rotate(-10); //Rotate Robot Left 10º
		}
		Consts.SENSOR_MOTOR.rotate(-90); //Rotate Ultrasonic null
		
	}

	@Override
	public void suppress() {
		Consts.PILOT.stop();
		Consts.SENSOR_MOTOR.rotateTo(0);

	}

}
