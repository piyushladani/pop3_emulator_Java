package pop3.emulator;
import java.io.*;
import java.util.Arrays;
import javax.websocket.Session;

public class ClientRequest {
		static String path = "/home/student/users";//path to access the directories in the system
		static int []delearray=new int[100];
		static int i=0;
	
		public static boolean doUser(String username) {

				boolean a=false;
				boolean b=false;
				b=findFile(username,new File(path));//to check weather the file at provided path exists
				if (b){a = true;}
				return a;
		}
	
		public static boolean doPass(String username, String password) throws IOException {
		
				boolean a=false;
				username = path+"/"+username+"/password.txt";//
				BufferedReader inputReader = new BufferedReader(new FileReader(username));
				try{
					String nextLine;
					while ((nextLine = inputReader.readLine()) != null){
						if (nextLine.equals(password)) { 
							a=true;}
					}
		
				}
				finally{ 
					inputReader.close();
				}
			return a;
		}

		public static String doStat(String username) {
				long l[]=new long[2];
				l[0]=new File(path+"/"+username+"/"+"emails").list().length;
				File f=new File (path+"/"+username+"/"+"emails");
				File[] list = f.listFiles();
				long le=0;
				if(list!=null)
					for (File fil : list){
						le =fil.length();
						l[1]=le+l[1];
					} 
				String s= Arrays.toString(l); //convert array to string
				s=s.replace(",","");
				s=s.replace("]","");
				s=s.replace("[","");
		return s;
		}
	
		public static String doList(String username,int emailnum,Session session) throws IOException{
				int[][] ar;
				boolean bo=false;
				long lo[];
				if(emailnum==0){
					int p= new File(path+"/"+username+"/"+"emails").list().length;
					int l[][]=new int[p][2];
					File f=new File (path+"/"+username+"/"+"emails");
					File[] list = f.listFiles();
					long i=0;
					int[] b = new int[10];
					int j=0;
					for (File fil : list){
						i =fil.length();
						b[j]=(int) i;
						j++;
					}
					
					for (int r=0,d=0; r < p; r++,d++) {
							for (int c=0; c < 2; c++) {
								if(c==0){
									l[r][c]=r+1;
								}
								else{
									l[r][c]= b[(int) d];
								}
					
							}
					}
					ar=l;
					int newArray[] = new int[ar.length*2];
					for(int m = 0; m < ar.length; m++) {	 //to convert multidimensional array to one dimensional array 
						int[] row = ar[m];				
							for(int n = 0; n < row.length; n++){
								int number = ar[m][n];
								newArray[m*row.length+n] = number;
							}
					}
					String s=Arrays.toString(newArray); //convert array to string
					s=s.replace(",","");
					s=s.replace("]","");
					s=s.replace("[","");
					return s;	 
				}
				
			else{
					lo=new long[2];
					File f=new File (path+"/"+username+"/"+"emails"+"/"+"email"+emailnum+".txt");
					String str= "email"+emailnum+".txt";
					bo=findFile(str,new File(path+"/"+username+"/"+"emails"));
					if(bo){
						long len =f.length();
						lo[1]=len;
						lo[0]=emailnum;
						System.out.print("\n LIST \n ---- \n" );
						System.out.print(" " + lo[0]+""+lo[1]);
						String s=Arrays.toString(lo); //convert array to string
						s=s.replace(",","");
						s=s.replace("]","");
						s=s.replace("[","");
						return s ;
					}
					else{
						session.getBasicRemote().sendText("-ERR no such message in maildrop");	
					}
				}
				return null;
		
		}

		public static String[] doRetr(String username, int emailnum, Session session) throws IOException {
			String[]str = new String[2];
			File file = new File (path+"/"+username+"/"+"emails"+"/"+"email"+emailnum+".txt");
			if(file.exists() && !file.isDirectory()) { 
				BufferedReader br = new BufferedReader(new FileReader(path+"/"+username+"/"+"emails"+"/"+"email"+emailnum+".txt"));
				try {
			
					StringBuilder sb = new StringBuilder();
					String line = br.readLine();

					while (line != null) {
						sb.append(System.lineSeparator());
						sb.append(line);
						line = br.readLine();
					}
					str[1] = sb.toString();
					str[1]=str[1].replaceFirst("\n", "");
				}
				finally {
					br.close();
				}
				Long l= file.length();
				str[0]=Long.toString(l); 
		 
				return str;
			}
			
			else{
			session.getBasicRemote().sendText("-ERR no such message in maildrop");	
			}
		return null;
		}
	
		public static boolean doDelete(String username, int emailnum) throws IOException{
			boolean b = false;
			File file=new File(path+"/"+username+"/emails/email"+emailnum+".txt");
			b= file.exists();
			if(b){
				delearray[i++]=emailnum;
			}
			return b;
		}

		public static String doTop(String username, int emailnum, int linenum,Session session) throws IOException {
			BufferedReader br=null;
			String line = null;
			File file = new File (path+"/"+username+"/"+"emails"+"/"+"email"+emailnum+".txt");
			if(file.exists() && !file.isDirectory()) {
				try {
					br = new BufferedReader(new FileReader(path+"/"+username+"/"+"emails"+"/"+"email"+emailnum+".txt"));
			
				} 
				catch (FileNotFoundException e) {
					e.printStackTrace();
			
				}
		
				StringBuilder sb = new StringBuilder();
				int i = 0;
         
				try {
					while(((line = br.readLine()) != null) && i < linenum) {
						System.out.println(line); 
						sb.append(System.lineSeparator());
						sb.append(line);
						i++;
					}
				}
				catch (IOException e) {
					e.printStackTrace();
				}   
       
				String s = sb.toString();
 
				return s;
			}
			else{
				session.getBasicRemote().sendText("-ERR no such message in maildrop");	
			}
			return null;
		}

		public static boolean doQuit(String username){
		
				for(int j=0;i>j;j++){
					int emailnum = delearray[j];
					File file=new File(path+"/"+username+"/emails/email"+emailnum+".txt");
					file.delete();
				}
		
				return true;
		}
		
		public static int doRset(String username){
				i=0;
				int l;
				l=new File(path+"/"+username+"/"+"emails").list().length;
				return l;
		}
	
		public static void doRename(String username) throws IOException {
				String absolutePath = path+"/"+username+"/emails/";
				File dir = new File(absolutePath);
				File[] filesInDir = dir.listFiles();
				Arrays.sort(filesInDir);
				int i = 0;
		        for(File file1:filesInDir) {
		        	i++;
		        	String oldName = file1.getName();
		        	oldName = absolutePath + oldName;
		        	File oldFile=new File(oldName);
		        	String newName = "email" + i + ".txt";
		        	newName = absolutePath +  newName;
		        	File newFile =new File(newName);
		        	oldFile.renameTo(newFile);
		        }
		}

		private static boolean findFile(String username, File file) {
	
				File[] list = file.listFiles();
				boolean a=false;
				boolean b=false;
				if(list!=null)
					for (File fil : list)
					{
						if (username.contentEquals(fil.getName()))
						{
							a=true;
							break;
						}
					}
				if(a){
					b = true;
				}
		return b;
		}
	
}
