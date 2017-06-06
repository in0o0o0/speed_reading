import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.*;


class rapidReading extends JFrame implements ActionListener{
	int  ch, ch1,i; 
	int cpos = 0;//出力用行バッファ linebuf に次の文字を 格納する位置
	int gapp = -1;//linebuf に最後に格納した空白文字の位置 
	final int COLUMNS  =40;//一行に入る最大の数
	final int LINE = 4;//何行作るか
	int []linebuf = new int[COLUMNS];
	int max;//最大で何行あるか
	String [] data= new String[1000];
	StringBuilder sb= new StringBuilder(COLUMNS);
	JLabel [] label = new JLabel[LINE];
	JButton back,play,next;
	ImageIcon pl,st;
	Font f = new Font("",Font.PLAIN, 20);
	Timer timer;
	int t;//タイマ
	int x;
	int y;
	int line=0;//今何行目か
	int count=0;//今何回めの座標の移動か(0~99)
	int cnt=0;//play or stop
	int flag=0;//何かファイルが選ばれているか
	
	
	rapidReading(){
		
		x=3000;
		y=30;//この間隔で座標をずらす
		timer = new Timer(1,this);
		timer.setActionCommand("timer");
		
		pl = new ImageIcon("../img/play.jpg");
		st = new ImageIcon("../img/stop.jpg");
		
		back = new JButton(new ImageIcon("../img/back.jpg"));
		back.setActionCommand("back");
		back.addActionListener(this);
		back.setBounds(100,410,100,100);
		play =new JButton(pl);
		play.setActionCommand("play");
		play.addActionListener(this);
		play.setBounds(200,410,100,100);
		next =new JButton(new ImageIcon("../img/next.jpg"));
		next.setActionCommand("next");
		next.addActionListener(this);
		next.setBounds(300,410,100,100);
		
		
		setLayout(null);
		add(back,0);
		add(play,0);
		add(next,0);
		JMenuBar menubar = new JMenuBar();
	  	JMenu file = new JMenu("File");
	  	JMenu speed = new JMenu("SPEED");


	  	menubar.add(file);
	  	menubar.add(speed);
	    JMenuItem n1 = new JMenuItem("New");
	    n1.addActionListener(this);
	    n1.setActionCommand("new");
	    file.add(n1);
	    
	    JMenuItem []s = new  JMenuItem[6];
	    for(int i=0;i<6;i++){
	    s[i] = new JMenuItem();
	    s[i].addActionListener(this);
	    s[i].setActionCommand(""+i+"");
	    speed.add(s[i],-1);
	    }
	  
	    s[0].setText("120");
	    s[1].setText("150");
	    s[2].setText("180");
	    s[3].setText("240");
	    s[4].setText("300");
	    s[5].setText("360");
	    setJMenuBar(menubar);
	    
	    for(int i=0;i<LINE;i++){
	    	label[i]=new JLabel();
	    	label[i].setBounds(0,i*100,500,100);
	    	label[i].setHorizontalAlignment(JLabel.CENTER);    
	    	label[i].setFont(f);
	    	label[i].setForeground(Color.blue);
	    	label[i].setBackground(Color.WHITE);
	    	label[i].setOpaque(true);
			
	    	add(label[i]);
	    }
		label[1].setText("select a file");
		
		getContentPane().setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500,600);
		setTitle("reading");
		setVisible(true);
	}
	public void changeText(){
		for(int i=0;i<LINE;i++)
		label[i].setText(data[i+line]);
		line++;
		
}
	public void changeTextBack(){
		line-=2;
		for(int i=0;i<LINE;i++)
		label[i].setText(data[i+line]);
		line++;
		
}
	public void changeSpeed(){
		t=count*y;
	}
	public void changeButton(){
		if(cnt==0)
			play.setIcon(st);
		else
			play.setIcon(pl);
		
		cnt =cnt==0?1:0;	
	}
	public void restPoint(){
		  for(int i=0;i<LINE;i++)
			  label[i].setBounds(0,i*100,500,100);
	}
	public void changePoint(){
		
		for(int i=0;i<LINE;i++)
			label[i].setBounds(0,i*100-count*1,500,100);//1だけ上にずらす
		count++;
	}
	
	public void  readFile(String s ){
		
		line=0;
		cpos=0;
		gapp=-1;
		
		for(int i=0;i<LINE-1;i++)
			data[line++]=" ";
		 try{
		      File file = new File(s);
		      FileReader filereader = new FileReader(file);

		      int ch;
		      while((ch = filereader.read()) != -1){
		    	  if(ch == '\n'){
		    		  for(int i=0;i<cpos;i++)
		                   	sb.append((char)linebuf[i]);
		                    	
		                        data[line++] =sb.toString();;
		                    	sb.delete(0,cpos);
		    		 cpos = 0;//次入るのは先頭
		                 gapp = -1;//空白文字は入っていない
		  			  break;
		    	  }else if(ch == ' '){
		    		 if (cpos >= COLUMNS) {//最後の次に空白がきた
		                  
		                    for(int i=0;i<cpos;i++)
		                   	sb.append((char)linebuf[i]);
		                    	
		                        data[line++] =sb.toString();;
		                    	sb.delete(0,cpos);
		                    	
		                   cpos = 0;//出力したので次は先頭から始める
		                     gapp = -1;
		                  }else{
		                   gapp = cpos;//最後に格納した空白文字の位置
		                  
		                   
		                  }
		    		 linebuf[cpos++]= ch;
		                   
		  		}else{
		  			if (cpos >= COLUMNS) {//次に入れる場所が最後の次の時
		  			
		  				  for(int i=0;i<=gapp;i++)
			                   	sb.append((char)linebuf[i]);
			                    	
			                  data[line++] =sb.toString();;
			                  sb.delete(0,cpos);
			                    	
		  					cpos = 0;//次は先頭に戻る
		  					int tmp=gapp+1;
		  				 while (tmp < COLUMNS) {
		                             linebuf[cpos++] = linebuf[tmp++];//残りを先頭に移す
		  				  }	  		
		  				   gapp = -1;//空白文字がまだ入っていない状態
		              }			 
		  			  linebuf[cpos++] = ch;
		  		 }
		    	 
		  	  }
		  	  //残っている文字を出力する
		          if (cpos > 0) {
		        	  for(int i=0;i<cpos;i++)
		        		  sb.append((char)linebuf[i]);
		        	  
		        	  data[line++] =sb.toString();;
	                  sb.delete(0,cpos);
	                  
		        }
		      filereader.close();
		    }catch(FileNotFoundException e){
		      System.out.println(e);
		    }catch(IOException e){
		      System.out.println(e);
		    }
		 
		 for(int i=0;i<4;i++)
				data[line++]=" ";	
		 	
		 max=line-4;		 		
		 line=0;
		 label[1].setText("");
		t=0;
		flag=1;
		
	}
