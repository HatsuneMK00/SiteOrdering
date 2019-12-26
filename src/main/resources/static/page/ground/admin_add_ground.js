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
    //创建一个编辑器
    // var editIndex = layedit.build('news_content');
    // var addNewsArray = [],addNews;
    // form.on("submit(addNews)",function(data){
    //     var broadcast_title=$(".broadcast_title").val();
    //     var broadcast_content=$(".broadcast_content").val();
    //     $.ajax({
    //         url : baseUrl+"news",
    //         type : "post",
    //         contentType:"application/json; charset=UTF-8",
    //         data:JSON.stringify({title:broadcast_title,content:broadcast_content}),
    //         dataType : "json",
    //         success : function(data){
    //             if(data.status===200){
    //                 top.layer.msg("新闻添加成功！");
    //                 layer.closeAll("iframe");
    //                 parent.location.reload();
    //                 // window.location.reload();
    //             }else{
    //                 alert("新闻添加失败");
    //             }
    //         },
    //         error:function () {
    //             alert("检查一下网络吧");
    //         }
    //     });
    //     return false;
    // })

})
