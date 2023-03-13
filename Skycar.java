import java.awt.*;  
import java.awt.event.*;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.io.*;
import java.net.*;

import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.*;
import org.json.simple.parser.*;


public class Skycar extends Frame implements ActionListener,WindowListener
{
	static Button[][] b;
	static Frame f1;static Panel p1,p2;
	static int r,c;
	static TextArea LogDisp;
	TextField setterminaltxt;
	static Button clrlog,placebox,placeblock,placeterminal,startplacing,execute,calculate,left,right,rawexecute,reset,resetall;
	static Button seqplacedestination,JSONREAD,JSONWRITE;
	static int action,changeexist,atleastanyagentchange;
	static box[] box; static int bx,ax,grip;String message,message1;static Profile[] Profile;
	//int[][] winchdown,winchup;static int[][] profilex,profiley;
	static dest[] dest,prevdest,dest2,dest3; static int dt;
	static dest[] Invdest;
	static double I = 500000;
	static double I1 = 50000;
	static comMatrix mm,block,around,PURANK,DORANK,continous;
	static Random rand; 
	static seqbx[][] seqbx;
	static CTseqbx[][] CTseqbx;
	int seqbxindex;double fact,ExecutionTime;
	static int marker,minx,miny,maxx,maxy;
	static Continuous C;
	
	public static void main(String[] args) throws IOException
	{
		Skycar s=new Skycar();
		s.start();
	}
	public void start()throws IOException
	{
		rand = new Random();
		LogDisp=new TextArea();
		setterminaltxt=new TextField();
		
		
		placebox=new Button();
		placebox.setLabel("placebox");
		placebox.addActionListener(this);
		
		placeblock=new Button();
		placeblock.setLabel("placeblock");
		placeblock.addActionListener(this);
		
		placeterminal=new Button();
		placeterminal.setLabel("setDestination");
		placeterminal.addActionListener(this);
		
		startplacing=new Button();
		startplacing.setLabel("startplacing");
		startplacing.addActionListener(this);
		
		calculate=new Button();
		calculate.setLabel("Calculate Path");
		calculate.addActionListener(this);
		
		execute=new Button();
		execute.setLabel("execute");
		execute.addActionListener(this);
		
		rawexecute=new Button();
		rawexecute.setLabel("rawexecute");
		rawexecute.addActionListener(this);
		
		
		seqplacedestination=new Button();
		seqplacedestination.setLabel("SeqPlacedestination");
		seqplacedestination.addActionListener(this);
		
		JSONREAD=new Button();
		JSONREAD.setLabel("JSONREAD");
		JSONREAD.addActionListener(this);
		
		JSONWRITE=new Button();
		JSONWRITE.setLabel("JSONWRITE");
		JSONWRITE.addActionListener(this);
		
		left=new Button();
		left.setLabel("<-");
		left.addActionListener(this);
		
		right=new Button();
		right.setLabel("->");
		right.addActionListener(this);
		
		
		clrlog=new Button();
		clrlog.setLabel("Clear log");
		clrlog.addActionListener(this);
		
		reset=new Button();
		reset.setLabel("Reset");
		reset.addActionListener(this);
		
		resetall=new Button();
		resetall.setLabel("Reset All");
		resetall.addActionListener(this);
		
		f1=new Frame();
		p1=new Panel();
		p2=new Panel();
		p1.setBounds(10,30,800,800);
		p2.setBounds(1200,50,220,500); 
		p2.setBackground(Color.GRAY);
		
		LogDisp.setBounds(1201,51,219,400);
		
		clrlog.setBounds(1260,460,100,30);
		
		placebox.setBounds(900,30,100,30);
		placeblock.setBounds(900,150,100,30);
		placeterminal.setBounds(900,90,100,30);
		setterminaltxt.setBounds(1020,90,100,30);
		
		seqplacedestination.setBounds(1020,30,150,30);
		JSONREAD.setBounds(1020,210,150,30);
		JSONWRITE.setBounds(1020,270,150,30);
		
		
		
		startplacing.setBounds(900,210,100,30);
		calculate.setBounds(900,270,100,30);
		
		left.setBounds(900,330,40,30);
		right.setBounds(960,330,40,30);
		
		execute.setBounds(900,390,100,30);
		rawexecute.setBounds(900,450,100,30);
		
		reset.setBounds(900,510,100,30);
		resetall.setBounds(900,570,100,30);
		
		
		f1.add(LogDisp);
		f1.add(clrlog);
		f1.add(placebox);
		f1.add(placeblock);
		f1.add(placeterminal);
		f1.add(startplacing);
		f1.add(calculate);
		f1.add(execute);
		f1.add(rawexecute);
		f1.add(right);
		f1.add(left);
		f1.add(reset);
		f1.add(resetall);
		f1.add(setterminaltxt);
		f1.add(seqplacedestination);
		f1.add(JSONREAD);
		f1.add(JSONWRITE);
	    
		
	
		//demo push
		
		Scanner sc=new Scanner(System.in);
		//r = sc.nextInt();
		//c = sc.nextInt();
		r = 51;
		c = 51;	
		
		mm = new comMatrix(r,c);
		block = new comMatrix(r,c);
		PURANK = new comMatrix(r,c);
		DORANK = new comMatrix(r,c);
		continous = new comMatrix(r,c);
		
		for(int i=0;i<=r+1;i++)
		{
			for(int j=0;j<=c+1;j++)
			{
				if(i==0||j==0||i==r+1||j==c+1)
				{
					block.m[i][j]=2;
					mm.m[i][j] = I;
				}
				else
				{
					block.m[i][j]=0;
					mm.m[i][j] = I1;
					
					PURANK.m[i][j]=1;     // z axis
					DORANK.m[i][j]=1;
				}
			}
		}
		
		
		GridLayout gl=new GridLayout(r+3,c+2);
		p1.setLayout(gl);
	  
		f1.addWindowListener(this);
		
		b = new Button[100][100];
		box = new box[100];  bx = 0; ax = 0;
		Profile = new Profile[100];
		dest = new dest[100]; dt = 0;
		prevdest = new dest[100];
		dest2 = new dest[100];
		dest3 = new dest[100];
		seqbx = new seqbx[50001][101];
		CTseqbx = new CTseqbx[50001][101];
		
		for(int i=0;i<=50000;i++)
		{
			for(int j=1;j<=100;j++)
			{
				seqbx[i][j] = new seqbx();       //descrete time seqbx
			}
		}
		
		for(int i=0;i<=4000;i++)
		{
			for(int j=1;j<=100;j++)
			{
				CTseqbx[i][j] = new CTseqbx();     //continous time seqbx
			}
		}
		
		
		
		
		for(int i=1;i<100;i++)
		{
			box[i] = new box();
			Profile[i] = new Profile();
			dest[i] = new dest(r,c);
			dest2[i] = new dest(r,c);
			dest3[i] = new dest(r,c);
			
			dest[i].havedest=0;
		
		}
		
		
		C = new Continuous(r,c);
		
		
		for(int i=0;i<=r+1;i++)
		{
			for(int j=0;j<=c+1;j++)
			{
				if(i==0||j==0||i==r+1||j==c+1)
				{
					b[i][j]=new Button("S");
					b[i][j].setBackground(Color.gray);
					b[i][j].setName(i+" "+j+" ");
				    p1.add(b[i][j]);
					b[i][j].addActionListener(this);
				}
				else
				{
				b[i][j]=new Button(" ");
				b[i][j].setBackground(Color.WHITE);
				b[i][j].setName(i+" "+j+" ");
				p1.add(b[i][j]);
				b[i][j].addActionListener(this);
				}
			}
		}
		
		
	     f1.add(p1);	
	     f1.add(p2);
	 
	     f1.setSize(1350,650);
	     f1.setLayout(null);  
	     f1.setVisible(true);
	     
	    
	
	     // tempprofilex and tempprofiley  *10 but it should be *100
	     
	     
	     int[][] tempprofilex = {
	    		 {0},
	    		 {0,533},
	    		 {0,264,435},
	    		 {0,228,159,423},
	    		 {0,213,137,135,429},
	    		 {0,199,121,117,113,423},
	    		 {0,189,102,103,103,105,442},
	    		 {0,185,91,101,86,99,86,501},
	    		 {0,186,88,83,82,84,83,93,454},
	    		 {0,196,70,78,91,64,73,88,87,590},
	    		 {0,182,82,67,73,73,73,58,64,93,545},
	    		 {0,184,65,74,54,70,70,62,58,74,98,533},
	    		 {0,181,68,52,60,61,56,61,63,51,72,94,469},
	    		 {0,329,73,46,53,53,115,60,35,53,47,95,83,560},
	    		 {0,194,61,60,37,57,48,52,52,56,51,59,73,94,574},
	    		 {0,190,53,50,52,43,51,49,49,51,52,52,58,77,95,501},
	    		 {0,192,67,51,48,49,46,40,39,50,38,48,53,67,69,93,565},
	    		 {0,183,70,44,49,45,46,42,45,39,41,41,45,50,65,66,91,529},
	    		 {0,178,69,51,41,42,39,40,38,40,36,43,40,49,58,57,74,99,567},
	    		 {0,185,59,50,40,43,40,42,38,39,37,41,40,44,49,57,64,73,101,746},
	    		 {0,186,67,57,36,35,30,40,37,38,44,34,39,46,47,47,45,57,71,94,593},
	    		 {0,221,40,52,41,40,32,42,37,32,42,62,23,31,40,79,50,34,37,95,87,928},
	    		 {0,184,66,47,46,28,35,35,36,29,34,35,36,38,37,40,37,46,50,48,72,98,552},
	    		 {0,180,68,43,41,41,38,56,32,20,22,50,31,17,41,52,29,44,46,53,79,57,99,659},

   };
	   
	     int[][] tempprofiley = {
	    		 {0},
	    		 {0,642},
	    		 {0,325,503},
	    		 {0,277,181,483},
	    		 {0,255,150,150,491},
	    		 {0,247,128,129,133,493},
	    		 {0,237,112,109,114,117,508},
	    		 {0,244,95,92,97,98,112,530},
	    		 {0,217,91,90,85,95,77,113,538},
	    		 {0,229,80,82,89,77,82,80,111,552},
	    		 {0,225,80,68,75,78,63,76,82,107,580},
	    		 {0,228,76,62,75,65,65,68,79,85,110,606},
	    		 {0,238,77,64,60,64,61,65,63,74,81,109,660},
	    		 {0,260,66,50,67,49,60,63,78,55,65,77,105,661},
	    		 {0,235,64,57,55,56,51,57,55,50,66,76,89,111,648},
	    		 {0,230,75,68,44,52,58,44,53,47,57,62,77,84,109,642},
	    		 {0,192,67,51,48,49,46,40,39,50,38,48,53,67,69,93,565},
	    		 {0,188,77,53,42,65,50,36,34,44,56,51,49,60,82,83,110,649},
	    		 {0,228,83,54,47,44,42,45,54,41,38,51,44,61,60,93,70,107,659},

	     };
	     
	     
	     fact=1.0;    // x13 ss
	     
	  // fact *10 
	     for(int i = 1;i<tempprofilex.length;i++)
	     {
	    	 for(int j=1;j<tempprofilex[i].length;j++)
	    	 {
	    		 tempprofilex[i][j] = (int) (tempprofilex[i][j]*fact);
	    	 }
	     }
	     // fact *10   
	     for(int i = 1;i<tempprofiley.length;i++)
	     {
	    	 for(int j=1;j<tempprofiley[i].length;j++)
	    	 {
	    		 tempprofiley[i][j] = (int) (tempprofiley[i][j]*fact);
	    	 }
	     }
	     
	     
	        
	     
	     //profilex = tempprofilex.clone();
	     //profiley = tempprofiley.clone();
	     
	         

	     
	     // winch  *10 but it should be *100
	     
	     int[][] tempwinch = {
	    		 {0},
	    		 {0,0},
	    		 {0,0,1386},
	    		 {0,0,0,1426},
	    		 {0,0,0,0,1820},
	    		 {0,0,0,0,0,1811},
	    		 {0,0,0,0,0,0,1803},
	    		 {0,0,0,0,0,0,0,2044},
	    		 {0,0,0,0,0,0,0,0,2150},
	    		 {0,0,0,0,0,0,0,0,0,2166},
	    		 {0,0,0,0,0,0,0,0,0,0,3157},
	    		 {0,0,0,0,0,0,0,0,0,0,0,2968},    		 
	     };
	     
	     
	     // fact *10  
	     for(int i = 2;i<tempwinch.length;i++)
	     {
	    	
	    	 tempwinch[i][i] = (int) ((fact*tempwinch[i][i])-(2*i));
	    	
	     }
	     
	     
	     
	     
	     
	     //winchdown = tempwinch.clone();
	     //winchup =   tempwinch.clone();
	     
	     
	     double grip1 = 1.4;
	     grip = (int)(grip1*fact*100.0);
	     
	     
	      
	      
	      
	      
	      
	      
	     
	    /* int port1 = 6662;
         int port2 = 6663;
         ServerSocket serverSocket = new ServerSocket(port1);
         Socket clientSocket1;Socket clientSocket2;*/
         int aaa;
         
	     while(1==1)
	        {
	    	
	         /*System.out.println("waiting a connection in port "+port1+"\n") ;
	        
	         clientSocket1 = serverSocket.accept();
	        
	         InputStream request = clientSocket1.getInputStream();
	         DataInputStream in = new DataInputStream(request);
	         String message = new String(in.readLine());
	         System.out.println("Message received: "+message);
	         clientSocket1.close();
	     */
	    	 int exception =0;
	    	 message = sc.next();
	    	 
	    	 try
	    	 {
	         JSONREAD(message);
		     calculatepath();
	    	 }
	    	 catch(Exception e)
	    	 {
	    		 System.out.println("RunTimeError Exception occureddd");
	    		e.printStackTrace();
	    		 exception = 1;
	    		 System.out.println("resetttt????????");
		         
		         aaa= sc.nextInt();
		         if(aaa==1)
		         {
		        	 resetall();
		         }
		         
	    	 }
		     
		     
		    /* CTWrite();*/
		     
		     
		     /*message1 = JSONWRITE();*/		     
		     	     		     		     
	         //message = message.replace("\"", "");

/*
	         clientSocket2 = new Socket("localhost", port2);
	         DataOutputStream dout = new DataOutputStream(clientSocket2.getOutputStream());
			 dout.writeUTF(message);
			 dout.flush();  
	         dout.close();  
	         clientSocket2.close();*/
	    	 
	      if(exception==0)
	      {
	    	  /*CTJSONWRITE();*/
	    	  
	         System.out.println("resetttt????????");
	         
	         aaa= sc.nextInt();
	         if(aaa==1)
	         {
	        	 resetall();
	         }
	         
	      }
	        }
             
	}
	
