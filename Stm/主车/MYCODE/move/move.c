#include <stm32f10x.h>   
#include "move.h"
#include "xj.h"
#include "csb.h"
#include "htxj.h"
#include "usart.h"
#include "canp_hostcom.h"
#include <zigbee.h>

#define Maxsize 100
u8 M[9][7]=    //�ܵ�ͼ
{
	{1,1,1,1,1,1,1},    //0.y
	{1,0,1,0,1,0,1},    //1.y
	{0,0,0,0,0,0,0},    //2.y
	{1,0,1,0,1,0,1},    //3.y
	{0,0,0,0,0,0,0},    //4.y
	{1,0,1,0,1,0,1},    //5.y
	{0,0,0,0,0,0,0},    //6.y
	{1,0,1,0,1,0,1},    //7.y
	{1,1,1,1,1,1,1},    //8.y
};
int cache[9][7];//��ʱ��ͼ
int car_go_flag = 1;
//***********************************************************************************//
//***********************************************************************************//
int JC_XY(int x,int y)
{
	if((x==2&&y==3)||(x==4&&y==4)||(x==6&&y==2)||(x==5&&y==5))
	return 1;
	return 0;
}


//***************************************************************************************//
struct path     //·�߶�ջ
{
    int i,j,p;
}Q[Maxsize];
int front=-1;
int rear=-1;

struct location//�洢·������
{
	int x,y;
}loc[Maxsize];
u8 count1=0,count2=0;
int current_x=-1;int current_y=-1;int current_d=-1;//��ǰ����,����
int target_x=-1;int target_y=-1;int target_d=-1;//Ŀ�����꣬����

//u8 obstacle_true=0;
u8 obstacle_flag=0;	   //�����ϰ���ı�־λ �����ϰ�����1
int obstacle_num=0;   //�ϰ������
int is_obstacle_black = 0;


void mask_M(u8 x,u8 y,u8 flag)		//���ε�ȡ��
{
	if(flag)
		M[x][y]=1;
	else 
		M[x][y]=0;
}

void CSXJ(int xjtime)
{
	Track_Flag=1;MP=0;
	Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
	delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
	TIM_Cmd(TIM2, ENABLE);
	delay_ms(xjtime);
	STOP1();
}



void HTXJ(int httime)
{
	delay_ms(100);
	htTrack_Flag=1;
	Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
	delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
	TIM_Cmd(TIM2, ENABLE);
	htTrack();
	delay_ms(httime);
	STOP1();
}


void STOP1(void)
{
 
	 Track_Flag=0;
	 htTrack_Flag=0;
	 STOP();
}
 
void car_go(u16 gomp)
{
	MP=0;G_Flag=1;	Stop_Flag=0;tempMP=0;			 					
	tempMP=gomp;
	Control(Car_Spend,Car_Spend);
	while(Stop_Flag!=0x03);
}
void car_left()
{	
	delay_ms(100);
	MP=0;L_Flag=1;	Stop_Flag=0;
	Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
	delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
	Control(0,0);
	delay_ms(80);
	Control(-100,100);
	delay_ms(400); 
	TIM_Cmd(TIM2, ENABLE);
	while(Stop_Flag!=0x02);
}
void car_right()
{
	delay_ms(100);
	MP=0;R_Flag=1;	Stop_Flag=0;
	Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
	delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
	Control(0,0);
	delay_ms(80);
	Control(100,-100);
	delay_ms(400); 
	TIM_Cmd(TIM2, ENABLE);
	while(Stop_Flag!=0x02);
}
void car_half_left(u16 left_time)
{
	delay_ms(50);
	MP=0;L_Flag=1;	Stop_Flag=0;
	Control(-80,80);
	delay_ms(left_time);
	STOP();
	delay_ms(50);
}
void car_half_right(u16 right_time)
{
	delay_ms(50);
	MP=0;R_Flag=1;	Stop_Flag=0;
	Control(80,-80);
	delay_ms(right_time);
	STOP();
	delay_ms(50);
}
void car_back(u16 backmp)
{
	delay_ms(100);
	MP=0;B_Flag=1;	Stop_Flag=0;tempMP=0;
	tempMP=backmp;
	Car_Spend=80;
	Control(-Car_Spend,-Car_Spend);
	while(Stop_Flag!=0x03);
}

void car_xj()
{
	Control(0,0);
	delay_ms(80);
	Control(100,100);
	Track_Flag=1;MP=0;
	Host_Open_UpTrack( Up_Track_Speed );  // ����Ѱ�������ϴ�
	delay_ms( 20); // �ȴ�һ��Ѱ�������ϴ�
	TIM_Cmd(TIM2, ENABLE);
	while(Track_Flag==1); 

}


