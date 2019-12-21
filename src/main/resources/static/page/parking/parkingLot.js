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
    var parkingLotData = '';
    $.get(baseUrl + "park/parkIdleTraversal", function (data) {
        var parkingLotData = data.data;
        //执行加载数据的方法
        newsList(parkingLotData);
    })


    //审核文章
    $(".audit_btn").click(function () {
        var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
        var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
        if ($checkbox.is(":checked")) {
            var index = layer.msg('审核中，请稍候', {icon: 16, time: false, shade: 0.8});
            setTimeout(function () {
                for (var j = 0; j < $checked.length; j++) {
                    for (var i = 0; i < parkingLotData.length; i++) {
                        if (parkingLotData[i].newsId == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")) {
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
    $("body").on("click", ".parkingLot_buy", function () {
        //先通过cookie的username获得用户名
        //然后获得用户id
        //然后获取当前的停车位的id
        //然后parkOrder/parkOrderAdd，让这个人租用这个停车位
        var baseUrl = getRootPath_web();
        var parkingLotId = Number($(this).parent().prev().prev().prev().html());
        $.ajax({
            url: baseUrl + "user/getUserByName",
            type: "get",
            dataType: "json",
            data: {username: $.cookie('username')},
            success: function (data) {
                if (data.code === "200") {
                    var userBean = data.data;
                    var userId = userBean.id;
                    $.ajax({
                        url: baseUrl + "parkOrder/parkOrderAdd",
                        type: "get",
                        dataType: "json",
                        data: {userid: userId, parkid: parkingLotId},
                        success: function (data) {
                            if (data.code === "200") {
                                alert("您已经成功租用了ID为"+parkingLotId+"的停车位");
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
                currData = parkingLotData.concat().splice(curr * nums - nums, nums);
            } else {
                currData = that.concat().splice(curr * nums - nums, nums);
            }
            if (currData.length != 0) {
                for (var i = 0; i < currData.length; i++) {
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td align="left" class="parkingLotId">' + currData[i].id + '</td>'
                        + '<td >' + currData[i].name + '</td>';
                    dataHtml += '<td>' + currData[i].price_per_day + '</td>';
                    dataHtml +=
                        '<td>'
                        + '<a class="layui-btn layui-btn-mini parkingLot_buy"><i class="iconfont icon-edit"></i> 租用</a>'
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
