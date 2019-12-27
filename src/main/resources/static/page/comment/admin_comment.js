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

    //查询
    $(".search_btn").click(function(){
        var newArray1 = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            var matchStr = $(".search_input").val();
            setTimeout(function(){
                $.ajax({
                    url :  baseUrl+"comment/match",
                    type : "post",
                    dataType : "json",
                    contentType : 'application/json; charset=UTF-8',
                    data: JSON.stringify({match:matchStr}),
                    async:false,
                    success : function(data){
                        if(data.status==200){
                            newsData=data.responseMap.result;
                            newsList(newsData);
                        }
                        else if(data.status==500){
                            layer.msg("错误的请求");
                        }
                        else{
                            layer.msg("无结果");
                            newsData = data.responseMap.result;
                            newsList(newsData);
                        }

                    },
                    error: function () {
                        layer.msg("检查一下网络吧");
                        newsData=[];
                        newsList([]);
                    }

                });
                layer.close(index);
            },1000);
        }else{
            refreshList();
        }
    });

    //批量删除
    $(".batchDel").click(function(){
            var $checked = $('.news_content').find('input[type="checkbox"][name="checked"]:checked');
            if ($checked.length > 0) {
                layer.confirm('确定删除？',{icon:3, title:'提示信息'},function(index){
                    var ids=[];
                    for(var i=0;i<$checked.length;i++){
                        ids.push(parseInt($checked[i].parentNode.parentNode.children[1].innerHTML));
                    }
                    $.ajax({
                        url: baseUrl + "comment/deleteByBatch",
                        type: "delete",
                        dataType: "json",
                        contentType: 'application/json;charset=UTF-8',
                        data:JSON.stringify({ids:ids}),
                        async:false,
                        success: function (data) {
                            if (data.status === 200) {
                                layer.msg("评论删除成功")
                                window.location.reload();
                            }
                            else{
                                layer.msg("评论删除失败");
                            }
                        },
                        error: function () {
                            layer.msg("检查一下网络吧");
                            window.location.reload();
                        }
                    });
                    layer.close(index);
                });
            } else {
                layer.msg("请选择需要审核的文章");
            }
        }
    );

    //全选
    form.on('checkbox(allChoose)', function(data){
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //通过判断文章是否全部选中来确定全选按钮是否选中
    form.on("checkbox(choose)",function(data){
        var child = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"])');
        var childChecked = $(data.elem).parents('table').find('tbody input[type="checkbox"]:not([name="show"]):checked')
        if(childChecked.length == child.length){
            $(data.elem).parents('table').find('thead input#allChoose').get(0).checked = true;
        }else{
            $(data.elem).parents('table').find('thead input#allChoose').get(0).checked = false;
        }
        form.render('checkbox');
    })

    //通过
    $("body").on("click",".news_pass",function(){

        var _this = $(this);

        layer.confirm('通过此评论？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].commentId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'comment/check/'+newsData[i].commentId,
                        type : "PUT",
                        dataType : "json",
                        contentType : 'application/json;charset=UTF-8',
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                console.log(newsData[i]);
                                newsData[i].checked="1";
                                layer.msg("审核成功");
                                newsList(newsData);

                            }else{
                                layer.msg("审核失败");
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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });

    });

    //不通过
    $("body").on("click",".news_reject",function(){

        var _this = $(this);

        layer.confirm('拒绝此评论？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].commentId == _this.attr("data-id")){

                    $.ajax({
                        url : baseUrl+'comment/uncheck/'+newsData[i].commentId,
                        type : "PUT",
                        dataType : "json",
                        contentType : 'application/json;charset=UTF-8',
                        async:false,
                        success : function(data){
                            if(data.status===200){
                                console.log(newsData[i]);
                                newsData[i].checked="-1";
                                layer.msg("审核成功");
                                newsList(newsData);

                            }else{
                                layer.msg("审核失败");
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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });

    });

    //删除
    $("body").on("click",".news_del",function(){

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
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });
    });

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
                        +'<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        +'<td>'+currData[i].commentId+'</td>'
                        +'<td>'+currData[i].content+'</td>'
                        +'<td>'+currData[i].groundId+'</td>'
                        +'<td>'+groundName+'</td>'
                        +'<td>'+currData[i].userId+'</td>'
                        +'<td>'+userName+'</td>'
                        // 时间格式化
                        +  '<td>'+fromDateToChineseString(orderTime)+'</td>'
                    ;

                    if(currData[i].checked == "0"){
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-btn-normal layui-btn-mini news_pass" data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe600;</i> 通过</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_reject" data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe640;</i> 不通过</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                            +'</td>'
                            +'</tr>';
                    }else{
                        var txt= '审核不通过';
                        if(currData[i].checked == "0"){
                            txt= '未审核';
                        }else if(currData[i].checked == "1"){
                            txt= '已通过';
                        }else if(currData[i].checked == "-1"){
                            txt= '未通过';
                        }
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-btn-disabled layui-btn-mini " data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe600;</i>'+ txt+'</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+currData[i].commentId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                            +'</td>'
                            +'</tr>';
                    }

                }
            }else{
                dataHtml = '<tr><td colspan="11">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 7; //每页出现的数据量
        laypage({
            cont : "page",
            pages : Math.ceil(that.length/nums),
            jump : function(obj){
                $(".news_content").html(renderDate(that,obj.curr));
                $('.news_list thead input[type="checkbox"]').prop("checked",false);
                form.render();
            }
        })
    }

    function refreshList(){
        $.ajax({
            url: baseUrl + "comment/allComment",
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
