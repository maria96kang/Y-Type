/*
 * Program: Y-Type
 * Name: Maria Kang and Austin Du
 * Date: June 12, 2014
 * Description: Enemy Class
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;


public class Enemy {
	
	//VARIABLES
	private int xPos, yPos, type;
	private int diameter;
	
	private double dx, dy, diffX, diffY;
	private double speed, angle;
	private long lastUpdated;
	
	private String word;
	private String originalWord;
	private static Font arial;
	
	private int collisionCounter;

	private boolean visible = true;
	
	private BufferedImage image = null;
	
	//CONSTRUCTOR
	public Enemy (int xPos, int yPos, String word, BufferedImage image1, BufferedImage image2){
		this.xPos = xPos;
		this.yPos = yPos;
		this.word = word; 
		this.originalWord = word; 
		
		//SETS APPROPERIATE IMAGE DEPENDING ON WORD LENGTH
		if(word.length() < 7)
			this.image = image1;
		else
			this.image = image2;
		
		speed = 3;
		
		changeAngle();
	
		arial = new Font("Arial", Font.BOLD, 12);
		
		lastUpdated = System.currentTimeMillis();
		
	}
	
//	public void draw(Graphics g, BufferedImage bImg){
//		g.drawImage(bImg,(int)xPos, (int)yPos, null);
//	}
	
	//DRAW METHOD
	public void draw(Graphics g){
		//WORD IS HIGHLIGHTED WHEN TYPED
		if(originalWord.length() > word.length())
			g.setColor(Color.getHSBColor(134, 177, 186));
		else
			g.setColor(Color.white);
		
		if (visible){
			g.drawImage(image, xPos-image.getWidth()/2, yPos-image.getHeight()/2, null);
			
			//g.fillOval(xPos-diameter/2, yPos-diameter/2, diameter, diameter);
			g.setFont(arial);
			g.drawString(word, xPos+image.getWidth()/2, yPos);
		}
	}
	
	public void setVisible(boolean v){
		visible = v;
	}
	public boolean getVisible(){
		return visible;
	}
	public void travel(){
		if (!visible){
			xPos=-50;
			yPos=-50;
		}
		long timeElapsed = System.currentTimeMillis()-lastUpdated;
		lastUpdated = System.currentTimeMillis();
		xPos += dx*(timeElapsed/(double)Game.fps);
		yPos += Math.abs(dy*(timeElapsed/(double)Game.fps));
		if (yPos < 650){
			changeAngle();
		}
		//xPos += dx;
		//yPos += Math.abs(dy);
		//System.out.println("Word: "+word+" | Angle: "+angle+" | " +diffX+" = "+(Game.width/2)+" - "+xPos+" | diffY: "+diffY+" | xPos: "+xPos+ " | yPos: "+ yPos+" | dx: "+dx+" | dy: "+dy+ " | addX: "+(dx*(timeElapsed/10))+" | addY: "+(dy*(timeElapsed/10))+" | "+(timeElapsed));
		//System.out.println("Word: "+word+" | Angle: "+angle+" | " +diffX+" = "+(Game.width/2)+" - "+xPos+" | diffY: "+diffY+" | xPos: "+xPos+ " | yPos: "+ yPos+" | dx: "+dx+" | dy: "+dy);
		//System.out.println(timeElapsed/100.0);
	}
	
	//RECOILS WHEN HIT BY BULLET, ANIMATION PURPOSES
	public void recoil(){
		xPos -= dx*(60/(double)Game.fps);
		yPos -= Math.abs(dy*(60/(double)Game.fps));
	}
	
	public void incrementCC(){
		collisionCounter++;
	}
	
	public int getCC(){
		return collisionCounter;
	}
	
	public String getOriginalWord(){
		return originalWord;
	}
	
	public int originalLength(){
		return originalWord.length();
	}
	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}
	
	public String getWord(){
		return word; 
	}
	
	public void setWord(String word){
		this.word = word; 
	}
	
	public void changeAngle(){
		diffX = Game.width/2-xPos;
		diffY = Game.height-yPos;
		angle = Math.toDegrees(Math.atan2(diffY,diffX));
		
		if (angle > 90 && diffX >= 0){
			angle = 180 - angle;
		}
		
		dx = Math.cos(Math.toRadians(angle)) * speed;
		dy = Math.sin(Math.toRadians(angle)) * speed;
	}
	

	
}
