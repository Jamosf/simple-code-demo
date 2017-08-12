def good():
	print "i am a chnese"
for i in range(0,2,6):
	good()
	print "this is the "+ str(i) + " times"
# a = raw_input("please input a number: ")
# print a
b = [1,2,3,"hello world"]  # list
b.append(8)
b.insert(2,6)
print b
c = (1,2,9,"yes today")  # trupe
print c
d = {'fengshangren':'168','zhanwenqing':'158'}  # dict
d['fengshangxin'] = '165'
print d
a = 'iloveyou'
print a[0]
print a[0:2]

file = open('1.txt','r+')
for line in file.readlines():
	file.write("nishi")
	print line
file.close()

def show():
	print 10
	return 0

show()

print 10 + 12
print 12**2

if a[0] > 1:
	print 'woaini'
else:
	print 'wobuaini'

e = 30
name = 'liangliang'
print ('{0} was {1} years old'.format(name,e))




