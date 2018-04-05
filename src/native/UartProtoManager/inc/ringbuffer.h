/*
 * ringbuffer.h
 *
 *  Created on: Dec 26, 2017
 *      Author: simon
 */

#ifndef INC_RINGBUFFER_H_
#define INC_RINGBUFFER_H_

#include <stdint.h>

#define BUFFER_FAIL 0
#define BUFFER_SUCCESS 1
#define BUFFER_EMPTY 2

// source: https://stackoverflow.com/questions/246977/is-using-flexible-array-members-in-c-bad-practice
typedef struct
{
    uint64_t next_read;
    uint64_t next_write;
    uint64_t capacity;
    int64_t data[];
} Ringbuffer;

Ringbuffer * allocRingbuffer(uint32_t bufferSize);

uint8_t bufferIn(Ringbuffer *pBuffer, int32_t data);

uint8_t bufferOut(Ringbuffer *pBuffer, int32_t *pData);

#endif /* INC_RINGBUFFER_H_ */