	public void resetall()
	{

		for(int i=1;i<=r;i++)
		{
			for(int j=1;j<=c;j++)
			{
			b[i][j].setLabel(" ");
			b[i][j].setBackground(Color.WHITE);
			block.m[i][j]=0;
			mm.m[i][j]=I1;
			
			PURANK.m[i][j]=1;     // z axis
			DORANK.m[i][j]=1;
			}
		}
		minx=0;maxx=0;miny=0;maxy=0;
		for(int i=1;i<100;i++)
		{
			box[i] = null;
			box[i] = new box();
			Profile[i] = null;
			Profile[i] = new Profile();
			
			dest[i] = new dest(r,c);
			dest2[i] = new dest(r,c);
			dest3[i] = new dest(r,c);
			
			dest[i].havedest=0;
		
		}
		
		C = null;
		C = new Continuous(r,c);
		
		
	}
	public void JSONREAD(String stringToParse)
	{
      
		JSONParser parser = new JSONParser();
	      try {
	    	 JSONObject temp;
	    	 JSONObject temp1;
	    	 Map<String, String> map = new HashMap<String, String>();
	      	 
	            String sumaaaa = "D:/Math Projects Java/MAPF"+stringToParse+".json";
		    	Object obj = parser.parse(new FileReader(sumaaaa));
		    	JSONObject jsonObject = (JSONObject)obj;
		    	 
		    	 
		       // JSONObject jsonObject = (JSONObject) parser.parse(stringToParse);  
		    	
		    	 JSONObject jsonObjectMAPD = (JSONObject) jsonObject.get("mapd");
		    	
	         Collection  subObject = jsonObjectMAPD.values();
	         Object[] sub1 =  subObject.toArray();
	         
	         int size = subObject.size();
	         int i = 0;
	         
	         Object[] key = jsonObjectMAPD.keySet().toArray();
	          
	    
	         int startx=0,starty=0,startz=0,pickx=0,picky=0,pickz=0,dropx=0,dropy=0,dropz=0;
	         int purank,dorank;bx = 0;ax=0;
	         while(i<size)
	         {
	        	
	          temp = (JSONObject)sub1[i];
	          String jobtype = temp.get("job")+"";
	          
	          
	          Collection ctmp = (Collection) temp.get("start");
	          Object[] str =  ctmp.toArray();
	     
	          startx = Integer.parseInt(str[0]+"");
	          starty = Integer.parseInt(str[1]+"");
	          startz = Integer.parseInt(str[2]+"");
	          
	          startx++; starty++; //+1 because json has 0 0 axis also for x and y
	          
	          ctmp = (Collection) temp.get("pickup");
	          str =  ctmp.toArray();
	          
	          
	          if(!((str[0]+"").equals("null")||(str[1]+"").toString().equals("null")))
	          {		  
	          pickx = Integer.parseInt(str[0]+"");
	          picky = Integer.parseInt(str[1]+"");
	          pickz = Integer.parseInt(str[2]+"");
	          
	          pickx++; picky++;   //+1 because json has 0 0 axis also for x and y
	          }
	          else if((str[0]+"").equals("null")||(str[1]+"").equals("null"))
	          {
	        	  pickx = 999999;
		          picky = 999999;
		          pickz = Integer.parseInt(str[2]+"");
		             
	          }
	          
	          ctmp = (Collection) temp.get("dropoff");
	          str =  ctmp.toArray();
	          
	          if(!((str[0]+"").equals("null")||(str[1]+"").toString().equals("null")))
	          {		  
	          dropx = Integer.parseInt(str[0]+"");
	          dropy = Integer.parseInt(str[1]+"");
	          dropz = Integer.parseInt(str[2]+"");
	          
	          dropx++; dropy++;   //+1 because json has 0 0 axis also for x and y
	          }
	          else if((str[0]+"").equals("null")||(str[1]+"").equals("null"))
	          {
	        	  dropx = 999999;
		          dropy = 999999;
		          dropz = Integer.parseInt(str[2]+"");
	          }
	          
	         bx++; 
	         box[bx].id = Integer.parseInt(key[i]+"");
	         box[bx].x = startx;    box[bx].startx = startx;
	         box[bx].y = starty;    box[bx].starty = starty;
	         
	         
	         purank = Integer.parseInt(temp.get("PU_rank")+"");
	         dorank = Integer.parseInt(temp.get("DO_rank")+"");
	                 
	         if(pickx!=999999)
	         {
	        	    box[bx].stacklen++; 
	        	    box[bx].stackdestx[box[bx].stacklen] = pickx;    
	        	    box[bx].stackdesty[box[bx].stacklen] = picky;
	        	    box[bx].rank[box[bx].stacklen] = purank;
	        	    box[bx].pickupordrop[box[bx].stacklen] = 1;
	        	    box[bx].pickz = pickz;
	    	    	    
	         }
	         if(dropx!=999999)
	         {
	        	    box[bx].stacklen++;
	        	    box[bx].stackdestx[box[bx].stacklen] = dropx;
	        	    box[bx].stackdesty[box[bx].stacklen] = dropy;
	        	    box[bx].rank[box[bx].stacklen] = dorank;
	        	    box[bx].pickupordrop[box[bx].stacklen] = 2;
	        	    box[bx].dropz = dropz;
	        	       
	         } 
	         
	         if(box[bx].stacklen>0)
	         {
	        	    dest[bx].x = box[bx].stackdestx[1];
					dest[bx].y = box[bx].stackdesty[1];
				    dest[bx].havedest=1;
				    if(b[dest[bx].x][dest[bx].y].getLabel().toString().equals(" "))
					{
						b[dest[bx].x][dest[bx].y].setLabel(box[bx].id+"'");
					}
				    dest[bx].dispx = box[bx].stackdestx[1];
					dest[bx].dispy = box[bx].stackdesty[1];
					
					box[bx].stacklennow = 1;
				    
	         }
	         b[startx][starty].setLabel(box[bx].id+"");
			 b[startx][starty].setBackground(Color.YELLOW);
	          
	         box[bx].z = 0; 
	         
	         
	         box[bx].jobtype = jobtype;
	         
	         if(!box[bx].jobtype.equals("T"+"")){box[bx].jobtype="I"+"";}
	         
	         box[bx].grip = grip;
	      
	         
	          
	         i++;
	         }
	         

	         
	         JSONObject jsonObjectCONFIG = (JSONObject) jsonObject.get("config");
	         
	         
	         minx = Integer.parseInt(jsonObjectCONFIG.get("MIN_X")+"")+1;
	         maxx = Integer.parseInt(jsonObjectCONFIG.get("MAX_X")+"")+1;
	         miny = Integer.parseInt(jsonObjectCONFIG.get("MIN_Y")+"")+1;
	         maxy = Integer.parseInt(jsonObjectCONFIG.get("MAX_Y")+"")+1;
	         
	       
	         for(i=1;i<=r;i++)
	         {
	        	 for(int j=1;j<=c;j++)
	        	 {
	        		 if(i<=minx-1||j<=miny-1||i>=maxx+1||j>=maxy+1)
	        		 {
	        			block.m[i][j] = 2;
	        			mm.m[i][j] = I;
	        			 
	        		 }
	        		 
	        		 
	        	 }
	         }
	         
	         
	         
	         
	         
	         
	         
	         Collection  subObjectOBSTACLES = (Collection) jsonObject.get("obstacles");
	         Object[] sub1OBSTACLES =  subObjectOBSTACLES.toArray();
	         int sizeOBSTACLES = subObjectOBSTACLES.size();
	         i = 0;
	        	 
	         System.out.println("sizeOBSTACLES "+sizeOBSTACLES);
	         
	         while(i<sizeOBSTACLES)
	         {
	        	 String str = (String) sub1OBSTACLES[i];
	        	 String str1="",str2="";
	        	 
	        	 int index = 0;
	        	 
	        	 for(int j=0;str.charAt(j)!=',';j++)
	        	 {
	        		 str1=str1+str.charAt(j);
	        		 index = j;
	        	 }
	        	 
	        	 index = index+2;
	        	 
	        	 for(int j=index;j<str.length();j++)
	        	 {
	        		 str2=str2+str.charAt(j);
	        	 }
	        	 		 
				 block.m[Integer.parseInt(str1+"")+1][Integer.parseInt(str2+"")+1] = 2;
     			 mm.m[Integer.parseInt(str1+"")+1][Integer.parseInt(str2+"")+1] = I;
     			 
     			 b[Integer.parseInt(str1+"")+1][Integer.parseInt(str2+"")+1].setBackground(Color.BLACK);
	        	 	 
				 i++;
	         }
	         
	        
	         
	         Collection  subObjectMAINTENANCE = (Collection) jsonObject.get("maintenance_inactive_node");
	         Object[] sub1MAINTENANCE =  subObjectMAINTENANCE.toArray();
	         int sizeMAINTENANCE = subObjectMAINTENANCE.size();
	         i = 0;
	         System.out.println("sizeMAINTENANCE "+sizeMAINTENANCE);
	         
	         
	         while(i<sizeMAINTENANCE)
	         {
	        	 
	        	 String str = (String)sub1MAINTENANCE[i];
	        	 String str1="",str2="";
	        	 
	        	 int index = 0;
	        	 
	        	 for(int j=0;str.charAt(j)!=',';j++)
	        	 {
	        		 str1=str1+str.charAt(j);
	        		 index = j;
	        	 }
	        	 
	        	 index = index+2;
	        	 
	        	 for(int j=index;j<str.length();j++)
	        	 {
	        		 str2=str2+str.charAt(j);
	        	 }
	        	 
	        	 b[Integer.parseInt(str1+"")+1][Integer.parseInt(str2+"")+1].setLabel("M"+"");                           
				 b[Integer.parseInt(str1+"")+1][Integer.parseInt(str2+"")+1].setBackground(Color.gray);
				 
				 i++;
	         }
	         
	         
	         Collection  subObjectENTANGLED = (Collection)jsonObject.get("entangled_skycars");
	         Object[] sub1ENTANGLED =  subObjectENTANGLED.toArray();
	         int sizeENTANGLED = subObjectENTANGLED.size();
	         i = 0;
	         
	         while(i<sizeENTANGLED)
	         {
	        	 String str = sub1ENTANGLED[i].toString();
	        	 int agent = Integer.parseInt(str+"");
	        	 
	        	 for(int k=1;k<=bx;k++)
	        	 {
	        		if(box[k].id==agent)
	        		{
	        			box[k].Entangled=1;
	        		}
	        	 }
	        	 i++;
	         }
	         
	         
	         JSONObject jsonObjectDELAY = (JSONObject) jsonObject.get("delays");
	         Collection  subObjectDELAY = jsonObjectDELAY.values();
	         Object[] sub1DELAY =  subObjectDELAY.toArray();
	         int sizeDELAY = subObjectDELAY.size();
	         i = 0;
	         
	         
	         Object[] delaykey = jsonObjectDELAY.keySet().toArray();
	         
	         while(i<sizeDELAY)
	         {
	        	
	        	   int delay = (int)(Double.parseDouble(sub1DELAY[i].toString())*100*fact);
	        	   
	    
	        	   
	        	   for(int k=1;k<=bx;k++)
	        	   {
	        		   if(box[k].id==Integer.parseInt(delaykey[i]+""))
	        		   {
	        			   box[k].tempwait = delay;
	        			   box[k].delay = delay;
	        		   }
	        		   
	        	   }
	        	   
	        	 i++;
	         }
	         //no code change
	         
	         JSONObject jsonObjectSCS = (JSONObject) jsonObject.get("scs_constraints");
	         Collection  subObjectSCS = jsonObjectSCS.values();
	         Object[] sub1SCS =  subObjectSCS.toArray();
	         int sizeSCS = subObjectSCS.size();
	         i = 0;
	         
	         Object[] SCSkey = jsonObjectSCS.keySet().toArray();
	         
	         
	         while(i<sizeSCS)
	         {
	        	
	        	 int agent  =  Integer.parseInt(SCSkey[i]+"");
	        	 
	        	 temp = (JSONObject)sub1SCS[i];
	        	 
	        	 Collection  tempc1 = temp.values();
		         Object[] tempc2 =  tempc1.toArray();
		         int sizetempc1 = tempc1.size();
		         
		         int j=0;
		         
		         Object[] tempSCSkey = temp.keySet().toArray();
		         
		         if(sizetempc1>=1)
		         {
		         ax++;
		       	 box[bx+ax].id = agent;
		         }
		       	 
		         while(j<sizetempc1)
		         {
		        	 
		        	 String str = (String)tempSCSkey[j];
		        	 int x,y;
		        	 
		        	 String str1="",str2="";
		        	 
		        	 int index = 0;
		        	 
		        	 for(int l=0;str.charAt(l)!=',';l++)
		        	 {
		        		 str1=str1+str.charAt(l);
		        		 index = l;
		        	 }
		        	 
		        	 index = index+2;
		        	 
		        	 for(int l=index;l<str.length();l++)
		        	 {
		        		 str2=str2+str.charAt(l);
		        	 }
		        	 
		        	 
		        	 x = Integer.parseInt(str1+"");
		        	 y = Integer.parseInt(str2+"");
		        	 x++;y++;
		        	 
		        	
		        	 
		        	 Collection  globaltime = (Collection)temp.get(str);
			         Object[] globaltimee =  globaltime.toArray();
		        	 
			         double globalstart,clearance;
			         
			         globalstart = Double.parseDouble(globaltimee[0]+"");
			         clearance = Double.parseDouble(globaltimee[1]+"");
		        	 
		        	 
			         clearance = clearance-globalstart;
			         clearance = clearance*100*fact;
			         
			         
		        	C.cc[bx+ax][x][y].timein = 0;
		        	C.cc[bx+ax][x][y].timeout = (int) clearance;
		        	
		      	 
		        	 j++;
		         }
		        
	        	 i++;
	         }
	         
            //read Profile data
	         
	         JSONObject jsonObjectProfile = (JSONObject) jsonObject.get("Profile");
	         Collection  subObjectProfile = jsonObjectProfile.values();
	         Object[] sub1Proflie =  subObjectProfile.toArray();
	         int sizeProfile = subObjectProfile.size();
	         i = 0;
	         
	         
	         
	         Object[] Profilekey = jsonObjectProfile.keySet().toArray();
	         
	         
	         while(i<sizeProfile)
	         {
	        	 int agent  =  Integer.parseInt(Profilekey[i]+"");
	        	 int agentno = 0;
	        	 
	        	 for(int p=1;p<=bx;p++) {if(box[p].id==agent){agentno = p;}}
	        	 
	        	 temp = (JSONObject)sub1Proflie[i];
	        	
	        	 
	        	 
	        	 JSONObject tempx = (JSONObject)temp.get("x");
	        	 Collection ctmpx = tempx.values();
	        	 Object[] subx = ctmpx.toArray();
	        	 
	        	 int totsizex = ctmpx.size();
	        	 int sizex = 0;
	        	 
	        	 Profile[agentno].profilex = new int[totsizex+1][];
	        	 Profile[agentno].profilex[0] = new int[1];
	        	 
	        	 while(sizex<totsizex)
	        	 {	 
	        		 Collection ctmpxx = (Collection)subx[sizex];
	        		 Object[] subxx = ctmpxx.toArray();
	        		 
	        		 int roundval = 0;
	        		 
	        	
	        		 Profile[agentno].profilex[subxx.length] = new int[subxx.length+1];
	        		 
	        		 for(int p=0;p<subxx.length;p++)
	        		 {
	        			 roundval= (int)(100.0*Double.parseDouble(subxx[p]+""));
			        	 Profile[agentno].profilex[subxx.length][p+1] = roundval; 	 
	        		 }	 
	        		 
	        		 sizex++;
	        	 }
	        	  Profile[agentno].xlen = subx.length;    
	        	 
	        	 
	        	 
	        	 JSONObject tempy = (JSONObject)temp.get("y");
	        	 Collection ctmpy = tempy.values();
	        	 Object[] suby = ctmpy.toArray();
	        	 
	        	 int totsizey = ctmpy.size();
	        	 int sizey = 0;
	        	 
	        	 Profile[agentno].profiley = new int[totsizey+1][];
	        	 Profile[agentno].profiley[0] = new int[1];
	        	 
	        	 
	        	 while(sizey<totsizey)
	        	 {	 
	        		 Collection ctmpyy = (Collection)suby[sizey];
	        		 Object[] subyy = ctmpyy.toArray();
	        		 
	        		 int roundval = 0;
	        		 
	        		 Profile[agentno].profiley[subyy.length] = new int[subyy.length+1];
	        		 
	        		 
	        		 for(int p=0;p<subyy.length;p++)
	        		 {
	        			 roundval= (int)(100.0*Double.parseDouble(subyy[p]+""));
			        	 Profile[agentno].profiley[subyy.length][p+1] = roundval; 	 
	        		 }	 
	        		 
	        		 sizey++;
	        	 }
	        	 Profile[agentno].ylen = suby.length;
	        	 
	        	 
	        	 
	        	 JSONObject tempcb = (JSONObject)temp.get("cb");
	        	 Collection ctmpcb = tempx.values();
	        	 Object[] subcb = ctmpx.toArray();
	        	 
	        	 int totsizecb = ctmpx.size();
	        	 int sizecb = 0;
	        	 
	        	 Profile[agentno].profilecb = new int[totsizecb+1][];
	        	 Profile[agentno].profilecb[0] = new int[1];
	        	 
	        	 while(sizecb<totsizecb)
	        	 {	 
	        		 Collection ctmpcbcb = (Collection)subcb[sizecb];
	        		 Object[] subcbcb = ctmpcbcb.toArray();
	        		 
	        		 int roundval = 0;
	        		 
	        		 Profile[agentno].profilecb[subcbcb.length] = new int[subcbcb.length+1];
	        		 
	        		 
	        		 for(int p=0;p<subcbcb.length;p++)
	        		 {
	        			 roundval= (int)(100.0*Double.parseDouble(subcbcb[p]+""));
			        	 Profile[agentno].profilecb[subcbcb.length][p+1] = roundval; 	 
	        		 }	 
	        		 
	        		 sizecb++;
	        	 }
	        	 Profile[agentno].cblen = subcb.length;		                 
		             
	        	 i++;
	         }
	         
	         
	         
	         
	         
	       
	         
	      } catch(Exception e) {
	         e.printStackTrace();
	      }
	}
	
