#ifndef _ZIGBEE_H
#define _ZIGBEE_H	 
#include "sys.h"

extern u8 voice2flag;		//语音播报内容开启标志位
extern u8 voice_send_allow;	//语音播报允许标志位，0为禁止，1为允许

extern u8 arr_voice1[80];
extern u8 arr_voice2[100];

void DZ_KG(u8 flag);
void LED_show(u8 data1,u8 data2,u8 data3,u8 data4);
void csb_jl_check(u8);


/****************语音播报**********************************/
//void save_data_voice(u8 len);  //语音保存并发送函数
//void voice_busy(void);//语音播报检测忙碌状态函数
void voice_send(u8 *voice);
void voice_light_current(u8 num);//播报第几档
void voice_light_setup(u8 num);//已将档位设置为第几档
			    
#endif


