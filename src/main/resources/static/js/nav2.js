/*
 * 用户界面
 * 存储用户界面的左侧导航栏的结构
 * author 陈杰
 */
/*这是全局的变量*/
var navs = [{
    "title" : "个人信息管理",
    "icon" : "icon-computer",
    "href" : "",
    "spread" : false
},{
    "title":"租入订单",
    "icon" : "icon-computer",
    "href" : "page/main.html",
    "spread" : false,
    "children":[
        {
            "title":"租入的车",
            "icon" : "icon-computer",
            "href" : "page/main.html",
            "spread" : false,
        },{
            "title":"租入的车位",
            "icon" : "icon-computer",
            "href" : "page/main.html",
            "spread" : false,
        }
    ]
},{
    "title":"租出订单",
    "icon" : "icon-computer",
    "href" : "",
    "spread" : false,
    "children":[
        {
            "title":"租出订单",
            "icon" : "icon-computer",
            "href" : "",
            "spread" : false,
        }
    ]
},{
    "title":"发布车辆出租信息",
    "icon" : "icon-computer",
    "href" : "page/main.html",
    "spread" : false
},{
    "title":"我要租车",
    "icon" : "icon-computer",
    "href" : "page/main.html",
    "spread" : false
},{
    "title":"我要租车位",
    "icon" : "icon-computer",
    "href" : "page/main.html",
    "spread" : false
},{
    "title":"留言系统",
    "icon" : "icon-computer",
    "href" : "page/main.html",
    "spread" : false
}]