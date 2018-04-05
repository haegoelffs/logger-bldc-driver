/*
 * serialCom.h
 *
 *  Created on: Apr 1, 2018
 *      Author: simon
 */

#ifndef INC_SERIALCOM_H_
#define INC_SERIALCOM_H_

#define SUCCESSFUL 0
#define CAN_NOT_OPEN_PORT -1
#define NO_VALID_BAUDRATE -2
#define NO_VALID_NR_DATABITS -3
#define CAN_NOT_SET_SETTINGS -4
#define NOT_INITIALIZED -5

typedef enum { false, true } boolean;
// source: https://stackoverflow.com/questions/1921539/using-boolean-values-in-c

int initSerialCom(const char *pPort, int symbolrate, boolean parity, boolean twoStopBits, int nrDatabits);

int isSerialComInitialized();

int writeSerialCom(char *pMsg, int size);

int readSerialCom(char *pBuffer, int size);

#endif /* INC_SERIALCOM_H_ */
