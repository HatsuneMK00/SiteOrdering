layui.config({
    base: "js/"
}).use(['form', 'layer', 'jquery', 'laypage'], function () {
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();

    $.ajax({
        url: baseUrl + "user/getByName",
        type: "get",
        dataType: "json",
        data: {userName: $.cookie('userName')},
        success: function (data) {
            if(data.status === 200){
                $('.userName').val($.cookie('userName'));
                var user_id = data.responseMap.result.userId;
                // var ground_id = parent.get_ground_id();
                $('.groundId').val($.cookie('groundId'));
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
});