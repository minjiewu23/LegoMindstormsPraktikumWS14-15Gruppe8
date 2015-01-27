import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.LightScanner;
import lejos.robotics.pathfinding.FourWayGridMesh;

interface LineDetectorListener {
	public void lineFound();
}

public class LineDetector implements Runnable, ScannerListener{
	
	private boolean stop = false;
	
	private boolean lineFound = false;
	
	private int linePosition = 0;
	
	private int minLightValue = 30;
	
	private int listenersCount = 0;
	
	private int scanCount = 0;
	
	private Scanner scanner;
	
	private int ttl = 2;
    private final Object lock = new Object();
	
	private LineDetectorListener[] listeners = new LineDetectorListener[3];
	
	public boolean lineFound() {
		return lineFound;
	}
	
	public void setLinePosition(int position) {
		synchronized (lock) {
			this.linePosition = position;
		}
	}
	
	public int getLinePosition() {
		synchronized (lock) {
			return this.linePosition;
		}
	}
	
	public LineDetector(int minLightValue, Scanner scanner) {
		stop = false;
		this.minLightValue = minLightValue;
		this.scanner = scanner;
	}
	
	public void setListener(LineDetectorListener listener) {
		this.listeners[listenersCount] = listener;
		listenersCount ++;
		listenersCount = Math.min(listenersCount, listeners.length);
	}
	
	public void stopScaning () {
		stop = true;
	}

	@Override
	public void run() {
		LightSensor sensor = new LightSensor(SensorPort.S1);
		while (!stop) {
			if (sensor.getLightValue() >= minLightValue) {
				lineFound = true;
				scanCount = 0;
				this.setLinePosition(Motor.C.getPosition());

				LCD.drawString("Arc: "+this.getLinePosition(), 0, 0);
			}
		}
	}

	@Override
	public void scannerRound() {
		scanCount++;
		if (scanCount > ttl) {
			this.lineFound = false;
		}
	}

}
