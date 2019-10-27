#####################说明#########################
# 运行本脚本前置条件：
# 1、安装python3, 设置python3的环境变量，安装selenium、browsermobproxy、bs4库
# 2、下载chromedriver
# 3、


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
import win32api
import win32gui
import win32con
import time

# websocket获取数据
import asyncio
import logging
from datetime import datetime

# 代理服务启动
# def start_proxy_server():

class Trade:
    def __init__(self):
        # proxy 脚本的位置
        self.proxy_server_path = r'C:\Users\Administrator\Downloads\browsermob-proxy-2.1.4-bin\browsermob-proxy-2.1.4\bin\browsermob-proxy.bat'
        # get请求的url
        self.req_url = 'https://cn.investing.com/common/modules/js_instrument_chart'
        # k线图
        self.canle_url = 'https://cn.investing.com/indices/hong-kong-40-futures-candlestick'
        
        # 启动proxy server
        self.server = Server(self.proxy_server_path)
        self.server.start()
        self.proxys = self.server.create_proxy()
        self.proxys.new_har("k线", options={'captureContent': True})
        
        # 设置chrome的模式
        self.option = Options()
        self.option.add_argument("--headless")
        self.option.add_argument("--proxy-server={0}".format(self.proxys.proxy))
        # self.caps = DesiredCapabilities.CHROME
        # self.caps["goog:loggingPrefs"] = {"performance": "ALL"}
        # self.driver = webdriver.Chrome(desired_capabilities=self.caps, options=self.option)
        self.driver = webdriver.Chrome(options=self.option)

    def get_history_data_from_chrome(self):
        try:
            self.driver.get("https://cn.investing.com/indices/hong-kong-40-futures-candlestick")
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
                        print(_candle)
            self.get_real_data()
        finally:
            self.server.stop()
            self.driver.close()
            self.driver.quit()

    def get_real_data(self):
        while True:
            soup = BeautifulSoup(self.driver.page_source,"lxml")
            print("%d：%s" % (int(round(time.time() * 1000)), self.driver.find_element_by_id("last_last").text))
            # candleStatus = soup.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(69)")[0]["fill"]
            # if candleStatus == "#32ea32":
            #     print("上一根k线状态：跌")
            # elif candleStatus == "#fe3232":
            #     print("上一根k线状态：涨")
            # else:
            #     print("error")
            # candleStatus = soup.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(70)")[0]["fill"]
            # if candleStatus == "#32ea32":
            #     print("k线状态：跌")
            # elif candleStatus == "#fe3232":
            #     print("k线状态：涨")
            # else:
            #     print("error")
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
    trade = Trade()
    trade.get_history_data_from_chrome()
    trade.click_buy()
    trade.click_sell()



