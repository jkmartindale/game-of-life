import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class JamesM_GraphicsStartMain
{
	static final int DISPLAY_WIDTH = 729 + 12;
	static final int DISPLAY_HEIGHT = 680;

	public static void main(String[] args)
	{
		JamesM_Display display = new JamesM_Display(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		JFrame frame = new JFrame();
		frame.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		frame.setTitle("Conway's Game of Life");
		ImageIcon icon = new ImageIcon("icon.png");
		frame.setIconImage(icon.getImage());
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setLayout(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(display);
		frame.setVisible(true);
	}
}