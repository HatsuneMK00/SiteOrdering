/**
 * Created by 蛟川小盆友 on 2017/12/19.
 */
/**
 * Created by 蛟川小盆友 on 2017/12/6.
 */
layui.config({
    base : "js/"
}).use(['form','layer','jquery','laypage'],function(){
    var form = layui.form(),
        layer = parent.layer === undefined ? layui.layer : parent.layer,
        laypage = layui.laypage,
        $ = layui.jquery;
    var baseUrl = getRootPath_web();
    //加载页面数据
    var newsData = '';
    $.get(baseUrl+"carout/orderTraversal", function(data){
        var newArray = [];
        //单击首页“待审核文章”加载的信息
        if($(".top_tab li.layui-this cite",parent.document).text() == "待审核文章"){
            if(window.sessionStorage.getItem("addNews")){
                var addNews = window.sessionStorage.getItem("addNews");
                newsData = JSON.parse(addNews).concat(data.data);
            }else{
                newsData = data.data;
            }
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].newsStatus == "待审核"){
                    newArray.push(newsData[i]);
                }
            }
            newsData = newArray;
            newsList(newsData);
        }else{    //正常加载信息
            newsData = data.data;
            if(window.sessionStorage.getItem("addNews")){
                var addNews = window.sessionStorage.getItem("addNews");
                newsData = JSON.parse(addNews).concat(newsData);
            }
            //执行加载数据的方法
            newsList();
        }
    })

    //查询
    $(".search_btn").click(function(){
        var newArray1 = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                $.ajax({
                    url :  baseUrl+"carout/orderTraversal",
                    type : "get",
                    dataType : "json",
                    success : function(data){

                        newsData = data.data;
                        var selectStr = $(".search_input").val();
                        for(var i=0;i<newsData.length;i++){
                            var newsStr = newsData[i];
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
                            if(newsStr.carName.indexOf(selectStr) > -1){
                                newsStr["carName"] = changeStr(newsStr.carName);
                            }
                            // if(newsStr.senderName.indexOf(selectStr) > -1){
                            //     newsStr["senderName"] = changeStr(newsStr.senderName);
                            // }
                            // if(newsStr.receiverName.indexOf(selectStr) > -1){
                            //     newsStr["receiverName"] = changeStr(newsStr.receiverName);
                            // }
                            if(newsStr.carName.indexOf(selectStr)>-1 ){
                                newArray1.push(newsStr);
                            }
                        }
                        newsData = newArray1;
                        newsList(newsData);
                    }
                });

                layer.close(index);
            },2000);
        }else{
            layer.msg("请输入需要查询的内容");
        }
    });







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

    //是否展示
    form.on('switch(isShow)', function(data){
        var index = layer.msg('修改中，请稍候',{icon: 16,time:false,shade:0.8});
        setTimeout(function(){
            layer.close(index);
            layer.msg("展示状态修改成功！");
        },2000);
    })

    //操作
    $("body").on("click",".news_edit",function(){  //编辑
        layer.alert('您点击了文章编辑按钮，由于是纯静态页面，所以暂时不存在编辑内容，后期会添加，敬请谅解。。。',{icon:6, title:'文章编辑'});
    })

    $("body").on("click",".news_collect",function(){  //收藏.

        var _this = $(this);

        layer.confirm('审核此信息？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].id == _this.attr("data-id")){
                    $.ajax({
                        url : baseUrl+"carout/orderPass",
                        type : "get",
                        data:{id:newsData[i].id  },
                        dataType : "json",
                        success : function(data){
                            linksData = data.data;
                            //alert(data);
                            if(data.code=="200"){
                                layer.msg("审核成功");
                                window.location.reload();
                            }else{
                                layer.msg("审核失败");
                            }
                        },
                        error:function () {
                            layer.msg("操作失败>_<检查一下网络吧");
                        }
                    });
                    // newsData.splice(i,1);
                    newsList(newsData);
                }
            }

            layer.close(index);
            //$(this).html("<i class='iconfont icon-star'></i> 已审核");
        });

    });

    $("body").on("click",".news_del",function(){  //删除
        var _this = $(this);

        layer.confirm('不通过此信息？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].id == _this.attr("data-id")){
                    $.ajax({
                        url : baseUrl+"carout/orderBan",
                        type : "get",
                        data:{id:newsData[i].id  },
                        dataType : "json",
                        success : function(data){
                            linksData = data.data;
                            //alert(data);
                            if(data.code=="200"){
                                layer.msg("不通过成功");
                                window.location.reload();
                            }else{
                                layer.msg("不通过失败");
                            }
                        },
                        error:function () {
                            layer.msg("操作失败>_<检查一下网络吧");
                        }
                    });
                    // newsData.splice(i,1);
                    //window.location.reload();
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
            if(!that){
                currData = newsData.concat().splice(curr*nums-nums, nums);
            }else{
                currData = that.concat().splice(curr*nums-nums, nums);
            }
            if(currData.length != 0){
                for(var i=0;i<currData.length;i++){
                    dataHtml += '<tr>'
                        +'<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        +'<td align="left">'+currData[i].carName+'</td>'
                        +'<td>'+currData[i].senderName+'</td>';
                    if(currData[i].receiver_id=="0")
                        dataHtml+='<td>'+'无人接单'+'</td>';
                    else
                        dataHtml+='<td>'+currData[i].receiverName+'</td>';

                    if(currData[i].status == "0"){
                        dataHtml += '<td style="color:#f00">'+'未审核'+'</td>';
                    }else if(currData[i].status == "1"){
                        dataHtml += '<td>'+'未完成'+'</td>';
                    }else if(currData[i].status == "2"){
                        dataHtml += '<td>'+'已租入'+'</td>';
                    }else if(currData[i].status == "3"){
                        dataHtml += '<td>'+'审核不通过'+'</td>';
                    }
                    if(currData[i].status == "0"){
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-btn-normal layui-btn-mini news_collect" data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 通过</a>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+data[i].id+'"><i class="layui-icon">&#xe640;</i> 不通过</a>'
                            +'</td>'
                            +'</tr>';
                    }else{
                        dataHtml += '<td>'
                            +  '<a class="layui-btn layui-bg-gray layui-btn-mini " data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 已审核</a>'
                            +'</td>'
                            +'</tr>';
                    }

                }
            }else{
                dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 13; //每页出现的数据量
        if(that){
            newsData = that;
        }
        laypage({
            cont : "page",
            pages : Math.ceil(newsData.length/nums),
            jump : function(obj){
                $(".news_content").html(renderDate(newsData,obj.curr));
                $('.news_list thead input[type="checkbox"]').prop("checked",false);
                form.render();
            }
        })
    }
})
