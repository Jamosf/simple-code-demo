from PIL import Image, ImageGrab
import os
from aip import AipOcr
import time
import base64
from io import BytesIO, StringIO
from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
import win32api, win32ui, win32gui, win32con
import sys

APP_ID = '10737093'
API_KEY = 'VDlmSvGusP6LCMq9ct8YP8CZ'
SCRET_KEY = 'dS0CMaXBEwceBtccQgrVmwzcvge61T9U'
app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

form_name_test = 'TOP环球模拟交易端'
handle = win32gui.FindWindow(None, form_name_test)
# im = screen.grabWindow(handle, 0, 0, 1920, 1080).toImage()
# while True:
# bbox = (1690, 60, 1920, 300)
bbox = (0, 0, 1920, 1080)
# os.remove('as.png')
im = ImageGrab.grab(bbox).tobytes()
f = StringIO(str(im))
# new_im = base64.b64encode(im)
im.save('as.png')
client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
msg = client.general(f.read())
print(msg)
for i in msg.get('words_result'):
    if '买多' in i.get('words'):
        # print(i.get('words').replace('最新', ''))
        print(i.get('location')['width'])
    # time.sleep(1)

# words = pytesseract.image_to_string(Image.open('as.png'))
# print(words)