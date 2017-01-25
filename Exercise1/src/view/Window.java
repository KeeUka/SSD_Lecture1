package view;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Window extends JFrame{

	private JLabel scoreLabel;
	
	public Window() {
		setSize(new Dimension(200,200));
		setLayout(new BorderLayout());
		scoreLabel = new JLabel("Score");
		add(scoreLabel);
		pack();
		setVisible(true);
	}
	
	public static void main(String[] args) {
		Window w = new Window();
	}
	
}
