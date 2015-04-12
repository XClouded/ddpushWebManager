package com.skywds.java;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

class ProcessAcceptThread implements Runnable
{
	private Socket client;
	private String pushSourceStr = null;
	private Map< String, String > paramsMap = null;
	
	public ProcessAcceptThread( Socket socket )
	{
		client = socket;
		paramsMap = new HashMap< String, String >();
	}
	
	@Override
	public void run()
	{
		accept( client );
	}
	
	protected synchronized void accept(Socket client)
	{
		paramsMap.clear();
		pushSourceStr = "";
		int result = 0;
		InputStream inp = null;
		OutputStream out = null;
		BufferedReader reader = null;
		BufferedWriter writer = null;
		try
		{
			
			inp = client.getInputStream();
			out = client.getOutputStream();
			
			reader = new BufferedReader( new InputStreamReader( inp ) );
			writer = new BufferedWriter( new OutputStreamWriter( out ) );
			
			//接受
			pushSourceStr = reader.readLine();
			log( "收到一个推送请求" );
			log( pushSourceStr );
			//http request 必须有这个两个参数
			if( pushSourceStr == null || pushSourceStr.indexOf( "?act=" ) == -1 || pushSourceStr.indexOf( "ticket=" ) == -1 )
			{
				result = 0;
			}
			else
			{
				result = processHttpPushRequest();
			}
			
			//响应
			StringBuffer sb = new StringBuffer("HTTP/1.1 200 OK\r\n");
			sb.append( "Content-Type:text/plain\r\n\r\n" );
			sb.append( result );
            writer.write( sb.toString() );
			writer.flush();
			
		}
		catch ( IOException e )
		{}
		finally
		{
			try
			{
				try	{reader.close();writer.close();inp.close();out.close();}catch ( Exception e2 ){}
				
				try{client.close();}catch ( Exception e2 ){}
				
				client = null;
				inp = null;
				out = null;
				reader = null;
				writer = null;
			}catch ( Exception e ){}
		}
	}
	
	//处理以HTTP方式发布的推送请求
	private int processHttpPushRequest()
	{
		boolean boolFlag = parseSourceStr();
		if( !boolFlag )
		{
			return 0;
		}
		
		
		//解析成功。开始分析推送方式
		String tmpAct = paramsMap.get( "act" );
		if( "user".equalsIgnoreCase( tmpAct ))
		{
			//给一个用户推送
			boolFlag = pushToUser();
		}
		else if( "more".equalsIgnoreCase( tmpAct ))
		{
			//给多个用户推送
			boolFlag = pushToMore();
		}
		
		if( boolFlag )
		{
			log( "任务提交到DDPUSH推送服务器成功" );
			return 1;
		}
		else
		{
			log( "任务提交到DDPUSH推送服务器失败" );
			return 0;
		}
	}
	
	private boolean pushToMore()
	{
		log( "收到一个推送多人消息...暂时没找到执行代码...服务器凌乱中...(~>__<~)" );
		return true;
	}
	
	private boolean pushToUser()
	{
		String tmpUuidStr = paramsMap.get( "uuid" ); 
		if( tmpUuidStr == null )
		{
			return false;
		}
		
		log( "从HTTP收到一个推送请求 推送类型:单个用户  推送目标UUID：" + tmpUuidStr + " 正在提交到DDPUSH推送服务器..." );
		
		boolean result = false;
		Pusher pusher = null;
		byte[] uuid = null;
		String data = null;
		String type = null;
		try
		{
			pusher = new Pusher(HttpPushServer.DDPUSH_PUSH_SERVER_IP, HttpPushServer.DDPUSH_PUSH_SERVER_PORT, HttpPushServer.PUSH_SERVER_TIMEOUT);
			uuid = StringUtil.hexStringToByteArray( tmpUuidStr );
			data = paramsMap.get( "data" );
			type = paramsMap.get( "type" );
			
			if( "10".equals( type ))
			{	
				//通用消息
				result = push0x10Message( pusher, uuid );
			}
			else if( "11".equals( type ))
			{
				//分类消息
				result = push0x11Message( pusher, uuid, Long.valueOf( data) );
			}
			else if( "20".equals( type ))
			{
				//自定义消息
				result = push0x20Message( pusher, uuid, data );
			}
			
			
		}
		catch ( Exception e )
		{
			return result;
		}
		finally
		{
			try
			{
				pusher.close();
				pusher = null;
				uuid = null;
				data = null;
				type = null;
			}catch ( Exception e ){}
		}
		
		return result;
	}
	
	//通用消息
	private boolean push0x10Message( Pusher pusher, byte[] uuid) throws Exception
	{
		return pusher.push0x10Message( uuid );
	}
	
	//分类消息
	private boolean push0x11Message( Pusher pusher, byte[] uuid, long data) throws Exception
	{
		return pusher.push0x11Message( uuid, data );
	}
	
	//自定义消息
	private boolean push0x20Message( Pusher pusher, byte[] uuid, String data ) throws Exception
	{
		return pusher.push0x20Message( uuid, URLDecoder.decode( data, "UTF-8" ).getBytes("UTF-8"));
	}
	
	private boolean parseSourceStr()
	{
		if( pushSourceStr == null || pushSourceStr.length() < 5)
		{
			return false;
		}
		
		try
		{
			//截取请求内容(请求地址)
			int offset = pushSourceStr.indexOf( " ", 6 );
			String content = pushSourceStr.substring( 6, offset );
			//System.out.println(content);
			String[] params = content.split( "&" );
			int size = params.length;

			if( params.length < 2)
			{
				return false;
			}
			
			for( int i = 0; i < size; i ++ )
			{
				parseKeyValue(params[i]);
			}
			
			params = null;
			
		}
		catch ( Exception e )
		{
			return false;
		}
		
		return true;
	}
	
	private void parseKeyValue( String tmpStr )
	{
		if( tmpStr == null )
		{
			return ;
		}
		
		try
		{
			String[] tmpKeyValue = tmpStr.split( "=" );
			if( tmpKeyValue.length == 2 )
			{
				paramsMap.put( tmpKeyValue[0].toLowerCase(),	tmpKeyValue[1] );
			}
			else
			{
				paramsMap.put( tmpKeyValue[0].toLowerCase(), "" );
			}
			tmpStr = null;
			tmpKeyValue = null;
		}
		catch ( Exception e )
		{
			// TODO: handle exception
		}
	}
	
	public static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SS");
	public static void log( String log )
	{
		if( HttpPushServer.DEBUG )
		{
			System.out.println( "" + format.format( new Date() ) + " : " + HttpPushServer.class.getSimpleName() + " : " + log);
		}
	}
}