#include "stm32f10x.h"
#include "delay.h"	   
#include "74hc595.h"

/***************************************************************
** 功能：     74HC595端口初始化
** 参数：	  无参数
** 返回值：   无
****************************************************************/
void HC595_Init()
{
//	GPIOD->CRL&=0X0000FFFF;
//	GPIOD->CRL|=0X33330000;	  // PD4/5/6/7推挽输出 
//	GPIOD->ODR|=0X0000; 	  // PD4/5/6/7输出低

//	OE=0;
//	SER=0;
//	SCLK=0;
//	RCLK=0;

}

/***************************************************************
** 功能：     向74HC595写入数据
** 参数：	  data：被写入的数据
** 返回值：   无
****************************************************************/
void Write_595(u8 Data)
{
 	u8 i,Data_Bit;
	SCLK = 0;
	RCLK = 0;
	for(i=0;i<8;i++)
	{
		Data_Bit   = Data&0X80;
		if (Data_Bit) //输出1bit数据
		{
			SER=1; //将74HC595串行数据输入引脚设置为高电平
		}
		else
		{
			SER=0; //将74HC595串行数据输入引脚设置为低电平
		}

		SCLK = 0;
		delay_us(10);
		SCLK = 1;
		delay_us(10);
		Data <<= 1;
	}
	RCLK = 1;
}


void test_hc595( void )
{

	u8 i;
	u8 ls_led =0;
	for(i=0;i<8;i++)
	{
      ls_led = 1<<i;
		  Write_595(ls_led);
		  delay_ms(300);
  }


}

