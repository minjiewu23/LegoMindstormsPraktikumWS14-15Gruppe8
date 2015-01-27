import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;

interface ScannerListener {
	public void scannerRound();
}

public class Scanner implements Runnable{

private boolean stop = false;

private NXTRegulatedMotor motor;

private ScannerListener listeners[] = new ScannerListener[3];

public Scanner() {
	stop = false;
	motor = Motor.C;
}

public void addListener(ScannerListener listener) {
	listeners[0] = listener;
}

public void stopScanning () {
	stop = true;
}

@Override
public void run() {
	while (!stop) {
		motor.rotateTo(-90);
		listeners[0].scannerRound();
		motor.rotateTo(90);
		listeners[0].scannerRound();
	}
}

}