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
    var ground_id = parseInt($.cookie('groundId'));
    if(ground_id==null)
    {
        layer.msg("不存在此场地");
    }

    var baseUrl = getRootPath_web();

    $.ajax({
        url: baseUrl + "ground/" + ground_id,
        type: "get",
        dataType: "json",
        data: {groundId:$.cookie('groundId')},
        async:false,
        success : function(data){
            if(data.status===200){
                $('.groundName').val(data.responseMap.result.groundName);
                $('.description').val(data.responseMap.result.description);
                $('.price').val(data.responseMap.result.description);
                $('.address').val(data.responseMap.result.description);
                $('.description').val(data.responseMap.result.description);
                if(data.responseMap.result.photo!=null && data.responseMap.result.photo!=""){
                    $("#photo").attr("src",data.responseMap.result.photo);
                }
            }else{
                layer.msg("没有这个场地");
            }
        },
        error:function () {
            layer.msg("检查一下网络吧");
        }
    });



});
