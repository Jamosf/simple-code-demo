#####################说明#########################
# 运行本脚本前置条件：
# 1、安装python3, 设置python3的环境变量，安装selenium、browsermobproxy、bs4库
# 2、下载chromedriver, 并设置环境变量
# 3、下载browsermobproxy


# selenium data
from selenium import webdriver
import time
import io
from bs4 import BeautifulSoup
import json
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from browsermobproxy import Server
from selenium.webdriver.chrome.options import Options
import re

# win32
import win32api, win32ui
import win32gui
import win32con
import time

# websocket获取数据
import asyncio
import logging
from datetime import datetime

# gui界面启动
import tkinter as tk

# 图片截取分析
from PIL import Image, ImageGrab
import os
from aip import AipOcr
import time
from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
import sys
# 多线程
from threading import Thread
from concurrent.futures import ThreadPoolExecutor, wait, ALL_COMPLETED

APP_ID = '10737093'
API_KEY = 'VDlmSvGusP6LCMq9ct8YP8CZ'
SCRET_KEY = 'dS0CMaXBEwceBtccQgrVmwzcvge61T9U'
form_name_test = 'TOP环球模拟交易端'
handle = win32gui.FindWindow(None, form_name_test)
app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

class Grab_data:
    def __init__(self):
        self.picList = []
        self.data_list = []
        pass
                
    def get_data_from_win32(self, filename):
        hwnd = 0 # 窗口的编号，0号表示当前活跃窗口
        # 根据窗口句柄获取窗口的设备上下文DC（Divice Context）
        hwndDC = win32gui.GetWindowDC(hwnd)
        # 根据窗口的DC获取mfcDC
        mfcDC = win32ui.CreateDCFromHandle(hwndDC)
        # mfcDC创建可兼容的DC
        saveDC = mfcDC.CreateCompatibleDC()
        # 创建bigmap准备保存图片
        saveBitMap = win32ui.CreateBitmap()
        # 获取监控器信息
        # MoniterDev = win32api.EnumDisplayMonitors(None, None)
        # w = MoniterDev[0][2][2]
        # h = MoniterDev[0][2][3]
        # print w,h　　　#图片大小
        # 为bitmap开辟空间
        saveBitMap.CreateCompatibleBitmap(mfcDC, 230, 240)
        # 高度saveDC，将截图保存到saveBitmap中
        saveDC.SelectObject(saveBitMap)
        # 截取从左上角（0，0）长宽为（w，h）的图片
        saveDC.BitBlt((0, 0), (230, 240), mfcDC, (1690, 60), win32con.SRCCOPY)
        saveBitMap.SaveBitmapFile(saveDC, filename)
        client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
        with open(filename, 'rb') as f:
            img = f.read()
            msg = client.basicGeneral(img)
            # print(msg.get('words_result'))
            for i in msg.get('words_result'):
                if '最新' in i.get('words'):
                    return i.get('words').replace('最新', '')
    
    def get_data_from_qt(self):
        while True:
            # print('执行')
            imgList = self.get_image_list()
            self.picList = self.picList[1:]
            if len(imgList) == 0:
                return
            time = imgList[0]
            img = imgList[1]
            img.save(str(time) + '.png')
            client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
            # print('截图')
            with open(str(time) + '.png', 'rb') as f:
                print('dakaile')
                img = f.read()
                msg = client.basicGeneral(img)
                # print(msg.get('words_result'))
                for i in msg.get('words_result'):
                    if '最新' in i.get('words'):
                        print('有数据')
                        self.data_list.append([imgList[0], i.get('words').replace('最新', '')])
                        break
                f.close()
                os.remove(str(time) + '.png')
                        
    def get_data_from_PIL(self):
        bbox = (1690, 60, 1920, 300)
        # bbox = (0, 0, 1920, 1080)
        # os.remove('as.png')
        im = ImageGrab.grab(bbox)
        im.save('as.png')
        client = AipOcr(APP_ID, API_KEY, SCRET_KEY)
        with open('as.png', 'rb') as f:
            img = f.read()
            msg = client.basicGeneral(img)
            # print(msg.get('words_result'))
            for i in msg.get('words_result'):
                if '最新' in i.get('words'):
                    return i.get('words').replace('最新', '')
                
    def get_grab_picture(self):
        img = screen.grabWindow(handle, 1690, 60, 230, 240).toImage()
        # app.exit()
        return img
    
    def get_data_from_2_thread(self):
        while True:
            list = [int(round(time.time())), self.get_grab_picture()]
            self.picList.append(list)
            time.sleep(1)
    def get_image_list(self):
        while True:
            if len(self.picList) != 0:
                return self.picList[0]
            time.sleep(0.1)
        
        
    def exec(self):
        for i in range(5):
            Thread(target=self.get_data_from_qt).start()
            # print(i + ':thread start')
        
        

