from bs4 import BeautifulSoup
import requests
import csv
import time
import lxml

url = "https://www.laohu8.com/hq/s/HSI?f=baidu&utm_source=baidu&utm_medium=aladingpc"
url2 = "https://cn.investing.com/indices/hang-sen-40-candlestick"
url3 = "https://cn.investing.com/indices/hong-kong-40-futures-candlestick"

while True:
    # print("fetch data start...")
    # response = requests.get(url)
    # html = BeautifulSoup(response.text, features="lxml")
    # quoteDiv = html.select("#root > div > div.column-wrap.clear > div.main-side > div:nth-child(1) > div > div.quote-wrap > div.quote-main > div > strong")[0].string
    # #for each in quoteDiv:        
    # print(float(quoteDiv))
    # time.sleep(1)

    print("other fetch data start...")
    header = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36"}
    # print(time.localtime())
    try:
        response = requests.get(url3, headers=header,timeout=3)
    except Exception:
        print("time out")
        break
    if response.status_code != 200 :
        print("error")
        break
    # print(time.localtime())
    html = BeautifulSoup(response.text, features="lxml")
    # print(response.text)
    quoteDiv = html.select("#last_last")[0].string.split(",")
    print("恒生指数： ", quoteDiv[0] + quoteDiv[1])
    #lastStaus = html.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker > path:nth-child(1)")
    lastStaus = html.select("#highcharts-4 > svg > g.highcharts-series-group > g.highcharts-series.highcharts-tracker")
    print(lastStaus)
    # lastStaus = lxml.etree.HTML(html).xpath("//*[@id='highcharts-4']/svg/g[7]/g[1]/path[1]")
    # print(lastStaus)
    # if lastStaus == "#fe3232" :
    #     print("涨")
    # time.sleep(1)
    # //*[@id="highcharts-4"]/svg/g[7]/g[1]/path[1]
    


