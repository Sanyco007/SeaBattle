package seabattle;

import gui.MainForm;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import net.Client;
import net.Connection;
import net.Server;

public class GameLogic {
    
	//������ ���� � ����� ������ � ��������
	public static final int WIDTH = 221;
	public static final int H = 22;
	
    //��������� ������ ����
    private final int WATER    = 0;
    private final int SHIP     = 1;
    private final int SHIPRED  = 2;
    private final int ATTACK   = 3;
    private final int SHIPDEAD = 4;

    //������ ����
    private final int SIZE = 10;
    
	//������� ����������
    private Connection conn;
    
    //��� ��� ��� ���
    private boolean turn = false;
    
    //���������� �� ����
    private boolean start = false;
    
    //���������� ���������� � ����
    private boolean enemyReady = false;
    
    //��������� �� ��������� �� ����
    public boolean connected = false;
    
    //���� ��� ����
    private int[][] userField  = new int[SIZE][SIZE];
    private int[][] enemyField = new int[SIZE][SIZE];
    
    //���������� �� ������������� �������� �� �������
    private int one   = 4;
    private int two   = 3;
    private int three = 2;
    private int four  = 1;
    
    private ArrayList<Ship> ships = new ArrayList<>();
    
    private MainForm form;
    
    public void setForm(MainForm form) {
    	this.form = form;
    }
     
    public int[][] getUserField() {
    	return userField;
    }
    
    public int[][] getEnemyField() {
    	return enemyField;
    }
    
    //������������� ��������� ��������
    public void initialize() {
    	one   = 4;
		two   = 3;
		three = 2;
		four  = 1;
		for (int i = 0; i < SIZE; i++) {
    		for (int j = 0; j < SIZE; j++) {
    			userField[i][j]  = WATER;
    			enemyField[i][j] = WATER;
    		}
    	}
		ships.clear();
    }
    
    //������ �������� ����������
    public void startConnection() {
    	if (Program.isServer) {
        	conn = new Server();
        	turn = true;
        }
        else {
        	conn = new Client(Program.ipAddress);
        }
    	conn.initialize(this);
        conn.start();
    }
    
    //����� ����� ������ ����
    public void startGame() {
    	start = true;
    	form.disableStart();
		conn.send("ready");
		if (!enemyReady) {
			form.setStatusMessage("�������� ����������...");
		}
		else if (turn) {
			form.setStatusMessage("��� ���");
		}
		else {
			form.setStatusMessage("��� ����������");
		}
    }
    
    //�������� ����� ������ ����
    public void setEnemyReady() {
    	enemyReady = true;
    	connected = true;
    	if (!start) return;
    	if (turn) {
			form.setStatusMessage("��� ���");
		}
		else {
			form.setStatusMessage("��� ����������");
		}
    }
    
    private void setAttack(int[][] field, int x, int y) {
    	if (x - 1 >= 0 && field[x - 1][y] == WATER) field[x - 1][y] = ATTACK;
		if (x - 1 >= 0 && y - 1 >= 0 && field[x - 1][y - 1] == WATER) field[x - 1][y - 1] = ATTACK;
		if (x - 1 >= 0 && y + 1 < 10 && field[x - 1][y + 1] == WATER) field[x - 1][y + 1] = ATTACK;
		//current
		if (field[x][y] == WATER) field[x][y] = ATTACK;
		if (y - 1 >= 0 && field[x][y - 1] == WATER) field[x][y - 1] = ATTACK;
		if (y + 1 < 10 && field[x][y + 1] == WATER) field[x][y + 1] = ATTACK;
		//right
		if (x + 1 < 10 && y - 1 >= 0 && field[x + 1][y - 1] == WATER) field[x + 1][y - 1] = ATTACK;
		if (x + 1 < 10 && y + 1 < 10 && field[x + 1][y + 1] == WATER)field[x + 1][y + 1] = ATTACK;
		if (x + 1 < 10 && field[x + 1][y] == WATER) field[x + 1][y] = ATTACK;
    }
    
    //����� ���������� ������
    public void attack(int x, int y) {
    	String sendStr = "";
    	if (userField[x][y] == SHIP) {
    		userField[x][y] = SHIPRED;
    		for (int i = 0; i < ships.size(); i++) {
    			ships.get(i).attack(x, y);
    		}
    		for (int i = 0; i < ships.size(); i++) {
    			if (ships.get(i).isDead()) {
    				ArrayList<Point> points = ships.get(i).getPoints();
    				for (Point p : points) {
    					userField[p.x][p.y] = SHIPDEAD;
    					sendStr += "|" + p.x + "|" + p.y;
    					setAttack(userField, p.x, p.y);
    				}
    				if (points.size() == 1) sendStr = "";
    				ships.remove(i--);
    			}
    		}
    	}
    	if (userField[x][y] == WATER) {
    		userField[x][y] = ATTACK;
    	}
    	int mode = userField[x][y];
    	if (sendStr.equals("")) {
    		conn.send(x + "|" + y + "|" + mode);
    	}
    	else {
    		sendStr = sendStr.substring(1);
    		conn.send(sendStr);
    	}
    	if (mode == SHIPRED || mode == SHIPDEAD) {
    		turn = false;
    		form.setStatusMessage("��� ����������");
    	}
    	else {
    		turn = true;
    		form.setStatusMessage("��� ���");
    	}
    	form.redraw();
    	if (ships.size() == 0) {
    		showMessage("�� ���������");
    		conn.stop();
    	}
    }
    
    public void showMessage(String message) {
    	form.redraw();
    	JOptionPane.showMessageDialog(null, message, "InfoBox", JOptionPane.INFORMATION_MESSAGE);
    }
    
