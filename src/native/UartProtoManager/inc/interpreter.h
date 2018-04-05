/*
 * interpreter.h
 *
 *  Created on: Apr 3, 2018
 *      Author: simon
 */

#ifndef INC_INTERPRETER_H_
#define INC_INTERPRETER_H_

#include <stdint.h>

// ======================================= definitions =========================================================
#define STX 2
#define ETX 3
#define GS 29
#define RS 30

#define NUMBER_0 48
#define NUMBER_1 49
#define NUMBER_2 50
#define NUMBER_3 51
#define NUMBER_4 52
#define NUMBER_5 53
#define NUMBER_6 54
#define NUMBER_7 55
#define NUMBER_8 56
#define NUMBER_9 57

#define MINUS 45

#define LOG_INT_TYPE NUMBER_1
#define LOG_STRING_TYPE NUMBER_2

void initInterpreter(
		void (*storeIntData_param)(uint8_t data_name, uint32_t timestamp, int32_t data),
		void (*storeStringData_param)(uint8_t data_name, uint32_t timestamp, uint8_t *pMsg),
		void (*printString_param)(char *),
		void (*printChar_param)(char));

void proceed(int8_t nextByte);

void printBytes(int8_t nextByte);


#endif /* INC_INTERPRETER_H_ */
