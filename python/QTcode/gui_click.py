from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
import win32api, win32gui, win32con
import tkinter as tk
import sys
import os
import configparser
import time
from aip import AipOcr
from looger import *

app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

class Click:
    def __init__(self):
        self.is_find_btn = False
        self.is_find_grab_area = False
        self.is_find_hold_btn = False
        self.config = configparser.ConfigParser()
        self.config.read('config.ini', encoding='utf-8')
        self.form_name_test = self.config.get('app', 'name')
        self.buy_long_btn = self.config.get('app', 'buyLongBtn')
        self.buy_short_btn = self.config.get('app', 'buyShortBtn')
        self.sell_btn = self.config.get('app', 'sellBtn')
        self.now_price = self.config.get('app', 'nowPrice')
        self.hold = self.config.get('app', 'hold')
        self.appId = self.config.get('baidu', 'appId')
        self.apiKey = self.config.get('baidu', 'apiKey')
        self.secretKey = self.config.get('baidu', 'secretKey')
        self.factor = float(self.config.get('grabconf', 'desktopFactor'))
        self.buy_long_btn_pos = self.get_pos_from_config('buyLong')
        self.buy_short_btn_pos = self.get_pos_from_config('buyShort')
        self.sell_btn_pos = self.get_pos_from_config('sell')
        self.grab_pos = self.get_pos_from_config('price')
        self.hold_pos = self.get_pos_from_config('hold')
        self.handle = win32gui.FindWindow(None, self.form_name_test)
        _, _, self.display_width, self.display_height = win32gui.GetWindowRect(self.handle)
    
    def get_pos_from_config(self, name):
        return list(map(float, self.config.get('pos', name).split(',')))
    
    def get_pos_from_msg(self, msg):
        for i in msg.get('words_result'):
            if self.buy_long_btn in i.get('words') and self.buy_short_btn in i.get('words') and self.sell_btn in i.get('words'):
                self.is_find_btn = True
                self.buy_long_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] / 6 + self.display_width*self.factor)
                self.buy_long_btn_pos[1] = i.get('location')['top']
    
                self.buy_short_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] / 2 + self.display_width*self.factor)
                self.buy_short_btn_pos[1] = i.get('location')['top']
                
                self.sell_btn_pos[0] = int(i.get('location')['left'] + i.get('location')['width'] * 5 / 6 + self.display_width*self.factor)
                self.sell_btn_pos[1] = i.get('location')['top'] 

            if self.now_price in i.get('words'):
                self.is_find_grab_area = True
                self.grab_pos[0] = i.get('location')['left'] + self.display_width*self.factor
                self.grab_pos[1] = i.get('location')['top']
                self.grab_pos[2] = i.get('location')['width']
                self.grab_pos[3] = i.get('location')['height']
            
            if self.hold in i.get('words'):
                self.is_find_hold_btn = True
                self.hold_pos[0] = i.get('location')['left']
                # 可平量往下偏移30个单位
                self.hold_pos[1] = i.get('location')['top'] + 30
    
    def init_btn_position(self):
        logger.info("init_btn_position start")
        try:
            if os.path.exists('desktop.png') is False:
                win32gui.ShowWindow(self.handle, win32con.SW_RESTORE)
                win32gui.SetForegroundWindow(self.handle)
                time.sleep(2)
                print(self.display_width*self.factor, 0, self.display_width, self.display_height)
                img = screen.grabWindow(self.handle, self.display_width*self.factor, 0, self.display_width, self.display_height).toImage()
                img.save('desktop.png')
            with open('desktop.png', 'rb') as f:
                img_content = f.read()
                client = AipOcr(self.appId, self.apiKey, self.secretKey)
                msg = client.accurate(img_content)
                print(msg)
                logger.info(msg)
                if msg.get('words_result') is None:
                    print('desktop图片失败出错！请检查desktop图片是否正常.')
                    f.close()
                    return
                self.get_pos_from_msg(msg)
                f.close()
                # os.remove('desktop.png')
        except:
            logger.info("init_btn_position failed")
        else:
            logger.info("init_btn_position success")
            
    def init_btn_position_with_retry(self):
        if any(self.buy_long_btn_pos) is True and any(self.buy_short_btn_pos) is True and any(self.sell_btn_pos) is True and any(self.grab_pos) is True and any(self.hold_pos) is True:
            logger.info("read position from config success")
            return
        while self.is_find_btn is False or self.is_find_grab_area is False or is_find_hold_btn is False:
            self.init_btn_position()
            time.sleep(5)
        # 将获取到的坐标位置保存到配置文件中
        self.set_pos_to_config()

    def set_pos_to_config(self):
        print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>")
        self.config.set('pos', 'price', ','.join(map(lambda x:str(x), self.grab_pos)))
        self.config.set('pos', 'buyLong', ','.join(map(lambda x:str(x), self.buy_long_btn_pos)))
        self.config.set('pos', 'buyShort', ','.join(map(lambda x:str(x), self.buy_short_btn_pos)))
        self.config.set('pos', 'sell', ','.join(map(lambda x:str(x), self.sell_btn_pos)))
        self.config.set('pos', 'hold', ','.join(map(lambda x:str(x), self.hold_pos)))
        print("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<")
    
    def click_windows(self, x, y):
        postion = win32api.GetCursorPos()
        win32api.SetCursorPos((int(x), int(y)))
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
        win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0) 

        time.sleep(0.1)
        win32api.SetCursorPos((postion[0], postion[1]))

    def click_buy_long(self):
        self.click_windows(self.buy_long_btn_pos[0], self.buy_long_btn_pos[1])

    def click_buy_short(self):
        self.click_windows(self.buy_short_btn_pos[0], self.buy_short_btn_pos[1])
    
    def click_sell(self):
        time.sleep(0.1)
        self.click_hold()
        self.click_hold()
        time.sleep(0.1)
        self.click_windows(self.sell_btn_pos[0], self.sell_btn_pos[1])

    def click_hold(self):
        self.click_windows(self.hold_pos[0], self.hold_pos[1])
        
    def printKeyInfo(self):
        print(self.buy_long_btn_pos)
        print(self.buy_short_btn_pos)
        print(self.sell_btn_pos)
        print(self.grab_pos)
        print(self.hold_pos)
        
    def run(self):
        self.init_btn_position_with_retry()
        
