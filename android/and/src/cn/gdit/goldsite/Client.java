package cn.gdit.goldsite;

/*
 * �ļ���д����
 * Lzz
 *
 * ����
 * 2016��11��19��
 */
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import android.graphics.Bitmap;

public class Client  extends MainActivity {
	private int port = 60000;
	public static  DataInputStream bInputStream;
	public static  DataOutputStream bOutputStream;
	public static Socket socket;
	static public short TYPE0 = 0x55;
	static public short TYPE1 = 0xaa;
	static public short TYPE2 = 0x02;
	static public short MAJOR = 0x00;
	static public short FIRST = 0x00;
	static public short SECOND = 0x00;
	static public short THRID = 0x00;
	static public MainActivity mainactivity;
	static public short CHECKSUM = 0x00;
	static public byte[] sbyte = new byte[40];
	static public int i=2;
	static public boolean ���� = false;
	static public int S1 = 0x00;
	static public int S2 = 0x00;
	static public int S3 = 0x00;
	static public int S4 = 0x00;
	static public int S5 = 0x00;
	static public int S6 = 0x00;
	public static boolean connect_flag = false;
	static public String cur_xy= "a11";


	static public Bitmap bitmap0 ;
	public void onDestory() {
		try {
			if (socket != null && !socket.isClosed()) {
				socket.close();                                                        ///�ر��׽���
				bInputStream.close();                                                  ///�ر�������
				bOutputStream.close();                                                 ///�ر������
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String IP) {
		try {
			socket = new Socket(IP, port);                                    ///����С��
			bInputStream = new DataInputStream(socket.getInputStream());      ///������
			bOutputStream = new DataOutputStream(socket.getOutputStream());   ///�����
			connect_flag = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	int save_num=0;
	public void send() {
		try {
			if (socket != null && !socket.isClosed()) {
				CHECKSUM = (short) ((MAJOR + FIRST + SECOND + THRID) % 256);
				// ���������ֽ�����

				byte[] sbyte = {(byte) TYPE0, (byte) TYPE1, (byte) MAJOR, (byte) FIRST,
						(byte) SECOND, (byte) THRID, (byte) CHECKSUM,
						(byte) 0xbb };
				MainActivity.���_ָ�� = false;
				 while(!MainActivity.���_ָ��)             //�ȴ�����ָ�����
				{
					 bOutputStream.write(sbyte, 0, sbyte.length);    //100          ///��������
					 delay(200);
					 if(���� == true){���� = false;break;}
				}
				delay(300);
				bOutputStream.flush();                                    ///��ջ���������
				Clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*************************************************����**********************************************************/
	public void ack(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0xff;
		FIRST = 0x00;
		SECOND =0x00;
		THRID = 0x00;
		send();
	}
	public void ��������(String x,String y)
	{
		TYPE1 = 0x06;
		if(x == "�������")
		{
			MAJOR = 0x20;
			FIRST = 0x01;
		}
		else if(x == "�ض�����")
		{
			MAJOR = 0x10;
			if(y == "��������")	FIRST = 0x01;
			else if(y == "����ת��")	FIRST = 0x02;
			else if(y == "��ֹ��ת")	FIRST = 0x03;
			else if(y == "����ת��")	FIRST = 0x04;
			else if(y == "��ֹ��ת")	FIRST = 0x05;
			else if(y == "ԭ�ص�ͷ")	FIRST = 0x06;
		}
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ���ߵ�()
	{
		TYPE1 = 0x0a;
		MAJOR = 0x01;
		FIRST = 0x01;
		SECOND =0x00;
		THRID = 0x00;
		send();
	}
	public void ��բ() {
		TYPE1 = 0x03;
		MAJOR = 0x01;
		FIRST = 0x01;
		SECOND = 0x00;
		THRID = 0x00;
		delay(300);���� = true;
		send();
	}

	public void ����(char y,int i,long l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x03;
		FIRST = (short) i;
		SECOND = (short) l;
		THRID = 0x00;
		send();
	}

	public void ǰ��(char y,int i,long l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x02;
		FIRST = (short) i;
		SECOND = (short) l;
		THRID = 0x00;
		send();
	}
	public void ��ת(char y,int i,int l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x04;
		FIRST = (short)i;
		SECOND = (short)l;
		THRID = 0x00;
		send();
	}

	public void Ѱ��(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x06;
		FIRST = 0x46;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void LED(String x) {
		FIRST = 0X00;
		SECOND = 0x00;
		THRID = 0x00;
		TYPE1 = 0x04;
		if(x=="��ʱ��")
			{MAJOR = 0x03;
			FIRST = 0x01;}
		else if(x=="��ʱ��")
			{MAJOR = 0x03;
			FIRST = 0x00;}
		else if(x=="��ʱ����")
			{MAJOR = 0x03;
			FIRST = 0x02;}
		else if((boolean)x.subSequence(1, 3).equals("��ʾ"))  //�1��ʾ111111����2��ʾ111111��
			{MAJOR = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(0,1),16).toString());
			FIRST = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(3,5),16).toString());
			SECOND = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(5,7),16).toString());
			THRID = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(7,9),16).toString());
			}
		else if((boolean)x.subSequence(0, 4).equals("��ʾ����"))
			{
			while(x.length() < 7)
			x="��ʾ����"+0+x.subSequence(4, x.length());
			MAJOR = 0X04;
			FIRST = 0X00;
			SECOND = (short) Integer.parseInt((String) x.subSequence(4,5));
			THRID = (short) Integer.parseInt((String) x.subSequence(5,7));
			}
		delay(300);
		send();
	}

	public void ������ʾת��(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x08;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ������ʾλ��(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x09;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = 0x00;
		send();
	}

	public void ��������1(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x10;
		FIRST = (byte)  0xff;
		SECOND = (byte) 0x13;
		THRID = (byte) 0x01;
		send();
	}

	public void ��������2(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x11;
		FIRST = (byte) 0x00;
		SECOND = (byte) 0x00;
		THRID = (byte) 0x00;
		send();
	}
// 0xff
//	0x11	����ʮλ	  	�����λ		0x00	0x00

//	0x12	0x01	��Ϊ0x00	����			//	0x13	0x01	��Ϊ0x00	��ɫ
//			0x02	��Ϊ0x00	Բ��			//			0x02	��Ϊ0x00	��ɫ
//			0x03	��Ϊ0x00	������		//			0x03	��Ϊ0x00	��ɫ
//			0x04	��Ϊ0x00	����			//			0x04	��Ϊ0x00	��ɫ
//			0x05	��Ϊ0x00	����			//			0x05	��Ϊ0x00	��ɫ
//			0x06	��Ϊ0x00	��ͼ			//			0x06	��Ϊ0x00	��ɫ
//			0x07	��Ϊ0x00	��ͼ			//			0x07	��Ϊ0x00	��ɫ
//			0x08	��Ϊ0x00	����ͼ		//			0x08	��Ϊ0x00	��ɫ

//	0x14	0x01	��Ϊ0x00	������¹ʣ�������		0x02	��Ϊ0x00	ǰ��ʩ����������

	public void ����(char y) { //���ܺ���
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x12;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void ת���(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		FIRST = 0x00;
		SECOND = 0x00;
		if(x=="��")
			{FIRST = 0x01;
			SECOND = 0x00;}
		if(x=="��")
			{FIRST = 0x00;
			SECOND = 0x01;}
		MAJOR = 0x20;
		THRID = 0x00;
		send();
	}


	public void BEEP(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x30;
		FIRST = 0x01;
		if(x=="��")
			FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void beep(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x31;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void ��ǰ����(char y ,String x) {
		if(y == 'C')                                 ///�༭Э��
			TYPE1 = 0x02;
		cur_xy = x;
		MAJOR = 0x35;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = (short) (x.charAt(2)-'0');
		send();
	}

	public void Ŀ������(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		cur_xy = x;
		MAJOR = 0x36;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = (short) (x.charAt(2)-'0');
		if(x.charAt(0)>'A'&&x.charAt(0)<'Z')
			FIRST=(short) (FIRST+32);
		send();
	}

	public void mask_M(char y,String x) {		//��ͼ����
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x38;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = (short) (x.charAt(2)-'0');
		send();
	}
	public void dir_change(char y,String x) {		//�������߿���
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x39;
		FIRST = (short) (x.charAt(0)-'0');			//������ֹ
		SECOND = (short) (x.charAt(1)-'0');			//����
		send();
	}


	public void ˫ɫ��(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		if(x == "��")
			FIRST = 0x55;
		if(x == "��")
			FIRST = 0xaa;
		MAJOR = 0x40;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void ��Ƭ(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		if(x == "��")
			MAJOR = 0x50;
		if(x == "��")
			MAJOR = 0x51;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}



	public void ���ƺ���(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x54;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void �⵵λ(char y,int x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x60;
		FIRST = (short)x;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void �⵵(char y,int x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = (short) (0x60+x);
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void etc��(char y) {			//i=1��
		if(y == 'C')
			FIRST = 0x02;
		else
			FIRST = 0xaa;
		TYPE1 = 0xAA;
		MAJOR = 0x70;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void Clear() {
		TYPE1 = 0xAA;
		MAJOR = 0x09;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
	}

	public void ����1(char y,String x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x71;
		FIRST =(short) x.charAt(0);
		SECOND =(short) x.charAt(1);
		THRID = (short) x.charAt(2);
		send();
	}

	public void ����2(char y,String x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x72;
		FIRST = (short) x.charAt(3);
		SECOND = (short) x.charAt(4);
		THRID = (short) x.charAt(5);
		send();
	}

	public void ͨѶ3(char y,int x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x73;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void rfid��(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x75;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ״̬(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x80;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void rfid(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x81;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ���ݻش�(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x82;
		if(x == "λ��")
			FIRST = 0x01;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ��λ�ش�(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x83;
        FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void ��������(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x90;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void OK() {
		MAJOR = 0xFF;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		���� = true;
		send();
	}


	/*************************************************************************************************************/

	/*************************************************�Ѿ�ʹ�õ���ָ��*********************************************/
	/*01-07 09 12 20 30 31 35 36 40 50-53 60-63 70*/
	/*************************************************************************************************************/

    //��ͨ����Ϣ����


	//////////////////////////��������//////////////////////////////////////////////
	public void send_voice(byte[] textbyte) {
		try {
			// ���������ֽ�����
			if (socket != null && !socket.isClosed()) {
				MainActivity.���_ָ�� = false;
				 while(!MainActivity.���_ָ��)             //�ȴ�����ָ�����
				{
					 bOutputStream.write(textbyte, 0, textbyte.length);        ///�����ı�
					 delay(200);
				}
				delay(1000);
				bOutputStream.flush();                                    ///��ջ���������

			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void TFT_control(String x)
	{
		TYPE1 = 0x0b;
		FIRST = 0X00;
		SECOND = 0x00;
		THRID = 0x00;
		if(x=="��ʱ��"){
			MAJOR = 0x30;
			FIRST = 0x01;
		}
		else if(x=="��ʱ��"){
			MAJOR = 0x30;
			FIRST = 0x00;
		}
		else if((boolean)x.subSequence(0, 4).equals("ָ��ͼƬ"))
		{
			MAJOR = 0x10;
			FIRST = 0x00;
			SECOND=(short) Integer.parseInt(Integer.valueOf((String) x.subSequence(4,6),16).toString());
			THRID = 0x00;
		}
		else if(x=="ͼƬ�Ϸ�"){
			MAJOR = 0x10;
			FIRST = 0x01;
		}
		else if(x=="ͼƬ�·�"){
			MAJOR = 0x10;
			FIRST = 0x02;
		}
		else if(x=="ͼƬ�Զ�"){
			MAJOR = 0x10;
			FIRST = 0x03;
		}
		else if((boolean)x.subSequence(0, 4).equals("��ʾ����"))  //�����abc123��
		{
			MAJOR = 0x20;
			FIRST =(short) x.charAt(4);
			SECOND =(short) x.charAt(5);
			THRID = (short) x.charAt(6);
			send();
			TYPE1 = 0x0b;
			MAJOR = 0x21;
			FIRST =(short) x.charAt(7);
			SECOND =(short) x.charAt(8);
			THRID = (short) x.charAt(9);
		}
		else if(x=="��ʱ����"){
			MAJOR = 0x30;
			FIRST = 0x02;
		}
		else if((boolean)x.subSequence(0, 4).equals("��ʾ����"))
		{
			MAJOR = 0x40;
			FIRST = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(4,6),16).toString());
			SECOND = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(6,8),16).toString());
			THRID = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(8,10),16).toString());
		}
		else if((boolean)x.subSequence(0, 4).equals("��ʾ����"))
		{
			while(x.length() < 7)
				x="��ʾ����"+0+x.subSequence(4, x.length());
			MAJOR = 0X50;
			FIRST = 0X00;
			SECOND = (short) Integer.parseInt((String) x.subSequence(4,5),16);
			THRID = (short) Integer.parseInt((String) x.subSequence(5,7),16);
		}
		send();
	}


	public byte[] bytesend(byte[] sbyte) {
		byte[] textbyte = new byte[sbyte.length + 5];
		textbyte[0] = (byte) 0xFD;
		textbyte[1] = (byte) (((sbyte.length + 2) >> 8) & 0xff);
		textbyte[2] = (byte) ((sbyte.length + 2) & 0xff);
		textbyte[3] = 0x01;// �ϳ���������
		textbyte[4] = (byte) 0x01;// �����ʽ
		for (int i = 0; i < sbyte.length; i++) {
			textbyte[i + 5] = sbyte[i];
		}
		return textbyte;
	}

	public void VoiceText(String src) {
		delay(300);
		try {
			byte[] sbyte = bytesend(src.getBytes("GBK"));
			send_voice(sbyte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}


	/////////////////////////��������////////////////////////////////////////////////

	public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}




}
