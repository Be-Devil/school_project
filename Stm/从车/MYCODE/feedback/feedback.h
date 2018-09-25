#ifndef __FEEDBACK_H
#define __FEEDBACK_H	 
#include "sys.h"

void back_function(void);		//反馈完成
void back_state(void);			//返回状态值
void RFID_state(void);			//返回rfid值
void voice_appoint(u8 i);			//发送特定语音函数
void data_other(u8 i);				//发送特定数据返回

extern u8 feedback_state1[8];			//从车状态返回
extern u8 feedback_state2[8];			//从车状态返回值
extern u8 feedback_state_flag1;			//状态返回第一次标志
extern u8 last_code[8];					//上一次收到的指令类型		
extern u8 other_data[8];
extern u8 OK_flag;

#endif
