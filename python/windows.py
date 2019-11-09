import time
import os
import sys
import win32api, win32ui, win32gui, win32con
import tkinter as tk
from aip import AipOcr
from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
from threading import Thread
import pandas as pd
import numpy as np

APP_ID = '10737093'
API_KEY = 'VDlmSvGusP6LCMq9ct8YP8CZ'
SCRET_KEY = 'dS0CMaXBEwceBtccQgrVmwzcvge61T9U'
form_name_test = 'TOP环球模拟交易端'
handle = win32gui.FindWindow(None, form_name_test)
app = QApplication(sys.argv)
screen = QApplication.primaryScreen()
grab_pos = [0, 0, 0, 0]

class Grab_data:
    def __init__(self):
        self.picList = []
        self.data_list = []
    
    def grab_and_save_img(self):
        while True:
            img = screen.grabWindow(handle, grab_pos[0], grab_pos[1], grab_pos[2], grab_pos[3]).toImage()
            list = [int(round(time.time())), img]
            self.picList.append(list)
            time.sleep(1)
    
    def parse_img(self, img, time):
        img.save(str(time) + '.png')
        with open(str(time) + '.png', 'rb') as f:
            img_content = f.read()
            client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
            msg = client.basicGeneral(img_content)
            for i in msg.get('words_result'):
                if '最新' in i.get('words'):
                    self.data_list.append([time, (i.get('words'))[-5:]])
                    break
            f.close()
            os.remove(str(time) + '.png')
        
    
    def get_data_from_img(self):
        while True:
            if len(self.picList) == 0:
                time.sleep(0.1)
                continue
            imgList = self.picList[0]
            self.picList = self.picList[1:]
            if len(imgList) == 0:
                return
            self.parse_img(imgList[1], imgList[0])
        
    def start_get_data_thread(self):
        for _ in range(5):
            Thread(target=self.get_data_from_img).start()
            
    def start_grab_pic_thread(self):
        Thread(target=self.grab_and_save_img).start()

class Gui:
    def __init__(self):
        self.buy_long_btn_pos = [0, 0]
        self.buy_short_btn_pos = [0, 0]
        self.sell_btn_pos = [0, 0]
        desktop = QApplication.desktop()
        self.display_height = desktop.screenGeometry().height()
        self.display_width = desktop.screenGeometry().width()
        self.is_find_btn = False
        self.is_find_grab_area = False
        
    def init_btn_position(self):
        win32gui.ShowWindow(handle, win32con.SW_RESTORE)
        win32gui.SetForegroundWindow(handle)
        if os.path.exists('desktop.png') is False:
            time.sleep(2)
            img = screen.grabWindow(handle, self.display_width*3/4, 0, self.display_width, self.display_height).toImage()
            img.save('desktop.png')
        with open('desktop.png', 'rb') as f:
            img_content = f.read()
            client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
            msg = client.accurate(img_content)
            print(msg.get('words_result'))
            for i in msg.get('words_result'):
                if '买多' in i.get('words') and '卖空' in i.get('words') and '平仓' in i.get('words'):
                    self.is_find_btn = True
                    self.buy_long_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] / 6 + self.display_width*3/4)
                    self.buy_long_btn_pos[1] = i.get('location')['top']
        
                    self.buy_short_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] / 2 + self.display_width*3/4)
                    self.buy_short_btn_pos[1] = i.get('location')['top']
                    
                    self.sell_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] * 5 / 6 + self.display_width*3/4)
                    self.sell_btn_pos[1] = i.get('location')['top']    
                if '最新' in i.get('words'):
                    self.is_find_grab_area = True
                    grab_pos[0] = i.get('location')['left'] + self.display_width*3/4
                    grab_pos[1] = i.get('location')['top']
                    grab_pos[2] = i.get('location')['width']
                    grab_pos[3] = i.get('location')['height']
            f.close()
            # os.remove('desktop.png')
            
    def init_btn_position_with_retry(self):
        while self.is_find_btn is False or self.is_find_grab_area is False:
            self.init_btn_position()
        print(self.buy_long_btn_pos)
        print(self.buy_short_btn_pos)
        print(self.sell_btn_pos)
        print(grab_pos)
    
    def click_windows(self, x, y):
        postion = win32api.GetCursorPos()
        win32api.SetCursorPos((x, y))
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0) 

        time.sleep(0.1)
        win32api.SetCursorPos((postion[0], postion[1]))

    def click_buy_long(self):
        self.click_windows(self.buy_long_btn_pos[0], self.buy_long_btn_pos[1])

    def click_buy_short(self):
        self.click_windows(self.buy_short_btn_pos[0], self.buy_short_btn_pos[1])
    
    def click_sell(self):
        self.click_windows(self.sell_btn_pos[0], self.sell_btn_pos[1])

