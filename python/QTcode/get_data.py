from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
import sys
import win32api, win32gui, win32con
from threading import Thread
import time
import os
from aip import AipOcr

from selenium import webdriver
from bs4 import BeautifulSoup
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from browsermobproxy import Server
from selenium.webdriver.chrome.options import Options
import json
import configparser
import gui_click as gc

from looger import *

app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

class Grab_data:
    def __init__(self):
        config = configparser.ConfigParser()
        config.read('config.ini', encoding='utf-8')
        self.picList = []
        self.data_list = []
        self.appId = config.get('baidu', 'appId')
        self.apiKey = config.get('baidu', 'apiKey')
        self.secretKey = config.get('baidu', 'secretKey')
        self.word = config.get('app', 'nowPrice')
        self.form_name_test = config.get('app', 'name')
        self.thread_num = int(config.get('grabconf', 'thread'))
        self.handle = win32gui.FindWindow(None, self.form_name_test)
        self.click = gc.Click()
        self.click.run()
        # self.grab_pos = self.click.grab_pos
    
    def grab_and_save_img(self):
        while True:
            img = screen.grabWindow(self.handle, self.click.grab_pos[0], self.click.grab_pos[1], self.click.grab_pos[2], self.click.grab_pos[3]).toImage()
            # print(type(img))
            list = [int(round(time.time())), img]
            self.picList.append(list)
            time.sleep(1)
    
    def start_grab_pic_thread(self):
        Thread(target=self.grab_and_save_img).start()
    
    def parse_img(self, img, t):
        img.save(str(t) + '.png')
        try:
            logger.info("parse image start")
            with open(str(t) + '.png', 'rb') as f:
                img_content = f.read()
                client = AipOcr(self.appId, self.apiKey, self.secretKey)
                msg = client.basicGeneral(img_content)
                if msg.get('words_result') is None:
                    print('截取的图片有问题，识别有误！')
                    return
                for i in msg.get('words_result'):
                    if self.word in i.get('words'):
                        logger.debug([t, (i.get('words'))[-5:], time.time()])
                        self.data_list.append([t, (i.get('words'))[-5:]])
                        break
        except:
            logger.info("parse image failed")
        finally:
            f.close()
            os.remove(str(t) + '.png')
        
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
        for _ in range(self.thread_num):
            Thread(target=self.get_data_from_img).start()
        
    def run(self):
        logger.info("grab data start")
        self.start_grab_pic_thread()
        self.start_get_data_thread()
        logger.info("grab data end")
            
class Crawl_data:
    def __init__(self):
        # 关键数据容器
        self.histroy_data_m = []
        self.close_price_list = []
        
        config = configparser.ConfigParser()
        config.read('config.ini', encoding='utf-8')
        # proxy 脚本的位置
        self.proxy_server_path = config.get('paths', 'porxyServerPath')
        # k线图
        self.canle_url = config.get('urls', 'candleUrl')
        # get请求的url
        self.req_url = config.get('urls', 'reqUrl')
        # 启动proxy server
        self.server = Server(self.proxy_server_path)
        self.server.start()
        self.proxys = self.server.create_proxy()
        self.proxys.new_har("k线", options={'captureContent': True})
        
        self.option = Options()
        self.option.add_argument("--headless")
        self.option.add_argument("--proxy-server={0}".format(self.proxys.proxy))
        self.driver = webdriver.Chrome(options=self.option)
    
    def get_history_data_from_chrome(self):
        try:
            logger.info("get_history_data_from_chrome start")
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
                        _candle = _candle[:5]
                        _candle[0] = _candle[0]/1000
                        self.histroy_data_m.append(_candle)
                        self.close_price_list.append(_candle[-1])
            self.histroy_data_m = self.histroy_data_m[-31:-1]
            self.close_price_list = self.close_price_list[-31:-1]
        except:
            logger.info("get_history_data_from_chrome failed")
        else:
            self.server.stop()
            self.driver.close()
            self.driver.quit()
            logger.info("get_history_data_from_chrome success")
    
    def printKeyInfo(self):
        print(self.histroy_data_m)
        print(self.close_price_list)
        
    def run(self):
        print("fake start")
        # self.get_history_data_from_chrome()
    
if __name__ == '__main__':
    # 测试截图获取数据功能
    grab_data = Grab_data()
    grab_data.run()
    
    # 测试从浏览器爬取数据功能
    # crawl_data = Crawl_data()
    # crawl_data.get_history_data_from_chrome()
    # crawl_data.printKeyInfo()
    