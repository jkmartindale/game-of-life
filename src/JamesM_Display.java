import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JSlider;
import javax.swing.JComponent;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/* Future Goals:
 * Import Rulestrings
 * Embed gamemodes into saves
 * About + help screen
 * Import other filetypes
 * http://www.mirekw.com/ca/ca_files_formats.html
 * Library function
 * Population starts immediately
 * Dynamically change board size
 * Infinite board size
 * http://www.conwaylife.com/wiki/Rulestring#Rules
 */
@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class JamesM_Display extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener
{
	//Window Stuff
	public static final int ROWS = 80;	//For the grid
	public static final int COLS = 100;	//
	final int X_GRID_OFFSET = 12;	//Margin between window
	final int Y_GRID_OFFSET = 12;	//and grid
	final int CELL_WIDTH = 6;
	final int CELL_HEIGHT = 6;
	final int DISPLAY_WIDTH;	//Window size
	final int DISPLAY_HEIGHT;	//(Not yet able to be changed)
	final JFileChooser picker; 	//File picker

	//UI Elements + Data Model
	StartButton startStop;
	Step step;
	Speed speedSlider;
	Load load;
	Clear clear;
	Save save;
	Settings customize;
	boolean simulationOn = false;
	boolean editing = false;
	int speed = 50;
	int generations = 0;
	int population = 0;
	Cell[][] grid;	//Stores the cells
	int button; 	//Used to detect mouse button
	int preset = 0;	//Used to store preset used

	//Game presets ({birth},{survives})
	static final boolean[][] conway = {{false, false, false, true, false, false, false, false, false},{false, false, true, true, false, false, false, false, false}};
	static final boolean[][] dayAndNight = {{false, false, false, true, false, false, true, true, true},{false, false, false, true, true, false, true, true, true}};
	static final boolean[][] highLife = {{false, false, false, true, false, false, true, false, false},{false, false, true, true, false, false, false, false, false}};
	static final boolean[][] lifeWithoutDeath = {{false, false, false, true, false, false, false, false, false},{true, true, true, true, true, true, true, true, true}};
	static final boolean[][] seeds = {{false, false, true, false, false, false, false, false, false},{false, false, false, false, false, false, false, false, false}};
	static boolean[][] rules = conway;

	//Editing interface (I didn't have the time to make a separate window)
	EditBox b0;
	EditBox b1;
	EditBox b2;
	EditBox b3;
	EditBox b4;
	EditBox b5;
	EditBox b6;
	EditBox b7;
	EditBox b8;
	EditBox s0;
	EditBox s1;
	EditBox s2;
	EditBox s3;
	EditBox s4;
	EditBox s5;
	EditBox s6;
	EditBox s7;
	EditBox s8;
	Gamerules presets;

	public JamesM_Display(int width, int height) 
	{
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;

		//Initialize the grid
		grid = new Cell[ROWS][COLS];
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLS; col++)
				grid[row][col] = new Cell();

		makeUI();

		//Customize the file picker
		picker = new JFileChooser();
		picker.setFileFilter(new ImageFilter());
		picker.setAcceptAllFileFilterUsed(false);
	}

	void makeUI()
	{
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		startStop = new StartButton();
		startStop.setBounds(12, 572, 100, 36);
		startStop.setBackground(Color.WHITE);
		add(startStop);

		step = new Step();
		step.setBounds(112,572,100,36);
		step.setBackground(Color.WHITE);
		add(step);

		speedSlider = new Speed();
		speedSlider.setBounds(212,573,200,35);
		speedSlider.setBackground(Color.WHITE);
		add(speedSlider);

		clear = new Clear();
		clear.setBounds(413,572,100,36);
		clear.setBackground(Color.WHITE);
		add(clear);

		load = new Load();
		load.setBounds(513,572,50,36);
		load.setBackground(Color.WHITE);
		add(load);

		save = new Save();
		save.setBounds(563,572,50,36);
		save.setBackground(Color.WHITE);
		add(save);

		customize = new Settings();
		customize.setBounds(613,572,100,36);
		customize.setBackground(Color.WHITE);
		add(customize);

		b0 = new EditBox();
		b0.setBounds(42, 595, 20, 20);
		add(b0);
		b1 = new EditBox();
		b1.setBounds(62, 595, 20, 20);
		add(b1);
		b2 = new EditBox();
		b2.setBounds(82, 595, 20, 20);
		add(b2);
		b3 = new EditBox();
		b3.setBounds(102, 595, 20, 20);
		add(b3);
		b4 = new EditBox();
		b4.setBounds(122, 595, 20, 20);
		add(b4);
		b5 = new EditBox();
		b5.setBounds(142, 595, 20, 20);
		add(b5);
		b6 = new EditBox();
		b6.setBounds(162, 595, 20, 20);
		add(b6);
		b7 = new EditBox();
		b7.setBounds(182, 595, 20, 20);
		add(b7);
		b8 = new EditBox();
		b8.setBounds(202, 595, 20, 20);
		add(b8);
		s0 = new EditBox();
		s0.setBounds(295, 595, 20, 20);
		add(s0);
		s1 = new EditBox();
		s1.setBounds(315, 595, 20, 20);
		add(s1);
		s2= new EditBox();
		s2.setBounds(335, 595, 20, 20);
		add(s2);
		s3 = new EditBox();
		s3.setBounds(355, 595, 20, 20);
		add(s3);
		s4 = new EditBox();
		s4.setBounds(375, 595, 20, 20);
		add(s4);
		s5 = new EditBox();
		s5.setBounds(395, 595, 20, 20);
		add(s5);
		s6 = new EditBox();
		s6.setBounds(415, 595, 20, 20);
		add(s6);
		s7 = new EditBox();
		s7.setBounds(435, 595, 20, 20);
		add(s7);
		s8 = new EditBox();
		s8.setBounds(455, 595, 20, 20);
		add(s8);

	  	presets = new Gamerules();
	  	presets.setBounds(478, 572, 135, 36);
	  	presets.setBackground(Color.WHITE);
	  	presets.setVisible(false);
	  	add(presets);

		repaint();
	}

	void drawGrid(Graphics g)
	{
		//Draw the lines
		for (int row = 0; row <= ROWS; row++)
			g.drawLine(X_GRID_OFFSET, Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET + COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)));
		for (int col = 0; col <= COLS; col++)
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET, X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET + ROWS * (CELL_HEIGHT + 1));

		//Draw the cells
		g.setColor(Color.BLACK);
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLS; col++)
				if (grid[row][col].alive)
					g.fillRect(13 + 7 * col, 13 + 7 * row, 6, 6);
	}

	public void paintComponent(Graphics g)
	{
		g.setColor(new Color(142,142,142));
		drawGrid(g);

		if (simulationOn)
		{
			try
			{
				Thread.sleep(150 - speed);	//Convert the speed to a sleep time (inverse relationship)
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
			}

			runGame();
		}

		if (editing)
		{
			//Draw birth numbers
			g.drawString("0", 49, 590);
			g.drawString("1", 69, 590);
			g.drawString("2", 89, 590);
			g.drawString("3", 109, 590);
			g.drawString("4", 129, 590);
			g.drawString("5", 149, 590);
			g.drawString("6", 169, 590);
			g.drawString("7", 189, 590);
			g.drawString("8", 209, 590);

			//Draw survival numbers
			g.drawString("0", 302, 590);
			g.drawString("1", 322, 590);
			g.drawString("2", 342, 590);
			g.drawString("3", 362, 590);
			g.drawString("4", 382, 590);
			g.drawString("5", 402, 590);
			g.drawString("6", 422, 590);
			g.drawString("7", 442, 590);
			g.drawString("8", 462, 590);

			//Draw labels
			g.drawString("Birth:", 12, 610);
			g.drawString("Survival:", 245, 610);
		}
		else
			//Status bar
			g.drawString("Generations: " + generations + "	 Population: " + population + "	 Speed: " + speed, 12, 630);

		repaint();
	}

	void runGame()
	{
		//Determine whether a cell lives or dies
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLS; col++)
			{
				int neighbors = findNeighbors(row, col);
				if (grid[row][col].alive == false)	//Check for birth
						grid[row][col].survives = rules[0][neighbors];
				else								//Check for survival
					grid[row][col].survives = rules[1][neighbors];
			}

		//Save the changes to the grid
		//(Conway's game is simultaneous, not sequential)
		population = 0;
		for (int row = 0; row < ROWS; row++)
			for (int col = 0; col < COLS; col++)
			{
				grid[row][col].alive = grid[row][col].survives;
				if (grid[row][col].alive)
					population++;
			}
		generations++;
	}

	int findNeighbors(int row, int col)
	{
		int count = 0;

		//Test each neighbor, starting at the top-left and going clockwise
		if (grid[(row - 1 + ROWS) % ROWS][(col - 1 + COLS) % COLS].alive)
			count++;
		if (grid[(row - 1 + ROWS) % ROWS][col].alive)
			count++;
		if (grid[(row - 1 + ROWS) % ROWS][(col + 1 + COLS) % COLS].alive)
			count++;
		if (grid[row][(col - 1 + COLS) % COLS].alive)
			count++;
		if (grid[row][(col + 1 + COLS) % COLS].alive)
			count++;
		if (grid[(row + 1 + ROWS) % ROWS][(col - 1 + COLS) % COLS].alive)
			count++;
		if (grid[(row + 1 + ROWS) % ROWS][col].alive)
			count++;
		if (grid[(row + 1 + ROWS) % ROWS][(col + 1 + COLS) % COLS].alive)
			count++;
		return count;
	}

	int findCellX(int x)
	{
		if (x < 12 || x > 711)
			return -1;
		x -= 12;
		return x / 7;
	}

	int findCellY(int y)
	{
		if (y < 12 || y > 571)
			return -1;
		y -= 12;
		return y / 7;
	}

	public void mousePressed(MouseEvent e)
	{
		button = e.getButton();

		//Middle-click: Make random playing field
		if (button == 2)
		{
			for (int row = 0; row < ROWS; row++)
				for (int col = 0; col < COLS; col++)
				{
					if (Math.random() < 0.5)
						grid[row][col].alive = false;
					else
						grid[row][col].alive = true;
				}
		}

		//MouseDragged events have no mouse button value, so the
		//MousePressed event is used to kickstart cell editing
		//by coloring the first cell (in case the cursor isn't dragged)
		//and then setting the mouse button value for MouseDragged
		//to read
		int row = findCellY(e.getY());
		int col = findCellX(e.getX());
		if (row != -1 && col != -1)
		{
			if (button == 1)
				grid[row][col].alive = true;
			if (button == 3)
				grid[row][col].alive = false;
		}
	}

	public void mouseDragged(MouseEvent e) 
	{
		int row = findCellY(e.getY());
		int col = findCellX(e.getX());
		if (row != -1 && col != -1)
		{
			if (button == 1)
				grid[row][col].alive = true;
			if (button == 3)
				grid[row][col].alive = false;
		}
	}
    
	//Binds the speed and speed slider to the mouse wheel
	public void mouseWheelMoved(MouseWheelEvent e)
	{
		int difference = e.getWheelRotation() * -10;
		speed = speed + difference;
		if (speed < 10)
			speed = 10;
		if (speed > 150)
			speed = 150;
		speedSlider.setValue(speed);
	}

	//Check the checkboxes
	public void actionItemChanged(ItemEvent e)
	{
		Object source = e.getItemSelectable();
		if (source == b0)
			rules[0][0] = b0.isSelected();
		else if (source == b1)
			rules[0][1] = b1.isSelected();
		else if (source == b2)
			rules[0][2] = b2.isSelected();
		else if (source == b3)
			rules[0][3] = b3.isSelected();
		else if (source == b4)
			rules[0][4] = b4.isSelected();
		else if (source == b5)
			rules[0][5] = b5.isSelected();
		else if (source == b6)
			rules[0][6] = b6.isSelected();
		else if (source == b7)
			rules[0][7] = b7.isSelected();
		else if (source == b8)
			rules[0][8] = b8.isSelected();
		else if (source == s0)
			rules[1][0] = s0.isSelected();
		else if (source == s1)
			rules[1][1] = s1.isSelected();
		else if (source == s2)
			rules[1][2] = s2.isSelected();
		else if (source == s3)
			rules[1][3] = s3.isSelected();
		else if (source == s4)
			rules[1][4] = s4.isSelected();
		else if (source == s5)
			rules[1][5] = s5.isSelected();
		else if (source == s6)
			rules[1][6] = s6.isSelected();
		else if (source == s7)
			rules[1][7] = s7.isSelected();
		else if (source == s8)
			rules[1][8] = s8.isSelected();
		presets.setSelectedIndex(5);
	}

	//UI Element classes
	private class StartButton extends JButton implements ActionListener
	{
		public StartButton()
		{
			super("Start");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0)
		{
			simulationOn = !simulationOn;

			if (simulationOn)
			{
				setText("Stop");
				step.setEnabled(false);
				clear.setEnabled(false);
				load.setEnabled(false);
				save.setEnabled(false);
				customize.setEnabled(false);
			}
			else
			{
				step.setEnabled(true);
				clear.setEnabled(true);
				load.setEnabled(true);
				save.setEnabled(true);
				customize.setEnabled(true);
				setText("Start");
			}

			repaint();
		}
	}
	private class Step extends JButton implements ActionListener
	{
		public Step() 
		{
			super("Step");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) 
		{
			runGame();
			repaint();
		}
	}
	private class Speed extends JSlider implements ChangeListener
	{
		public Speed() 
		{
			super(0, 150, 50);
			addChangeListener((ChangeListener) this);
		}

		public void stateChanged(ChangeEvent e) 
		{
			speed = speedSlider.getValue();
			repaint();
		}
	}
	private class Clear extends JButton implements ActionListener
	{
		public Clear() 
		{
			super("Clear");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) 
		{
			for (int row = 0; row < ROWS; row++)
				for (int col = 0; col < COLS; col++)
					grid[row][col] = new Cell();
			generations = 0;
			population = 0;
			repaint();
		}
	}
	private class Load extends JButton implements ActionListener
	{
		public Load() 
		{
			super(new ImageIcon("load.png"));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e) 
		{
			//Show the file picker and open an image
			int returnVal = picker.showDialog(JamesM_Display.this, "Import");
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = picker.getSelectedFile();
				try
				{
					//Read the blue channel of the image (it's the easiest)
					BufferedImage image = ImageIO.read(file);
					int height = ROWS * 10 + 1;
					int width = COLS * 10 + 1;
					if (image.getHeight() < height)
						height = image.getHeight();
					if (image.getWidth() < width)
						width = image.getWidth();
					for (int x = 1; x < width; x += 10)		//Loop through the image, reading each cell
						for (int y = 1; y < height; y+= 10)	//If the image is black (saves are transparent)
							if ((image.getRGB(x, y) & 0xFF) < 50)
								grid[y / 10][x / 10].alive = true;
				}
				catch (IOException error)
				{
					error.printStackTrace();
				}
			}

			repaint();
		}
	}
	private class Save extends JButton implements ActionListener
	{
		public Save() 
		{
			super(new ImageIcon("save.png"));
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			//Generate image container
			BufferedImage image = new BufferedImage(COLS * 10 + 1, ROWS * 10 + 1, BufferedImage.TYPE_INT_RGB);
			image.createGraphics();
			Graphics graphics = image.getGraphics();
			graphics.setColor(new Color(142,142,142));
			graphics.fillRect(0, 0, COLS * 10 + 1, ROWS * 10 + 1);

			//Make photo
			for (int row = 0; row < ROWS; row++)
				for (int col = 0; col < COLS; col++)
				{
					if (grid[row][col].alive)
						graphics.setColor(Color.BLACK);
					else
						graphics.setColor(Color.WHITE);
					graphics.fillRect(1 + 10 * col, 1 + 10 * row, 9, 9);
				}

			//Save it
			int returnVal = picker.showSaveDialog(JamesM_Display.this);
			if (returnVal == JFileChooser.APPROVE_OPTION)
			{
				File file = picker.getSelectedFile();
				if (!(file.getName().toLowerCase().endsWith(".png")))	//Guarantee .png extension
					file = new File(file + ".png");
				try
				{
					ImageIO.write((RenderedImage) image, "png", file);
				}
				catch (IOException i)
				{
					i.printStackTrace();
				}
			}
			repaint();
		}
	}
	private class Settings extends JButton implements ActionListener
	{
		public Settings() 
		{
			super("Customize");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			editing = !editing;
			if (editing)
			{
				setText("Done");
				startStop.setVisible(false);
				step.setVisible(false);
				speedSlider.setVisible(false);
				clear.setVisible(false);
				load.setVisible(false);
				save.setVisible(false);
				b0.setVisible(true);
				b1.setVisible(true);
				b2.setVisible(true);
				b3.setVisible(true);
				b4.setVisible(true);
				b5.setVisible(true);
				b6.setVisible(true);
				b7.setVisible(true);
				b8.setVisible(true);
				s0.setVisible(true);
				s1.setVisible(true);
				s2.setVisible(true);
				s3.setVisible(true);
				s4.setVisible(true);
				s5.setVisible(true);
				s6.setVisible(true);
				s7.setVisible(true);
				s8.setVisible(true);
				presets.setVisible(true);
			}
			else
			{
				startStop.setVisible(true);
				step.setVisible(true);
				speedSlider.setVisible(true);
				clear.setVisible(true);
				load.setVisible(true);
				save.setVisible(true);
				b0.setVisible(false);
				b1.setVisible(false);
				b2.setVisible(false);
				b3.setVisible(false);
				b4.setVisible(false);
				b5.setVisible(false);
				b6.setVisible(false);
				b7.setVisible(false);
				b8.setVisible(false);
				s0.setVisible(false);
				s1.setVisible(false);
				s2.setVisible(false);
				s3.setVisible(false);
				s4.setVisible(false);
				s5.setVisible(false);
				s6.setVisible(false);
				s7.setVisible(false);
				s8.setVisible(false);
				presets.setVisible(false);
				setText("Customize");
			}
		}
	}
	private class Gamerules extends JComboBox implements ActionListener 
	{
		public Gamerules()
		{
			super(new String[] {"Conway's Game of Life", "Day and Night", "High Life", "Life Without Death", "Seeds", "Custom"});
			setSelectedIndex(preset);
			b0.setSelected(rules[0][0]);
			b1.setSelected(rules[0][1]);
			b2.setSelected(rules[0][2]);
			b3.setSelected(rules[0][3]);
			b4.setSelected(rules[0][4]);
			b5.setSelected(rules[0][5]);
			b6.setSelected(rules[0][6]);
			b7.setSelected(rules[0][7]);
			b8.setSelected(rules[0][8]);
			s0.setSelected(rules[1][0]);
			s1.setSelected(rules[1][1]);
			s2.setSelected(rules[1][2]);
			s3.setSelected(rules[1][3]);
			s4.setSelected(rules[1][4]);
			s5.setSelected(rules[1][5]);
			s6.setSelected(rules[1][6]);
			s7.setSelected(rules[1][7]);
			s8.setSelected(rules[1][8]);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			switch (presets.getSelectedIndex())
			{
				case 0: rules = conway; break;
				case 1: rules = dayAndNight; break;
				case 2: rules = highLife; break;
				case 3: rules = lifeWithoutDeath; break;
				case 4: rules = seeds; break;
			}
			b0.setSelected(rules[0][0]);
			b1.setSelected(rules[0][1]);
			b2.setSelected(rules[0][2]);
			b3.setSelected(rules[0][3]);
			b4.setSelected(rules[0][4]);
			b5.setSelected(rules[0][5]);
			b6.setSelected(rules[0][6]);
			b7.setSelected(rules[0][7]);
			b8.setSelected(rules[0][8]);
			s0.setSelected(rules[1][0]);
			s1.setSelected(rules[1][1]);
			s2.setSelected(rules[1][2]);
			s3.setSelected(rules[1][3]);
			s4.setSelected(rules[1][4]);
			s5.setSelected(rules[1][5]);
			s6.setSelected(rules[1][6]);
			s7.setSelected(rules[1][7]);
			s8.setSelected(rules[1][8]);
		}
	}
	private class EditBox extends JCheckBox implements ActionListener
	{
		public EditBox()
		{
			setBackground(Color.WHITE);
			setVisible(false);
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent e)
		{
			EditBox source = (EditBox) e.getSource();
			if (source == b0)
				rules[0][0] = b0.isSelected();
			else if (source == b1)
				rules[0][1] = b1.isSelected();
			else if (source == b2)
				rules[0][2] = b2.isSelected();
			else if (source == b3)
				rules[0][3] = b3.isSelected();
			else if (source == b4)
				rules[0][4] = b4.isSelected();
			else if (source == b5)
				rules[0][5] = b5.isSelected();
			else if (source == b6)
				rules[0][6] = b6.isSelected();
			else if (source == b7)
				rules[0][7] = b7.isSelected();
			else if (source == b8)
				rules[0][8] = b8.isSelected();
			else if (source == s0)
				rules[1][0] = s0.isSelected();
			else if (source == s1)
				rules[1][1] = s1.isSelected();
			else if (source == s2)
				rules[1][2] = s2.isSelected();
			else if (source == s3)
				rules[1][3] = s3.isSelected();
			else if (source == s4)
				rules[1][4] = s4.isSelected();
			else if (source == s5)
				rules[1][5] = s5.isSelected();
			else if (source == s6)
				rules[1][6] = s6.isSelected();
			else if (source == s7)
				rules[1][7] = s7.isSelected();
			else if (source == s8)
				rules[1][8] = s8.isSelected();
			presets.setSelectedIndex(5);
		}
	}
	//Empty (but unfortunately, required) mouse event handlers
	public void mouseMoved(MouseEvent e)
	{
	}
	public void mouseClicked(MouseEvent e)
	{
	}
	public void mouseEntered(MouseEvent e)
	{
	}
	public void mouseExited(MouseEvent e)
	{
	}
	public void mouseReleased(MouseEvent e)
	{
	}
}