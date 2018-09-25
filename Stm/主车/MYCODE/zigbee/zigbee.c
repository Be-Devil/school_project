#include <stm32f10x.h>	   
#include "zigbee.h"
#include "xj.h"
#include "usart.h"
#include "hw.h"
#include "csb.h"
#include "led.h"
#include "delay.h"
#include "uart_my.h"

u8 voice2flag=0;	   //�����������ݿ�����־λ,
u8 voice_send_allow=0;//�������������־λ��0Ϊ��ֹ��1Ϊ����

/*************************��բ����****************************************/
u8 DZ_K[8]={0x55,0x03,0x01,0x01,0x00,0x000,0x02,0xbb};//��բ��
u8 DZ_G[8]={0x55,0x03,0x01,0x02,0x00,0x00,0x03,0xBB}; //��բ��
void DZ_KG(u8 flag){
	u8 i = 0;
	if(flag)
	{
		for(i = 0;i<3;i++)
		{
			delay_ms(10);
			send_data_zigbee(DZ_K,8); 
		}
	}
	else 
		send_data_zigbee(DZ_G,8);
}

/**************************LED��ʾ******************************************/
/*
 0x55,0x04|0xXX|0xXX,0xXX,0xXX|0xXX|0xBB
 ��ͷ	  |��ָ��|��ָ��	|У���|��β
��ָ��	0x01 ����д���һ�������
		0x02 ����д��ڶ��������
		0x03 ���뵹��ʱģʽ
		0x04 �ڶ�����ʾ���� 

��ָ��					��ָ��
0x01			���ݡ�1�������ݡ�2��|���ݡ�3�������ݡ�4��|���ݡ�5�������ݡ�6��
0x02			���ݡ�1�������ݡ�2��|���ݡ�3�������ݡ�4��|���ݡ�5�������ݡ�6��
0x03			0x00/0x01/0x02���ر�/��/�����|0x00|0x00
0x04			0x00|0x0X|0xXX
*/
void LED_show(u8 data1,u8 data2,u8 data3,u8 data4){
	u8 Data1,Data2,Data3,Data4;
	u8 jyh=0;
	u8 da[8];
	Data1=data1&0xff;
	Data2=data2&0xff;
	Data3=data3&0xff;
	Data4=data4&0xff;
	jyh=(Data1+Data2+Data3+Data4)%256;
	da[0]=0x55;
	da[1]=0x04;
	da[2]=Data1;
	da[3]=Data2;
	da[4]=Data3;
	da[5]=Data4;
	da[6]=jyh;
	da[7]=0xbb;
	send_data_zigbee(da,8);
}


/**********************************************************************
*********************TFT��ʾ������************************************
*********************************************************************/




/***********************����������ಢʾ********************************/

void csb_jl_check(u8 flag){  //��ֵ0�޷�������
	u16 csb_jl_end;
	u16 csb_jl_arr[5]={0,0,0,0,0};
	u8 t;
	u16 i,j;
	delay_ms(200);
	for(i=0;i<5;i++){
		tran();
		delay_ms(50);		
		csb_jl_arr[i]=dis;
	}
	for(i=0;i<4;i++){
		for(j=0;j<4-i;j++){
			if(csb_jl_arr[j]>csb_jl_arr[j+1]){
				t=csb_jl_arr[j+1];
				csb_jl_arr[j+1]=csb_jl_arr[j];
				csb_jl_arr[j]=t;
			}	
		}
	}
	csb_jl_end=(csb_jl_arr[1]+csb_jl_arr[2]+csb_jl_arr[3])/3;
	if(!flag)
		LED_show(0x04,0x00,csb_jl_end/100,csb_jl_end%100);//��ʾ����
	else 
		dis=csb_jl_end;
	return ;
}

