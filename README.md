# meethere

## doc

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
    - uid： order的二维数组，其中result[i]为查询uid中的第i+1个uid对应的order列表
    - gid: 同上
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
    - uid: comment的二维数组，其中result[i]表示第i+1个用户的留言列表
    - gid: 同上
    - 空： comment列表
    - 无前缀： comment列表