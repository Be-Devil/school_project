#include <stm32f10x.h>  
#include <test.h>
#include "infrared.h"
#include "xj.h"
#include "usart.h"
#include "hw.h"
#include "csb.h"
#include "bh1750.h"
#include "74hc595.h"
#include "led.h"
#include "move.h"

u8 license_plate1[3] = {0x00};
u8 license_plate2[3] = {0x00};



/******************光照调档***********************/

 u8 H_1[4]={0x00,0xFF,0x0C,~(0x0C)};	 //光照加1档
 u8 H_2[4]={0x00,0xFF,0x18,~(0x18)};	 //光照加2档
 u8 H_3[4]={0x00,0xFF,0x5E,~(0x5E)};	 //光照加3档
 
void hw_light(int light_ed){
	int ini_light=0;
	int light[3]={0,0,0};
	int light_n=1;
	int i,j;
	ini_light=Dispose();
	for(i=0;i<3;i++){
		Transmition(H_1,4);			 //换挡
		delay_ms(1000);
		light[i]=Dispose();	   		//读出档位			
	}
	for(j=0;j<3;j++)
		if(ini_light>light[j])light_n++;
	
	if(light_n<light_ed) light_n+=4;
	switch(light_n-light_ed){
		
		case 0:
		Transmition(H_1,4);
		LED2=!LED2;
		break;
		
		case 2:
		Transmition(H_3,4);
		LED2=!LED2;
		break;
		
		case 3:
		Transmition(H_2,4);
		LED2=!LED2;
		break;
	}
	
}

/************光照检测函数*************/
u8 hw_check_data = 1;
u8 hw_light_check(){				 //检测光照档位
	int ini_light=0;
	int light[3]={0,0,0};
	int light_n=0;
	int i,j;
	ini_light=Dispose();
	for(i=0;i<3;i++){
		Transmition(H_1,4);			 //换挡
		delay_ms(1300);
		light[i]=Dispose();	   		//读出档位			
	}
	Transmition(H_1,4);			 	//调回原档
	for(j=0;j<3;j++)
		if(ini_light>light[j])light_n++;

	return ++light_n;
}





/*************************立体显示***************************************/
/*
控制指令结构
0xFF|0xXX|XX|XX|XX|XX
起始位|模式|数据【1】|数据【2】|数据【3】|数据【4】
 */
 /**************************************************
模式说明
模式					说明
0x20|接收前四位车牌信息模式
0x10|接收后两位车牌信息与两位坐标信息模式【并显示】
0x11|显示距离模式
0x12|显示图形模式
0x13|显示颜色模式
0x14|显示路况模式
0x15|显示默认模式
*/
/***************************************************
车牌显示模式说明
模式|数据【1】|数据【2】|数据【3】|数据【4】
0x20|车牌【1】|车牌【2】|车牌【3】|车牌【4】
0x10|车牌【5】|车牌【6】|横坐标|纵坐标					 
说明：在车牌模式下，车牌信息包括六个车牌字符和地图上某个位置的坐标，共八个字符（注意：车牌信息格式为字符串格式）
*/
/***************************************************
距离模式说明
模式|数据【1】|数据【2】|数据【3】|数据【4】
0x11|距离十位|距离各位|0x00|0x00
说明：在距离模式下，数据【1】~数据【2】为需要显示的距离信息（注：距离显示格式为十进制）。其余为为0x00，保留不用
*/
/***************************************************
图形模式下说明
模式|数据【1】|数据【2】~数据【4】|说明
	 0x01		   矩形
	 0x02		   圆形
	 0x03		   三角形
	 0x04		   菱形
0x12|0x05|均为0x00|梯形
 	 0x06		   饼图
	 0x07		   靶图
	 0x08		   条形图
*/
/***************************************************
颜色模式说明
模式|数据【1】|数据【2】~数据【4】|说明
	 0x01		   红色
	 0x02		   绿色
	 0x03		   蓝色
	 0x04		   黄色
0x13|0x05|均为0x00|紫色
 	 0x06		   青色
	 0x07		   黑色
	 0x08		   白色
*/
/***************************************************
路况模式说明
模式|数据【1】|数据【2】~数据【4】|说明
0x14|0x01	  |均为0x00			  |前方有事故，请绕行
0x14|0x02	  |均为0x00			  |前方施工，请绕行
*/
/***************************************************
默认模式说明
模式|数据【1】|数据【2】~数据【4】|说明
0x15|0x01|0x00|显示默认信息	  
*/
//void litishow(u8 da1,u8 da2,u8 da3,u8 da4,u8 da5){
//	u8 data[6];
//	data[0]=0xff;
//	data[1]=da1;
//	data[2]=da2;
//	data[3]=da3;
//	data[4]=da4;
//	data[5]=da5;
//	Track_Flag=0;MP=0;
//	Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
//	STOP();
//	Transmition(data,6);
//	delay_ms(200);	
//}
///*******************立体显示车牌号码与坐标**************************/
void litishow_carnum(u8 da1,u8 da2){
	u8 data[6]={0xff,0x20,'1','2','3','4'};
	u8 data2[6]={0xff,0x10,'1','2','3','4'};
	
	data[2]=(u8)license_plate1[0];
	data[3]=(u8)license_plate1[1];
	data[4]=(u8)license_plate1[2];
	data[5]=(u8)license_plate2[0];	
	
	data2[2]=(u8)license_plate2[1];
	data2[3]=(u8)license_plate2[2];
	data2[4]=(u8)(da2+65);
	data2[5]=(u8)(da1+48);
	
	Transmition(data,6);
	delay_ms(1000);
	Transmition(data2,6);
}


/*************************************************************
************立体显示转向******************
*************************************************************/
u8 LITISHOW_X ='3'-0x30;
u8 LITISHOW_Y ='e'-0x61;

u8 already_litishow_dir=0;		//已经立体显示了
void litishow_dir()
{
	if(already_litishow_dir==0)
	{	
		already_litishow_dir=1;
		if( ((current_y<LITISHOW_Y)&&(current_d==1))||		
			((current_y>LITISHOW_Y)&&(current_d==3))||		//
			((current_x<LITISHOW_X)&&(current_d==2))||		//
			((current_x>LITISHOW_X)&&(current_d==4))	)
			car_half_right(RIGHT_HALF_TIME);

		else  if(	((current_y>LITISHOW_Y)&&(current_d==1))||
					((current_y<LITISHOW_Y)&&(current_d==3))||	//	
					((current_x>LITISHOW_X)&&(current_d==2))||	//
					((current_x<LITISHOW_X)&&(current_d==4))	)
			
		car_half_left(LEFT_HALF_TIME);
	}
	else if(already_litishow_dir==1)
	{
		already_litishow_dir=0;
		if( ((current_y<LITISHOW_Y)&&(current_d==1))||		
			((current_y>LITISHOW_Y)&&(current_d==3))||		//
			((current_x<LITISHOW_X)&&(current_d==2))||		//
			((current_x>LITISHOW_X)&&(current_d==4))	)
		car_left();
		
		else  if(	((current_y>LITISHOW_Y)&&(current_d==1))||
					((current_y<LITISHOW_Y)&&(current_d==3))||	//	
					((current_x>LITISHOW_X)&&(current_d==2))||	//
					((current_x<LITISHOW_X)&&(current_d==4))	)
			
		car_right();
	}	

}