    //����� ��������� �� �����
    public void answer(int x, int y, int mode) {
    	enemyField[x][y] = mode;
    	if (mode == SHIPDEAD) {
    		setAttack(enemyField, x, y);
    	}
    	int count = 0;
    	for (int i = 0 ; i < SIZE; i++) {
    		for (int j = 0; j < SIZE; j++) {
    			if (enemyField[i][j] == SHIPDEAD) {
    				count++;
    			}
    		}
    	}
    	if (count == 20) {
    		showMessage("�� ��������");
    		conn.stop();
    	}
    	if (mode == SHIPRED || mode == SHIPDEAD) {
    		turn = true;
    		form.setStatusMessage("��� ���");
    	}
    	else {
    		form.setStatusMessage("��� ����������");
    	}
    	form.redraw();
    }
    
    public void mouseDown(int x, int y, boolean isEnemyField) {
    	//������������ ����� ��������
    	if (isEnemyField) {
			if (!connected || !start) return;
			if (turn) {
				if (x < 10 && y < 10 && (enemyField[x][y] == WATER)) {
					turn = false;
					String coords = x + "|" + y;
					conn.send(coords);
				}
			}
		}
    	//������������ ���������� �������
		else {
			//����������� ���� �������
			int count = form.getShipLevel();
			if (count == 1 && one   == 0) return;
			if (count == 2 && two   == 0) return;
			if (count == 3 && three == 0) return;
			if (count == 4 && four  == 0) return;
			//���������� ��������������� �������, ���� ��������
			if (form.isHorizontal()) {
				boolean result = true;
				for (int i = 0; i < count; i++) {
					if (!checkCell(x + i, y)) {
						result = false;
						break;
					}
				}
				if (result) {
					Ship ship = new Ship();
					for (int i = 0; i < count; i++) {
						userField[x + i][y] = SHIP;
						ship.addPoint(x + i, y);
					}
					ships.add(ship);
					//���� ������� ��������
					if (count == 1) one--;
					if (count == 2) two--;
					if (count == 3) three--;
					if (count == 4) four--;
				}
			}
			//���������� ������������� �������, ���� ��������
			else {
				boolean result = true;
				for (int i = 0; i < count; i++) {
					if (!checkCell(x, y + i)) {
						result = false;
						break;
					}
				}
				if (result) {
					Ship ship = new Ship();
					for (int i = 0; i < count; i++) {
						userField[x][y + i] = SHIP;
						ship.addPoint(x, y + i);
					}
					ships.add(ship);
					//���� ������� ��������
					if (count == 1) one--;
					if (count == 2) two--;
					if (count == 3) three--;
					if (count == 4) four--;
				}
			}
			//���������� ������� ��������, ���� ��� ����������
			if (one == 0) {
				form.disableRadioButton(1);
			}
			if (two == 0) {
				form.disableRadioButton(2);
			}
			if (three == 0) {
				form.disableRadioButton(3);
			}
			if (four == 0) {
				form.disableRadioButton(4);
			}
			if (one == 0 && two == 0 && three == 0 && four == 0) {
				form.enableStartButton();
			}
		}
		form.redraw();
    }
    
    //�������� ���������� ������������� ������ � ���� ������
    private boolean checkCell(int x, int y) {
		boolean result = true;
		int[][] field = userField;
		try {
			//left
			if (x - 1 >= 0 && field[x - 1][y] != WATER) result = false;
			if (x - 1 >= 0 && y - 1 >= 0 && field[x - 1][y - 1] != WATER) result = false;
			if (x - 1 >= 0 && y + 1 < 10 && field[x - 1][y + 1] != WATER) result = false;
			//current
			if (field[x][y] != WATER) result = false;
			if (y - 1 >= 0 && field[x][y - 1] != WATER) result = false;
			if (y + 1 < 10 && field[x][y + 1] != WATER) result = false;
			//right
			if (x + 1 < 10 && y - 1 >= 0 && field[x + 1][y - 1] != WATER) result = false;
			if (x + 1 < 10 && y + 1 < 10 && field[x + 1][y + 1] != WATER) result = false;
			if (x + 1 < 10 && field[x + 1][y] != WATER) result = false;
		}
		catch (Exception e) { 
			result = false; 
		}
		return result;
	}
    
    public void draw(Graphics gr, boolean isEnemyField) {
    	int[][] field = isEnemyField ? enemyField : userField;
    	gr.setColor(Color.blue);
        gr.fillRect(0, 0, WIDTH, WIDTH);
        for (int i = 0; i < SIZE; i++) {
        	for (int j = 0; j < SIZE; j++) {
        		if (field[i][j] == SHIP) {
        			gr.setColor(Color.yellow);
        			gr.fillRect(i * H, j * H, H, H);
        		}
        		if (field[i][j] == SHIPRED) {
        			gr.setColor(Color.red);
        			gr.fillRect(i * H, j * H, H, H);
        		}
        		if (field[i][j] == ATTACK) {
        			
        			gr.setColor(Color.gray);
        			gr.fillRect(i * H + 10, j * H + 10, 4, 4);
        		}
        		if (field[i][j] == SHIPDEAD) {
        			gr.setColor(Color.lightGray);
        			gr.fillRect(i * H, j * H, H, H);
        		}
        	}
        }
        gr.setColor(Color.black);
        for (int i = 0; i < WIDTH; i += H) {
        	gr.drawLine(i, 0, i, WIDTH);
        }
        for (int i = 0; i < WIDTH; i += H) {
        	gr.drawLine(0, i, WIDTH - 1, i);
        }
    }
	
}
