#ifndef _ZIGBEE_H
#define _ZIGBEE_H	 
#include "sys.h"

extern u8 voice2flag;		//�����������ݿ�����־λ
extern u8 voice_send_allow;	//�������������־λ��0Ϊ��ֹ��1Ϊ����

extern u8 arr_voice1[80];
extern u8 arr_voice2[100];

void DZ_KG(u8 flag);
void LED_show(u8 data1,u8 data2,u8 data3,u8 data4);
void csb_jl_check(u8);


/****************��������**********************************/
//void save_data_voice(u8 len);  //�������沢���ͺ���
//void voice_busy(void);//�����������æµ״̬����
void voice_send(u8 *voice);
void voice_light_current(u8 num);//�����ڼ���
void voice_light_setup(u8 num);//�ѽ���λ����Ϊ�ڼ���
			    
#endif


