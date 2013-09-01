package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import seabattle.GameLogic;

public class MainForm {

	//Графические компоненты
    private JFrame frame;
    private Canvas cUser;
    private Canvas cEnemy;
    private JLabel statusLabel;
    
    //Радиокнопки
    private JRadioButton jrbOne;
    private JRadioButton jrbTwo;
    private JRadioButton jrbThree;
    private JRadioButton jrbFour;
    
    private JRadioButton jrbVertical;
    private JRadioButton jrbHorizontal;
    
    //Кнопки
    private JButton jbClear;
    private JButton jbStart;
    
    //Обект для обеспечения логики игры
    private GameLogic game;
    
    public MainForm(GameLogic game) {
    	this.game = game;
    	initializeComponents();
    	game.setForm(this);
        game.initialize();
        game.startConnection();
    }
    
    private void initializeComponents() {
    	frame = new JFrame("Морской бой");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(710, 460);
        frame.setResizable(false);
        
        JPanel center = new JPanel();
        center.setLayout(null);
        center.add(getShipsPanel());
        center.add(getDirectionPanel());
        center.add(getUserPanel());
        center.add(getEnemyPanel());
        
        //Инициализация и добавление кнопок
        initClearButton();
        initStartButton();
        center.add(jbClear);
        center.add(jbStart);
        
        frame.add(center, BorderLayout.CENTER);
        frame.add(getStatusBar(), BorderLayout.SOUTH);
        
        setScreenCenter();
    }
    
    private JPanel getShipsPanel() {
        JPanel pShips = new JPanel(new GridLayout(0, 1));
        pShips.setBorder(BorderFactory.createTitledBorder("Выбор корабля"));
        pShips.setLocation(15, 10);
        pShips.setSize(150, 135);
        //radio buttons
        jrbOne   = new JRadioButton("Однопалубный");
        jrbTwo   = new JRadioButton("Двухпалубный");
        jrbThree = new JRadioButton("Трехпалубный");
        jrbFour  = new JRadioButton("Четырехпалубный");
        jrbOne.setSelected(true);
        //Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(jrbOne);
        group.add(jrbTwo);
        group.add(jrbThree);
        group.add(jrbFour);
        //add buttons to panel
        pShips.add(jrbOne);
        pShips.add(jrbTwo);
        pShips.add(jrbThree);
        pShips.add(jrbFour);
        return pShips;
    }
        
    private void initClearButton() {
        jbClear = new JButton("Очистить");
        jbClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				game.initialize();
				jrbOne.setEnabled(true);
				jrbTwo.setEnabled(true);
				jrbThree.setEnabled(true);
				jrbFour.setEnabled(true);
				jbStart.setEnabled(false);
				cUser.redraw();
				cEnemy.redraw();
			}
		});
        jbClear.setLocation(30, 260);
        jbClear.setSize(120, 28);
    }
    
    public void disableStart() {
    	jbStart.setEnabled(false);
    }
    
    private void initStartButton() {
        jbStart = new JButton("Старт");
        jbStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jbClear.setEnabled(false);
				game.startGame();
			}
		});
        jbStart.setLocation(30, 360);
        jbStart.setEnabled(false);
        jbStart.setSize(120, 28);
    }
    
    private JPanel getDirectionPanel() {
        JPanel pDirection = new JPanel(new GridLayout(0, 1));
        pDirection.setBorder(BorderFactory.createTitledBorder("Положение корабля"));
        pDirection.setLocation(15, 160);
        pDirection.setSize(150, 75);
        //radio buttons
        jrbVertical = new JRadioButton("Вертикально");
        jrbVertical.setSelected(true);
        jrbHorizontal = new JRadioButton("Горизонтально");
        //Group the radio buttons
        ButtonGroup group = new ButtonGroup();
        group.add(jrbVertical);
        group.add(jrbHorizontal);
        //add buttons to panel
        pDirection.add(jrbVertical);
        pDirection.add(jrbHorizontal);
        return pDirection;
    }
    
    private JPanel getUserPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Корабли игрока"));
        panel.setLayout(null);
        panel.setLocation(185, 10);
        panel.setSize(250, 265);
        cUser = new Canvas(game, false);
        panel.add(cUser);
        return panel;
    }
    
    private JPanel getEnemyPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Корабли врага"));
        panel.setLayout(null);
        panel.setLocation(442, 10);
        panel.setSize(250, 265);
        cEnemy = new Canvas(game, true);
        panel.add(cEnemy);
        return panel;
    }
    
    private JPanel getStatusBar() {
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        frame.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(frame.getWidth(), 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusLabel = new JLabel("Подключение...");
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusPanel.add(statusLabel);
        return statusPanel;
    }
    
    private void setScreenCenter() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
    
    public void redraw() {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			cUser.redraw();
    	    	cEnemy.redraw();
    		}
    	});
    }
    
    public void setStatusMessage(final String message) {
    	SwingUtilities.invokeLater(new Runnable() {
    		public void run() {
    			statusLabel.setText(message);
    		}
    	});
    }
    
    public int getShipLevel() {
    	int level = 0;
    	if (jrbOne.isSelected()) {
			level = 1;
		}
		if (jrbTwo.isSelected()) {
			level = 2;
		}
		if (jrbThree.isSelected()) {
			level = 3;
		}
		if (jrbFour.isSelected()) {
			level = 4;
		}
		return level;
    }
    
    public boolean isHorizontal() {
    	return jrbHorizontal.isSelected();
    }
    
    public void disableRadioButton(int index) {
    	switch (index) {
    		case 1: jrbOne.setEnabled(false);   break;
    		case 2: jrbTwo.setEnabled(false);   break;
    		case 3: jrbThree.setEnabled(false); break;
    		case 4: jrbFour.setEnabled(false);  break;
    	}
    }
    
    public void enableStartButton() {
    	jbStart.setEnabled(true);
    }
        
    public void show() {
        frame.setVisible(true);
    }

}
