import serial
import time
import sys
val = int(sys.argv[1])
char = chr(val)
ser = serial.Serial('/dev/tty.usbmodem14111',9600)
time.sleep(2)
print("PRINTING INPUTTED VALUE!" + str(val))
ser.write(char)

