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

    var baseUrl = getRootPath_web();

    var user_id = 0;
    //加载页面数据——所有该用户的预约信息
    $.ajax({
        url: baseUrl + "user/getByName",
        type: "get",
        dataType: "json",
        data: {userName: $.cookie('userName')},
        success: function (data) {
            if (data.status === 200) {
                user_id = data.responseMap.result.userId;
                //加载页面数据
                $.ajax({
                    url: baseUrl + "order/user/" + user_id + "/preOrder",
                    type: "get",
                    dataType: "json",
                    data: {userId: user_id},
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
            }
            else {
                alert("用户不存在");
                $(window).attr('location', baseUrl);
            }
        },
        error: function () {
            alert("检查一下网络吧");
            $(window).attr('location', baseUrl);
        }
    });

    //操作
    //还车
    // $("body").on("click", ".cars_return", function () {
    //     //已经获得了用户id，然后获得car_id
    //     //然后carout/orderReturn归还这辆车
    //     var car_id = Number($(this).parent().prev().prev().prev().prev().prev().prev().html());
    //     $.ajax({
    //         url: baseUrl + "carout/orderReturn",
    //         type: "get",
    //         dataType: "json",
    //         data: {id: car_id, receiver_id: user_id},
    //         success: function (data) {
    //             /*alert("p1");
    //             alert(data.code);*/
    //             if (data.code === "200") {
    //                 alert("您已成功归还ID为" + car_id + "的汽车");
    //                 //刷新页面
    //                 window.location.reload()
    //             } else {
    //                 /*alert("else clause");*/
    //                 alert("database error");
    //             }
    //         },
    //         error: function () {
    //             /*alert("p2");*/
    //             alert("database error1");
    //         }
    //
    //     });
    // });


    //渲染数据函数
    function newsList(that) {

        function renderDate(data, curr) {
            var dataHtml = '';
            var currData = data.concat().splice(curr * nums - nums, nums);;
            if (currData.length !== 0) {
                for (var i = 0; i < currData.length; i++) {
                    dataHtml += '<tr>'
                        + '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        + '<td >' + currData[i].preOrderId + '</td>'
                        + '<td >' + currData[i].groundId + '</td>'
                        + '<td >' + currData[i].userId + '</td>'
                        + '<td >' + currData[i].orderTime + '</td>'
                        + '<td >' + currData[i].price + '</td>'
                        + '<td >' + currData[i].startTime + '</td>'
                        + '<td >' + currData[i].duration + '</td>'
                        + '<td >' + currData[i].checked + '</td>'
                        + '</tr>';
                }
            } else {
                dataHtml = '<tr><td colspan="9">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 9; //每页出现的数据量
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
})
