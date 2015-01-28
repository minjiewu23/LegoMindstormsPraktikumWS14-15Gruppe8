package gruppe8;

import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.TouchSensor;

public class ManhattanProject {
    
	public static void main(String[] args){
		
		
	       TouchSensor touch1 = new TouchSensor(SensorPort.S3);
	       TouchSensor touch2 = new TouchSensor(SensorPort.S4);
	       int count1 = 0 ;
	       int count2 = 0;
	     
	           if(touch1.isPressed()){
	             count1++;
	             LCD.clear();
	             LCD.drawString("Left: "+count1, 3, 3);
	             LCD.refresh();
	             while ( touch1.isPressed())
	                 {}
	              }
	           if(touch2.isPressed()){
		             count2++;
		             LCD.clear();
		             LCD.drawString("Right: "+count2, 3, 4);
		             LCD.refresh();
		             while ( touch2.isPressed())
		                 {}
	           }
	           
	}
}
