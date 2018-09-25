#include <stm32f10x.h>  
#include <test.h>
#include "infrared.h"
#include "xj.h"
#include "usart.h"
#include "hw.h"
#include "csb.h"
#include "bh1750.h"
#include "74hc595.h"
#include "led.h"
#include "move.h"

u8 license_plate1[3] = {0x00};
u8 license_plate2[3] = {0x00};



/******************���յ���***********************/

 u8 H_1[4]={0x00,0xFF,0x0C,~(0x0C)};	 //���ռ�1��
 u8 H_2[4]={0x00,0xFF,0x18,~(0x18)};	 //���ռ�2��
 u8 H_3[4]={0x00,0xFF,0x5E,~(0x5E)};	 //���ռ�3��
 
void hw_light(int light_ed){
	int ini_light=0;
	int light[3]={0,0,0};
	int light_n=1;
	int i,j;
	ini_light=Dispose();
	for(i=0;i<3;i++){
		Transmition(H_1,4);			 //����
		delay_ms(1000);
		light[i]=Dispose();	   		//������λ			
	}
	for(j=0;j<3;j++)
		if(ini_light>light[j])light_n++;
	
	if(light_n<light_ed) light_n+=4;
	switch(light_n-light_ed){
		
		case 0:
		Transmition(H_1,4);
		LED2=!LED2;
		break;
		
		case 2:
		Transmition(H_3,4);
		LED2=!LED2;
		break;
		
		case 3:
		Transmition(H_2,4);
		LED2=!LED2;
		break;
	}
	
}

/************���ռ�⺯��*************/
u8 hw_check_data = 1;
u8 hw_light_check(){				 //�����յ�λ
	int ini_light=0;
	int light[3]={0,0,0};
	int light_n=0;
	int i,j;
	ini_light=Dispose();
	for(i=0;i<3;i++){
		Transmition(H_1,4);			 //����
		delay_ms(1300);
		light[i]=Dispose();	   		//������λ			
	}
	Transmition(H_1,4);			 	//����ԭ��
	for(j=0;j<3;j++)
		if(ini_light>light[j])light_n++;

	return ++light_n;
}





