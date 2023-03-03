
/*
	Student name and family: Arash ahmadi
	Student ID: 9717023103
	Semester: Spring 2019
	Lecturer: Amanj Khorramian
*/

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.io.*;

enum Piece {
	BLACK, WHITE;
}

class MainBoard extends JFrame {
	JFrame jf = this;
	int black = 2, white = 2, gray = 60;
	Piece now = Piece.BLACK;
	Piece reverseNow = Piece.WHITE;
	boolean isBotPlaying = false;
	boolean disablingUI = false;
	JPanel jp = new JPanel(new GridLayout(8, 8, 4, 4));
	JLabel[][] disk = new JLabel[8][8];
	JMenuBar jmb = new JMenuBar();
	JMenu fileMenu = new JMenu("Menu");
	JMenuItem newItem = new JMenuItem("New");
	JMenuItem saveItem = new JMenuItem("Save");
	JMenuItem saveAsItem = new JMenuItem("Save as..");
	JMenuItem openItem = new JMenuItem("Open");
	JMenuItem exitItem = new JMenuItem("Exit");
//    JMenu editMenu = new JMenu("Edit");
//    JMenuItem changeColorItem = new JMenuItem("Change the colors");
//    JMenuItem changeFontItem = new JMenuItem("Change the fonts");
	JCheckBoxMenuItem botItem = new JCheckBoxMenuItem("Play with bot");
	LiveScore ls = new LiveScore();
	Information in = new Information();
	Thread t = new Thread(in);
	int level = 0;

	MainBoard() {
		super("Reversi game");
		t.start();
		in.setTurn(Piece.BLACK);
		in.showTurn(Piece.BLACK);
		setSize(500, 500);
		setLocation(50, 180);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().add(jp);
		jp.setBackground(new Color(158, 158, 158));
		newItem.setMnemonic(KeyEvent.VK_N);
		KeyStroke keyStrokeToOpen = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
		newItem.setAccelerator(keyStrokeToOpen);
		/////////////////////////////////////////////////
		saveItem.setMnemonic(KeyEvent.VK_S);
		KeyStroke keyStrokeToOpen1 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
		saveItem.setAccelerator(keyStrokeToOpen1);
		//////////////////////////////////////////////////
		saveAsItem.setMnemonic(KeyEvent.VK_S);
		KeyStroke keyStrokeToOpen2 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.VK_ALT);
		saveAsItem.setAccelerator(keyStrokeToOpen2);
		///////////////////////////////////////////////////
		openItem.setMnemonic(KeyEvent.VK_O);
		KeyStroke keyStrokeToOpen3 = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
		openItem.setAccelerator(keyStrokeToOpen3);
		///////////////////////////////////////////////
		exitItem.setMnemonic(KeyEvent.VK_E);
		KeyStroke keyStrokeToOpen4 = KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK);
		exitItem.setAccelerator(keyStrokeToOpen4);
		///////////////////////////////////////////////
		exitItem.setMnemonic(KeyEvent.VK_B);
		KeyStroke keyStrokeToOpen5 = KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK);
		botItem.setAccelerator(keyStrokeToOpen5);
		///////////////////////////////////////////////
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++) {
				disk[i][j] = new JLabel("\u2022", SwingConstants.CENTER);
				jp.add(disk[i][j]);
				disk[i][j].setForeground(Color.lightGray);
				disk[i][j].setOpaque(true);
				disk[i][j].setBackground(Color.lightGray);
				disk[i][j].setFont(new Font("Arial", Font.PLAIN, 120));
				disk[i][j].addMouseListener(new ML(i, j));
			}
		exitItem.addActionListener(new All("exit"));
		newItem.addActionListener(new All("new"));
		openItem.addActionListener(new All("open"));
		saveAsItem.addActionListener(new All("saveas"));
		saveItem.addActionListener(new All("save"));
		botItem.addActionListener(new CheckingBot());
		disk[3][3].setForeground(Color.white);
		disk[4][4].setForeground(Color.white);
		disk[3][4].setForeground(Color.black);
		disk[4][3].setForeground(Color.black);
		setJMenuBar(jmb);
		jmb.add(fileMenu);
