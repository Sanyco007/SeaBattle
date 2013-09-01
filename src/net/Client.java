package net;

import java.io.IOException;
import java.net.Socket;

import seabattle.Program;

public class Client extends Connection {

	private Socket socket;
	private String ipAddress;
	
	public Client(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	//��������� ���������� � ��������
	@Override
	protected void connect() {
		try {
			socket = new Socket(ipAddress, 11000);
			inputStream = socket.getInputStream();
			outputStream = socket.getOutputStream();
			Program.mainForm.setStatusMessage("���������� [����������� ��������]");
			connected = true;
		} 
		catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

}
