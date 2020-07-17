**项目说明** 
- 本项目是基于cofcoFrame的分支，采用流行的框架springMvc+spring+mybatis+shiro+redis+ehcache开发,实现了权限管理（菜单权限、数据权限），完善的代码生成器


**项目功能** 
- 权限管理：采用Shiro实现功能权限和机构部门的数据控件权限，可控件菜单权限、按钮权限、机构部门权限（数据权限）
- 缓存：使用redis+ehcahe整合shiro自定义sessionDao实现分布式集群共享session，redis可采用单机方式，也可以集群哨兵模式。可以灵活的切换模式
- app接口：基于Json web token (JWT)认证用户信息,使用swagger生成一个具有互动性的api文档控制台。
- 页面交互使用了vue+html和最普通的jsp+jstl标签，两种交互都写了相应的模板，可以选择适合的交互方式。
- 完善的代码生成机制，可在线生成entity、xml、dao、service、html、js、sql代码,可快速开发基本功能代码，能把更多的精力放在问题难点。
- 采用layer友好的弹框，和layerUI相对漂亮的界面，让OA系统看起来稍微好看点。

 **技术选型：**
  
- 核心框架：Spring Framework 4.3.7.RELEASE
- 缓存：redis 3.07
- 权限框架：Apache Shiro 1.3
- 视图框架：Spring MVC 4.3
- 持久层框架：MyBatis 3.3
- 前端页面：Vue2.x、jstl、bootstrap、layer、layerUI


 **软件环境** 
- JDK1.8
- Maven3.0
- Tomcat7.0
- redis 3.07


 **本地部署**
- 创建数据库，数据库编码为UTF-8,导入cofco.sql脚本
- 修改conf/jdbc.properties文件，更改DB账号和密码
- redis服务,可以使用单机redis也可以配置哨兵集群模式






