package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;

import main.WorldGenerator.Tile;

/**
 * @Description: Renders the heightmap
 * 
 */

public class Main extends JFrame{
	private static final long serialVersionUID = 1L;

	// Powers of 2 from 4 to 256
	public static int GRID_SIZE = 128;
	public static int PIXEL_SIZE = (1024 / GRID_SIZE);

	public static boolean COLOR = true;

	public static int X_OFFSET = 5;
	public static int Y_OFFSET = 40;

	private WorldGenerator generator;
	private Tile heights[][];
	private int seaLevel = 120;
	private int seed;

	public void generateWorld() {
		seed = new Random().nextInt(10000000);
		heights = generator.genHeights(seed);
	}

	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {

				float height = heights[i][j].height;

				if (COLOR) {
					if (height > seaLevel) {
						g.setColor(new Color(0, (int)height, 0));
					} else {
						g.setColor(new Color(0, 55, 255));
					}
				}else {
					g.setColor(new Color((int)height, (int)height, (int)height));
				}

				g.fillRect(i * PIXEL_SIZE + X_OFFSET, j * PIXEL_SIZE + Y_OFFSET, PIXEL_SIZE, PIXEL_SIZE);
			}
		}
	}

	/* Constructor Method */
	public Main() {

		// Basic Java Stuff
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("World Generation Test");
		this.setResizable(false);
		this.setSize(GRID_SIZE * PIXEL_SIZE + 10, GRID_SIZE * PIXEL_SIZE + 45);
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// If the space bar is pressed, regenerate the world
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					generateWorld();
					repaint();
				}
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					repaint();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
			}
		});

		generator = new WorldGenerator(GRID_SIZE);

		// Self Explanatory
		this.generateWorld();
		
		this.setVisible(true);
	}

	/* Main Method */
	public static void main(String[] args) {
		new Main();
	}
}
