# vue-admin-template

> 基于 vue-admin-template 改造的 vue admin 管理后台模板，接口数据均由mock模拟返回，适配对应的接口即可使用。

## Demo

线上预览: [http://vue-admin.findfuture.cn/](http://vue-admin.findfuture.cn/)

![](//img.cdn.aliyun.dcloud.net.cn/stream/plugin_screens/499f9040-d6eb-11ea-aa0f-8ba3ce0886f3_0.png?1596613611)
![](//img.cdn.aliyun.dcloud.net.cn/stream/plugin_screens/499f9040-d6eb-11ea-aa0f-8ba3ce0886f3_1.png?1596613615)
![](//img.cdn.aliyun.dcloud.net.cn/stream/plugin_screens/499f9040-d6eb-11ea-aa0f-8ba3ce0886f3_2.png?1596613621)
![](//img.cdn.aliyun.dcloud.net.cn/stream/plugin_screens/499f9040-d6eb-11ea-aa0f-8ba3ce0886f3_3.png?1596613625)

## 构建

```bash
# 克隆项目(码云)
git clone https://gitee.com/iimeepo/vue-admin-template

# 克隆项目(GitHub)
git clone https://github.com/iimeepo/vue-admin-template

# 进入项目目录
cd vue-admin-template

# 安装依赖
npm install

# 解决 npm 下载速度慢的问题
npm install --registry=https://registry.npm.taobao.org

# 启动服务
npm run dev
```

浏览器访问: [http://localhost:9529](http://localhost:9529)

## 发布

```bash
# 构建测试环境
npm run build:stage

# 构建生产环境
npm run build:prod
```

## 其它

```bash
# 预览发布环境效果
npm run preview

# 预览发布环境效果 + 静态资源分析
npm run preview -- --report

# 代码格式检查
npm run lint

# 代码格式检查并自动修复
npm run lint -- --fix
```
