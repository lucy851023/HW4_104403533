//104403533_莊于萱_資管二B    //主程式於此Class
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
public class FishTank extends JFrame{
	private JPanel panel;//畫布，為水族箱的背景
	private ArrayList<Fish> fish = new ArrayList<Fish>();//存放Fish的ArrayList
	private ArrayList<Turtle> turtle= new ArrayList<Turtle>();//存放Turtle的ArrayList
	private JLabel location;//游標位置
	private JButton[] buttons = new JButton[6];//按鈕
	private JPanel buttonArea;//放按鈕的JPanel
	private JLabel funtion;//顯示目前功能的JLabel
	private JLabel fishNumber;//顯示魚數量的JLabel
	private int fishCount;//目前魚數量
	private int now=-1;//紀錄目前點了哪個按鈕
	private boolean isStopAll=false;
	private ExecutorService executorService;
	
	public FishTank(){
		super("水族箱");
		buttonArea = new JPanel();
		buttonArea.setLayout(new GridLayout(3,3));
		buttonArea.setBackground(Color.DARK_GRAY);
		buttons[0]= new JButton("新增魚");
		buttons[1]= new JButton("移除選取");
		buttons[2]= new JButton("停止/繼續選取");
		buttons[3]= new JButton("新增烏龜");
		buttons[4]= new JButton("移除全部");
		buttons[5]= new JButton("停止/繼續全部");
		
		buttons[0].addActionListener(new ButtonHandler());
		buttons[1].addActionListener(new ButtonHandler());
		buttons[2].addActionListener(new ButtonHandler());
		buttons[3].addActionListener(new ButtonHandler());
		buttons[4].addActionListener(new DeleteHandler());
		buttons[5].addActionListener(new StopAllHandler());
		for(JButton b:buttons)
			buttonArea.add(b);
		funtion = new JLabel("目前功能:");
		fishNumber = new JLabel("魚數量:");
		
		funtion.setForeground(Color.CYAN);
		fishNumber.setForeground(Color.CYAN);
		buttonArea.add(funtion);
		buttonArea.add(fishNumber);
		
		add(buttonArea,BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setLayout(null);
		Color water = new Color(0,153,255);//設定水族箱背景色
		panel.setBackground(water);
		
		panel.addMouseMotionListener(new mouseHandler());
		panel.addMouseListener(new AnimalHandler());
		
		
		executorService = Executors.newCachedThreadPool();
		
		location = new JLabel("游標位置:");
		add(panel,BorderLayout.CENTER);
		add(location,BorderLayout.SOUTH);
		
		
	}
	private class mouseHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent event){
			location.setText(String.format("游標位置:[%d,%d]", event.getX(),event.getY()));
		}
	}
	
	private class AnimalHandler extends MouseAdapter{//畫布的MouseHandler
		public void mousePressed(MouseEvent event)
		{	
			if(now==0){//按了"新增魚"
				fish.add(new Fish(event.getPoint()));//新增魚
				
				fish.get(fish.size()-1).addMouseListener(new PictureHandler());//幫魚加圖片的MouseHandler然後加進fish(ArrayList)裡
				panel.add(fish.get(fish.size()-1));//加進畫布裡
				
				executorService.execute(fish.get(fish.size()-1));//執行魚的執行序
				fishCount++;//魚數量+1
				fishNumber.setText("魚數量:"+fishCount);
			}else if(now==3){//按了"新增烏龜"
				turtle.add(new Turtle(event.getPoint()));//新增烏龜
				turtle.get(turtle.size()-1).addMouseListener(new PictureHandler());//幫烏龜加圖片的MouseHandler然後加進fish(ArrayList)裡
				panel.add(turtle.get(turtle.size()-1));//加進畫布裡
				
				executorService.execute(turtle.get(turtle.size()-1));//執行烏龜的執行序
			}
		}
		
	}
	private class PictureHandler extends MouseAdapter{//魚、烏龜圖片的MouseHandler
		public void mousePressed(MouseEvent event){
			if(now==1)//按了"移除選取"
			{
				JLabel source = (JLabel)event.getSource();
				
				if(event.getSource() instanceof Fish)//判斷點選的是魚還是烏龜
				{
					fishCount--;
					fishNumber.setText("魚數量:"+fishCount);
					fish.remove(event.getSource());//從fish(ArrayLIst)裡移除
				}else{
					turtle.remove(event.getSource());//從turtle(ArrayLIst)裡移除
				}
				
				panel.remove(source);//從畫布上移除
				panel.repaint();
			}else if(now==2)/*按了"停止/繼續選取"*/{
				if(event.getSource() instanceof Fish)//判斷點選的是魚還是烏龜
				{	
					Fish f =  (Fish)event.getSource();
					if(f.getisStop()==false)//判斷魚的狀態是否為停止
						f.terminate(); //停止
					else
						f.keepMove();//繼續
				}else{
					Turtle t = (Turtle)event.getSource();
					if(t.getisStop()==false)//判斷烏龜的狀態是否為停止
						t.terminate();//停止
					else
						t.keepMove();//繼續
				}
					 
			}
		}
	}
	private class DeleteHandler implements ActionListener{//按鈕"移除全部"的Handler
		public void actionPerformed(ActionEvent e){
			now=4;
			funtion.setText("目前功能:"+e.getActionCommand());
			fish.clear();//將fish清空
			turtle.clear();//turtle清空
				
			panel.removeAll();//移除畫布上的所有東西
			panel.repaint();
			fishCount=0;//魚數量歸零
			fishNumber.setText("魚數量:"+fishCount);
		}
	}
	private class ButtonHandler implements ActionListener{//按鈕"新增魚"、"新增烏龜"、"移除選取"、"停止/繼續選取"的Handler
		public void actionPerformed(ActionEvent e){
				funtion.setText("目前功能:"+e.getActionCommand());
				if(e.getSource()==buttons[0])//判斷點選哪個按鈕
					now = 0;
				else if(e.getSource()==buttons[3])
					now = 3;
				else if(e.getSource()==buttons[1])
					now =1 ;
				else if(e.getSource()==buttons[2]){
					now =2;
				}
		}
	}
	private class StopAllHandler implements ActionListener{//按鈕"停止/繼續全部"的Handler
		public void actionPerformed(ActionEvent e){
			now=5;
			
			if(!isStopAll){//判斷目前狀態為停止還是繼續
				for(Fish f:fish)//讓所有魚停止
					f.terminate();
				for(Turtle t:turtle)//讓所有烏龜停止
					t.terminate();
				funtion.setText("目前功能:停止全部");
			}
			else{
				for(Fish f:fish)//讓所有魚動
					f.keepMove();
				for(Turtle t:turtle)//讓所有烏龜動
					t.keepMove();
				funtion.setText("目前功能:繼續全部");
			}
			isStopAll = !isStopAll;//改變狀態
			
		}
	}

	public static void main(String[] args){//主程式
		FishTank moveLabel = new FishTank();
		moveLabel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		moveLabel.setSize(900,650); 
		moveLabel.setVisible(true); 
		moveLabel.setResizable(false);
	}
}
