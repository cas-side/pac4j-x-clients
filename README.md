# pac4j-x-clients

## 基础设施版本

- pac4j: 3.6.1
- spring-webmvc-pac4j: 3.2.0
- spring-security-pac4j: 4.0.0

## try

启动 `src/test/java/com/github/casside/pac4jx/clients/app/Application.java`

### try 账号密码登录

浏览器访问 `http://domain:8080/xxx` 会自动重定向到 spring 自带的登录页，

输入账号 `admin/admin`

### try 企业微信应用二维码授权


浏览器访问 `http://domain:8080/third/wechat_work/index`，会跳转到企业微信应用二维码授权页面
