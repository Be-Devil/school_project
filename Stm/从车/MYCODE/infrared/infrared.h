#ifndef _INFRARED_H
#define _INFRARED_H	 
#include "sys.h"

extern u8 LITISHOW_X ;			//小车在地图的x坐标	(1-0x30)
extern u8 LITISHOW_Y ;			//小车在地图的y坐标（B-0x61)
	

 extern u8 H_1[4];
 extern u8 H_2[4];
 extern u8 H_3[4];

extern u8 license_plate1[3];
extern u8 license_plate2[3] ;

extern u8 hw_check_data;


void hw_light(int light_ed); //光照档位设置
u8 hw_light_check(void);	//光照档位检测

//void litishow(u8 da1,u8 da2,u8 da3,u8 da4,u8 da5);
void litishow_carnum(u8 da1,u8 da2);
void litishow_dir(void);



extern u8 H_1[4];	 //光源档位加1
extern u8 H_2[4];	 //光源档位加2
extern u8 H_3[4];	 //光源档位加3

#endif

