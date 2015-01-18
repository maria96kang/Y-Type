/*
 * Program: Y-Type
 * Name: Austin Du and Maria Kang
 * Date: June 12, 2014
 * Teacher: Mrs. S
 * Description: Game class which runs the game and contains all the necessary methods
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import javax.imageio.*;
import javax.swing.*;

public class Game extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	
	//VARIABLES
	static int width, height;
	static int fps = 50;
	private int key = -1;
	
	private BufferedImage bImg = null;
	private BufferedImage spaceShip = null;
	private BufferedImage enemy1 = null;
	private BufferedImage enemy2 = null;
	private BufferedImage menuBg = null;
	private BufferedImage instruBg = null;
	
	private ArrayList<Bullet> bulletList = new ArrayList<Bullet>();
	private ArrayList<Enemy> enemyList = new ArrayList<Enemy>();
	private ArrayList<Score> scores = new ArrayList<Score>();
	
	private String smallWords[] = new String[260]; //ten words per letter
	private String largeWords[] = new String[260]; //ten words per letter
	private ArrayList<String> currentWords = new ArrayList<String>();
	private int index = -1; //index position of current word being typed
	
	private int mouseX=0, mouseY=0;
	private int gameState=0;
	
	private double angle = Math.toRadians(90);
	
	private ArrayList<Integer> previousIndex = new ArrayList<Integer>();
	
	private Timer t;
	private long lastUpdated;
	
	private int waveLevel = 1;
	private int wavePeriod = 10000;
	private int enemyPeriod = 2000;
	private int waveInitializer = wavePeriod;
	private int enemyInitializer = enemyPeriod;
	private int userScore = 0;
	
	
	public boolean lose = false; 
	private String name = "";
	
	//GAME CONSTRUCTOR
	public Game(){
		
		super("Invasion");
		
		Container c = getContentPane();
		//c.setBackground(Color.darkGray);
		c.setBackground(Color.black);
		
		setSize(350,700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setResizable(false);
		
		addMouseListener(this);
		addMouseMotionListener(this);
		addKeyListener(this);
		
		width = this.getSize().width;
	    height = this.getSize().height;
	  
	    
	    fileReader();
	    
	    //IMAGE LOADING
	    try {
		    bImg = ImageIO.read(new File("img/W_7.png")); //Bullet
		    spaceShip = ImageIO.read(new File("img/W_8.png"));
		    enemy1 = ImageIO.read(new File("img/W_9.png"));
		    enemy2 = ImageIO.read(new File("img/W_10.png"));
		    menuBg = ImageIO.read(new File("img/menuBg.jpg"));
		    instruBg = ImageIO.read(new File("img/instruBg.jpg"));
		}catch (IOException e) {
			System.out.print("Error loading image " + e);
		}  
	    
	    //TIMER
		t = new Timer(fps, this);
		t.start();
		
		lastUpdated = System.currentTimeMillis();
		
	}
	
	//FILE READER METHOD
	public void fileReader(){
		 
		try {
			
			FileReader frS = new FileReader(new File("smallWords.txt")); //reading files
			BufferedReader bS = new BufferedReader(frS); //buffered reader
			FileReader frL = new FileReader(new File("largeWords.txt")); //reading files
			BufferedReader bL = new BufferedReader(frL); //buffered reader
			
			for (int i=0;i<smallWords.length;i++){ //reading lines, adding current scores to array with names
				String a=bS.readLine();
				smallWords[i]=a;
			}
			
			for (int i=0;i<largeWords.length;i++){
				String a = bL.readLine();
				largeWords[i]=a;
		}
				
			//closing files
			bS.close();
			frS.close();
			bL.close();
			frL.close();
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	//PAINT METHOD
	public void paint(Graphics g){
		super.paint(g);
		
		if (gameState==0){
			g.drawImage(menuBg, 0,0, null);
		}
		else if (gameState==1){
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial", Font.PLAIN, 12));
			g.drawString("Score: "+userScore, width-75, height-15);
			
			if (waveInitializer > wavePeriod && waveInitializer < wavePeriod+2000){
				g.setColor(Color.white);
				g.setFont(new Font("Arial", Font.BOLD, 40));
				g.drawString("Wave "+waveLevel, width/2-70, height/2-20);
			}
			
			for (int i=0;i<bulletList.size();i++){
				bulletList.get(i).draw(g, bImg);
			}
			
			//draws spaceship (rotation code - next 4 lines - are from an online source)
			double locationX = spaceShip.getWidth() / 2;
			double locationY = spaceShip.getHeight() / 2;
			AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(90)-angle, locationX, locationY);
			AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
			g.drawImage(op.filter(spaceShip,null), 10+Game.width/2 - spaceShip.getWidth()/2, 20+Game.height - (spaceShip.getHeight()+5), null);
			
			for (int i=0;i<enemyList.size();i++){
				enemyList.get(i).draw(g);
			}
		}else if (gameState==2){
			g.drawImage(instruBg, 0,0,null);
		}
	}
	
	public void actionPerformed(ActionEvent e) {
		
		if (gameState==0){
			
		}
		else if (gameState==1){
			long timeElapsed = System.currentTimeMillis()- lastUpdated;
			lastUpdated = System.currentTimeMillis();

			if (timeElapsed >= fps){
				waveInitializer += fps;
			}

			if (waveInitializer > wavePeriod * 2){
				boolean go=true;
				double nextLevel=0;
				for (int i=0;i<currentWords.size();i++){
					if (!currentWords.get(i).equals("")){
						if (go)
							nextLevel=System.currentTimeMillis();
						go=false;
						break;
					}
						
				}
				System.out.println(go);
				
//				if (!go && System.currentTimeMillis()>nextLevel+5000){
//					go=true;
//				}
				
				if (go){
					enemyList.clear();
					currentWords.clear();
					previousIndex.clear();
					if (enemyPeriod>300){
						enemyPeriod -=200;
					}
					waveLevel++;
					waveInitializer = 0;
				}
			}else if (waveInitializer > wavePeriod){
				enemyInitializer += fps;
				if (enemyInitializer > enemyPeriod && enemyList.size() < 25){ // ensures no stackoverflow error
					
					//DETERMINES THE TYPE OF ALIEN THAT GOES ON THE SCREEN
					int num = (int)(Math.random()*10);
					boolean wordLength = true;
					if(num < 7)
						wordLength = false;

					enemyList.add(new Enemy((int)(Math.random()*width), 0, selectWord(wordLength), enemy1, enemy2));
					//(int)(Math.random()*3)+1
					currentWords.add(enemyList.get(enemyList.size()-1).getWord());
					enemyInitializer = 0;

					for (int i = 0; i < currentWords.size(); i++){
						System.out.print(currentWords.get(i)+" ");
					}

					System.out.println();
				}
			}

			for (int i=0;i<enemyList.size();i++){
				if (enemyList.get(i).getVisible()){
					enemyList.get(i).travel();
				}

				/*
			if (enemyList.get(i).getY()>height+50 || enemyList.get(i).getY()<-50 || enemyList.get(i).getX()>width+50 || enemyList.get(i).getX()<-50){
			//if (enemyList.get(i).getY()>height+50){
				enemyList.remove(i);
				currentWords.remove(i); 
				index = -1; 
			}
				 */
				
				//LOSING CONDITIONS
				if(enemyList.get(i).getY() > Game.height - (spaceShip.getHeight()+5)){
					enemyList.remove(i);
					currentWords.remove(i); 
					index = -1; 
					lose = true; 
				}

				/*
			if(index != -1 && collision(enemyList.get(i), bulletList.get(0))){
				System.out.println(bulletList.size());
				if(bulletList.size() > 0)
					bulletList.remove(i);
			}
				 */

			}

			//BULLET TRAVEL
			for (int i=0;i<bulletList.size();i++){
				bulletList.get(i).travel();
//				if (bulletList.get(i).getX()>width+10 || bulletList.get(i).getX()<-10 || bulletList.get(i).getY()<-10 || bulletList.get(i).getY()>height+10){
//					bulletList.remove(i);
//				}else{
					//collision method
					if(index != -1 && collision(enemyList.get(index), bulletList.get(i))){
						//System.out.println(bulletList.size());
						enemyList.get(index).incrementCC();
						enemyList.get(index).recoil();
						bulletList.remove(i);
						i--;
					}
				//}
			}
			
			//for (int i=0;i<enemyList.size();i++){
			for (int i=0;i<bulletList.size();i++){
				for (int j=0;j<previousIndex.size();j++){
					if (previousIndex.get(j)!=-1){
							if (collision(enemyList.get(previousIndex.get(j)), bulletList.get(i))){
								enemyList.get(previousIndex.get(j)).incrementCC();
								enemyList.get(previousIndex.get(j)).recoil();
								//break;
								j=0;
								i=0;
							}
						}
					}
				}
			//}
			
			//REMOVES BULLETS
			for (int i=0;i<bulletList.size();i++){
				if (bulletList.get(i).getY() < bulletList.get(i).getOutY()){
					bulletList.remove(i);
				}
			}
			
			
			for (int i=0;i<previousIndex.size();i++){
				int pI = previousIndex.get(i);
				if (pI!=-1){
					System.out.println(enemyList.get(pI).getOriginalWord()+": "+enemyList.get(pI).getCC() +" "+ enemyList.get(pI).getOriginalWord().length());
					if (enemyList.get(pI).getCC() >= enemyList.get(pI).getOriginalWord().length()){
//						enemyList.remove(i);
//						currentWords.remove(i);
//						if (index!=-1){
//							index--;
//						}
						System.out.println(enemyList.get(pI).getOriginalWord()+ " set to invisible");
						enemyList.get(pI).setVisible(false);
//						//currentWords.set(i, "");
						previousIndex.set(i, -1);
						i=0;
					}
				}
			}
			
