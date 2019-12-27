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
    //获取场地ID
    // function get_ground_id(){
    //
    //     return ground_id;
    // }

    //跳转到租用场地的专用页面
    $("body").on("click", ".preorder_ground", function () {
        var ground_id = Number($(this).parent().prev().prev().prev().prev().html());
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "预约场馆",
            type : 2,
            content : "preorder.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });
    //跳转到查看评论的专用页面
    $("body").on("click", ".watch_comment", function () {
        var ground_id = Number($(this).parent().prev().prev().prev().prev().html());
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "场馆评论",
            type : 2,
            content : "comment.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });

    //跳转到评论的专用页面
    $("body").on("click", ".make_comment", function () {
        var ground_id = Number($(this).parent().prev().prev().prev().prev().html());
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "对场馆进行评论",
            type : 2,
            content : "make_comment.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
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
                        + '<td id="ground_id_i_need">'+currData[i].groundId+'</td>'
                        + '<td>' + currData[i].pricePerHour + '</td>'
                        + '<td>' + currData[i].address + '</td>'
                        + '<td>' + currData[i].description + '</td>'
                        + '<td>'
                        +  '<a class="layui-btn layui-btn-normal layui-btn-mini preorder_ground" data-id="'+data[i].commentId+'"><i class="layui-icon">&#xe698;</i> 预约</a>'
                        +  '<a class="layui-btn layui-btn-warm layui-btn-mini watch_comment" data-id="'+data[i].commentId+'"><i class="layui-icon">&#xe63a;</i> 查看评论</a>'
                        +  '<a class="layui-btn layui-btn-mini make_comment" data-id="'+data[i].commentId+'"><i class="layui-icon">&#xe642;</i> 发表评论</a>'
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
