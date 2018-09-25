#ifndef _MOVE_H
#define _MOVE_H	 
#include "sys.h"

#define DAOZA_X '1'-0x30		//��բ��x����
#define DAOZA_Y 'f'-0x61		//��բ��y����

/*****************�������߱�׼ֵ***************************/
#define CAR_GO_TEMPMP	28  			//ðͷ����ֵ
#define CAR_BACK_TEMPMP	45				//1,3�ߺ�������ֵ	 180
#define CAR_SCXJ_HTXJ	100				//CSXJ��HTXJʱ��

#define CAR_SHORT_TIME	800				//ѭ���������е��ʱ��
#define CAR_LONG_TIME	800				//ѭ���������е��ʱ��

#define LEFT_HALF_TIME	600				//��ת45��ʱ����ʱ
#define RIGHT_HALF_TIME	600				//��ת45��ʱ����ʱ
											
extern int obstacle_num;   //�ϰ������
extern u8 M[9][7];
extern int car_go_flag;

void mask_M(u8 x,u8 y,u8 flag);		//���ε�ȡ��
void CSXJ(int xjtime);
void HTXJ (int httime);
void STOP1(void);
void car_go(u16 gomp);		   //ðͷ
void car_left(void);	   //������			   //���ķ���
void car_right(void);	   //������			   //���ķ���
void car_back(u16 backmp);	   //�����
void car_xj(void);		   //ѭ��				//��������
void car_longline(void);   //С���ߵ������м�   //��������
void car_shortline(void);  //С���ߵ������м�   //��������

void car_half_left(u16 left_time);				//��ת��ʱ�亯��
void car_half_right(u16 right_time);			//��ת��ʱʱ�亯��

extern int current_x;
extern int current_y;
extern int current_d;
u8 longline_flag(u8 x,u8 y);   //�ж��Ƿ��ں����м�
u8 shortline_flag(u8 x,u8 y);  //�ж��Ƿ��������м�
u8 front_flag(u8 x,u8 y);      //�ж��Ƿ��� 1 �������
u8 back_flag(u8 x,u8 y);       //�ж��Ƿ��� 3 �������
u8 crossroad_flag(u8 x,u8 y);              //�ж��Ƿ���ʮ��·�ڴ�

void car_curxyd(u8 x,u8 y,u8 d);     //����С����ʼ�����꼰����
void way_car(u8 x,u8 y,u8 d);        //����Ŀ��㣬���ߵ�Ŀ���
u8 breadth(void);                          //���ù���������ӵ��յ�
void recursive(u8 front);              //�ݹ��㷨�ҵ����·��
void way_run(u8 next_x,u8 next_y);    //С���ߵ���һ��
void way_dir(u8 this_d,u8 next_d);    //��ת���򣬵�ǰ�㷽��Ŀ�귽��
void way_init(void);

u8 obstacle_check(u8 x,u8 y);					//�ж��Ƿ��ڱ�־�ﴦ�����ǲ����иõ㳬��������
void updata_luxin(u8 x,u8 y,u8 d);

		 				    
#endif






