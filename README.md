# gobang
+ ### 三个界面
  + /go 棋盘界面，登录后可进；输入相同房间号的两人都准备后可下棋 get
  + /view/login 邮箱登录界面，登录后跳转到/go get
  + /view/register 邮箱注册界面，需验证邮箱，注册成功后跳转到/view/register get
+ ### 一些接口
  + /email 获取验证码 post
  + /login 验证登录 post
  + /register 注册 post
  + /gobang 连接websocket websocket
+ ### 运行环境
  + springboot打成war包 + mysql + tomcat
