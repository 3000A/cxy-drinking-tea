package Games5;
import java.util.Random;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class PinBall
{
	// ����Ŀ��
	private final int TABLE_WIDTH = 1000;
	// ����ĸ߶�
	private final int TABLE_HEIGHT = 800;
	// ���ĵĴ�ֱλ��
	private final int RACKET_Y = TABLE_HEIGHT-50;
	// ���涨�����ĵĸ߶ȺͿ��
	private final int RACKET_HEIGHT = 30;
	private final int RACKET_WIDTH = 200;
	// С��Ĵ�С
	private final int BALL_SIZE = 24;
	private Frame f = new Frame("������Ϸ");
	Random rand = new Random();
	// С������������ٶ�
	private int ySpeed = -20;
	// ����һ��-0.5~0.5�ı��ʣ����ڿ���С������з���
	private double xyRate = rand.nextDouble() - 0.5;
	// С�����������ٶ�
	public int xSpeed = (int)(ySpeed * xyRate * 2);
	// ballX��ballY����С�������
	private int ballX = 100;
	private int ballY = RACKET_Y-BALL_SIZE*2;
	// racketX�������ĵ�ˮƽλ��
	private int racketX = ballX+BALL_SIZE/2-RACKET_WIDTH/2;
	// ��ҵ�����ֵ
	private int lives=3;
	// ����ֵ��ʾ�����壨֮������ú���ͼƬ���棩
	Font LifeFont= new Font("TimesRoman",Font.BOLD,30);
	private MyCanvas tableArea = new MyCanvas();
	Timer timer;
	// ��Ϸ�Ƿ���������
	private boolean isLose = false;
	// ��Ϸ�Ƿ�ʼ�����
	private boolean isStart = false;
	public void init(){
		//���¹رհ�ť���˳�����
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		// ���������������Ѵ�С
		tableArea.setPreferredSize(
			new Dimension(TABLE_WIDTH , TABLE_HEIGHT));
		f.add(tableArea);
		// ������̼�����
		KeyAdapter keyProcessor = new KeyAdapter(){
			public void keyPressed(KeyEvent ke){
				if(isStart) {
					// �����������Ҽ�ʱ������ˮƽ����ֱ���١�����
					if (ke.getKeyCode() == KeyEvent.VK_LEFT){
						if (racketX > 0)
						racketX -= 50;
					}
					if (ke.getKeyCode() == KeyEvent.VK_RIGHT){
						if (racketX < TABLE_WIDTH - RACKET_WIDTH)
						racketX += 50;
					}
				}

			}
		};
		// Ϊ���ں�tableArea����ֱ���Ӽ��̼�����
		f.addKeyListener(keyProcessor);
		tableArea.addKeyListener(keyProcessor);
		// ����ÿ0.1��ִ��һ�ε��¼���������
		ActionListener taskPerformer = evt ->{
			// ���С���������ұ߿�
			if (ballX  <= 0 || ballX >= TABLE_WIDTH - BALL_SIZE){
				xSpeed = -xSpeed;
			}
			// ���С��߶ȳ���������λ�ã��Һ��������ķ�Χ֮�ڣ�����ֵ-1��
			if (ballY >= RACKET_Y - BALL_SIZE &&
				(ballX < racketX-BALL_SIZE || ballX > racketX + RACKET_WIDTH+BALL_SIZE)){
				// �������ֵ-1
				lives--;
				// �������ֵ��Ϊ0����Ϸ����
				if(lives==0) {
					timer.stop();
					// ������Ϸ�Ƿ���������Ϊtrue��
					isLose = true;
					tableArea.repaint();
				}
				else {
					// ballX��ballY����С�������
					ballX = 100;
					ballY = RACKET_Y-BALL_SIZE*2;
					// racketX�������ĵ�ˮƽλ��
					racketX = ballX+BALL_SIZE/2-RACKET_WIDTH/2;
					isStart=false;
				}
			}
			//���С��ײ���ϱ߿�
			else if (ballY  <= 0) {
				ySpeed = -ySpeed;
			}
			// ���С��λ������֮�ڣ��ҵ�������λ�ã�С�򷴵�----------------------------------------------------start
			else if (ballY >= RACKET_Y - BALL_SIZE
					&& ballX > racketX && ballX <= racketX + RACKET_WIDTH){	
				ySpeed = -ySpeed;
				xSpeed = (int)(-(xSpeed/Math.abs(xSpeed))*(ySpeed*(Math.abs(0.02*(racketX+(RACKET_WIDTH)/2-ballX))>1?(Math.abs(0.02*(racketX+(RACKET_WIDTH)/2-ballX))):1)));
			}
			// С����������
			if(isStart){
				ballY += ySpeed;
				ballX += xSpeed;
			}
			tableArea.repaint();
		};
		timer = new Timer(50, taskPerformer);
		timer.start();
		
		// �������¼��������������������Ϸ��ʼ
		tableArea.addMouseListener(new MouseAdapter (){
			public void mousePressed(MouseEvent e) {
				if(!isStart) {
					isStart = true;
				}
			}
		});
		// �������ƶ��¼���������ʹ����С���λ�ø������
		tableArea.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseMoved(MouseEvent e) {
				if(!isStart) {
					ballX=e.getX()-BALL_SIZE/2;
					racketX=e.getX()-RACKET_WIDTH/2;
				}
			}
		});
		f.pack();
		f.setVisible(true);
	}
	public static void main(String[] args){
		new PinBall().init();
	}
	class MyCanvas extends Canvas{
		// ��дCanvas��paint������ʵ�ֻ滭
		public void paint(Graphics g){
			// �����Ϸ�Ѿ�����
			if (isLose)
			{
				g.setColor(new Color(255, 0, 0));
				g.setFont(new Font("Times" , Font.BOLD, 30));
				g.drawString("��Ϸ�ѽ�����" , TABLE_WIDTH/2-80 ,TABLE_HEIGHT/2);
			}
			// �����Ϸ��δ����
			else{
				// ������ɫ��������С��
				g.setColor(new Color(230, 230, 80));
				g.fillOval(ballX , ballY , BALL_SIZE, BALL_SIZE);
				// ������ɫ������������
				g.setColor(new Color(80, 80, 200));
				g.fillRect(racketX , RACKET_Y
					, RACKET_WIDTH , RACKET_HEIGHT);
				// ������ɫ������������ֵ
				g.setColor(Color.red);
				g.setFont(LifeFont);
				g.drawString("Life:"+lives, 20,30);
			}
		}
	}
}