void car_longline()
{
	CSXJ(CAR_LONG_TIME);

//    printf("\t�����е�\n");
}
void car_shortline()
{
	CSXJ(CAR_SHORT_TIME);

}


u8 longline_flag(u8 x,u8 y)   //�ж��Ƿ��ں����м�
{
    if((y==0)||(y==2)||(y==4)||(y==6))
        return 1;
    else
        return 0;
}

u8 shortline_flag(u8 x,u8 y)  //�ж��Ƿ��������м�
{
    if(((x==3)||(x==5)))
        return 1;
    else
        return 0;
}

u8 front_flag(u8 x,u8 y)      //�ж��Ƿ��� 1 �������
{
    if(x==1)
        return 1;
    else
        return 0;
}
u8 back_flag(u8 x,u8 y)       //�ж��Ƿ��� 3 �������
{
    if(x==7)
        return 1;
    else
        return 0;
}

u8 crossroad_flag(u8 x,u8 y)              //�ж��Ƿ���ʮ��·�ڴ�
{
    if(((x==2)||(x==4)||(x==6))&&((y==1)||(y==3)||(y==5)))
        return 1;
    else
        return 0;
}
/**********************����*********************************************/
u8 obstacle_check(u8 x,u8 y)					//�ж��Ƿ��ڱ�־�ﴦ�����ǲ����иõ㳬��������
{
	if(!obstacle_flag)													                                      
	{
	if(JC_XY(x,y))
		return 1; 
	else
        return 0;
	}
	else 
	{
		if( longline_flag(x,y)	)
	        return 2;
		else if( shortline_flag(x,y))
			 return 3;
		else return 1;
	}

}

u8 	 check_obstacle(u8 x,u8 y,u8 fangxiang)
{
	if(longline_flag(x,y)&& fangxiang==2)  { y++; if(JC_XY(x,y)) return 1; else return 0;}
	if(longline_flag(x,y)&& fangxiang==4)  { y--; if(JC_XY(x,y)) return 1; else return 0;}
   	if(shortline_flag(x,y)&& fangxiang==1)  { x--; if(JC_XY(x,y)) return 1; else return 0;}
   	if(shortline_flag(x,y)&& fangxiang==3)  { x++; if(JC_XY(x,y)) return 1; else return 0;}
	return 0;	
}

void updata_luxin(u8 x,u8 y,u8 d)
{
   if(d==1 && dis<=300)  M[x-1][y] =1;
   if(d==3 && dis<=300)  M[x+1][y] =1;
   if(d==2 && dis<=360)  M[x][y+1] =1;
   if(d==4 && dis<=360)  M[x][y-1] =1;

    for(count1=0;count1<9;count1++){        //��ʼ�������ͼ
		for(count2=0;count2<7;count2++){
			cache[count1][count2]=M[count1][count2];
		}
 	}

   	for(count1=0;count1<Maxsize;count1++){	//��ʼ���������
		loc[count1].x=0;
		loc[count1].y=0;
	}
    if(!breadth()){return ; }
}




void car_curxyd(u8 x,u8 y,u8 d)
{
    current_x=x;
    current_y=y;
    current_d=d;
}

