from bs4 import BeautifulSoup
import requests
import sys
import os
from concurrent.futures import ThreadPoolExecutor, wait, ALL_COMPLETED
import redis
import pickle

sys.setrecursionlimit(1000000)

url = "http://a1.0e8c2c0f.rocks/pw/thread.php?fid=3"

base_url = "http://a1.0e8c2c0f.rocks/pw/"

header = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36"}
response = requests.get(url, verify=False)

# response_html = BeautifulSoup(response.text.encode("ISO-8859-1").decode("gbk"), "lxml")

if os.path.exists("/Users/Jamos/Documents/python/1024xp"):
    os.chdir("/Users/Jamos/Documents/python/1024xp")
    pass
else:
    os.mkdir("/Users/Jamos/Documents/python/1024xp")
    os.chdir("/Users/Jamos/Documents/python/1024xp")

r = redis.Redis(host="127.0.0.1", port=6379)

def get_urls(html):
    print("getting urls.....")
    img_url_list = []
    soup = BeautifulSoup(html, "lxml")
    one_page_film_list = soup.select("td h3 a")
    for one_item_url in one_page_film_list:
        if "动漫" in one_item_url.string:
            continue
        sub_url = base_url + one_item_url["href"]
        sub_response = requests.get(sub_url, verify=False).text
        img_url_html = BeautifulSoup(sub_response, "lxml")
        img_url_list.extend(img_url_html.select("#read_tpc img"))
        #print(img_url_list)
    print("finished!!")
    return img_url_list

def get_urls_to_redis(html):
    print("getting urls.....")
    img_url_list = []
    soup = BeautifulSoup(html, "lxml")
    one_page_film_list = soup.select("td h3 a")
    for one_item_url in one_page_film_list:
        if "动漫" in one_item_url.string:
            continue
        sub_url = base_url + one_item_url["href"]
        if r.exists(sub_url) == 1:
            continue
        sub_response = requests.get(sub_url, verify=False).text
        img_url_html = BeautifulSoup(sub_response, "lxml")
        byte_list = pickle.dumps(img_url_html.select("#read_tpc img"))
        # print(byte_list)
        r.rpush(sub_url, byte_list)
        # img_url_list.extend(img_url_html.select("#read_tpc img"))
        #print(img_url_list)
    print("finished!!")

def fecth(url):
    with requests.get(url, verify=False) as response:
        return response.content

def download_1024xp(url):
    filename = hash(url)
    print("downloading...")
    if os.path.exists("/Users/Jamos/Documents/python/1024xp/" + str(filename) + ".jpg"):
        print("already exist!!")
        return
    try:
        img_content = fecth(url)
        with open(str(filename) + ".jpg", "wb") as wf:
            wf.write(img_content)
    except Exception as err:
        print(err)

def exec():
    executor = ThreadPoolExecutor(max_workers=20)
    get_urls_to_redis(response.text)
    key_list = r.keys("*")
    for key in key_list:
        future_task = [executor.submit(download_1024xp, url["src"]) for url in pickle.loads(r.lindex(key, 0))]
        wait(future_task, return_when=ALL_COMPLETED)
    
    

if __name__ == '__main__':
    print("starting...")
    exec()
    print("finished")