//u8 arr_voice1[80]={ 0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0Xb6 ,0xBB ,0xCA ,0xAE ,//1
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xB6 ,0XFE ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC8 ,0XFD ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCB ,0XC4 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCE ,0XE5 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC1 ,0XF9 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC6 ,0XDF ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XB0 ,0XCB ,0xCA ,0xAE ,
//					};		//�����������256���ֽڣ���������˴���Ϊ250���ֽڣ�
//u8 arr_voice2[100]={0xFD ,0X00 ,0X61 ,0X01 ,0X01 ,0xB6 ,0xFE ,0xCA ,0xAE ,//1
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xB6 ,0XFE ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC8 ,0XFD ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCB ,0XC4 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCE ,0XE5 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC1 ,0XF9 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC6 ,0XDF ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XB0 ,0XCB ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xBE ,0xC5 ,0xCA ,0xAE ,//��������ʮ
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xD2 ,0xBB ,0xB0 ,0xD9 ,//������һ��
//					};		//�����������256���ֽڣ���������˴���Ϊ250���ֽڣ�
/****************����������־�����****************************************
**�ش���������		�ش�����	��������
**��ʼ���ɹ�		0x4A		оƬ��ʼ���ɹ�
**�յ���ȷ����֡	0x41		����оƬ�յ���ȷ������֡
**�յ���������֡	0x45		����оƬ�յ����������֡
**оƬæµ			0x4E		����оƬ���ںϳ�״̬
**оƬ����			0x4F		����оƬ���ڿ���״̬,�յ�״̬��ѯ����һ֡
								��һ֡���ݺϳɽ�����оƬ���ڿ���״̬
****************************************************************************/
//void save_data_voice(u8 len)  //�������²����ͺ���
//{
//	u8 i;
//	if(voice2flag==0)		  //0
//	{
//		for(i=0;i<len;i++)
//			arr_voice1[i+5]=USART1_RX_BUF[i];
//		voice2flag=1;
//	}
//	else if(voice2flag==1)	 //1
//	{
//		for(i=0;i<len;i++)
//			arr_voice1[i+5]=USART1_RX_BUF[i];
//		TIM4->CR1|=0x01; 
//	}
//	flag1=0;
//}
//
//void voice_busy(void)	 //���æµ����
//{
//	U2SendChar(0xFD);
//	U2SendChar(0x00);
//	U2SendChar(0x01);
//	U2SendChar(0x21);	
//}
//void voice_send(u8 *voice)	//������������
//{
//	u8 i;
//	U2SendChar(0xFD);
//	U2SendChar(0x00);
//	U2SendChar(0x52);
//	U2SendChar(0x01);
//	U2SendChar(0x01);
//	for(i=0;i<80;i++)
//		U2SendChar(voice[i]);
//	return ;
//}


///****************************������λ����****************************/
//u8 arr_voice_choice[12]={0xC1,0XE3,0xD2 ,0xBB ,0xB6 ,0xFE ,0xC8 ,0xFD ,0xCB ,0xC4 ,0xCE ,0xE5 } ; //012345

//u8 arr_voice_light[30]={0xB5 ,0xB1 ,0xC7 ,0xB0 ,0xD5 ,0xD5 ,0xC3 ,0xF7 ,0xB5 ,0xB3 ,	//��ǰ������
//						0xCE ,0xBB ,0xCE ,0xAA ,0xD2 ,0xBB,0xB5 ,0xB3 						//λΪ
//						}; 
//void voice_light_current(u8 num)		//��ǰ������λΪ
//{	
//	u8 i;
//	arr_voice_light[14]=arr_voice_choice[num*2]; 
//	arr_voice_light[15]=arr_voice_choice[num*2+1]; 
//	U2SendChar(0xFD);
//	U2SendChar(0x00);
//	U2SendChar(0x20);
//	U2SendChar(0x01);
//	U2SendChar(0x01);
//	for(i=0;i<30;i++)
//		U2SendChar(arr_voice_light[i]);
//	return ;
//}

//u8 arr_voice_light_setup[30]={0xD2 ,0xD1 ,0xBD ,0xAB ,0xD5 ,0xD5 ,0xC3 ,0xF7 ,0xB5 ,0xB3 ,
//							0xCE ,0xBB ,0xC9 ,0xE8 ,0xD6 ,0xC3 ,0xCE ,0xAA ,0xD2 ,0xBB ,
//							0xB5 ,0xB3
//							};
//void voice_light_setup(u8 num)		//�ѽ���ǰ������λ����Ϊ
//{
//	u8 i;
//	arr_voice_light_setup[18]=arr_voice_choice[num*2]; 
//	arr_voice_light_setup[19]=arr_voice_choice[num*2+1]; 
//	U2SendChar(0xFD);
//	U2SendChar(0x00);
//	U2SendChar(0x20);
//	U2SendChar(0x01);
//	U2SendChar(0x01);
//	for(i=0;i<30;i++)
//		U2SendChar(arr_voice_light_setup[i]);
//	return ;
//}
