package cn.image.processing;

/*
 * �ļ���д����
 * Lzz
 * 
 * ����
 * 2016��11��19��
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
	
	public void ��ͨ��(){
		Ready();
		TrafficSignRecognition ��ͨ�� =new  TrafficSignRecognition (MainActivity.bitmap1);
		while(!��ͨ��.��ɱ�־λ);
		VoiceText(��ͨ��.��ͨ���ź�);delay(500);
		if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
				client.dir_change('z',"12");
		else 
		if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
				client.dir_change('z',"02");
		else
				client.dir_change('z',"13");	
		
		
		
//		switch((int)(Client.cur_xy.charAt(2)-'0'))
//		{
//			case 1:
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "f20");
//				else 
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "b20");
//				else
//						client.Ŀ������('Z', "d40");
//				break;
//
//			case 2:
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "f60");
//				else 
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "f20");
//				else
//						client.Ŀ������('Z', "d40");
//				break;
//
//			case 3:
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "b60");
//				else 
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "f60");
//				else
//						client.Ŀ������('Z', "d40");
//				break;
//
//			case 4:
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "b20");
//				else 
//				if(��ͨ��.��ͨ���ź�.equals("����ת")||��ͨ��.��ͨ���ź�.equals("��ֹ��ת"))
//						client.Ŀ������('Z', "b60");
//				else
//						client.Ŀ������('Z', "d40");
//				break;
//
//		}
		}
	
	public void ��ά��ǰ(){
		Ready();
		int x = 0;
		for(int i = 0;i<10; i++)
		{
		delay(1000);
		QRcode ��ά��=new QRcode (MainActivity.bitmap1);
		while(!��ά��.��ɱ�־λ);
		if(��ά��.��ά����.length() >1)
		{
//		MainActivity.m1 = Integer.parseInt(��ά��.��ά����.substring(8,9));
			MainActivity.m4 = ��ά��.��ά����;
			MainActivity.m4z = ��ά��.��ά����.substring(0, 2)+'0';
			MainActivity.m4c = ��ά��.��ά����.substring(2, 4)+'0';
		VoiceText(��ά��.��ά����);delay(500);
		break;
		}
		if(i%2 == 1)
		{
		client.����('Z', 80, 10);
		client.ack('Z');
		x++;
		}
		}
		if(x != 0)
		for(;x>0;x--)
		{
		client.ǰ��('Z', 80, 11);
		client.ack('Z');
		}
		}
	
	public void ��ά���(){
		int x,y=0;
		Ready();
		for(x = 0;x<8; x++)
		{
		delay(2000);
		QRcode ��ά��=new QRcode (MainActivity.bitmap1);
		while(!��ά��.��ɱ�־λ);
		if(��ά��.��ά����.length() >1)
		{
			VoiceText(��ά��.��ά����);delay(500);
//			char[] a1 = new char[7];
//			int[] a0 = new int[7];
//			if(��ά��.��ά����.length() == 6)
//				��ά��.��ά����=0+��ά��.��ά����;	
//
//			if(��ά��.��ά����.length() == 7)
//			for(int i=0;i<7;i++)
//			a1[i]=��ά��.��ά����.charAt(i);
//
//
//			for(int i=0;i<7;i++)
//			a0[i]=a1[i]-'0';
//
//			JY.HMM(a0);
//			MainActivity.m4z = Integer.toHexString(Integer.parseInt(JY.SZ_string[0], 2)).substring(0,2) + 0;
//			MainActivity.m4c = Integer.toHexString(Integer.parseInt(JY.SZ_string[0], 2)).substring(2,4) + 0;
			MainActivity.m2 = ��ά��.��ά����;
			client.beep('Z');break;
		}
		if(x%2 == 1)
		{
		client.ǰ��('Z', 80, 5);
		client.ack('Z');
		y++;
		}
		}
		if(y != 0)
		for(;y>0;y--)
		{
		client.����('Z', 80, 3);
		client.ack('Z');
		}
		}
	
	public void ����() {
		Ready();
		LicensePlateRecognition ���� = new LicensePlateRecognition(MainActivity.bitmap1);
		while(!����.��ɱ�־λ);
		VoiceText("��"+����.ʶ����);delay(500);
		MainActivity.m3 = ����.ʶ����;
		}	
	
	public void ��״() {
		Ready();
		ColorShapeRecognition ��״ = new ColorShapeRecognition(MainActivity.bitmap1);
		while(!��״.��ɱ�־λ);
		VoiceText(ColorShapeRecognition.Result);delay(ColorShapeRecognition.Result.length()/3*1000+2000);
		if(ColorShapeRecognition.Result1.length() == 6)
		MainActivity.m3 = ColorShapeRecognition.Result1;
		}	
	
	
	//////////////////////////��������//////////////////////////////////////////////

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

		try {
			byte[] sbyte = bytesend(src.getBytes("GBK"));
			client.send_voice(sbyte);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	

	/////////////////////////��������////////////////////////////////////////////////
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