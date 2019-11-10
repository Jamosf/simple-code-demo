from PyQt5.QtWidgets import QApplication
from PyQt5.QtGui import *
import win32api, win32gui, win32con
import tkinter as tk
import sys
import os
import configparser
import time
from aip import AipOcr

app = QApplication(sys.argv)
screen = QApplication.primaryScreen()

class Click:
    def __init__(self):
        self.buy_long_btn_pos = [0, 0]
        self.buy_short_btn_pos = [0, 0]
        self.sell_btn_pos = [0, 0]
        self.grab_pos = [0, 0, 0, 0]
        self.is_find_btn = False
        self.is_find_grab_area = False
        desktop = QApplication.desktop()
        self.display_height = desktop.screenGeometry().height()
        self.display_width = desktop.screenGeometry().width()
        config = configparser.ConfigParser()
        config.read('config.ini', encoding='utf-8')
        self.form_name_test = config.get('app', 'name')
        self.buy_long_btn = config.get('app', 'buyLongBtn')
        self.buy_short_btn = config.get('app', 'buyShortBtn')
        self.sell_btn = config.get('app', 'sellBtn')
        self.now_price = config.get('app', 'nowPrice')
        self.appId = config.get('baidu', 'appId')
        self.apiKey = config.get('baidu', 'apiKey')
        self.secretKey = config.get('baidu', 'secretKey')
        self.factor = float(config.get('grabconf', 'desktopFactor'))
        self.handle = win32gui.FindWindow(None, self.form_name_test)
    
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
    
    def init_btn_position(self):
        win32gui.ShowWindow(self.handle, win32con.SW_RESTORE)
        win32gui.SetForegroundWindow(self.handle)
        time.sleep(2)
        img = screen.grabWindow(self.handle, self.display_width*self.factor, 0, self.display_width, self.display_height).toImage()
        img.save('desktop.png')
        with open('desktop.png', 'rb') as f:
            img_content = f.read()
            client = AipOcr(self.appId, self.apiKey, self.secretKey)
            msg = client.accurate(img_content)
            # print(msg.get('words_result'))
            if msg.get('words_result') is None:
                print('desktop图片失败出错！请检查desktop图片是否正常.')
                f.close()
                return
            self.get_pos_from_msg(msg)
            f.close()
            os.remove('desktop.png')
            
    def init_btn_position_with_retry(self):
        while self.is_find_btn is False or self.is_find_grab_area is False:
            self.init_btn_position()
    
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
        
    def printKeyInfo(self):
        print(self.buy_long_btn_pos)
        print(self.buy_short_btn_pos)
        print(self.sell_btn_pos)
        print(self.grab_pos)
        
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
    # click = Click()
    # click.init_btn_position_with_retry()
    # click.printKeyInfo()
    # click.click_buy_long()
    
    # 测试gui功能
    gui = Gui()
    gui.start_main_tk()