	public void CTJSONWRITE() throws IOException
	{
		
		JSONObject JSONObj = new JSONObject();
		Object obj;
		
		double makesspan = 0;
		double sumofcost = 0;
		int nos = 0;
		int consecutivemovement=0;
		
		
		for(int k = 1;k<=bx;k++)
		{
			if(box[k].cost>makesspan){makesspan = box[k].cost;}
			sumofcost = sumofcost+box[k].cost;
			consecutivemovement = consecutivemovement+box[k].PCM;
			nos = nos+box[k].NOS;		
		}
		
		
		
		obj = makesspan;
		JSONObj.put("MakeSpan", obj);
		
		obj = sumofcost;
		JSONObj.put("SumofCost", obj);
		
		obj = nos;
		JSONObj.put("Number Of Steps", obj);
		
		obj = consecutivemovement;
		JSONObj.put("Total Consecutive movement of all agents", obj);
		
		obj = ExecutionTime;
		JSONObj.put("ExecutionTime", obj);
		
		
		
		ArrayList<Integer>location=new ArrayList<Integer>();
		
		ArrayList<Object>list=new ArrayList<Object>();
		ArrayList<Object> List=new ArrayList<Object>();
		ArrayList<Object> Master=new ArrayList<Object>();
		
		
		JSONObject tempJSONObj = new JSONObject();
		
		String LogWrite = "";
		String axis = "";
		String dir = "";
		
		for(int k=1;k<=bx;k++)
		{
			
			LogWrite = LogWrite+"\nSkycar-"+box[k].id+"\n";
				
			for(int j=1;j<=box[k].continousindex;j++)
			{
				
				location.add(CTseqbx[j][k].x1-1);
				location.add(CTseqbx[j][k].y1-1);
				List.add(location.clone());
				location.clear();
				
				location.add(CTseqbx[j][k].x2-1);
				location.add(CTseqbx[j][k].y2-1);
				List.add(location.clone());
				location.clear();
				
				
				List.add(CTseqbx[j][k].t1);
			
				List.add(CTseqbx[j][k].t2);
				
				List.add(CTseqbx[j][k].p);
		
				List.add(CTseqbx[j][k].d);
					
				Master.add(List.clone());
				List.clear();
				
				if(j==1)
				{
				    axis = "O"; dir = "O";
					LogWrite = LogWrite+axis+" "+dir+" "+"("+(CTseqbx[1][k].x1-1)+","+(CTseqbx[1][k].y1-1)+")"+"\n";		
				}
				
				if(CTseqbx[j][k].x2!=CTseqbx[j][k].x1)
				{
					axis = "x";
					if(CTseqbx[j][k].x2>CTseqbx[j][k].x1){dir = "f";}
					if(CTseqbx[j][k].x2<CTseqbx[j][k].x1){dir = "b";}
				}
				if(CTseqbx[j][k].y2!=CTseqbx[j][k].y1)
				{
					axis = "y";
					if(CTseqbx[j][k].y2>CTseqbx[j][k].y1){dir = "f";}
					if(CTseqbx[j][k].y2<CTseqbx[j][k].y1){dir = "b";}		
				}
				if(CTseqbx[j][k].x2==CTseqbx[j][k].x1 && CTseqbx[j][k].y2==CTseqbx[j][k].y1)
				{
					axis = "O";
					dir  = "O";
				}
				
				
				LogWrite = LogWrite+axis+" "+dir+" "+"("+(CTseqbx[j][k].x2-1)+","+(CTseqbx[j][k].y2-1)+")"+"    "+"["+CTseqbx[j][k].t1+", "+CTseqbx[j][k].t2+", "+CTseqbx[j][k].p+", "+CTseqbx[j][k].d+"]"+"\n";
					
			}
			
			tempJSONObj.put(box[k].id+"",Master.clone());
			Master.clear();
									
		}
		
		JSONObj.put("Solution",tempJSONObj);
		
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss");	
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());	
		String Logfilename = sdf1.format(timestamp).toString();
		Logfilename = "LogFile-"+Logfilename;
	
		
		FileWriter file = new FileWriter("D:\\Test cases output\\"+Logfilename+".txt");
		
