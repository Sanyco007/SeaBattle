package net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import seabattle.GameLogic;

public abstract class Connection implements Runnable {

	protected InputStream inputStream;
	protected OutputStream outputStream;
	protected String sendStr = null;
	protected boolean run;
	
	protected boolean connected = false;
	
	private GameLogic game;
	
	public void initialize(GameLogic game) {
		this.game = game;
	}
	
	//Запуск соединения
	public void start() {
		run = true;
		Thread work = new Thread(this);
		work.setDaemon(true);
		work.start();
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public void stop() {
		run = false;
	}
	
	//Отправка данных по сети
	public synchronized void send(String str) {
		sendStr = str;
	}
	
	protected abstract void connect();
	
	@Override
	public void run() {
		try {
			connect();
			while (run) {
				//Считывание и обработка данных
				if (inputStream.available() > 0) {
					byte[] buffer = new byte[inputStream.available()];
					inputStream.read(buffer);
					String line = new String(buffer);
					if (line.equals("ready")) {
						game.setEnemyReady();
					}
					else {
						String[] values = line.split("\\D");
						if (values.length == 3) {
							int x = Integer.parseInt(values[0]);
							int y = Integer.parseInt(values[1]);
							int mode = Integer.parseInt(values[2]);
							game.answer(x, y, mode);
						}
						else if (values.length == 2) {
							int x = Integer.parseInt(values[0]);
							int y = Integer.parseInt(values[1]);
							game.attack(x, y);
						}
						else {
							for (int i = 0; i < values.length; i += 2) {
								int x = Integer.parseInt(values[i]);
								int y = Integer.parseInt(values[i + 1]);
								game.answer(x, y, 4);
							}
						}
					}
				}
				//Отправка данных
				if (sendStr != null) {
					byte[] data = sendStr.getBytes();
					outputStream.write(data);
					sendStr = null;
				}
				Thread.sleep(100);
			}
		} 
		catch (IOException | InterruptedException e) {
			System.err.println(e.getMessage());
		}	
	}
	
}
