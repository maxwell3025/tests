package tests;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class tests extends JPanel implements MouseMotionListener, KeyListener, Runnable, MouseListener {
	BufferedImage screenbuffer = new BufferedImage(1000, 1000, BufferedImage.TYPE_INT_ARGB);
	Graphics2D graphics = screenbuffer.createGraphics();
	int res = 256;
	boolean mode = true;
	double x0;
	double y0;
	double x1;
	double y1;
	double floorres = 5;
	double fogchange = 0;
	double fogdist = 2;
	double cloudroll = 0;
	double health = 100;
	List<healthpack> healthpacks = new ArrayList<healthpack>();
	int wait = 0;
	int movetype = 0;
	int brickres = 1;
	int renderdist = 400;
	int movedtime = 0;
	int age = 0;
	int enemies = 0;
	int startage;
	int agediff;
	int shootwait = 1000000;
	double skyheight = 1;
	double reflectiveness = 1;
	spritepoint[] sprites = { new spritepoint(1.5, 1.5, 0, this), new spritepoint(3.5, 1.5, 1, this),
			new spritepoint(5.5, 1.0, 0, this), new spritepoint(5.5, 1.5, 0, this), new spritepoint(5.5, 2.0, 0, this),
			new spritepoint(5.5, 1.25, 0, this), new spritepoint(5.5, 1.75, 0, this),
			new spritepoint(7.5, 5.5, 12, this), new spritepoint(5.5, 5.5, 12, this), new spritepoint(7, 5.5, 12, this),
			new spritepoint(6, 5.5, 12, this), new spritepoint(6.5, 5.5, 12, this) };
	List<spritepoint> touse;
	Mine[] mines = new Mine[sprites.length];
	Color[][][] floorbuf = new Color[10][res][res];
	BufferedImage[][] textures = new BufferedImage[20][2];
	Color[][][][] rasters = new Color[10][2][res][res];
	Color[][][] spriterasters = new Color[20][res][res];
	BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
	Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursorImg, new Point(0, 0), "blank cursor");
	boolean[] ispressed = new boolean[525];
	Color[][] skygraphics;

	Color fogcolor = new Color(0, 0, 0);
	double fovinv = 2;
	double fov = 0.5;
	double a = 5.2;
	double abuf = a;
	double playerheight;
	double fattness = 1;
	double myx = Math.random() * 1000;
	double myy = Math.random() * 1000;
	double myybuf = myy;
	double myxbuf = myx;
	double mps = 0.25;
	double mpt = mps * 0.01;
	boolean movingmouse = false;
	boolean donewithframe = false;
	boolean doneyet = false;
	boolean flashon = false;
	boolean haspow = true;
	boolean dead = false;
	boolean button1;
	int tohit;
	double power = 10000;
	BufferedImage overlay;
	BufferedImage overlay2;
	int threadnum = 0;
	JFrame panelon = new JFrame();
	int[][] mapy = new int[1000][1000];// {{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
										// 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 1, 2, 1, 3, 1, 4, 1, 1, 1, 0, 0, 1 },
	// { 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
	// { 1, 1, 0, 1, 0, 1, 0, 1, 0, 1, 0, 0, 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 0, 1 },
	// { 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
	// { 1, 1, 0, 1, 0, 1, 0, 0, 0, 1, 0, 0, 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1 },
	// { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0, 1 }};

	public tests() {
		touse=new ArrayList<spritepoint>();
		for(spritepoint a:sprites){
			touse.add(a);
		}
		for (int x = 0; x < 1000; x++) {
			for (int y = 0; y < 1000; y++) {
				if (Math.random() < 0.01) {
					mapy[x][y] = 1;
				}

			}
		}
		panelon.add(this);
		if (mode) {
			fogcolor = new Color(230, 230, 255);
		}
		setSize(1000, 1000);
		setVisible(true);
		panelon.addMouseMotionListener(this);
		panelon.addMouseListener(this);
		panelon.addKeyListener(this);
		screenbuffer.setAccelerationPriority(1);

		for (int i = 0; i < touse.size(); i++) {
			if (touse.get(i).value == 0)
				mines[i] = new Mine(this, touse.get(i));
		}
		while (mapy[(int) Math.floor(myxbuf)][(int) Math.floor(myybuf)] != 0) {
			myxbuf = Math.random() * 1000;
			myybuf = Math.random() * 1000;
		}
		for (int i = 0; i < touse.size(); i++) {

			if (touse.get(i).value == 0)
				new Thread(mines[i]).start();
		}
		updategraphics();
		panelon.setDefaultCloseOperation(3);
		panelon.setLocation(0, 0);
		panelon.getContentPane().setCursor(blankCursor);
		panelon.getContentPane().setPreferredSize(new Dimension(1080, 720));
		panelon.pack();
		panelon.setLocationRelativeTo(null);
		panelon.setVisible(true);

	}

	public void updategraphics() {
		try {
			overlay = ImageIO.read(getClass().getClassLoader().getResource("images/overlay.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			overlay2 = ImageIO.read(getClass().getClassLoader().getResource("images/overlay2.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		skygraphics = loadgraphics("images/sky.png", res * 4);
		for (int i = 0; i < 10; i++) {

			URL image0 = this.getClass().getClassLoader().getResource("images/image" + i + "-0.png");
			URL image1 = this.getClass().getClassLoader().getResource("images/image" + i + "-1.png");
			try {
				BufferedImage img0 = ImageIO.read(image0);
				BufferedImage img1 = ImageIO.read(image1);
				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_ARGB);
				textures[i][1] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_ARGB);
				if (i == 1) {
					for (int x = 0; x < brickres; x++) {
						for (int y = 0; y < brickres; y++) {
							textures[i][0].createGraphics().drawImage(img0,
									(int) Math.ceil((double) res / brickres * x) + 1,
									(int) Math.ceil((double) res / brickres * y) + 1,
									(int) Math.ceil((double) res / brickres), (int) Math.ceil((double) res / brickres),
									null);
							textures[i][1].createGraphics().drawImage(img1,
									(int) Math.ceil((double) res / brickres * x) + 1,
									(int) Math.ceil((double) res / brickres * y) + 1,
									(int) Math.ceil((double) res / brickres), (int) Math.ceil((double) res / brickres),
									null);
						}
					}

				} else {

					textures[i][0].createGraphics().drawImage(img0, 1, 1, res, res, null);
					textures[i][1].createGraphics().drawImage(img1, 1, 1, res, res, null);
				}
			} catch (FileNotFoundException e) {
				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
				textures[i][1] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
			} catch (IllegalArgumentException e) {

				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
				textures[i][1] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
			} catch (IOException e) {

			}
			for (int x = 0; x < res; x++) {
				for (int y = 0; y < res; y++) {
					rasters[i][0][x][y] = new Color(textures[i][0].getRGB(x + 1, y + 1));
					rasters[i][1][x][y] = new Color(textures[i][1].getRGB(x + 1, y + 1));
				}
			}
		}
		for (int i = 0; i < 20; i++) {

			URL image0 = this.getClass().getClassLoader().getResource("images/image" + i + ".png");
			try {
				BufferedImage img0 = ImageIO.read(image0);
				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_ARGB);
				textures[i][0].createGraphics().drawImage(img0, 0, 0, res + 1, res + 1, null);
			} catch (FileNotFoundException e) {
				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
			} catch (IllegalArgumentException e) {

				textures[i][0] = new BufferedImage(res + 1, res + 1, BufferedImage.TYPE_INT_RGB);
			} catch (IOException e) {

			}
			for (int x = 0; x < res; x++) {
				for (int y = 0; y < res; y++) {
					spriterasters[i][x][y] = new Color(textures[i][0].getRGB(x + 1, y + 1));

				}
			}
		}
		for (int i = 0; i < 10; i++) {
			floorbuf[i] = loadgraphics("images/floor" + i + ".png", res);
		}

	}

	public synchronized void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(screenbuffer, 0, 0, getWidth(), getHeight(), null);
	}

	public double findforx(double x, double y) {
		try {
			return Math.abs(1 / x);
		} catch (ArithmeticException e) {
			return 0;
		}

	}

	public double findfory(double x, double y) {
		try {
			return Math.abs(1 / y);
		} catch (ArithmeticException e) {
			return 0;
		}
	}

	public double findforxinit(double x, double y, double playerx, double playery) {
		double m = 0;
		try {
			m = Math.abs(1 / x);
		} finally {
			m *= difference(x, playerx);
		}
		return m;

	}

	public double findforyinit(double x, double y, double playerx, double playery) {
		double m = 0;
		try {
			m = Math.abs(1 / y);
		} finally {
			m *= difference(y, playery);
		}
		return m;
	}

	public static double difference(double x, double playerx) {
		if (x < 0) {
			return playerx - Math.floor(playerx);
		} else {
			return Math.ceil(playerx) - playerx;
		}
	}

	public double finddist(double x, double y) {
		if (mode) {
			return dist(x, y) * 0.05;
		}
		double newx = x0 * x + y0 * y;
		double newy = x1 * x + y1 * y;
		if (flashon) {
			return (2 - (newx * 0.3 - Math.abs(newy))) + newx * 0.4;
		}

		return newx * 10;

	}

	public static double dist(double x, double y) {
		return Math.sqrt(x * x + y * y);
	}

	public static int[] atblock(double x, double y, int[][] map) {
		int right;
		int left;
		int[] bleh = new int[2];

		int wallx = (int) Math.round(x);
		int wally = (int) Math.round(y);
		if (Math.abs(wallx - x) < Math.abs(wally - y)) {
			bleh[1] = 1;
			try {
				left = map[wallx][(int) Math.floor(y)];
			} catch (ArrayIndexOutOfBoundsException e) {
				left = 0;
			}
			try {
				right = map[wallx - 1][(int) Math.floor(y)];
			} catch (ArrayIndexOutOfBoundsException e) {
				right = 0;
			}
			if (left == 0 && right == 0) {
				bleh[0] = 0;
			} else if (((!(left == 0)) && (!(right == 0)))) {
				bleh[0] = 0;
			} else {
				bleh[0] = Math.max(left, right) + 1;
			}
		} else {
			bleh[1] = 0;
			try {
				left = map[(int) Math.floor(x)][wally];
			} catch (ArrayIndexOutOfBoundsException e) {
				left = 0;
			}
			try {
				right = map[(int) Math.floor(x)][wally - 1];
			} catch (ArrayIndexOutOfBoundsException e) {

				right = 0;
			}

			if (left == 0 && right == 0) {
				bleh[0] = 0;
			} else if (((!(left == 0)) && (!(right == 0)))) {
				bleh[0] = 0;
			} else {
				bleh[0] = Math.max(left, right) + 1;
			}

		}
		return bleh;
	}

	public synchronized void update() {
		myy = myybuf;
		myx = myxbuf;
		a = abuf;
		double playerheightbuf = playerheight;

		graphics.clearRect(0, 0, 1000, 1000);
		double[] zbuf = new double[1000];
		int screenseg = 0;
		int blocktype = 0;
		for (double b = 1; b > -1; b -= 0.002) {
			screenseg++;

			double x = Math.cos(a) + Math.sin(-a) * b * fov;
			double y = Math.sin(a) + Math.cos(-a) * b * fov;
			double stepx = findforx(x, y);
			double stepy = findfory(x, y);
			double mxn = findforxinit(x, y, myx, myy);
			double myn = findforyinit(x, y, myx, myy);
			double mx = 0;
			double my = 0;
			double m = Math.max(mx, my);
			double realx = x * m + myx + cloudroll;
			double realy = y * m + myy;
			boolean isx;
			int texlev;
			if (my + myn < mx + mxn) {
				my += myn;
				myn = stepy;
			} else {
				mx += mxn;
				mxn = stepx;
			}
			m = Math.max(mx, my);
			realy = y * m + myy;
			realx = x * m + myx;
			if ((screenseg - 1) % floorres < 1) {
				for (double lol = 500; lol <= 1000; lol += floorres) {
					double zray = 1 / ((double) (lol - 500) * 0.004 * fov);
					double ratiox = x * zray / (1 + playerheightbuf * 0.25);
					double ratioy = y * zray / (1 + playerheightbuf * 0.25);
					double x1 = (ratiox + myx);
					double y1 = (ratioy + myy);
					double dist = finddist(ratiox, ratioy);
					double xdif = x1 - Math.floor(x1);
					double ydif = y1 - Math.floor(y1);
					int x2 = (int) ((double) res * xdif);
					int y2 = (int) ((double) res * ydif);
					int floortype = 0;
					if (dist > fogdist) {
						double fogshade = fogdist / dist;
						graphics.setColor(new Color(
								(int) (floorbuf[floortype][x2][y2].getRed() * fogshade
										+ fogcolor.getRed() * (1 - fogshade)),
								(int) (floorbuf[floortype][x2][y2].getGreen() * fogshade
										+ fogcolor.getGreen() * (1 - fogshade)),
								(int) (floorbuf[floortype][x2][y2].getBlue() * fogshade
										+ fogcolor.getBlue() * (1 - fogshade))));
					} else {
						graphics.setColor(floorbuf[floortype][x2][y2]);
					}
					graphics.fillRect(screenseg, (int) (lol), (int) Math.ceil(floorres) + 1, (int) Math.ceil(floorres));
					ratiox = x * zray;
					ratioy = y * zray;
					if (mode) {
						dist = dist(ratiox, ratioy) * 0.1;
					} else {
						dist = dist(ratiox, ratioy) * 100;
					}

					x1 = (ratiox + Math.sin(cloudroll) + myx * 0.01);
					y1 = (ratioy + Math.cos(cloudroll) + myy * 0.01);
					xdif = x1 * 0.1 - Math.floor(x1 * 0.1);
					ydif = y1 * 0.1 - Math.floor(y1 * 0.1);
					x2 = (int) ((double) (res * 4) * xdif);
					y2 = (int) ((double) (res * 4) * ydif);

					if (dist > fogdist * 5) {
						double fogshade = fogdist * 5 / dist;
						graphics.setColor(new Color(
								(int) (skygraphics[x2][y2].getRed() * fogshade + fogcolor.getRed() * (1 - fogshade)),
								(int) (skygraphics[x2][y2].getGreen() * fogshade
										+ fogcolor.getGreen() * (1 - fogshade)),
								(int) (skygraphics[x2][y2].getBlue() * fogshade
										+ fogcolor.getBlue() * (1 - fogshade))));
					} else {
						graphics.setColor(skygraphics[x2][y2]);
					}
					graphics.fillRect(screenseg, 1000 - (int) (lol), (int) Math.ceil(floorres) + 1,
							(int) Math.ceil(floorres));
				}
			}

			for (int c = 0; c < renderdist; c++) {
				isx = atblock(realx, realy, mapy)[1] == 1;
				if ((atblock(realx, realy, mapy)[0] == 0) && (c < renderdist - 1)) {
					if (my + myn < mx + mxn) {
						my += myn;
						myn = stepy;
					} else {
						mx += mxn;
						mxn = stepx;
					}
					m = Math.max(mx, my);
					realy = y * m + myy;
					realx = x * m + myx;
				} else {
					zbuf[screenseg - 1] = m;
					blocktype = atblock(realx, realy, mapy)[0];
					if (!isx) {
						texlev = (int) ((realx - Math.floor(realx)) * res);
					} else {
						texlev = (int) ((realy - Math.floor(realy)) * res);
					}
					try {
						if (!isx) {
							graphics.setColor(new Color(blocktype, texlev, texlev));
						} else {
							graphics.setColor(new Color(texlev, blocktype, texlev));
						}
					} catch (IllegalArgumentException e) {

					}
					int isy;
					if (isx) {
						isy = 0;
					} else {
						isy = 1;
					}
					Color[] refrencing;
					try {
						refrencing = rasters[blocktype - 1][isy][texlev];

					} catch (ArrayIndexOutOfBoundsException e) {
						refrencing = rasters[blocktype][isy][texlev];
					}
					int topedge = 500 - (int) (250 * fovinv / (m / (1 + playerheightbuf * 0.5)));
					int bottomedge = (int) (500 * fovinv / (m));
					double segment = (double) bottomedge / (double) res;
					double fogshade;
					double type;
					for (int g = 0; g < res; g++) {
						if (isx) {
							type = stepx;
						} else {
							type = stepy;
						}
						type = Math.pow(type, reflectiveness);
						double rawshade = finddist(realx - myx, realy - myy);
						fogshade = fogdist / (rawshade * type);
						if (rawshade * type > fogdist) {
							graphics.setColor(new Color(
									(int) (refrencing[g].getRed() * fogshade + fogcolor.getRed() * (1 - fogshade)),
									(int) (refrencing[g].getGreen() * fogshade + fogcolor.getGreen() * (1 - fogshade)),
									(int) (refrencing[g].getBlue() * fogshade + fogcolor.getBlue() * (1 - fogshade))));
						} else {
							graphics.setColor(refrencing[g]);
						}

						graphics.drawRect(screenseg, (int) (topedge + g * segment), 0, (int) segment);
					}
					break;

				}
			}
		}

		double x1 = Math.cos(a) + Math.sin(-a) * fov;
		double y1 = Math.sin(a) + Math.cos(-a) * fov;
		double x2 = Math.cos(a) + Math.sin(-a) * -fov;
		double y2 = Math.sin(a) + Math.cos(-a) * -fov;
		double invdet = 1 / (x1 * y2 - x2 * y1);
		double buffer = y2 * invdet;
		y2 = x1 * invdet;
		x1 = buffer;
		x2 = -x2 * invdet;
		y1 = -y1 * invdet;
		int length = 500;
		Color spritecolor = Color.BLACK;
		int target = -1;
		for (int i = 0; i < touse.size(); i++) {
			double spritex = touse.get(i).x;
			double spritey = touse.get(i).y;
			double rx = spritex - myx;
			double ry = spritey - myy;
			int spritevalue = touse.get(i).value;
			double spriteposx = (spritex - myx) * x1 + (spritey - myy) * x2;
			double spriteposy = (spritex - myx) * y1 + (spritey - myy) * y2;
			double width;

			width = (1 + (spriteposy - spriteposx) / (spriteposx + spriteposy)) / 2;

			double dist = spriteposx + spriteposy;

			for (int g = (int) (-(length / 2) * fovinv / dist); g < (length / 2) * fovinv / dist; g++) {
				double xspan = g / (length * fovinv / dist) + 0.5;
				int xspanint = (int) (xspan * res);
				double change = (500 * fovinv / dist) / res;
				int screenline = (int) (g + width * 1000);

				if (screenline < 1000 && screenline > 0) {

					if (zbuf[screenline] > dist) {
						if (screenline == 500) {
							if (touse.get(i).value == 12) {
								target = i;
							}
						}
						for (int h = 0; h < res; h++) {

							spritecolor = spriterasters[spritevalue][xspanint][h];

							if (spritecolor.equals(Color.BLACK)) {
								continue;
							}
							double beginshade = finddist(rx, ry) / fogdist;
							if (beginshade > 1) {
								double shade = 1 / beginshade;
								graphics.setColor(new Color(
										(int) Math
												.floor(spritecolor.getRed() * shade + fogcolor.getRed() * (1 - shade)),
										(int) Math.floor(
												spritecolor.getGreen() * shade + fogcolor.getGreen() * (1 - shade)),
										(int) Math.floor(
												spritecolor.getBlue() * shade + fogcolor.getBlue() * (1 - shade))));
							} else {
								graphics.setColor(spritecolor);
							}

							graphics.drawRect(screenline + 1,
									500 - (int) ((250 * fovinv / dist * (1 + playerheightbuf * 0.5)) - change * h), 0,
									(int) change);
						}
					}
				}
			}

		}
		tohit = target;

		if (dead) {
			graphics.setColor(Color.RED);
			graphics.setFont(new Font("arial", Font.PLAIN, 100));
			graphics.drawString("you are dead", 200, 500);
		} else {
			graphics.setColor(Color.blue);
			graphics.setFont(new Font("arial", Font.PLAIN, 35));
			graphics.drawString("Power:" + Math.floor(power), 10, 70);
			graphics.setColor(new Color(128, 128, 0));
			graphics.fillRect(10 + (int) (power * 0.02), 80, 200 - (int) (power * 0.02), 10);
			graphics.setColor(Color.YELLOW);
			graphics.fillRect(10, 80, (int) (power * 0.02), 10);
			graphics.setColor(Color.blue);
			drawborder(10, 80, 200, 10);
			graphics.setColor(Color.RED);
			graphics.setFont(new Font("arial", Font.PLAIN, 35));
			graphics.drawString("Health:" + health, 800, 70);
			graphics.fillRect(800, 80, (int) (health * 1.6), 10);
			graphics.setColor(new Color(128, 0, 0));
			graphics.fillRect(800 + (int) (health * 1.6), 80, 160 - (int) (health * 1.6), 10);

			graphics.setColor(Color.blue);
			drawborder(800, 80, 160, 10);

		}
		graphics.drawImage(overlay, 0, 0, 1000, 1000, null);
		if (shootwait < 100) {
			graphics.drawImage(overlay2, 0, 0, 1000, 1000, null);
		}

		repaint();
	}

	public void explode(double x, double y, double size, int damage) {
		double xdif = x - myx;
		double ydif = y - myy;
		if (xdif * xdif + ydif * ydif < size) {
			health -= 20;
		}
	}

	public void drawborder(int a, int b, int c, int d) {
		for (int i = 0; i < 2; i++) {
			graphics.drawRect(a - i - 1, b - i - 1, c + i * 2, d + i * 2);
		}

	}

	public static void main(String[] args) {
		tests t = new tests();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
		new Thread(t).start();
	}

	public void mouseDragged(MouseEvent e) {
		mousemovements(e);
	}

	public Color[][] loadgraphics(String url, int reso) {
		URL graphicslocation = this.getClass().getClassLoader().getResource(url);
		BufferedImage image = new BufferedImage(reso, reso, BufferedImage.TYPE_INT_RGB);
		try {
			image = ImageIO.read(graphicslocation);
		} catch (IllegalArgumentException e1) {
			image = new BufferedImage(res, res, BufferedImage.TYPE_INT_RGB);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedImage finalimage = new BufferedImage(reso, reso, BufferedImage.TYPE_INT_RGB);
		finalimage.createGraphics().drawImage(image, 0, 0, reso, reso, null);
		Color[][] raster = new Color[reso][reso];
		for (int x = 0; x < reso; x++) {
			for (int y = 0; y < reso; y++) {
				raster[x][y] = new Color(finalimage.getRGB(x, y));
			}
		}
		return raster;
	}

	public void mouseMoved(MouseEvent e) {

		mousemovements(e);
		// update();
	}

	public void mousemovements(MouseEvent e) {
		if (!movingmouse) {
			abuf += (double) ((getWidth() / 2) - e.getX()) / 200;
			updatesprites();
			try {
				new Robot().mouseMove(getWidth() / 2 + panelon.getX(), getHeight() / 2 + panelon.getY());
			} catch (AWTException e1) {
			}
		}
	}

	public void keyTyped(KeyEvent e) {

	}

	public void keyPressed(KeyEvent e) {
		if (!ispressed[e.getKeyCode()]) {
			ispressed[e.getKeyCode()] = true;
		}
		try {
			if (e.getKeyCode() == KeyEvent.VK_E) {
				movingmouse = !movingmouse;
				if (!movingmouse) {

					panelon.getContentPane().setCursor(blankCursor);
				} else {
					panelon.getContentPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
					new Robot().mouseMove(getWidth() / 2, getHeight() / 2);
				}

			}
			if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
				System.exit(ABORT);
			}
			if (e.getKeyCode() == KeyEvent.VK_F) {
				if (haspow) {
					flashon = !flashon;
				}
				playsound("audio/click.wav");

			}

			if (e.getKeyCode() == KeyEvent.VK_O) {
				fovinv += 0.1;
			}

			if (e.getKeyCode() == KeyEvent.VK_P) {
				fovinv -= 0.1;
			}
			if (e.getKeyCode() == KeyEvent.VK_N) {
				res -= 1;
				updategraphics();
			}
			if (e.getKeyCode() == KeyEvent.VK_M) {
				res += 1;
				updategraphics();
			}
			if (e.getKeyCode() == KeyEvent.VK_D) {
				if (ispressed[KeyEvent.VK_CONTROL]) {

				}

			}
			if (e.getKeyCode() == KeyEvent.VK_T) {
				mode = !mode;
				if (mode) {
					fogcolor = new Color(230, 230, 255);
				} else {
					fogcolor = new Color(0, 0, 0);
				}
			}

		} catch (ArrayIndexOutOfBoundsException e1) {
		} catch (AWTException e1) {
		}
		try {
			fov = 1 / fovinv;
		} catch (ArithmeticException nul) {
			fov = 0;
		}
		// update();

	}

	public void keyReleased(KeyEvent e) {
		ispressed[e.getKeyCode()] = false;

	}

	public void movechar(double x, double y) {
		if (!dead) {
			try {
				if (mapy[(int) Math.floor(myxbuf + x)][(int) Math.floor(myybuf + y)] == 0) {
					if (movetype == 0) {
						myybuf += y;
						myxbuf += x;
					} else if (movetype == 1) {
						myybuf += y * 0.5;
						myxbuf += x * 0.5;
					}

					updatesprites();
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				if (movetype == 0) {
					myybuf += y;
					myxbuf += x;
				} else if (movetype == 1) {
					myybuf += y * 0.25;
					myxbuf += x * 0.25;
				}
				updatesprites();
			}
		}

	}

	public void updatesprites() throws NullPointerException{
		for (int i = 0; i < touse.size(); i++) {

			touse.get(i).update();
		}
		bubblesort();
	}

	public void bubblesort() {
		for (int i = touse.size() - 1; i > 0; i--) {
			for (int g = 0; g < i; g++) {
				if (touse.get(g).dist < touse.get(g + 1).dist) {
					swap(g, g + 1);
				}
			}
		}
	}

	public void swap(int a, int b) {
		spritepoint buf = touse.get(a);
		touse.set(a, touse.get(b));
		touse.set(b, buf);
	}

	protected void player_interaction(){

		double pastplayerheight = playerheight;
		if (ispressed[KeyEvent.VK_SHIFT]) {
			playerheight = 1;
			movetype = 1;
		} else {
			playerheight = (Math.sin(movedtime * 0.01) * 0.1) - 0.75;
			movetype = 0;
		}

		double xnow = myxbuf;
		double ynow = myybuf;
		if (mode) {
			reflectiveness = 0.125;
		} else {
			reflectiveness = 0.5;
		}
		x0 = Math.cos(-a);
		y0 = -Math.sin(-a);
		x1 = Math.sin(-a);
		y1 = Math.cos(-a);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
		}
		if (flashon) {
			power -= 0.1;
		}
		if (haspow && power < 0) {
			playsound("audio/powout.wav");
		}
		haspow = power > 0;
		flashon = flashon && haspow;
		if (ispressed[KeyEvent.VK_D]) {
			movechar(Math.sin(a) * mpt, -Math.cos(a) * mpt);
		}
		if (ispressed[KeyEvent.VK_A]) {
			movechar(-Math.sin(a) * mpt, Math.cos(a) * mpt);

		}
		if (ispressed[KeyEvent.VK_S]) {
			movechar(-Math.cos(a) * mpt, -Math.sin(a) * mpt);
		}
		if (ispressed[KeyEvent.VK_W]) {
			movechar(Math.cos(a) * mpt, Math.sin(a) * mpt);

		}
		if (ispressed[KeyEvent.VK_I]) {
			skyheight -= 0.005;
		}
		if (ispressed[KeyEvent.VK_K]) {
			skyheight += 0.005;

		}

		cloudroll += 0.00005;
		if (cloudroll > Math.PI * 2) {
			double thing = Math.PI * 2;
			cloudroll = ((cloudroll / thing) - Math.floor(cloudroll / thing)) * thing;
		}
		dead = health <= 0;
		if (dead) {
			fogcolor = new Color(128, 0, 0);
			fogdist = 0.01;
		}
		if (ynow != myy || xnow != myx) {
			movedtime++;
			if ((!ispressed[KeyEvent.VK_SHIFT]) && (playerheight > -0.7 && !(pastplayerheight > -0.7))) {
				playsound("audio/step.wav");
			}
		}
		if (age > 10000) {
			for (int i = 0; i < touse.size(); i++) {

				double dist = dist((myx - touse.get(i).x), (myy - touse.get(i).y));
				if (touse.get(i).value == 12) {
					touse.get(i).x += ((myx - touse.get(i).x) / dist) * 0.005;
					touse.get(i).y += ((myy - touse.get(i).y) / dist) * 0.005;
				}
				if (touse.get(i).value == 13) {
					touse.get(i).age++;
				}
			}
		}

	}

	public void aitick() {
		int lag = 0;
		for (int i = 0; i < touse.size(); i++) {
			if (touse.get(i - lag).age > 1000) {
				if (touse.get(i - lag).value == 13) {
					touse.remove(i - lag);
					lag++;
				}
			}
		}

		if (Math.random() < 0.1) {
			if(enemies<1000){
			touse.add(new spritepoint(Math.random() * 1000, Math.random() * 1000, 12, this));
			enemies++;
			}
			
		}
		if (Math.random() < 0.1) {
			if (healthpacks.size() < 10) {
				spritepoint toadd = new spritepoint(Math.random() * 25 - 12.5 + myy, Math.random() * 25 - 12.5 + myx, 14, this);
				touse.add(toadd);
				healthpacks.add(new healthpack(this, toadd, 0));
			}
		}
	}

	public static ArrayList<spritepoint> aslist(spritepoint[] in) {
		ArrayList<spritepoint> stuff = new ArrayList<spritepoint>();
		for (spritepoint a : in) {
			stuff.add(a);
		}
		return stuff;
	}

	public void combattick() {
		// TODO
		for (int i = 0; i < touse.size(); i++) {
			try{
				if (touse.get(i).value == 12) {
				if (dist(touse.get(i).x - myx, touse.get(i).y - myy) < 1) {
					health -= 0.01;
				}
			}
			} catch(NullPointerException e){
				System.out.println(i);
			}
			
		}
		if ((button1 && agediff % 50 == 49) & shootwait > 10) {
			shoot();
		}
		age++;
		agediff = age - startage;
		shootwait++;
	}

	public void run() {
		threadnum++;
		if (threadnum == 1) {
			while (true) {
					player_interaction();
				
			}
		}
		if (threadnum == 2) {
			while (true) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
					try{
					updatesprites();
				}catch(NullPointerException e){
					
				}
				
				
				update();
			}
		}
		if (threadnum == 3) {
			while (true) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				combattick();
			}
		}
		if (threadnum == 4) {
			while (true) {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				aitick();
			}
		}

	}

	public void playsound(String filename) {
		try {
			URL url = this.getClass().getClassLoader().getResource(filename);
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
			Clip clip = AudioSystem.getClip();
			clip.open(audioIn);
			clip.start();
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
			System.out.println("1");
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("2");
		} catch (LineUnavailableException e) {
			e.printStackTrace();
			System.out.println("3");
		}
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		startage = age;
		if (e.getButton() == MouseEvent.BUTTON1) {
			button1 = true;
		}

	}

	public void shoot() {
		shootwait = 0;
		playsound("audio/shot.wav");
		try {
			if (touse.get(tohit).value == 12) {

				touse.get(tohit).value++;
				System.out.println("bang");
			}
			System.out.println(tohit);
		} catch (ArrayIndexOutOfBoundsException ex) {

		}
	}

	public void mouseReleased(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			button1 = false;
		}

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
