layui.config({
    base : "../../js/"
}).use(['form','layer','jquery','laypage'],function(){
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    var baseUrl = getRootPath_web();
    //加载页面数据
    var newsData = [];
    refreshList();

    // //查询
    // $(".search_btn").click(function(){
    //     var newArray1 = [];
    //     if($(".search_input").val() !== ''){
    //         var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
    //         var matchStr = $(".search_input").val();
    //         setTimeout(function(){
    //             $.ajax({
    //                 url :  baseUrl+"comment/match",
    //                 type : "post",
    //                 dataType : "json",
    //                 contentType : 'application/json; charset=UTF-8',
    //                 data: JSON.stringify({match:matchStr, groundId: parseInt($.cookie('groundId'))}),
    //                 async:false,
    //                 success : function(data){
    //                     if(data.status===200){
    //                         newsData=data.responseMap.result;
    //                         newsList(newsData);
    //                     }
    //                     else if(data.status===500){
    //                         layer.msg("错误的请求");
    //                     }
    //                     else{
    //                         layer.msg("无结果");
    //                         newsData = data.responseMap.result;
    //                         newsList(newsData);
    //                     }
    //
    //                 },
    //                 error: function () {
    //                     layer.msg("检查一下网络吧");
    //                     newsData=[];
    //                     newsList([]);
    //                 }
    //
    //             });
    //             layer.close(index);
    //         },1000);
    //     }else{
    //         refreshList();
    //     }
    // });



    function newsList(that){
        //渲染数据
        function renderDate(data,curr){
            var dataHtml = '';
            var currData;
            currData = that.concat().splice(curr*nums-nums, nums);
            if(currData.length !== 0){
                for(var i=0;i<currData.length;i++){
                    var groundName = (currData[i].groundName==null)?'':currData[i].groundName;
                    var userName = (currData[i].userName==null)?'':currData[i].userName;
                    var orderTime=new Date(currData[i].date);
                    dataHtml += '<tr>'
                        +'<td>'+currData[i].commentId+'</td>'
                        +'<td>'+currData[i].content+'</td>'
                        +'<td>'+currData[i].groundId+'</td>'
                        +'<td>'+groundName+'</td>'
                        +'<td>'+currData[i].userId+'</td>'
                        +'<td>'+userName+'</td>'
                        // 时间格式化
                        +  '<td>'+fromDateToChineseString(orderTime)+'</td>'
                    ;
                }
            }else{
                dataHtml = '<tr><td colspan="7">暂时没有评论</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 7; //每页出现的数据量
        laypage({
            cont : "page",
            pages : Math.ceil(that.length/nums),
            jump : function(obj){
                $(".comment_content").html(renderDate(that,obj.curr));
                $('.comment_list thead input[type="checkbox"]').prop("checked",false);
                form.render();
            }
        })
    }

    function refreshList(){
        $.ajax({
            url: baseUrl + "checkedcomment/ground/" + $.cookie('groundId'),
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status === 200) {
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
})
