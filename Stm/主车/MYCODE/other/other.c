#include <stm32f10x_lib.h>	   
#include "other.h"
#include "xj.h"
#include "74hc595.h"
#include "led.h"

/*************����ת��ƿ���********************/
void lr_led(u8 zy_led)	 //0==ȫ�أ�1=��ƿ���2=�ҵƿ�
{
	switch(zy_led)
	{
		case 0:LED_L=1;LED_R=1;
			break;			 //��,��ת��ƹر�
		case 1:LED_L=1;
			break;			 //��ת��ƴ�
		case 2:LED_R=1;
			break;			 //��ת��ƿ�    
		default:
			break;	
	}
}
/*************˫ɫ�ƿ���**********************/
//void color2led(u8 kg_ssd)	  //˫ɫ��	//0ȫ��	1ȫ��  2/3���̣��ƣ�
//{
//    switch(kg_ssd)
//	{
//	case 0:  Write_595(0X55);break;	 //˫ɫ��ȫ�����
//	case 1:  Write_595(0XAA);break;	 //˫ɫ��ȫ���̵�
//	case 2:  Write_595(0X66);break;	 //˫ɫ�ƺ��̼��
//	case 3:  Write_595(0X99);break;//˫ɫ�ƺ��̼��	
//	default: break;	
//	}
//}



