//烏龜的Runnable
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.lang.Runnable;
public class Turtle extends JLabel implements Runnable{
	private Icon[] picture = {new ImageIcon("w2.png"),new ImageIcon("w.png")};//烏龜的圖片
	private int right;//左或右
	private Random generator = new Random();
	private Timer time;//讓烏龜動的Timer
	private int x,y;//座標
	private int d;//游的距離
	private boolean isStop=false;//烏龜狀態為停止或繼續動
	private int one=1;//烏龜下降的距離
	public Turtle(Point p){
		right = generator.nextInt(2);//random方向
		x = p.x-picture[right].getIconWidth()/2;
		y = p.y-picture[right].getIconHeight()/2;
		d = generator.nextInt(5)+1;//random速度
		setIcon(picture[right]);
		setBounds(x,y,picture[right].getIconWidth(),picture[right].getIconHeight());
	}
	public void terminate(){//讓烏龜停止的方法
		d = 0;
		one=0;
		isStop=true;
	}
	public void keepMove(){//讓烏龜動的方法
		d = generator.nextInt(4)+1;
		one=1;
		isStop=false;
	}
	public boolean getisStop(){
		return isStop;
	}
	public void run(){
		
		TurtleMove turtle = new TurtleMove();
		time = new Timer(25,turtle);
		time.start();

		
	}
	
	public class TurtleMove implements ActionListener{//讓烏龜移動的Handler
		public void actionPerformed(ActionEvent e){
			if(y<518-(picture[0].getIconHeight()-30)){
				y=y+one;
			}else if(right==1){
				if(x<893-picture[0].getIconWidth()){
					x=x+d;
				}else{
					right=0;
					setIcon(picture[right]);
					x=x-d;
				}
			}else{
				if(x>0)
					x=x-d;
				else{
					right=1;
					setIcon(picture[right]);
					x=x+d;
				}
			}
			setLocation(x,y);
		}
	}
}
