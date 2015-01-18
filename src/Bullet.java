/*
 * Program: Y-Type
 * Name: Maria Kang and Austin Du
 * Date: June 12, 2014
 * Description: Bullet  class
 */

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Bullet {
	
	//VARIABLES
	public double xPos, yPos;
	private double dx, dy;
	private double angle;
	private int speed = 30;
	private int outX, outY=0;
	
	//CONSTRUCTOR
	public Bullet(double angle){
		this.angle=angle;
		this.xPos = Game.width/2;
		this.yPos = Game.height;
		this.dx = Math.cos(angle)*speed;
		this.dy = Math.sin(angle)*speed;

	}
	
	//DRAWS BULLET IMAGE AND ROTATES IT ACCORDINGLY
	public void draw(Graphics g, BufferedImage bImg){
		//ROTATION code taken from online (the next four lines)
		double locationX = bImg.getWidth() / 2;
		double locationY = bImg.getHeight() / 2;
		AffineTransform tx = AffineTransform.getRotateInstance(Math.toRadians(90)-angle, locationX, locationY);
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
		
		g.drawImage(op.filter(bImg,null),(int)xPos, (int)yPos, null);
		
	}
	
	//BULLET TRAVELS
	public void travel(){
		xPos += dx;
		yPos -= dy;
	}
	

	
	public void setY(double yPos){
		this.yPos = yPos;
	}
	
	public double getX(){
		return xPos;
	}
	
	public double getY(){
		return yPos;
	}

	public void setOutY(int outY){
		this.outY=outY;
	}
	public int getOutY(){
		return outY;
	}
}

