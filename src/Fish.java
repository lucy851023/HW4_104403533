//����Runnable
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.lang.Runnable;

public class Fish extends JLabel implements Runnable{
	private ImageIcon[][] picture = {{new ImageIcon("2.png"),new ImageIcon("4.png"),new ImageIcon("6.png")},
								{new ImageIcon("1.png"),new ImageIcon("3.png"),new ImageIcon("5.png")}};//�����Ϥ�
	private int right;//���Υk
	private int kind;//����
	private int size;//�j�p
	private int down;//�W�ΤU
	private int d;//�媺�Z��
	private Random ran = new Random();
	private Timer move;//�����ʪ�Timer
	private Timer change;//random ��V�B�t�ת�Timer
	private int x,y;//�y��
	private Icon[] newImage = new ImageIcon[2];//���ܹϤ��j�p�᪺Icon
	private boolean isStop=false;//�ثe�o���������A��������~��
	
	public Fish(Point p){
		kind = ran.nextInt(3);//��random����.��V.�j�p.�t��
		right = ran.nextInt(2);
		down = ran.nextInt(2);
		size = ran.nextInt(100)+50;
		d = ran.nextInt(6)+1;
		newImage[0] = new ImageIcon(picture[0][kind].getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH));
		newImage[1] = new ImageIcon(picture[1][kind].getImage().getScaledInstance(size,size,Image.SCALE_SMOOTH));
		//��Ϥ��j�p
		x = p.x-newImage[right].getIconWidth()/2;
		y = p.y-newImage[right].getIconHeight()/2;

		setIcon(newImage[right]);
		setBounds(x,y,newImage[right].getIconWidth(),newImage[right].getIconHeight());
	}
	private class Fishmove implements ActionListener{//�������ʪ�Handler
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
	public void terminate(){//���������k
		d = 0;
		isStop = true;
	}
	public void keepMove(){//�����~��媺��k
		d = ran.nextInt(5)+1;
		isStop = false;
	}
	public void run()
	{
		
		Fishmove  fishmove = new  Fishmove();
		move = new Timer(25,fishmove);
			
		change = new Timer(7000,new ActionListener(){
			public void actionPerformed(ActionEvent e){//random ��V�B�t��
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
