#include <feedback.h>
#include <usart.h>
#include <delay.h>
#include <led.h>
#include "canp_hostcom.h"
#include <test.h>
#include "uart_my.h"
#include "move.h"
#include <infrared.h>

u8 feedback_state_flag1=0;
u8 OK_flag=1;

void back_function()
{
	u8 back_date[8];
	//beep = 1;
	back_date[0] = 0x55;
	back_date[1] = 0x02;
	back_date[2] = 0x37;
	back_date[3] = 0x00;
	back_date[4] = 0x00;
	back_date[5] = 0x00;
	back_date[6] = 0x37;
	back_date[7] = 0xbb;
	delay_ms(25);
	//回复应答
	send_data_zigbee(back_date, 8);
	OK_flag = 1;
	beep = 0;
}
u8 feedback_state1[8];
u8 feedback_state2[8];
void back_state()
{
	//beep = 1;
	delay_ms(20);
	send_data_zigbee(feedback_state1 , 8);
	delay_ms(20);
	send_data_zigbee(feedback_state2 , 8);
	OK_flag=1;
	beep = 0;
}
extern u8 read_rfid_my[16];
void RFID_state()
{
	u8 rfid_num2_state[8]={0x55,0,0,0,0,0,0,0xbb};
	u8 i,j;
	for(i=0;i<4;i++)
	{
		rfid_num2_state[1]=0x41+i;
		for(j=0;j<4;j++)
			rfid_num2_state[j+2]=read_rfid_my[i*4+j];
		rfid_num2_state[6]=	(rfid_num2_state[2]+rfid_num2_state[3]+rfid_num2_state[4]+rfid_num2_state[5])%256;
		delay_ms(20);
		send_data_zigbee(rfid_num2_state,8);
	}
	OK_flag=1;
}
void voice_appoint(u8 i)			//发送特定指令
{
	u8 voice[8]={0x55,0x40,0x20,0x01,0x00,0x00,0x21,0xbb};
	if(i){voice[2]=0x01;voice[6]=0x02;}
	send_data_zigbee(voice,8);
}
u8 other_data[8]={0x55,0x82,0x00,0x00,0x00,0x00,0x00,0xbb};
void data_other(u8 i)
{
	delay_ms(20);
	if(i == 1)
	{
		other_data[2]=0x01;
		other_data[3]=current_y+0x61;
		other_data[4]=current_x+0x30;
		other_data[5]=current_d;
		other_data[6]=(other_data[2]+other_data[3]+other_data[4]+other_data[5])%256;
		send_data_zigbee(other_data,8);
	}
	else if(i == 2)	
	{
		other_data[2]=0x02;		//光档位
		other_data[3]=hw_check_data;
		other_data[4]=0x00;
		other_data[5]=0x00;
		other_data[6]=(other_data[2]+other_data[3]+other_data[4]+other_data[5])%256;
		send_data_zigbee(other_data,8);
	}
	else if(i == 0)send_data_zigbee(other_data,8);
	else back_function();
	OK_flag = 1;
}

