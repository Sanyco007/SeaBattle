package seabattle;

import gui.MainForm;
import gui.StartForm;

public class Program {

	//���������� ����������� ����������
	public static boolean isServer = true;
	public static String ipAddress;
	public static MainForm mainForm;
	
	//���� � ���������
    public static void main(String[] args) {
        new StartForm().show();
    }
}
