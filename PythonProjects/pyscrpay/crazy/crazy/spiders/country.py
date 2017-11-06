# -*- coding: utf-8 -*-
import scrapy
from scrapy import Request
import re
from crazy.items import ImageItem
from scrapy.contrib.loader import ItemLoader
import urlparse

class FirstSpider(scrapy.Spider):
    name = "first"
    allowed_domains = ["1024.c2048ao.pw/"]
    start_urls = ['http://1024.c2048ao.pw/pw/thread.php?fid=16']
    download_delay = 2

    def parse(self, response):
        cate = response.xpath('//td[@style="text-align:left;padding-left:8px"]/h3/a/@href').extract()
        url_content = urlparse.urlparse(response.url)
        scheme = url_content.scheme
        net_location = url_content.netloc
        for link in cate:
            if re.findall(r"htm_data",link) == '':
                continue
            else:
                url = scheme + "://" + net_location + "/pw/" + link
                yield Request(url, callback=self.download_image,dont_filter=True)
            #
            # request.meta['*'] = item
            # return request

    def download_image(self,response):
        item = ImageItem()
        srcs = response.xpath('//div[@class="tpc_content"]/a/img/@src').extract()
        item['image_urls'] = srcs
        yield item
        # yield {"test" : srcs}
