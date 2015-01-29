import java.io.DataInputStream;
import java.io.DataOutputStream;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.robotics.subsumption.Behavior;


public class GateBehaviour implements Behavior{
	
	private RemoteDevice remoteDevice;
	private BTConnection connection;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean Connect;
	
	/*
	 * Return true, when the Gate is open
	 * Return false, when there isn't Bluetooth connection
	 * @see lejos.robotics.subsumption.Behavior#takeControl()
	 */
	@Override
	public boolean takeControl() {
		return Connect;
	}

	@Override
	public void action() {
		remoteDevice = new RemoteDevice("TestName", "00165304779A", 0);
		if (remoteDevice == null)
			Connect = false;

		connection = Bluetooth.connect(remoteDevice);
		if (connection == null)
			Connect = false;

		inputStream = connection.openDataInputStream();
		outputStream = connection.openDataOutputStream();

		Connect = (inputStream != null && outputStream != null);
		
	}

	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
}
