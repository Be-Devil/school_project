/************************************************************
**  智能小车核心板控制程序
**	www.r8c.com
**  版本号：V1.1
**  时间  ：2017.03.08
**  作者  ：BKRC
**  修改记录： 增加CAN接口
**  推荐使用 参数 ： 寻迹速度 50   转弯速度  80
*************************************************************/
#include "stm32f10x.h"  //包含
#include "sys.h"
#include "usart.h"
#include "delay_drv.h"
#include "init.h"
#include "led.h"
#include "test.h"
#include "djqd.h"
#include "key.h"
#include "xj.h"
#include "csb.h"
#include "hw.h"
#include "74hc595.h"
#include "bh1750.h"
#include "uart_my.h"
#include "canp_hostcom.h"
#include "can_drv.h"
#include "fifo_drv.h"
#include "data_channel.h"
#include "power_check.h"
#include "syn7318.h"
#include "iic.h"
#include "hmc5883l.h"
#include "feedback.h"
#include "rc522_my.h"
#include "code.h"
#include "move.h"
#include "delay.h"
#include "timer.h"
#include "infrared.h"
#include "zigbee.h"

#define  NUM  11 	 // 定义接收数据长度
#define  ZCKZ_ADDR    0xAA  // 定义运输标志物地址编号
#define  YSBZW_ADDR   0x02  // 定义运输标志物地址编号
#define  DZ_ADDR      0x03  // 定义道闸标志物地址编号
#define  LEDXS_ADDR   0x04  // 定义LED显示标志物地址编号
#define  JXB_ADDR     0x05  // 定义机械臂标志物地址编号


void IO_Init(void); //IO初始化
void check_send4state(void);		//回传状态值

//u8 mp;
u8 G_Tab[6]={0x03,0x05,0x14,0x45,0xDE,0x92};	   //定义红外发射数组（默认红外报警开）
u8 S_Tab[NUM]; 	   //定义主返回数据数组
//u8 C_Tab[NUM]; 	   //定义运动标志物返回数据数组

u8 Stop_Flag=0;    //状态标志位
u8 Track_Flag=0;   //循迹标志位
u8 G_Flag=0;	   //前进标志位
u8 B_Flag=0;	   //后退标志位
u8 L_Flag=0;	   //左转标志位
u8 R_Flag=0;	   //右转标志位
u8 SD_Flag=1;      //运动标志物数据返回允许标志位
u8 Rx_Flag =0;

u16 CodedDisk=0;   //码盘值统计
u16 tempMP=0;	   //接收码盘值
u16 MP;			   //控制码盘值
int Car_Spend = 40;//小车速度默认值
int count = 0;	   //计数
int LSpeed;		   //循迹左轮速度
int RSpeed;		   //循迹右轮速度
u8 Line_Flag=0;	   //
u8 send_Flag=0;	   // 发送标志位

unsigned Light=0; //光照度

extern u8 WRITE_RFID[16];
extern u8 read_rfid_my[16];
extern u8 rfid_num2_state[19];

extern u8 LITISHOW_X ;			//小车在地图的x坐标	(1-0x30)
extern u8 LITISHOW_Y ;			//小车在地图的y坐标（B-0x61)
// 主函数

RCC_ClocksTypeDef RCC_Clocks;

void SysTick_Handler(void)
{
	global_times++;
	if(delay_ms_const)
		delay_ms_const--;
}