		file.write(message+"\n");
        file.write(LogWrite);
        file.close();
		
       
	}
	

	
	
	public int headache(int k)
	{
		int headache=0;	
		
		if(box[k].pickupordrop[box[k].stacklennow]==1)
		{
			if(PURANK.m[dest[k].x][dest[k].y]==box[k].rank[box[k].stacklennow]){headache=1;}
		}
		if(box[k].pickupordrop[box[k].stacklennow]==2)
		{
			if(DORANK.m[dest[k].x][dest[k].y]==box[k].rank[box[k].stacklennow]){headache=1;}
		}
		if((box[k].stacklennow>box[k].stacklen)||box[k].stacklen==0)
		{
			headache=1;
		}
			
		return headache;
	}
	
	public void calculatepath()
	{

		 long start1 = System.currentTimeMillis();
		
		
		
		for(int i=0;i<=r+1;i++)
		{
			for(int j=0;j<=c+1;j++)
			{
					
				if(b[i][j].getLabel().toString().equals("B")||b[i][j].getLabel().toString().equals("S")||b[i][j].getLabel().toString().equals("M"))
				 {
					mm.m[i][j] = I;
					block.m[i][j]=2;
				 }
				
			}
		}
		
		
		 
		 for(int k=1;k<=bx;k++)
		 {
		 dest[k].destdisplayclr = Color.WHITE;	 
		 }
		 
		
			
		for(int k=1;k<=bx;k++)
		{
			 if(dest[k].havedest==1)
			 {
			  for(int i=1;i<=box[k].stacklen;i++)
			  {  
				  block.m[box[k].stackdestx[i]][box[k].stackdesty[i]]=1;
				  
			  }
			 }
			 if(dest[k].havedest==0)
			 {
				 box[k].isblock=1;
	             mm.m[box[k].x][box[k].y] = I;
	             block.m[box[k].x][box[k].y]=2; 
			 }
			 
		}
		
		
		
		for(int k=1;k<=bx;k++)
		{
		     
            if(dest[k].havedest==0)
            {
            	int s=0,s1;
            		
				s1 = checkanyotherdestination(k);
				if(s1==1){s = 0;}
                if(s1==0){s = noblocksurrounddest(box[k].x,box[k].y);}
                if(box[k].Entangled==1){s=1;}
                 
            	if(s==1)
            	{
            		
            	 subcalculatedestiationMatrix(k);
            	 box[k].isblock=1;
            	 
            	 mm.m[box[k].x][box[k].y] = I;
            	 block.m[box[k].x][box[k].y]=2;
            	 
            	 dest[k].havedest=0;
            	 System.out.println("Agentblock "+box[k].id);
            	}
            	if(s==0)
            	{
                    
            		findnewdestinationforK(k);
            		dest[k].havedest=1;
            		box[k].stacklennow++;
            		
            		box[k].isblock=0;
                    mm.m[box[k].x][box[k].y] = I1;
            		System.out.println("AgentNOTblock "+box[k].id);
            		
            		
            	}
            	
            }
            
		}	
		
		//temporily making travel jobs as block and will make unblock after internal jobs completed
		for(int k=1;k<=bx;k++)
		{
			if(box[k].jobtype.equals("T"+"")){ mm.m[box[k].x][box[k].y] = I;block.m[box[k].x][box[k].y] = 2;}
		}
			
		
		calculatedestiationMatrix(bx);
		
		for(int k=1;k<=bx;k++)
		{
			for(int i=0;i<=r+1;i++)
			{
				for(int j=0;j<=c+1;j++)
				{
					dest2[k].D.m[i][j] = dest[k].D.m[i][j];
				}
			}
			changeDest2Matrixunused(k);
			assignDest2MatrixtoDest3Matrix(k);
		}
		
		
		
		
		for(int k=1;k<=bx;k++)
		 {
			 if(dest[k].havedest==1)
			 {
				 if(box[k].x==box[k].stackdestx[1]&&box[k].y==box[k].stackdesty[1])
				 {
					 if(headache(k)==0){ 
						 
						 goawayasofnow(k);
						 subcalculatedestiationMatrix(k);
						 for(int i=0;i<=r+1;i++)
							{
								for(int j=0;j<=c+1;j++)
								{
									dest2[k].D.m[i][j] = dest[k].D.m[i][j];
								}
							}
						 changeDest2Matrixunused(k);
						 assignDest2MatrixtoDest3Matrix(k);
						 
						 
					 }
					 
					
				 }
			 }
			 
			 
		 }
		 
		 
		// if agent 'k' has 'k2' in its destination and agent 'k1' destination is 'k' and if 'delay of k2' - 'delay of k1' is greater than delaylimit	 
		
				for(int k=1;k<=bx;k++)
				{
					for(int k1=1;k1<=bx;k1++)
					{
						if(k!=k1)
						{
							if(dest[k1].x==box[k].startx&&dest[k1].y==box[k].starty)
							{
								for(int k2=1;k2<=bx;k2++)
								{
									if(k2!=k&&k2!=k1)
									{
										if(box[k2].startx==dest[k].x&&box[k2].starty==dest[k].y)
										{
										  int delay2 = box[k2].delay;
										  int delay1 = box[k1].delay;
										  //delaylimit default 25 seconds
										  int delaylimit = 25*100;
										  
										  if((delay2-delay1)>delaylimit)
										  {		
										  goawayasofnow(k);
										  subcalculatedestiationMatrix(k);
										  for(int i=0;i<=r+1;i++){for(int j=0;j<=c+1;j++){dest2[k].D.m[i][j] = dest[k].D.m[i][j];}}
										  changeDest2Matrixunused(k);
										  assignDest2MatrixtoDest3Matrix(k);
											
										  }
											
										}
									}
								}
							}
						}
					}
				}
				
				 
		
		double val=1;double set=0;
		
		for(int i=1;i<=bx;i++)
		{
			
			val=val*box[i].isblock;
			box[i].nxtx=box[i].x;
			box[i].nxty=box[i].y;
			box[i].curentcellvcal=dest3[i].D.m[box[i].x][box[i].y];
			box[i].prvcurentcellvcal=box[i].curentcellvcal;
			
		}
		String tmp1;
		
	
		int[] findnextposition = new int [bx+1];
		int extratime=0;
		
		
		seqbxindex=0;
		
		for(int k=1;k<=bx;k++)
		{

			seqbx[seqbxindex][k].x = box[k].x;
		    seqbx[seqbxindex][k].y = box[k].y;
		    seqbx[seqbxindex][k].z = box[k].z;
		    
		    box[k].individualtimeseq = seqbxindex;
		    
		    
		    writeccdata(k,box[k].x,box[k].y,0,(int)I);
		    
		}
		
		
		
		String jobtype = "I";
		
		
		
		while(val!=1)
		{		
            
			
			
			val=1;
			changeexist=0;
			seqbxindex++;
			        
			for(int k=1;k<=bx;k++)
			{
					
				if(box[k].isblock!=1&&box[k].jobtype.equals(jobtype)){box[k].WAIT = box[k].wait+box[k].tempwait+box[k].winchwait;}
				
				if(box[k].isblock!=1&&box[k].jobtype.equals(jobtype)&&box[k].WAIT==0)
				{
				changeDest2Matrixunused(k);
				assignDest2MatrixtoDest3Matrix(k);
				
				}
			
			}

			
			
			for(int k=1;k<=bx;k++)
			{
			 if(box[k].isblock!=1&&box[k].jobtype.equals(jobtype)&&box[k].WAIT==0)
			 { 
				 if(box[k].reacheddestination==1)
				 {
					 if(box[k].stackdestdumy[box[k].stacklennow]==0){doreacheddest(k);}
					 else if(box[k].stackdestdumy[box[k].stacklennow]==1)
					 {
						 System.out.println("ingayaaaa?dddd");
						 box[k].continousindex--;
						 box[k].reacheddestination=0;
						 box[k].stacklennow++;
						 dest[k].x=  box[k].stackdestx[box[k].stacklennow];
						 dest[k].y=  box[k].stackdesty[box[k].stacklennow];
						 subcalculatedestiationMatrix(k);
						 
					 }	 
				 }
				 else if(box[k].reacheddestination==0)
				 {
					 findnextposition[k] = prefindnextposition(k);
					 if(findnextposition[k]==1)
						{
						
						nextwait(k); 
						 
						InstantCTWritet1(k,seqbxindex-1);//write continous time 1
						findnextposition[k] = 0;
						box[k].NOS++;
					    box[k].prvx = box[k].x;   box[k].prvy = box[k].y;	
					    box[k].actualprvx = box[k].x;   box[k].actualprvy = box[k].y;	  //actual previous x and y should not be changed in middle of program run
					    box[k].prvcurentcellvcal=dest[k].D.m[box[k].x][box[k].y];
						    	
						
				        }		 
					 caldeflectionlimit(k);
				 }
			     	 
			 }
				
			 else if(box[k].isblock!=1&&box[k].jobtype.equals(jobtype)&&box[k].WAIT!=0){ reducewait(k); }
			 
			 seqbx[seqbxindex][k].x = box[k].x;
			 seqbx[seqbxindex][k].y = box[k].y;
			 seqbx[seqbxindex][k].z = box[k].z;
			 
			 
			 
			if(box[k].jobtype.equals(jobtype)){ val=val*box[k].isblock;}
			 
				
			}
		
			
			
		   // if(seqbxindex>=80000) {val = 1;}
			
			
			if(val==1&&jobtype=="I")
			   {

				   for(int k=1;k<=bx;k++)
				   {
					   if(box[k].jobtype.equals("T")){val = 100; jobtype = "T";}
					   if(box[k].jobtype.equals("T")){mm.m[box[k].x][box[k].y] = I1;block.m[box[k].x][box[k].y] = 0;changeexist=1;}
				   }
				   
				   for(int i=1;i<=r;i++)
				   {
					   for(int j=1;j<=c;j++)
					   { 
						   if(b[i][j].getLabel().toString().equals("M"))
						   {
							   block.m[i][j]=0;
							   mm.m[i][j] = I1;
							   b[i][j].setLabel("");                           
							   b[i][j].setBackground(Color.WHITE);
						
						   }
						  	   
					   }
					   
				   }
				   
			  }
			  			
			
		if(changeexist==1)
		{	
			calculatedestiationMatrix(bx);
			
			
			for(int k=1;k<=bx;k++)
			{
				for(int i=1;i<=r;i++)
				{
					for(int j=1;j<=c;j++)
					{
						dest2[k].D.m[i][j] = dest[k].D.m[i][j];
					}
				}
			}
			
			
			
			for(int k=1;k<=bx;k++)
			{
				box[k].curentcellvcal = dest[k].D.m[box[k].x][box[k].y];
				
				if((int)box[k].curentcellvcal!=(int)box[k].prvcurentcellvcal)
				{
					box[k].prvx=0;box[k].prvy=0;
					
					//System.out.println("apooooooooooooom");
				}			
			}
			
			
		}
		
		
		
		
					/*Thread.sleep(5);
			        continoodraw();*/
		
		}
		
		
		
		
		marker=1;
		
		long start2 = System.currentTimeMillis();
		start2= start2-extratime;
		double start3 = (start2-start1);
		
		
		start3 = start3/1000;
		ExecutionTime = start3;
		
		LogDisp.setText("Execution Time :  "+ ExecutionTime +"secs");
		

		double sumofcost = 0;double consecutivemovement=0;double nos=0;double makesspan=0;
		for(int k = 1;k<=bx;k++)
		{
			if(box[k].cost>makesspan){makesspan = box[k].cost;}
			consecutivemovement = consecutivemovement+box[k].PCM;
			sumofcost = sumofcost+box[k].cost;
			nos = nos+box[k].NOS;
			
		}
		
		System.out.println("sumofcost :  "+ sumofcost+"secs");
		System.out.println("makesspan :  "+ makesspan+"secs");
		System.out.println("consecutivemovement :  "+ consecutivemovement);
		System.out.println("no of steps :"+ nos);
		
		System.out.println("seqbxindex   ......."+seqbxindex);
		
	}
	
	public void writevalidposition(int k,int forb,int nxtx,int nxty,int reqdist)
	{
		
		int dirx=0,diry=0;String axis="";
		
		if(nxtx==box[k].x){axis = "y";}
		if(nxty==box[k].y){axis = "x";}
		if(axis.equals("x")){if(nxtx>box[k].x){dirx = 1;  diry = 0;}else{dirx = -1; diry = 0;}}
		if(axis.equals("y")){if(nxty>box[k].y){dirx = 0;  diry = 1;}else{dirx = 0;  diry = -1;}}
		
		
		
		
		if(axis.equals("x")){box[k].speedlist = Profile[k].profilex[reqdist].clone();}
		if(axis.equals("y")){box[k].speedlist = Profile[k].profiley[reqdist].clone();}
		box[k].waitindex = 0;
		writeseqccdata(k,dirx,diry,reqdist,axis);		
				
		if(reqdist>1){box[k].determined = 1;}
		box[k].PCM++;
		
		
		
	}
	
	public int getvalidposition(int k,int forb,int nxtx,int nxty)
	{
	
			
			int dirx=0,diry=0;String axis="";int maxdist,reqdist;
			
			if(nxtx==box[k].x){axis = "y";}
			if(nxty==box[k].y){axis = "x";}
			if(axis.equals("x")){if(nxtx>box[k].x){dirx = 1;  diry = 0;}else{dirx = -1; diry = 0;}}
			if(axis.equals("y")){if(nxty>box[k].y){dirx = 0;  diry = 1;}else{dirx = 0;  diry = -1;}}
					
		
			if(forb == -1)                //agent moves backward
			{
				maxdist = 1;
				reqdist = predictREQDist(k,dirx,diry,axis,maxdist);
				
				return reqdist;
				
			}
			
			
			
			// agent moves forward.....
			
				
			maxdist = predictMaxDist(k,dirx,diry,axis);	
			reqdist = predictREQDist(k,dirx,diry,axis,maxdist);
			
			
			
			return reqdist;
		
	}
		
	//lamda1() - return 0 if there is agent'y' with startx and starty at destination(x,y)(for agent 'x') and atleast 1 spacearound for that agent 'y'
		//lamda() returns 0 to restrict the possibility for agent to move
		
	public int lamda1(int x,int y,int k)
	{
		for(int k1=1;k1<=bx;k1++){if(k1!=k){if(box[k1].x==x&&box[k1].y==y){if((box[k1].startx==x&&box[k1].starty==y)&&checkspacearound(box[k1].x,box[k1].y,k1)>=1){return 0;}}}}
		return 1;	
	}
		
		
	public int prefindnextposition(int k)
	{
		int f=0;
	
		if(dest3[k].D.m[box[k].x][box[k].y]>5||dest3[k].D.m[box[k].x][box[k].y]==1)
		{
			
		 f = findnextposition1(k);		
		 
		}
		
		if(dest3[k].D.m[box[k].x][box[k].y]<=5&&dest3[k].D.m[box[k].x][box[k].y]!=1)   // checking from 2 to 3 "3 may increase in future"
		{
        int headache=0;	
		
        headache = headache(k);
        
        boolean H;
        H = (headache==1)&&(lamda1(dest[k].x,dest[k].y,k)==1||checkanyotherdestination(k)==1);
        
		if(H)
		{ 
			if(mm.m[box[k].x][box[k].y]==I)
			{
				block.m[box[k].x][box[k].y] = 0;
				mm.m[box[k].x][box[k].y]=I1;
				changeexist = 1;
			}
				
			f = findnextposition1(k);	
		}
		if(!H)
		{ 
			if(mm.m[box[k].x][box[k].y]!=I)
			{
				block.m[box[k].x][box[k].y] = 2;
				mm.m[box[k].x][box[k].y]=I;
				changeexist = 1;
			}
			
			f = 0;
			
		}
		
		}
		
		if(f==1)
		{
			if(dest3[k].D.m[box[k].nxtx][box[k].nxty]<dest3[k].D.m[box[k].x][box[k].y])
			{
				if(box[k].f==0)
				{
				box[k].pb = box[k].b;
				box[k].b = 0;
				}
				box[k].f++;
				
			}
			if(dest3[k].D.m[box[k].nxtx][box[k].nxty]>dest3[k].D.m[box[k].x][box[k].y])
			{
				if(box[k].b==0)
				{
				box[k].pf = box[k].f;
				box[k].f = 0;
				}
				box[k].b++;
			}
			
			
		}
		
		
		if(f==0){box[k].tempwait=300;}
		
		
		return f;
		
	}
		
	public int findnextposition1(int k)
	{
		
		
		if(box[k].determined==1)         //no need to find new node since it is already determined from continuous time step i have copied the below code inside if loop from one of below code(a[i]>0.5) 
		{ 
			box[] tbox = new box[5];
	        double time;
	        
			for(int i=0;i<=4;i++)	{tbox[i] = new box();}
			
			tbox[0].x = box[k].x;   tbox[0].y = box[k].y;
			tbox[1].x = box[k].x+1; tbox[1].y = box[k].y;
			tbox[2].x = box[k].x-1; tbox[2].y = box[k].y;
			tbox[3].x = box[k].x;   tbox[3].y = box[k].y+1;
			tbox[4].x = box[k].x;   tbox[4].y = box[k].y-1;
			
			
			
			for(int i=1;i<=4;i++)
			{
				if(C.cc[k][tbox[i].x][tbox[i].y].isagentpresent==1)
				{
					box[k].nxtx=tbox[i].x;box[k].nxty=tbox[i].y;
					time = time(box[k].x,box[k].y,k);
					box[k].prvtime=time;
					box[k].prvtimeCagnt=box[k].curtimeCagnt;
					return 1;
				}
			}
			
		}
		
		if(box[k].determined==0)
		{
			int f;
		
			f = findnextposition(k);
			
		
			
			
			return f;
			
		}
		return 0;
		
		
	}
				
	public int findnextposition(int k)
	{		
		
		
		if(dest3[k].D.m[box[k].x][box[k].y]==1){destmatrixeq1check(k);return 0;}
		if(dest3[k].D.m[box[k].x][box[k].y]==I1){return 0;}    // put waitindex to 1 if return 0 bcoz skcar stops there so speed start from first
		
		

		box[] tbox = new box[5];
		box tbox2 = new box();
		double[] ctime = new double[5];
		double[]a =new double[5];
		double a2;
		double time;int vali=0;
			
		
		for(int i=1;i<=4;i++){a[i]=0;}
		for(int i=0;i<=4;i++)
		{
			tbox[i] = new box();
			ctime[i] = -500;
		}
		
		tbox[0].x = box[k].x;   tbox[0].y = box[k].y;
		tbox[1].x = box[k].x+1; tbox[1].y = box[k].y;
		tbox[2].x = box[k].x-1; tbox[2].y = box[k].y;
		tbox[3].x = box[k].x;   tbox[3].y = box[k].y+1;
		tbox[4].x = box[k].x;   tbox[4].y = box[k].y-1;
		

		
		
			for(int i=1;i<=4;i++)
			{

				if(dest3[k].D.m[tbox[i].x][tbox[i].y]==dest3[k].D.m[box[k].x][box[k].y]-1)
				{
					a[i]=0.5;
					
					if(box[k].actualprvx==tbox[i].x||box[k].actualprvy==tbox[i].y){a[i]=1;}

			    }
				
			}
					
			for(int i=1;i<4;i++)
			{
				for(int j=i+1;j<=4;j++)
				{
					if(a[i]<a[j])
					{
						tbox2.x = tbox[j].x;   tbox2.y = tbox[j].y;
						tbox[j].x = tbox[i].x; tbox[j].y = tbox[i].y;
						tbox[i].x = tbox2.x;    tbox[i].y = tbox2.y;
						
						a2   = a[j];
						a[j] = a[i];
						a[i] = a2;
						
					}
				}
			}
			
			int nonxtagent=0;
			
			for(int i=4;i>=1;i--)
			{
				if(a[i]>=0.5)
				{

					
					if(dest3[k].D.m[tbox[i].x][tbox[i].y]!=I&&(anyotheragent(tbox[i].x,tbox[i].y,k)==0))
					{

					if(calculateispossible(tbox[i].x,tbox[i].y,k)==1)
					{
						
						vali=1;
						int tt = getvalidposition(k,1,tbox[i].x,tbox[i].y);
						if(tt>=1)
						{

						box[k].nxtx=tbox[i].x;box[k].nxty=tbox[i].y;
						time = time(box[k].x,box[k].y,k);
						box[k].prvtime=time;
						box[k].prvtimeCagnt=box[k].curtimeCagnt;
						
						writevalidposition(k,1,tbox[i].x,tbox[i].y,tt);
						
						return 1;
						}		
						
					}
					}
					if(nonxtagent==0)
					{
						nonxtagent=nonxtagent(tbox[i].x,tbox[i].y,k);
					}
					
				}
			}
			
			
			if(nonxtagent==1){ return 0;}
			
			
			if(vali==1){return 0;}
			
            int limit = 1;
			
			//for loop control if run time error occurs due to this loop control increase pb>= limit below
			if(box[k].pb==box[k].f&&box[k].pb>=limit&&checkspacearound(box[k].x,box[k].y,k)>=2)
			{
				return 0;
			}
			

			// move backward to different axis than the forward axis
			int j=1;
			int forwardx = 0,forwardy = 0;
			for(int i=1;i<=4;i++){if(a[i]>=0.5){forwardx = tbox[i].x;forwardy = tbox[i].y;}}
			int backwardx,backwardy;
			
			
			for(int i=1;i<=4;i++)
			{
				if(a[i]==0)
				{
					if(dest3[k].D.m[tbox[i].x][tbox[i].y]!=I&&(anyotheragent(tbox[i].x,tbox[i].y,k)==0))
					{
						if(checkprvtimeconflict(tbox[i].x,tbox[i].y,k)!=1)
						{
						ctime[i]=time(tbox[i].x,tbox[i].y,k);
						
						backwardx = tbox[i].x;backwardy = tbox[i].y;
						if(forwardx!=backwardx&&forwardy!=backwardy){j = i;}
						
						}
						
					}
				}
			}
			
			
			for(int i=1;i<=4;i++)
			{
				
				if(ctime[i]>ctime[j]){j=i;}
				
				
			}
			
			if(checkspacearound(tbox[0].x,tbox[0].y,k)<3)
			{
				ctime[0]=time(box[k].x,box[k].y,k);
				
				
			}
			
		
			int tt = getvalidposition(k,-1,tbox[j].x,tbox[j].y);
			
			if(ctime[j]>0&&(ctime[j]>ctime[0])&&(tt>=1))
			{
				box[k].nxtx=tbox[j].x;box[k].nxty=tbox[j].y;
				time = time(box[k].x,box[k].y,k);
				box[k].prvtime=time;
				box[k].prvtimeCagnt=box[k].curtimeCagnt;
				
				writevalidposition(k,-1,tbox[j].x,tbox[j].y,tt);
				
				return 1;
				
				
			}
			
		
		
	
		return 0;
		
		}
	
	public int calculateispossible(int x,int y,int k)
	{
		int a,temp;
		if(dest3[k].D.m[x][y]==1)
		{
			return 1;
		}
		
		if(checkprvtimeconflict(x,y,k)==1)
		{
			return 0;
		}
		
		temp = 0;
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
				if(dest3[k1].D.m[x][y]!=I1)
				{
					temp=1;
				}
			}
		}
		
		if(temp==0){return 1;}
		
		double m1= dest3[k].D.m[box[k].x][box[k].y]-1;
		
        while(m1>1)
			{
        	a=0;
				for(int i=1;i<=r;i++)
				{
					for(int j=1;j<=c;j++)
					{
						if(dest3[k].D.m[i][j]==m1)
						{
						
							if(anyotheragent1(i,j,x,y,k)==0)
							{
								a=1;
								if(checkifspacesurround2(i,j,k)==1)
								{	
									   return 1;
								}
								
							}
							
						}
											
					}
				}
				
				
			if(a==1){m1--;}
			if(a==0){return 0;}
				
				
				}
			
        if(m1==1)
        {
        	return 1;
        }
        
        
       return 0;
	}
	
	public int anyotheragent(int x,int y,int k)
	{
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
			if((box[k1].x==x&&box[k1].y==y)||(box[k1].nxtx==x&&box[k1].nxty==y))
			{
				return 1;
					
			}
			}
		}
		
		return 0;
	}
		
	public void doreacheddest(int k)
	{

		 if(box[k].pickupordrop[box[k].stacklennow]==1)//pickup
		{
			if(box[k].winchwait==0)
			{	
			box[k].startwork++;
			
			if(box[k].startwork<=box[k].pickz)
			{
				box[k].z = box[k].z+1; 
				box[k].winchwait = Profile[k].profilecb[box[k].pickz][box[k].winchwaitindex];
				box[k].winchwaitindex++;
			}
			if(box[k].startwork==box[k].pickz+1)
			{
				box[k].z = box[k].z;
				/*box[k].winchwait = box[k].grip   since using profile winch no seperate grip wait as of now*/
				box[k].winchwaitindex = 1;
				
			}
			if(box[k].startwork>box[k].pickz+1&&box[k].startwork<(2*box[k].pickz+1))
			{
				box[k].z = box[k].z-1;
				box[k].winchwait = Profile[k].profilecb[box[k].pickz][box[k].winchwaitindex];
				box[k].winchwaitindex++;
				
			}
			
			if(box[k].startwork>=(2*box[k].pickz+1))
			{
				box[k].z = box[k].z-1;
				box[k].reacheddestination=0;
				box[k].startwork=0;
				//PURANK.m[box[k].x][box[k].y] = PURANK.m[box[k].x][box[k].y]+1;
				box[k].prvnodehasPorDjob = 1;
				box[k].winchwaitindex = 1;
				
				
				
				if(box[k].stacklennow==box[k].stacklen)
				{
					box[k].stacklennow++;
				}
				
				if(box[k].stacklennow<box[k].stacklen)
				{
					box[k].stacklennow++;
					dest[k].x=  box[k].stackdestx[box[k].stacklennow];
					dest[k].y=  box[k].stackdesty[box[k].stacklennow];	 
					subcalculatedestiationMatrix(k);  
				}
				
				
				
				InstantCTWritet2(k,seqbxindex); //write continous time 2
			}
			
			}
			
		}
		if(box[k].pickupordrop[box[k].stacklennow]==2)//dropdown
		{
			if(box[k].winchwait==0)
			{
			box[k].startwork++;
		
			if(box[k].startwork<=box[k].dropz)
			{
				box[k].z = box[k].z+1;
				box[k].winchwait = Profile[k].profilecb[box[k].dropz][box[k].winchwaitindex];
				box[k].winchwaitindex++;
				
			}
			if(box[k].startwork==box[k].dropz+1)
			{
				box[k].z = box[k].z;
				/*box[k].winchwait = box[k].grip   since using profile winch no seperate grip wait as of now*/
				box[k].winchwaitindex = 1;
			}
			if(box[k].startwork>box[k].dropz+1&&box[k].startwork<(2*box[k].dropz+1))
			{
				box[k].z = box[k].z-1;
				box[k].winchwait = Profile[k].profilecb[box[k].dropz][box[k].winchwaitindex];
				box[k].winchwaitindex++;
				
			}
			
			
			if(box[k].startwork>=(2*box[k].dropz+1))
			{
				box[k].z = box[k].z-1;
				box[k].reacheddestination=0;
				box[k].startwork=0;
				//DORANK.m[box[k].x][box[k].y] = DORANK.m[box[k].x][box[k].y]+1;
				box[k].prvnodehasPorDjob = 2;
				box[k].winchwaitindex = 1;
				
				
				if(box[k].stacklennow==box[k].stacklen)
				{
					box[k].stacklennow++;
				}
				
				if(box[k].stacklennow<box[k].stacklen)
				{
					box[k].stacklennow++;
					dest[k].x=  box[k].stackdestx[box[k].stacklennow];
					dest[k].y=  box[k].stackdesty[box[k].stacklennow];	 
					subcalculatedestiationMatrix(k);  
				}
				
			    InstantCTWritet2(k,seqbxindex);  //write continous time 2
			    
			   
			}
				
			
		}
		
		}
		
		
	}
	
	public void destmatrixeq1check(int k)
	{

		if(dest3[k].D.m[box[k].x][box[k].y]==1)
		{
			if((box[k].stacklennow<=box[k].stacklen)&&(box[k].stacklen>0)&&!(box[k].jobtype.equals("T"))){box[k].reacheddestination=1;InstantCTWritet1(k,seqbxindex-1);}//write continous time 2
			box[k].prvx=0;
			box[k].prvy=0;
			
	
				
		  if(box[k].stacklennow==box[k].stacklen+1||box[k].jobtype.equals("T"))
			{
			  
			int s=0,s1;int centerpath=0;
			
			centerpath = checkcenterpath1(k);
			if(centerpath == 1)
			{
				s = 0;
			}
			
			if(centerpath == 0)
			{
			s1 = checkanyotherdestination(k);
			if(s1==1){s = 0;}
            if(s1==0)
            {
            	//s = noblocksurrounddest(box[k].x,box[k].y);
            	int thread = threadforotherterminal(box[k].x,box[k].y);
            	if(thread==1){
            		System.out.println("above threadterminal by this agent "+box[k].id);
            		s=0;
            		
            	}
            	if(thread==0){s=1;}
    
            }
			}
            
            if(box[k].jobtype.equals("T")){s=1;}
           
        	if(s==1)
        	{
   		
        	box[k].isblock=1;

        	System.out.println("Agentblockk "+box[k].id);
        	box[k].individualtimeseq = seqbxindex;
        	box[k].cost = seqbxindex/100;
        
        	changeexist=1;
        	dest[k].havedest=0;
        	 
        	mm.m[box[k].x][box[k].y] = I;
        	block.m[box[k].x][box[k].y]=2;
        	     	 
        	}
        	if(s==0)
        	{
        	
        		findnewdestinationforK(k);
        		subcalculatedestiationMatrix(k);
        		
        	}
        		
	       	}
		  
		}
		
		

	}
	
	public int anyotheragent1(int x,int y,int x1,int y1,int k)
	{
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
			if((box[k1].nxtx==x&&box[k1].nxty==y))
			{
				if(dest3[k1].D.m[x1][y1]!=I1)
				{
				return 1;
				}
			}
			}
		}
		
		return 0;
	}
	public int nonxtagent(int x,int y,int k)
	{
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
				if(box[k1].x==x&&box[k1].y==y)
				{
				if(box[k1].nxtx!=x||box[k1].nxty!=y)
				{
					return 1;
				}
			    }
			}
		}
		return 0;
	}
		
    public int checkifspace(int x,int y,int k)
    {	
    	for(int k1=1;k1<=bx;k1++)
    	{
    		if(k1!=k)
    		{
    			if(dest3[k1].D.m[x][y]!=I1)
    			{
    				return 0;
    			}
    		}
    	}
    	
    	return 1;
    }
    
    public double time(int x,int y,int k)
    {
     	double InfTime=5000000;
		
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
			
					if(dest3[k1].D.m[box[k1].x][box[k1].y]!=1)
					{
						//included dest3[k1].D.m[x][y]!=0 because dest matrix is not assigned initial and default it is assigned static 0
						if(dest3[k1].D.m[x][y]!=I1&&dest3[k1].D.m[x][y]!=0&&dest3[k].D.m[box[k1].x][box[k1].y]!=I1)
						{
						
						if((dest3[k1].D.m[box[k1].x][box[k1].y]-dest3[k1].D.m[x][y])<InfTime)
						{
							InfTime = dest3[k1].D.m[box[k1].x][box[k1].y]-dest3[k1].D.m[x][y];
							box[k].curtimeCagnt = k1;
						}
						
						}
					}
					}
		}
		
		return InfTime;
    }
           
    public int checkifspacesurround2(int x,int y,int k)
    {
    	box[] tbox = new box[5];
    	int[]a =new int[10001];
    	for(int i=1;i<=4;i++){tbox[i] = new box();a[i]=0;}
		tbox[1].x = x+1; tbox[1].y = y;
		tbox[2].x = x-1; tbox[2].y = y;
		tbox[3].x = x;   tbox[3].y = y+1;
		tbox[4].x = x;   tbox[4].y = y-1;
    	
    	for(int i=1;i<=4;i++)
    	{
    		if(dest[k].D.m[tbox[i].x][tbox[i].y]!=I)
    		{
    			if(anyotheragent(tbox[i].x,tbox[i].y,k)==0)
    		    {
    			a[(int) dest[k].D.m[tbox[i].x][tbox[i].y]]++;
    		    }
    			
    		}
    	}
    	for(int i=1;i<=4;i++)
    	{
    		if(dest[k].D.m[tbox[i].x][tbox[i].y]!=I)
    		{
    		if(a[(int) dest[k].D.m[tbox[i].x][tbox[i].y]]>=2)
    		{
    			return 1;
    		}
    		}
    	}
    	
    	return 0;
    }
        
    public int checkprvtimeconflict(int x,int y,int k)
    {
    	double time;
    	if(x==box[k].prvx&&y==box[k].prvy)
    	{
    		time=timeto(x,y,box[k].prvtimeCagnt);
    		if(time<=box[k].prvtime)
    		{
    			return 1;
    		}
    	}
    	
    	return 0;
    }
	   
    public double timeto(int x,int y,int k)
    {
    	double time=5000000;

    	if(k!=0)
    	{
    	if(dest3[k].D.m[x][y]!=I1)
    	{
    	time = dest3[k].D.m[box[k].x][box[k].y]-dest3[k].D.m[x][y];
    	}
    	}
    	return time;
    }
	public void assignDest2MatrixtoDest3Matrix(int k)
	{
		
		for(int i=0;i<=r+1;i++)
		{
			for(int j=0;j<=c+1;j++)
			{
				dest3[k].D.m[i][j]=dest2[k].D.m[i][j];
			}
		}
		
	}
	
	public void changeDest2Matrixunused(int k)
	{
		double val;
			 val = dest[k].D.m[box[k].x][box[k].y];
			 
			 for(int i=1;i<=r;i++)
			 {
				 for(int j=1;j<=c;j++)
				 {
					
					 
					 if(dest[k].D.m[i][j]>=val&&(i!=box[k].x||j!=box[k].y)&&dest[k].D.m[i][j]!=I)
					 {
						 dest2[k].D.m[i][j] = I1; 
						
					 }
							
					 else
					 {
						 dest2[k].D.m[i][j] = dest[k].D.m[i][j];
					 }
			     }
			 }
			 for(double m=val-1;m>=2;m--)
			 {
				 
				 for(int i=1;i<=r;i++)
				 {
					 for(int j=1;j<=c;j++)
					 {
						 
						 if(dest2[k].D.m[i][j]==m)
						 {
						 if(checkneighbor4Dest2(k,m,i,j)==0)
						 {
							 dest2[k].D.m[i][j] = I1;
						 }
						 }
							 
					 }
				 }		 
						 
			 }
	
		
	}
	
	public int checkneighbor4Dest2(int k,double m,int x,int y)
	{
		
		if(dest2[k].D.m[x+1][y]==m+1){return 1;}
		if(dest2[k].D.m[x-1][y]==m+1){return 1;}
		if(dest2[k].D.m[x][y+1]==m+1){return 1;}
		if(dest2[k].D.m[x][y-1]==m+1){return 1;}
		
		return 0;
		
	}
	public int checkneighbor4Dest3(int k,double m,int x,int y)
	{
		
		if(dest3[k].D.m[x+1][y]==m+1){return 1;}
		if(dest3[k].D.m[x-1][y]==m+1){return 1;}
		if(dest3[k].D.m[x][y+1]==m+1){return 1;}
		if(dest3[k].D.m[x][y-1]==m+1){return 1;}
		
		return 0;
		
	}
	public int checkspacearound(int x,int y,int k)
	{
		box[] tbox = new box[5];
    	int a=0;
    	for(int i=1;i<=4;i++){tbox[i] = new box();}
		tbox[1].x = x+1; tbox[1].y = y;
		tbox[2].x = x-1; tbox[2].y = y;
		tbox[3].x = x;   tbox[3].y = y+1;
		tbox[4].x = x;   tbox[4].y = y-1;
		
		for(int i=1;i<=4;i++)
		{
			if(dest3[k].D.m[tbox[i].x][tbox[i].y]!=I)
			{
			if(anyotheragent(tbox[i].x,tbox[i].y,k)==0)
			{
				a++;
			}
			}
		}
		return a;
		
	}
	public int checkcenterpath1(int k)
	{
		
		int centerx,centery;
		
		centerx = (minx+maxx)/2;
		centery = (miny+maxy)/2;
		
		if(box[k].x==centerx||box[k].y==centery){return 1;}
		
		
		
		return 0;
	}
	
	public int checkanyotherdestination(int k)
	{
		
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k)
			{
				for(int i=box[k1].stacklennow;i<=box[k1].stacklen;i++)
				{
				      
				      if(box[k].x==box[k1].stackdestx[i]&&box[k].y==box[k1].stackdesty[i]){return 1;}
				}
			
				//if(dest[k1].x==box[k].x&&dest[k1].y==box[k].y){return 1;}//added this line because new destination may come for other agents after they finished all the jobs
			}
			
			
		}
		
			
		return 0;
	}
	
	public int noblocksurrounddest(int x,int y)
	{
		
		box[] tbox = new box[9];
		
		for(int i=1;i<=8;i++)
		{
			tbox[i] = new box();	
		}
		
		tbox[1].x = x+1;    tbox[1].y = y;
		tbox[2].x = x-1;    tbox[2].y = y;
		tbox[3].x = x;      tbox[3].y = y+1;
		tbox[4].x = x;      tbox[4].y = y-1;
		tbox[5].x = x+1;    tbox[5].y = y+1;
		tbox[6].x = x-1;    tbox[6].y = y+1;
		tbox[7].x = x+1;    tbox[7].y = y-1;
		tbox[8].x = x-1;    tbox[8].y = y-1;
		
		
		for(int i=1;i<=8;i++)
		{
			if(block.m[tbox[i].x][tbox[i].y]==2){return 0;}
		}
		
		
		
		return 1;
		
	}
	
	public int threadforotherterminal(int x,int y)
	{
		int total = 0;
		box[] tbox = new box[5];
		box[] intbox = new box[5];
		
		for(int i=1;i<=4;i++)
		{
			tbox[i] = new box();	
			intbox[i] = new box();
		}
		
		tbox[1].x = x+1;    tbox[1].y = y;
		tbox[2].x = x-1;    tbox[2].y = y;
		tbox[3].x = x;      tbox[3].y = y+1;
		tbox[4].x = x;      tbox[4].y = y-1;
		
		
		
		for(int i=1;i<=4;i++)
		{
			if(block.m[tbox[i].x][tbox[i].y]==1)
			{
				intbox[1].x = tbox[i].x+1;    intbox[1].y = tbox[i].y;
				intbox[2].x = tbox[i].x-1;    intbox[2].y = tbox[i].y;;
				intbox[3].x = tbox[i].x;      intbox[3].y = tbox[i].y+1;
				intbox[4].x = tbox[i].x;      intbox[4].y = tbox[i].y-1;
				
				int count = 0;
				
				for(int j=1;j<=4;j++)
				{
					if(block.m[intbox[j].x][intbox[j].y]>=1)
					{
						count++;
					}
					
				}
				if(count>=4){
					
					System.out.println((tbox[i].x-1)+"  "+(tbox[i].y-1));
					return 1;
					
				}
							
			}
		}
		
		
		
		return 0;
		
	}
	
	
	
	
	
	public void findnewdestinationforK(int k)
	{
		subcalculatedestiationMatrix(k);
		
		int val = 2;
		
		if(checkanyotherdestination(k)==0)
		   {block.m[box[k].x][box[k].y] = 0;}
		else if(checkanyotherdestination(k)==1)
		  {block.m[box[k].x][box[k].y] = 1;}
		
		
		while(1==1)
		{
		for(int i=r;i>=1;i--)
		{
			for(int j=c;j>=1;j--)
			{
				if((block.m[i][j]==0)&&dest[k].D.m[i][j]==val)
				{
					if(noblocksurrounddest(i,j)==1&&(i!=((minx+maxx)/2)&&j!=((miny+maxy)/2)))
					{
						dest[k].x = i;
						dest[k].y = j;
						block.m[i][j] = 2;
						return;
					}
				}
			}
		}
		val++;
		}
		
	}
	
	public void calculatedestiationMatrix(int bx)
	{
		
		for(int k=1;k<=bx;k++)
		{
			if(box[k].isblock!=1&&mm.m[box[k].x][box[k].y]!=I)
			{
			subcalculatedestiationMatrix(k);
			}
			
		}
		
		
	}
	public void subcalculatedestiationMatrix(int k)
	{
		
		AlgoA(k);
	}
	
