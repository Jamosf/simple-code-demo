import time
import os
import sys
import win32api, win32ui, win32gui, win32con
import tkinter as tk
from aip import AipOcr
from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
from threading import Thread

APP_ID = '10737093'
API_KEY = 'VDlmSvGusP6LCMq9ct8YP8CZ'
SCRET_KEY = 'dS0CMaXBEwceBtccQgrVmwzcvge61T9U'
client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
form_name_test = 'TOP环球模拟交易端'
handle = win32gui.FindWindow(None, form_name_test)
app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

class Grab_data:
    def __init__(self):
        self.picList = []
        self.data_list = []
    
    def grab_and_save_img(self):
        while True:
            img = screen.grabWindow(handle, 1690, 60, 230, 240).toImage()
            list = [int(round(time.time())), img]
            self.picList.append(list)
            time.sleep(1)
    
    def parse_img(self, img, time):
        img.save(str(time) + '.png')
        with open(str(time) + '.png', 'rb') as f:
            img_content = f.read()
            msg = client.basicGeneral(img_content)
            for i in msg.get('words_result'):
                if '最新' in i.get('words'):
                    self.data_list.append([time, i.get('words').replace('最新', '')])
                    break
            f.close()
            os.remove(str(time) + '.png')
    
    def get_data_from_img(self):
        while True:
            if len(self.picList) == 0:
                continue
            imgList = self.picList[0]
            self.picList = self.picList[1:]
            if len(imgList) == 0:
                return
            self.parse_img(imgList[1], imgList[0])
        
    def start_get_data_thread(self):
        for _ in range(10):
            Thread(target=self.get_data_from_img).start()
            
    def start_grab_pic_thread(self):
        Thread(target=self.grab_and_save_img).start()

class Gui:
    def __init__(self):
        self.root = []
    
    def click_windows(self, x, y):
        postion = win32api.GetCursorPos()
        win32api.SetCursorPos((x, y))
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0) 

        time.sleep(0.1)
        win32api.SetCursorPos((postion[0], postion[1]))

    def click_buy(self):
        self.click_windows(1600, 900)

    def click_sell(self):
        self.click_windows(1650, 900)

class Q_trade:
    def __init__(self):
        self.histroy_data_m = []
        pass

    def get_real_data(self, grab_data):
        last_time = 0
        while True:
            if(len(grab_data.data_list) == 0):
                continue
            highest_price = 0
            lowest_price = 100000
            open_price = 0
            close_price = 0
            print(grab_data.picList)
            now_time = grab_data.data_list[0][0]
            now_data = float(grab_data.data_list[0][1])
            grab_data.data_list = grab_data.data_list[1:]
        
            if(last_time == 0):
                last_time = now_time - now_time%60
            if(now_time%60 == 1):
                open_price = now_data
            if(now_time%60 == 0):
                close_price = now_data
            if(now_data > highest_price):
                highest_price = now_data
            if(now_data < lowest_price):
                lowest_price = now_data
            if(now_time - last_time == 60):
                print("new k is formed")
                one_minute_candle = [now_time*1000, open_price, highest_price, lowest_price, close_price, 0, 0]
                print(one_minute_candle)
                self.histroy_data_m.append(one_minute_candle)
                # self.histroy_data_m = self.histroy_data_m[1:]
                last_time = now_time
                print(self.histroy_data_m)
            print("%d：%s" % (now_time, now_data))  
            time.sleep(1) 
            
    def start_get_real_data_thread(self):
        Thread(target=self.get_real_data, args=(grab_data,)).start()


if __name__ == '__main__':
    grab_data = Grab_data()
    # 开启线程，一个用于截图，5个用于解析图片内容
    grab_data.start_grab_pic_thread()
    grab_data.start_get_data_thread()
    
    q_trade = Q_trade()
    # 分析上面抓取的数据，量化程序
    q_trade.get_real_data(grab_data)

    app.exit()