extern void pwm_test(void);
u8 etc_flag=0;
u8 etc_control=0;
u8 etc_x = '4'-0x30;
u8 etc_y = 'c'-0x61;
u8 voice_flag = 0;
int main(void)
{
	u32 ut=0;
	u16 track_temp_data =0;
	global_times = 0;
	SystemInit();
	delay_init_0(72);
	Delay_us_Init();
	RCC_GetClocksFreq(&RCC_Clocks);
	SysTick_Config(RCC_Clocks.HCLK_Frequency / 1000);
	NVIC_Configuration(); 	 //设置NVIC中断分组0:2位抢占优先级，2位响应优先级
	TIM7_Int_Init(4999,7199);
	TIM8_Int_Init(99,79);//((1+TIM_Prescaler)/72M)*(1+TIM_Period )

	uart1_init(115200);	    // 串口初始化为115200
	SYN7318_Init();
	IO_Init();                  //IO初始化
	S_Tab[0]=0x55;
	S_Tab[1]=0xaa;
	
	CanP_Init();
	STOP();
	Host_Close_UpTrack();
 	Set_Track_Init();
	rc522_init();
	
	Power_Check();  	//电量检测  无需实时操作
	Send_Electric( Electric_Buf[0],Electric_Buf[1]);	
	
	while(1)
	{
		ut ++;

		if( !(ut %499999))
		{
			Send_Debug_Info("@",1); // 上传调试信息
			LED0=!LED0;			//程序运行状态
			Power_Check();  	//电量检测  无需实时操作
			Set_Track_Init();
			Send_Electric( Electric_Buf[0],Electric_Buf[1]);
			if(etc_flag>0&&etc_flag<10)
			{
				if(3==etc_control++)
				{
					if(etc_flag>2)
						{
							etc_flag=0;
							back_function();
							mask_M(etc_x,etc_y,1);
						}
					else
						{
							car_back(40);
							delay_ms(100);
							car_go(40);
							etc_flag++;
						}
					etc_control=0;
				}
			}
		}
		if(!KEY0)			  //按键K1按下
		{
			delay_ms(10);
			while(!KEY0);
//			beep = 1;
//			delay_ms(1000);
//			beep = 0;
//			car_curxyd('1'-0x30,'f'-0x61,3);
//			way_car('4'-0x30,'b'-0x61,0);
//			way_car('1'-0x30,'f'-0x61,0);
			voice_test("一二三木头人",12);
			while(KEY0);
			delay_ms(10);
		}
		if(!KEY1)			//按键K2按下
		{
			delay_ms(10);
			while(!KEY1);
			{
				myrc522_write(12,WRITE_RFID);
			}
		}
		if(!KEY2)			  //按键K3按下
		{
			delay_ms(10);
			while(!KEY2);
			SYN7318_Open();			 // 三合一测试
			Three_One_Test( 1);   // 0单次识别   1多次识别
		}
		if(!KEY3)			  //按键K4按下
		{
			delay_ms(10);
			if(!KEY3)
			{
				back_function(); 
			}
		}

		if(Wifi_Rx_flag ==1)  // wifi 接收标记
		{
			Wifi_Rx_flag =0;

			if(Wifi_Rx_Buf[0]==0xFD)  // 接收到的语音控制数据，直接转发
			{
//				for(ut=0;ut<5;ut++){send_data_zigbee( Wifi_Rx_Buf , (Wifi_Rx_num +1));delay_ms(2);}
//				delay_ms(20);
//				back_function();
				if( (last_code[1]!=Wifi_Rx_Buf[2]) ||
				(last_code[2]!=Wifi_Rx_Buf[5]) ||
				(last_code[3]!=Wifi_Rx_Buf[6]) ||
				(last_code[4]!=Wifi_Rx_Buf[7])||
				(last_code[5]!=Wifi_Rx_Buf[8])	)	//判断是否与上次接受的指令不同
				{
					OK_flag=0;voice_flag = 1;
					last_code[1]=Wifi_Rx_Buf[2]; 
					last_code[2]=Wifi_Rx_Buf[5];
					last_code[3]=Wifi_Rx_Buf[6];
					last_code[4]=Wifi_Rx_Buf[7];
					last_code[5]=Wifi_Rx_Buf[8];	
					TIM_Cmd(TIM7,ENABLE);
				}
				else 									//否则返回应答
					back_function();
				
			}
			else if(Wifi_Rx_Buf[0]==0x55)  // 接收到55开头数据
			{
				Normal_data();  //正常数据处理
			}
			else {
				Abnormal_data();      // 异常数据处理
				Wifi_Rx_num=7;
			}

		}
		if(voice_flag == 1)
		{
			TIM_Cmd(TIM7,DISABLE);
			Stop_Flag=Zigb_Rx_Buf[4] ;	  // 语音播报完成
			Zigbee_Rx_flag =0;
			back_function();
			voice_flag = 0;
		}

		if(Rx_Flag ==1)	//接收到控制指令
		{

		STOP();
		delay_us(5);


		if(Wifi_Rx_Buf[1]==ZCKZ_ADDR) 	   //主车控制
		{
			 switch(Wifi_Rx_Buf[2])
		    {
					case 0x01:	// 停止
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
	                	STOP();
						break;
					case 0x02:	//前进
						MP=0;G_Flag=1;	Stop_Flag=0;tempMP=0;
						tempMP=Wifi_Rx_Buf[5];tempMP<<=8;tempMP|=Wifi_Rx_Buf[4];
						Car_Spend = Wifi_Rx_Buf[3];
						Control(Car_Spend,Car_Spend);
						while(Stop_Flag!=0x03);
						break;
					case 0x03:	//后退
						MP=0;B_Flag=1;	Stop_Flag=0;tempMP=0;
						tempMP=Wifi_Rx_Buf[5];tempMP<<=8; tempMP+=Wifi_Rx_Buf[4];
						Car_Spend = Wifi_Rx_Buf[3];
						Control(-Car_Spend,-Car_Spend);
						while(Stop_Flag!=0x03);
						break;
					case 0x04:	//左转
//						MP=0;L_Flag=1;	Stop_Flag=0;
//						Car_Spend = Wifi_Rx_Buf[3];
//						Host_Open_UpTrack( Up_Track_Speed );
//						Control(-Car_Spend,Car_Spend);
//						while(Stop_Flag!=0x02);
						way_dir(Wifi_Rx_Buf[3],Wifi_Rx_Buf[4]);
						break;
					case 0x05:	//右转
						MP=0;R_Flag=1;	Stop_Flag=0;
						Car_Spend = Wifi_Rx_Buf[3];
						Host_Open_UpTrack( Up_Track_Speed );
						Control(Car_Spend,-Car_Spend);
						while(Stop_Flag!=0x02);
						break;
					case 0x06:	 //循迹
						car_xj();
						break;
					case 0x07:	//码盘值清零
						CodedDisk=0;
						break;
					case 0x08:
						//立体显示
						litishow_dir();
						break;
					case 0x09:				//改变半转弯位置
						LITISHOW_X=Wifi_Rx_Buf[4]-0x30;
						LITISHOW_Y=Wifi_Rx_Buf[3]-0x61;
						break;
					
					case 0x10:  //红外数据前三位
						G_Tab[0]=Wifi_Rx_Buf[3];
						G_Tab[1]=Wifi_Rx_Buf[4];
						G_Tab[2]=Wifi_Rx_Buf[5];
						break;
					case 0x11: 	//红外数据后三位
						G_Tab[3]=Wifi_Rx_Buf[3];//数据第四位
						G_Tab[4]=Wifi_Rx_Buf[4];//低位校验码
						G_Tab[5]=Wifi_Rx_Buf[5];//高位校验码
						break;
					case 0x12://通知小车单片机发送红外线
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						Transmition(G_Tab,6);
						break;

					case 0x20:	//转向灯控制
						if(Wifi_Rx_Buf[3])	LED_L=0;
						else 	LED_L=1;
						if(Wifi_Rx_Buf[4])	LED_R=0;
						else 	LED_R=1;
						break;
					case 0x30:	//蜂鸣器控制
						if(Wifi_Rx_Buf[3])	BEEP=0;
						else 	BEEP=1;
						break;

					case 0x31:		//小beep3声
						beep = 0; delay_ms(300);beep = 1; delay_ms(300);
						beep = 0; delay_ms(300);beep = 1; delay_ms(300);
						beep = 0; delay_ms(300);beep = 1; delay_ms(300);
						break;

					case 0x35:		//设置当前点
						car_curxyd(Wifi_Rx_Buf[4]-0x30,Wifi_Rx_Buf[3]-0x61,Wifi_Rx_Buf[5]);
						break;


					case 0x36:		//到达目标点
						way_car(Wifi_Rx_Buf[4]-0x30,Wifi_Rx_Buf[3]-0x61,Wifi_Rx_Buf[5]);
						break;
					
					case 0x38:		//地图掩码
						mask_M(Wifi_Rx_Buf[4]-0x30,Wifi_Rx_Buf[3]-0x61,Wifi_Rx_Buf[5]);
						break;
					case 0x39:		//方向行走控制
						dir_change(Wifi_Rx_Buf[3],Wifi_Rx_Buf[4]);//flag:1允许，0禁止。dir:2右，3后，4左
						break;

					case 0x40:	//双色灯控制
					//	Write_595(Wifi_Rx_Buf[3]);
						break;
					case 0x50:  //红外发射控制相片上翻
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
	                	STOP();
						Transmition(H_S,4);
						break;
					 case 0x51:    //红外发射控制相片下翻
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						Transmition(H_X,4);
						break;
					 
					 case 0x54:    //红外发射车牌号码
						litishow_carnum(LITISHOW_X,LITISHOW_Y);
						break;

					  case 0x60:    //红外发射控制光照档强度
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						hw_light(Wifi_Rx_Buf[3]);
						break;
					 case 0x61:    //红外发射控制光源强度档位加1
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						beep=1;
						delay_ms(100);
						beep=0;
						Transmition(H_1,4);
						break;


					 case 0x62:    //红外发射控制光源强度档位加2
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						Transmition(H_2,4);
						break;

					 case 0x63:    //红外发射控制光源强度档位加3
						Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
	                	STOP();
						Transmition(H_3,4);
						break;
					 case 0x70:				//etc_flag控制打开
						if(Wifi_Rx_Buf[3]==0xaa)
						{
							etc_flag = 1;
							mask_M(etc_x,etc_y,0);
						}
						else if(Wifi_Rx_Buf[3]==0x02)
						{
							etc_flag = 10;
							Wifi_Rx_Buf[1]=0x02;
							Wifi_Rx_Buf[2]=0x70;
							Wifi_Rx_Buf[3]=0x01;
							Wifi_Rx_Buf[6]=0x71;
							TIM_Cmd(TIM7, ENABLE);
						}
						 
					 break;
						
					 case 0x71:  //车牌数据前三位
						license_plate1[0]=Wifi_Rx_Buf[3];
						license_plate1[1]=Wifi_Rx_Buf[4];
						license_plate1[2]=Wifi_Rx_Buf[5];
						break;
					case 0x72: 	//车牌数据后三位
						license_plate2[0]=Wifi_Rx_Buf[3];
						license_plate2[1]=Wifi_Rx_Buf[4];
						license_plate2[2]=Wifi_Rx_Buf[5];
						break;
					
					case 0x73: 	//备用
						Wifi_Rx_Buf[3]=Wifi_Rx_Buf[3];
						Wifi_Rx_Buf[4]=Wifi_Rx_Buf[4];
						Wifi_Rx_Buf[5]=Wifi_Rx_Buf[5];
						break;
						
					case 0x75:				//rfid_do控制打开
						rfid_do	= 1;
						break;
					
				   case 0x80:	//运动标志物数据返回允许位
						check_send4state();
						break;
				   
				   case 0x81:	//rfid信息回
						rc522_read(12,read_rfid_my);
						rfid_num2_state[0]=0x55;
						rfid_num2_state[1]=0x81;
						rfid_num2_state[18]=0xbb;
						for(ut =0 ;ut<16;ut++)
							rfid_num2_state[ut+2]=read_rfid_my[ut];
						RFID_state();
						break;
				   case 0x82:	//简单信息返回
						data_other(Wifi_Rx_Buf[3]);
						break;
				   
				   case 0x83:	//光档信息返回
							//测量光档值
							hw_check_data = hw_light_check();
							data_other(2);//设置光档值返回
							break;

					case 0x90:	//语音控制命令
							SYN7318_Test_();
						break;
					case 0xA0:	// 循迹板设置命令
						Set_Track_Init();  // 恢复默认值
						break;

					case 0xA1:	// 循迹板设置命令
						track_temp_data =	(Wifi_Rx_Buf[3] <<8)+Wifi_Rx_Buf[4];
						Set_Track_Pwr( track_temp_data);  // 设置功率
					break;

					case 0xA2:	// 循迹板设置命令  55 aa a0 0x xh xl sum bb
						track_temp_data =(Wifi_Rx_Buf[4] <<8)+Wifi_Rx_Buf[5];
						Set_Track_Yzbj(Wifi_Rx_Buf[3],track_temp_data);  // 设置单个阈值
						break;

					case 0XF0:	//蜂鸣器3声
						beep = 1; delay_ms(200); beep = 0; delay_ms(200);
						beep = 1; delay_ms(200); beep = 0; delay_ms(200);
						beep = 1; delay_ms(200); beep = 0;
						break;
					case 0XF1:	//蜂鸣器2声
						beep = 1; delay_ms(200); beep = 0; delay_ms(200);
						beep = 1; delay_ms(200); beep = 0;
						break;
					case 0XFF:	
						
						break;

					default:Track_Flag=0;MP=0;
						Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0;
		                STOP();
						break;
				}
				if(((Wifi_Rx_Buf[2]&0xf0)!=0x80)&& (Wifi_Rx_Buf[2]!=0x70))//Wifi_Rx_Buf[2]!=0x80&&Wifi_Rx_Buf[2]!=0x81&&Wifi_Rx_Buf[2]!=0x82)		//回馈信息指令
				{
					back_function();
				}
				Rx_Flag =0;
		}
		else if(Wifi_Rx_Buf[1]==0x02)//||Wifi_Rx_Buf[1]==0x03)
		{
			TIM_Cmd(TIM7, ENABLE);  //使能TIMx		//打开定时器循环发送
		}
		else
		{
			for(ut = 0;ut<3;ut++)
			{
				send_data_zigbee( Wifi_Rx_Buf ,8);			  // 非主车数据转发给其他zigbee节点)
				delay_ms(10);
			}
			back_function();
		}
	}

	if(Zigbee_Rx_flag ==1)	 //zigbee返回信息
	{
		STOP();
		delay_us(5);
		if(Zigb_Rx_Buf[1]==0x02)	 //从车状态返回值
		{
			TIM_Cmd(TIM7, DISABLE);	//关闭定时器发送
			if(etc_flag == 0)
				back_function();
		}
		else if(Zigb_Rx_Buf[1]==0x21)	 //第一组数据
		{
			feedback_state[0]= 0x55;
			feedback_state[1]= 0x02;
			feedback_state[2]=Zigb_Rx_Buf[2];
			feedback_state[3]=Zigb_Rx_Buf[3];
			feedback_state[4]=Zigb_Rx_Buf[4];
			feedback_state[5]=Zigb_Rx_Buf[5];
			feedback_state_flag1=1;
		}
		else if(feedback_state_flag1==1&&Zigb_Rx_Buf[1]==0x22)	 //第二组数据
		{
			//关闭定时器发送
			TIM_Cmd(TIM7, DISABLE);
			feedback_state[6]=Zigb_Rx_Buf[2];
			feedback_state[7]=Zigb_Rx_Buf[3];
			feedback_state[8]=Zigb_Rx_Buf[4];
			feedback_state[9]=Zigb_Rx_Buf[5];
			feedback_state[10] = 0xBB;
			feedback_state_flag1=0;
			back_state();
		}
		
		else if(Zigb_Rx_Buf[1]==0x40)
		{
			voice_appoint(Zigb_Rx_Buf[2]);
		}
		else if(Zigb_Rx_Buf[1]==0x41)	 //第一组数据
		{
			rfid_num2_state[0]= 0x55;
			rfid_num2_state[2]=Zigb_Rx_Buf[2];
			rfid_num2_state[3]=Zigb_Rx_Buf[3];
			rfid_num2_state[4]=Zigb_Rx_Buf[4];
			rfid_num2_state[5]=Zigb_Rx_Buf[5];
		}
		else if(Zigb_Rx_Buf[1]==0x42)	 //第二组数据
		{
			rfid_num2_state[1]= 0x81;
			rfid_num2_state[6]=Zigb_Rx_Buf[2];
			rfid_num2_state[7]=Zigb_Rx_Buf[3];
			rfid_num2_state[8]=Zigb_Rx_Buf[4];
			rfid_num2_state[9]=Zigb_Rx_Buf[5];
			
		}
		else if(Zigb_Rx_Buf[1]==0x43)	 //第三组数据
		{
			rfid_num2_state[18]= 0xbb;
			rfid_num2_state[10]=Zigb_Rx_Buf[2];
			rfid_num2_state[11]=Zigb_Rx_Buf[3];
			rfid_num2_state[12]=Zigb_Rx_Buf[4];
			rfid_num2_state[13]=Zigb_Rx_Buf[5];
		}
		else if(rfid_num2_state[0]==0x55&&rfid_num2_state[1]==0x81&&rfid_num2_state[18]==0xbb&&Zigb_Rx_Buf[1]==0x44)	 //第二组数据
		{
			TIM_Cmd(TIM7, DISABLE);
			rfid_num2_state[14]=Zigb_Rx_Buf[2];
			rfid_num2_state[15]=Zigb_Rx_Buf[3];
			rfid_num2_state[16]=Zigb_Rx_Buf[4];
			rfid_num2_state[17]=Zigb_Rx_Buf[5];
			RFID_state();
		}
		else if(Zigb_Rx_Buf[1]==0x82)
		{
			etc_flag = 0;
			TIM_Cmd(TIM7, DISABLE);
			for(ut=2;ut<7;ut++)
				other_data[ut]=Zigb_Rx_Buf[ut];
			data_other(0);
		}
		else if(Zigb_Rx_Buf[1]==0x90)	//语音控制
		{
			Zigb_Rx_Buf[1]=0x06;
			send_data_zigbee(Zigb_Rx_Buf,8);
			delay_ms(10);
		}
		else if( Zigb_Rx_Buf[1]==0x03) // 道闸
		{
		   if(Zigb_Rx_Buf[2]==0x01)
		   {
				if(Zigb_Rx_Buf[3]==0x01)
				{
					//关闭定时器发送
					TIM_Cmd(TIM7,DISABLE);
					Stop_Flag=Zigb_Rx_Buf[4] ;	  // 05 车道道闸完成 
					Zigbee_Rx_flag =0;
					if(Zigb_Rx_Buf[4]==0x55)
						send_data_zigbee(DZ_K,8);
//					if(Zigb_Rx_Buf[4]==0x05)
//						back_function();
				}
				else Zigbee_Rx_flag =0;
		   }
		   else Zigbee_Rx_flag =0;
		}
		else if( Zigb_Rx_Buf[1]==0x06) // 语音
		{
		   if(Zigb_Rx_Buf[2]==0x01)
		   {
				if(Zigb_Rx_Buf[3]==0x41||Zigb_Rx_Buf[3]==0x4f)
				{
					//关闭定时器发送
					TIM_Cmd(TIM7,DISABLE);
					Stop_Flag=Zigb_Rx_Buf[4] ;	  // 语音播报完成
					Zigbee_Rx_flag =0;
					if(voice_flag == 1)
					{
						back_function();
						voice_flag = 0;
					}
				}
				else Zigbee_Rx_flag =0;
		   }
		   else Zigbee_Rx_flag =0;
		}
		
		else if(Zigb_Rx_Buf[1]==0x0c) // ETC
		{
		   if(Zigb_Rx_Buf[2]==0x01)
		   {
				if(Zigb_Rx_Buf[3]==0x01)
				{
					Stop_Flag=Zigb_Rx_Buf[4] ;	  //06 ETC
					Zigbee_Rx_flag =0;
					if(etc_flag > 0)
					{
						if(etc_flag>9)
						{
							Wifi_Rx_Buf[1]=0x02;
							Wifi_Rx_Buf[2]=0x70;
							Wifi_Rx_Buf[3]=0x00;
							Wifi_Rx_Buf[6]=0x70;
							TIM_Cmd(TIM7, ENABLE);
						}
						else back_function();	
					}	
					etc_flag=0;
				}
				else Zigbee_Rx_flag =0;
		   }
		   else Zigbee_Rx_flag =0;
		}
		
		else Zigbee_Rx_flag =0;
		Zigbee_Rx_flag =0;
	}
}

}

