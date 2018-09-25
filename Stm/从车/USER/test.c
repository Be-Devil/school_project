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
u8 G_Tab[6]={0x03,0x05,0x14,0x45,0xDE,0x92};;	   //定义红外发射数组
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
int Car_Spend = 70;//小车速度默认值
int count = 0;	   //计数
int LSpeed;		   //循迹左轮速度
int RSpeed;		   //循迹右轮速度
u8 Line_Flag=0;	   // 
u8 send_Flag=0;	   // 发送标志位

unsigned Light=0; //光照度

extern u8 WRITE_RFID[16];
extern u8 read_rfid_my[16];
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
int main(void)
{
	u32 ut=0;
	u8 debug_flag=0;
//	u8 i;
	u16 track_temp_data =0;
	global_times = 0;
	SystemInit();
	Delay_us_Init();
	RCC_GetClocksFreq(&RCC_Clocks);
	SysTick_Config(RCC_Clocks.HCLK_Frequency / 1000);	
	

	delay_init_0(72);
	NVIC_Configuration(); 	 //设置NVIC中断分组0:2位抢占优先级，2位响应优先级
	
	TIM8_Int_Init(99,79);//((1+TIM_Prescaler)/72M)*(1+TIM_Period )

	
	uart1_init(115200);	    // 串口初始化为115200
	SYN7318_Init();
	

	IO_Init();                  //IO初始化

	S_Tab[0]=0x55;
	S_Tab[1]=0x21;
	
	CanP_Init();
	

	STOP();
	Host_Close_UpTrack();
	
	Send_Debug_Info("A1B2C3\n",8); // 上传调试信息

 	Set_Track_Init();
	rc522_init();
	
	while(1)
	{	 	   
		ut ++;
		if( !(ut %499999))
		{		
			Send_Debug_Info("@",1); // 上传调试信息
			LED0=!LED0;			//程序运行状态	
			Set_Track_Init();
			Power_Check();  	//电量检测  无需实时操作
			
			Send_Electric( Electric_Buf[0],Electric_Buf[1]);	
			if(etc_flag>0)
			{
				if(3==etc_control++)
				{
					if(etc_flag>2)
						{
							etc_flag=0;
							mask_M(etc_x,etc_y,1);
							data_other(1);			//返回当前坐标（正常反馈已屏蔽，用数据反馈代替正常反馈）
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
			delay_ms(30);
			while(!KEY0);
			{
				beep = 1;
				delay_ms(1000);
				beep = 0;
				car_curxyd('1'-0x30,'f'-0x61,3);
				way_car('4'-0x30,'b'-0x61,0);
				way_car('1'-0x30,'f'-0x61,0);
			}
		}
		if(!KEY1)			//按键K2按下
		{  
			
			delay_ms(10);
			while(!KEY1);
			{
				check_send4state();
			}
			
		}
		if(!KEY2)			  //按键K3按下
		{
			delay_ms(10);
			while(!KEY2); 
			{			
//				SYN7318_Open();			 // 三合一测试
//				Three_One_Test( 1);   // 0单次识别   1多次识别
				back_function();
				
			}
		}
		if(!KEY3)			  //按键K4按下
		{
			delay_ms(10);
			if(!KEY3)
			{
				 back_function(); 
			}
		}
	
		if(Zigbee_Rx_flag ==1)	 //zigbee返回信息
		{

			STOP();
			delay_us(5);
				

			if(Zigb_Rx_Buf[1]==YSBZW_ADDR) 	   //从车控制
			{
				 switch(Zigb_Rx_Buf[2])
				{
						case 0x01:	// 停止
							if(debug_flag==0){
								Track_Flag=0;MP=0;
								Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
								STOP();
							}
							else {SYN_TTS("停止");delay_ms(500);}
							break;
						case 0x02:	//前进
							if(debug_flag==0){
								MP=0;G_Flag=1;	Stop_Flag=0;tempMP=0;			 					
								tempMP=Zigb_Rx_Buf[5];tempMP<<=8;tempMP|=Zigb_Rx_Buf[4];
								Car_Spend = Zigb_Rx_Buf[3];
								Control(Car_Spend,Car_Spend);
								while(Stop_Flag!=0x03);
							}
							else {SYN_TTS("前进");delay_ms(500);}
							break;
						case 0x03:	//后退
							if(debug_flag==0){
								MP=0;B_Flag=1;	Stop_Flag=0;tempMP=0;
								tempMP=Zigb_Rx_Buf[5];tempMP<<=8; tempMP+=Zigb_Rx_Buf[4];
								Car_Spend = Zigb_Rx_Buf[3];
								Control(-Car_Spend,-Car_Spend);
								while(Stop_Flag!=0x03);
							}
							else {SYN_TTS("后退");delay_ms(500);}
							break;
						case 0x04:	//左转
							if(debug_flag==0){
//								MP=0;L_Flag=1;	Stop_Flag=0;
//								Car_Spend = Zigb_Rx_Buf[3];
//								Host_Open_UpTrack( Up_Track_Speed );
//								Control(-Car_Spend,Car_Spend);
//								while(Stop_Flag!=0x02);
								way_dir(Zigb_Rx_Buf[3],Zigb_Rx_Buf[4]);
							}
							else {SYN_TTS("转弯");delay_ms(500);}
							break;
						case 0x05:	//右转
							if(debug_flag==0){
								MP=0;R_Flag=1;	Stop_Flag=0;
								Car_Spend = Zigb_Rx_Buf[3];
								Host_Open_UpTrack( Up_Track_Speed );
								Control(Car_Spend,-Car_Spend);
								while(Stop_Flag!=0x02);
							}
							else {SYN_TTS("右转");delay_ms(500);}
							break;
						case 0x06:	 //循迹
							if(debug_flag==0){
//								Car_Spend = Zigb_Rx_Buf[3];
//								Track_Flag=1;MP=0; 					  
//								Host_Open_UpTrack( Up_Track_Speed );  // 开启寻迹数据上传
//								delay_ms( 100); // 等待一会寻迹数据上传
//								TIM_Cmd(TIM2, ENABLE);
								car_xj();
							}
							else {SYN_TTS("循迹");delay_ms(500);}
							break;
						case 0x07:	//码盘值清零
							if(debug_flag==0)
								CodedDisk=0;
							else {SYN_TTS("码盘清零");delay_ms(500);}
							break;
						case 0x08://立体显示转弯
							if(debug_flag==0)
								litishow_dir();
							else {SYN_TTS("立体转弯");delay_ms(500);}
							break;
						case 0x09:				//改变半转弯位置
							if(debug_flag==0){
								LITISHOW_X=Zigb_Rx_Buf[4]-0x30;
								LITISHOW_Y=Zigb_Rx_Buf[3]-0x61;
							}
							else {SYN_TTS("改变位置");delay_ms(500);}
							break;

						case 0x10:  //红外数据前三位
							if(debug_flag==0){
								G_Tab[0]=Zigb_Rx_Buf[3];
								G_Tab[1]=Zigb_Rx_Buf[4];
								G_Tab[2]=Zigb_Rx_Buf[5];
							}
							else {SYN_TTS("红外前三");delay_ms(500);}
							break;
						case 0x11: 	//红外数据后三位
							if(debug_flag==0){
								G_Tab[3]=Zigb_Rx_Buf[3];//数据第四位
								G_Tab[4]=Zigb_Rx_Buf[4];//低位校验码
								G_Tab[5]=Zigb_Rx_Buf[5];//高位校验码
							}
							else {SYN_TTS("红外后三");delay_ms(500);}
							break;
						case 0x12://通知小车单片机发送红外线
							if(debug_flag==0){
								Track_Flag=0;MP=0;
								Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
								STOP();
								Transmition(G_Tab,6);
							}
							else {SYN_TTS("红外发");delay_ms(500);}
							break;	
												
						case 0x20:	//转向灯控制
							if(Zigb_Rx_Buf[3])	LED_L=0;
							else 	LED_L=1;
							if(Zigb_Rx_Buf[4])	LED_R=0;
							else 	LED_R=1;
							break;					
						case 0x30:	//蜂鸣器控制
							if(Zigb_Rx_Buf[3])	BEEP=0;
							else 	BEEP=1;
							break;
						
						case 0x31:		//小beep3声
							beep = 1; delay_ms(300);beep = 0; delay_ms(300);
							beep = 1; delay_ms(300);beep = 0; delay_ms(300);
							beep = 1; delay_ms(300);beep = 0; delay_ms(300);
						break;
					
						case 0x35:		//设置当前点
							if(debug_flag==0){
								car_curxyd(Zigb_Rx_Buf[4]-0x30,Zigb_Rx_Buf[3]-0x61,Zigb_Rx_Buf[5]);
							}
							else {SYN_TTS("当前点");delay_ms(500);}
							break;
						
						case 0x36:		//到达目标点
							if(debug_flag==0){
								way_car(Zigb_Rx_Buf[4]-0x30,Zigb_Rx_Buf[3]-0x61,Zigb_Rx_Buf[5]);
							}
							else {SYN_TTS("目标点");delay_ms(500);}
							break;
						case 0x38:		//地图掩码
							if(debug_flag==0){
								mask_M(Zigb_Rx_Buf[4]-0x30,Zigb_Rx_Buf[3]-0x61,Zigb_Rx_Buf[5]);
							}
							else {SYN_TTS("掩码");delay_ms(500);}
						break;
						
						case 0x39:		//方向行走控制
							if(debug_flag==0){
								dir_change(Zigb_Rx_Buf[3],Zigb_Rx_Buf[4]);//flag:1允许，0禁止。dir:2右，3后，4左
							}
							else {SYN_TTS("方向控制");delay_ms(500);}
						break;
						
						case 0x40:	//双色灯控制
						//	Write_595(Zigb_Rx_Buf[3]);
							break;
						case 0x50:  //红外发射控制相片上翻 
							if(debug_flag==0){
								Track_Flag=0;MP=0;
								Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
								STOP();
								Transmition(H_S,4);
							}
							else {SYN_TTS("上翻");delay_ms(500);}
							break;
						 case 0x51:    //红外发射控制相片下翻 
							if(debug_flag==0){
								Track_Flag=0;MP=0;
								Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
								STOP();
								Transmition(H_X,4);
							}
							else {SYN_TTS("下翻");delay_ms(500);}
							break;
						 
						case 0x54:    //红外发射车牌号码
							if(debug_flag==0)
								litishow_carnum(LITISHOW_X,LITISHOW_Y);
							else {SYN_TTS("车牌红外");delay_ms(500);}
							break;
						
						case 0x60:    //红外发射控制光照档强度
							if(debug_flag==0)
								hw_light((int)Zigb_Rx_Buf[3]);
							else {SYN_TTS("光档");delay_ms(500);}
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
							if(Zigb_Rx_Buf[3]==0x01)
							{
								etc_flag = 1;
								mask_M(etc_x,etc_y,0);
							}
							else if(Zigb_Rx_Buf[3]==0x00)
							{
								etc_flag = 0;
								mask_M(etc_x,etc_y,0);
							}
							break;
						case 0x71:  //车牌数据前三位
							if(debug_flag==0){
								license_plate1[0]=Zigb_Rx_Buf[3];
								license_plate1[1]=Zigb_Rx_Buf[4];
								license_plate1[2]=Zigb_Rx_Buf[5];
							}
							else {SYN_TTS("车牌前三");delay_ms(500);}
							break;
						case 0x72: 	//车牌数据后三位
							if(debug_flag==0){
								license_plate2[0]=Zigb_Rx_Buf[3];
								license_plate2[1]=Zigb_Rx_Buf[4];
								license_plate2[2]=Zigb_Rx_Buf[5];
							}
							else {SYN_TTS("车牌后三");delay_ms(500);}
							break;
							
						case 0x73: 	//备用
							Zigb_Rx_Buf[3]=Zigb_Rx_Buf[3];
							Zigb_Rx_Buf[4]=Zigb_Rx_Buf[4];
							Zigb_Rx_Buf[5]=Zigb_Rx_Buf[5];
							break;

						case 0x75:				//rfid_do控制打开
							rfid_do	= 1;
							break;
						
					   case 0x80:	//运动标志物数据返回允许位
							check_send4state();
							break;
					   
						case 0x81:	//rfid信息回
							rc522_read(12,read_rfid_my);
							RFID_state();
							break;
						
						case 0x82:	//简单信息返回
							if(debug_flag==0){
								data_other(Zigb_Rx_Buf[3]);
							}
							else {SYN_TTS("数据返回");delay_ms(500);data_other(11);}
							break;
						 
						case 0x83:	//光档信息返回
							if(debug_flag==0){
								//测量光档值
								hw_check_data = hw_light_check();
								data_other(2);//设置光档值返回
							}
							else {SYN_TTS("光档返回");delay_ms(500);data_other(11);}
							break;
							
						case 0x90:	//语音控制命令
							SYN7318_Test_();
							break;

						case 0xA0:	// 循迹板设置命令 
							Set_Track_Init();  // 恢复默认值					
							break;

						case 0xA1:	// 循迹板设置命令 
							track_temp_data =(Zigb_Rx_Buf[3] <<8)+Zigb_Rx_Buf[4];
							Set_Track_Pwr( track_temp_data);  // 设置功率				
						break;

						case 0xA2:	// 循迹板设置命令  55 aa a0 0x xh xl sum bb
							track_temp_data =(Zigb_Rx_Buf[4] <<8)+Zigb_Rx_Buf[5];
							Set_Track_Yzbj(Zigb_Rx_Buf[3],track_temp_data);  // 设置单个阈值					
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
										
						default:Track_Flag=0;MP=0;
							Stop_Flag=0;G_Flag=0;B_Flag=0;L_Flag=0;R_Flag=0; 
							STOP();
							break;											 
					}
					if((Zigb_Rx_Buf[2]&0xf0)!=0x80)		//回馈完成指令
					{
						back_function();			
					}
			}
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

extern u8 feedback_state1[8];
extern u8 feedback_state2[8];
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
		x[1][i]= Dispose();					 
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
	
	
	feedback_state1[0]=0x55;
	feedback_state1[1]=0x21;
	feedback_state1[2]=S_Tab[2];
	feedback_state1[3]=S_Tab[3];
	feedback_state1[4]=S_Tab[4];
	feedback_state1[5]=S_Tab[5];
	feedback_state1[6]=(feedback_state1[2]+feedback_state1[3]+feedback_state1[4]+feedback_state1[5])%256;
	feedback_state1[7]=0xbb;


	feedback_state2[0]=0x55;
	feedback_state2[1]=0x22;
	feedback_state2[2]=S_Tab[6];
	feedback_state2[3]=S_Tab[7];
	feedback_state2[4]=S_Tab[8];
	feedback_state2[5]=S_Tab[9];
	feedback_state2[6]=(feedback_state2[2]+feedback_state2[3]+feedback_state2[4]+feedback_state2[5])%256;
	feedback_state2[7]=0xbb;
	
	back_state();			//return state 
	
}
