#include <stm32f10x.h>
#include "code.h"
#include "uart_my.h"
#include "data_channel.h"
#include "canp_hostcom.h"
#include "move.h"
#include "test.h"

/*****************************************************************/
/**********语音播报函数****************************************
**参数：DATA-播报数组
		num -数组大小
******************************************************************/
void voice_test(u8 *DATA,int num)		//语音播报函数，传入播报数组和大小(最大250)
{
	u8 data[255]={0xfd,0x00,0x00,0x01,0x01};	//语音播报前缀
	u8 i;
	num = num+2;
	data[1] = (num&0xff00)>>8;
	data[2] = num&0x00ff;
	
	for(i =0;i<250;i++)
		data[5+i]=DATA[i];
	
	send_data_zigbee( data , num+3 ); 		//语音播报6:1,4,5
}


void dir_change(u8 flag,u8 dir)//flag:1允许，0禁止。dir:2右，3后，4左
{
	u8 next_x=current_x,next_y=current_y;
	if((dir == 2&&flag == 1)||(dir == 4&&flag == 0))//向右转
	{
		if(current_d == 1)next_y+=1;
		else if(current_d == 2)next_x+=1;
		else if(current_d == 3)next_y-=1;
		else if(current_d == 4)next_x-=1;
	}
	else if((dir == 4&&flag == 1)||(dir == 2&&flag == 0))//向左转
	{
		if(current_d == 1)next_y-=1;
		else if(current_d == 2)next_x-=1;
		else if(current_d == 3)next_y+=1;
		else if(current_d == 4)next_x+=1;
	}
	else if(dir == 3&&flag == 1)//向后退
	{
		if(current_d == 1)next_x+=1;
		else if(current_d == 2)next_y-=1;
		else if(current_d == 3)next_x-=1;
		else if(current_d == 4)next_y+=1;
	}
	mask_M(current_x,current_y,1);
	way_car(next_x,next_y,0);
}


