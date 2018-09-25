#include <feedback.h>
#include <usart.h>
#include <delay.h>
#include <led.h>
#include <uart_my.h>
#include <test.h>
#include <rc522_my.h>
#include <move.h>
#include <infrared.h>

u8 feedback_state_flag1=0;
u8 OK_flag=1;


void back_function()
{
	u8 back_date[8];
	u8 i;
	for(i=0;i<19;i++)				//清除rfid信息
		rfid_num2_state[i] = 0;
	beep = 1;
	back_date[0] = 0x55;
	back_date[1] = 0xaa;
	back_date[2] = 0x37;
	back_date[3] = 0x00;
	back_date[4] = 0x00;
	back_date[5] = 0x00;
	back_date[6] = 0x37;
	back_date[7] = 0xbb;
	delay_ms(20);
	//回复应答
	send_data_wifi(back_date, 8);
	OK_flag = 1;
	beep = 0;
	
}
u8 feedback_state[11];
void back_state()
{
	beep = 1;
	delay_ms(20);
	send_data_wifi(feedback_state , 11);
	OK_flag=1;
	beep = 0;
}
extern u8 read_rfid_my[16];
u8 rfid_num2_state[19];
void RFID_state()
{
	beep = 1;
	delay_ms(20);
	send_data_wifi(rfid_num2_state , 19);
	beep = 0;
	OK_flag = 1;
}
void voice_appoint(u8 i)			//发送特定指令
{
	u8 voice[8]={0x55,0x06,0x20,0x01,0x00,0x00,0x21,0xbb};
	if(i==1){voice[2]=0x10;voice[6]=0x11;}
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
		send_data_wifi(other_data,8);
	}
	else if(i == 2)	//光档位
	{
		other_data[2]=0x02;		
		other_data[3]=hw_check_data;
		other_data[4]=0x00;
		other_data[5]=0x00;
		other_data[6]=(other_data[2]+other_data[3]+other_data[4]+other_data[5])%256;
		send_data_wifi(other_data,8);
	}
	else if(i == 0)send_data_wifi(other_data,8);
	else back_function();
	OK_flag = 1;
}





