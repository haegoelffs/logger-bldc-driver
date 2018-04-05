PROJECT = uartProtoManager
OBJS = src/$(PROJECT).o \
src/serialCom.o \
src/interpreter.o

CC      = gcc
INC = -I"/opt/java/jdk1.8.0_102/include" -I"/opt/java/jdk1.8.0_102/include/linux" -I"./inc"

all: $(OBJS)
	gcc -shared -o $(PROJECT).so $(OBJS) -O2

# erzeugen der .o-Dateien
%.o: %.c
	gcc -Wall -fPIC $(INC) -o $@ -c $<