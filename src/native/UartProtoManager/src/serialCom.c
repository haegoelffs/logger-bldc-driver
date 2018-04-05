/*
 ============================================================================
 Name        : serialCom.c
 Author      : S.Buehlmann
 Version     :
 Copyright   : FHNW EIT Bachelor Thesis Bühlmann & Rotzler 2018
 Description :
 ============================================================================
 */

#include <stdio.h>
#include <stdint.h>
#include <fcntl.h>  /* File Control Definitions          */
#include <termios.h>/* POSIX Terminal Control Definitions*/
#include <unistd.h> /* UNIX Standard Definitions         */
#include <errno.h>  /* ERROR Number Definitions          */

#include <string.h> // for memset

#include "serialCom.h"

// tutorial: http://xanthium.in/Serial-Port-Programming-on-Linux
int fd = 0;

int initSerialCom(const char *pPort, int symbolrate, boolean parity,
		boolean twoStopBits, int nrDatabits) {
	// open port
	fd = open(pPort, O_RDWR | O_NOCTTY);

	if (fd == -1) {
		printf("Error! in Opening port\n");
		return CAN_NOT_OPEN_PORT;
	} else {
		printf("Port opened Successfully\n");
	}

	// create struct to enter settings
	struct termios serialPortSettings;
	memset(&serialPortSettings, 0, sizeof(serialPortSettings)); // set all variables of port to 0

	// setting the Baudrate
	int symbolrate_coded = 0;
	switch(symbolrate){
	case 50:
		symbolrate_coded = B50;
		break;
	case 75:
		symbolrate_coded = B75;
		break;
	case 110:
		symbolrate_coded = B110;
		break;
	case 134:
		symbolrate_coded = B134;
		break;
	case 150:
		symbolrate_coded = B150;
		break;
	case 200:
		symbolrate_coded = B200;
		break;
	case 300:
		symbolrate_coded = B300;
		break;
	case 600:
		symbolrate_coded = B600;
		break;
	case 1200:
		symbolrate_coded = B1200;
		break;
	case 1800:
		symbolrate_coded = B1800;
		break;
	case 2400:
		symbolrate_coded = 2400;
		break;
	case 4800:
		symbolrate_coded = B4800;
		break;
	case 9600:
		symbolrate_coded = B9600;
		break;
	case 19200:
		symbolrate_coded = B19200;
		break;
	case 38400:
		symbolrate_coded = B38400;
		break;
	case 57600:
		symbolrate_coded = B57600;
		break;
	case 115200:
		symbolrate_coded = B115200;
		break;
	case 230400:
		symbolrate_coded = B230400;
		break;
	case 460800:
		symbolrate_coded = B460800;
		break;
	case 500000:
		symbolrate_coded = B500000;
		break;
	case 576000:
		symbolrate_coded = B576000;
		break;
	case 921600:
		symbolrate_coded = B921600;
		break;
	case 1000000:
		symbolrate_coded = B1000000;
		break;
	case 1152000:
		symbolrate_coded = B1152000;
		break;
	case 1500000:
		symbolrate_coded = B1500000;
		break;
	case 2000000:
		symbolrate_coded = B2000000;
		break;
	case 2500000:
		symbolrate_coded = B2500000;
		break;
	case 3000000:
		symbolrate_coded = B3000000;
		break;
	case 3500000:
		symbolrate_coded = B3500000;
		break;
	case 4000000:
		symbolrate_coded = B4000000;
		break;
	default:
		return NO_VALID_BAUDRATE;
		break;
	}

	if (cfsetispeed(&serialPortSettings, symbolrate_coded) != 0) { // setting the input speed or read speed
		return NO_VALID_BAUDRATE;
		return -1;
	}
	cfsetospeed(&serialPortSettings, symbolrate_coded); // setting the output speed or write speed
	printf("symbolrate adjusted\n");

	// Configuring parity
	if (parity) {
		serialPortSettings.c_cflag |= PARENB; /*SET   Parity Bit PARENB*/
	} else {
		serialPortSettings.c_cflag &= ~PARENB; /*CLEAR Parity Bit PARENB*/
	}
	printf("parity adjusted\n");

	// configuring stop bits
	if (twoStopBits) {
		serialPortSettings.c_cflag |= CSTOPB; //Stop bits = 2
	} else {
		serialPortSettings.c_cflag &= ~CSTOPB; //Stop bits = 1
	}
	printf("stopbits adjusted\n");

	// configuring the nr. of data bits
	serialPortSettings.c_cflag &= ~CSIZE; // Clears the Mask
	switch (nrDatabits) {
	case 5:
		serialPortSettings.c_cflag |= CS5;
		break;
	case 6:
		serialPortSettings.c_cflag |= CS6;
		break;
	case 7:
		serialPortSettings.c_cflag |= CS7;
		break;
	case 8:
		serialPortSettings.c_cflag |= CS8;
		break;
	default:
		return NO_VALID_NR_DATABITS;
	}
	printf("data bits adjusted\n");

	// configuring hardware based flow control
	serialPortSettings.c_cflag &= ~CRTSCTS; // turn off
	printf("turned off hardware flow control\n");

	// configuring software based flow control
	serialPortSettings.c_iflag &= ~(IXON | IXOFF | IXANY); // turn off
	printf("turned off software flow control\n");

	// configuring the receiver
	serialPortSettings.c_cflag |= CREAD | CLOCAL; // turn on
	// polling mode
	serialPortSettings.c_cc[VMIN] = 0;
	serialPortSettings.c_cc[VTIME] = 0;
	printf("started receiver in polling mode\n");

	/** Setting the mode of operation
	 *  1. Canonical mode.
	 *  This is most useful when dealing with real terminals, or devices that provide line-by-line communication. The terminal driver returns data line-by-line.
	 *
	 *  2. Non-canonical mode.
	 *  In this mode, no special processing is done, and the terminal driver returns individual characters.
	 *  Source: https://en.wikibooks.org/wiki/Serial_Programming/termios
	 *
	 *  ICANON Enable canonical mode
	 *  ECHO   Echo input characters.
	 *
	 *  ECHOE  If ICANON is also set, the ERASE character erases the preceding input character, and WERASE erases the preceding word.
	 */
	//SerialPortSettings.c_iflag &= ~(ICANON | ECHO | ECHOE | ISIG);

	// set settings
	if (tcsetattr(fd, TCSANOW, &serialPortSettings) != 0) {	// TCSANOW tells to make the changes now without waiting
		return CAN_NOT_SET_SETTINGS;
	}

	printf("settings stored\n");
	return SUCCESSFUL;
}

int isSerialComInitialized() {
	return fd != 0;
}

int writeSerialCom(char *pMsg, int size) {
	if (!isSerialComInitialized()) {
		return NOT_INITIALIZED;
	}

	return write(fd, pMsg, size);
}

int readSerialCom(char *pBuffer, int size) {
	if (!isSerialComInitialized()) {
		return NOT_INITIALIZED;
	}

	return read(fd, pBuffer, size);
}

int main() {
	char *portname = "/dev/ttyUSB0";

	if (initSerialCom(portname, B115200, 1, 0, 8) != 0) {
		return -1;
	}

	char buffer[30];
	memset(&buffer, 0, sizeof(buffer)); // set all variables of port to 0

	while (1) {
		int nrData = readSerialCom(buffer, 30);

		if (nrData == -1) {
			printf("error while reading serial port\n");
		} else if (nrData > 0) {
			int cnt;
			for (cnt = 0; cnt < nrData; cnt++) {
				printf("%c", buffer[cnt]);
			}
		}
	}
}
