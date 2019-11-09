import tkinter as tk
import tkinter.messagebox as tm

root = tk.Tk()
root.title('交易辅助程序')
# root.geometry('500x300')
tk.Label(root, text='输入MA5数值').grid(row=0, sticky=tk.E)
ma5 = tk.Entry(root)
ma5.grid(row=0, column=1, sticky=tk.E)

tk.Label(root, text='输入MA10数值').grid(row=1, sticky=tk.E)
ma10 = tk.Entry(root)
ma10.grid(row=1, column=1, sticky=tk.E)

tk.Label(root, text='输入MA30数值').grid(row=2, sticky=tk.E)
ma30 = tk.Entry(root)
ma30.grid(row=2, column=1, sticky=tk.E)

def show():
    tm.showinfo('name', [ma5.get(), ma10.get(), ma30.get()])
    root.quit()

tk.Button(root, text='确定', command=show).grid(row=3, column=0, columnspan=2)
# lbl.pack(side=tk.LEFT)
# txt.pack(side=tk.LEFT)


# print()

root.mainloop()