package com.org.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFactory {

	public static java.sql.Date utilDateTosqlDate(java.util.Date utilDate) {
		
		return new java.sql.Date(utilDate.getTime());
	}
	
	public static java.util.Date sqlDateToutilDate(java.sql.Date sqlDate){
		
		return new java.util.Date (sqlDate.getTime());
	}
	
	public static String sqlDateToString(java.sql.Date sqlDate) {
		
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new java.util.Date (sqlDate.getTime()));
	}
	
	public static Timestamp stringToTimestamp(String time) throws ParseException {
		DateFormat format = DateFormat.getDateTimeInstance();
		format.setLenient(false);
		return new Timestamp(format.parse(time).getTime());
	}
	
}
