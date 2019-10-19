from selenium import webdriver
import time
import io
from bs4 import BeautifulSoup
import json
from selenium.webdriver.common.desired_capabilities import DesiredCapabilities
from browsermobproxy import Server
from selenium.webdriver.chrome.options import Options
import re

# websocket获取数据
import asyncio
import logging
from datetime import datetime

# 代理服务启动
server = Server("/Users/Jamos/Documents/python/browsermob-proxy/bin/browsermob-proxy")
server.start()
proxys = server.create_proxy()
# proxys.blacklist(".*investing.*", 404)

# 设置chrome的模式
option = Options()
option.add_argument("--headless")
option.add_argument("--proxy-server={0}".format(proxys.proxy))

caps = DesiredCapabilities.CHROME
caps["goog:loggingPrefs"] = {"performance": "ALL"}

# get请求的url
req_url = "https://cn.investing.com/common/modules/js_instrument_chart"

try:
    driver = webdriver.Chrome(desired_capabilities=caps, options=option)
    print("1")
    proxys.new_har("k线", options={'captureContent': True})
    print("2")
    # driver.get("https://cn.investing.com/indices/hong-kong-40-futures-candlestick")
    driver.get("https://cn.investing.com/indices/sensex-candlestick")
    # print(driver.find_element_by_id("last_last").text)
    # driver.find_element_by_css_selector(".fchart-switches-timeframes a").click()
    time.sleep(5)
    print("3")
    result = proxys.har
    print("4")
    for entry in result["log"]["entries"]:
        _url = entry["request"]["url"]
        if req_url in _url:
            _response = entry["response"]
            _content = _response["content"]["text"]
            _candles = json.loads(_content)["candles"]
            for _candle in _candles:
                print(_candle)
    # print(driver.find_element_by_css_selector("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(70)"))
    # with io.open("index.html", "w") as wf:
    #     wf.write(driver.page_source)
    #     wf.close()
    # for respRecived in driver.get_log("performance"):
    #     try:
    #         resp = json.loads(respRecived["message"])["message"]
    #         if resp["method"] == "Network.responseReceived" and req_url in resp["params"]["response"]["url"]:
    #             print(resp["method"])
    #             print(resp["params"]["response"]["url"])
    #             print(req_url in resp["params"]["response"]["url"])
    #             print(resp)
    #             # print(resp["headers"])
    #     except:
    #         pass

    # while True:
    #     soup = BeautifulSoup(driver.page_source,"lxml")
    #     print("当前点位：%s" % driver.find_element_by_id("last_last").text)
    #     candleStatus = soup.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(69)")[0]["fill"]
    #     if candleStatus == "#32ea32":
    #         print("上一根k线状态：跌")
    #     elif candleStatus == "#fe3232":
    #         print("上一根k线状态：涨")
    #     else:
    #         print("error")
    #     candleStatus = soup.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(70)")[0]["fill"]
    #     if candleStatus == "#32ea32":
    #         print("k线状态：跌")
    #     elif candleStatus == "#fe3232":
    #         print("k线状态：涨")
    #     else:
    #         print("error")
    #     time.sleep(1)
finally:
    server.stop()
    driver.close()
    driver.quit()

# driver.find_element_by_id("markets_subnav_link").
# driver.find_element_by_id("su").click()