public static void main(String args[]){
	 new rapidReading();
  }

public void actionPerformed(ActionEvent e) {
	
	String es=e.getActionCommand();
	
	if(es.equals("timer")){
		if(t%x==0){//秒に一回文字をずらす
			if(line<=max){
				changeText();
				restPoint();
			
			}else {
				flag=0;//ファイルが選択されていない状態
				timer.stop();
				changeButton();
				restPoint();
				label[1].setText("select a file");
			}
			count=0;
		}else if(t%y==0){//座標をずらす
			changePoint();
		}
		t++;
	}else if(es.equals("new")){
	JFileChooser filechooser = new JFileChooser("/Users/akihito/Documents/English/reading/SpeedReading");
    int selected = filechooser.showOpenDialog(this);
    
    if (selected == JFileChooser.APPROVE_OPTION){
      File file = filechooser.getSelectedFile();
      String path = file.getAbsolutePath();//絶対パスを取得
      
     int length=(int)file.length();
 
      readFile(path);
    }
	}else if(es.equals("back")){
		if(line<=1);
		else
		changeTextBack();
	}else if(es.equals("play")){
		if(flag==0);
		else{
		if(cnt==0){
			
			timer.start();
		}else
			timer.stop();		
		changeButton();
		}
		
	}else if(es.equals("next")){
		if(line>max);
		else
			changeText();		
	}else{ 
		if(es.equals("0"))
			x=3000;	
		else if(es.equals("1"))
			x=2500;	
		else if(es.equals("2"))
			x=2000;		
		else if(es.equals("3"))
			x=1500;		
		else if(es.equals("4"))
			x=1200;
		else if(es.equals("5"))
			x=1000;
		
		y=x/100;
		changeSpeed();
	}
    	
}
}

