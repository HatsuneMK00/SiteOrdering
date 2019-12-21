/*
 * 后台
 * 存储后台管理系统的左侧导航栏的结构
 * author 陈杰
 */
/*
* 全局变量*/
var navs = [{
    "title": "后台首页",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "注册用户管理",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "停车位信息管理",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "停车位订单审核",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "新闻动态管理",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "车辆求租信息审核",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {
    "title": "车辆出租信息审核",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false
}, {

    "title": "车辆及车位租赁信息",
    "icon": "&#xe61c;",
    "href": "",
    "spread": false,
    "children": [
        {
            "title": "车辆求租信息",
            "icon": "&#xe631;",
            "href": "page/car/car_rent_in.jsp",
            "spread": false
        },
        {
            "title": "车辆出租信息",
            "icon": "&#xe631;",
            "href": "page/car/car_rent_out.jsp",
            "spread": false
        },
        {
            "title": "停车位信息",
            "icon": "&#xe631;",
            "href": "page/car/car_rent_out.jsp",
            "spread": false
        }
    ]
}, {
    "title": "留言管理",
    "icon": "&#xe631;",
    "href": "page/car/car_rent_out.jsp",
    "spread": false
}, {
    "title": "系统基本参数",
    "icon": "&#xe631;",
    "href": "page/car/car_rent_out.jsp",
    "spread": false
}]