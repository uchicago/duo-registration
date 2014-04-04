/**
 * @author Daniel Yu, danielyu@uchicago.edu
 */


package edu.uchicago.duo.service;

public interface DuoObjInterface {
	
	public String getObjById ();
	
	public String getObjStatusById (String param1);
	
	public String getObjByParam (String param1, String param2, String attribute);
	
	public String createObjByParam (String param1, String param2, String param3, String param4);
	
	public void associateObjs (String param1, String param2);
	
	public String objActionById (String param1, String param2);
	
}
