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


    //加载页面数据
    var carsData = '';
    $.get(baseUrl + "carout/passedOrder", function (data) {
        var carsData = data.data;
        //执行加载数据的方法
        newsList(carsData);
    })


    //审核文章
    $(".audit_btn").click(function () {
        var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
        var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
        if ($checkbox.is(":checked")) {
            var index = layer.msg('审核中，请稍候', {icon: 16, time: false, shade: 0.8});
            setTimeout(function () {
                for (var j = 0; j < $checked.length; j++) {
                    for (var i = 0; i < carsData.length; i++) {
                        if (carsData[i].newsId == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")) {
                            //修改列表中的文字
                            $checked.eq(j).parents("tr").find("td:eq(3)").text("审核通过").removeAttr("style");
                            //将选中状态删除
                            $checked.eq(j).parents("tr").find('input[type="checkbox"][name="checked"]').prop("checked", false);
                            form.render();
                        }
                    }
                }
                layer.close(index);
                layer.msg("审核成功");
            }, 2000);
        } else {
            layer.msg("请选择需要审核的文章");
        }
    })


    //是否展示
    form.on('switch(isShow)', function (data) {
        var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
        setTimeout(function () {
            layer.close(index);
            layer.msg("展示状态修改成功！");
        }, 2000);
    })

    //操作
    //租用
    $("body").on("click", ".cars_buy", function () {
        //先通过cookie的username获得用户名
        //然后获得用户id
        //然后carout/oderAccept这个车辆
        var baseUrl = getRootPath_web();
        var car_id = Number($(this).parent().prev().prev().prev().prev().prev().prev().html());
        $.ajax({
            url: baseUrl + "user/getUserByName",
            type: "get",
            dataType: "json",
            data: {username: $.cookie('username')},
            success: function (data) {
                if (data.code === "200") {
                    /*alert("p100");*/
                    var userBean = data.data;
                    var userId = userBean.id;

                    /*alert("当前用户id:" + userId);
                    alert(typeof userId);*/
                    $.ajax({
                        url: baseUrl + "carout/orderAccept",
                        type: "get",
                        dataType: "json",
                        data: {id: car_id, receiver_id: userId},
                        success: function (data) {
                            /*alert("p1");
                            alert(data.code);*/
                            if (data.code === "200") {
                                alert("您已成功租用ID为" +car_id+ "的汽车。在‘租入订单’-‘租入的车’页面中刷新就可以看到");
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
                } else {
                    alert("database error2");
                }
            },
            error: function () {
                alert("database error3");
            }
        });
    })

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
                        +'<td>'+currData[i].id+'</td>'
                        +'<td>'+currData[i].carName+'</td>'
                        + '<td align="left" class="car_id">' + currData[i].car_id + '</td>'
                        + '<td >' + currData[i].sender_id + '</td>';
                    dataHtml += '<td>' + currData[i].status + '</td>'+'<td>'+currData[i].senderName+'</td>';
                    dataHtml +=
                        '<td>'
                        + '<a class="layui-btn layui-btn-mini cars_buy"><i class="iconfont icon-edit"></i> 租用</a>'
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
