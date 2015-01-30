package bluetooth;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

/**
 * Use this class to communicate with the gate.
 * The gate stay open for a predefined time and automatically close afterwards.
 * The connection will be terminated automatically after some time, but please always disconnect 
 * manually as soon as possible so the gates aren't blocked longer than necessary (the NXTs can't handle)
 * more than one incoming Bluetooth connection at once).
 */
public class GateControl {
	private RemoteDevice remoteDevice;
	private BTConnection connection;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;

	/**
	 * Connects to the brick that controls the gate.
	 * Required before calling any other method.
	 * 
	 * @return true if the connection to the gate was successful, false otherwise (brick not found, connection failed, ...)
	 */
	public boolean connectionToGateSuccessful() {
		String gateBrickName = Variables.GATE_BRICK_NAME;
		
		remoteDevice = Bluetooth.getKnownDevice(gateBrickName);
		if (remoteDevice == null) {
			System.out.println("Device not found.");
			return false;
		}		

		connection = Bluetooth.connect(remoteDevice);
		if (connection == null) {
			System.out.println("Connection failed.");
			return false;
		}

		System.out.println("Connected.");

		inputStream = connection.openDataInputStream();
		outputStream = connection.openDataOutputStream();

		return (inputStream != null && outputStream != null);
	}
	
	
	/**
	 * Tells the brick to close the connection.
	 * @return true if the command to close the connection was sent, false otherwise
	 */
	public boolean disconnectFromGate() {
		boolean successful = sendCommand(Variables.COMMAND_CLOSE_CONNECTION);
		if (successful)
			connection.close();
		return successful;
	}

	/**
	 * Tells the brick to turn the gate.
	 * @return true if the command was sent, false otherwise
	 */
	public boolean turnClockwise(int angle) {
		return sendCommand(angle);
	}
	
	public boolean sendCommand(int command) {
		try {
			outputStream.writeInt(command);
			outputStream.flush();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}