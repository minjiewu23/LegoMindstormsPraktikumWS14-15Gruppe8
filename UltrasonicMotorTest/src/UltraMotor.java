import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;


public class UltraMotor implements ButtonListener{

		public boolean buttonPressed = false;
	
	    public static void main(String[] args) throws Exception {
	    	UltraMotor tests = new UltraMotor();
	    	System.out.println("Conect");
			Button.ENTER.addButtonListener(tests);
			Button.ESCAPE.addButtonListener(tests);
			while (!tests.buttonPressed) {
				Thread.sleep(100);
			} 
	       
	    }

		@Override
		public void buttonPressed(Button b) {
			if(b == Button.ENTER){
				DifferentialPilot pilot = new DifferentialPilot(20, 100, Motor.A, Motor.B);
				UltrasonicSensor headsensor = new UltrasonicSensor(SensorPort.S2);
				int distance=255;
				while(!buttonPressed){
					if(distance < 20){
						pilot.setTravelSpeed(200);
						pilot.travel(-50);
					}
					distance = headsensor.getDistance();
					if(distance > 20) pilot.stop();
			        System.out.println("Distance: "+distance);
					if(b == Button.ESCAPE){
						buttonPressed = true;
					}
				}
			}			
		}

		@Override
		public void buttonReleased(Button b) {
			// TODO Auto-generated method stub
			
		}
}