public void AlgoA(int k) 
	{
	  //cv - column vector
	
	int cngC;double min1;
	
	for(int i=0;i<=r+1;i++)
	{
		for(int j=0;j<=c+1;j++)
		{
			dest[k].D.m[i][j] = mm.m[i][j];		
			
		}
	}
	
	if(dest[k].havedest==1)
	{dest[k].D.m[dest[k].x][dest[k].y] = 1;}
	
	if(dest[k].havedest==0)
	{dest[k].D.m[box[k].x][box[k].y] = 1;}
	
	
	
	int set=1;
	int method = 2;
	
	if(method == 1)
	{
	while (set==1)
	{
		
		
         cngC=0;	
		for(int j=1;j<=c;j++)
		{
		for(int i=1;i<=r;i++)
		{
			if(dest[k].D.m[i][j]!=I&&dest[k].D.m[i][j]!=1)
		{
			min1 = find4min(dest[k].D.m[i-1][j],dest[k].D.m[i+1][j],dest[k].D.m[i][j-1],dest[k].D.m[i][j+1]);			
			if((min1<I1)&&((dest[k].D.m[i][j])>(min1+1)))
			{
				dest[k].D.m[i][j] = min1+1;
				cngC++;
			}
				
		}
		}
	    }
		
	if(cngC==0)	
	{
		set=0;
	}
	
	}	
	}
	
	if(method == 2)
	{
		
		int cellval = 1;
		int chng = 0;
		
		while(chng == 0)
		{
		chng = 1;	
		for(int j=1;j<=c;j++)
		{
		for(int i=1;i<=r;i++)
		{
			if(dest[k].D.m[i][j]==cellval)
			{
				if(dest[k].D.m[i-1][j]!=I&&dest[k].D.m[i-1][j]>(cellval+1)){dest[k].D.m[i-1][j] = cellval+1;chng = 0;}
				if(dest[k].D.m[i+1][j]!=I&&dest[k].D.m[i+1][j]>(cellval+1)){dest[k].D.m[i+1][j] = cellval+1;chng = 0;}
				if(dest[k].D.m[i][j-1]!=I&&dest[k].D.m[i][j-1]>(cellval+1)){dest[k].D.m[i][j-1] = cellval+1;chng = 0;}
				if(dest[k].D.m[i][j+1]!=I&&dest[k].D.m[i][j+1]>(cellval+1)){dest[k].D.m[i][j+1] = cellval+1;chng = 0;}
			
			}
			
		}
		}
		cellval++;
		}
		
		
		
	}
	
		
	}

