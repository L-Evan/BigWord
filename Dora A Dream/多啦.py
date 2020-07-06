# 画树2
import datetime
#turtle库
import turtle
#随机库
import random
#数学库
import math
'''
作者: 180021105120 刘堉嘉 18软件工程3班
樱花树下的哆啦A梦  
2020年6月4日19:26:05
'''
# 樱花树背景  改造知乎版本
#----------------------------------------------------
#树枝长度  画笔  q是树枝分叉数
def tree(len,t,q):
    #长度为5 或者分叉不够1.5画颜色
    if len<5 or q<1.5 :
        #在最后的时候得画一下树叶
        if random.randint(0, 1) == 0 :
            # 有概率白色
            t.color('snow')
        else:
            # 接近根部，粉色
            t.color('lightcoral')
        #下次终止
        q = 1.5
        # 递归结束出口
        len/=2
        #3为极限
        if(len<3):
            #递归出口
            return  1
    elif len<8:
        # 有概率白色
        if random.randint(0, 1) == 0 and len<7:
            # 另外一种颜色
            t.color('snow')
        else :
            # 接近根部，粉色
            t.color('lightcoral')
        # 画笔变细/2
        t.pensize(len / 2)
    else:
        # 树干颜色
        t.color('sienna')
        # 画笔变细/10
        t.pensize(len/10)
    # 画树枝
    t.pd()
    # 最根部长点
    # if(len == 80):
    #     t.fd(len/2)
    t.fd(len)
    t.up()
    #树变化交叉系数
    a = random.random()
    # 树长度系数  控制树枝短
    b = random.random()
    # 树平恒系数
    c = random.uniform(-0.1,0.1)
    # 计算 分叉的个数
    qc = q/2*(1+c)
    # 偏转
    t.right(30*a)
    #画右边
    q1 = tree(len-10*b,t, qc)
    #复原
    t.left(60*a)
    #画左边
    q2 = tree(len-10*b,t, q-q1)
    # print(q1-q2)
    # 转到相对中间
    t.right(30*a)
    # 返回
    t.fd(-len)
    # 台笔
    t.up()
    # 返回已有枝条数量
    return q1+q2

# 掉落的花瓣
def petal(m, t):
    # 随机位置
    for i in range(m):
        # 范围内随机 长和宽
        a = 400- 800 * random.random()
        # 范围内随机
        b = 30- 150 * random.random()
        # 台笔
        t.up()
        # 向前到达b x
        t.forward(b)
        # 转方向
        t.left(90)
        # 向前到达a y
        t.forward(a)
        # 下笔
        t.down()
        t.color('lightcoral')  # 淡珊瑚色
        # 画点大小
        t.circle(1)
        # 台笔
        t.up()
        # 回去位置
        t.backward(a)
        # 返回
        t.right(90)
        # 返回
        t.backward(b)
# 画树 x y 坐标  big 树枝理论数量  翻车概率小很多
def main(x,y,big = 20000):
    # 取turtle
    t = turtle
    #设置背景演示
    turtle.screensize(bg='wheat')
    #移动位置
    t.up()
    # 到达画树的点
    t.goto(x,y)
    # 窗口句柄
    win = turtle.Screen()
    # 转
    t.left(90)
    # 主干长度40  树枝总数20000  平衡效果  让树2边差不多大
    tree(80,t,big)
    pass

# 自画哆啦A梦
#----------------------------------------------------
# 笔
t = turtle
# 窗口
screen = t.Screen()
# 画普通原  ang角度  fill是否填充
def drawcircle(x, y,r,ang,  color,fill = False):
    turtle.pu()
    # 去到对应的位置
    t.goto(x, y)
    turtle.pd()
    #填充颜色
    if fill:
        # 开始
        t.begin_fill()
        # 画圆
        t.circle(r, ang);
        # 画布颜色
        turtle.color(color)
        # 开始
        t.end_fill()
    else:
        t.circle(r, ang);
