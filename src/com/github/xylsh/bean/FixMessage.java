package com.github.xylsh.bean;

/**
 * ��ʱ����ʵ����.
 *
 */
public class FixMessage {

	private int id;
	private String phoneNumber;   //����
	private String msgText;       //��������
	private String sendTime;      //����ʱ��
	private int haveSend;         //�Ƿ��ѷ��ͣ�0��ʾδ���ͣ�1��ʾ�ѷ���
	
	public FixMessage() {
	}

	public FixMessage(int id, String phoneNumber, String msgText,
			String sendTime, int haveSend) {
		super();
		this.id = id;
		this.phoneNumber = phoneNumber;
		this.msgText = msgText;
		this.sendTime = sendTime;
		this.haveSend = haveSend;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getMsgText() {
		return msgText;
	}
	public void setMsgText(String msgText) {
		this.msgText = msgText;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public int getHaveSend() {
		return haveSend;
	}
	public void setHaveSend(int haveSend) {
		this.haveSend = haveSend;
	}
}
