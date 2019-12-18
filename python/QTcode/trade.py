import time
import os
import sys
from aip import AipOcr
from threading import Thread
import pandas as pd
import numpy as np
import configparser
import get_data as gd
import gui_click as gc
from looger import *

class Q_trade:
    def __init__(self):
        crawl_data = gd.Crawl_data()
        crawl_data.run()
        self.histroy_data_m = crawl_data.histroy_data_m
        self.close_price_list = crawl_data.close_price_list
        
        self.grab_data = gd.Grab_data()
        self.grab_data.run()
        
        self.buy_long_cnt = 0
        self.buy_short_cnt = 0
        self.sell_cnt = 0
        self.earn = 0
        self.loss = 0
        self.buy_long_price = 0
        self.buy_short_price = 0 

    # 以 1 作为一个k线的开始， 60 结束。
    def get_one_minute_candle_data(self, now_time, one_minute_price):
        open_price = one_minute_price[0]
        close_price = one_minute_price[-1]
        highest_price = max(one_minute_price)
        lowest_price = min(one_minute_price)
        if open_price > close_price:
            one_minute_candle = [now_time, open_price, highest_price, lowest_price, close_price, 'green']
        else:
            one_minute_candle = [now_time, open_price, highest_price, lowest_price, close_price, 'red']
        return one_minute_candle
    
    def get_real_data(self):
        last_time = 0
        one_minute_price = []
        while True:
            if(len(self.grab_data.data_list) == 0):
                continue
            
            try:
                now_time = self.grab_data.data_list[0][0]
                now_price = int(self.grab_data.data_list[0][1])
                self.grab_data.data_list = self.grab_data.data_list[1:]
            except ValueError:
                logger.info("get_real_data value error_1.")
                continue
        
            if(last_time == 0):
                last_time = now_time - now_time % 60
                logger.info("last_time is %d" % last_time)
            # print(now_time, now_price)
            one_minute_price.append(now_price)
            
            # 每秒执行卖出策略检查
            self.sell_strategy(now_price)
            
            if(now_time - last_time == 60 or now_time % 60 == 0):
                logger.info("new candle is formed")
                
                try:
                    one_minute_candle = self.get_one_minute_candle_data(now_time, one_minute_price)
                    self.histroy_data_m.append(one_minute_candle)
                    self.close_price_list.append(one_minute_candle[-2])
                    
                    if len(self.histroy_data_m) > 30:
                        self.histroy_data_m = self.histroy_data_m[1:]
                        self.close_price_list = self.close_price_list[1:]
                except ValueError:
                    logger.info("get_real_data value error_2.")
                else:
                    logger.info(self.histroy_data_m)
                    # 每次K线形成的时候执行买入检查
                    self.buy_strategy(now_price)
                
                    # 清空状态
                    last_time = now_time
                    one_minute_price.clear()
            time.sleep(1)
    
    def get_ma(self, interval):
        try:
            dataframe = {'data':self.close_price_list}
            df = pd.DataFrame(dataframe)
            df = df.data.rolling(interval).mean()
        except:
            logger.info("get_ma error.")
            return 0
        else:
            return df[len(df) - 1]

    def buy_strategy(self, now_price):
        ma5 = self.get_ma(5)
        ma10 = self.get_ma(10)
        ma30 = self.get_ma(30)
        if np.isnan(ma5) or np.isnan(ma10) or np.isnan(ma10):
            return
        if ma5 > ma10 and ma10 > ma30 and len(self.histroy_data_m[-1]) > 5 and self.histroy_data_m[-1][5] is 'green':
            if self.buy_long_cnt == 0 and self.buy_short_cnt == 0:
                self.grab_data.click.click_buy_long()
                self.buy_long_price = now_price
                self.buy_long_cnt = self.buy_long_cnt + 1
                logger.info("buy long at %d", now_price)
        if ma5 < ma10 and ma10 < ma30 and len(self.histroy_data_m[-1]) > 5 and self.histroy_data_m[-1][5] is 'red':
            if self.buy_long_cnt == 0 and self.buy_short_cnt == 0:
                self.grab_data.click.click_buy_short()
                self.buy_short_price = now_price
                self.buy_short_cnt = self.buy_short_cnt + 1
                logger.info("buy short at %d", now_price)
    
    # 存在的问题：卖出成功如何判断？            
    def sell_strategy(self, now_price):
        if self.buy_long_cnt > 0 and self.buy_long_price - now_price >= 6:
            self.grab_data.click.click_sell()
            self.buy_long_cnt = self.buy_long_cnt - 1
            logger.info("earn money over 6, sell at %d", now_price)
        if self.buy_short_cnt > 0 and now_price - self.buy_short_price >= 15:
            self.grab_data.click.click_sell()
            self.buy_short_cnt = self.buy_short_cnt - 1
            logger.info("lost money over 15, sell at %d", now_price)
            
    def printKeyInfo(self):
        print(self.buy_long_cnt)


if __name__ == '__main__':
    if os.path.exists('config.ini') is False:
        print('配置文件损坏，程序无法运行!')
        exit()
    # 测试获取实时数据的功能
    q_trade = Q_trade()
    q_trade.get_real_data()

    gd.app.exit()