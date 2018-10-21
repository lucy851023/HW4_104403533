//魚的Runnable
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.lang.Runnable;

public class Fish extends JLabel implements Runnable{
	private ImageIcon[][] picture = {{new ImageIcon("2.png"),new ImageIcon("4.png"),new ImageIcon("6.png")},
								{new ImageIcon("1.png"),new ImageIcon("3.png"),new ImageIcon("5.png")}};//魚的圖片
	private int right;//左或右
	private int kind;//種類
	private int size;//大小
	private int down;//上或下
	private int d;//游的距離
	private Random ran = new Random();
	private Timer move;//讓魚動的Timer
	private Timer change;//random 方向、速度的Timer
	private int x,y;//座標
	private Icon[] newImage = new ImageIcon[2];//改變圖片大小後的Icon
	private boolean isStop=false;//目前這隻魚的狀態為停止或繼續
	
	public Fish(Point p){
		kind = ran.nextInt(3);//先random種類.方向.大小.速度
		right = ran.nextInt(2);
		down = ran.nextInt(2);
		size = ran.nextInt(100)+50;
		d = ran.nextInt(6)+1;
		newImage[0] = new ImageIcon(picture[0][kind].getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH));
		newImage[1] = new ImageIcon(picture[1][kind].getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH));
		//改圖片大小
		x = p.x-newImage[right].getIconWidth()/2;
		y = p.y-newImage[right].getIconHeight()/2;

		setIcon(newImage[right]);
		setBounds(x,y,newImage[right].getIconWidth(),newImage[right].getIconHeight());
	}
	private class Fishmove implements ActionListener{//讓魚移動的Handler
		public void actionPerformed(ActionEvent e){
			setIcon(newImage[right]);
			if(right==1){
				if(x<893-newImage[right].getIconWidth()){
					x=x+d;
				}else{
					right=0;
					setIcon(newImage[right]);
					x=x-d;
				}
					
			}else{
				if(x>0){
					x=x-d;
				}else{
					right = 1;
					setIcon(newImage[right]);
					x=x+d;
				}
			}
			
			if(down==1){
				if(y+newImage[right].getIconHeight()<518)
					y=y+d;
				else{
					down=0;
					y=y-d;}
			}else{
				if(y>0)
					y=y-d;
				else{
					down=1;
					y=y+d;
					}
			}
			setLocation(x,y);
		}
		
	}
	public void terminate(){//讓魚停止的方法
		d = 0;
		isStop = true;
	}
	public void keepMove(){//讓魚繼續游的方法
		d = ran.nextInt(5)+1;
		isStop = false;
	}
	public void run()
	{
		
		Fishmove  fishmove = new  Fishmove();
		move = new Timer(25,fishmove);
			
		change = new Timer(7000,new ActionListener(){
			public void actionPerformed(ActionEvent e){//random 方向、速度
				if(d!=0){
				Random r = new Random();
				d = r.nextInt(4)+1;
				down = r.nextInt(2);
				right = r.nextInt(2);
				}
			}	
		});
		move.start();
		change.start();
		}
	public boolean getisStop(){
		return isStop;
	}
}
