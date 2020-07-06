//app.js
import api from "utils/api" 
let app = getApp();
App({ 
  onLaunch: function () {
    // 展示本地存储能力
    app = this
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)
    app.globalData.openid =  wx.getStorageSync('openid') || null
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
              console.log(res.code) 
              console.log(openid)
              // console.log('index.js中获取到的openid：' + openid.data.openid) 
              app.globalData.openid = openid.data.openid;
              wx.setStorageSync('openid', app.globalData.openid);
            }
          })
        }
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              app.globalData.userInfo = res.userInfo 
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
  },
  globalData: {
    userInfo: null,
    openid:null
  }
})