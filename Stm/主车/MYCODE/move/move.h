#ifndef _MOVE_H
#define _MOVE_H	 
#include "sys.h"

#define DAOZA_X '1'-0x30		//开闸点x坐标
#define DAOZA_Y 'f'-0x61		//开闸点y坐标

/*****************主车行走标准值***************************/
#define CAR_GO_TEMPMP	28  			//冒头码盘值
#define CAR_BACK_TEMPMP	45				//1,3边后退码盘值	 180
#define CAR_SCXJ_HTXJ	100				//CSXJ和HTXJ时间

#define CAR_SHORT_TIME	800				//循迹到竖线中点的时间
#define CAR_LONG_TIME	800				//循迹到横线中点的时间

#define LEFT_HALF_TIME	600				//左转45度时间延时
#define RIGHT_HALF_TIME	600				//右转45度时间延时
											
extern int obstacle_num;   //障碍物个数
extern u8 M[9][7];
extern int car_go_flag;

void mask_M(u8 x,u8 y,u8 flag);		//屏蔽点取消
void CSXJ(int xjtime);
void HTXJ (int httime);
void STOP1(void);
void car_go(u16 gomp);		   //冒头
void car_left(void);	   //向左走			   //更改方向
void car_right(void);	   //向右走			   //更改方向
void car_back(u16 backmp);	   //向后走
void car_xj(void);		   //循迹				//更改坐标
void car_longline(void);   //小车走到长线中间   //更改坐标
void car_shortline(void);  //小车走到短线中间   //更改坐标

void car_half_left(u16 left_time);				//左转带时间函数
void car_half_right(u16 right_time);			//右转延时时间函数

extern int current_x;
extern int current_y;
extern int current_d;
u8 longline_flag(u8 x,u8 y);   //判断是否在横线中间
u8 shortline_flag(u8 x,u8 y);  //判断是否在竖线中间
u8 front_flag(u8 x,u8 y);      //判断是否在 1 方向边上
u8 back_flag(u8 x,u8 y);       //判断是否在 3 方向边上
u8 crossroad_flag(u8 x,u8 y);              //判断是否在十字路口处

void car_curxyd(u8 x,u8 y,u8 d);     //设置小车起始点坐标及方向
void way_car(u8 x,u8 y,u8 d);        //传入目标点，并走到目标点
u8 breadth(void);                          //调用广度优先蔓延到终点
void recursive(u8 front);              //递归算法找到最佳路径
void way_run(u8 next_x,u8 next_y);    //小车走到下一步
void way_dir(u8 this_d,u8 next_d);    //调转方向，当前点方向，目标方向
void way_init(void);

u8 obstacle_check(u8 x,u8 y);					//判断是否在标志物处，若是不进行该点超声波避障
void updata_luxin(u8 x,u8 y,u8 d);

		 				    
#endif






