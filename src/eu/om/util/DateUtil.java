package eu.om.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static Date string2date(String day, String time) {
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd:hh:mm");
			Date d = formatter.parse(day + ":" + time);
			return d;
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
}