/***************************************************************
** 功能：     初始化核心板所使用端口
** 参数：	  无参数
** 返回值：    无
****************************************************************/
void IO_Init(void)
{
	YJ_INIT();			//初始化硬件

	GPIOB->CRH&=0XFFFFFFF0;
	GPIOB->CRH|=0X00000008;//PB8 设置成输入
	GPIOB->ODR|=1<<8;	   //PB8上拉

	GPIOC->CRH&=0X000FFFFF;
	GPIOC->CRH|=0X33300000;   //PC13/PC14/PC15推挽输出
  GPIOC->ODR|=0XE000;       //PC13/PC14/PC15输出高

	GPIOD->CRH&=0XFFF0FFFF;
	GPIOD->CRH|=0X00030000;   //PD12推挽输出
  GPIOD->ODR|=0X1000;       //PD12推挽高

	LED_L=1;
	LED_R=1;
	BEEP=1;

	beep=0;

}

extern u8 feedback_state[11];
void check_send4state()
{
	u8 i;
	u8 j;
	u16 temp;
	u16 x[2][10];
	S_Tab[2]=Stop_Flag;

	if(PSS==1) S_Tab[3]=1;
	else S_Tab[3]=0;

	for(i=0;i<10;i++)
	{
		tran();
		x[0][i]=dis;
		x[1][i]= Dispose();	//??
	}
	S_Tab[8]=CodedDisk%256;  //??
	S_Tab[9]=CodedDisk/256;

	for (j = 0; j < 9; j++)
	{
		for (i = 0; i < 9 - j; i++)
		{
			if (x[0][i] > x[0][i+1])
			{
			temp = x[0][i];
			x[0][i] = x[0][i+1];
			x[0][i+1] = temp;

			temp = x[1][i];
			x[1][i] = x[1][i+1];
			x[1][i+1] = temp;
			}
		}
	}
	x[0][1] = (x[0][2]+x[0][3]+x[0][4]+x[0][5]+x[0][6]+x[0][7])/6;
	x[1][1] = (x[1][2]+x[1][3]+x[1][4]+x[1][5]+x[1][6]+x[1][7])/6;

	S_Tab[4]=x[0][1]%256;
	S_Tab[5]=x[0][1]/256;

	S_Tab[6]=x[1][1]%256;
	S_Tab[7]=x[1][1]/256;

	S_Tab[10] = 0xbb;

	for(i=0;i<11;i++)		//save state
	{
		feedback_state[i]=S_Tab[i];
	}
	back_state();			//return state
}
