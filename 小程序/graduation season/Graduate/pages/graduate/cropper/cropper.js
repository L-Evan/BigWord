const util = require('../../../utils/util.js')
//获取应用实例
const app = getApp()
Page({
  data: {
    src:'',
    width: 250,//宽度
    height: 300,//高度
    max_width: 400,
    max_height: 400,
    disable_rotate:false,//是否禁用旋转
    disable_ratio: false,//锁定比例
    limit_move: false,//是否限制移动 
    isfirst :true
  },
  onLoad: function (options) {    
    let that = this 
    this.cropper = this.selectComponent("#image-cropper");  
    setTimeout( (function() { 
      that.upload()
      //循环代码
    } ), 1000); 
  } ,
  cropperload(e) {
    console.log('cropper加载完成');
  },
  loadimage(e){
    wx.hideLoading();
    console.log('图片'); 
    this.cropper.imgReset();
  },
  clickcut(e) {
    console.log(e.detail);
    //图片预览
    wx.previewImage({
      current: e.detail.url, // 当前显示图片的http链接
      urls: [e.detail.url] // 需要预览的图片http链接列表
    })
  },
  upload(){   
    let that = this;
    wx.chooseImage({
      count: 1, 
      sizeType: [ 'compressed'],
      sourceType: ['album', 'camera'],
      success(res) {   
        wx.showLoading({
          title: '加载中',
        })
        const tempFilePaths = res.tempFilePaths[0];    
        //重置图片角度、缩放、位置
        that.cropper.imgReset();
        that.setData({
          src: tempFilePaths
        });
      },fail:function(res){ 
        console.log(res)
        wx.navigateBack({
          delta: -1 
        })
      } 
    })
  },
  setWidth(e){
    this.setData({
      width: e.detail.value < 10 ? 10 : e.detail.value
    });
    this.setData({
      cut_left: this.cropper.data.cut_left
    });
  },
  setHeight(e){
    this.setData({
      height: e.detail.value < 10 ? 10 : e.detail.value
    });
    this.setData({
      cut_top: this.cropper.data.cut_top
    });
  },
  switchChangeDisableRatio(e){
    //设置宽度之后使剪裁框居中
    this.setData({
      disable_ratio: e.detail.value
    });
  },
  setCutTop(e) {
    this.setData({
      cut_top: e.detail.value
    });
    this.setData({
      cut_top: this.cropper.data.cut_top
    });
  },
  setCutLeft(e) {
    this.setData({
      cut_left: e.detail.value
    });
    this.setData({
      cut_left: this.cropper.data.cut_left
    });
  },
  switchChangeDisableRotate(e) {
    //开启旋转的同时不限制移动
    if (!e.detail.value){
      this.setData({
        limit_move: false,
        disable_rotate: e.detail.value
      });
    }else{
      this.setData({
        disable_rotate: e.detail.value
      });
    }
  },
  switchChangeLimitMove(e) {
    //限制移动的同时锁定旋转
    if (e.detail.value){
      this.setData({
        disable_rotate: true
      });
    }
    this.cropper.setLimitMove(e.detail.value);
  },
  switchChangeDisableWidth(e) {
    this.setData({
      disable_width: e.detail.value
    });
  },
  switchChangeDisableHeight(e) {
    this.setData({
      disable_height: e.detail.value
    });
  },
  submit(){ 
    this.cropper.getImg((obj)=>{
      app.globalData.imgSrc = obj.url;
      var pages = getCurrentPages();
      var beforePage = pages[pages.length - 2]; 
      wx.navigateBack({
        delta: -1,
        success: function(){
          console.log(obj)
          beforePage.SubForm(obj);
        }
      })
    });
  },
  rotate(){
    //在用户旋转的基础上旋转90°
    this.cropper.setAngle(this.cropper.data.angle+=90);
  },
  top(){
    this.data.top = setInterval(() => {
      this.cropper.setTransform({
        y: -3
      });
    }, 1000 / 60)
  },
  bottom(){
    this.data.bottom = setInterval(() => {
      this.cropper.setTransform({
        y: 3
      });
    }, 1000 / 60)
  },
  left(){
    this.data.left = setInterval(() => {
      this.cropper.setTransform({
        x: -3
      });
    }, 1000 / 60)
  },
  right(){
    this.data.right = setInterval(() => {
      this.cropper.setTransform({
        x: 3
      });
    }, 1000 / 60)
  },
  narrow() {
    this.data.narrow = setInterval(() => {
      this.cropper.setTransform({
        scale: -0.02
      });
    }, 1000 / 60)
  },
  enlarge() {
    this.data.enlarge = setInterval(() => {
      this.cropper.setTransform({
        scale: 0.02
      });
    }, 1000 / 60)
  },
  end(e) {
    clearInterval(this.data[e.currentTarget.dataset.type]);
  },
})
