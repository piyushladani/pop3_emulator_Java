package de.fuoas.research;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Servlet implementation class ProtocolServer
 */
@ServerEndpoint("/ProtocolServer")
public class ProtocolServer{
	private static final long serialVersionUID = 1L;
    
	@OnOpen
	public void onOpen(Session session)
	{
		System.out.println("Session " + session.getId() + " has opened a connection!");
	}
	
	@OnMessage
	public void onMessage(String message, Session session)
	{
		if(message.contains("Invite")) 
		{
			System.out.println("Received Invite from Client!");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if(session.isOpen())
					session.getBasicRemote().sendText("200OK");
			}
			catch(IOException e) {
				e.printStackTrace();
			}
		}
		else if(message.contains("Ack"))
		{
			System.out.println("Received Ack from Client");
		}
	}
	
	@OnClose
	public void onClose(Session session)
	{
		System.out.println("Session " + session.getId() + " has ended!");
	}
}
