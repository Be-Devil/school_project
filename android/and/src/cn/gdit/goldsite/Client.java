package cn.gdit.goldsite;

/*
 * 文件编写作者
 * Lzz
 *
 * 日期
 * 2016年11月19日
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
	static public boolean 锁定 = false;
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
				socket.close();                                                        ///关闭套接字
				bInputStream.close();                                                  ///关闭输入流
				bOutputStream.close();                                                 ///关闭输出流
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void connect(String IP) {
		try {
			socket = new Socket(IP, port);                                    ///连接小车
			bInputStream = new DataInputStream(socket.getInputStream());      ///输入流
			bOutputStream = new DataOutputStream(socket.getOutputStream());   ///输出流
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
				// 发送数据字节数组

				byte[] sbyte = {(byte) TYPE0, (byte) TYPE1, (byte) MAJOR, (byte) FIRST,
						(byte) SECOND, (byte) THRID, (byte) CHECKSUM,
						(byte) 0xbb };
				MainActivity.完成_指令 = false;
				 while(!MainActivity.完成_指令)             //等待上条指令完成
				{
					 bOutputStream.write(sbyte, 0, sbyte.length);    //100          ///发送数组
					 delay(200);
					 if(锁定 == true){锁定 = false;break;}
				}
				delay(300);
				bOutputStream.flush();                                    ///清空缓冲区数据
				Clear();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/*************************************************主车**********************************************************/
	public void ack(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0xff;
		FIRST = 0x00;
		SECOND =0x00;
		THRID = 0x00;
		send();
	}
	public void 语音控制(String x,String y)
	{
		TYPE1 = 0x06;
		if(x == "随机语音")
		{
			MAJOR = 0x20;
			FIRST = 0x01;
		}
		else if(x == "特定语音")
		{
			MAJOR = 0x10;
			if(y == "语音唤醒")	FIRST = 0x01;
			else if(y == "向右转弯")	FIRST = 0x02;
			else if(y == "禁止右转")	FIRST = 0x03;
			else if(y == "向左转弯")	FIRST = 0x04;
			else if(y == "禁止左转")	FIRST = 0x05;
			else if(y == "原地掉头")	FIRST = 0x06;
		}
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void 无线电()
	{
		TYPE1 = 0x0a;
		MAJOR = 0x01;
		FIRST = 0x01;
		SECOND =0x00;
		THRID = 0x00;
		send();
	}
	public void 道闸() {
		TYPE1 = 0x03;
		MAJOR = 0x01;
		FIRST = 0x01;
		SECOND = 0x00;
		THRID = 0x00;
		delay(300);锁定 = true;
		send();
	}

	public void 后退(char y,int i,long l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x03;
		FIRST = (short) i;
		SECOND = (short) l;
		THRID = 0x00;
		send();
	}

	public void 前进(char y,int i,long l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x02;
		FIRST = (short) i;
		SECOND = (short) l;
		THRID = 0x00;
		send();
	}
	public void 左转(char y,int i,int l) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x04;
		FIRST = (short)i;
		SECOND = (short)l;
		THRID = 0x00;
		send();
	}

	public void 寻迹(char y) {
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
		if(x=="计时开")
			{MAJOR = 0x03;
			FIRST = 0x01;}
		else if(x=="计时关")
			{MAJOR = 0x03;
			FIRST = 0x00;}
		else if(x=="计时清零")
			{MAJOR = 0x03;
			FIRST = 0x02;}
		else if((boolean)x.subSequence(1, 3).equals("显示"))  //填“1显示111111”或“2显示111111”
			{MAJOR = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(0,1),16).toString());
			FIRST = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(3,5),16).toString());
			SECOND = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(5,7),16).toString());
			THRID = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(7,9),16).toString());
			}
		else if((boolean)x.subSequence(0, 4).equals("显示距离"))
			{
			while(x.length() < 7)
			x="显示距离"+0+x.subSequence(4, x.length());
			MAJOR = 0X04;
			FIRST = 0X00;
			SECOND = (short) Integer.parseInt((String) x.subSequence(4,5));
			THRID = (short) Integer.parseInt((String) x.subSequence(5,7));
			}
		delay(300);
		send();
	}

	public void 立体显示转弯(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x08;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void 立体显示位置(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x09;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = 0x00;
		send();
	}

	public void 红外数据1(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x10;
		FIRST = (byte)  0xff;
		SECOND = (byte) 0x13;
		THRID = (byte) 0x01;
		send();
	}

	public void 红外数据2(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x11;
		FIRST = (byte) 0x00;
		SECOND = (byte) 0x00;
		THRID = (byte) 0x00;
		send();
	}
// 0xff
//	0x11	距离十位	  	距离个位		0x00	0x00

//	0x12	0x01	均为0x00	矩形			//	0x13	0x01	均为0x00	红色
//			0x02	均为0x00	圆形			//			0x02	均为0x00	绿色
//			0x03	均为0x00	三角形		//			0x03	均为0x00	蓝色
//			0x04	均为0x00	菱形			//			0x04	均为0x00	黄色
//			0x05	均为0x00	梯形			//			0x05	均为0x00	紫色
//			0x06	均为0x00	饼图			//			0x06	均为0x00	青色
//			0x07	均为0x00	靶图			//			0x07	均为0x00	黑色
//			0x08	均为0x00	条形图		//			0x08	均为0x00	白色

//	0x14	0x01	均为0x00	隧道有事故，请绕行		0x02	均为0x00	前方施工，请绕行

	public void 红外(char y) { //万能红外
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x12;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void 转向灯(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		FIRST = 0x00;
		SECOND = 0x00;
		if(x=="左")
			{FIRST = 0x01;
			SECOND = 0x00;}
		if(x=="右")
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
		if(x=="关")
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

	public void 当前坐标(char y ,String x) {
		if(y == 'C')                                 ///编辑协议
			TYPE1 = 0x02;
		cur_xy = x;
		MAJOR = 0x35;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = (short) (x.charAt(2)-'0');
		send();
	}

	public void 目标坐标(char y,String x) {
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

	public void mask_M(char y,String x) {		//地图掩码
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x38;
		FIRST = (short) x.charAt(0);
		SECOND = (short) x.charAt(1);
		THRID = (short) (x.charAt(2)-'0');
		send();
	}
	public void dir_change(char y,String x) {		//方向行走控制
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x39;
		FIRST = (short) (x.charAt(0)-'0');			//允许，禁止
		SECOND = (short) (x.charAt(1)-'0');			//方向
		send();
	}


	public void 双色灯(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		if(x == "红")
			FIRST = 0x55;
		if(x == "绿")
			FIRST = 0xaa;
		MAJOR = 0x40;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void 照片(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		if(x == "上")
			MAJOR = 0x50;
		if(x == "下")
			MAJOR = 0x51;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}



	public void 车牌红外(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x54;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void 光档位(char y,int x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x60;
		FIRST = (short)x;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void 光档(char y,int x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = (short) (0x60+x);
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void etc打开(char y) {			//i=1打开
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

	public void 车牌1(char y,String x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x71;
		FIRST =(short) x.charAt(0);
		SECOND =(short) x.charAt(1);
		THRID = (short) x.charAt(2);
		send();
	}

	public void 车牌2(char y,String x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x72;
		FIRST = (short) x.charAt(3);
		SECOND = (short) x.charAt(4);
		THRID = (short) x.charAt(5);
		send();
	}

	public void 通讯3(char y,int x) {
		if(y == 'C')
		TYPE1 = 0x02;
		MAJOR = 0x73;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}

	public void rfid打开(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x75;
		FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void 状态(char y) {
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
	public void 数据回传(char y,String x) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x82;
		if(x == "位置")
			FIRST = 0x01;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void 档位回传(char y) {
		if(y == 'C')
			TYPE1 = 0x02;
		MAJOR = 0x83;
        FIRST = 0x00;
		SECOND = 0x00;
		THRID = 0x00;
		send();
	}
	public void 语音函数(char y) {
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
		锁定 = true;
		send();
	}


	/*************************************************************************************************************/

	/*************************************************已经使用的主指令*********************************************/
	/*01-07 09 12 20 30 31 35 36 40 50-53 60-63 70*/
	/*************************************************************************************************************/

    //交通灯信息反馈


	//////////////////////////语音播报//////////////////////////////////////////////
	public void send_voice(byte[] textbyte) {
		try {
			// 发送数据字节数组
			if (socket != null && !socket.isClosed()) {
				MainActivity.完成_指令 = false;
				 while(!MainActivity.完成_指令)             //等待上条指令完成
				{
					 bOutputStream.write(textbyte, 0, textbyte.length);        ///发送文本
					 delay(200);
				}
				delay(1000);
				bOutputStream.flush();                                    ///清空缓冲区数据

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
		if(x=="计时开"){
			MAJOR = 0x30;
			FIRST = 0x01;
		}
		else if(x=="计时关"){
			MAJOR = 0x30;
			FIRST = 0x00;
		}
		else if((boolean)x.subSequence(0, 4).equals("指定图片"))
		{
			MAJOR = 0x10;
			FIRST = 0x00;
			SECOND=(short) Integer.parseInt(Integer.valueOf((String) x.subSequence(4,6),16).toString());
			THRID = 0x00;
		}
		else if(x=="图片上翻"){
			MAJOR = 0x10;
			FIRST = 0x01;
		}
		else if(x=="图片下翻"){
			MAJOR = 0x10;
			FIRST = 0x02;
		}
		else if(x=="图片自动"){
			MAJOR = 0x10;
			FIRST = 0x03;
		}
		else if((boolean)x.subSequence(0, 4).equals("显示车牌"))  //填“车牌abc123”
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
		else if(x=="计时清零"){
			MAJOR = 0x30;
			FIRST = 0x02;
		}
		else if((boolean)x.subSequence(0, 4).equals("显示数据"))
		{
			MAJOR = 0x40;
			FIRST = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(4,6),16).toString());
			SECOND = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(6,8),16).toString());
			THRID = (short) Integer.parseInt(Integer.valueOf((String) x.subSequence(8,10),16).toString());
		}
		else if((boolean)x.subSequence(0, 4).equals("显示距离"))
		{
			while(x.length() < 7)
				x="显示距离"+0+x.subSequence(4, x.length());
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
		textbyte[3] = 0x01;// 合成语音命令
		textbyte[4] = (byte) 0x01;// 编码格式
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


	/////////////////////////语音播报////////////////////////////////////////////////

	public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}




}
