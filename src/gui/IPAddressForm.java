package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import seabattle.GameLogic;
import seabattle.Program;

public class IPAddressForm {
    
    private JFrame frame;
    private JTextField jtfIPAddress;
    
    public IPAddressForm() {
        frame = new JFrame("Морской бой");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        
        frame.setSize(330, 125);
        frame.setResizable(false);
        
        JLabel label = new JLabel("IP-адрес:");
        label.setLocation(20, 25);
        label.setSize(55, 14);
        
        jtfIPAddress = new JTextField("127.0.0.1");
        jtfIPAddress.setSize(210, 20);
        jtfIPAddress.setLocation(84, 22);
        
        JButton jbConnect = new JButton("Подключение");
        jbConnect.setSize(130, 24);
        jbConnect.setLocation(100, 60);
        
        jbConnect.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
                frame.dispose();
                Program.ipAddress = jtfIPAddress.getText();
                Program.isServer = false;
                Program.mainForm = new MainForm(new GameLogic());
                Program.mainForm.show();
            }
        });
        
        frame.add(label);
        frame.add(jtfIPAddress);
        frame.add(jbConnect);
        
        setScreenCenter();
    }
    
    private void setScreenCenter() {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        frame.setLocation(x, y);
    }
    
    public void show() {
        frame.setVisible(true);
    }
    
}
