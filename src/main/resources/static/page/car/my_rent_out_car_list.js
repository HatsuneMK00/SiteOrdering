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
        async:false,
        success: function (data) {
            if (data.code === "200") {
                var userBean = data.data;
                user_id = userBean.id;
                //加载页面数据
                var carsData = '';
                $.get(baseUrl + "carout/myUpOrder?myId=" + user_id, function (data) {
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
    //删除
    $("body").on("click", ".cars_delete", function () {
        //首先要获得订单id，
        //然后carout/orderDelete来删除这个订单。但是车辆信息会一直保存在数据库里面
        var order_id = Number($(this).parent().prev().prev().prev().html());
        var baseUrl = getRootPath_web();

        $.ajax({
            url: baseUrl + "carout/orderDelete",
            type: "get",
            dataType: "json",
            data: {id: order_id},
            success: function (data) {
                if (data.code === "200") {
                    alert("您已成功删除订单ID为" + order_id + "的汽车订单");
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
                        + '<td align="left">' + currData[i].car_id + '</td>'/*车辆ID*/
                    + '<td align="left">' + currData[i].carName + '</td>';

                    dataHtml +='<td>'+currData[i].id+'</td>';/*订单ID*/

                    if(currData[i].status==2){
                        dataHtml+= '<td >被ID为'+currData[i].receiver_id+' 的用户：' + currData[i].receiverName + ' 租用了</td>';
                    } else {
                        dataHtml+='<td><i>还没有被任何用户租用</i></td>';
                    }

                    /*dataHtml += '<td>' + currData[i].status + '</td>';*/
                    if(currData[i].status==0){
                        dataHtml+='<td>等待管理员审核</td>';
                    } else if(currData[i].status==1){
                        dataHtml+='<td>已经通过管理员审核,等待被租用</td>';
                    } else if(currData[i].status==2){
                        dataHtml+='<td>已经被其他用户租用，等待归还</td>';
                    } else{
                        dataHtml+='<td>审核失败</td>';
                    }

                    if(currData[i].status==2){
                        dataHtml+=   '<td>'
                            + '<a class="layui-btn layui-btn-mini"> 删除</a>'
                            + '</td>'
                            + '</tr>';
                    } else{
                        dataHtml +=
                            '<td>'
                            + '<a class="layui-btn layui-btn-mini cars_delete"><i class="iconfont icon-edit"></i> 删除</a>'
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
