package com.skywds.java;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpPushServer
{
	public static int HTTP_PUSH_PORT = 8888;	//http服务器端口号
	public static String HTTP_PUSH_SERVER_IP = "";	//http服务器IP   请填写当前机器的IP
	public static String DDPUSH_PUSH_SERVER_IP = ""; 	//ddpush 服务器IP 
	public static int DDPUSH_PUSH_SERVER_PORT = 9999;		//ddpush 推送服务器端口号
	public static int PUSH_SERVER_TIMEOUT = 5000;	//ddpush 推送服务器连接超时时间
	public static boolean DEBUG = true;
	public static final String PROPERTY_NAME = "httppush";
	
	protected ServerSocket server = null;
	protected boolean resetFlag = true;
	protected boolean stoped = false;
	
	ExecutorService executors;

	protected boolean init()
	{
		try
		{
			HTTP_PUSH_PORT = Integer.parseInt( PropertyUtil.getProperty( PROPERTY_NAME, "HTTP_PUSH_PORT" ) );
			HTTP_PUSH_SERVER_IP = PropertyUtil.getProperty( PROPERTY_NAME, "HTTP_PUSH_SERVER_IP" );
			DDPUSH_PUSH_SERVER_IP = PropertyUtil.getProperty( PROPERTY_NAME, "DDPUSH_PUSH_SERVER_IP" );
			DDPUSH_PUSH_SERVER_PORT = Integer.parseInt( PropertyUtil.getProperty( PROPERTY_NAME, "DDPUSH_PUSH_SERVER_PORT" ) );
			PUSH_SERVER_TIMEOUT = Integer.parseInt( PropertyUtil.getProperty( PROPERTY_NAME, "CONNECT_DDPUSHPUSH_SERVER_TIMEOUT" ) );
		}
		catch ( Exception e )
		{
			System.out.println("读取配置文件失败! 将使用默认参数");
		}
		
		if( null == HTTP_PUSH_SERVER_IP || 
			"".equals( HTTP_PUSH_SERVER_IP ) || 
			null == HTTP_PUSH_SERVER_IP || 
			"".equals( HTTP_PUSH_SERVER_IP ) )
		{
			return false;
		}
		return true;
	}
	
	public void start()
	{
		if( !init() )
		{
			System.out.println("请在properties文件中配置 DDPUSH 推送服务器IP 和 HTTP 发布推送消息时请求的服务器IP地址");
			System.err.println("启动失败!");
			return ;
		}
		//0的md5 uuid是 cfcd208495d565ef66e7dff9f98764da
		//executors = Executors.newFixedThreadPool( 4 );
		System.out.println("HTTP push server starting .... ");
		while ( !stoped && !Thread.interrupted() )
		{
			reset();
			accept(server);
			//try{Thread.sleep( 5 );}catch ( Exception e ){}
		}
		stop();
		//executors.shutdown();
		System.out.println("HTTP push server stoped...");
		
	}
	
	private void accept( ServerSocket server2 )
	{
		try
		{
			//executors.execute( new ProcessAcceptThread( server.accept() ) );
			new Thread( new ProcessAcceptThread( server.accept() ) ).start();
		}
		catch ( IOException e )
		{
			if( server.isClosed() ) resetFlag = true;
		}
	}

	public void stop()
	{
		stoped = true;
		try
		{ 
			server.close(); 
			server = null;
		} catch ( IOException e ) {}
	}
	
	protected synchronized void reset()
	{
		if( !resetFlag ) return ;
		
		if( server != null )
		{
			try	{ server.close(); server = null; }catch ( Exception e ){};
		}
		
		try	{
			server = new ServerSocket( HttpPushServer.HTTP_PUSH_PORT );
			resetFlag = false;
		}catch ( Exception e ){
			try{ Thread.sleep( 100 );	}catch ( InterruptedException e1 ) {};
		}
		
	}
	
	public static void main( String[] args )
	{
		new HttpPushServer().start();
	}
	

	
}
