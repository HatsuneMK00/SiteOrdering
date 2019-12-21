/*用来显示我已经租进来的车子，并且具有还车的功能*/
/**
 * Created by 蛟川小盆友 on 2017/12/6.
 */
layui.config({
    base: "js/"
}).use(['form', 'layer', 'jquery', 'laypage'], function () {
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos;
    if (pathName.length > 1) {
        pos = curWwwPath.indexOf(pathName);
    }
    else {
        pos = -1;
    }
    //获取主机地址，如： http://localhost:8083
    var localhostPath;
    if (pos > 0) {
        localhostPath = curWwwPath.substring(0, pos) + "/";
    }
    else {
        localhostPath = curWwwPath;
    }
    var baseUrl = localhostPath;

    var user_id = 0;
    $.ajax({
        /*这种方法貌似是异步的。user_id不能立刻地就带到下一个$.ajax()里面去*/
        url: baseUrl + "user/getUserByName",
        type: "get",
        dataType: "json",
        data: {username: $.cookie('username')},
        success: function (data) {
            if (data.code === "200") {
                var userBean = data.data;
                user_id = userBean.id;
                //加载页面数据
                var carsData = '';
                $.get(baseUrl + "carout/myInOrder?receiverId=" + user_id, function (data) {
                    var carsData = data.data;
                    //执行加载数据的方法
                    newsList(carsData);
                })
            }
            else {
                alert("DataBase Error. 102");
            }
        },
        error: function () {
            alert("Database error . 101");
        }
    });

    //操作
    //还车
    $("body").on("click", ".cars_return", function () {
        //已经获得了用户id，然后获得car_id
        //然后carout/orderReturn归还这辆车
        var car_id = Number($(this).parent().prev().prev().prev().prev().prev().prev().html());
        $.ajax({
            url: baseUrl + "carout/orderReturn",
            type: "get",
            dataType: "json",
            data: {id: car_id, receiver_id: user_id},
            success: function (data) {
                /*alert("p1");
                alert(data.code);*/
                if (data.code === "200") {
                    alert("您已成功归还ID为" + car_id + "的汽车");
                    //刷新页面
                    window.location.reload()
                } else {
                    /*alert("else clause");*/
                    alert("database error");
                }
            },
            error: function () {
                /*alert("p2");*/
                alert("database error1");
            }

        });
    });

    function newsList(that) {
        //渲染数据
        function renderDate(data, curr) {
            var dataHtml = '';
            if (!that) {
                currData = carsData.concat().splice(curr * nums - nums, nums);
            } else {
                currData = that.concat().splice(curr * nums - nums, nums);
            }
            if (currData.length != 0) {
                for (var i = 0; i < currData.length; i++) {
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td align="left" class="car_id">' + currData[i].id + '</td>'
                        + '<td align="left" class="car_id">' + currData[i].car_id + '</td>'
                        + '<td align="left" class="car_id">' + currData[i].carName + '</td>'
                        + '<td >' + currData[i].sender_id + '</td>'
                        + '<td >' + currData[i].senderName + '</td>';
                    dataHtml += '<td>' + currData[i].status + '</td>';
                    dataHtml +=
                        '<td>'
                        + '<a class="layui-btn layui-btn-mini cars_return"><i class="iconfont icon-edit"></i> 还车</a>'
                        + '</td>'
                        + '</tr>';
                }
            } else {
                dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 8; //每页出现的数据量
        if (that) {
            carsData = that;
        }
        laypage({
            cont: "page",
            pages: Math.ceil(carsData.length / nums),
            jump: function (obj) {
                $(".cars_content").html(renderDate(carsData, obj.curr));
                $('.cars_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }
})
