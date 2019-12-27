layui.config({
    base : "../../js/"
}).use(['form','layer','jquery','layedit','upload'],function(){
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        layedit = layui.layedit,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();
    $('.userName').val($.cookie('userName'));
    $('.groundId').val($.cookie('groundId'));
    var user_id = parseInt($.cookie('userId'));
    var ground_id = parseInt($.cookie('groundId'));
    $.ajax({
        url: baseUrl + "ground/" + $.cookie('groundId'),
        type: "get",
        dataType: "json",
        data: {groundId:$.cookie('groundId')},
        async:false,
        success : function(data){
            if(data.status===200){
                var ground_name = data.responseMap.result.groundName;
                $('.groundName').val(ground_name);
            }else{
                layer.msg("没有这个场馆");
            }
        },
        error:function () {
            layer.msg("检查一下网络吧");
        }
    });
    //提交场地的评论
    $(".comment_add").click(function() {
        var comment = $('.makeComment').val();
        $.ajax({
            url: baseUrl + "comment",
            type: "post",
            dataType: "json",
            contentType : 'application/json;charset=UTF-8',
            data: JSON.stringify({userId: user_id,groundId:ground_id,content: comment}),
            async:false,
            success : function(data){
                if(data.status===200){
                    layer.msg("评论成功");
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg("评论失败");
                }
            },
            error:function () {
                layer.msg("检查一下网络吧");
            }
        });

    });
    //重置表格
    $(".comment_reset").click(function(){
            window.location.reload();
    });
});