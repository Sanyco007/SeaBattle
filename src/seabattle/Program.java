package seabattle;

import gui.MainForm;
import gui.StartForm;

public class Program {

	//√лобальные статические переменные
	public static boolean isServer = true;
	public static String ipAddress;
	public static MainForm mainForm;
	
	//¬ход в программу
    public static void main(String[] args) {
        new StartForm().show();
    }
}
