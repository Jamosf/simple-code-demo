from PIL import Image, ImageGrab
import os
from aip import AipOcr
import time
APP_ID = '10737093'
API_KEY = 'VDlmSvGusP6LCMq9ct8YP8CZ'
SCRET_KEY = 'dS0CMaXBEwceBtccQgrVmwzcvge61T9U'


while True:
    bbox = (1690, 60, 1920, 300)
    # bbox = (0, 0, 1920, 1080)
    # os.remove('as.png')
    im = ImageGrab.grab(bbox)
    # im.save('as.png')
    client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
    # with open('as.png', 'rb') as f:
    #     img = f.read()
    msg = client.basicGeneral(im)
        # print(msg.get('words_result'))
    for i in msg.get('words_result'):
        if '最新' in i.get('words'):
            print(i.get('words').replace('最新', ''))
        # time.sleep(1)

# words = pytesseract.image_to_string(Image.open('as.png'))
# print(words)