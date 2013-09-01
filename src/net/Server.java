package net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import seabattle.Program;

public class Server extends Connection implements Runnable {

	@Override
	protected void connect() {
		try {
			Socket socket = new ServerSocket(11000).accept();
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			Program.mainForm.setStatusMessage("Подключено [расстановка кораблей]");
			connected = true;
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
}