//			for (int i=0;i<previousIndex.size();i++){
//				//System.out.print(previousIndex.get(i)+" ");
//				if (previousIndex.get(i)==-1){
//					previousIndex.remove(i);
//					i--;
//				}
//			}
			
			//System.out.println(enemyList.size()+" "+bulletList.size());
//			if (index!=-1)
//			System.out.println("focus: "+enemyList.get(index).getWord());
//			for (int i=0;i<enemyList.size();i++){
//				if (!enemyList.get(i).getVisible()){
//					enemyList.remove(i);
//					currentWords.remove(i);
//					if (index!=-1){
//						index--;
//					}
//				}
//			}
			
			//LOSING CONDITIONS
			if(lose){
				
				System.out.println("Score: "+ userScore);
				
				
				Object[] options = {"Ok"};
				JPanel panel = new JPanel();
				panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
				
				JLabel info = new JLabel("Game Over!");
				JLabel info2 = new JLabel("Score: "+userScore);
				JLabel info3 = new JLabel("\n");
				JLabel label = new JLabel("Enter name: ");
				
				JTextField txt = new JTextField(8);
				
				panel.add(info);
				panel.add(info2);
				panel.add(info3);
				panel.add(label);
				panel.add(txt);
				
				int selectedOption = JOptionPane.showOptionDialog(null, panel, "Game Over", JOptionPane.NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options , options[0]);
				
				if(selectedOption == 0)
				{
				    name = txt.getText();
				    // ...
				}
				
				//String name = JOptionPane.showInputDialog(null,"Game Over! Score: "+userScore+"\n\nEnter name: ", "2048 Game Over",1);
				Score s[]=new Score[6];

				try { //adding current scores and new scores to the array, then sorting it accordingly to get top 5 (effectively removing the lowest score), and writing those top 5 back to the text file..
					FileReader fr = new FileReader(new File("score.txt"));
					BufferedReader b = new BufferedReader(fr); 
					for (int i=0;i<5;i++){
						String a=b.readLine();
						if (a!=null){
							s[i]=new Score(a.substring(0, a.indexOf("\t")), Integer.parseInt(a.substring(a.indexOf("\t")+1)));
						}
					}
					s[5]=new Score(name,userScore);
					Arrays.sort(s);

					FileWriter fw = new FileWriter(new File("score.txt"));
					BufferedWriter w = new BufferedWriter(fw);
					for(int i=0;i<5;i++) {
						w.write(s[i].getName()+"\t"+s[i].getScore());
						w.newLine();
					}

					b.close(); //closing everything
					w.close();
					fr.close();
					fw.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}


				//			 try {
				//
				//				File file = new File("highscore.txt");
				//	 
				//				FileWriter fw = new FileWriter(file.getName(),true);
				//				BufferedWriter bw = new BufferedWriter(fw);
				//				bw.write(name+" "+userScore+"\n");
				//				bw.close();
				//			} catch (IOException e1) {
				//				e1.printStackTrace();
				//			}

				newGame();
				gameState=0;
			}

		}
		
		repaint();
	}
	
	//RESETS ALL VARIABLES
	public void newGame(){
		
		waveLevel = 1;
		lose = false; 
		index = -1;
		name = "";
		userScore = 0;
		wavePeriod = 100000;
		enemyPeriod = 2000;
		waveInitializer = wavePeriod;
		enemyInitializer = enemyPeriod;
		
		bulletList = new ArrayList<Bullet>();
		enemyList = new ArrayList<Enemy>();
		currentWords = new ArrayList<String>();
		previousIndex.clear();
	}
	
	//READS HIGHSCORE FILES
	public void readFile() throws IOException{
		
		scores = new ArrayList<Score>();
		
		BufferedReader br = new BufferedReader(new FileReader(new File("score.txt")));
		String line = br.readLine();
		while (line != null) {
			String player = line.substring(0, line.indexOf(" "));
			int points = Integer.parseInt(line.substring(line.indexOf(" ")+1));
			scores.add(new Score(player, points));	
			line = br.readLine();
		}
		
		Collections.sort(scores);
	 
		br.close();
	}

	public void keyPressed(KeyEvent e) {
		if(gameState==1){
			key = e.getKeyCode();
			char keyChar = e.getKeyText(key).toLowerCase().charAt(0);
			//if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z){
			
			//CHECKS IF A KEY IS PRESSED
			if(index == -1){
				//CHECKS IF KEY MATCHES FIRST LETTER IN WORDLIST
				for (int i=0;i<currentWords.size();i++){

					if (!currentWords.get(i).equals("") && currentWords.get(i).charAt(0) == keyChar){
						
						//BULLET TARGETS ENEMY
						angle = Math.atan2((Game.height-enemyList.get(i).getY()),(enemyList.get(i).getX()-Game.width/2));
						bulletList.add(new Bullet(angle));
						
						bulletList.get(bulletList.size()-1).setOutY((int)enemyList.get(i).getY());
						
						//ADDS SCORE
						userScore++; 
						index = i; 
						
						currentWords.set(i,currentWords.get(i).substring(1));
						enemyList.get(i).setWord(currentWords.get(i));

						break; 

					}
				}
			}else{

				//System.out.println("index in else: "+index);

				if (currentWords.get(index).charAt(0) == keyChar){
					//enemyList.get(i).setTarget(true);

					userScore++; 

					angle = Math.atan2((Game.height-enemyList.get(index).getY()),(enemyList.get(index).getX()-Game.width/2));
					bulletList.add(new Bullet(angle));
					bulletList.get(bulletList.size()-1).setOutY((int)enemyList.get(index).getY());

					currentWords.set(index,currentWords.get(index).substring(1));
					enemyList.get(index).setWord(currentWords.get(index));

					if(enemyList.get(index).getWord().equals("")){
						previousIndex.add(index);
						index = -1; 
					}

					//System.out.println(keyChar);

				}
			}

			key = 0;
		}
	}

	public void keyReleased(KeyEvent e) {	
	}

	public void keyTyped(KeyEvent e) {

	}
	
	//RECURSION METHOD 
	//MAKES SURE NO FIRST LETTER REPEAT ON SCREEN
	public String selectWord(boolean longWord){
		
		String word;
		boolean taken = false;
		char firstLetter;
		
		if (longWord){
			word = largeWords[(int)(Math.random()*260)];
		}else{
			word = smallWords[(int)(Math.random()*260)];
		}
		
		firstLetter=word.charAt(0);
		
		for (int i=0;i<currentWords.size();i++){
			if (!currentWords.get(i).equals("") && currentWords.get(i).charAt(0) == firstLetter){
				taken=true;
			}
		}
		if (taken){
			return selectWord(longWord);
		}
//		else{
//			currentWords.add(word);
//		}
		
		return word;
	}
	
	//CHECK COLLISON
	public boolean collision(Enemy e, Bullet b){
//		if (b.getX()>e.getX()-20 && b.getX()<e.getX()+20 && b.getY()<e.getY()+20 && b.getY()>e.getY()-20){
//			return true;
//		}return false;
		
		/*
		if (b.getY()<e.getY()+20){ //pseudo collision so bullets dont miss...
			return true;
		}
		//System.out.println("false");
		return false;
		*/
		
		return b.getY() < e.getY() + 20;
		
	}

	public void mouseDragged(MouseEvent arg0) {
	}
	public void mouseMoved(MouseEvent e) {
		mouseX = e.getX();
		mouseY = e.getY();
	}
	public void mouseClicked(MouseEvent e) {
		if (gameState == 0){
			if (mouseX > 50 && mouseX < 300 && mouseY>200 && mouseY<250){
				gameState=1;
			}else if (mouseX > 50 && mouseX < 300 && mouseY>275 && mouseY<325){
				gameState=2;
			}else if (mouseX > 50 && mouseX < 300 && mouseY > 350 && mouseY<400){
				Score s[]=new Score[5]; //Top 5 scores
				
				try {
					FileReader fr = new FileReader(new File("score.txt")); //reading files
					BufferedReader b = new BufferedReader(fr); //buffered reader
					for (int i=0;i<5;i++){ //reading lines, adding current scores to array with names
						String a=b.readLine();
						s[i]=new Score(a.substring(0, a.indexOf("\t")), Integer.parseInt(a.substring(a.indexOf("\t")+1)));
					}
					
					Arrays.sort(s); //sorting array in highest to lowest
					
					String hs="";
					for (int i=0;i<5;i++){
						hs+=s[i]+"\n"; // for display
					}
					
					//displaying highscore
					JOptionPane.showMessageDialog(null,"Highscore\n\n"+hs, "2048 Highscores",1);
					
					//closing filereaders
					b.close();
					fr.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}else if (mouseX > 50 && mouseX < 300 && mouseY > 425 && mouseY<475){
				System.exit(0);
			}
		}else if (gameState==2){
			gameState=0;
		}
	}
	public void mouseEntered(MouseEvent arg0) {	
	}
	public void mouseExited(MouseEvent arg0) {	
	}
	public void mousePressed(MouseEvent arg0) {	
	}
	public void mouseReleased(MouseEvent arg0) {
	}
}
