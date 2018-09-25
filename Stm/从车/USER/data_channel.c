#include "test.h"
#include "delay.h"
#include "uart_my.h"
#include "exit.h"
#include "led.h"
#include "usart.h"
#define __DATA_CHANNEL_C__
#include "data_channel.h"
#include "feedback.h"


u8 Zigbee_Rx_Max =8;
u8 Wifi_Rx_num =0;
u8 Wifi_Rx_flag=0;

u8 Zigbee_Rx_num =0;
u8 Zigbee_Rx_flag=0;
u8 Zigbee_RxZt_flag =0;

extern u8 OK_flag;
void Wifi_data_Receive( u8 res)  // wifi 数据接收处理 
{
	if(OK_flag)	//如果上一条指令完成
	{
		rxd1_timer4_1ms=0;

		if(rxd1_timer4_flag==1) //  5ms时间 数据帧重新开始
		{
			 rxd1_timer4_flag=0;
			 TIM4->CR1|=0x01;     //使能定时器4
			 Wifi_Rx_num =0;
			 Wifi_Rx_Buf[Wifi_Rx_num]=res;
		}
		else if(Wifi_Rx_num < WIFI_MAX_NUM )	
		{
			  Wifi_Rx_Buf[++Wifi_Rx_num]=res;	 // 接收数据帧
		}

		else   // 数据超出最大接收数据长度时，舍去不要
		{
				;
		}
	}

}

void Zigbee_data_Receive( u8 res)  // zigbee 数据接收处理
{
    u8 sum;
	if(OK_flag)	//如果上一条指令完成
	{
		if(Zigbee_Rx_num >0)
		{
		   Zigb_Rx_Buf[Zigbee_Rx_num]=res;
		   Zigbee_Rx_num++;
		}
		else if (res==0x55)		// 寻找包头
		{
			
		   Zigb_Rx_Buf[0]=res;
		   Zigbee_Rx_num=1;
		}
		else Zigbee_Rx_num =0;
		
	   if(Zigbee_Rx_num >= Zigbee_Rx_Max)
		{

			if((Zigb_Rx_Buf[Zigbee_Rx_Max -1]==0xbb)&&(Zigbee_RxZt_flag ==0)&&(Zigb_Rx_Buf[1]!=0xfd))	 // 判断包尾	//change by ygm
			{									  
				//主指令与三位副指令左求和校验
				//注意：在求和溢出时应该对和做256取余。
				Zigbee_Rx_num=0;	  // 计数清零
				Zigbee_RxZt_flag =0;  // 接收状态清零
				sum=(Zigb_Rx_Buf[2]+Zigb_Rx_Buf[3]+Zigb_Rx_Buf[4]+Zigb_Rx_Buf[5])%256;
				if(sum==Zigb_Rx_Buf[6])		//判断求和
				{
					//*****************************/
					if( (last_code[1]!=Zigb_Rx_Buf[1]) ||
						(last_code[2]!=Zigb_Rx_Buf[2]) ||
						(last_code[3]!=Zigb_Rx_Buf[3]) ||
						(last_code[4]!=Zigb_Rx_Buf[4]) ||
						(last_code[5]!=Zigb_Rx_Buf[5])	)	//判断是否与上次接受的指令不同
					{
						Zigbee_Rx_flag=1;OK_flag=0;
						last_code[1]=Zigb_Rx_Buf[1]; 
						last_code[2]=Zigb_Rx_Buf[2];
						last_code[3]=Zigb_Rx_Buf[3];
						last_code[4]=Zigb_Rx_Buf[4];
						last_code[5]=Zigb_Rx_Buf[5];	
					}
					else if(Zigb_Rx_Buf[2]==0x80)			//相同且为状态指令，返回状态数据
						back_state();
					else if(Zigb_Rx_Buf[2]==0x81)			//相同且rfid指令，返回rfid数据
						RFID_state();
					else if(Zigb_Rx_Buf[2]==0x82||Zigb_Rx_Buf[2]==0x83)			//相同且为普通常规状态指令，返回按要求的状态
						data_other(0);
					else 									//否则返回应答
						back_function();
				}
				else Zigbee_Rx_flag =0;
			}
			else if((Zigb_Rx_Buf[1]==0xfd)&&(Zigbee_RxZt_flag ==0))
			{
				if(Zigb_Rx_Buf[2]>8)	
				{ 
					Zigbee_Rx_Max = Zigb_Rx_Buf[2];
					Zigbee_RxZt_flag =1;  // 接收状态1  接着接收数据
				}
				else 			 
				{		 
					Zigbee_Rx_num=0;		// 计数清零
					if(Zigb_Rx_Buf[Zigbee_Rx_Max-1]==0xbb)
					{
						Zigbee_RxZt_flag =0;  // 接收状态清零
						//  send_Flag=1;			//change by ygm
						Zigbee_Rx_Max =8;
						USART_WIFI_JX( Zigb_Rx_Buf ); // 解析串口数据
					}
				}	  


			}
			else if((Zigbee_RxZt_flag ==1)&&(Zigb_Rx_Buf[Zigbee_Rx_Max -1]==0xbb))
			{
				Zigbee_Rx_num=0;	  // 计数清零
				Zigbee_RxZt_flag =0;  // 接收状态清零
				Zigbee_Rx_Max =8;
				USART_WIFI_JX( Zigb_Rx_Buf ); // 解析串口数据
			}
			else{ 		//接收错误指令，打开蜂鸣器
					Zigbee_Rx_flag =0; 
					Zigbee_Rx_num =0;
				}		
		}
	}

}


