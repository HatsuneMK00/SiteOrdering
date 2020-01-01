# Meethere场馆预约系统
## Contribution
- 项目经理：曹威杰
- 前端开发：袁非凡、曹威杰
- 后端开发：郭省吾、罗竣夫
- 测试部分：
  - 单元：郭省吾
  - 系统：罗竣夫
  - 集成：罗竣夫
  - 性能：曹威杰
## [Meethere home](http://47.101.217.16:8080/ "start Meethere")
- Deployed with Ali light-weight ECS server
  - 1 CORE/2GB RAM/40GB ROM/10Mbps Bandwidth
- Backend
  - Springboot
- Frontend
  - Layui
## Navigation
- [source code](https://github.com/HatsuneMK00/SiteOrdering/tree/master/src/main/java)
- [unit testing code](https://github.com/HatsuneMK00/SiteOrdering/tree/master/src/test)
- [testing converage](https://github.com/HatsuneMK00/SiteOrdering/tree/master/覆盖度部分)
- [integration testing](https://github.com/HatsuneMK00/SiteOrdering/tree/master/接口测试部分)
- [system testing](https://github.com/HatsuneMK00/SiteOrdering/tree/master/系统测试部分)
- [performance testing](https://github.com/HatsuneMK00/SiteOrdering/tree/master/性能测试部分)


<!-- ## doc

### 关于搜索功能的说明

#### /order/match

- 请求体使用json传递查询字符串。键为"match"。例如 {"match": "time: 2019-12-23"}
- 可用前缀：
    - time: 搜索当天全部订单
        - 例如：time: 2019-12-23
    - uid: 搜索用户uid的全部订单
        - 例如：uid: 1,2,3,4 / uid: 1
    - gid: 搜索场馆gid的全部订单
        - 同上
    - 空： 全部
    - 无前缀： 不允许，返回500状态码
- 返回值（键result对应的值的类型）
    - time: order列表
    - uid： order列表
    - gid: order列表
    - 空： order列表

#### /ground/match

- 请求体使用json传递查询字符串。键为"match"。
- 可用前缀：
    - gid: 搜索场馆gid的全部场馆
        - 例如：gid: 1,2,3
    - 空： 全部
    - 无前缀： 与场馆名称进行匹配
- 返回值（键result对应的值的类型）
    - gid: ground列表
    - 空： ground列表
    - 无前缀： ground列表
    
#### /comment/match

- 请求体使用json传递查询字符串。键为"match"。
- 可用前缀：
    - uid: 搜索用户uid的全部留言
        - 例如：uid: 1,2,3
    - gid: 搜索场馆gid的全部留言
    - 空： 全部
    - 无前缀： 与留言内容进行匹配
- 返回值（键result对应的值的类型）
    - uid: comment列表
    - gid: comment列表
    - 空： comment列表
    - 无前缀： comment列表 -->