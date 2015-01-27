import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;


public class Ultrasonic implements ButtonListener{

		public boolean buttonPressed = false;
	
	    public static void main(String[] args) throws Exception {
	    	Ultrasonic tests = new Ultrasonic();
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
				UltrasonicSensor headsensor = new UltrasonicSensor(SensorPort.S2);
		        int distance;
				distance = headsensor.getDistance();
		        System.out.println("Distance: "+distance);
			}else if(b == Button.ESCAPE){
				buttonPressed = true;
			}
			
		}

		@Override
		public void buttonReleased(Button b) {
			// TODO Auto-generated method stub
			
		}
}