public double find4min(double u,double d,double l,double r)
{
	if(u<d)
	{
		if(u<l)
		{
			if(u<r)
			{
				return u;
			}
			else if(r<=u)
			{
				return r;
			}
		}
		else if(l<=u)
		{
			if(l<r)
			{
				return l;
			}
			else if(r<=l)
			{
				return r;
			}
		}
	}
	else if(d<=u)
	{
		if(d<l)
		{
			if(d<r)
			{
				return d;
			}
			else if(r>=d)
			{
				return r;
			}
		}
		else if(l<=d)
		{
			if(l<r)
			{
				return l;
			}
			else if(r<=l)
			{
				return r;
			}
			
		}
	}
	return r;
}

public double mod(double a)
{
	double b;
	
	b = Math.pow(a,2);
	b = Math.pow(b,0.5);
	
	return b;
	
}

public void drawmap(int marker,int a)
{
	   
		for(int k=1;k<=bx;k++)
		{
		  
		  if(seqbx[marker][k].x!=seqbx[marker-a][k].x||seqbx[marker][k].y!=seqbx[marker-a][k].y)
		  {
		  b[seqbx[marker][k].x][seqbx[marker][k].y].setLabel(box[k].id+"");
		  b[seqbx[marker][k].x][seqbx[marker][k].y].setBackground(Color.YELLOW);
		  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setLabel(" ");
		  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setBackground(Color.WHITE);
		  
		  
		  for(int k1=1;k1<=bx;k1++)
			{
			 
			  if(seqbx[marker-a][k].x==dest[k1].dispx&&seqbx[marker-a][k].y==dest[k1].dispy)
			  {
				  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setLabel(box[k1].id+"'");
				  if(k==k1)
				  {
					  if(a==1) 
					  {
						  dest[k1].destdisplayclr = Color.CYAN;
						  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setBackground(dest[k1].destdisplayclr);
					  }
					  if(a==-1)
					  {
						  dest[k1].destdisplayclr = Color.WHITE;
						  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setBackground(dest[k1].destdisplayclr);
					  }
				  }
				  else
				  {
					  
						  b[seqbx[marker-a][k].x][seqbx[marker-a][k].y].setBackground(dest[k1].destdisplayclr);
					  
				  }
				  
				  
			  }
			  
			}
			
		  
		  }
		}
		
		
		
}


