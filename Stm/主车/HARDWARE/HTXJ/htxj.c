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
//** 功能：     循迹函数
//** 参数：	  无参数
//** 返回值：    无
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

			 if(Line_Flag!=2) //出循迹位 不为2
		{
			

			if(gd==0XF3||gd==0XFB) //2、中间4、3传感器检测到黑线，微右拐 11110011  11111011
			{ 
				LSpeed=-(Car_Spend+100);
				RSpeed=-(Car_Spend-100);
				Line_Flag=0;

			}
			if(gd==0XF9||gd==0XFD)//3、中间3、2传感器检测到黑线，再微右拐	11111001 11111101
			{ 
				 LSpeed=-(Car_Spend+100);
				 RSpeed=-(Car_Spend-100);				 
				 Line_Flag=0;

			}
			if(gd==0XFC)//4、中间2、1传感器检测到黑线，强右拐			11111100
			{ 
				LSpeed=-(Car_Spend+100);
				RSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			}
			if(gd==0XFE)//5、最右边1传感器检测到黑线，再强右拐			11111110
			{ 
				 LSpeed=-(Car_Spend+100);
				 RSpeed=-(Car_Spend-120); 			
				 Line_Flag=1;	   //出循迹位为一

			}
		}  

		if(Line_Flag!=1)
		{

			if(gd==0XCF)//6、中间6、5传感器检测到黑线，微左拐		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 
			if(gd==0X9F||gd==0XDF)//7、中间7、6传感器检测到黑线，再微左拐		 
			{ 
				RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 			  
			if(gd==0X3F||gd==0XBF)//8、中间8、7传感器检测到黑线，强左拐		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-100);
				 Line_Flag=0;

			} 
			if(gd==0X7F)//9、最左8传感器检测到黑线，再强左拐		 
			{ 
				 RSpeed=-(Car_Spend+100);
				 LSpeed=-(Car_Spend-120);
				 Line_Flag=2;

			}
		}
		   	if(gd==0xFF)   //循迹灯全亮	 停止   黑线灯灭位为零  白线灯亮为一
		{
			if(Line_Flag==0)  	   //出循迹线为零时 判断计数
			{
				if(count++>1000) //计数
				{
					count=0;
		            STOP();
					Track_Flag=0;
					Stop_Flag=4;		  //出循迹线
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
			Host_Close_UpTrack( );  // 关闭寻迹数据上传	
		}
	
}


