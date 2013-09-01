package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import seabattle.GameLogic;
import seabattle.Program;

public class StartForm {
    
    private JFrame frame;
    private JButton jbCreate;
    private JButton jbConnect;
    private JButton jbExit;
    
    public StartForm() {
        initializeComponents();
    }
    
    //Инициализация компонент
    private void initializeComponents() {
    	frame = new JFrame("Морской бой");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.setSize(380, 240);
        frame.setResizable(false);
        
        jbCreate = new JButton("Создать");
        jbCreate.setSize(180, 40);
        jbCreate.setLocation(100, 30);
        
        jbConnect = new JButton("Подключится");
        jbConnect.setSize(180, 40);
        jbConnect.setLocation(100, 90);
        
        jbExit = new JButton("Выход");
        jbExit.setSize(180, 40);
        jbExit.setLocation(100, 150);
        
        jbCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
                Program.mainForm = new MainForm(new GameLogic());
                Program.mainForm.show();
            }
        });
        
        jbConnect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
               frame.setVisible(false);
               frame.dispose();
               new IPAddressForm().show();
            }
        });
        
        jbExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
                System.exit(0);
            }
        });
        
        frame.add(jbCreate);
        frame.add(jbConnect);
        frame.add(jbExit);
        
        setScreenCenter();
    }
    
    //Установка формы в центре экрана
    private void setScreenCenter() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
    
    //Отображение формы
    public void show() {
        frame.setVisible(true);
    }
    
}
