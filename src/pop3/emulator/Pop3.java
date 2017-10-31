package pop3.emulator;
import java.util.logging.Logger;
import java.io.*;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class ProtocolServer
 */

@ServerEndpoint("/Pop3")
public class Pop3{
	String username;
	String pass;
	String stat;
	String list;
	String []retr;
	String top;
	int rset;
	Boolean quit=false;
	Boolean usercheck=false;
	Boolean logincheck=false;
	private static Logger logger = Logger.getLogger(Pop3.class.getName());
	
	@OnOpen
	
	public void onOpen(Session session)
	{
		System.out.println(getClass().getClassLoader().getResource("logging.properties"));
		logger.info("Session " + session.getId() + " has opened a connection.");
		System.out.println(System.getProperty("user.home"));
		
	}
	

	@OnMessage
	public void onMessage(String message, Session session) throws IOException
	{
		System.out.println(System.getProperty("user.home"));

		if(message.contains("USER")) 
		{
			doSleep("USER"); //Suspends the current thread for the specified number of milliseconds
			try {
				if(session.isOpen())
				{	
				username=message.replace("USER", "");// to remove the keyword USER
				username=username.trim();
				boolean u=false;
				u=ClientRequest.doUser(username);//check weather the user exists in the given database(directory)
				if(u) {
						session.getBasicRemote().sendText("+OK"+" "+username+" exists");//send response back to the client
						usercheck=true;	
					}
					else  {
						session.getBasicRemote().sendText("-ERR "+username+" does not exist");
					usercheck=false;
					}
			}
				else
					session.getBasicRemote().sendText("-ERR session is closed");
				}
			catch(IOException e) {
				e.printStackTrace();
			}
			
		}
		
		else if(message.contains("PASS"))
		{
			doSleep("PASS");//Suspends the current thread for the specified number of milliseconds
			if(usercheck)//check if entered username is correct or not
			{
			try {
				if(session.isOpen())
				{
					pass=message.replace("PASS", "");
					pass=pass.trim();
					boolean b=false;
					
					b=ClientRequest.doPass(username,pass); //check weather the entered password is matched with the given username.
					if(b) {
						session.getBasicRemote().sendText("+OK login successful");//send response back to the client
						ClientRequest.doRename(username);
						logincheck=true;
						
					}
					else{  
						session.getBasicRemote().sendText("-ERR password for "+username+" is inncorrect");
						logincheck=false;}
				}
				}
			catch(IOException e) {
				e.printStackTrace();
				}
			}
			else{
				session.getBasicRemote().sendText("-ERR verify the username first");
			}
	
		}
		
		else if(message.contains("STAT"))
		{
			doSleep("STAT");	
			if(logincheck)//check if entered user is logged in or not
			{
				try {
					if(session.isOpen()){	
						stat=ClientRequest.doStat(username); //to find total number of e-mails and the total size of all those e-mails.
						session.getBasicRemote().sendText("+OK "+stat);
					}
					else	
					session.getBasicRemote().sendText("-ERR");
					}
				catch(IOException e) {
					e.printStackTrace();
					}
			}
			else{
				session.getBasicRemote().sendText("-ERR login required");
			}
		}
		
		else if(message.contains("RSET"))
		{
			doSleep("RSET");
			if(logincheck){
				try {
					if(session.isOpen()){	
						rset=ClientRequest.doRset(username);
						session.getBasicRemote().sendText("+OK maildrop has "+rset+" messages");
						
					}
					else	
						session.getBasicRemote().sendText("-ERR session is closed");
				}
				catch(IOException e) {
				e.printStackTrace();
				}
			}
			else
				session.getBasicRemote().sendText("-ERR login required");
		}
	
		else if(message.contains("QUIT"))
		{
			doSleep("QUIT");
			if(logincheck){
				try {
					if(session.isOpen()){	
						quit=ClientRequest.doQuit(username);
						if(quit){
							session.getBasicRemote().sendText("+OK server signing off (maildrop empty)");
							session.close();
							}
						else
							session.getBasicRemote().sendText("-ERR some message(s) can't be deleted");
					}
					else	
						session.getBasicRemote().sendText("-ERR session is closed");
				}
				catch(IOException e) {
				e.printStackTrace();
				}
			}
			else
				try {
					if(session.isOpen()){	
							session.getBasicRemote().sendText("+OK server signing off");
							session.close();
					}
					else	
						session.getBasicRemote().sendText("-ERR session is closed");
				}
				catch(IOException e) {
				e.printStackTrace();
				}
		}
		
		else if(message.contains("NOOP"))
		{
			doSleep("NOOP");
			if(logincheck){
						try {
							if(session.isOpen())
								session.getBasicRemote().sendText("+OK server has just received NOOP request");
							else	
								session.getBasicRemote().sendText("-ERR session id closed");
						}
						catch(IOException e) {
							e.printStackTrace();
						}
			}
			else{
				session.getBasicRemote().sendText("-ERR login required");	
			}
		}
		
		else if(message.contains("LIST")){
			
			doSleep("LIST");
			
			if(logincheck){
				try {
					if(session.isOpen()){	
							int k = 0;
							list=message.replace("LIST", "");
							list=list.trim();
							if(list.contentEquals(""));
							else
							k=Integer.parseInt(list);
							if(ClientRequest.doList(username,k,session)!=null){
								String s =ClientRequest.doList(username,k,session);
								session.getBasicRemote().sendText("+OK\n"+s);
							}
					}
					else	
					session.getBasicRemote().sendText("-ERR session is closed");
					}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			else{
				session.getBasicRemote().sendText("-ERR login required");
			}
		}
		
		else if(message.contains("RETR")){
			
			doSleep("RETR");
			
			if(logincheck){
				try {
					if(session.isOpen())
					{
					list=message.replace("RETR", "");
					list=list.trim();
					int k=Integer.parseInt(list);
					if(ClientRequest.doRetr(username,k,session)!=null){
						String s[] =ClientRequest.doRetr(username,k,session);
						session.getBasicRemote().sendText("+OK "+s[0]+" octets\n"+s[1]);
					}
			
				}
				else	
					session.getBasicRemote().sendText("-ERR session is closed");
				}
				catch(IOException e) {
				e.printStackTrace();
				}
			}
			
			else{
				session.getBasicRemote().sendText("-ERR login required");
			}
		}

		else if(message.contains("DELE"))
		{
		doSleep("DELE");
			if(logincheck){
				try {
					if(session.isOpen()){	
						list=message.replace("DELE", "");
						list=list.trim();
						int k=Integer.parseInt(list);
						boolean b =ClientRequest.doDelete(username,k);
						if(b)
							session.getBasicRemote().sendText("+OK message "+k+" is deleted");
						else
							session.getBasicRemote().sendText("-ERR no such message in maildrop");
					}
					else	
					session.getBasicRemote().sendText("-ERR session is closed");
					}
				catch(IOException e) {
					e.printStackTrace();
				}
			}
			else{
				session.getBasicRemote().sendText("-ERR login required");
			}
		}
		
		else if(message.contains("TOP")){
			
			if(logincheck){
			try {
				if(session.isOpen())
				{
					top=message.replace("TOP", "");
					top=top.trim();
					String[] str1=top.split("\\$");//split two values sent from client.
				
					int k=Integer.parseInt(str1[0]);
					int j=Integer.parseInt(str1[1]);
					
					if(ClientRequest.doTop(username,k,j,session)!=null){
						String st=ClientRequest.doTop(username,k,j,session);
						session.getBasicRemote().sendText("+OK "+st);
					}
			}
				else	 
					session.getBasicRemote().sendText("-ERR session is closed");
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
			
			else{
				session.getBasicRemote().sendText("-ERR login required");
			}
		}
	}
	

	@OnClose
	public void onClose(Session session)
	{
		System.out.println(System.getProperty("user.home"));
		logger.info("Session " + session.getId() + " has ended!");
	}
		


	private void doSleep(String command) {
		logger.info("Received "+command+" from Client");
		try {
			Thread.sleep(1000);
		} 
		catch (InterruptedException e1) {
			e1.printStackTrace();
		}
	}
}

