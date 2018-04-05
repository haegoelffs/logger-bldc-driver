/*
 * uartProtoManager.c
 *
 *  Created on: Mar 28, 2018
 *      Author: simon
 */

#include <jni.h>
#include <stdio.h>
#include <stdint.h>
#include <string.h>

#include "loggerbldcmotordriver_serialcom_UARTProtoManager.h"
#include "serialCom.h"
#include "interpreter.h"

// ======================================= defines ================================================================

// ======================================= variables ==============================================================
JNIEnv *pEnv;
jobject this_obj;
jmethodID bufferIntData_id;
jmethodID bufferStringData_id;
jmethodID printString_id;

// ======================================= jni utility functions =================================================
jmethodID loadMethodID_bufferIntData(JNIEnv *pEnv, jobject obj){
	// java method: private void buffer_intData(byte data_name, long timestamp, long data)
	jclass obj_class = (*pEnv)->GetObjectClass(pEnv, obj);
	jmethodID id_method = (*pEnv)->GetMethodID(pEnv, obj_class, "buffer_intData", "(BJJ)V");
	// expensive operation (source: http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html)

	return id_method;
}

jmethodID loadMethodID_bufferStringData(JNIEnv *pEnv, jobject obj){
	// private void buffer_stringData(byte data_name, long timestamp, byte[] data)
	jclass obj_class = (*pEnv)->GetObjectClass(pEnv, obj);
	jmethodID id_method = (*pEnv)->GetMethodID(pEnv, obj_class, "buffer_stringData", "(BJ[B)V");
	// expensive operation (source: http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html)

	return id_method;
}

jmethodID loadMethodID_printString(JNIEnv *pEnv, jobject obj){
	// private void printString(byte[] data)
	jclass obj_class = (*pEnv)->GetObjectClass(pEnv, obj);
	jmethodID id_method = (*pEnv)->GetMethodID(pEnv, obj_class, "printString", "([B)V");
	// expensive operation (source: http://journals.ecs.soton.ac.uk/java/tutorial/native1.1/implementing/method.html)

	return id_method;
}

void callMethod_bufferIntData(JNIEnv *pEnv, jobject obj, jmethodID method_id, uint8_t header, uint64_t timestamp, int64_t value){
	(*pEnv)->CallVoidMethod(pEnv, obj, method_id, (jbyte)header, (jlong)timestamp, (jlong)value);
}

void callMethod_bufferStringData(JNIEnv *pEnv, jobject obj, jmethodID method_id, uint8_t header, uint64_t timestamp, jbyte *pMsg, uint64_t size){
	jbyteArray array = (*pEnv)->NewByteArray(pEnv, (jsize)size);
	(*pEnv)->SetByteArrayRegion(pEnv, array, (jsize)0, (jsize)size, pMsg);
	// void (JNICALL *SetByteArrayRegion)(JNIEnv *env, jbyteArray array, jsize start, jsize len, const jbyte *buf);

	(*pEnv)->CallVoidMethod(pEnv, obj, method_id, (jbyte)header, (jlong)timestamp, array);
}

void callMethod_printString(JNIEnv *pEnv, jobject obj, jmethodID method_id, jbyte *pMsg, uint64_t size){
	jbyteArray array = (*pEnv)->NewByteArray(pEnv, (jsize)size);
	(*pEnv)->SetByteArrayRegion(pEnv, array, (jsize)0, (jsize)size, pMsg);
	// void (JNICALL *SetByteArrayRegion)(JNIEnv *env, jbyteArray array, jsize start, jsize len, const jbyte *buf);

	(*pEnv)->CallVoidMethod(pEnv, obj, method_id, array);
}

// ======================================= functions ============================================================
void printChar(char msg){
	callMethod_printString(pEnv, this_obj, printString_id, (jbyte *)&msg, 1);
}

void printString(char *pMsg){
	// count until (but without) zero-byte
	int cnt = 0;
	char *pTemp = pMsg;

	while(1){
		if(*pTemp != 0){
			cnt++;
			pTemp++;
		}else{
			break;
		}
	}
	callMethod_printString(pEnv, this_obj, printString_id, (jbyte *)pMsg, cnt);
}

//void (*storeIntData_param)(uint8_t, uint64_t, int64_t)
void storeIntData(uint8_t data_name, uint32_t timestamp, int32_t data){
	callMethod_bufferIntData(pEnv, this_obj, bufferIntData_id, data_name, timestamp, data);
}


//void (*storeStringData_param)(uint8_t, uint64_t, uint8_t *)
void storeStringData(uint8_t data_name, uint32_t timestamp, uint8_t *pMsg){
	callMethod_bufferStringData(pEnv, this_obj, bufferStringData_id, data_name, timestamp, (jbyte *)pMsg, 100);
}

// ======================================= jni functions ========================================================
JNIEXPORT jint JNICALL Java_loggerbldcmotordriver_serialcom_UARTProtoManager_startNative
  (JNIEnv *pEnv_param, jobject this_obj_param, jstring port_j, jint symbolrate_j, jboolean parity_j, jboolean twoStopBits_j, jint nrDatabits_j){
	pEnv = pEnv_param;
	this_obj = this_obj_param;
	bufferIntData_id = loadMethodID_bufferIntData(pEnv, this_obj); // expensive
	bufferStringData_id = loadMethodID_bufferStringData(pEnv, this_obj); // expensive
	printString_id = loadMethodID_printString(pEnv, this_obj); // expensive

	printString("UART ProtoManager started...\n");

	const char *pPort = (*pEnv)->GetStringUTFChars(pEnv, port_j, 0);

	boolean parity = false;
	if(parity_j == JNI_TRUE){
		parity = true;
	}

	boolean twoStopBits = false;
	if(twoStopBits_j == JNI_TRUE){
		twoStopBits = true;
	}

	int symbolrate = (int)symbolrate_j;
	int nrDatabits = (int)nrDatabits_j;

	// init interpreter
	initInterpreter(&storeIntData,&storeStringData,&printString,&printChar);


	// init serial com
	printString("Init serial communication...\n");

	char temp_buffer[100];
	sprintf(temp_buffer, "Port: %s Symbolrate: %d Nr. databits: %d Parity: %d Two stop bits: %d\n",pPort, symbolrate, nrDatabits, parity, twoStopBits);
	printString(temp_buffer);

	int initCode = 0;
	initCode = initSerialCom(pPort, symbolrate, parity, twoStopBits, nrDatabits);

	if (initCode != SUCCESSFUL) {
			return initCode;
	}
	printString("Serial communication started.\n");

	char buffer[30];
	memset(&buffer, 0, sizeof(buffer)); // set all variables of port to 0

	char nextByte = 0;
	while (1) {
		if(readSerialCom(&nextByte, 1)>0){
			proceed(nextByte);
			printBytes(nextByte);
		}
	}

	(*pEnv)->ReleaseStringUTFChars(pEnv, port_j, pPort);
	return 0;
}
