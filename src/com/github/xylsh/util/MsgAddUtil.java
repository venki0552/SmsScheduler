package com.github.xylsh.util;

import java.util.Calendar;
import java.util.regex.Pattern;

import com.github.xylsh.receiver.AlarmReceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;

public class MsgAddUtil {

	/**
	 * ��Ӷ�ʱ����
	 * 
	 * @param context
	 *            ������
	 * @param phone
	 *            ����
	 * @param msgText
	 *            ��������
	 * @param year
	 *            ����ʱ��,��
	 * @param month
	 *            ����ʱ��,��
	 * @param day
	 *            ����ʱ��,��
	 * @param hour
	 *            ����ʱ��,Сʱ
	 * @param minute
	 *            ����ʱ��,����
	 * @return �ɹ�����true,ʧ�ܷ���false
	 */
	public static boolean addMsg(Context context, Editable phone,
			Editable msgText, int year, int month, int day, int hour, int minute) {
		return addMsg(context, phone.toString(), msgText.toString(), year,
				month, day, hour, minute);
	}

	/**
	 * ��Ӷ�ʱ����
	 * 
	 * @param context
	 *            ������
	 * @param phone
	 *            ����
	 * @param msgText
	 *            ��������
	 * @param year
	 *            ����ʱ��,��
	 * @param month
	 *            ����ʱ��,��
	 * @param day
	 *            ����ʱ��,��
	 * @param hour
	 *            ����ʱ��,Сʱ
	 * @param minute
	 *            ����ʱ��,����
	 * @return �ɹ�����true,ʧ�ܷ���false
	 */
	public static boolean addMsg(Context context, String phone, String msgText,
			int year, int month, int day, int hour, int minute) {
		// ȥ�����˿ո�
		phone = phone.trim();
		msgText = msgText.trim();
		if (!isValid(phone, msgText, year, month, day, hour, minute)) {
			return false;
		}

		int id = (int) addToDB(context, phone, msgText, year, month, day, hour,
				minute);
		if (id < 0) {
			return false;
		}
		addFixTimeMsg(context, id, phone, msgText, year, month, day, hour,
				minute);

		return true;
	}

	/**
	 * ��Ӷ�ʱ���ŵ����ݿ�
	 * 
	 * @param context
	 *            ������
	 * @param phone
	 *            ����
	 * @param msgText
	 *            ��������
	 * @param year
	 *            ����ʱ��,��
	 * @param month
	 *            ����ʱ��,��
	 * @param day
	 *            ����ʱ��,��
	 * @param hour
	 *            ����ʱ��,Сʱ
	 * @param minute
	 *            ����ʱ��,����
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	private static long addToDB(Context context, String phone, String msgText,
			int year, int month, int day, int hour, int minute) {
		FixMsgSQLiteHelper sqlHelper = new FixMsgSQLiteHelper(context,
				FixMsgSQLiteHelper.DB_NAME, null, 1);
		String sendTime = year + "-" + (month + 1) + "-" + day + " " + hour
				+ ":" + minute; // month��0��ʼ��ģ����Դ洢ʱ+1
		long num = sqlHelper.insertFixMsg(phone, msgText, sendTime);
		return num;
	}

	/**
	 * ��Ӷ�ʱ���ŵ�ϵͳ
	 * 
	 * @param context
	 *            ������
	 * @param requestCode
	 *            Private request code for the sender (currently not used).
	 *            ���趨ϵͳ��ʱ���ͺ�ȡ����ʱ����ʱҪ��֤requestCodeһ�²���
	 * @param phone
	 *            ����
	 * @param msgText
	 *            ��������
	 * @param year
	 *            ����ʱ��,��
	 * @param month
	 *            ����ʱ��,��
	 * @param day
	 *            ����ʱ��,��
	 * @param hour
	 *            ����ʱ��,Сʱ
	 * @param minute
	 *            ����ʱ��,����
	 */
	public static void addFixTimeMsg(Context context, int requestCode,
			String phone, String msgText, int year, int month, int day,
			int hour, int minute) {
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction("com.github.xylsh.receiver.send_msg");
		intent.putExtra("com.github.xylsh.phone_number", phone);
		intent.putExtra("com.github.xylsh.msg_text", msgText);
		intent.putExtra("com.github.xylsh.msg_id", requestCode);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, 0);

		Calendar c = Calendar.getInstance();
		c.set(year, month, day, hour, minute, 0);

		// Beginning in API 19,����ʹ��setExact(...)
		aManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
				pendingIntent);
	}

	/**
	 * ȡ��ĳ����ʱ����
	 * 
	 * @param context
	 *            ������
	 * @param requestCode
	 *            ���趨ϵͳ��ʱ����ʱ��requestCode
	 */
	public static void cancleFixTimeMsg(Context context, int requestCode) {
		AlarmManager aManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent(context, AlarmReceiver.class);
		intent.setAction("com.github.xylsh.receiver.send_msg");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
				requestCode, intent, 0);
		aManager.cancel(pendingIntent);
	}

	/**
	 * ��֤�����Ƿ�Ϸ�
	 * 
	 * @return �Ϸ�����true,�Ƿ�����false
	 */
	public static boolean isValid(String phone, String msgText, int year,
			int month, int day, int hour, int minute) {
		// ������֤����
		if (phone.length() == 0 || msgText.length() == 0) {
			return false;
		}

		// ����ֻ�������֣��ݲ�����+86��ʽ�ĺ���
		if (!phone.matches("^[0-9]+$")) {
			return false;
		}

		// ��õ�ǰʱ��
		Calendar c = Calendar.getInstance();
		int currYear = c.get(Calendar.YEAR);
		int currMonth = c.get(Calendar.MONTH);
		int currDay = c.get(Calendar.DAY_OF_MONTH);
		int currHour = c.get(Calendar.HOUR_OF_DAY);
		int currMinute = c.get(Calendar.MINUTE) + 0; // ʱ�������ǵ�ǰʱ���3����֮��,����+3

		// ʱ�������ǵ�ǰʱ���3����֮��
		try {
			// �����ǹ�ȥ������
			if (year < currYear
					|| (year == currYear && month < currMonth)
					|| (year == currYear && month == currMonth && day < currDay)) {
				return false;
			}
			// ��������ǽ��죬��ô�����ǹ�ȥ��ʱ��
			if (year == currYear || month == currMonth || day == currDay) {
				if (hour < currHour
						|| (hour == currHour && minute < currMinute)) {
					return false;
				}
			}

		} catch (Exception e) {
			return false;
			// throw new IllegalArgumentException();
		}

		return true;
	}

}