class Gui:
    def __init__(self):
        self.root = tk.Tk()
        pass  
        
    def get_ma_from_input(self, ma5_text, ma10_text, ma30_text):
        self.ma5 = ma5_text
        self.ma10 = ma10_text
        self.ma30 = ma30_text
        self.root.quit()

    def start_main_tk(self):
        tk.Label(self.root, text='输入MA5数值').grid(row=0, sticky=tk.E)
        ma5_input = tk.Entry(self.root)
        ma5_input.grid(row=0, column=1, sticky=tk.E)

        tk.Label(self.root, text='输入MA10数值').grid(row=1, sticky=tk.E)
        ma10_input = tk.Entry(self.root)
        ma10_input.grid(row=1, column=1, sticky=tk.E)

        tk.Label(self.root, text='输入MA30数值').grid(row=2, sticky=tk.E)
        ma30_input = tk.Entry(self.root)
        ma30_input.grid(row=2, column=1, sticky=tk.E)
        
        tk.Button(self.root, text='确定', command=lambda:self.save_ma_to_ini(ma5_input.get(), ma10_input.get(), ma30_input.get())).grid(row=3, column=0, columnspan=2)

        self.root.mainloop()
    
if __name__ == '__main__':
    # 测试click功能
    click = Click()
    #click.init_btn_position()
    click.init_btn_position_with_retry()
    click.printKeyInfo()
    # click.click_buy_long()
    time.sleep(1)
    click.click_hold()
    click.click_hold()
    click.click_hold()
    time.sleep(0.5)
    click.click_sell()
    
    # 测试gui功能
    # gui = Gui()
    # gui.start_main_tk()