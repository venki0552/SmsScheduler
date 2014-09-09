package com.github.xylsh.util;

import java.util.Calendar;

/**
 * ������ع���
 *
 */
public class CalendarUtil {

	/**
	 * ������"2014-09-01\n19 : 31"���ַ�������Calendar����
	 * 
	 * @param date
	 * @return
	 */
	public static Calendar getCalendarFromStr(String date) {
		Calendar c = Calendar.getInstance();

		String[] timeSplits = date.split("\n");
		String[] dateStr = timeSplits[0].split("[-]");
		String[] timeStr = timeSplits[1].split("[:]");
		int year = Integer.parseInt(dateStr[0].trim());
		int month = Integer.parseInt(dateStr[1].trim()) - 1; // Calendar���·��Ǵ�0��ʼ���
		int day = Integer.parseInt(dateStr[2].trim());
		int hour = Integer.parseInt(timeStr[0].trim());
		int minute = Integer.parseInt(timeStr[1].trim());

		c.set(year, month, day, hour, minute, 1);
		return c;
	}
	
}
