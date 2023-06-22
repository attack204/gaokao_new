# 开发规范及建议

## 前提：尽量减少IDEA的警告 

1. 类字段尽量声明为`final`，包括`static`字段及普通字段； 
2. 禁止`List`/`Set`/`Map`的raw使用；
3. `@Autowired`注解优先使用构造方法注入；
4. 使用`@GetMapping`代替`@RequestMapping(method = RequestMethod.GET)`，
   `@PostMapping`代替`@RequestMapping(method = RequestMethod.POST)`；
5. 若一个Controller中多个Mapping拥有相同的前缀，则前缀应抽离到类上；若多个Controller拥有相同的前缀，则提取共有前缀到父类；
6. 对代码的修改，Git commit信息应以`type: msg`的格式进行提交（推荐使用插件Git Commit Template），`type`有如下几个选项：

- feat: 代表新增特性；
- refactor: 代表重构；
- docs: 文档改变；
- style: 代码格式改变；
- perf: 性能优化；
- test : 增加测试；
- build : 改变了build工具 如 grunt换成了npm；
- revert : 撤销上一次的 commit；
- chore : 构建过程或辅助工具的变动；
- fix: bug修复

7. Git commit最小化，不要将多次commit合并为一次提交；
8. 避免魔数的出现，多次出现的字符串或者数字抽取为常量；前端使用的Key强制抽取为常量；
9. Logger打印日志使用字符串模板，禁止使用字符串拼接；
10. 禁止新增使用标记为`@Deprecated`的方法；

## 技术选型

- 核心框架：Spring Boot
- 数据库层：Spring data jpa
- 安全框架：Spring Security
- 数据库连接池：Druid
- 前端：Vue.js
- 数据库：mysql8以上

## 快速开始

- IDEA安装lombok插件
- 创建mysql数据库

```sql
CREATE DATABASE IF NOT EXISTS Gaokao DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
CREATE USER 'Gaokao'@'%' IDENTIFIED BY 'Gaokao522';
GRANT ALL privileges ON Gaokao.* TO 'Gaokao'@'%';
flush privileges;
```

## 数据初始化

* 创建好数据库后，执行doc文件夹下的几个sql文件。（除了insert_perm_data.sql）
* 注：**最后一步执行insert_perm_data.sql**，导入管理平台权限信息。
  * 超管帐号：admin
  * 密码：12345678

超级管理员初始化权限后，就可以通过给超级管理员分配权限来查看运营管理的页面了。分配权限后，需要刷新页面重新登录才会生效。
