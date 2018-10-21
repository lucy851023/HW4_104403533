//104403533_���_��_��ޤGB    //�D�{����Class
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;
public class FishTank extends JFrame{
	private JPanel panel;//�e���A�����ڽc���I��
	private ArrayList<Fish> fish = new ArrayList<Fish>();//�s��Fish��ArrayList
	private ArrayList<Turtle> turtle= new ArrayList<Turtle>();//�s��Turtle��ArrayList
	private JLabel location;//��Ц�m
	private JButton[] buttons = new JButton[6];//���s
	private JPanel buttonArea;//����s��JPanel
	private JLabel funtion;//��ܥثe�\�઺JLabel
	private JLabel fishNumber;//��ܳ��ƶq��JLabel
	private int fishCount;//�ثe���ƶq
	private int now=-1;//�����ثe�I�F���ӫ��s
	private boolean isStopAll=false;
	private ExecutorService executorService;
	
	public FishTank(){
		super("���ڽc");
		buttonArea = new JPanel();
		buttonArea.setLayout(new GridLayout(3,3));
		buttonArea.setBackground(Color.DARK_GRAY);
		buttons[0]= new JButton("�s�W��");
		buttons[1]= new JButton("�������");
		buttons[2]= new JButton("����/�~����");
		buttons[3]= new JButton("�s�W�Q�t");
		buttons[4]= new JButton("��������");
		buttons[5]= new JButton("����/�~�����");
		
		buttons[0].addActionListener(new ButtonHandler());
		buttons[1].addActionListener(new ButtonHandler());
		buttons[2].addActionListener(new ButtonHandler());
		buttons[3].addActionListener(new ButtonHandler());
		buttons[4].addActionListener(new DeleteHandler());
		buttons[5].addActionListener(new StopAllHandler());
		for(JButton b:buttons)
			buttonArea.add(b);
		funtion = new JLabel("�ثe�\��:");
		fishNumber = new JLabel("���ƶq:");
		
		funtion.setForeground(Color.CYAN);
		fishNumber.setForeground(Color.CYAN);
		buttonArea.add(funtion);
		buttonArea.add(fishNumber);
		
		add(buttonArea,BorderLayout.NORTH);
		
		panel = new JPanel();
		panel.setLayout(null);
		Color water = new Color(0,153,255);//�]�w���ڽc�I����
		panel.setBackground(water);
		
		panel.addMouseMotionListener(new mouseHandler());
		panel.addMouseListener(new AnimalHandler());
		
		
		executorService = Executors.newCachedThreadPool();
		
		location = new JLabel("��Ц�m:");
		add(panel,BorderLayout.CENTER);
		add(location,BorderLayout.SOUTH);
		
		
	}
	private class mouseHandler extends MouseMotionAdapter{
		public void mouseMoved(MouseEvent event){
			location.setText(String.format("��Ц�m:[%d,%d]", event.getX(),event.getY()));
		}
	}
	
	private class AnimalHandler extends MouseAdapter{//�e����MouseHandler
		public void mousePressed(MouseEvent event)
		{	
			if(now==0){//���F"�s�W��"
				fish.add(new Fish(event.getPoint()));//�s�W��
				
				fish.get(fish.size()-1).addMouseListener(new PictureHandler());//�����[�Ϥ���MouseHandler�M��[�ifish(ArrayList)��
				panel.add(fish.get(fish.size()-1));//�[�i�e����
				
				executorService.execute(fish.get(fish.size()-1));//���泽�������
				fishCount++;//���ƶq+1
				fishNumber.setText("���ƶq:"+fishCount);
			}else if(now==3){//���F"�s�W�Q�t"
				turtle.add(new Turtle(event.getPoint()));//�s�W�Q�t
				turtle.get(turtle.size()-1).addMouseListener(new PictureHandler());//���Q�t�[�Ϥ���MouseHandler�M��[�ifish(ArrayList)��
				panel.add(turtle.get(turtle.size()-1));//�[�i�e����
				
				executorService.execute(turtle.get(turtle.size()-1));//����Q�t�������
			}
		}
		
	}
	private class PictureHandler extends MouseAdapter{//���B�Q�t�Ϥ���MouseHandler
		public void mousePressed(MouseEvent event){
			if(now==1)//���F"�������"
			{
				JLabel source = (JLabel)event.getSource();
				
				if(event.getSource() instanceof Fish)//�P�_�I�諸�O���٬O�Q�t
				{
					fishCount--;
					fishNumber.setText("���ƶq:"+fishCount);
					fish.remove(event.getSource());//�qfish(ArrayLIst)�̲���
				}else{
					turtle.remove(event.getSource());//�qturtle(ArrayLIst)�̲���
				}
				
				panel.remove(source);//�q�e���W����
				panel.repaint();
			}else if(now==2)/*���F"����/�~����"*/{
				if(event.getSource() instanceof Fish)//�P�_�I�諸�O���٬O�Q�t
				{	
					Fish f =  (Fish)event.getSource();
					if(f.getisStop()==false)//�P�_�������A�O�_������
						f.terminate(); //����
					else
						f.keepMove();//�~��
				}else{
					Turtle t = (Turtle)event.getSource();
					if(t.getisStop()==false)//�P�_�Q�t�����A�O�_������
						t.terminate();//����
					else
						t.keepMove();//�~��
				}
					 
			}
		}
	}
	private class DeleteHandler implements ActionListener{//���s"��������"��Handler
		public void actionPerformed(ActionEvent e){
			now=4;
			funtion.setText("�ثe�\��:"+e.getActionCommand());
			fish.clear();//�Nfish�M��
			turtle.clear();//turtle�M��
				
			panel.removeAll();//�����e���W���Ҧ��F��
			panel.repaint();
			fishCount=0;//���ƶq�k�s
			fishNumber.setText("���ƶq:"+fishCount);
		}
	}
	private class ButtonHandler implements ActionListener{//���s"�s�W��"�B"�s�W�Q�t"�B"�������"�B"����/�~����"��Handler
		public void actionPerformed(ActionEvent e){
				funtion.setText("�ثe�\��:"+e.getActionCommand());
				if(e.getSource()==buttons[0])//�P�_�I����ӫ��s
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
	private class StopAllHandler implements ActionListener{//���s"����/�~�����"��Handler
		public void actionPerformed(ActionEvent e){
			now=5;
			
			if(!isStopAll){//�P�_�ثe���A�������٬O�~��
				for(Fish f:fish)//���Ҧ�������
					f.terminate();
				for(Turtle t:turtle)//���Ҧ��Q�t����
					t.terminate();
				funtion.setText("�ثe�\��:�������");
			}
			else{
				for(Fish f:fish)//���Ҧ�����
					f.keepMove();
				for(Turtle t:turtle)//���Ҧ��Q�t��
					t.keepMove();
				funtion.setText("�ثe�\��:�~�����");
			}
			isStopAll = !isStopAll;//���ܪ��A
			
		}
	}

	public static void main(String[] args){//�D�{��
		FishTank moveLabel = new FishTank();
		moveLabel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		moveLabel.setSize(900,650); 
		moveLabel.setVisible(true); 
		moveLabel.setResizable(false);
	}
}
