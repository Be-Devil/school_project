#ifndef __FEEDBACK_H
#define __FEEDBACK_H	 
#include "sys.h"

void back_function(void);		//�������
void back_state(void);			//����״ֵ̬
void RFID_state(void);			//����rfidֵ
void voice_appoint(u8 i);			//�����ض���������
void data_other(u8 i);				//�����ض����ݷ���

extern u8 feedback_state1[8];			//�ӳ�״̬����
extern u8 feedback_state2[8];			//�ӳ�״̬����ֵ
extern u8 feedback_state_flag1;			//״̬���ص�һ�α�־
extern u8 last_code[8];					//��һ���յ���ָ������		
extern u8 other_data[8];
extern u8 OK_flag;

#endif