# 方便画椭圆定义上一个点击点的源心位置
lastx = 0
lasty = 0
# 定义一个关联事件，当鼠标点击时，显示点击坐标，方便找点
def location(x,y):
    global lastx,lasty
    #判断点击边界内  吧图片显示出来对照
    if(x>300 and y>300 ):
        #设置背景
        turtle.bgpic(r'duola.png')
    #边界外隐藏查看具体效果
    elif(x<-300 and y>300 ):
        turtle.bgpic(r"")
    #输出坐标  中心计算中心点
    print("坐标：", x, y)
    # 输出中心
    print("中心：", (lastx+x)/2, (lasty+y)/2)
    # 赋值x
    lastx = x
    # 赋值y
    lasty = y
#无轨迹移点
def gopoint(x,y):
    turtle.penup()
    turtle.goto(x,y)
    turtle.pendown()

# 椭圆 x y a b 开始角度  角度 精度 斜率  是否逆序画 方便填充颜色
def ellipseR(x,y,a,b,startangle,angle,steps,rotateAngle,bool=True):
    # 固定精度   作为参数是为了画更快调试
    steps = 500
    turtle.penup()
    # 开始角度计算
    startsteps = startangle*steps//angle
    # 根据精度  计算所需步数
    minAngle = (2*math.pi/360) * angle / steps
    # 斜率计算
    rotateAngle = rotateAngle/360*2*math.pi
    # 是否逆序画  方便填充
    if bool:
        # 开始的位置
        start = startsteps
    else :
        # 逆序位置
        start = steps-1
    # 根据步数循环移动点位置
    for i in range(startsteps,steps):
        # 是否逆序
        if(not bool):
            # 计算逆序
            i = steps-1-i+startsteps
        # 计算x y
        nextPoint = [a*math.sin((i+1)*minAngle),-b*math.cos((i+1)*minAngle)]
        # 计算x y
        nextPoint = [x+nextPoint[0]*math.cos(rotateAngle)-nextPoint[1]*math.sin(rotateAngle),
                     y+nextPoint[0]*math.sin(rotateAngle)+nextPoint[1]*math.cos(rotateAngle)]
        # 开始的时候无轨迹 跳转
        if i == start:
            # 跳转
            turtle.setpos(nextPoint)
            # 无笔痕迹
            turtle.pd()
        # 移动到每个点
        turtle.setpos(nextPoint)

# 头外围框
def bluehead():
    # 画笔颜色
    t.color('blue')
    # 去到头那里
    gopoint(117,-35)
    t.begin_fill()
    t.pd()
    #下边
    ellipseR(113,23,120,60,-5,20,130,8)
    # 外围
    ellipseR(115,18,66,64,40,230,130,0)
    t.pu()
    # 内围
    ellipseR(70,5,66,57,40,180,200,0,bool = False)
    #上围
    # ellipseR(82,65,20,8,10,125,130,30)
    t.end_fill()

# 领子
def linz():
    # 红色
    t.color('red')
    # 起笔
    t.penup()
    # 跳到领子附近
    gopoint(81,-47)
    # 填充
    t.begin_fill()
    # 笔抬起
    t.pd()
    # 改变方向
    turtle.setheading(90)
    # 画2边
    t.fd(8)
    # 领上
    ellipseR(113,23,120,60,340,378,130,8)
    # 设置方向
    turtle.setheading(-90)
    # 画2条左右线
    t.fd(8)
    # 领下
    ellipseR(113,16,120,60,340,378,130,8,bool=False)
    # 填充
    t.end_fill()
# 眼睛
def yanjin():
    # 黑线
    t.color('black')
    # 到点
    gopoint(90,62)
    # 眼肉
    t.begin_fill()
    # 眼球
    ellipseR(82,65,20,8,40,580,130,30)
    # 填充白色
    t.color('white')
    # 填充
    t.end_fill()
    # 眼纹
    t.color('black')
    # 眼线
    ellipseR(68, 61, 10, 5, 30, 150, 130, 30)

# 鼻子
def bizi():
    # 定方向
    turtle.setheading(0)
    # 红底
    drawcircle(54,48,8,360,"red",fill=True)
    # 白光
    drawcircle(51, 56, 2, 360, "white", fill=True)
    # ellipseR(53, 57, 20, 20, 40, 180, 200, 0)
