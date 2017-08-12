# -*- coding: utf-8 -*-
# import scrapy
# from scrapy.linkextractors import LinkExtractor
# from scrapy.spiders import CrawlSpider, Rule
# from example.items import ExampleItem
# import urlparse
# import urllib


# class CountrySpider(CrawlSpider):
#     name = 'country'
#     start_urls = ['http://1024.c2048ao.pw/pw/htm_data/15/1708/730626.html']
#
#     def parse(self,response):
#         cate = response.xpath('//div[@class="tpc_content"]')
#         link_list = cate.xpath('./a/@href').extract()
#         for url in link_list:
#             yield{
#                 # "test" : url
#                 scrapy.Request(url ,callback=self.download_image)
#             }
#         # yield {
#         #   "test" : cate.xpath('./a/@href').extract_first()
#         # }
#
#
#             # print base_url
#             # yield{
#             #    scrapy.Request(base_url,callback=self.download_image)
#             # }
#
#     def download_image(self,response):
#         url_content = urlparse.urlparse(response.url)
#         url_query = url_content.query
#         attr_file = url_query.split("&")[0].split("=")
#         file_name = urllib.unquote(attr_file[1])
#         download_path = "pic/" + file_name
#         with open(download_path, "wb") as wf:
#             wf.write(response.body)

# -*- coding: utf-8 -*-
import scrapy
from scrapy import Request
import re
from newproject.items import ImageItem
from scrapy.contrib.loader import ItemLoader
import urlparse

class XiaohuaSpider(scrapy.Spider):
    name = "baidumeinv"
    allowed_domains = ["1024.c2048ao.pw/"]
    start_urls = ['http://1024.c2048ao.pw/pw/thread.php?fid=14']
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



