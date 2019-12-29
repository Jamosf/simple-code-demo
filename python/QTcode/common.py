from PyQt5.QtCore import QBuffer
from PIL import Image, ImageGrab
from io import BytesIO, StringIO


def get_img_buffer(buffer, image):
    buffer.open(QBuffer.ReadWrite)
    image.save(buffer, 'png')
    # pil_im = Image.open(BytesIO(buffer.data()))

    # pil_im.show()
    return buffer.data()

