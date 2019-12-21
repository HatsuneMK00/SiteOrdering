/**
 * Created by 蛟川小盆友 on 2017/12/6.
 */
layui.config({
    base : "js/"
}).use(['form','layer','jquery','layedit','laydate'],function(){
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        layedit = layui.layedit,
        laydate = layui.laydate,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();
   // alert(baseUrl);
    //创建一个编辑器
    var editIndex = layedit.build('news_content');
    var addNewsArray = [],addNews;
    form.on("submit(addNews)",function(data){
        var title=$(".carTitle").val();

        var pay=$(".carCost").val();
        var status="待审核";
        $.ajax({
            url : baseUrl+"park/parkAdd",
            type : "get",
            data:{name:title,price:pay },
            dataType : "json",
            success : function(data){
                linksData = data.data;
                //alert(data);
                if(data.code=="200"){
                    top.layer.msg("停车信息添加成功！");
                }else{
                    alert("停车信息添加失败");
                }
            },
            error:function () {
                alert("停车信息添加失败>_<检查一下网络吧");
            }
        });
        setTimeout(function(){
            top.layer.close(index);
            //top.layer.msg("文章添加成功！");
            layer.closeAll("iframe");
            //刷新父页面
            parent.location.reload();
        },2000);
        return false;
    })

})
