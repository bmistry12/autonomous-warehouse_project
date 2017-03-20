package com.bestroboticsteam.communication;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.BaseConnectionHandler;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class PCConnectionHandler extends BaseConnectionHandler {

	private String robotName;
	private static final int NUM_RETRYS = 10;

	final static Logger logger = Logger.getLogger(PCConnectionHandler.class);

	public PCConnectionHandler(String robotName) {
		this(robotName, true);
	}

	public PCConnectionHandler(String robotName, boolean bluetooth) {
		this.robotName = robotName;
		this.protocol = bluetooth ? BaseConnectionHandler.BLUETOOTH_PROTOCOL : BaseConnectionHandler.USB_PROTOCOL;
	}

	@Override
	public void run() {
		/**
		 * run() -> Runs the connection. Can be used either in the main thread
		 * or a separate thread:
		 * 
		 * Thread b = new Thread(new PCBluetoothHandler(ROBOT_NAME)); b.start();
		 * 
		 * or
		 * 
		 * PCBluetoothHandler b = new PCBluetoothHandler(ROBOT_NAME)); b.run();
		 * 
		 * Method will retry for NUM_RETRY times and still failing after that
		 * will mean ERROR message
		 * 
		 */

		logger.info("Attempting connection to: " + this.robotName);

		this.status = BaseConnectionHandler.CONNECTING;

		NXTConnector conn = new NXTConnector();

		conn.addLogListener(new NXTCommLogListener() {

			public void logEvent(String message) {
				logger.debug("Bluetooth Log Event. ROBOT: " + robotName + " " + message);
			}

			public void logEvent(Throwable throwable) {
				logger.debug("Bluetooth Throwable. ROBOT: " + robotName, throwable);
			}

		});
		
		int time_delay = 1000;

		for (int retry = 0; retry < PCConnectionHandler.NUM_RETRYS; retry++) {
			boolean sucessful = conn.connectTo(this.protocol + this.robotName);

			if (sucessful) {
				this.status = CONNECTED;
				logger.info(this.robotName + " Connection Established via " + this.protocol);
				input = new MyDataInputStream(conn.getInputStream());
				output = new MyDataOutputStream(conn.getOutputStream());
				return;
			} else {
				this.status = BaseConnectionHandler.RETRYING;
				logger.info("Retrying connection in " + time_delay/1000 + " seconds...");
				try {
					Thread.sleep(time_delay);
				} catch (InterruptedException e) {
					logger.info("Communication thread interrupted", e);
					logger.info("Cleaning Up...");
					return;
				}
				time_delay = Math.min(30000, time_delay * 2);
			}
		}

		this.status = BaseConnectionHandler.DISCONNECTED;
		logger.error(this.robotName + " Error connecting");
	}

	public Communicatable receiveObject(Communicatable obj) throws ConnectionNotEstablishedException {
		logger.info(this.robotName + " Receiving: " + obj.toString());
		return super.receiveObject(obj);
	}

	public void sendObject(Communicatable obj) throws ConnectionNotEstablishedException {
		logger.info(this.robotName + " Sending: " + obj.toString());
		super.sendObject(obj);
	}
}
