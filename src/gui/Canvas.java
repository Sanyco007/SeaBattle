package gui;

import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import seabattle.GameLogic;

public class Canvas extends JPanel {
    
	private static final long serialVersionUID = 1L;

	private GameLogic game;
	private boolean enemyField = false;
	
	public Canvas(GameLogic _game, boolean _enemyField) {
        super();
        this.game = _game;
        this.enemyField = _enemyField;
        setDoubleBuffered(true);
        setSize(GameLogic.WIDTH, GameLogic.WIDTH);
        setLocation(16, 29);
        addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				int x = e.getX() / GameLogic.H;
				int y = e.getY() / GameLogic.H;
				game.mouseDown(x, y, enemyField);
			}
		});
    }
    
    public void redraw() {
        repaint();
    }
    
    @Override
    public void paint(Graphics gr) {
        game.draw(gr, enemyField);
    }
    
}