//        jmb.add(editMenu);
		fileMenu.add(newItem);
		fileMenu.add(saveItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(openItem);
		fileMenu.add(openItem);
		fileMenu.add(botItem);
		fileMenu.add(exitItem);
//        editMenu.add(changeColorItem);
//        editMenu.add(changeFontItem);
		setVisible(true);
		check(now, true);
	}

	boolean check(Piece turn, boolean yay) {
		int flag = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isAllowed(i, j, turn, false) > 0) {
					if (yay == true) {
						disk[i][j].setBackground(Color.gray);
						disk[i][j].setForeground(Color.gray);
					}
					flag++;

				}
			}
		}
		numbersOfPieces();
		if (flag == 0) {
			return false;
		}
		return true;
	}

	void clearReds() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (disk[i][j].getBackground() == Color.gray) {
					disk[i][j].setBackground(Color.lightGray);
					disk[i][j].setForeground(Color.lightGray);
				}
			}
		}
	}

	void status(boolean yuno) {
		int flag = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (isAllowed(i, j, Piece.BLACK, false) > 0 || isAllowed(i, j, Piece.WHITE, false) > 0) {
					flag++;
				}
			}
		}
		if (flag == 0 || yuno == true) {
			numbersOfPieces();
			if (white > black) {
				in.j3.setText("White Wins!");
				JOptionPane.showMessageDialog(null, "White Wins!");

			}
			if (white < black) {
				in.j3.setText("Black Wins!");
				JOptionPane.showMessageDialog(null, "Black Wins!");

			}
			if (white == black) {
				in.j3.setText("It's a draw!!");
				JOptionPane.showMessageDialog(null, "It's a draw!!");

			}
			startingOverFromTheBegining();
		}
	}

	void numbersOfPieces() {
		black = 0;
		white = 0;
		gray = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (disk[i][j].getForeground() == Color.BLACK) {
					black++;
				}
				if (disk[i][j].getForeground() == Color.WHITE) {
					white++;
				}
				if (disk[i][j].getForeground() == Color.GRAY) {
					gray++;
				}
			}
		}
	}

	void startingOverFromTheBegining() {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				disk[i][j].setForeground(Color.lightGray);
			}
		}
		disk[3][3].setForeground(Color.white);
		disk[4][4].setForeground(Color.white);
		disk[3][4].setForeground(Color.black);
		disk[4][3].setForeground(Color.black);
		black = 2;
		white = 2;
		now = Piece.BLACK;
		in.setTurn(now);
		in.showTurn(now);
		in.setTimerBlack(0, 0);
		in.setTimerWhite(0, 0);
		in.setTimerMain(0, 0);
		ls.clearScore();
		level = 0;
		ls.score[1][0] = new JLabel("0", SwingConstants.CENTER);
		ls.score[1][1] = new JLabel("2", SwingConstants.CENTER);
		ls.score[1][2] = new JLabel("2", SwingConstants.CENTER);
		numbersOfPieces();
		ls.setScore(level, true, white);
		ls.setScore(level, false, black);
		clearReds();
		check(now, true);
	}

	class All implements ActionListener {
		String x;
		boolean yuki = false;

		All(String x) {
			this.x = x;
		}

		public void actionPerformed(ActionEvent e) {
			switch (x) {
			case "exit":
				System.exit(0);
				break;
			case "new":
				startingOverFromTheBegining();
				break;
			case "save":
				int sel1 = 0;
				JFileChooser fileChooser1 = new JFileChooser();
				if (yuki == false) {
					fileChooser1.setDialogTitle("Specify a file to open");
					sel1 = fileChooser1.showSaveDialog(jf);
					yuki = true;
				}
				if (sel1 == JFileChooser.APPROVE_OPTION || yuki == false) {
					Color[][] color = new Color[8][8];
					File fileToSave = fileChooser1.getSelectedFile();
					String tmp = "";
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							color[i][j] = disk[i][j].getForeground();
							if (color[i][j] == Color.BLACK) {
								tmp += "b,";
							}
							if (color[i][j] == Color.WHITE) {
								tmp += "w,";
							}
							if (color[i][j] == Color.lightGray) {
								tmp += "l,";
							}
							if (color[i][j] == Color.gray) {
								tmp += "g,";
							}
						}
					}
					if (now == Piece.BLACK) {
						tmp += "b";
					}
					if (now == Piece.WHITE) {
						tmp += "w";
					}
					tmp += "," + in.getTimerMain() + "," + in.getTimerBlack() + "," + in.getTimerWhite();
					try {
						RandomAccessFile raf = new RandomAccessFile(fileToSave, "rw");
						raf.writeUTF(tmp);
						raf.close();
					} catch (Exception e5) {
					}
					;
				}
				break;
			case "saveas":
				JFileChooser fileChooser4 = new JFileChooser();
				fileChooser4.setDialogTitle("Specify a file to open");
				int sel2 = fileChooser4.showSaveDialog(jf);
				if (sel2 == JFileChooser.APPROVE_OPTION) {
					Color[][] color = new Color[8][8];
					File fileToSave = fileChooser4.getSelectedFile();
					String tmp = "";
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							color[i][j] = disk[i][j].getForeground();
							if (color[i][j] == Color.BLACK) {
								tmp += "b,";
							}
							if (color[i][j] == Color.WHITE) {
								tmp += "w,";
							}
							if (color[i][j] == Color.lightGray) {
								tmp += "l,";
							}
							if (color[i][j] == Color.gray) {
								tmp += "g,";
							}
						}
					}
					if (now == Piece.BLACK) {
						tmp += "b";
					}
					if (now == Piece.WHITE) {
						tmp += "w";
					}
					tmp += "," + in.getTimerMain() + "," + in.getTimerBlack() + "," + in.getTimerWhite();
					try {
						RandomAccessFile raf = new RandomAccessFile(fileToSave, "rw");
						raf.writeUTF(tmp);
						raf.close();
					} catch (Exception e5) {
					}
					;
				}
				break;
			case "open":
				String[] tmp2;
				JFileChooser fileChooser2 = new JFileChooser();
				fileChooser2.setDialogTitle("Specify a file to open");
				int sel3 = fileChooser2.showOpenDialog(jf);
				if (sel3 == JFileChooser.APPROVE_OPTION) {
					File fileToOpen = fileChooser2.getSelectedFile();
					try {
						RandomAccessFile raf = new RandomAccessFile(fileToOpen, "rw");
						clearReds();
						String ff = raf.readUTF();
						tmp2 = ff.split(",");
						int i = 0;
						for (int j = 1; j < 64; j++) {
							if (tmp2[j].equals("b")) {
								disk[i][j % 8].setForeground(Color.BLACK);
							}
							if (tmp2[j].equals("w")) {
								disk[i][j % 8].setForeground(Color.WHITE);
							}
							if (tmp2[j].equals("l")) {
								disk[i][j % 8].setForeground(Color.lightGray);
							}
							if (tmp2[j].equals("g")) {
								disk[i][j % 8].setForeground(Color.gray);
							}
							if (j % 8 == 0) {
								i++;
							}
						}
						if (tmp2[64].equals("b")) {
							now = Piece.BLACK;
						}
						if (tmp2[64].equals("w")) {
							now = Piece.WHITE;
						}
						check(now, true);
						in.setTimerMain(Integer.parseInt(tmp2[65]), Integer.parseInt(tmp2[66]));
						in.setTimerBlack(Integer.parseInt(tmp2[67]), Integer.parseInt(tmp2[68]));
						in.setTimerWhite(Integer.parseInt(tmp2[69]), Integer.parseInt(tmp2[70]));
						in.setTurn(now);
						in.showTurn(now);
						level = 0;
						ls.clearScore();
						numbersOfPieces();
						ls.setScore(level, true, white);
						ls.setScore(level, false, black);

						raf.close();
					} catch (Exception e5) {
					}
					;
				}
				break;
			}
		}
	}

	class CheckingBot implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			AbstractButton aButton = (AbstractButton) event.getSource();
			boolean selected = aButton.getModel().isSelected();
			if (selected) {
				isBotPlaying = true;
			} else {
				isBotPlaying = false;
			}
		}
	}

	class ML implements MouseListener {
		JLabel label;
		int x;
		int y;
		Color color;
		Piece temp;
		int[] cor = new int[2];
		boolean shouldIBreakIt = false;
		void doBot() {
			if (isBotPlaying && now == Piece.WHITE) {
				color = Color.WHITE;
				cor = botTurn();
				ActionListener taskPerformer = new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						clearReds();
						disablingUI = false;
						isAllowed(cor[0], cor[1], now, true);
						in.setTurn(Piece.BLACK);
						disk[cor[0]][cor[1]].setForeground(color);
						now = Piece.BLACK;
						in.showTurn(Piece.BLACK);
						level++;
						numbersOfPieces();
						ls.setScore(level, true, white);
						ls.setScore(level, false, black);
						status(false);
						disk[cor[0]][cor[1]].setBackground(Color.lightGray);
						if (check(now, false) == false) {
							now = Piece.WHITE;
							in.setTurn(now);
							in.showTurn(now);
							numbersOfPieces();
							doBot();
						}
						check(now, true);
						status(false);
					}
				};
				Timer timer = new Timer(2000, taskPerformer);
				timer.setRepeats(false);
				timer.start();
			}
		}
		ML(int x, int y) {
			label = disk[x][y];
			this.x = x;
			this.y = y;
		}

		public void mouseClicked(MouseEvent e) {
			if (disablingUI == false && isAllowed(x, y, now, true) > 0) {
				clearReds();
				if (now == Piece.BLACK) {
					reverseNow = Piece.WHITE;
					color = Color.BLACK;
				} else {
					reverseNow = Piece.BLACK;
					color = Color.WHITE;
				}				
				disk[x][y].setForeground(color);
				/////////////////////
				now = temp;
				now = reverseNow;
				reverseNow = temp;
				/////////////////////
				in.setTurn(now);
				in.showTurn(now);
				level++;
				numbersOfPieces();
				ls.setScore(level, true, white);
				ls.setScore(level, false, black);
				status(false);
				if (check(now, false) == false) {
					now = reverseNow;
					if (now == Piece.BLACK) {
						reverseNow = Piece.WHITE;
						color = Color.BLACK;
					} else {
						reverseNow = Piece.BLACK;
						color = Color.WHITE;
					}	
					in.setTurn(now);
					in.showTurn(now);
					numbersOfPieces();
				}
				check(now, true);
				status(false);
				doBot();
				
			}
		}

		public void mouseEntered(MouseEvent e) {
			if (disk[x][y].getForeground() == Color.lightGray) {
				disk[x][y].setForeground(Color.gray);
			}
			if (disk[x][y].getForeground() != Color.BLUE)
				disk[x][y].setBackground(Color.gray);
		}

		public void mouseExited(MouseEvent e) {
			if (isAllowed(x, y, now, false) == 0) {
				if (disk[x][y].getForeground() == Color.gray) {
					disk[x][y].setForeground(Color.lightGray);
				}
				disk[x][y].setBackground(Color.lightGray);
			}
		}

		public void mousePressed(MouseEvent e) {
		}

		public void mouseReleased(MouseEvent e) {
		}
	}

	int[] botTurn() {
		ArrayList<String> chosen = new ArrayList<String>();
		int[] cor = new int[2];
		int ret = 0;
		int count = 0;
		disablingUI = true;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				ret = isAllowed(i, j, Piece.WHITE, false);
				if (ret > 0) {
					chosen.add(count++, ret + "," + i + "," + j);
				}
			}
		}
		Collections.sort(chosen);
		System.out.println(chosen);
		cor[0] = Integer.parseInt(chosen.get(count - 1).split(",")[1]);
		cor[1] = Integer.parseInt(chosen.get(count - 1).split(",")[2]);
		disk[cor[0]][cor[1]].setBackground(Color.BLUE);
		disk[cor[0]][cor[1]].setForeground(Color.BLUE);
		System.out.println(chosen.get(count - 1).split(",")[0] + " -->> " + cor[0] + "," + cor[1]);
		return cor;
	}

	int isAllowed(int x, int y, Piece turn, Boolean doIt) {
		Color self;
		Color reverse;
		int flag = 0;
		boolean temp = false;
		if (turn == Piece.BLACK) {
			self = Color.BLACK;
		} else {
			self = Color.WHITE;
		}
		if (turn == Piece.BLACK) {
			reverse = Color.WHITE;
		} else {
			reverse = Color.BLACK;
		}
		if (disk[x][y].getForeground() == Color.BLACK || disk[x][y].getForeground() == Color.WHITE) {
			return 0;
		}
		/////////////////////////////////////////// 1

		try {
			if (disk[x + 1][y].getForeground() == reverse) {
				for (int i = x + 2; i < 8; i++) {
					if (disk[i][y].getForeground() == Color.lightGray || disk[i][y].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][y].getForeground() == self) {
						for (int m = x + 1; m <= i; m++) {
							if (doIt == true)
								disk[m][y].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 2
		try {
			if (disk[x + 1][y - 1].getForeground() == reverse) {
				for (int i = x + 2, j = y - 2; i < 8 && j >= 0; i++, j--) {
					if (disk[i][j].getForeground() == Color.lightGray || disk[i][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][j].getForeground() == self) {
						for (int m = x + 1, n = y - 1; m <= i && n >= j; m++, n--) {
							if (doIt == true)
								disk[m][n].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 3
		try {
			if (disk[x][y - 1].getForeground() == reverse) {
				for (int j = y - 2; j >= 0; j--) {
					if (disk[x][j].getForeground() == Color.lightGray || disk[x][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[x][j].getForeground() == self) {
						for (int n = y - 1; n >= j; n--) {
							if (doIt == true)
								disk[x][n].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 4
		try {
			if (disk[x - 1][y - 1].getForeground() == reverse) {

				for (int i = x - 2, j = y - 2; i >= 0 && j >= 0; i--, j--) {
					if (disk[i][j].getForeground() == Color.lightGray || disk[i][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][j].getForeground() == self) {
						for (int m = x - 1, n = y - 1; m >= i && n >= j; m--, n--) {
							if (doIt == true)
								disk[m][n].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 5
		try {
			if (disk[x - 1][y].getForeground() == reverse) {
				for (int i = x - 2; i >= 0; i--) {
					if (disk[i][y].getForeground() == Color.lightGray || disk[i][y].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][y].getForeground() == self) {
						for (int m = x - 1; m >= i; m--) {
							if (doIt == true)
								disk[m][y].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 6
		try {
			if (disk[x - 1][y + 1].getForeground() == reverse) {

				for (int i = x - 2, j = y + 2; i >= 0 && j <= 8; i--, j++) {
					if (disk[i][j].getForeground() == Color.lightGray || disk[i][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][j].getForeground() == self) {
						for (int m = x - 1, n = y + 1; m >= i && n <= j; m--, n++) {
							if (doIt == true)
								disk[m][n].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 7
		try {
			if (disk[x][y + 1].getForeground() == reverse) {
				for (int j = y + 2; j < 8; j++) {
					if (disk[x][j].getForeground() == Color.lightGray || disk[x][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[x][j].getForeground() == self) {
						for (int n = y + 1; n <= j; n++) {
							if (doIt == true)
								disk[x][n].setForeground(self);
							flag++;
						}

						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		////////////////////////////////////////// 8
		try {
			if (disk[x + 1][y + 1].getForeground() == reverse) {

				for (int i = x + 2, j = y + 2; i < 8 && j < 8; i++, j++) {
					if (disk[i][j].getForeground() == Color.lightGray || disk[i][j].getForeground() == Color.gray) {
						break;
					}
					if (disk[i][j].getForeground() == self) {
						for (int m = x + 1, n = y + 1; m <= i && n <= j; m++, n++) {
							if (doIt == true)
								disk[m][n].setForeground(self);
							flag++;
						}
						temp = true;
					}
					if (temp == true) {
						temp = false;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		//////////////////////////////////////////
		return flag;
	}
}

class Information extends JFrame implements Runnable {
	Piece now2;
	MainBoard main;
	JPanel jp = new JPanel(new GridLayout(1, 3, 4, 4));
	JPanel p1 = new JPanel(new GridLayout(1, 1));
	JPanel p2 = new JPanel(new GridLayout(3, 1));
	JPanel p3 = new JPanel(new GridLayout(1, 1));
	JLabel j1 = new JLabel("", SwingConstants.CENTER);
	/////////////////////
	JLabel j2_1 = new JLabel("", SwingConstants.CENTER);
	JLabel j2_2 = new JLabel("", SwingConstants.CENTER);
	JLabel j2_3 = new JLabel("", SwingConstants.CENTER);
	////////////////////
	JLabel j3 = new JLabel("", SwingConstants.CENTER);

	void setMainBoard(MainBoard a) {
		main = a;
	}

	Information() {
		super("Information");
		setSize(500, 110);
		setLocation(50, 50);
		p1.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 7, new Color(200, 200, 200)));
		p2.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 7, new Color(200, 200, 200)));
		p3.setBorder(BorderFactory.createMatteBorder(7, 7, 7, 7, new Color(200, 200, 200)));
		p1.add(j1);
		p2.add(j2_1);
		p2.add(j2_2);
		p2.add(j2_3);
		p3.add(j3);
		jp.add(p1);
		jp.add(p2);
		jp.add(p3);
		j1.setFont(new Font("Arial", Font.BOLD, 16));
		j2_1.setFont(new Font("Arial", Font.BOLD, 15));
		j2_2.setFont(new Font("Arial", Font.PLAIN, 15));
		j2_3.setFont(new Font("Arial", Font.PLAIN, 15));
		j3.setFont(new Font("Arial", Font.BOLD, 16));
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().add(jp);
		setVisible(true);
		j1.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
	}

	void showTurn(Piece var) {
		if (var == Piece.BLACK) {
			j1.setText("It's BLACK's turn!");
		}
		if (var == Piece.WHITE) {
			j1.setText("It's WHITE's turn!");
		}
	}

	int sec = 0, min = 0;
	int secb = 0, minb = 0;
	int secw = 0, minw = 0;
	String s = "", m = "", sb = "", mb = "", sw = "", mw = "";

	void setTurn(Piece n) {
		now2 = n;
	}

	public void run() {
		while (true) {
			sec++;
			if (now2 == Piece.BLACK) {
				secb++;
			}
			if (now2 == Piece.WHITE) {
				secw++;
			}
			if (sec == 60) {
				sec = 0;
				min++;
			}
			if (secb == 60) {
				secb = 0;
				minb++;
			}
			if (secw == 60) {
				secw = 0;
				minw++;
			}
			if (min < 10) {
				m = "0" + min;
			} else {
				m = min + "";
			}
			if (sec < 10) {
				s = "0" + sec;
			} else {
				s = sec + "";
			}
			///////////////////////
			if (minw < 10) {
				mw = "0" + minw;
			} else {
				mw = minw + "";
			}
			if (secw < 10) {
				sw = "0" + secw;
			} else {
				sw = secw + "";
			}
			///////////////////////
			if (minb < 10) {
				mb = "0" + minb;
			} else {
				mb = minb + "";
			}
			if (secb < 10) {
				sb = "0" + secb;
			} else {
				sb = secb + "";
			}
			////////////////////////

			j2_1.setText(m + ":" + s);
			j2_2.setText("Black: " + mb + ":" + sb);
			j2_3.setText("White: " + mw + ":" + sw);
			if (min >= 20) {
				JOptionPane.showMessageDialog(null, "Times Up!");
				main.status(true);
				main.startingOverFromTheBegining();
				secw = 0;
				minw = 0;
				secb = 0;
				minb = 0;
				sec = 0;
				min = 0;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}
	}

	void setTimerMain(int m, int s) {
		sec = s;
		min = m;
	}

	void setTimerBlack(int m, int s) {
		secb = s;
		minb = m;
	}

	void setTimerWhite(int m, int s) {
		secw = s;
		minw = m;
	}

	String getTimerMain() {
		return m + "," + s;
	}

	String getTimerBlack() {
		return mb + "," + sb;
	}

	String getTimerWhite() {
		return mw + "," + sw;
	}
}

class LiveScore extends JFrame {
	JPanel jp = new JPanel(new GridLayout(64, 3, 1, 1));
	JLabel[][] score = new JLabel[62][3];

	LiveScore() {
		super("Live scores");
		setSize(200, 850);
		setLocation(580, 50);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getContentPane().add(jp);
		jp.setBackground(Color.gray);
		score[0][0] = new JLabel("LEVEL", SwingConstants.CENTER);
		score[0][1] = new JLabel("WHITE", SwingConstants.CENTER);
		score[0][2] = new JLabel("BLACK", SwingConstants.CENTER);
		score[1][0] = new JLabel("0", SwingConstants.CENTER);
		score[1][1] = new JLabel("2", SwingConstants.CENTER);
		score[1][2] = new JLabel("2", SwingConstants.CENTER);
		for (int i = 2; i < 62; i++) {
			score[i][0] = new JLabel("" + (i - 1), SwingConstants.CENTER);
			score[i][1] = new JLabel("", SwingConstants.CENTER);
			score[i][2] = new JLabel("", SwingConstants.CENTER);
		}
		for (int i = 0; i < 62; i++)
			for (int j = 0; j < 3; j++) {
				score[i][j].setOpaque(true);
				score[i][j].setBackground(Color.lightGray);
				jp.add(score[i][j]);
			}
		setVisible(true);
	}

	void setScore(int level, boolean white, int value) {
		score[level + 1][white ? 1 : 2].setText(value + "");
	}

	void clearScore() {
		for (int i = 1; i < 61; i++) {
			for (int j = 1; j < 3; j++) {
				score[i + 1][j].setText("");
			}
		}
	}
}

public class Reversi {
	public static void main(String[] args) {
		MainBoard func = new MainBoard();
		func.in.setMainBoard(func);
	}
}