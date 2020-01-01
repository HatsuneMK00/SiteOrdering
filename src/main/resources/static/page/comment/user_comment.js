layui.config({
    base: "js/"
}).use(['form', 'layer', 'jquery', 'laypage'], function () {
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();
    //加载页面数据
    var newsData = [];
    refreshList();

    var user_id = 0;

    $("body").on("click",".refresh",function(){
        refreshList();
    });
    //删除
    $("body").on("click",".comment_del",function(){
        var _this = $(this);
        layer.confirm('删除此评论？',{icon:3, title:'提示信息'},function(index){
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].commentId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'comment/'+newsData[i].commentId,
                        type : "delete",
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                newsData.splice(i,1);
                                layer.msg("删除成功");
                                newsList(newsData);

                            }else{
                                layer.msg("删除失败");
                                newsList(newsData);
                            }
                        },
                        error:function () {
                            layer.msg("检查一下网络吧");
                            window.location.reload();
                        }
                    });
                    newsList(newsData);
                }
            }

            layer.close(index);
        });
    });


    //渲染数据函数
    function newsList(that) {

        function renderDate(data, curr) {
            var dataHtml = '';
            var currData = data.concat().splice(curr * nums - nums, nums);
            if (currData.length !== 0) {
                for (var i = 0; i < currData.length; i++) {
                    var check = "未通过审核";
                    if(currData[i].checked === 0)
                    {
                        check = "正在审核";
                    }
                    else if (currData[i].checked === 1){
                        check = "通过审核";
                    }
                    var t="";
                    if(currData[i].date!=null && currData[i].date!=""){
                        t=fromDateToChineseString(new Date(currData[i].date));
                    }

                    dataHtml += '<tr>'
                        + '<td >' + currData[i].userId + '</td>'
                        + '<td >' + currData[i].commentId + '</td>'
                        + '<td >' + currData[i].groundId + '</td>'
                        + '<td >' + currData[i].groundName + '</td>'
                        + '<td >' + t + '</td>'
                        + '<td >' + currData[i].content + '</td>'
                        + '<td >' + check + '</td>'
                        + '<td>'
                        + '<a class="layui-btn layui-btn-danger layui-btn-mini comment_del" data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                        + '</td>'
                        + '</tr>';
                }
            } else {
                dataHtml = '<tr><td colspan="10">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 10; //每页出现的数据量
        laypage({
            cont: "page",
            pages: Math.ceil(that.length / nums),
            jump: function (obj) {
                $(".my_comments_content").html(renderDate(that, obj.curr));
                $('.my_comments_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }

    //加载页面数据——所有该用户的预约信息
    function refreshList(){
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
                        url: baseUrl + "comment/user/" + user_id,
                        type: "get",
                        dataType: "json",
                        data: {userId: user_id},
                        success: function (data) {
                            if(data.status === 200){
                                layer.msg("获取评论");
                                newsData=data.responseMap.result;
                                newsList(newsData);
                            }
                            else{
                                layer.msg("获取评论失败");
                                newsList([]);
                            }

                        },
                        error: function () {
                            layer.msg("检查一下网络吧");
                            newsList([]);
                        }
                    });
                }
                else {
                    layer.msg("用户不存在");
                    newsList([]);
                }
            },
            error: function () {
                layer.msg("检查一下网络吧");
                newsList([]);
            }
        });
    }
});
