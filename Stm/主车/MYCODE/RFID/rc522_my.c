#include <stm32f10x.h>
#include "rc522_my.h"
#include "delay.h"
#include "rc522.h"
#include "led.h"
#include "test.h"
#include "usart.h"


u8 KEY_A[6]={0xff,0xff,0xff,0xff,0xff,0xff};   	// A密钥
u8 WRITE_RFID[16]={0x01 ,0x02 ,0x03 ,0x04 ,0x05 ,0xD0 ,0xBF ,0xCB ,0xC4 ,0xD6 };
u8 read_rfid_my[16];
u8 read_rfid_my[16];

u8 rfid_do = 0;

/***************************************************************
** 功能：   RC522通信初始化
** 参数：	  无参数
** 返回值：   无
****************************************************************/
void rc522_init(void)
{
	  RC522_Uart_init(9600);
	  //delay_ms(500);
	  InitRc522();				//初始化射频卡模块
}

/***************************************************************
** 功能：   RC522读卡
** 参数：	  card_addr     要读取的数据块地址
**          RFID_DATA     存放读出数据的数组           
** 返回值：   1 读卡成功
**            0 读卡失败
****************************************************************/

int rc522_read(u8 card_addr,u8 *RFID_DATA)
{
     int status;
	   unsigned char CT[2];//卡类型
     unsigned char SN[4]; //卡号
	   unsigned char READ_RFID[16]; //读卡数据 
	    u8 i;
	
	   u8 card_key=(card_addr/4)*4+3;  // 获取块对应秘钥块区
	  
			if(card_addr>0x3f)	 // 	 S50卡块地址0~63（0x3f）
		  {
				return 0;
		  }
	
			status = PcdRequest(PICC_REQALL,CT);/*尋卡*/
	

			if(status==MI_OK)							//尋卡成功
		{
				LED1=~LED1;
				status=MI_ERR;
				status = PcdAnticoll(SN);	/*防冲撞*/			 
		}

			if (status==MI_OK)						//防衝撞成功
			{
				LED2=~LED2;
				status=MI_ERR;		
				status =PcdSelect(SN);
			}
			else
			{
			BEEP=1;	
			}

			if(status==MI_OK)//選卡成功
			{
			   	LED3=~LED3;
				status=MI_ERR;
				status =PcdAuthState(KEYA,card_key,KEY_A,SN);	//验证A密钥	  一般只验证A
			}
			
			if(status==MI_OK)//驗證成功
			{
			     LED1=~LED1;
				status=MI_ERR;
				status=PcdRead(card_addr,READ_RFID);	  //读取数据
			}
			 
			if(status==MI_OK)//讀卡成功
			{
				LED2=~LED2;								   // LED4被点亮
				status=MI_ERR;				 
				for(i=0;i<16;i++)
				{
					RFID_DATA[i]= READ_RFID[i]; 
				} 
				return 1;
			}
			else
			BEEP=1;	
			;
			return 0;

}



/***************************************************************
** 功能：   RC522写卡
** 参数：	  card_addr     要读取的数据块地址
**          RFID_DATA     存放写入数据的数组           
** 返回值：   1 读卡成功
**            0 读卡失败
****************************************************************/

int myrc522_write(u8 card_addr,u8 *RFID_DATA)
{
       int status;
	   unsigned char CT[2];//卡类型
       unsigned char SN[4]; //卡号
	
	   u8 card_key=(card_addr/4)*4+3;  // 获取块对应秘钥块区
	 
			if(card_addr>0x3f)	 // 	 S50卡块地址0~63（0x3f）
		  {
				return 0;
		  }
	
			status = PcdRequest(PICC_REQALL,CT);/*尋卡*/
	

			if(status==MI_OK)							//尋卡成功
		{
				LED1=~LED1;
				status=MI_ERR;
				status = PcdAnticoll(SN);	/*防冲撞*/			 
		}

			if (status==MI_OK)						//防衝撞成功
			{
				LED2=~LED2;
				status=MI_ERR;		
				status =PcdSelect(SN);
			}
			else
			{
			BEEP=1;	
			}

			if(status==MI_OK)//選卡成功
			{
			   	LED3=~LED3;
				status=MI_ERR;
				status =PcdAuthState(KEYA,card_key,KEY_A,SN);	//验证A密钥	  一般只验证A
			}
			
			if(status==MI_OK)//驗證成功
			{
			     LED1=~LED1;
				status=MI_ERR;
				status=PcdWrite(card_addr,RFID_DATA);	  //写入数据
			}

			if(status==MI_OK)
			{
		
				return 1;

			}
			else 
			{
				return 0;
			}

}
/***************rc522从车读卡返回数据函数***************
void rc522_read_my()
{
		u8 j =0,i=0, m = 0;
		static u8 rfid_flag_receive = 0;
		if(rfid_flag_receive == 0)
		{
			rfid_flag = rc522_read(2,read_Data);
			if(rfid_flag)
			{
//				beep = 0;
				TIM_Cmd(TIM8, DISABLE);
				Car_Spend = 70;
				rfid_flag_receive = 1;
				for(j = 0;j<4;j++)
				{
					USART2_RX_BUF[1]=0x40+j;
					USART2_RX_BUF[2]=read_Data[4*j];
					USART2_RX_BUF[3]=read_Data[4*j+1];
					USART2_RX_BUF[4]=read_Data[4*j+2];
					USART2_RX_BUF[5]=read_Data[4*j+3];
					USART2_RX_BUF[6]=(USART2_RX_BUF[2]+USART2_RX_BUF[3]+USART2_RX_BUF[4]+USART2_RX_BUF[5])%256;
					USART2_RX_BUF[7]=0xbb;
					for(m = 0 ;m < 3;m++)
					{
						delay_ms(100);
						for(i=0;i<8;i++)		
						{
							U2SendChar(USART2_RX_BUF[i]);	//回复应答
						}	
					}
				}
				//beep = 1;
				 
			}
	}
}
*/
