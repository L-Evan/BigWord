export default {
  /** 基础获取部分 */
  getData: 'https://www.tbetbe.com/graduate/request/openid.php', // 获取openid，课表，成绩等
  checkBind: 'https://www.tbetbe.com/cj/checkbind.php', // 检查是否已经绑定
  checkPhoneBind: 'https://www.tbetbe.com/cj/checkPhoneBind.php', // 检查手机是否已经绑定
  unBind: 'https://www.tbetbe.com/cj/unbind.php', // 解绑操作
  getInfo: 'https://www.tbetbe.com/cj/getName.php', // 获取个人信息

  /** 首页 */
  getGPS: 'https://www.tbetbe.com/cj/gps.php', // 获取用户位置
  livePusher: 'https://www.tbetbe.com/cj/livePusher.php', // 动态推送
  myLevel: 'https://www.tbetbe.com/cj/mylevel.php', // 首页个人中心 - 我的等级
  baiduMap: 'https://api.map.baidu.com/geocoder/v2/', // 百度地图经纬度转城市
  getWeather: 'https://free-api.heweather.net/s6/weather/now?', // 获取天气信息

  /** 北理出行 */
  campusBus:'https://www.tbetbe.com/cj/campusBus.php', // 获取校园巴士信息
  ZHBus:'https://api.timecont.com/', // 获取珠海公交/华发-沃尔玛信息

  /** 校园社区 */
  getBBS: 'https://www.tbetbe.com/cj/bbs.php', // 加载/发表/删除/评论帖子
  uploadImage: 'https://pan.tbetbe.com/cj/uploadbbsimage.php', // 上传图片

  /** 院线热映 */
  in_theaters: 'https://douban.uieee.com/v2/movie/in_theaters', // 正在热映
  film_coming_soon: 'https://douban.uieee.com/v2/movie/coming_soon', // 即将上映

  /** OCR/语音识别接口 */
  OCR: 'https://pan.tbetbe.com/cj/api/smartimage/sample.php', // 文字识别
  audioRec: "https://www.tbetbe.com/cj/api/voice.php", // 语音识别
  autoChat: 'https://www.tbetbe.com/cj/api/chat.php', // 智能回复

  /** 准考证防丢 */
  memo: 'https://www.tbetbe.com/cj/forget.php', // 获取/更新准考证号

  /** 站内消息 */
  chat: 'https://www.tbetbe.com/cj/chat/chat.php', // 获取聊天信息，搜索同学，标记已读
  send: 'https://www.tbetbe.com/cj/chat/send.php', // 发送消息 
  msgNotice: 'https://www.tbetbe.com/cj/sms/main/send/msgNotice.php' ,// 发送短信提醒
 /** 毕业照 */
 gra_image: 'https://pan.tbetbe.com/graduate/image',//图片域名
 gra_rqsdata: 'https://www.tbetbe.com/graduate/request/getData.php',//请求获得数据
 gra_token:'https://sub.tbetbe.com/request/getToken.php', //gra token
 sub_image: 'https://aip.baidubce.com/rest/2.0/image-classify/v1/body_seg', //获取切割图片
 getsub_image: 'https://pan.tbetbe.com/graduate/request/getHeads.php', //存切割图片
 deletesub_image: 'https://www.tbetbe.com/graduate/request/deleteHead.php', //删除图片
 music: 'https://www.tbetbe.com/graduate/music/graduation.mp3' //音乐

}