# class Gui:
#     def __init__(self):
#         self.root = 

class Trade:
    def __init__(self):
        # proxy 脚本的位置
        self.proxy_server_path = r'C:\Program Files\browsermob-proxy-2.1.4\bin\browsermob-proxy.bat'
        # k线图
        self.canle_url = 'https://cn.investing.com/indices/hong-kong-40-futures-candlestick'
        
        # get请求的url
        self.req_url = 'https://cn.investing.com/common/modules/js_instrument_chart'
        
        # 启动proxy server
        self.server = Server(self.proxy_server_path)
        self.server.start()
        self.proxys = self.server.create_proxy()
        self.proxys.new_har("k线", options={'captureContent': True})
        
        # 设置chrome的模式
        self.option = Options()
        # self.option.add_argument("--headless")
        self.option.add_argument("--proxy-server={0}".format(self.proxys.proxy))
        # self.caps = DesiredCapabilities.CHROME
        # self.caps["goog:loggingPrefs"] = {"performance": "ALL"}
        # self.driver = webdriver.Chrome(desired_capabilities=self.caps, options=self.option)
        # self.driver = webdriver.Chrome(options=self.option)

        # 保存k线数据的容器
        self.histroy_data_m = []

    def get_history_data_from_chrome(self):
        try:
            self.driver.get(self.canle_url)
            # 切换到分钟k线的界面
            self.driver.find_element_by_css_selector(".fchart-switches-timeframes a").click()
            # 等待5s时间来加载
            time.sleep(5)
            result = self.proxys.har
            for entry in result["log"]["entries"]:
                _url = entry["request"]["url"]
                if self.req_url in _url:
                    _response = entry["response"]
                    _content = _response["content"]["text"]
                    _candles = json.loads(_content)["candles"]
                    for _candle in _candles:
                        self.histroy_data_m.append(_candle)
                        # print(_candle)
            self.histroy_data_m = self.histroy_data_m[39:]
            print(self.histroy_data_m)
            self.get_real_data()
        finally:
            self.server.stop()
            self.driver.close()
            self.driver.quit()

    def get_last_data_time(self):
        return self.histroy_data_m[-1][0]

    def get_real_data(self, grab_data):
        last_time = 0
        while True:
            # print('获取数据线程')
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
        
            # last_time = self.get_last_data_time()
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

    def click_windows(self, x, y):
        # form_name = ''
        # form_name_test = 'TOP环球模拟交易端'

        # handle = win32gui.FindWindow(None, form_name_test)
        postion = win32api.GetCursorPos()
        # 设置鼠标位置
        win32api.SetCursorPos((x, y))
        # 模拟鼠标按下事件
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
        # 模拟鼠标放开操作
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0) 

        time.sleep(0.1)
        win32api.SetCursorPos((postion[0], postion[1]))

    def click_buy(self):
        self.click_windows(1600, 900)

    def click_sell(self):
        self.click_windows(1650, 900)


if __name__ == '__main__':
    grab_data = Grab_data()
    trade = Trade()
    Thread(target=grab_data.get_data_from_2_thread).start()
    Thread(target=trade.get_real_data, args=(grab_data,)).start()
    grab_data.exec()
    # trade.get_history_data_from_chrome()
    # trade.get_real_data()
    trade.click_buy()
    trade.click_sell()
    app.exit()



