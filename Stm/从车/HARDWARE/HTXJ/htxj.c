#include <htxj.h>
#include "stm32f10x.h"
#include "usart.h"	   
#include "xj.h"
#include "key.h"
#include "led.h"
#include "djqd.h"
#include "test.h"
#include "canp_hostcom.h"

u8 htTrack_Flag;

///***************************************************************
//** ���ܣ�     ѭ������
//** ������	  �޲���
//** ����ֵ��    ��
//****************************************************************/
//
void htTrack()
{
	
		if(gd==0)
		{
				STOP();
				htTrack_Flag=0;
				Stop_Flag=1;
		}
		else
		{
			Stop_Flag=0;

			if(gd==0XE7||gd==0XF7||gd==0XEF)
			{
			  LSpeed=-Car_Spend+20;
			  RSpeed=-Car_Spend+20;
			}

			 if(Line_Flag!=2) //��ѭ��λ ��Ϊ2
		{
			

			if(gd==0XF3||gd==0XFB) //2���м�4��3��������⵽���ߣ�΢�ҹ� 11110011  11111011
			{ 
				LSpeed=-(Car_Spend+100);
				RSpeed=-(Car_Spend-100);
				Line_Flag=0;

			}
			if(gd==0XF9||gd==0XFD)//3���м�3��2��������⵽���ߣ���΢�ҹ�	11111001 11111101
			{ 
				 LSpeed=-(Car_Spend+100);
				 RSpeed=-(Car_Spend-100);				 
				 Line_Flag=0;

			}
			if(gd==0XFC)//4���м�2��1��������⵽���ߣ�ǿ�ҹ�			11111100
			{ 
				LSpeed=-(Car_Spend+100);
				RSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			}
			if(gd==0XFE)//5�����ұ�1��������⵽���ߣ���ǿ�ҹ�			11111110
			{ 
				 LSpeed=-(Car_Spend+100);
				 RSpeed=-(Car_Spend-120); 			
				 Line_Flag=1;	   //��ѭ��λΪһ

			}
		}  

		if(Line_Flag!=1)
		{

			if(gd==0XCF)//6���м�6��5��������⵽���ߣ�΢���		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 
			if(gd==0X9F||gd==0XDF)//7���м�7��6��������⵽���ߣ���΢���		 
			{ 
				RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 			  
			if(gd==0X3F||gd==0XBF)//8���м�8��7��������⵽���ߣ�ǿ���		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 
			if(gd==0X7F)//9������8��������⵽���ߣ���ǿ���		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-120);
				 Line_Flag=2;

			}
		}
		   	if(gd==0xFF)   //ѭ����ȫ��	 ֹͣ   ���ߵ���λΪ��  ���ߵ���Ϊһ
		{
			if(Line_Flag==0)  	   //��ѭ����Ϊ��ʱ �жϼ���
			{
				if(count++>1000) //����
				{
					count=0;
		            STOP();
					Track_Flag=0;
					Stop_Flag=4;		  //��ѭ����
				}		
			}

		}
		else	count=0;
            
			if(!htTrack_Flag==0)
		  {
			Control(LSpeed,RSpeed);
		  }
        }
		
					
	if( Track_Flag==0)
		{
			Host_Close_UpTrack( );  // �ر�Ѱ�������ϴ�	
		}
	
}