void way_car(u8 x,u8 y,u8 d)
{
///////////////////////��ʼֵ�趨//////////////////////////////////
    target_x=x;                 //�����յ�����ֵ������
    target_y=y;
    target_d=d;
	

	if (obstacle_flag==1 && longline_flag(current_x,current_y) )
	{
		HTXJ(CAR_LONG_TIME);
		if(current_d==2)	{ current_y--; M[current_x][current_y+2] =1;}
		if(current_d==4)	{ current_y++; M[current_x][current_y-2] =1;}
		obstacle_flag=0;
	}
	else if (obstacle_flag==1 && shortline_flag(current_x,current_y) )
	{
		HTXJ(CAR_SHORT_TIME);
		if(current_d==1)	{ current_x++; M[current_x-2][current_y] =1;}
		if(current_d==3)	{ current_x--; M[current_x+2][current_y] =1;}
		obstacle_flag=0;
	}
	else  obstacle_flag=0;


    for(count1=0;count1<9;count1++){        //��ʼ�������ͼ
		for(count2=0;count2<7;count2++){
			cache[count1][count2]=M[count1][count2];
		}
	}

//	if(traffic_light_finsh==1)
//    {
//        traffic_light_finsh--;
//        cache[traffic_x][traffic_y]=1;  //���ε�ǰ��
//    }

	for(count1=0;count1<Maxsize;count1++){	//��ʼ���������
		loc[count1].x=0;
		loc[count1].y=0;
	}

////////////////////////·�߹滮Ԥ����ȥ��λ��Ӱ��//////////////////////////////
   	if(longline_flag(current_x,current_y) ){//�����ǰ���ں����м䣬���ߵ�ʮ��·�ڷ���ת��
   	    car_xj();
			if(car_go_flag)
		car_go(CAR_GO_TEMPMP);
			car_go_flag = 1;
   	    if(current_d==2)
            current_y++;
        else if(current_d==4)
            current_y--;
   	}
	else if(shortline_flag(current_x,current_y) ){//�����ǰ���������м䣬ͬ��
        car_xj();
		car_go(CAR_GO_TEMPMP);
			car_go_flag = 1;
        if(current_d==1)
            current_x--;
        else if(current_d==3)
            current_x++;
	}
	else if(front_flag(current_x,current_y)){//�����1���ϣ����˻�һ����
		if(current_d==1){
				if(car_go_flag==0)
			car_back(33);
			car_go_flag = 1;
			car_back(CAR_BACK_TEMPMP);
			current_x++;
		}
	}
	else if(back_flag(current_x,current_y)){//�����3���ϣ�ͬ��
		if(current_d==3){
				if(car_go_flag==0)
			car_back(33);
			car_go_flag = 1;
			car_back(CAR_BACK_TEMPMP);
			current_x--;
		}
	}

/////////////////////����·�߹滮�㷨����·��/////////////////////////

    if(!breadth()){//���û���ҵ�·
	STOP();  
    return ; 
	}
/////////////////////�������ÿһ��·��////////////////////////////////
	for(count1=0;count1<Maxsize;count1++){
	    if((loc[count1].x==-1)&&(loc[count1].y==-1))
		{
			if(target_d!=0)
		    {
		        way_dir(current_d,target_d);
		        current_d=target_d;
		    }
			if( !obstacle_flag && check_obstacle(loc[count1-1].x,loc[count1-1].y,current_d)&& obstacle_num)
			{
				//csb_jl_check(1);
  				if(((d==2||d==4)&& dis<=360)||((d==1||d==3) && dis<=300)){ obstacle_flag=1;
				 obstacle_num--;}
				
			}			
			break;
		}

    	if(!obstacle_flag)
			way_run(loc[count1].x,loc[count1].y);	 //����ϰ���־��Ϊtrue��ִֹͣ��·�߹滮
		else
		{
			if(obstacle_check(loc[count1-2].x,loc[count1-2].y)==1)
			{ 
			current_x= loc[count1-2].x;						//���µ�ǰ����
			current_y= loc[count1-2].y;
			}
			else if(obstacle_check(loc[count1-2].x,loc[count1-2].y)==2)		//���ߺ���
			{
			HTXJ(CAR_LONG_TIME);
			current_x= loc[count1-3].x;						//���µ�ǰ����
			current_y= loc[count1-3].y;
			}
			else 														//���ߺ���
			{
			HTXJ(CAR_SHORT_TIME);
			current_x= loc[count1-3].x;						//���µ�ǰ����
			current_y= loc[count1-3].y;
			} 
		   	updata_luxin(loc[count1-2].x,loc[count1-2].y,current_d);	 //���µ�ͼ�������ٴ�·�߹滮�㷨
			obstacle_flag=0;
			count1=0;										 //�ϰ���־����0
		}
	}
////////////////////������ͷ�����Ƿ���ҪŤת////////////////////////////////////
}

