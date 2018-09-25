#include "timer.h"
#include "canp_hostcom.h"
#include "data_channel.h"
#include "syn7318.h"
#include "djqd.h"
#include "rc522_my.h"
#include "move.h"
#include "xj.h"
#include "exit.h"
#include "code.h"

void TIM5_Int_Init(u16 arr,u16 psc)
{
  TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;
// 	NVIC_InitTypeDef NVIC_InitStructure;

	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM5, ENABLE); //ʱ��ʹ��
	
	//��ʱ��TIM5��ʼ��
	TIM_TimeBaseStructure.TIM_Period = arr; //��������һ�������¼�װ�����Զ���װ�ؼĴ������ڵ�ֵ	
	TIM_TimeBaseStructure.TIM_Prescaler =psc; //����������ΪTIMxʱ��Ƶ�ʳ�����Ԥ��Ƶֵ
	TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1; //����ʱ�ӷָ�:TDTS = Tck_tim
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;  //TIM���ϼ���ģʽ
	TIM_TimeBaseInit(TIM5, &TIM_TimeBaseStructure); //����ָ���Ĳ�����ʼ��TIMx��ʱ�������λ
 
	TIM_ITConfig(TIM5,TIM_IT_Update, DISABLE ); //ʹ��ָ����TIM5�ж�,��������ж�

	

	TIM_Cmd(TIM5, DISABLE);  //ʹ��TIMx					 
}

void TIM6_Int_Init(u16 arr,u16 psc)
{
  TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;
 	NVIC_InitTypeDef NVIC_InitStructure;


	RCC_APB1PeriphClockCmd(RCC_APB1Periph_TIM6, ENABLE); //ʱ��ʹ��
	
	//��ʱ��TIM6��ʼ��
	TIM_TimeBaseStructure.TIM_Period = arr; //��������һ�������¼�װ�����Զ���װ�ؼĴ������ڵ�ֵ	
	TIM_TimeBaseStructure.TIM_Prescaler =psc; //����������ΪTIMxʱ��Ƶ�ʳ�����Ԥ��Ƶֵ
	TIM_TimeBaseStructure.TIM_ClockDivision = TIM_CKD_DIV1; //����ʱ�ӷָ�:TDTS = Tck_tim
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;  //TIM���ϼ���ģʽ
	TIM_TimeBaseInit(TIM6, &TIM_TimeBaseStructure); //����ָ���Ĳ�����ʼ��TIMx��ʱ�������λ
 
	TIM_ITConfig(TIM6,TIM_IT_Update, ENABLE ); //ʹ��ָ����TIM6�ж�,��������ж�

	NVIC_PriorityGroupConfig(NVIC_PriorityGroup_0); //�жϷ��� 0
	//�ж����ȼ�NVIC����
 	NVIC_InitStructure.NVIC_IRQChannel = TIM6_IRQn;  //TIM6�ж�
 	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 0;  //��ռ���ȼ�0��
 	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 3;  //�����ȼ�3��
 	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE; //IRQͨ����ʹ��
 	NVIC_Init(&NVIC_InitStructure);  //��ʼ��NVIC�Ĵ���
	
	TIM_Cmd(TIM6, ENABLE);  //ʹ��TIMx					 
}
 //��ʱ��6�жϷ������
 void TIM6_IRQHandler(void)   //TIM6�ж�
 {
 	if (TIM_GetITStatus(TIM6, TIM_IT_Update) != RESET)  //���TIM6�����жϷ������
 		{
 		   TIM_ClearITPendingBit(TIM6, TIM_IT_Update  );  //���TIMx�����жϱ�־ 
			   
			CanP_Host_Main();     // ��ѯCAN��������
			CanP_CanTx_Check();
			  
 		}
 }

 void TIM8_Int_Init(u16 arr,u16 psc)
{
  TIM_TimeBaseInitTypeDef  TIM_TimeBaseStructure;
	NVIC_InitTypeDef NVIC_InitStructure;
 
	RCC_APB2PeriphClockCmd(RCC_APB2Periph_TIM8, ENABLE); //ʱ��ʹ��
	
	//��ʱ��TIM3��ʼ��
	TIM_TimeBaseStructure.TIM_Period = arr; //��������һ�������¼�װ�����Զ���װ�ؼĴ������ڵ�ֵ	
	TIM_TimeBaseStructure.TIM_Prescaler =psc; //����������ΪTIMxʱ��Ƶ�ʳ�����Ԥ��Ƶֵ
	TIM_TimeBaseStructure.TIM_ClockDivision = 0; //����ʱ�ӷָ�:TDTS = Tck_tim
	TIM_TimeBaseStructure.TIM_CounterMode = TIM_CounterMode_Up;  //TIM���ϼ���ģʽ
	TIM_TimeBaseStructure.TIM_RepetitionCounter=0;
	TIM_TimeBaseInit(TIM8, &TIM_TimeBaseStructure); //����ָ���Ĳ�����ʼ��TIMx��ʱ�������λ
  TIM_ClearFlag(TIM8, TIM_FLAG_Update);
	TIM_ITConfig(TIM8,TIM_IT_Update|TIM_IT_Trigger,ENABLE ); //ʹ��ָ����TIM3�ж�,��������ж�

	//�ж����ȼ�NVIC����
	NVIC_InitStructure.NVIC_IRQChannel = TIM8_UP_IRQn;  //TIM3�ж�
	NVIC_InitStructure.NVIC_IRQChannelPreemptionPriority = 4;  //��ռ���ȼ�0��
	NVIC_InitStructure.NVIC_IRQChannelSubPriority = 4;  //�����ȼ�3��
	NVIC_InitStructure.NVIC_IRQChannelCmd = ENABLE; //IRQͨ����ʹ��
	NVIC_Init(&NVIC_InitStructure);  //��ʼ��NVIC�Ĵ���

	TIM_Cmd(TIM8, DISABLE);  //ʹ��TIMx					 
}

