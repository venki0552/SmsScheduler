package com.github.xylsh.receiver;

import java.util.ArrayList;

import com.github.xylsh.util.FixMsgSQLiteHelper;
import com.github.xylsh.util.MsgAddUtil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * ���ն�ʱ�㲥���յ��㲥���Ͷ���
 *
 */
public class AlarmReceiver extends BroadcastReceiver {

	public AlarmReceiver() {
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		String phoneNumber = intent.getStringExtra("com.github.xylsh.phone_number");
		String msgText = intent.getStringExtra("com.github.xylsh.msg_text");
		int msgId = intent.getIntExtra("com.github.xylsh.msg_id", -1);
		
		//���Ͷ���
		sendMsg(context, phoneNumber, msgText);
		
		//���������¼�ѷ���
		FixMsgSQLiteHelper sqlHelper = new FixMsgSQLiteHelper(context, FixMsgSQLiteHelper.DB_NAME,
				null, 1);
		sqlHelper.setMsgHaveSend( msgId );
	}
	
	/**
	 * ���Ͷ���
	 * @param context
	 * @param phoneNumber
	 * @param msgText
	 */
	public void sendMsg(Context context,String phoneNumber,String msgText){
		SmsManager smsManager = SmsManager.getDefault();
		ArrayList<String> msgTexts = smsManager.divideMessage(msgText);   //������Ź�������ֶ�������
		for( String msgStr : msgTexts ){
			smsManager.sendTextMessage(phoneNumber, null, msgStr, null, null);
		}
		Toast.makeText(context, "������һ����ʱ����!", Toast.LENGTH_LONG).show();
	}
}
