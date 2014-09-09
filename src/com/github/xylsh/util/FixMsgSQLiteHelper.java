package com.github.xylsh.util;

import java.util.ArrayList;

import com.github.xylsh.bean.FixMessage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * �������ݿ��helper
 *
 */
public class FixMsgSQLiteHelper extends SQLiteOpenHelper {

	/**
	 * ���ݿ�����
	 */
	public static final String DB_NAME = "FixMsgDB";
	/**
	 * ���ݱ�����
	 */
	public static final String FIX_MSG_TABLE_NAME = "MsgDataTable";

	public FixMsgSQLiteHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ FIX_MSG_TABLE_NAME
				+ " ( id integer primary key autoincrement,phone_number text, msg_text text, send_time text, have_send integer )";
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// ���ݿⱻ�ı�ʱ����ԭ�ȵı�ɾ����Ȼ�����±�
		String sql = "drop table if exists " + FIX_MSG_TABLE_NAME;
		db.execSQL(sql);
		onCreate(db);
	}


	/**
	 * ���δ���Ͷ���
	 * @return δ���Ͷ����б�
	 */
	public ArrayList<FixMessage> getNotSendFixMsg() {
		return getFixMsg("have_send=?", new String[] { "0" });
	}

	/**
	 * ����ѷ��Ͷ���
	 * @return �ѷ��Ͷ����б�
	 */
	public ArrayList<FixMessage> getHaveSendFixMsg() {
		return getFixMsg("have_send=?", new String[] { "1" });
	}

	/**
	 * ��ȡ���ݿ��е�FixMsg
	 * 
	 * @param selection
	 *            A filter declaring which rows to return, formatted as an SQL
	 *            WHERE clause (excluding the WHERE itself). Passing null will
	 *            return all rows for the given table.
	 * @param selectionArgs
	 *            You may include ?s in selection, which will be replaced by the
	 *            values from selectionArgs, in order that they appear in the
	 *            selection. The values will be bound as Strings.
	 * @return
	 */
	public ArrayList<FixMessage> getFixMsg(String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(FIX_MSG_TABLE_NAME, null, selection,
				selectionArgs, null, null, "id desc", null);
		ArrayList<FixMessage> msgList = new ArrayList<FixMessage>();
		FixMessage currMsg;
		while (cursor.moveToNext()) {
			currMsg = new FixMessage();
			currMsg.setId(cursor.getInt(0));
			currMsg.setPhoneNumber(cursor.getString(1));
			currMsg.setMsgText(cursor.getString(2));
			currMsg.setSendTime(cursor.getString(3));
			currMsg.setHaveSend(cursor.getInt(4));
			msgList.add(currMsg);
		}

		return msgList;
	}
	
	/**
	 * ����һ����ʱ���ż�¼�����ݿ�
	 * 
	 * @param phoneNumber
	 * @param msgText
	 * @param sendTime
	 * @return the row ID of the newly inserted row, or -1 if an error occurred
	 */
	public long insertFixMsg(String phoneNumber, String msgText, String sendTime) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("phone_number", phoneNumber);
		cv.put("msg_text", msgText);
		cv.put("send_time", sendTime);
		cv.put("have_send", 0); // 0��ʾδ����
		return db.insert(FIX_MSG_TABLE_NAME, null, cv);
	}

	/**
	 * ɾ�����ݿ��е�ָ����ʱ����
	 * @param msg
	 */
	public void deleteFixMsg(FixMessage msg) {
		deleteFixMsg(msg.getId());
	}

	/**
	 * ɾ�����ݿ��е�ָ����ʱ����
	 * @param idToDel
	 */
	public void deleteFixMsg(int idToDel) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(FIX_MSG_TABLE_NAME, "id=?",
				new String[] { String.valueOf(idToDel) });
	}
	
	/**
	 * ����ָ��id��have_send�ֶ�Ϊ1
	 * @param id
	 */
	public void setMsgHaveSend(int id){
		if( id < 0 ){
			return;
		}
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "update "+ FIX_MSG_TABLE_NAME +" set have_send=1 where id=" + id;
		db.execSQL(sql);
	}

}
