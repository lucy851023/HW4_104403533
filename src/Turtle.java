//�Q�t��Runnable
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.lang.Runnable;
public class Turtle extends JLabel implements Runnable{
	private Icon[] picture = {new ImageIcon("w2.png"),new ImageIcon("w.png")};//�Q�t���Ϥ�
	private int right;//���Υk
	private Random generator = new Random();
	private Timer time;//���Q�t�ʪ�Timer
	private int x,y;//�y��
	private int d;//�媺�Z��
	private boolean isStop=false;//�Q�t���A��������~���
	private int one=1;//�Q�t�U�����Z��
	public Turtle(Point p){
		right = generator.nextInt(2);//random��V
		x = p.x-picture[right].getIconWidth()/2;
		y = p.y-picture[right].getIconHeight()/2;
		d = generator.nextInt(5)+1;//random�t��
		setIcon(picture[right]);
		setBounds(x,y,picture[right].getIconWidth(),picture[right].getIconHeight());
	}
	public void terminate(){//���Q�t�����k
		d = 0;
		one=0;
		isStop=true;
	}
	public void keepMove(){//���Q�t�ʪ���k
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
	
	public class TurtleMove implements ActionListener{//���Q�t���ʪ�Handler
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
