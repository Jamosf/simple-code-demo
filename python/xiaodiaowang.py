from bs4 import BeautifulSoup
import requests
import sys
import os
from concurrent.futures import ThreadPoolExecutor, wait, ALL_COMPLETED
import aiohttp
import asyncio
# import ssl

# ssl._create_default_https_context = ssl._create_unverified_context
# print(sys.getdefaultencoding())

url = "https://www.xiaopian.com/"

header = {"User-Agent": "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/77.0.3865.75 Safari/537.36"}
response = requests.get(url, verify=False)

response_html = BeautifulSoup(response.text.encode("ISO-8859-1").decode("gbk"), "lxml")
movie_list = response_html.select(".co_content222 a")

if os.path.exists("/Users/Jamos/Documents/python/photo"):
    os.chdir("/Users/Jamos/Documents/python/photo")
    pass
else:
    os.mkdir("/Users/Jamos/Documents/python/photo")
    os.chdir("/Users/Jamos/Documents/python/photo")

def get_img_url_list(movie_list):
    img_url_list = []
    for movie in movie_list:
        sub_url = url + movie["href"]
        sub_response = requests.get(sub_url, verify=False).text
        img_url_html = BeautifulSoup(sub_response, "lxml")
        img_url_list.extend(img_url_html.select("img"))
    return img_url_list
    
def download(url, filename):
    if os.path.exists("/Users/Jamos/Documents/python/photo/" + filename):
        return
    img_content = requests.get(url, verify=False).content
    with open(filename, "wb") as wf:
        wf.write(img_content)

def exec():
    executor = ThreadPoolExecutor(max_workers=10)
    img_url_list = get_img_url_list(movie_list)
    future_task = [executor.submit(download, url["src"], url["alt"].encode("ISO-8859-1").decode("gbk")) for url in img_url_list]
    wait(future_task, return_when=ALL_COMPLETED)


    


if __name__ == '__main__':
    print("start...")
    exec()
    print("complete!")