class Q_trade:
    def __init__(self):
        self.histroy_data_m = []
        self.close_price_list = []
        self.ma5 = 0
        self.ma10 = 0
        self.ma30 = 0
        self.buy_long_cnt = 0
        self.buy_short_cnt = 0
        self.sell_cnt = 0
        self.earn = 0
        self.loss = 0
        self.buy_long_price = 0
        self.buy_short_price = 0
        pass
    
    # 以 0 作为一个k线的开始， 59 结束。
    def get_one_minute_candle_data(self, now_time, one_minute_price):
        open_price = one_minute_price[0]
        close_price = one_minute_price[-1]
        highest_price = max(one_minute_price)
        lowest_price = min(one_minute_price)
        if open_price > close_price:
            one_minute_candle = [now_time*1000, open_price, highest_price, lowest_price, close_price, 'green']
        else:
            one_minute_candle = [now_time*1000, open_price, highest_price, lowest_price, close_price, 'red']
        return one_minute_candle
    
    def get_real_data(self, grab_data):
        last_time = 0
        one_minute_price = []
        isTheFirst = True
        while True:
            if(len(grab_data.data_list) == 0):
                continue
            
            now_time = grab_data.data_list[0][0]
            now_price = int(grab_data.data_list[0][1])
            grab_data.data_list = grab_data.data_list[1:]
        
            if(last_time == 0):
                last_time = now_time - now_time % 60 - 1
            print(now_time, now_price)
            one_minute_price.append(now_price)
            
            # 每秒执行卖出策略检查
            self.sell_strategy(now_price)
            
            if(now_time - last_time == 60):
                if isTheFirst is True:
                    isTheFirst = False
                    last_time = now_time
                    one_minute_price.clear()
                    continue
                print("new candle is formed")
                
                one_minute_candle = self.get_one_minute_candle_data(now_time, one_minute_price)
                self.histroy_data_m.append(one_minute_candle)
                self.close_price_list.append(one_minute_candle[-2])
                
                if len(self.histroy_data_m) > 30:
                    self.histroy_data_m = self.histroy_data_m[1:]
                    self.close_price_list = self.close_price_list[1:]
                
                # 每次K线形成的时候执行买入检查
                self.buy_strategy(now_price)
                
                # 清空状态
                last_time = now_time
                one_minute_price.clear()
            time.sleep(1)
    
    def get_ma(self, interval):
        dataframe = {'data':self.close_price_list}
        df = pd.DataFrame(dataframe)
        df = df.data.rolling(interval).mean()
        return df[len(df) - 1]
                
    def buy_strategy(self, now_price):
        ma5 = self.get_ma(5)
        ma10 = self.get_ma(10)
        ma30 = self.get_ma(30)
        if np.isnan(ma5) or np.isnan(ma10) or np.isnan(ma10):
            return
        if ma5 > ma10 and ma10 > ma30 and self.histroy_data_m[-2][5] is 'green':
            if self.buy_long_cnt == 0 and self.buy_short_cnt == 0:
                gui.click_buy_long()
                self.buy_long_price = now_price
                self.buy_long_cnt = self.buy_long_cnt + 1
        if ma5 < ma10 and ma10 < ma30 and self.histroy_data_m[-2][5] is 'red':
            if self.buy_long_cnt == 0 and self.buy_short_cnt == 0:
                gui.click_buy_short()
                self.buy_short_price = now_price
                self.buy_short_cnt = self.buy_short_cnt + 1
                
    def sell_strategy(self, now_price):
        if self.buy_long_cnt > 0 and self.buy_long_price - now_price >= 6:
            gui.click_sell()
            self.buy_long_cnt = self.buy_long_cnt - 1
        if self.buy_short_cnt > 0 and now_price - self.buy_short_price >= 15:
            gui.click_sell()
            self.buy_short_cnt = self.buy_short_cnt - 1           
            
    def start_get_real_data_thread(self):
        Thread(target=self.get_real_data, args=(grab_data,)).start()


if __name__ == '__main__':
    gui = Gui()
    gui.init_btn_position_with_retry()
    
    grab_data = Grab_data()
    # 开启线程，一个用于截图，5个用于解析图片内容
    grab_data.start_grab_pic_thread()
    grab_data.start_get_data_thread()
    
    q_trade = Q_trade()
    # 分析上面抓取的数据，量化程序
    q_trade.get_real_data(grab_data)

    app.exit()