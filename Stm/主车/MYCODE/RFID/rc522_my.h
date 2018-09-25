#ifndef __RC522_MY_H
#define __RC522_MY_H	 
#include "sys.h"

extern void rc522_init(void);
extern int rc522_read(u8 card_addr,u8 *RFID_DATA);	
extern int myrc522_write(u8 card_addr,u8 *RFID_DATA);		
//void rc522_read_my(void );

extern u8 rfid_do;
extern u8 WRITE_RFID[16];
extern u8 read_rfid_my[16];

#endif