# 铃铛
def lindan():
    # 定方向
    turtle.setheading(0)
    t.color("black")
    # 画原
    drawcircle(70, -55, 12, 360, "yellow",fill=True)
    t.color("black")
    # 画孔
    drawcircle(62, -50, 3, 360, "black", fill=True)
    # 画条纹
    gopoint(61,-41)
    # 画条纹
    t.goto(80, -50)
    # 画条纹
    gopoint(63, -37)
    # 画条纹
    t.goto(82, -46)
#嘴巴 和白头
def sitou():
    #画填充白
    t.color("black")
    # 到点画
    gopoint(68,-28)
    # 填充
    t.begin_fill()
    # # 嘴下
    ellipseR(68, 13, 45, 35, -55, 40, 10, 60)
    # 嘴上
    ellipseR(59, 45, 45, 37, -25, 70, 150, -5, bool=False)
    # 上翘的鼻子
    ellipseR(44, 24, 12, 8, 60, 275, 130, 70, bool=False)
    # 头外围
    ellipseR(115, 18, 66, 64, -42, 255, 130, 0, bool=False)
    # 甜白
    t.color("white")
    # 填充
    t.end_fill()
    # 黑色
    t.color("black")
    # 到达点填充  减误差
    gopoint(69,-27)
    # 填充
    t.begin_fill()
    # # 嘴下
    ellipseR(68, 13, 45, 35, -55, 40, 10,60)
    # # 嘴上
    ellipseR(59, 45, 45, 37, -5, 70, 130, -5,bool=False)
    # # 嘴左
    t.color((144, 51, 80))
    # 到达减少误差
    turtle.goto(67,-12)
    # 填充
    t.end_fill()
    #--------------------
    # 嘴内
    t.pu()
    # 设置舌头颜色 取的rbg
    t.color( (174, 81, 87))
    # 到达点填充
    t.goto(101,27)
    # 填充
    t.begin_fill()
    # 石头上 边缘
    ellipseR(59, 45, 45, 37, 20, 70, 130, -5, bool=False)
    # 石头
    ellipseR(85, 0, 27, 15, -185, 40, 130, 58)
    # 石头下 边缘
    ellipseR(68, 13, 45, 35, 30, 40, 10, 60)
    t.end_fill()
    # 黑色
    t.color("black")
    # 3条胡须
    gopoint(73,48)
    # 3条胡须
    turtle.goto(108, 56)
    # 3条胡须
    gopoint(74, 39)
    # 3条胡须
    turtle.goto(110, 38)
    # 3条胡须
    gopoint(73, 30)
    # 3条胡须
    turtle.goto(106, 19)
#手 尾巴  脚的⚪
def zhiti():
    # 左手 要填充白色
    t.begin_fill()
    # 方向
    turtle.setheading(12)
    # 黑色
    t.color("black")
    # 画左手
    drawcircle(18, 1, 14, 360, "black")
    # 白色
    t.color("white")
    # 填充
    t.end_fill()
    # 定方向 下面都是0方向
    turtle.setheading(0)
    # 右手 要白色
    t.begin_fill()
    # 黑色
    t.color("black")
    # 画圆
    drawcircle(186, -73, 13, 360, "black")
    t.color("white")
    # 填充
    t.end_fill()
    # 尾巴
    t.color("red")
    # 画尾巴
    drawcircle(189, -99, 7, 360, "red",fill=True)
    # 黑色
    t.color("black")
    # 尾巴线
    gopoint(176,-92)
    # 划线
    t.goto(182,-91)

# 右边蓝色圣体
def bluezhiti():
    # 黑色
    t.color("black")
    # 到达点 无色
    gopoint(105,-46)
    t.begin_fill()
    # 背包右
    ellipseR(101, -64, 27, 15, -90, 33, 18, 125,bool=False)
    # 右腿上
    ellipseR(110, -110, 27, 23, 150, 330, 20, 0)
    # 右腿下
    ellipseR(110, -108, 60, 23, -15, 80, 20, 0)
    # 屁股
    ellipseR(159, -88, 27, 15, -90, 40, 20, 110)
    # 定方向
    turtle.setheading(-90)
    # 右手下
    ellipseR(154, -8, 60, 60, 320, 355, 20, 25)
    # 画右手右
    drawcircle(174, -64, 20, -55, "black")
    # 右手上  方向
    turtle.setheading(122)
    # 画右手上
    drawcircle(181, -51, 30, 92, "black")
    # 领下
    ellipseR(113, 16, 120, 60, -10, 18, 130, 8,bool=False)
    t.color("blue")
    t.end_fill()