void TIM8_UP_IRQHandler(void)   
{     
	int i,j=0,l=0;
    if (TIM_GetITStatus(TIM8, TIM_IT_Update) != RESET)   
    {  
        TIM_ClearITPendingBit(TIM8, TIM_IT_Update);
				TIM_Cmd(TIM8, DISABLE);  
				Track_Flag=0;
				Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
				delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
				STOP();
				Control(30,30);
				delay_ms(100);STOP();
				gd= (Get_Host_UpTrack( TRACK_H8) )&0xff;  // ��ȡѭ������
				gd_= (Get_Host_UpTrack( TRACK_Q7) )&0xff;  // ��ȡѭ������
				if(gd==0xFF&&gd_==0x7f)
					gd_flag++;
				Control(30,30);
				delay_ms(100);STOP();
				if(rfid_do == 1)
					for(i=0; i<6;i++)
					{
						 Control(30,30);
						 delay_ms(100);
						 STOP();
						 delay_ms(500);
						 j = rc522_read(12,read_rfid_my);
						if(j == 1&&l==0)
						{
							beep = 1; delay_ms(200); beep = 0; delay_ms(200);	
							beep = 1; delay_ms(200); beep = 0; delay_ms(200);	
							beep = 1; delay_ms(200); beep = 0;
							l++;
							voice_test (read_rfid_my,16);
						}
					}
				else
				{
					 Control(30,30);
					 delay_ms(200);
					 STOP();
				} 
				if(gd_flag == 0)   
				{
					Host_Close_UpTrack( );  // �ر�Ѱ�������ϴ�	
					car_go_flag = 0;
					Track_Flag=0;
					Control(30,30);delay_ms(250);
					STOP();
				}
				else
				{
					Track_Flag=1;
					gd_flag = 0;
					TIM_Cmd(TIM2, ENABLE);  //ʹ��TIMx	
				}
				rfid_do = 0;
				
    }          
} 