public void InstantCTWritet1(int k,double realtime)
{
	box[k].continousindex++;
	CTseqbx[box[k].continousindex][k].x1 = box[k].x;
	CTseqbx[box[k].continousindex][k].y1 = box[k].y;
	CTseqbx[box[k].continousindex][k].z1 = box[k].z;
	
	CTseqbx[box[k].continousindex][k].t1 = (realtime/(100.0*fact));
	
	CTseqbx[box[k].continousindex][k].p="False";
	CTseqbx[box[k].continousindex][k].d="False";
	
	if(box[k].reacheddestination==1)
	{
		if(box[k].pickupordrop[box[k].stacklennow]==1){CTseqbx[box[k].continousindex][k].p="True";}
		if(box[k].pickupordrop[box[k].stacklennow]==2){CTseqbx[box[k].continousindex][k].d="True";}
	}
	
}

public void InstantCTWritet2(int k,double realtime)
{
	
	CTseqbx[box[k].continousindex][k].x2 = box[k].x;
	CTseqbx[box[k].continousindex][k].y2 = box[k].y;
	CTseqbx[box[k].continousindex][k].z2 = box[k].z;
	
	CTseqbx[box[k].continousindex][k].t2 = (realtime/(100.0*fact));
	
}

public void caldeflectionlimit(int k)
{
    int currentdistance = (int) dest[k].D.m[box[k].x][box[k].y];
	if(currentdistance>=box[k].previousdistance){box[k].deflectionlimit++;}
	if(box[k].deflectionlimit>=3)
	{
		box[k].prvx=0; box[k].prvy=0;
		box[k].deflectionlimit = 0;
		
	}
	box[k].previousdistance = currentdistance;	
}

public void writeccdata(int k,int x,int y,int timein,int timeout)
{
	C.cc[k][x][y].timein = timein;
	C.cc[k][x][y].timeout = timeout;
	C.cc[k][x][y].isagentpresent = 1;
	
}

