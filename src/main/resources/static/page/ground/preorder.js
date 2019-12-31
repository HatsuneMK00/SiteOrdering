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
    $.ajax({
        url: baseUrl + "ground/" + $.cookie('groundId'),
        type: "get",
        dataType: "json",
        data: {groundId:$.cookie('groundId')},
        async:false,
        success : function(data){
            if(data.status===200){
                $('.groundName').val(data.responseMap.result.groundName);
            }else{
                layer.msg("没有这个场地");
            }
        },
        error:function () {
            layer.msg("检查一下网络吧");
        }
    });
    //提交场地预约信息
    $(".preorder_add").click(function(){
            //忽略秒和分
            var user_num=$("input:text[name='userNum']").val();
            var dates=$("input:text[name='date']").val().split("~");
            for(var i=0;i<2;i++){
                dates[i]=new Date(dates[i]);
                dates[i].setMinutes(0);
                dates[i].setSeconds(0);
            }

            var duration = Math.ceil((dates[1] - dates[0])/3600000);

            if(duration<0){
                layer.msg("时间范围异常");
                return;
            }

            console.log(dates[0].toString()+":持续"+duration);

            occupy(dates[0],duration,$.cookie('groundId'),user_num);
        }
    );


    //占用场地的时间和人数
    function occupy(t,d,gid,n){
        var tt=fromDateToString(t);
        $.ajax({
            url: baseUrl + "order/user/"+user_id+"/order",
            type: "post",
            dataType: "json",
            data: {groundId:gid,startTime:tt,duration:d,userNum:n},
            async:false,
            success : function(data){
                if(data.status===200){
                    top.layer.msg("场地预约成功！");
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg("场地预约失败");
                }
            },
            error:function () {
                layer.msg("检查一下网络吧");
            }
        });
    }


    //重置表格
    $(".preorder_reset").click(function(){
            window.location.reload();
        }
    );
});