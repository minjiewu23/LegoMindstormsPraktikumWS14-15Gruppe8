package gruppe8;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.util.TextMenu;

public class BluetoothHandler extends Thread implements DiscoveryListener{

	private List<RemoteDevice> devices;
	private StreamConnection connection;
	private DataInputStream inputStream;
	private DataOutputStream outputStream;
	private boolean started;

	public static boolean start = false;
	
	public BluetoothHandler() {
		this.devices = new ArrayList<RemoteDevice>();
		searchForDevices();
		setDaemon(true);
		this.started = false;
		while(!Button.ESCAPE.isPressed() && !started) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
		}

	}

	
	public void run() 
	{
		LCD.clear();
		int i = 0;
		while (!Button.ESCAPE.isPressed() && i != 2) {
			i = read();

			LCD.drawString("warten" , 0, 1);
			switch(i)
			{
				case 1: LCD.drawString("READY", 0, 1);break;
				case 2:
				{	started = true;
				}break;
			}
			

		}
	}
	

	private void send(int requestCode) {
		try {
			outputStream.writeInt(requestCode);
			outputStream.flush();
		} catch (IOException e) {
		}
	}

	private int read() {
		int requestCode = -1;
		try {
			while (inputStream.available() < 1) {
			}

			requestCode = inputStream.readInt();
		} catch (IOException e) {
		}
		return requestCode;
	}

	@Override
	public void deviceDiscovered(RemoteDevice remoteDevice,
			DeviceClass deviceClass) {
		devices.add(remoteDevice);
	}

	@Override
	public void inquiryCompleted(int arg0) {
		String[] items = new String[devices.size()];
		for (int i = 0; i < devices.size(); i++) {
			RemoteDevice remoteDevice = devices.get(i);
			items[i] = "[" + remoteDevice.getFriendlyName(false) + "]"
					+ remoteDevice.getBluetoothAddress();
		}
		TextMenu menu = new TextMenu(items);

		int selection = menu.select();
		if (selection < 0) {
			// abort stuffs
			return;
		}
		RemoteDevice remoteDevice = devices.get(selection);
		this.connection = Bluetooth.connect(remoteDevice);
		this.inputStream = connection.openDataInputStream();
		this.outputStream = connection.openDataOutputStream();
		run();

	}

	public void searchForDevices() {
		LocalDevice localDevice;
		try {
			localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent discoveryAgent = localDevice.getDiscoveryAgent();
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC, this);
		} catch (BluetoothStateException e) {
		}
	}

}