# 臭脚
def choujiao():
    t.color("black")
    # 跳到填充点
    gopoint(70, -83)
    t.begin_fill()
    # 左腿肉下
    ellipseR(66, -103, 20, 15, 110, 230, 170, 60)
    # 左腿肉上
    ellipseR(57, -93, 18, 10, 55, 290, 180, 50, bool=False)
    # 跳到填充点 让链接自然
    gopoint(58, -84)
    # 蓝色
    t.color("blue")
    # 颜色填充
    t.end_fill()
    # 左脚
    t.goto(67, -121)
    # 开始填充
    t.begin_fill()
    # 黑色
    t.color("black")
    # 画圆
    ellipseR(66, -103, 20, 15, 87, 320, 180, 60)
    # 白色
    t.color("white")
    # 填充
    t.end_fill()
    # 右脚
    t.goto(89, -124)
    # 开始
    t.begin_fill()
    # 黑颜色
    t.color("black")
    # 填充
    t.begin_fill()
    # 花园
    ellipseR(84, -105, 23, 20, 100, 340, 180, 38)
    # 白色
    t.color("white")
    # 填充
    t.end_fill()

# 口袋
def koudai():
    # 到点
    gopoint(74, -83)
    t.begin_fill()
    # 底部口袋左
    turtle.setheading(-50)
    # 花园
    drawcircle(73, -84, 25, -70, "black")
    # 口袋上
    t.goto(82, -47)
    # 画线
    t.goto(105, -46)
    # 画口袋
    ellipseR(103, -67, 27, 15, -90, 35, 20, 125, bool=False)
    # 填充
    t.color("white")
    # 填充
    t.end_fill()
    # 口袋上
    t.color("black")
    # 到达
    gopoint(69,-65)
    # 开始填充
    t.begin_fill()
    #画线
    t.goto(106,-66)
    # 口右
    turtle.setheading(-240)
    # 画圆
    drawcircle(104,-66,20,-60,"black")
    # 口袋左
    turtle.setheading(-50)
    # 画口袋做
    drawcircle(73, -84, 25, -70, "black")
    # 白色
    t.color("white")
    # 填充
    t.end_fill()

# 左手
def zuoshou():
    # 到达点
    gopoint(62,-51)
    # 开始填充
    t.begin_fill()
    # 黑色
    t.color("black")
    # 左手左
    ellipseR(48, -20, 50, 15, -45, 57, 180, -50,bool=False)
    # 方向
    turtle.setheading(12)
    # 左手上
    drawcircle(18, 1, 14, 60, "black")
    # 左手右
    t.color("black")
    # 左手右
    ellipseR(48, -25, 60, 15, 163, 220, 180, -45,bool=False)
    # 起
    t.pd()
    # 嘴边缘
    t.goto(66,-29)
    # 靠近铃铛
    t.goto(68, -34)
    # 铃铛上
    turtle.setheading(-180)
    # 画圆
    drawcircle(68, -33, 10, 150, "black")
    # 修正颜色填充
    gopoint(55, -44)
    t.color("blue")
    # 填充
    t.end_fill()
    pass

# 水杯
def suibei():
    # 到杯底
    gopoint(3, 22)
    # 填充
    t.begin_fill()
    # 黑色
    t.color("black")
    t.pd()
    # 到杯顶
    t.goto(-6,21)
    # 杯盖
    t.goto(-11, 56)
    # 盖原
    turtle.setheading(-150)
    drawcircle(-11,56,40,-40,"black")
    # 到杯底
    t.goto(20,27)
    # 绕手
    turtle.setheading(160)
    # 画杯底 也是手
    drawcircle(20, 27, 14, 100, "black")
    t.color("white")
    # 填充
    t.end_fill()

