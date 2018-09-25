#include <stm32f10x.h>	   
#include "zigbee.h"
#include "xj.h"
#include "usart.h"
#include "hw.h"
#include "csb.h"
#include "led.h"
#include "delay.h"
#include "uart_my.h"

u8 voice2flag=0;	   //语音播报内容开启标志位,
u8 voice_send_allow=0;//语音播报允许标志位，0为禁止，1为允许

/*************************道闸控制****************************************/
u8 DZ_K[8]={0x55,0x03,0x01,0x01,0x00,0x000,0x02,0xbb};//道闸开
u8 DZ_G[8]={0x55,0x03,0x01,0x02,0x00,0x00,0x03,0xBB}; //道闸关
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

/**************************LED显示******************************************/
/*
 0x55,0x04|0xXX|0xXX,0xXX,0xXX|0xXX|0xBB
 包头	  |主指令|副指令	|校验和|包尾
主指令	0x01 数据写入第一排数码管
		0x02 数据写入第二排数码管
		0x03 进入倒计时模式
		0x04 第二排显示距离 

主指令					副指令
0x01			数据【1】、数据【2】|数据【3】、数据【4】|数据【5】、数据【6】
0x02			数据【1】、数据【2】|数据【3】、数据【4】|数据【5】、数据【6】
0x03			0x00/0x01/0x02（关闭/打开/清除）|0x00|0x00
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
*********************TFT显示器控制************************************
*********************************************************************/




/***********************超显声波测距并示********************************/

void csb_jl_check(u8 flag){  //赋值0无返回数据
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
		LED_show(0x04,0x00,csb_jl_end/100,csb_jl_end%100);//显示距离
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
//					};		//语音播报最多256个字节，保险起见此处设为250个字节；
//u8 arr_voice2[100]={0xFD ,0X00 ,0X61 ,0X01 ,0X01 ,0xB6 ,0xFE ,0xCA ,0xAE ,//1
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xB6 ,0XFE ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC8 ,0XFD ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCB ,0XC4 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XCE ,0XE5 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC1 ,0XF9 ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XC6 ,0XDF ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0XB0 ,0XCB ,0xCA ,0xAE ,
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xBE ,0xC5 ,0xCA ,0xAE ,//此容量九十
//					0xB4 ,0xCB ,0xC8 ,0xDD ,0xC1 ,0xBF ,0xD2 ,0xBB ,0xB0 ,0xD9 ,//此容量一百
//					};		//语音播报最多256个字节，保险起见此处设为250个字节；
/****************语音播报标志物控制****************************************
**回传数据类型		回传数据	触发条件
**初始化成功		0x4A		芯片初始化成功
**收到正确命令帧	0x41		语音芯片收到正确的命令帧
**收到错误命令帧	0x45		语音芯片收到错误的命令帧
**芯片忙碌			0x4E		语音芯片处于合成状态
**芯片空闲			0x4F		语音芯片处于空闲状态,收到状态查询命令一帧
								或一帧数据合成结束，芯片处于空闲状态
****************************************************************************/
//void save_data_voice(u8 len)  //语音更新并发送函数
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
//void voice_busy(void)	 //检测忙碌函数
//{
//	U2SendChar(0xFD);
//	U2SendChar(0x00);
//	U2SendChar(0x01);
//	U2SendChar(0x21);	
//}
//void voice_send(u8 *voice)	//语音播报函数
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


///****************************照明档位播报****************************/
//u8 arr_voice_choice[12]={0xC1,0XE3,0xD2 ,0xBB ,0xB6 ,0xFE ,0xC8 ,0xFD ,0xCB ,0xC4 ,0xCE ,0xE5 } ; //012345

//u8 arr_voice_light[30]={0xB5 ,0xB1 ,0xC7 ,0xB0 ,0xD5 ,0xD5 ,0xC3 ,0xF7 ,0xB5 ,0xB3 ,	//当前照明档
//						0xCE ,0xBB ,0xCE ,0xAA ,0xD2 ,0xBB,0xB5 ,0xB3 						//位为
//						}; 
//void voice_light_current(u8 num)		//当前照明档位为
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
//void voice_light_setup(u8 num)		//已将当前照明档位设置为
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
