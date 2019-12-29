import logging

# 获取logger实例，如果参数为空则返回root logger
logger = logging.getLogger("AppName")

# 指定logger输出格式
formatter = logging.Formatter('%(asctime)s %(levelname)-8s: %(message)s')

# 文件日志
# t = time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
# file_handler = logging.FileHandler(str(int(round(time.time()))) + ".log")
file_handler = logging.FileHandler("trade_log.txt")
file_handler.setFormatter(formatter)  # 可以通过setFormatter指定输出格式

# 控制台
console_handler = logging.StreamHandler()
console_handler.setFormatter(formatter)

# 为logger添加的日志处理器
logger.addHandler(file_handler)
logger.addHandler(console_handler)

logger.setLevel(logging.INFO)