/*
 * interpreter.c
 *
 *  Created on: Apr 3, 2018
 *      Author: simon
 */
// ======================================= includes =========================================================
#include <stdint.h>
#include <string.h>

#include "serialCom.h"
#include "interpreter.h"

// ======================================= defines ==========================================================
#define MAX_STRING_LENGHT 100
#define MAX_SOURCE_LENGHT 8

// ======================================= variables ========================================================
typedef enum {
	standby,
	read_timestamp,
	read_source,
	read_data_type,
	read_int_data,
	read_negative_int_data,
	read_string_data,
	ignore_data
} State;

State state = standby;

uint64_t timestamp = 0;

uint8_t data_name = 0;
uint8_t data_type = 0;

int64_t intData = 0;
uint8_t pStringData[MAX_STRING_LENGHT];
uint64_t nextStringBufferByte = 0;

static void (*pStoreIntData)(uint8_t data_name, uint32_t timestamp,
		int32_t data);
static void (*pStoreStringData)(uint8_t data_name, uint32_t timestamp,
		uint8_t *pMsg);
static void (*printString_listener)(char *);
static void (*printChar_listener)(char);

// ======================================= methods ========================================================
int8_t getNumericValue(uint8_t asciiByte, uint8_t *pNum) {
	switch (asciiByte) {
	case MINUS:
		return -1;

	case NUMBER_0:
		*pNum = 0;
		return 0;

	case NUMBER_1:
		*pNum = 1;
		return 0;

	case NUMBER_2:
		*pNum = 2;
		return 0;

	case NUMBER_3:
		*pNum = 3;
		return 0;

	case NUMBER_4:
		*pNum = 4;
		return 0;

	case NUMBER_5:
		*pNum = 5;
		return 0;

	case NUMBER_6:
		*pNum = 6;
		return 0;

	case NUMBER_7:
		*pNum = 7;
		return 0;

	case NUMBER_8:
		*pNum = 8;
		return 0;

	case NUMBER_9:
		*pNum = 9;
		return 0;

	default:
		return -2;
	}
}

void initInterpreter(
		void (*storeIntData_param)(uint8_t data_name, uint32_t timestamp,
				int32_t data),
		void (*storeStringData_param)(uint8_t data_name, uint32_t timestamp,
				uint8_t *pMsg),
				void (*printString_param)(char *),
				void (*printChar_param)(char)) {

	pStoreIntData = storeIntData_param;
	pStoreStringData = storeStringData_param;

	printString_listener = printString_param;
	printChar_listener = printChar_param;

	memset(pStringData, 0, MAX_STRING_LENGHT); // set all string data bytes to 0

	printString_listener("interpreter initalized.\n");
}

void proceed(int8_t nextByte) {
	if (nextByte == STX) {
		timestamp = 0;

		data_type = 0;
		data_name = 0;
		intData = 0;

		nextStringBufferByte = 0;
		memset(pStringData, 0, MAX_STRING_LENGHT);

		state = read_timestamp;
	}

	switch (state) {
	case standby:
		break;

	case read_timestamp:
		if (nextByte == GS) {
			state = read_source;
		} else {
			uint8_t num = 0;
			int8_t numType = getNumericValue(nextByte, &num);

			if (numType == -1) {
				// minus value --> invalid --> ignore
			} else if (numType == -2) {
				// invalid --> ignore
			} else {
				// value from 0 to 9
				timestamp = timestamp * 10 + num;
			}
		}
		break;

	case read_source:
		switch (nextByte) {
		case ETX:
			state = standby;
			break;

		case RS:
			state = read_data_type;
			break;

		default:
			data_name = nextByte;
			break;
		}
		break;

	case read_data_type:
		if (nextByte == RS) {
			switch (data_type) {
			case LOG_INT_TYPE:
				state = read_int_data;
				break;

			case LOG_STRING_TYPE:
				state = read_string_data;
				break;

			default:
				// unknown data type
				state = ignore_data;
			}
		} else {
			data_type = nextByte;
		}
		break;

	case read_int_data:
		if (nextByte == GS) {
			// store
			pStoreIntData(data_name, timestamp, intData);

			// reset
			data_name = 0;
			intData = 0;

			state = read_source;
		} else {
			uint8_t num = 0;
			int8_t numType = getNumericValue(nextByte, &num);

			if (numType == -1) {
				// minus value
				state = read_negative_int_data;
			} else if (numType == -2) {
				// invalid --> ignore
				state = ignore_data;
			} else {
				// value from 0 to 9
				intData = intData * 10 + num;
			}
		}
		break;

	case read_negative_int_data:
		if (nextByte == GS) {
			// store
			pStoreIntData(data_name, timestamp, intData);

			// reset
			data_name = 0;
			intData = 0;

			state = read_source;
		} else {
			uint8_t num = 0;
			int8_t numType = getNumericValue(nextByte, &num);

			if (numType == -1) {
				// minus value --> invalid --> ignore
				state = ignore_data;
			} else if (numType == -2) {
				// invalid --> ignore
				state = ignore_data;
			} else {
				// value from 0 to 9
				intData = intData * 10 + num;

				if (intData > 0) {
					intData = intData * (-1);
				}
			}
		}
		break;

	case read_string_data:
		if (nextByte == GS) {
			// store
			pStoreStringData(data_name, timestamp, pStringData);

			// reset
			data_name = 0;
			nextStringBufferByte = 0;
			memset(pStringData, 0, MAX_STRING_LENGHT);

			state = read_source;
		} else {
			pStringData[nextStringBufferByte] = nextByte;
			nextStringBufferByte++;
		}
		break;

	case ignore_data:
		if (nextByte == GS) {
			state = read_source;
		}
		break;
	}
}

void printBytes(int8_t nextByte) {
	switch (nextByte) {
	case STX:
		printString_listener("<STX>");
		break;
	case ETX:
		printString_listener("<ETX>\n");
		break;
	case GS:
		printString_listener("<GS>");
		break;
	case RS:
		printString_listener("<RS>");
		break;
	default:
		printChar_listener((char)nextByte);
		break;
	}
}