# 画7段数码管
#----------------------------------------------------
#得到原型
def getChar(num):
    # 没个字符需要的笔画
    a={
        '0': [1,2,3,4,5,6],
        '1':[1,6],
        '2':[5,6,0,3,2],
        '3':[5,6,0,1,2],
        '4':[4,0,6,1],
        '5':[0,1,2,4,5],
        '6': [0, 1, 2, 3, 4, 5],
        '7': [1,5, 6],
        '8': [0, 1, 2, 3, 4, 5, 6],
        '9': [0, 1, 2,4, 5, 6],
        'L':[4,3,2],
        'Y': [0, 1, 2,4, 6],
        'J':[6,1,2,3]
    }
    return a[num]
# led管
def drawDigit(char):
    # 笔
    t = turtle
    # 得到字符列表
    array = getChar(char)
    # 画数码管
    for i in range(0,7):
        drawLine(i in array)
        if i!=3:
            t.right(90)
    t.right(180)
    t.up()
    t.fd(10)
# 画实 虚线
def drawLine(bol):
    randomColor()
    t = turtle
    if not bol:
        t.up()
        t.fd(60)
        return
    t.pu()
    turtle.fd(5)
    t.pd()
    t.fd(50)
    t.pu()
    t.fd(5)
#     简单画字  size作为平很 线长度
def drawFont(str,h,size):
    randomColor()
    t = turtle
    t.up()
    t.right(90)
    t.fd(h+10)
    t.write(str,font=("微软雅黑", size, "bold"))
    t.fd(-h-10)
    t.left(90)
    t.fd(size+30)

#画时间
def drawDate(date): #date的格式为%Y-%m=%d+，如：2020-05=07+
   for i in date:
      #  看到画哪个了
      if i == '-':
         drawFont('年',60,80)
      elif i == '=':
         drawFont('月',60,80)
      elif i == '+':
         drawFont('日',60,80)
      else:
         drawDigit(i)
# 画所有字符
def drawStr(ary):
    turtle.fd(20)
    for i in ary:
        drawDigit(i)
    turtle.fd(20)
def randomColor():
    # 随机颜色列表
    color = ['#e54d42','#f37b1d','#fbbd08','#8dc63f','#39b54a','#1cbbb4','#0081ff','#6739b6','#9c26b0','#e03997','#a5673f','#8799a3','#aaaaaa','#333333']
    c = random.randint(0,len(color)-1)
    turtle.color(color[c])
# 画时间和作者
def drawAutor():
    # 角度为0
    turtle.setheading(0)
    turtle.up()
    # 到达画作者的地方
    turtle.goto(-700,-300)
    turtle.pensize(7)
    # 画对应的字
    drawFont('作',60,50)
    drawFont('者',60,50)
    drawFont(':',60,50)
    # 画刘堉嘉  缩写
    drawStr('LYJ')
    # 到达画时间的
    turtle.goto(-161,-300)
    # 画时间
    drawDate( datetime.datetime.now().strftime('%Y-%m=%d+'))
 
# 画多来A梦
def duolaAmen():
    # 嘴巴
    sitou()
    #后脑勺
    bluehead()
    # 眼睛
    yanjin()
    # 领子
    linz()
    #鼻子
    bizi()
    # 肢体的一些原型
    zhiti()
    # 口袋
    koudai()
    # # 臭脚
    choujiao()
    # 右边圣体
    bluezhiti()

    # 左手
    zuoshou()
    # # 铃铛
    lindan()
    #水杯
    suibei()


# 画布大小 设置  防止树太高
turtle.screensize(2000,2000)
# 初始化窗口大小
turtle.setup(0.9,0.9)
# 把颜色255模式
turtle.colormode(255)
# 左键监听 点击位置  和2点中心 方便画画
turtle.onscreenclick(location, btn=1)
# 不看画的过程
t.tracer(False)
# 画树  翻车概率小很多
main(2,-58,5000)
# 开
t.tracer(True)
# 关箭头设置更新延迟
t.getscreen().tracer(5, 0)
petal(200, t)
# 笔大小变回
t.pensize(1)
# 画哆啦A梦
# # 方便对照着画 提交附件  设置图片
try:
    turtle.bgpic(r'duola.png')
except:
    print("没有背景图呢")
duolaAmen()
# 画时间名字
drawAutor()
# 真名字
t.up()
t.goto(-642,-158)
drawFont('刘堉嘉',60,50)
# 停下来
t.done()

