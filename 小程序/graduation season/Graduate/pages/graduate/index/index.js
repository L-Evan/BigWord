import api from "../../../utils/api"

let app = getApp();
var urlre = /^(https):\/\/([\w.]+\/?)\S*/;
let index = -1,
  items = [],
  flag = true,
  itemId = 1,
  zindex = 100,
  zdindex = 100;
let isloding = false;
let that;
let modent = 0;
const canvasPre = 1.5; // 展示的canvas占mask的百分比 
let temptime = 0;
const maskCanvas = wx.createCanvasContext('maskCanvas');
// const shareCanvas = wx.createCanvasContext('shareCanvas');
Page({
      /**
       * 页面的初始数据
       */
      data: {
        canvasPre: canvasPre,
        //音乐url
        musicurl: api.music,
        //音乐播放控制 
        shifouplay: false,
        //偏移
        backtop: 0.16,
        backleft: 0,
        openid: null,
        urlhead: api.gra_image, //api地址
        slidebar: 0,
        remind: '加载中',
        content: 0,
        ani_slide1: '', //侧边栏打开动画
        ani_slide2: '', //背景按钮动画
        ani_slide3: '', //衣服按钮动画
        ani_slide4: '', //工具动画按钮
        choose: 0, //判断是否选中 
        syshcw: 16 / 9, //系统比例
        canvasz: 1000, //画布高度
        index: -1, //显示人像下表
        token: '',
        canvasHeight: 320,
        canvasWidth: 568,
        imgList: [],
        clothesid: 0, //衣服s
        clothesImage: [{
          image: '/clothe (1).png'
        }],
        headid: 0,
        headsImage: [], //头部列表
        backId: 0,
        pointId: 0,
        headwidth: 7,
        num: 1, //上传图片的数量
        //背景图
        backImage: [{
          hCw: 0.666,
          url: '/back (1).jpg'
        }],
        itemList: []
      },
      /**
       * 生命周期函数--监听页面加载
       */
      onLoad: function (options) {
        that = this 
        //合成要不要重新渲染
        this.isdraw = 0
        this.drawTime = 0
        this.getToken()
        this.updata()
        items = this.data.itemList;
        //获取屏幕信息
        wx.getSystemInfo({
          success: sysData => {
            this.sysData = sysData
            console.log(sysData)
            this.setData({
              // 如果觉得不清晰的话，可以把所有组件、宽高放大一倍  canvasPre
              canvasWidth: this.sysData.windowWidth * canvasPre,
              canvasHeight: this.sysData.windowWidth * canvasPre * this.data.backImage[this.data.backId].hCw,
            })
            console.log(this.data.canvasHeight)
            console.log(this.data.canvasWidth)
          }
        }) 
  },
  updata() {
    wx.request({
      url: api.gra_rqsdata,
      data: {
        openid: app.globalData.openid
      },
      success(res) {
        console.log(res)
        let tempback = that.data.backImage;
        tempback = res.data.back
        let tempclothe = that.data.clothesImage;
        tempclothe = res.data.clothe
        let temphead = null;
        if (app.globalData.openid) {
          temphead = res.data.head
        }
        
        that.setData({
          [res.data.musickey]: res.data.music ? res.data.music : api.music,
          clothesImage: tempclothe,
          backImage: tempback,
          headsImage: temphead == null ? that.data.headsImage : temphead
        }) 
        //播放音乐
        that.playMusic();
        wx.getImageInfo({
          src: api.gra_image + '/back (1).jpg',
          success(res) { 
            console.log(res)
              that.data.backImage[0].url = res.path
              that.data.backImage[0].hCw = res.height / res.width  
         }
        }) 
      },
      fail(res) {
        console.log(res)
        that.serverHide();
      }
    })
  },
  /**
   * 播放音乐
   */
  playMusic: function (ev) {
    var shifouplay = this.data.shifouplay;
    if (shifouplay) {
      wx.pauseBackgroundAudio();
      this.setData({
        shifouplay: false
      })
    } else {
      wx.playBackgroundAudio({
        dataUrl: that.data.musicurl,
        title: '毕业快乐',
        success(res) {
          console.log(res)
        },
        fail(res) {
          console.log(res)
        }
      })
      this.setData({
        shifouplay: true
      })
    }
  },
  //切换画布
  changeBack(e) {
    let id = e.currentTarget.dataset.id;
    let backImage = this.data.backImage;
    let len = this.data.itemList.length;
    let nid = id;
    if (urlre.test(that.data.backImage[nid].url))
      wx.getImageInfo({
        src: that.data.backImage[nid].url,
        success(res) {
          that.data.backImage[nid].url = res.path
          that.data.backImage[nid].hCw =  res.height/res.width
        }
      })
    this.setData({
      backId: nid,
      canvasHeight: this.data.canvasWidth * this.data.backImage[nid].hCw
    })
    this.drawTime = 0
  },
  getopenid() {
    wx.login({
      success: function (res) {
        if (res.code) {
          //发起网络请求
          wx.request({
            url: api.getData,
            data: {
              code: res.code
            },
            success: function (openid) {
              // console.log('index.js中获取到的openid：' + openid.data.openid) 
              app.globalData.openid = res.data.openid;
              wx.setStorageSync('openid', app.globalData.openid);
            }
          })
        }
      }
    })
  },
  //选择图片  
  ChooseImage(res) {
    if (res.detail.userInfo) {
      if (this.data.openid == null && !app.globalData.openid) {
        this.getopenid()
      }
      wx.navigateTo({
        url: '../cropper/cropper',
      })
    }
  },
  /**
   * 裁剪头
   */
  Heads(e) {
    let id = e.target.dataset.id
    if (urlre.test(this.data.headsImage[id].image)) {
      console.log("update")
      this.setDropItem({
        url: this.data.headsImage[id].image,
        top: that.data.clicky,
        left: that.data.clickx,
        ishead: true,
        id: id
      })
      return
    }
    console.log(e.target.dataset.id)

    let item = JSON.parse(JSON.stringify(this.data.headsImage[id]));
    item.id = ++itemId
    //item.left = this.data.clickx - item.width / 2
    item.left = this.data.clickx - item.width / 2
    item.top = this.data.clicky - item.height / 2
    item.x = this.data.clickx
    item.y = this.data.clicky
    zindex++
    item.z = zindex
    items[items.length] = item
    this.setData({
      itemList: items
    })
    this.drawTime = 0;
  },
  /**
   * 存衣服
   */
  Clothes(e) {
    console.log(e.target.dataset.url)
    this.setDropItem({
      url: e.target.dataset.url, //this.data.clothesImage[this.data.clothesid].url,
      top: this.data.clicky,
      left: this.data.clickx,
      isclose: true,
      id: e.target.dataset.id
    });
  },
  //获得时间挫
  getTime() {
    //时间挫
    var timestamp = Date.parse(new Date());
    timestamp = timestamp / 1000;
    return timestamp;
  },
  /**
   * 分割头像 接收回来的
   */
  SubForm: function (e) {
    //询问
    wx.showModal({
      title: '提交',
      content: '人体切割, 建议图片只留头部（需要耐心等待）',
      cancelText: '取消',
      confirmText: '提交',
      success: res => {
        //运行
        if (res.confirm) {
          wx.showLoading({
            title: '图片处理中...',
          })
          let token = this.data.token
          //判断token
          if (token == '') {
            that.serverHide()
            console.log("无token")
            that.getToken() 
            return;
          }
          let imgList = e.url
          wx.getFileSystemManager().readFile({
            filePath: imgList, //选择图片返回的相对路径
            encoding: 'base64', //编码格式
            success: res => { //成功的回调
              console.log(res)
              //连接console.log('data:image/png;base64,' + res.data)   
              imgList = res.data;
              //请求百度云
              wx.request({
                url: api.sub_image,
                //必须
                header: {
                  "content-type": "application/x-www-form-urlencoded"
                },
                method: 'post',
                data: {
                  image: imgList,
                  access_token: token,
                  type: 'foreground',
                },
                success(res) { //成功的回调  
                  console.log(res)
                  //请求自己服务器
                  wx.request({
                    url: api.getsub_image,
                    //必须
                    header: {
                      "content-type": "application/x-www-form-urlencoded"
                    },
                    method: 'post',
                    data: {
                      'foreground': res.data.foreground,
                      openid: app.globalData.openid,
                      error_code: res.data.error_code ? res.data.error_code : 0
                    },
                    success(res) {
                      console.log(res)
                      if (urlre.test(res.data)) {  
                        wx.hideLoading({})
                        let img = res.data;
                        console.log(res)
                        //放到画图里面
                        that.setDropItem({
                          url: img,
                          top: that.data.clicky,
                          left: that.data.clickx,
                          ishead: true
                        })
                      } else {
                        if (res.data == 0) {
                          wx.showToast({
                            title: '数据错误！',
                            icon: 'none'
                          })
                        } else {
                          wx.showToast({
                            title: res.data,
                            icon: 'none'
                          })
                        }
                      }
                    },
                    fail(res) {
                      wx.hideLoading({})
                      console.log(res)
                    },
                    complete(res) {
                      console.log(res)
                    }
                  })
                },
                fail(res) {
                  wx.hideLoading({})
                  console.log("和服务器无关")
                  wx.showToast({
                    title: '上传错误！',
                    icon: 'none'
                  })
                }
              })
            }
          })
        }
      }
    })
  },
  //关掉loding 服务器繁忙
  serverHide() {
    wx.hideLoading();
    wx.showToast({
      title: '服务器繁忙,请再稍等一会！',
      icon: 'none',
      duration: 2000
    })
  },
  //token
  getToken() {
    var that = this
    //拿token
    wx.request({
      url: api.gra_token,
      success(res) {
        that.data.token = res.data.access_token
        console.log("token:" + res.data.access_token)
      },
      fail(res) {
        console.log(res)
        wx.showToast({
          title: "系统繁忙！",
          duration: 2000
        })
      }
    })
  },
  //放头
  setDropItem(imgData) {
    wx.showLoading({
      title: "放置中..."
    })
    let data = {}
    console.log(imgData)
    wx.getImageInfo({
      src: imgData.url,
      success: res => {
        console.log(res)
        let proportion = res.height / res.width;
        // 初始化数据
        data.width = 100; //宽度
        data.height = 100 * proportion; //高度   
        data.image = res.path; //地址 
        data.id = ++itemId; //id 
        data.top = imgData.top ? imgData.top - data.height / 2 : 0; //top定位
        data.left = this.sysData.windowWidth/2- data.width / 2//imgData.left ? imgData.left - data.width / 2 : 0; //left定位
        //圆心坐标
        data.x = data.left + data.width / 2;
        data.y = data.top + data.height / 2;
        data.scale = 1; //scale缩放
        data.oScale = 1; //方向缩放  
        data.rotate = 1; //旋转角度
        data.active = false; //选中状态
        zindex++
        data.z = zindex;
        data.angle = 0
        //头方向  meiyon
        if (imgData.arg) {
          data.rotate = imgData.arg + 1
          data.angle = imgData.arg;
        }
        console.log(data)
        items[items.length] = data;
        //头铁列表
        let heads = that.data.headsImage
        //头部
        if (imgData.ishead) {
          //保留数据
          data.url = imgData.url
          //是头部信息
          data.ishead = true;
          //处理是否新增
          if (imgData.id || imgData.id == 0) {
            heads[imgData.id] = data;
          } else {
            console.log("+")
            //[].concat 可能浪费内存
            heads = [data].concat(heads)
          }
        }
        let closs = that.data.clothesImage
        //可能bug点
        if (imgData.isclose) {
          closs[imgData.id].image = data.image
        }
        this.setData({
          itemList: items,
          headsImage: heads,
          clothesImage: closs
        })
        this.drawTime = 0
        console.log(items)

      },
      complete(res) {
        wx.hideLoading({})
      }
    })
  },
  setHeadDown(res) {
    if (index != -1 && items[index].active) {
      items[index].z = --zdindex;
      items[index].active = false;
      this.setData({
        ['itemList[' + index + ']']: items[index]
      })
      index = -1
      return
    }
    zdindex--;
    console.log(items[index])
    for (let i = 0; i < items.length; i++) {
      if (items[i].ishead != true) {
        items[i].z = zdindex;
      }
      this.setData({
        itemList: items
      })
    }
  },
  setHeadUp(res) {
    if (index != -1 && items[index].active) {
      items[index].z = ++zindex;
      items[index].active = false
      this.setData({
        ['itemList[' + index + ']']: items[index]
      })
      index = -1
      return
    }
    zindex++;
    for (let i = 0; i < items.length; i++) {
      if (items[i].ishead != true) {
        items[i].z = zindex;
      }
      this.setData({
        itemList: items
      })
    }
  },
  //start图片
  WraptouchStart: function (e) {
    wx.vibrateShort()
    let id = e.currentTarget.dataset.id
    //找点
    for (let i = 0; i < items.length; i++) {
      items[i].active = false;
      if (e.currentTarget.dataset.id == items[i].id) {
        console.log(items[i])
        console.log("现在的高度" + items[i].z)
        zindex += 1;
        items[i].z = zindex;
        index = i;
        items[index].active = true;
      }
    }
    console.log()
    this.setData({
      itemList: items,
      index: id,
      clicky: e.changedTouches[0].pageY,
      clickx:  e.changedTouches[0].pageX,
    })
    //定x,y 
    console.log("x:" + this.data.clickx + "y:" + this.data.clicky)
    //没点东西
    if (id == -1) {
      return
    }
    items[index].lx = e.touches[0].pageX //clientX;
    items[index].ly = e.touches[0].pageY //clientY;   
  },
  //移动
  WraptouchMove(e) {
    //节流
    if (flag) {
      flag = false;
      setTimeout(() => {
        flag = true;
        console.log("??")
      }, 100)
    }
    if (e.currentTarget.dataset.id == -1) {
      return
    }
    // console.log('WraptouchMove', e)
    items[index]._lx = e.touches[0].pageX //clientX;
    items[index]._ly = e.touches[0].pageY //clientY;

    items[index].left += items[index]._lx - items[index].lx;
    items[index].top += items[index]._ly - items[index].ly;
    items[index].x += items[index]._lx - items[index].lx;
    items[index].y += items[index]._ly - items[index].ly;

    items[index].lx = e.touches[0].pageX //clientX;
    items[index].ly = e.touches[0].pageY //clientY;
    // console.log(items)itemList: items,
    this.setData({
      ['itemList[' + index + ']']: items[index]
    })
  },
  //每次结束画一下
  WraptouchEnd() {
    this.drawTime = 0
    //this.synthesis()
  },
  oTouchStart(e) {
    //找到点击的那个图片对象，并记录
    for (let i = 0; i < items.length; i++) {
      items[i].active = false;
      if (e.currentTarget.dataset.id == items[i].id) {
        console.log('e.currentTarget.dataset.id', e.currentTarget.dataset.id)
        index = i;
        items[index].active = true;
      }
    }
    //获取作为移动前角度的坐标
    items[index].tx = e.touches[0].pageX //clientX;
    items[index].ty = e.touches[0].pageY //clientX;;
    //移动前的角度
    items[index].anglePre = this.countDeg(items[index].x, items[index].y, items[index].tx, items[index].ty)
    //获取图片半径
    items[index].r = this.getDistancs(items[index].x, items[index].y, items[index].left, items[index].top);
    console.log(items[index])
    console.log("over")
  },
  oTouchMove: function (e) {
    if (flag) {
      flag = false;
      setTimeout(() => {
        flag = true;
        console.log("?")
      }, 100)
    }
    console.log(e.touches[0])
    //记录移动后的位置
    items[index]._tx = e.touches[0].pageX //clientX;
    items[index]._ty = e.touches[0].pageY //clientY; 
    //移动的点到圆心的距离
    items[index].disPtoO = this.getDistancs(items[index].x, items[index].y, items[index]._tx, items[index]._ty - 10)

    items[index].scale = items[index].disPtoO / items[index].r;
    items[index].oScale = 1 / items[index].scale;

    //移动后位置的角度
    items[index].angleNext = this.countDeg(items[index].x, items[index].y, items[index]._tx, items[index]._ty)
    //角度差
    items[index].new_rotate = items[index].angleNext - items[index].anglePre;

    //叠加的角度差
    items[index].rotate += items[index].new_rotate;
    items[index].angle = items[index].rotate; //赋值

    //用过移动后的坐标赋值为移动前坐标
    items[index].tx = e.touches[0].pageX //clientX;
    items[index].ty = e.touches[0].pageY //clientX;
    items[index].anglePre = this.countDeg(items[index].x, items[index].y, items[index].tx, items[index].ty)
    console.log(11)
    //赋值setData渲染
    this.setData({
      ['itemList[' + index + ']']: items[index]
    })
  },
  /**
   * 更新中心点位置
   */
  getDistancs(cx, cy, pointer_x, pointer_y) {
    var ox = pointer_x - cx;
    var oy = pointer_y - cy;
    return Math.sqrt(
      ox * ox + oy * oy
    );
  },
  /*
   *参数1和2为图片圆心坐标
   *参数3和4为手点击的坐标
   *返回值为手点击的坐标到圆心的角度
   */
  countDeg: function (cx, cy, pointer_x, pointer_y) {
    var ox = pointer_x - cx;
    var oy = pointer_y - cy;
    var to = Math.abs(ox / oy);
    var angle = Math.atan(to) / (2 * Math.PI) * 360;
    // console.log("ox.oy:", ox, oy)
    if (ox < 0 && oy < 0) //相对在左上角，第四象限，js中坐标系是从左上角开始的，这里的象限是正常坐标系  
    {
      angle = -angle;
    } else if (ox <= 0 && oy >= 0) //左下角,3象限  
    {
      angle = -(180 - angle)
    } else if (ox > 0 && oy < 0) //右上角，1象限  
    {
      angle = angle;
    } else if (ox > 0 && oy > 0) //右下角，2象限  
    {
      angle = 180 - angle;
    }
    return angle;
  },
  /**
   * 添加衣服
   */
  addclos(res) {
    wx.chooseImage({
      count: 1,
      sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: (result) => {
        console.log(result)
        let closes = [{
          image: result.tempFilePaths[0]
        }].concat(this.data.clothesImage)
        this.setData({
          clothesImage: closes
        })
      },
    })
  },
  /**
   * 添加背景
   */
  addback(res) {
    wx.chooseImage({
      count: 1,
      sizeType: ['original'],
      sourceType: ['album', 'camera'],
      success: (result) => {
        console.log(result)
        //计算比例
        wx.getImageInfo({
          src: result.tempFilePaths[0],
          success(res) {
            console.log(res)
            let backs = [{
              url: result.tempFilePaths[0],
              hCw: res.height / res.width
            }].concat(that.data.backImage)
            that.setData({
              backId:0, 
              canvasHeight: that.data.canvasWidth * backs[0].hCw,
              backImage: backs
            })
          }
        })

      },
    })
  },
  /**
   * 重置
   */
  initadd() {
    wx.showModal({
      title: '重置',
      content: '重置会清空画布，不可逆哦！并且图片要重新加载！',
      success(res) {
        if (res.confirm) {
          that.drawTime = 0
          console.log('用户点击确定')
          //let backImage = JSON.parse(JSON.stringify(this.data.newbackImage)) //利用json 进行拷贝 
          items = []
          that.setData({
            itemList: items
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      }
    });

  },
  /**
   *  不用了  清楚缓存文件
   */
  clearList() {
    let that = this
    let save = wx.getFileSystemManager();
    //读取
    save.readdir({
      dirPath: wx.env.USER_DATA_PATH,
      success(res) {
        console.log(res)
        res.files.forEach((el) => { // 遍历文件列表里的数据
          console.log(el)
          if (el != 'miniprogramLog')
            save.unlink({
              filePath: wx.env.USER_DATA_PATH + '/' + el,
              complete(res) {
                console.log(res)
              }
            })
        })
      },
      complete(res) {
        items = [];
      }
    })
  },
  deleteItem: function (e) {
    let newList = [];
    for (let i = 0; i < items.length; i++) {
      if (e.currentTarget.dataset.id != items[i].id) {
        newList.push(items[i])
        items[i].active = false
        index = -1;
      }
    }
    // if (newList.length > 0) {
    //   newList[newList.length - 1].active = true;
    // }
    items = newList;
    this.setData({
      itemList: items
    })
    this.drawTime = 0
    // this.synthesis();
  },
  /**
   * 合成图片
   */
  openMask() {
    // if(this.isdraw!=0 ){  
    //   this.isdraw+=1
    // }
    // if (this.drawTime == 0 ) {  
      wx.showLoading({
        title: '合成图片中',
      })
    /*if (isloding == false) {
      wx.showLoading({
        title: '合成图片中',
      })
      isloding = true;
    }*/
    this.synthesis()
    // }
    //当没被关闭选择 让可以转动 
     if (index != -1 && items.length>index) {
      let item = items; 
      item[index].active = false;
      this.setData({
        index: -1,
        itemList: item
      })
    }
    this.setData({
      showCanvas: true
    })
  },
  /**
   * 绘制
   */
  synthesis() { // 画布绘制
    //有在画的   没用
    this.isdraw += 1
    this.drawTime = 1
    console.log("渲染")
    console.log('合成图片')
    //allUrl = this.synUrl() ---------------------------------
    var that = this
    //异步列表   
    let allUrl = []
    //背景图片url 
    let id = this.data.backId
    let url = that.data.backImage[id].url[0] == '/' ? api.gra_image + that.data.backImage[id].url : that.data.backImage[id].url;
    //!urlre.test(that.data.backImage[id].url) ? id :  
    let backimage = !urlre.test(url) ? url : new Promise(function (resolve, reject) {
      wx.getImageInfo({
        src: url,
        success: function (res) {
          let backimg = that.data.backImage
          backimg[id].hcw = res.height / res.width
          backimg[id].url = res.path
          
          that.setData({
            backImage: backimg,
            canvasHeight: backimg[id].hcw * that.data.canvasWidth
          }) 
          resolve(res.path);
        }
      })
    }) 
    //添加  
    allUrl.push(backimage)  
    //找到所有  衣服网络图片 
    //同步
    Promise.all(allUrl).then(res => { 
      console.log(res)
      //计算底部 
      const top = this.data.backtop * this.data.canvasHeight
      //背景色
      maskCanvas.save();
      maskCanvas.beginPath();  
      maskCanvas.setFillStyle('#ffffff');
      maskCanvas.fillRect(0, this.data.canvasHeight, this.data.canvasWidth, top)
      maskCanvas.closePath();
      maskCanvas.stroke();
      //背景
      maskCanvas.drawImage(res[0], 0, 0, this.data.canvasWidth, this.data.canvasHeight);
      /*
         num为canvas内背景图占canvas的百分比，若全背景num =1
         prop值为canvas内背景的宽度与可移动区域的宽度的比，如一致，则prop =1;
        */
      //参数
      const num = 1,
        prop = canvasPre;
      //画人  高度优先 渲染顺序列表  因为要等image url变成连接
      let temps = items.sort((a, b) => a.z - b.z)
      temps.forEach((currentValue, index) => {
        maskCanvas.save();
        maskCanvas.translate((this.data.canvasWidth) * (1 - num) / 2, 0);
        maskCanvas.beginPath();
        maskCanvas.translate(currentValue.x * prop, currentValue.y * prop); //圆心坐标
        maskCanvas.rotate(currentValue.angle * Math.PI / 180);
        maskCanvas.translate(-(currentValue.width * currentValue.scale * prop / 2), -(currentValue.height * currentValue.scale * prop / 2))
        maskCanvas.drawImage(currentValue.image, 0, 0, currentValue.width * currentValue.scale * prop, currentValue.height * currentValue.scale * prop);
        maskCanvas.restore();
      })
      //logo
      let bian = 15 * canvasPre
      let big = 60 * canvasPre
      maskCanvas.save();
      maskCanvas.translate(bian, bian);
      maskCanvas.drawImage("/images/logo.png", 0, 0, big, big);
      maskCanvas.restore();
      //下面
      maskCanvas.save();
      maskCanvas.translate(0, this.data.canvasHeight);
      
      //字条 
      const bi = 4.139
      bian = 0.02* top;
      big = top - 2 * bian;
      let left = 0.05 * this.data.canvasWidth;
      let len = big * bi ;
      maskCanvas.drawImage("/images/zitiaoe.png", left, bian, len, big);
      //二维码
      bian = 0.05 * top;
      big = top - 2 * bian;
      let right = 0.02 * this.data.canvasWidth;
      maskCanvas.drawImage("/images/erweima.jpg", this.data.canvasWidth - right - big, bian, big, big);
      maskCanvas.restore();

      //绘制全部
      maskCanvas.draw(false, (e) => {
        wx.canvasToTempFilePath({
          canvasId: 'maskCanvas',
          success: res => {
            this.isdraw -= 1
            console.log('draw success')
            console.log(res.tempFilePath)
            this.setData({
              canvasTemImg: res.tempFilePath
            })
          },
          complete(res) {
            isloding = false
            wx.hideLoading({})
          }
        }, this)
      })

    })
  },
  //showCanvas是预览
  disappearCanvas() {
    //关闭加载
    wx.hideLoading({})
    this.setData({
      showCanvas: false
    })
  },
  //保存到画册
  saveImg: function () {
    wx.saveImageToPhotosAlbum({
      filePath: this.data.canvasTemImg,
      success: res => {
        wx.showToast({
          title: '保存成功',
          icon: "success"
        })
      },
      fail: res => {
        console.log(res)
        wx.openSetting({
          success: settingdata => {
            console.log(settingdata)
            if (settingdata.authSetting['scope.writePhotosAlbum']) {
              wx.showToast({
                title: '系统繁忙，可以尝试重新保存试试哦！',
              })
              console.log('获取权限成功，给出再次点击图片保存到相册的提示。')
            } else {
              wx.showToast({
                title: '获取权限失败，无法保存！',
              })
              console.log('获取权限失败，给出不给权限就无法正常使用的提示')
            }
          },
          fail: error => {
            console.log(error)
          }
        })
        wx.showModal({
          title: '提示',
          content: '保存失败，请确保相册权限已打开',
        })
      }
    })
  },

  onShow: function () {
    this.ani_slide1 = wx.createAnimation({
      duration: 500,
      timingFunction: 'ease',
    })
    setTimeout(function () {
      that.setData({
        remind: ''
      });
    }, 1500);
    setTimeout(function () {
      this.setData({
        content: 1,
      })
    }.bind(this), 6900)

    // this.ani_slide2 = wx.createAnimation({
    //   duration: 500,
    //   timingFunction: 'ease',
    // })
    // this.ani_slide3 = wx.createAnimation({
    //   duration: 500,
    //   timingFunction: 'ease',
    // })
    // this.ani_slide4 = wx.createAnimation({
    //   duration: 500,
    //   timingFunction: 'ease',
    // })
  },
  //开启侧边栏
  putbar: function () {
    this.ani_slide1.translateX((-380 / 750) * wx.getSystemInfoSync().windowHeight).step()
    this.setData({
      ani_slide1: this.ani_slide1.export()
    })
    setTimeout(function () {
      this.ani_slide1.translateX((-370 / 750) * wx.getSystemInfoSync().windowHeight).step()
      this.setData({
        ani_slide1: this.ani_slide1.export(),
        slidebar: 1
      })
    }.bind(this), 500)
  },
  //关闭侧边栏
  closebar: function () {
    this.ani_slide1.translateX((0 / 750) * wx.getSystemInfoSync().windowHeight).step()
    this.setData({
      ani_slide1: this.ani_slide1.export(),
      slidebar: 0
    })
    setTimeout(function () {
      this.setData({
        ani_slide1: this.ani_slide1.export()
      })
    }.bind(this), 500)
  },
  //选择背景、衣服、工具
  slidechoose: function (e) {
    let temp = e.currentTarget.id
    if (temp == 0) {
      this.setData({
        choose: 0
      })
    } else if (temp == 1) {
      this.setData({
        choose: 1
      })
    } else if (temp == 2) {
      this.setData({
        choose: 2
      })
    }
  },
  //start
  handleTouchStart: function (e) {
    this.startTime = e.timeStamp;
  },
  //touch end
  handleTouchEnd: function (e) {
    this.endTime = e.timeStamp;
  },
  handleLongPress: function (e) {
    console.log("endTime - startTime = " + (this.endTime - this.startTime));
    console.log("长按");
    let id = e.target.dataset.id
    let itmp = that.data.headsImage[id]
    //取http的或者file
    let deurl = urlre.test(itmp.image) ? itmp.image : itmp.url;
    console.log(deurl)
    console.log(app.globalData.openid)
    wx.showModal({
      title: '提示',
      content: '是否删除人像图？',
      success(res) {
        if (res.confirm) {
          wx.request({
            url: api.deletesub_image,
            header: {
              'content-type': 'application/x-www-form-urlencoded'
            },
            method: 'POST',
            data: {
              'url': deurl,
              'openid': app.globalData.openid
            },
            success(res) {
              console.log(res)
              let heads = that.data.headsImage
              let itemps = [];
              if (res.data > 0) {
                heads.forEach((val, i) => {
                  console.log(val)
                  console.log(id)
                  console.log(i)
                  if (i != id)
                    itemps.push(val)
                })
                that.setData({
                  headsImage: itemps
                })
              } else {
                wx.showToast({
                  title: '数据错误！建议重新打开！',
                  icon: 'none'
                })
              }
            }
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
        }
      },
      fail(res) {
        wx.showToast({
          title: '系统繁忙！',
          icon: 'none'
        })
      }
    })
  },

})