void way_run(u8 next_x,u8 next_y)
{
	//�ж���һ���ǲ�����Ҫ��բ��
	if(next_x == DAOZA_X&&next_y == DAOZA_Y)
	{
		DZ_KG(1);
	}
/////////////�����ǰ������ֱ����//////////////
    if((current_x-next_x==1)||(current_x-next_x==-1))
    {//////////////////��������/////////////
        if(current_x-next_x==1)         //1
            way_dir(current_d,1);
        else if(current_x-next_x==-1)   //3
            way_dir(current_d,3);

    //////////////////���ߵ���һ��/////////////////
        if(shortline_flag(next_x,next_y))//����������е�
        {
			if(obstacle_check(next_x,next_y)&& obstacle_num && !obstacle_flag)
			{
				//csb_jl_check(1);
				if(((current_d==2||current_d==4)&& dis<=360)||
				  ((current_d==1||current_d==3) && dis<=300)){ obstacle_flag=1;	obstacle_num--;}
				
			}
			if(!obstacle_flag){
            	car_shortline();
			}
        }
        else if(crossroad_flag(next_x,next_y))//�����ʮ��·�ڴ�
        {

			if(obstacle_check(next_x,next_y)&& obstacle_num && !obstacle_flag)
			{
				//csb_jl_check(1);
				if(((current_d==2||current_d==4)&& dis<=360)||
				  ((current_d==1||current_d==3) && dis<=300)){ obstacle_flag=1;	obstacle_num--;}
				
			}
			if(!obstacle_flag){
            	car_xj();
				if(car_go_flag)
		car_go(CAR_GO_TEMPMP);
			car_go_flag = 1;
			}
        }
        else
        {
            car_xj();
        }
       if(!obstacle_flag) current_x=next_x;
//        printf("\t\t(%d,%d)--%d\n",current_x,current_y,current_d);
    }
///////////////�����ǰ����ˮƽ����//////////////
    else if((current_y-next_y==-1)||(current_y-next_y==1))
    {
        if(current_y-next_y==-1)        //2
            way_dir(current_d,2);
        else if(current_y-next_y==1)         //4
            way_dir(current_d,4);

    //////////////////���ߵ���һ��/////////////////
        if(longline_flag(next_x,next_y))
        {

			if(obstacle_check(next_x,next_y)&& obstacle_num && !obstacle_flag)
			{
				//csb_jl_check(1);
				if(((current_d==2||current_d==4)&& dis<=360)||
				  ((current_d==1||current_d==3) && dis<=300)){ obstacle_flag=1; obstacle_num--;}
			
			}
			if(!obstacle_flag)
			{
	            car_longline();
			}
        }
        else
        {

			if(obstacle_check(next_x,next_y)&& obstacle_num && !obstacle_flag)
			{
				//csb_jl_check(1);
				if(((current_d==2||current_d==4)&& dis<=360)||
				  ((current_d==1||current_d==3) && dis<=300)){ obstacle_flag=1; obstacle_num--;}
			
			}
			if(!obstacle_flag)
			{
	            car_xj();
				if(car_go_flag)
				car_go(CAR_GO_TEMPMP);
				car_go_flag = 1;
			}
        }
        if(!obstacle_flag)  current_y=next_y;
    }
    return ;
}


void way_dir(u8 this_d,u8 next_d)//��ת���򣬵�ǰ�㷽��Ŀ�귽��
{
    if(this_d-next_d==1||this_d-next_d==-3)
    {
        car_left();
    }
    else if(this_d-next_d==3||this_d-next_d==-1)
    {
        car_right();
    }
    else if(this_d-next_d==2||this_d-next_d==-2)
    {
        car_right();
        car_right();
    }
    current_d=next_d;
}

u8 breadth()                    //�������
{
    int q;
	int i,j,find=0,di;
	for(q=0;q<100;q++){
		Q[q].i=100;
		Q[q].j=100;
		Q[q].p=100;
	}
	front=-1;
	rear=-1;

	rear++;
	Q[rear].i=current_x;Q[rear].j=current_y;Q[rear].p=-1;
	while(front<=rear&&find==0)
	{
		front++;
		i=Q[front].i;j=Q[front].j;
		if(i==target_x&&j==target_y)
		{
			find=1;
			recursive(front);
			return 1;
		}
		if(i>0&&i<9&&j>0&&j<7)	//����ڵ�ͼ��Χ��
		{
			for(di=0;di<4;di++)                 //�������ĸ�����������
			{
				switch(di)
				{
				case 0:
					i=Q[front].i-1;j=Q[front].j;break;
				case 1:
					i=Q[front].i;j=Q[front].j+1;break;
				case 2:
					i=Q[front].i+1;j=Q[front].j;break;
				case 3:
					i=Q[front].i;j=Q[front].j-1;break;
				}
				if(cache[i][j]==0)
				{
					rear++;
					Q[rear].i=i;Q[rear].j=j;Q[rear].p=front;
					cache[i][j]=-1;
				}
			}
		}
	}
	return 0;

}

void recursive(u8 front)                  //�����Ǹ��ݹ��㷨�ҳ����·��
{
    int k=front,i=0,j;
	while(k!=0)               //���ģ��ݹ�ó�·��
	{
		j=k;
		k=Q[k].p;
		Q[j].p=-1;
	}
//    printf("�Թ�����·�����£�\n");
	k=0;
	while(k<Maxsize)
	{
		if(Q[k].p==-1)
		{
			i++;
//			printf("{%d,%d}\t",Q[k].i,Q[k].j);

			loc[i-1].x=Q[k].i;
			loc[i-1].y=Q[k].j;

			cache[Q[k].i][Q[k].j]=3;
//			if(i%5==0)
//				printf("\n");
		}
		k++;
	}
	loc[i].x=-1;
	loc[i].y=-1;

}




