layui.config({
    base : "../../js/"
}).use(['form','layer','jquery','layedit','upload'],function(){
    var form = layui.form(),
    layer = parent.layer === undefined ? layui.layer : parent.layer,
    laypage = layui.laypage,
    layedit = layui.layedit,
    $ = layui.jquery;

    var static_url="../../image/upload.jpg";
    $("#photo").attr("src",static_url);

    var this_url="";
    var userId = parseInt($.cookie('userId'));
    if(isNaN(userId)){
        layer.msg("请登陆");
        layer.closeAll("iframe");
    }


    var baseUrl = getRootPath_web();
    layui.upload({
        mehtod:"post",
        url : baseUrl+'file/uploadImage',
        success: function(res){
            this_url=res.responseMap.url;
            $("#photo").attr("src",this_url);
        }
    });

    //提交一个场地
    $(".ground_add").click(function(){
            //忽略秒和分
            var dates=$("input:text[name='date']").val().split("~");
            for(var i=0;i<2;i++){
                dates[i]=new Date(dates[i]);
                dates[i].setMinutes(0);
                dates[i].setSeconds(0);
            }
            var date1=new Date(Date.now());
            date1=new Date(date1);
            date1.setHours(0);
            date1.setMinutes(0);
            date1.setSeconds(0);

            var duration1 = Math.ceil((dates[0] - date1.getTime())/3600000);

            if(duration1<0){
                layer.msg("时间范围异常");
                return;
            }
            var date2=dates[1]; var duration2 = 876000;

            console.log(date1.toString()+":持续"+duration1);
            console.log(date2.toString()+":持续"+duration2);

            var name = $(".groundName").val();
            var add = $(".address").val();
            var pri = parseInt($(".price").val());
            var des = $(".description").val();

            var jsonData=JSON.stringify({groundName: name ,address: add,pricePerHour:pri ,description: des, photo:this_url});

            $.ajax({
                url: baseUrl + "groundWOFileOperation",
                type: "post",
                dataType: "json",
                contentType: 'application/json;charset=UTF-8',
                data: jsonData,
                async:false,
                success : function(data){
                    if(data.status===200){
                        occupy(date1,duration1,data.responseMap.result.groundId);
                        occupy(date2,duration2,data.responseMap.result.groundId);
                        top.layer.msg("场地添加成功！");
                        layer.closeAll("iframe");
                        parent.location.reload();
                    }else{
                        layer.msg("场地添加失败");
                    }
                },
                error:function () {
                    layer.msg("检查一下网络吧");
                }
            });
        }
    );


    //占用场地的时间
    function occupy(t,d,gid){
        var tt=fromDateToString(t);
        $.ajax({
            url: baseUrl + "order/admin/"+userId+"/order",
            type: "post",
            dataType: "json",
            data: {groundId:gid,startTime:tt,duration:d},
            async:false,
            success : function(data){
                if(data.status===200){
                    top.layer.msg("场地添加成功！");
                    layer.closeAll("iframe");
                    parent.location.reload();
                }else{
                    layer.msg("场地添加失败");
                }
            },
            error:function () {
                layer.msg("检查一下网络吧");
            }
        });
    }

    //重置表格
    $(".ground_reset").click(function(){
            window.location.reload();
        }
    );

});
