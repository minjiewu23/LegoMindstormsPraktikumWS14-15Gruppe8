package gruppe8;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;
import lejos.robotics.navigation.DifferentialPilot;

public class ManhattanProject {
    
	public static void main(String[] args){
		
		DifferentialPilot pilot = new DifferentialPilot(10, 10, Motor.A, Motor.B);
	       TouchSensor touch1 = new TouchSensor(SensorPort.S3);
	       TouchSensor touch2 = new TouchSensor(SensorPort.S4);
	       int count1 = 0 ;
	       int count2 = 0;



//	           if(touch1.isPressed()){
//	        	   pilot.stop();
//	        	   pilot.rotate(120);
//
//	             count1++;
//	             LCD.drawString("Left: "+count1, 3, 3);
//	             LCD.refresh();
//	             while ( touch1.isPressed())
//	                 {}
//	              }
//	           if(touch2.isPressed()){
//	        	   pilot.stop();
//	        	   pilot.rotate(-120);
//	        	   
//		             count2++;
//		             LCD.drawString("Right: "+count2, 3, 4);
//		             LCD.refresh();
//		             while ( touch2.isPressed())
//		                 {}
//	           }
//		       pilot.backward();
	       
	       BluetoothHandler blehandler = new BluetoothHandler();
	       
	     
	           
	}
}