public void deleteccdata(int k,int x,int y)
{
	C.cc[k][x][y].timein = 0;
	C.cc[k][x][y].timeout = 0;
	C.cc[k][x][y].isagentpresent = 0;
	
}
public void writeseqccdata(int k,int dirx,int diry,int dist,String axis)
{

	int[] profile = null;
	
	int x1 = box[k].x; int y1 = box[k].y;	
	if(axis.equals("x")){profile = Profile[k].profilex[dist].clone();}
	if(axis.equals("y")){profile = Profile[k].profiley[dist].clone();}
		
	int t1 = 0,t2 = 0;	
	boolean b;int prvt2 = 0, prvt1 = 0;
	
	
	t2 = seqbxindex-1+profile[1];
	C.cc[k][box[k].x][box[k].y].timeout = t2;
	
	
	for(int i=1;i<=dist;i++)
	{
		
		x1 = x1+dirx;
		y1 = y1+diry;
		
		if(i==1&&i!=dist)
		{
		t1 = seqbxindex-1;
		t2 = t1+profile[i]+profile[i+1];
		
		}
		if(i!=1&&i!=dist)
		{
		t1 = prvt1+profile[i-1];
		t2 = t1+profile[i]+profile[i+1];		
			
		}
		if(i==dist&&i!=1)
		{		
		t1 = prvt1+profile[i-1];
		t2 = (int) I;			
		}
		
		if(i==1&&i==dist)
		{
			t1 = seqbxindex-1;
			t2 = (int) I;
			
		}
		
		
		prvt1 = t1;
		prvt2 = t2;
		
		
		writeccdata(k,x1,y1,t1,t2);
		
	}
	
	
	
	
	
}

public void nextwait(int k)
{

	box[k].waitindex++;
	box[k].wait=box[k].speedlist[box[k].waitindex];
	if(box[k].waitindex==box[k].speedlist.length-1)
	{
		box[k].determined = 0;
	}
	
	
	box[k].wait = box[k].wait-1;  // beacuse the agent not only calculates the next and wait in that sequence it also starts to wait from dt
	
}

public void reducewait(int k)
{

	if(box[k].wait!=0)
	{
		box[k].wait--;
		if(box[k].wait==0)
		{
			deleteccdata(k,box[k].x,box[k].y);
			
			if(box[k].prvnodehasPorDjob>=1)
			{
				if(box[k].prvnodehasPorDjob==1){PURANK.m[box[k].x][box[k].y] = PURANK.m[box[k].x][box[k].y]+1;}
				if(box[k].prvnodehasPorDjob==2){DORANK.m[box[k].x][box[k].y] = DORANK.m[box[k].x][box[k].y]+1;}
				box[k].prvnodehasPorDjob = 0;
			}
			
			box[k].x=box[k].nxtx;     box[k].y=box[k].nxty;
			InstantCTWritet2(k,seqbxindex);   /// write continous time 2
			
			
			
		}
		
	
	}
	
	if(box[k].tempwait!=0)
	{
		box[k].tempwait--;
	
	}
	
	if(box[k].winchwait!=0)
	{
		box[k].winchwait--;
	}
	
	
	
}

public int predictMaxDist(int k,int dirx,int diry,String axis)
{
	int x1 = box[k].x;
	int y1 = box[k].y;
	int dist = 0;
	
	
	while(1==1)
	{
		
    x1 = x1+dirx;
	y1 = y1+diry;	
			
	if(dest3[k].D.m[x1][y1]==I1||dest3[k].D.m[x1][y1]==I){return dist;}
		
	if(dest3[k].D.m[x1][y1]<=(5-1))
	{
		int headache = 0;
		headache = headache(k);
		if(headache==0){return dist;}
	}

	if(axis.equals("x")){if(dist==Profile[k].profilex.length-1){return dist;}}
	if(axis.equals("y")){if(dist==Profile[k].profiley.length-1){return dist;}}
	
	
     dist++;
	

	}
		
	

}

public int predictREQDist(int k,int dirx,int diry,String axis,int max)
{
	int dist = max;
	int a;
	while(1==1)
	{
		
		a = subpredictREQDist(k,dirx,diry,axis,dist);
		
		if(a==1){
			
			return dist;
			
		}
		
		dist--;
		if(dist==0){return dist;}
		
	}
		

}

public int subpredictREQDist(int k,int dirx,int diry,String axis,int dist)
{
	
int[] profile = null;

int x1 = box[k].x; int y1 = box[k].y;	
if(axis.equals("x")){profile = Profile[k].profilex[dist].clone();}
if(axis.equals("y")){profile = Profile[k].profiley[dist].clone();}
	
int t1 = 0,t2 = 0; int prvt1 = 0,prvt2 = 0;
boolean b;

	for(int i=1;i<=dist;i++)
	{
		
		x1 = x1+dirx;
		y1 = y1+diry;
		
		if(i==1&&i!=dist)
		{
		t1 = seqbxindex-1;
		t2 = t1+profile[i]+profile[i+1];
		
		}
		if(i!=1&&i!=dist)
		{
		t1 = prvt1+profile[i-1];
		t2 = t1+profile[i]+profile[i+1];		
			
		}
		if(i==dist&&i!=1)
		{		
		t1 = prvt1+profile[i-1];
		t2 = (int) I;			
		}
		
		if(i==1&&i==dist)
		{
			t1 = seqbxindex-1;
			t2 = (int) I;
			
		}
		
		
		prvt1 = t1;
		prvt2 = t2;
		
		for(int k1=1;k1<=bx;k1++)
		{
			if(k1!=k&&C.cc[k1][x1][y1].isagentpresent==1)
			{
				
			 b = (t2<C.cc[k1][x1][y1].timein)||(t1>C.cc[k1][x1][y1].timeout);
			 
			 if(!b){return 0;}	
				
			}
				
		}
		
		for(int k1=bx+1;k1<=bx+ax;k1++)
		{
			if(box[k].id==box[k1].id)
			{
				 b = (t2<C.cc[k1][x1][y1].timein)||(t1>C.cc[k1][x1][y1].timeout);
				 
				 if(!b){return 0;}	
				
			}
			
		}
		
		
	}
	
	
	
	return 1;
	
}



public void goawayasofnow(int k)
{
	int val = 3;
	
	while(1==1)
	{
	for(int i=r;i>=1;i--)
	{
		for(int j=c;j>=1;j--)
		{
			if((block.m[i][j]==0)&&dest[k].D.m[i][j]==val&&!(box[k].x==i&&box[k].y==j))
			{	
					for(int p=box[k].stacklen;p>=1;p--)
					{
						box[k].stackdestx[p+1] = box[k].stackdestx[p];
						box[k].stackdesty[p+1] = box[k].stackdesty[p];		
						box[k].pickupordrop[p+1] = box[k].pickupordrop[p];
						box[k].rank[p+1] = box[k].rank[p];
					}
					
					box[k].stackdestx[1] = i;
					box[k].stackdesty[1] = j;
					
					box[k].pickupordrop[1] = 1;
					box[k].rank[1]=1;
					PURANK.m[i][j] = 1;

					box[k].stacklen++;
								
					
					dest[k].x = i;
					dest[k].y = j;
					
					
					block.m[i][j] = 1;
					box[k].stackdestdumy[box[k].stacklennow]=1;
					
					
					
					
					return;
				
			}
		}
	}
	val++;
	
	
	}
	
	
}

public void actionPerformed(ActionEvent arg)
{
	if(arg.getSource().equals(clrlog)){LogDisp.setText("");}
	if(arg.getSource().equals(execute)){action=8;}
	
	for(int i =0;i<=r+1;i++)
	{
		for(int j=0;j<=c+1;j++)
		{	
			if(arg.getSource().equals(b[i][j]))
			{
				int x = i-1;
				int y = j-1;			
				LogDisp.setText("("+x+","+y+")");							
			}		
		}
		
	}
	
	if(action==8)
	{
		for(int i=marker+1;i<=seqbxindex;i++)
		{
			drawmap(i,1);
			try {Thread.sleep(1);}
			catch (InterruptedException e) {e.printStackTrace();}
		}
		
		marker = seqbxindex;
		action = 0;
	}	
}
	



	public void windowActivated(WindowEvent arg) 
	{
		
	}
	public void windowClosed(WindowEvent arg0) {}
	public void windowClosing(WindowEvent arg0) {f1.setVisible(false);}
	public void windowDeactivated(WindowEvent arg0) {}
	public void windowDeiconified(WindowEvent arg0) {}
	public void windowIconified(WindowEvent arg0){}
	public void windowOpened(WindowEvent arg0) {}

}

class DestMatrix
{
	double m[][];double I = 50000;
	int r,c;
	DestMatrix(int r,int c)
	{
		this.r = r;
		this.c = c;
		
		m = new double[r+2][c+2];
		for(int i=0;i<=r+1;i++)
		{
			m[i][0]=I;
			m[i][c+1]=I;
		}
		for(int j=0;j<=c+1;j++)
		{
			m[0][j]=I;
			m[r+1][j]=I;
		}
	}
	
}

class comMatrix 
{
	double m[][];double I = 500000;
	int r,c;
	comMatrix(int r,int c)
	{
		this.r = r;
		this.c = c;
		
		m = new double[r+2][c+2];
		for(int i=0;i<=r+1;i++)
		{
			m[i][0]=I;
			m[i][c+1]=I;
		}
		for(int j=0;j<=c+1;j++)
		{
			m[0][j]=I;
			m[r+1][j]=I;
		}
		
		
	}
}



class box
{
	Color clr;
	int id,wait,tempwait,WAIT,waitindex,grip;String xory;
	int x,y,z,winchwait,winchwaitindex,winchupord;
	int[]stackdestx;
	int[]stackdesty,rank;int[] stackdestdumy; int stacklen,stacklennow;
    int nxtx,nxty,prvx,prvy,actualprvx,actualprvy;
    double prvtime;
	int prvtimeCagnt;int startx,starty,delay;
	int curtimeCagnt;
	int isblock,reacheddestination,startwork;
	double prvcurentcellvcal;
	double curentcellvcal;
	int previousdistance;
	int deflectionlimit,determined;
	int[] pickupordrop,speedlist;int cost;
	int pickz,dropz,continousindex;int PCM;//PCM - profile consecutive movement
    int[][]winch;int NOS;//no of steps
    String jobtype;int Entangled;int f,b,pf,pb;int prvnodehasPorDjob;
	box()
	{
		jobtype="";
		stacklen = 0;prvx = 0;prvy = 0;actualprvx=0;actualprvx=0;  //actual previous x and y should not be changed in middle of program run
		stacklennow = 0;wait=0;
		stackdestx = new int[10];
		stackdesty = new int[10];
		stackdestdumy = new int[10];
		rank = new int[10];
		reacheddestination = 0;
		startwork=0;wait = 0;winchwait=0;determined=0;
		pickupordrop = new int[15];
		xory = "y";waitindex=1;continousindex=0;
		delay = 0;
		winchupord = 0;winchwaitindex=1;PCM=0;
		NOS = 0;cost=0;
		Entangled = 0;
	}
	
	
	int individualtimeseq;
	
	
}
class Profile
{
	int[][] profilex,profiley,profilecb;
	int xlen,ylen,cblen;
	Profile()
	{
	
		xlen = 0;
		ylen = 0;
		cblen = 0;
		
	}
		
}

class dest
{
	Color clr;
	DestMatrix D; int r,c;
	int havedest;int x,y;
	int dispx,dispy;
	Color destdisplayclr;
	dest(int r,int c)
	{
		this.r = r;
		this.c = c;
		D = new DestMatrix(r,c);
		dispx = 0;
		dispy = 0;
		destdisplayclr = Color.WHITE;
		havedest = 0;
	}
	

}

class seqbx
{
	int x;int y;
	int z;	
}

class CTseqbx
{
	int x1,x2;int y1,y2;
	int z1,z2;
	double t1,t2;
	String p,d;
	CTseqbx()
	{
		t1 = 0;t2 = 0;p="False";d="False";
		
	}
}

class Continuous
{
	int r,c;
	
	ContinuousCell cc[][][];double I = 50000;
    int totagent = 100;
	
	Continuous(int r,int c)
	{
		this.r = r;
		this.c = c;
		
		cc = new ContinuousCell[totagent+1][r+2][c+2];
		
		for(int k = 1;k<=totagent;k++)
		{
		for(int i = 0;i<=r+1;i++)
		{
			for(int j = 0;j<=c+1;j++)
			{
				
					
					 cc[k][i][j] = new ContinuousCell();
					
				
			}
		}
		}
	}
		
}


class ContinuousCell
{
	int isagentpresent;
	int timein,timeout;
	ContinuousCell()
	{
		timein = 0;
		timeout = 0;	
	}
		
}