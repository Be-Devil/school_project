#include "stm32f10x.h"
#include "usart.h"	   
#include "xj.h"
#include "key.h"
#include "led.h"
#include "djqd.h"
#include "test.h"
#include "canp_hostcom.h"
u8 gd;
u8 gd_;

/***************************************************************
** 功能：     循迹端口初始化
** 参数：	  无参数
** 返回值：    无
****************************************************************/
void XJ_Init()  // 循迹数据通过CAN传输
{
// 	RCC->APB2ENR|=1<<2;    //使能PORTA时钟
//	
//	GPIOA->CRL&=0XFFFFFFF0;	  
// 	GPIOA->CRL|=0X00000003;//PA0 设置成输出 				   
// 	GPIOA->ODR|=1<<0;	   //P输出为高
//	
//  Track_Usart2_Close();
	
//	GPIOA->CRL&=0X00000000;	  
// 	GPIOA->CRL|=0X88888888;//PA0 设置成输出 				   
// 	GPIOA->ODR|=0x00ff;	   //P输出为高	

}

 void Track_Usart2_Open( void)  // 循迹数据串口2 传输使能 低有效
{
     TRACK_EN =0;
}

void Track_Usart2_Close( void)  // 循迹数据串口2 传输禁止 
{
     TRACK_EN =1;
}


/***************************************************************
** 功能：     循迹函数
** 参数：	  无参数
** 返回值：    无
****************************************************************/
void Track(void)
{
	if(gd==0||gd_==0)	//循迹灯全灭 停止
	{	
		STOP();
		Track_Flag=0;
		Stop_Flag=1;
		
	}
	else
	{
	   	Stop_Flag=0;
		if(gd_==0x6f)
		{
				 RSpeed=Car_Spend+50;
				 LSpeed=Car_Spend-50;
				 Line_Flag=0;		
		}
		
		if(gd_==0x77)
		{
				 RSpeed=Car_Spend-50;
				 LSpeed=Car_Spend+50;
				 Line_Flag=0;		
		}		
		
		if(gd==0XE7||gd==0XF7||gd==0XEF||gd==0XDF||gd==0XFB)//1、中间3/4传感器检测到黑线，全速运行
		{ 
			LSpeed=Car_Spend-30;
			RSpeed=Car_Spend-30;

		}

		if(Line_Flag!=2)
		{
			

			if(gd==0XF3) //2、中间4、3传感器检测到黑线，微右拐
			{ 
				LSpeed=Car_Spend+100;
				RSpeed=Car_Spend-100;
				Line_Flag=0;

			}
			if(gd==0XF9)//3、中间3、2传感器检测到黑线，再微右拐
			{ 
				 LSpeed=Car_Spend+100;
				 RSpeed=Car_Spend-100;				 
				 Line_Flag=0;

			}
			if(gd==0XFC)//4、中间2、1传感器检测到黑线，强右拐
			{ 
				LSpeed=Car_Spend+100;
				RSpeed=Car_Spend-100;
				 Line_Flag=0;

			}
			if(gd==0XFE)//5、最右边1传感器检测到黑线，再强右拐
			{ 
				 LSpeed=Car_Spend+100;
				 RSpeed=Car_Spend-100;			
				 Line_Flag=1;

			}
		}
		if(Line_Flag!=1)
		{

			if(gd==0XCF)//6、中间6、5传感器检测到黑线，微左拐		 
			{ 
				 RSpeed=Car_Spend+100;
				 LSpeed=Car_Spend-100;
				 Line_Flag=0;

			} 
			if(gd==0X9F||gd==0XDF)//7、中间7、6传感器检测到黑线，再微左拐		 
			{ 
				RSpeed=Car_Spend+100;
				 LSpeed=Car_Spend-100;
				 Line_Flag=0;

			} 			  
			if(gd==0X3F||gd==0XBF)//8、中间8、7传感器检测到黑线，强左拐		 
			{ 
				 RSpeed=Car_Spend+100;
				 LSpeed=Car_Spend-100;
				 Line_Flag=0;

			} 
			if(gd==0X7F)//9、最左8传感器检测到黑线，再强左拐		 
			{ 
				 RSpeed=Car_Spend+100;
				 LSpeed=Car_Spend-100;
				 Line_Flag=2;

			}
		}
		
			if(gd<0x80&&((gd&0x01) == 0)&&((gd&0x18) > 0))	 
			{ 
				LSpeed = 0;
				RSpeed = 0;
		    STOP();
				TIM_Cmd(TIM8, ENABLE);  //使能TIMx	
			}

		if(gd==0xFF&&gd_==0x7f)   //循迹灯全亮
		{
				LSpeed = 0;
				RSpeed = 0;
		    STOP();
				TIM_Cmd(TIM8, ENABLE);  //使能TIMx	
		 }
		else	count=0;

	   	if(!Track_Flag==0)
		{
			Control(LSpeed,RSpeed);
		}

	}
	
	if( Track_Flag==0)
		{
			Host_Close_UpTrack( );  // 关闭寻迹数据上传	
		}
	
}


void Xj_Track_Test(void)
{
//    u8 track_flag =0;
	 DJ_Init();				 //电机驱动初始化
	 PWM_Init();             //PWM初始化 
	 LED_Init();
	 XJ_Init();				//循迹初始化
	flag2 =0;
//	uart1_init(115200);	 
 	Track_Usart2_Open();
	delay_ms(100);
	Track_Flag =1;
	while(1)
		{
    
	if( flag2 )  
	  {            
	     gd = USART2_RX_BUF[0];	
		   flag2 =0;
			 LED0 =!LED0;
	   }
	 
			Track();
    			
			
    }

}



