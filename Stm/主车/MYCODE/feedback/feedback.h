#ifndef __FEEDBACK_H
#define __FEEDBACK_H	 
#include "sys.h"

void back_function(void);			//�������
void back_state(void);				//����״ֵ̬
void RFID_state(void);				//����rfid����
void data_other(u8 i);				//������ͨ��������ֵ
void voice_appoint(u8 i);			//�����ض���������

extern u8 feedback_state[11];			//�ӳ�״̬����ֵ
extern u8 feedback_state_flag1;			//״̬���ص�һ�α�־
extern u8 last_code[8];					//��һ���յ���ָ������	
extern u8 rfid_num2_state[19];			//�ӳ�����rfid״̬
extern u8 other_data[8];				//�����������ݷ���

extern u8 OK_flag;
#endif
