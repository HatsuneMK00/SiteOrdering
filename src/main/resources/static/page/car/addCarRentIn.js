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

    //获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
    var curWwwPath = window.document.location.href;
    //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
    var pathName = window.document.location.pathname;
    var pos;
    if(pathName.length>1){pos = curWwwPath.indexOf(pathName);}
    else{pos = -1;}
    //获取主机地址，如： http://localhost:8083
    var localhostPath;
    if(pos>0){localhostPath = curWwwPath.substring(0, pos)+"/";}
    else{localhostPath=curWwwPath;}
    var baseUrl =localhostPath;

    //创建一个编辑器
    var editIndex = layedit.build('news_content');
    var addNewsArray = [],addNews;
    form.on("submit(addNews)",function(data){
        // //是否添加过信息
        // if(window.sessionStorage.getItem("addNews")){
        //     addNewsArray = JSON.parse(window.sessionStorage.getItem("addNews"));
        // }
        // //显示、审核状态
        // var isShow = data.field.show=="on" ? "checked" : "",
        //     newsStatus = data.field.shenhe=="on" ? "审核通过" : "待审核";
        //
        // addNews = '{"newsName":"'+$(".newsName").val()+'",';  //文章名称
        // addNews += '"newsId":"'+new Date().getTime()+'",';	 //文章id
        // addNews += '"newsLook":"'+$(".newsLook option").eq($(".newsLook").val()).text()+'",'; //开放浏览
        // addNews += '"newsTime":"'+$(".newsTime").val()+'",'; //发布时间
        // addNews += '"newsAuthor":"'+$(".newsAuthor").val()+'",'; //文章作者
        // addNews += '"isShow":"'+ isShow +'",';  //是否展示
        // addNews += '"newsStatus":"'+ newsStatus +'"}'; //审核状态
        // addNewsArray.unshift(JSON.parse(addNews));
        // window.sessionStorage.setItem("addNews",JSON.stringify(addNewsArray));
        // //弹出loading
        // var index = top.layer.msg('数据提交中，请稍候',{icon: 16,time:false,shade:0.8});

        var title=$(".carTitle").val();
        var user=$(".carUser").val();
        var date=$(".carTime").val();
        var pay=$(".carCost").val();
        var status="待审核";
        $.ajax({
            url : baseUrl+"car/addCarRentIn",
            type : "get",
            data:{title:title,user:user,date:date,pay:pay,status:status },
            dataType : "json",
            success : function(data){
                linksData = data.data;
                //alert(data);
                if(linksData==1){
                    top.layer.msg("租车信息添加成功！");
                }else{
                    alert("租车信息添加失败");
                }
            },
            error:function () {
                alert("租车信息添加失败>_<");
            }
        })
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
