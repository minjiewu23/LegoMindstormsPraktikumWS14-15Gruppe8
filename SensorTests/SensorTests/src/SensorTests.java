import javax.microedition.sensor.SensorInfo;
import javax.microedition.sensor.SensorListener;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.RotateMoveController;


public class SensorTests implements ButtonListener{
	public boolean buttonPressed = false;
	
	public static void main(String[] args) throws Exception {
		SensorTests tests = new SensorTests();
		tests.rotateMotor();
		Button.ENTER.addButtonListener(tests);
		Button.ESCAPE.addButtonListener(tests);
		while (!tests.buttonPressed) {
			Thread.sleep(100);
		}
	}
	
	public void rotateMotor() throws InterruptedException {
		while (!buttonPressed) {
			Motor.C.rotateTo(0);
			Thread.sleep(100);
			Motor.C.rotateTo(90);
			Thread.sleep(100);
			Motor.C.rotateTo(0);
			Thread.sleep(100);
			Motor.C.rotateTo(-90);
			Thread.sleep(100);
		}
	}

	@Override
	public void buttonPressed(Button b) {
		LCD.drawString("Pressed", 0, 0);
		if (b == Button.ENTER) {
			UltrasonicSensor ultrasonic = new UltrasonicSensor(SensorPort.S2);
			LightSensor lightSensor = new LightSensor(SensorPort.S1);
			int ultra = ultrasonic.getDistance();
			String ultraUnit = ultrasonic.getUnits();
			int light = lightSensor.getLightValue();
			int normalLight = lightSensor.getNormalizedLightValue();
			LCD.refresh();
			LCD.drawString("Ultrasonic: "+ultra+ultraUnit, 0, 0);
			LCD.drawString("Light Value: "+light, 0, 1);
			LCD.drawString("NormalizedLightValue"+normalLight, 0, 2);
		} else if (Button.ESCAPE == b) {
			buttonPressed = true;
		}
		
	}

	@Override
	public void buttonReleased(Button b) {
		// TODO Auto-generated method stub
		
	}
}
