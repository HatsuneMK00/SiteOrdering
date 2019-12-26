layui.config({
    base : "../../js/"
}).use(['form','layer','jquery','layedit','upload'],function(){
    var form = layui.form(),
    layer = parent.layer === undefined ? layui.layer : parent.layer,
    laypage = layui.laypage,
    layedit = layui.layedit,
    laydate = layui.laydate,
    $ = layui.jquery;

    var static_url="../../image/upload.jpg";
    $("#photo").attr("src",static_url);

    var this_url="";

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
                        top.layer.msg("场地添加成功！");
                        layer.closeAll("iframe");
                        parent.location.reload();
                    }else{
                        alert("场地添加失败");
                    }
                },
                error:function () {
                    alert("检查一下网络吧");
                }
            });
        }
    );

    //重置表格
    $(".ground_reset").click(function(){
            window.location.reload();
        }
    );

});
