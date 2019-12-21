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
                $.get(baseUrl + "parkOrder/getParkOrderByUserid?userid=" + user_id, function (data) {
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
    //归还
    $("body").on("click", ".parkingLot_return", function () {
        //先通过cookie的username获得用户名
        //然后获得用户id
        //然后获取当前的停车位租用订单的订单号parkingLotOrderId
        //然后parkOrder/parkOrderFinish，让这个人租用这个停车位
        var baseUrl = getRootPath_web();
        var parkingLotOrderId = Number($(this).parent().prev().prev().html());

        $.ajax({
            url: baseUrl + "parkOrder/parkOrderFinish",
            type: "get",
            dataType: "json",
            data: {id:parkingLotOrderId},
            success: function (data) {
                if (data.code === "200") {
                    alert("您已经成功归还了ID为" + parkingLotOrderId + "的停车位");
                    //刷新页面
                    window.location.reload();
                } else {
                    alert("database error0");
                }
            },
            error: function () {
                alert("database error1");
            }

        });

    });

    function newsList(that) {
        //渲染数据
        function renderDate(data, curr) {
            var dataHtml = '';
            if (!that) {
                currData = parkingLotData.concat().splice(curr * nums - nums, nums);
            } else {
                currData = that.concat().splice(curr * nums - nums, nums);
            }
            if (currData.length != 0) {
                for (var i = 0; i < currData.length; i++) {
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td align="left" class="parkingLotOrderId">' + currData[i].parkid + '</td>'
                        + '<td align="left">' + currData[i].parkName + '</td>'
                        + '<td >' + currData[i].startdate + '</td>';
                    dataHtml += '<td>' + currData[i].id + '</td>';
                    if(currData[i].status===0){
                        dataHtml+='<td> 正在使用中</td>';
                    }
                    else {
                        dataHtml+='<td>已归还</td>';
                    }
                    if(currData[i].status===0){
                        dataHtml +=
                            '<td>'
                            + '<a class="layui-btn layui-btn-mini parkingLot_return"><i class="iconfont icon-edit"></i> 归还</a>'
                            + '</td>'
                            + '</tr>';
                    }else{//如果已经归还了，那么这个按钮就不能再按了
                        dataHtml +=
                            '<td>'
                            + '<a class="layui-btn layui-btn-mini" disabled="true"> 归还</a>'
                            + '</td>'
                            + '</tr>';
                    }
                }
            } else {
                dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 8; //每页出现的数据量
        if (that) {
            parkingLotData = that;
        }
        laypage({
            cont: "page",
            pages: Math.ceil(parkingLotData.length / nums),
            jump: function (obj) {
                $(".parkingLot_content").html(renderDate(parkingLotData, obj.curr));
                $('.parkingLot_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }
})
