#include <stm32f10x_lib.h>	   
#include "other.h"
#include "xj.h"
#include "74hc595.h"
#include "led.h"

/*************左右转向灯控制********************/
void lr_led(u8 zy_led)	 //0==全关；1=左灯开，2=右灯开
{
	switch(zy_led)
	{
		case 0:LED_L=1;LED_R=1;
			break;			 //左,右转向灯关闭
		case 1:LED_L=1;
			break;			 //左转向灯打开
		case 2:LED_R=1;
			break;			 //右转向灯开    
		default:
			break;	
	}
}
/*************双色灯控制**********************/
//void color2led(u8 kg_ssd)	  //双色灯	//0全绿	1全红  2/3红绿（黄）
//{
//    switch(kg_ssd)
//	{
//	case 0:  Write_595(0X55);break;	 //双色灯全亮红灯
//	case 1:  Write_595(0XAA);break;	 //双色灯全亮绿灯
//	case 2:  Write_595(0X66);break;	 //双色灯红绿间隔
//	case 3:  Write_595(0X99);break;//双色灯红绿间隔	
//	default: break;	
//	}
//}



