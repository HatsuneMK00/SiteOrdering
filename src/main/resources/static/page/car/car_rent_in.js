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

    var baseUrl = getRootPath_web();

    //加载页面数据——所有的场地信息
    $.ajax({
        url: baseUrl + "ground",
        type: "get",
        dataType: "json",
        success: function (data) {
            if(data.status === 200){
                newsList(data.responseMap.result);
            }
            else{
                alert("获取用户信息失败");
                $(window).attr('location', baseUrl);
            }

        },
        error: function (data) {
            alert("检查一下网络吧");
            $(window).attr('location', baseUrl);
        }
    });


    // 审核文章
    // $(".audit_btn").click(function () {
    //     var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
    //     var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
    //     if ($checkbox.is(":checked")) {
    //         var index = layer.msg('审核中，请稍候', {icon: 16, time: false, shade: 0.8});
    //         setTimeout(function () {
    //             for (var j = 0; j < $checked.length; j++) {
    //                 for (var i = 0; i < carsData.length; i++) {
    //                     if (carsData[i].newsId == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")) {
    //                         //修改列表中的文字
    //                         $checked.eq(j).parents("tr").find("td:eq(3)").text("审核通过").removeAttr("style");
    //                         //将选中状态删除
    //                         $checked.eq(j).parents("tr").find('input[type="checkbox"][name="checked"]').prop("checked", false);
    //                         form.render();
    //                     }
    //                 }
    //             }
    //             layer.close(index);
    //             layer.msg("审核成功");
    //         }, 2000);
    //     } else {
    //         layer.msg("请选择需要审核的文章");
    //     }
    // });
    //
    //
    // 是否展示
    // form.on('switch(isShow)', function (data) {
    //     var index = layer.msg('修改中，请稍候', {icon: 16, time: false, shade: 0.8});
    //     setTimeout(function () {
    //         layer.close(index);
    //         layer.msg("展示状态修改成功！");
    //     }, 2000);
    // });
    //


    // 操作
    // 租用场地
    $("body").on("click", ".preorder_ground", function () {
        //先通过cookie的username获得用户名
        //然后获得用户id
        var ground_id = Number($(this).parent().prev().prev().prev().prev().html());
        $.ajax({
            url: baseUrl + "user/getByName",
            type: "get",
            dataType: "json",
            data: {userName: $.cookie('userName')},
            success: function (data) {
                if (data.status === 200) {
                    /*alert("p100");*/
                    var user_id = data.responmseMap.result.userId;

                    /*alert("当前用户id:" + userId);
                    alert(typeof userId);*/
                    $.ajax({
                        url: baseUrl + "order/user/"+ userId +"/order",
                        type: "post",
                        dataType: "json",
                        data: {groundId: ground_id, userId: user_id},
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
    });

    //渲染数据函数
    function newsList(that) {
        function renderDate(data, curr) {
            var dataHtml = '';
            var currData = data.concat().splice(curr * nums - nums, nums);
            if (currData.length !== 0) {
                for (var i = 0; i < currData.length; i++) {
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td>'+currData[i].groundName+'</td>'
                        + '<td>'+currData[i].groundId+'</td>'
                        + '<td>' + currData[i].pricePerHour + '</td>'
                        + '<td>' + currData[i].address + '</td>'
                        + '<td>' + currData[i].description + '</td>'
                        + '<td>'
                        +       '<a class="layui-btn layui-btn-mini preorder_ground"><i class="iconfont icon-edit"></i> 预约</a>'
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
        laypage({
            cont: "page",
            pages: Math.ceil(that.length / nums),
            jump: function (obj) {
                $(".cars_content").html(renderDate(that, obj.curr));
                $('.cars_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }
});
