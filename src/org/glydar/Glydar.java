package org.glydar;

/**
 * Glydar - Java CubeWorld Server
 * TODO List (In order of importance)
 * See development board on private trello
 * @author Glydar Team
 *
 */

public class Glydar
{
	static CWServer SERVER;
	
	public static void main(String[] args)
	{
	        if (args.length > 0 && args[0].equals("")) {
                    
                }
		
		SERVER = new CWServer();
		
		try
		{
			SERVER.run();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return;
	}
	
	public static CWServer getServer() 
	{
		return SERVER;
	}
}