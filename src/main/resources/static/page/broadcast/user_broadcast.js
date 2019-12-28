layui.config({
    base : "../../js/"
}).use(['form','layer','jquery','laypage'],function(){
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;

    //加载页面数据
    var newsData = '';
    var baseUrl = getRootPath_web();
    refreshNewsLists();

    //查询
    var filteredNewsData=[];
    $(".search_btn").click(function(){
        filteredNewsData = [];
        if($(".search_input").val() !== ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                for(var i=0;i<newsData.length;i++){
                    var str = JSON.parse(JSON.stringify(newsData[i]));
                    var selectStr = $(".search_input").val();
                    function changeStr(data){
                        var dataStr = '';
                        var showNum = data.split(eval("/"+selectStr+"/ig")).length - 1;
                        if(showNum > 1){
                            for (var j=0;j<showNum;j++) {
                                dataStr += data.split(eval("/"+selectStr+"/ig"))[j] + "<i style='color:#03c339;font-weight:bold;'>" + selectStr + "</i>";
                            }
                            dataStr += data.split(eval("/"+selectStr+"/ig"))[showNum];
                            return dataStr;
                        }else{
                            dataStr = data.split(eval("/"+selectStr+"/ig"))[0] + "<i style='color:#03c339;font-weight:bold;'>" + selectStr + "</i>" + data.split(eval("/"+selectStr+"/ig"))[1];
                            return dataStr;
                        }
                    }


                    if(str.title!=null&&str.title.indexOf(selectStr) > -1){
                        str["title"] = changeStr(str.title);
                    }
                    if(str.content!=null&&str.content.indexOf(selectStr) > -1){
                        str["title"] = changeStr(str.content);

                    }
                    if((str.title!=null&&str.content!=null)&&(str.title.indexOf(selectStr) > -1 || str.content.indexOf(selectStr) > -1)){
                        filteredNewsData.push(str);
                    }


                }

                newsList(filteredNewsData);
                layer.close(index);
            },1000);
        }else{
            newsList(newsData);
        }
    })


    function newsList(data_param){
        //根据data源和offset渲染数据
        function renderDate(data,curr){
            var dataHtml = '';
            var currData = data.concat().splice(curr*nums-nums, nums);
            if(currData.length != 0){
                for(var i=0;i<currData.length;i++){
                    var t=new Date(currData[i].date);
                    dataHtml += '<tr>'
                        +  '<td>'+currData[i].newsId+'</td>'
                        +  '<td>'+currData[i].title+'</td>'
                        +  '<td>'+currData[i].content+'</td>'
                        +  '<td>'+fromDateToChineseString(t)+'</td>'
                        +'</tr>';
                }
            }
            else{
                dataHtml = '<tr><td colspan="4">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页渲染
        var nums = 12; //每页出现的数据量
        laypage({
            cont : "page",
            pages : Math.ceil(data_param.length/nums),
            jump : function(obj){
                $(".news_content").html(renderDate(data_param,obj.curr));
                $('.news_list thead input[type="checkbox"]').prop("checked",false);
                form.render();
            }
        })
    }

    function refreshNewsLists(){
        $.ajax({
            url: baseUrl + "news",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status === 200) {
                    newsData=data.responseMap.result;
                    newsList(newsData);
                }
                else{
                    top.layer.msg("无新闻");
                    newsData=[];
                    newsList(newsData);
                }
            },
            error: function () {
                alert("检查一下网络吧");
                $(window).attr('location', baseUrl);
            }
        });
    }
});