/*************************������ʾ***************************************/
/*
����ָ��ṹ
0xFF|0xXX|XX|XX|XX|XX
��ʼλ|ģʽ|���ݡ�1��|���ݡ�2��|���ݡ�3��|���ݡ�4��
 */
 /**************************************************
ģʽ˵��
ģʽ					˵��
0x20|����ǰ��λ������Ϣģʽ
0x10|���պ���λ������Ϣ����λ������Ϣģʽ������ʾ��
0x11|��ʾ����ģʽ
0x12|��ʾͼ��ģʽ
0x13|��ʾ��ɫģʽ
0x14|��ʾ·��ģʽ
0x15|��ʾĬ��ģʽ
*/
/***************************************************
������ʾģʽ˵��
ģʽ|���ݡ�1��|���ݡ�2��|���ݡ�3��|���ݡ�4��
0x20|���ơ�1��|���ơ�2��|���ơ�3��|���ơ�4��
0x10|���ơ�5��|���ơ�6��|������|������					 
˵�����ڳ���ģʽ�£�������Ϣ�������������ַ��͵�ͼ��ĳ��λ�õ����꣬���˸��ַ���ע�⣺������Ϣ��ʽΪ�ַ�����ʽ��
*/
/***************************************************
����ģʽ˵��
ģʽ|���ݡ�1��|���ݡ�2��|���ݡ�3��|���ݡ�4��
0x11|����ʮλ|�����λ|0x00|0x00
˵�����ھ���ģʽ�£����ݡ�1��~���ݡ�2��Ϊ��Ҫ��ʾ�ľ�����Ϣ��ע��������ʾ��ʽΪʮ���ƣ�������ΪΪ0x00����������
*/
/***************************************************
ͼ��ģʽ��˵��
ģʽ|���ݡ�1��|���ݡ�2��~���ݡ�4��|˵��
	 0x01		   ����
	 0x02		   Բ��
	 0x03		   ������
	 0x04		   ����
0x12|0x05|��Ϊ0x00|����
 	 0x06		   ��ͼ
	 0x07		   ��ͼ
	 0x08		   ����ͼ
*/
/***************************************************
��ɫģʽ˵��
ģʽ|���ݡ�1��|���ݡ�2��~���ݡ�4��|˵��
	 0x01		   ��ɫ
	 0x02		   ��ɫ
	 0x03		   ��ɫ
	 0x04		   ��ɫ
0x13|0x05|��Ϊ0x00|��ɫ
 	 0x06		   ��ɫ
	 0x07		   ��ɫ
	 0x08		   ��ɫ
*/
/***************************************************
·��ģʽ˵��
ģʽ|���ݡ�1��|���ݡ�2��~���ݡ�4��|˵��
0x14|0x01	  |��Ϊ0x00			  |ǰ�����¹ʣ�������
0x14|0x02	  |��Ϊ0x00			  |ǰ��ʩ����������
*/
/***************************************************
Ĭ��ģʽ˵��
ģʽ|���ݡ�1��|���ݡ�2��~���ݡ�4��|˵��
0x15|0x01|0x00|��ʾĬ����Ϣ	  
*/
//void litishow(u8 da1,u8 da2,u8 da3,u8 da4,u8 da5){
//	u8 data[6];
//	data[0]=0xff;
//	data[1]=da1;
//	data[2]=da2;
//	data[3]=da3;
//	data[4]=da4;
//	data[5]=da5;
//	Track_Flag=0;MP=0;
//	Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
//	STOP();
//	Transmition(data,6);
//	delay_ms(200);	
//}
///*******************������ʾ���ƺ���������**************************/
void litishow_carnum(u8 da1,u8 da2){
	u8 data[6]={0xff,0x20,'1','2','3','4'};
	u8 data2[6]={0xff,0x10,'1','2','3','4'};
	
	data[2]=(u8)license_plate1[0];
	data[3]=(u8)license_plate1[1];
	data[4]=(u8)license_plate1[2];
	data[5]=(u8)license_plate2[0];	
	
	data2[2]=(u8)license_plate2[1];
	data2[3]=(u8)license_plate2[2];
	data2[4]=(u8)(da2+65);
	data2[5]=(u8)(da1+48);
	
	Transmition(data,6);
	delay_ms(1000);
	Transmition(data2,6);
}


/*************************************************************
************������ʾת��******************
*************************************************************/
u8 LITISHOW_X ='3'-0x30;
u8 LITISHOW_Y ='e'-0x61;

u8 already_litishow_dir=0;		//�Ѿ�������ʾ��
void litishow_dir()
{
	if(already_litishow_dir==0)
	{	
		already_litishow_dir=1;
		if( ((current_y<LITISHOW_Y)&&(current_d==1))||		
			((current_y>LITISHOW_Y)&&(current_d==3))||		//
			((current_x<LITISHOW_X)&&(current_d==2))||		//
			((current_x>LITISHOW_X)&&(current_d==4))	)
			car_half_right(RIGHT_HALF_TIME);

		else  if(	((current_y>LITISHOW_Y)&&(current_d==1))||
					((current_y<LITISHOW_Y)&&(current_d==3))||	//	
					((current_x>LITISHOW_X)&&(current_d==2))||	//
					((current_x<LITISHOW_X)&&(current_d==4))	)
			
		car_half_left(LEFT_HALF_TIME);
	}
	else if(already_litishow_dir==1)
	{
		already_litishow_dir=0;
		if( ((current_y<LITISHOW_Y)&&(current_d==1))||		
			((current_y>LITISHOW_Y)&&(current_d==3))||		//
			((current_x<LITISHOW_X)&&(current_d==2))||		//
			((current_x>LITISHOW_X)&&(current_d==4))	)
		car_left();
		
		else  if(	((current_y>LITISHOW_Y)&&(current_d==1))||
					((current_y<LITISHOW_Y)&&(current_d==3))||	//	
					((current_x>LITISHOW_X)&&(current_d==2))||	//
					((current_x<LITISHOW_X)&&(current_d==4))	)
			
		car_right();
	}	

}








