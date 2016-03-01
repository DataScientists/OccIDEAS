package org.occideas.utilities;

import java.util.List;

public class CommonUtil {

	 public static boolean isListEmpty(List<? extends Object> list){
	    	return list == null || list.isEmpty();
	 }
	 public static boolean isNumeric(String str)  
	 {  
	   try  
	   {  
	     Integer.parseInt(str);  
	   }  
	   catch(NumberFormatException nfe)  
	   {  
	     return false;  
	   }  
	   return true;  
	 }
	
}
