/*
 * 用户界面
 * 存储用户界面的左侧导航栏的结构
 * author 陈杰
 */
/*这是全局的变量*/
var navs = [{
    "title": "个人信息管理",
    "icon": "icon-computer",
    "href": "page/user/userInfo.html",
    "spread": false
}, {
    "title": "租入订单",
    "icon": "icon-computer",
    "href": "page/main.html",
    "spread": false,
    "children": [
        {
            "title": "租入的车",
            "icon": "icon-computer",
            "href": "page/car/my_rent_in_cars.jsp",
            "spread": false,
        }, {
            "title": "租入的车位",
            "icon": "icon-computer",
            "href": "page/parking/my_rent_in_parking_lot.jsp",
            "spread": false,
        }
    ]
}, {
    "title": "租出订单",
    "icon": "icon-computer",
    "href": "",
    "spread": false,
    "children": [
        {
            "title": "租出订单",
            "icon": "icon-computer",
            "href": "page/car/my_rent_out_car_list.jsp",
            "spread": false,
        }
    ]
}, {
    "title": "发布车辆出租信息",
    "icon": "icon-computer",
    "href": "page/car/car_rent_out.html",
    "spread": false
}, {
    "title": "我要租车",
    "icon": "icon-computer",
    "href": 'page/car/car_rent_in.jsp',
    "spread": false
}, {
    "title": "我要租车位",
    "icon": "icon-computer",
    "href": "page/parking/parkingLot.jsp",
    "spread": false
}, {
    "title": "留言",
    "icon": "icon-computer",
    "href": "page/message/message.html",
    "spread": false
}, {
    "title": "来自管理员的公告",
    "icon": "icon-computer",
    "href": "page/message/user_broadcast.jsp",
    "spread": false
}]