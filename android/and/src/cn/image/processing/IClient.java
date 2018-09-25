package cn.image.processing;

/*
 * 文件编写作者
 * Lzz
 * 
 * 日期
 * 2016年11月19日
 */
import java.io.UnsupportedEncodingException;

import cn.gdit.goldsite.Client;
import cn.gdit.goldsite.MainActivity;
import cn.image.processing.LicensePlateRecognition;
import cn.image.processing.QRcode;
import cn.image.processing.TrafficSignRecognition;
import cn.image.processing.ColorShapeRecognition;

public class IClient {
	public Client client = new Client();
	public String string = "";
	
	public void 交通灯(){
		Ready();
		TrafficSignRecognition 交通灯 =new  TrafficSignRecognition (MainActivity.bitmap1);
		while(!交通灯.完成标志位);
		VoiceText(交通灯.交通灯信号);delay(500);
		if(交通灯.交通灯信号.equals("请右转")||交通灯.交通灯信号.equals("禁止左转"))
				client.dir_change('z',"12");
		else 
		if(交通灯.交通灯信号.equals("请左转")||交通灯.交通灯信号.equals("禁止右转"))
				client.dir_change('z',"02");
		else
				client.dir_change('z',"13");	
		
		
		
//		switch((int)(Client.cur_xy.charAt(2)-'0'))
//		{
//			case 1:
//				if(交通灯.交通灯信号.equals("请右转")||交通灯.交通灯信号.equals("禁止左转"))
//						client.目标坐标('Z', "f20");
//				else 
//				if(交通灯.交通灯信号.equals("请左转")||交通灯.交通灯信号.equals("禁止右转"))
//						client.目标坐标('Z', "b20");
//				else
//						client.目标坐标('Z', "d40");
//				break;
//
//			case 2:
//				if(交通灯.交通灯信号.equals("请右转")||交通灯.交通灯信号.equals("禁止左转"))
//						client.目标坐标('Z', "f60");
//				else 
//				if(交通灯.交通灯信号.equals("请左转")||交通灯.交通灯信号.equals("禁止右转"))
//						client.目标坐标('Z', "f20");
//				else
//						client.目标坐标('Z', "d40");
//				break;
//
//			case 3:
//				if(交通灯.交通灯信号.equals("请右转")||交通灯.交通灯信号.equals("禁止左转"))
//						client.目标坐标('Z', "b60");
//				else 
//				if(交通灯.交通灯信号.equals("请左转")||交通灯.交通灯信号.equals("禁止右转"))
//						client.目标坐标('Z', "f60");
//				else
//						client.目标坐标('Z', "d40");
//				break;
//
//			case 4:
//				if(交通灯.交通灯信号.equals("请右转")||交通灯.交通灯信号.equals("禁止左转"))
//						client.目标坐标('Z', "b20");
//				else 
//				if(交通灯.交通灯信号.equals("请左转")||交通灯.交通灯信号.equals("禁止右转"))
//						client.目标坐标('Z', "b60");
//				else
//						client.目标坐标('Z', "d40");
//				break;
//
//		}
		}
	
	public void 二维码前(){
		Ready();
		int x = 0;
		for(int i = 0;i<10; i++)
		{
		delay(1000);
		QRcode 二维码=new QRcode (MainActivity.bitmap1);
		while(!二维码.完成标志位);
		if(二维码.二维码结果.length() >1)
		{
//		MainActivity.m1 = Integer.parseInt(二维码.二维码结果.substring(8,9));
			MainActivity.m4 = 二维码.二维码结果;
			MainActivity.m4z = 二维码.二维码结果.substring(0, 2)+'0';
			MainActivity.m4c = 二维码.二维码结果.substring(2, 4)+'0';
		VoiceText(二维码.二维码结果);delay(500);
		break;
		}
		if(i%2 == 1)
		{
		client.后退('Z', 80, 10);
		client.ack('Z');
		x++;
		}
		}
		if(x != 0)
		for(;x>0;x--)
		{
		client.前进('Z', 80, 11);
		client.ack('Z');
		}
		}
	
	public void 二维码侧(){
		int x,y=0;
		Ready();
		for(x = 0;x<8; x++)
		{
		delay(2000);
		QRcode 二维码=new QRcode (MainActivity.bitmap1);
		while(!二维码.完成标志位);
		if(二维码.二维码结果.length() >1)
		{
			VoiceText(二维码.二维码结果);delay(500);
//			char[] a1 = new char[7];
//			int[] a0 = new int[7];
//			if(二维码.二维码结果.length() == 6)
//				二维码.二维码结果=0+二维码.二维码结果;	
//
//			if(二维码.二维码结果.length() == 7)
//			for(int i=0;i<7;i++)
//			a1[i]=二维码.二维码结果.charAt(i);
//
//
//			for(int i=0;i<7;i++)
//			a0[i]=a1[i]-'0';
//
//			JY.HMM(a0);
//			MainActivity.m4z = Integer.toHexString(Integer.parseInt(JY.SZ_string[0], 2)).substring(0,2) + 0;
//			MainActivity.m4c = Integer.toHexString(Integer.parseInt(JY.SZ_string[0], 2)).substring(2,4) + 0;
			MainActivity.m2 = 二维码.二维码结果;
			client.beep('Z');break;
		}
		if(x%2 == 1)
		{
		client.前进('Z', 80, 5);
		client.ack('Z');
		y++;
		}
		}
		if(y != 0)
		for(;y>0;y--)
		{
		client.后退('Z', 80, 3);
		client.ack('Z');
		}
		}
	
	public void 车牌() {
		Ready();
		LicensePlateRecognition 车牌 = new LicensePlateRecognition(MainActivity.bitmap1);
		while(!车牌.完成标志位);
		VoiceText("国"+车牌.识别结果);delay(500);
		MainActivity.m3 = 车牌.识别结果;
		}	
	
	public void 形状() {
		Ready();
		ColorShapeRecognition 形状 = new ColorShapeRecognition(MainActivity.bitmap1);
		while(!形状.完成标志位);
		VoiceText(ColorShapeRecognition.Result);delay(ColorShapeRecognition.Result.length()/3*1000+2000);
		if(ColorShapeRecognition.Result1.length() == 6)
		MainActivity.m3 = ColorShapeRecognition.Result1;
		}	
	
	
	//////////////////////////语音播报//////////////////////////////////////////////

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

		try {
			byte[] sbyte = bytesend(src.getBytes("GBK"));
			client.send_voice(sbyte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

	/////////////////////////语音播报////////////////////////////////////////////////
	public void Ready(){
		while(!Client.connect_flag);
		delay(2000);
	}

	
	public void delay(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}