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
    //加载页面数据
    var newsData = '';
    $.get(baseUrl+"car/getCarRentIn", function(data){
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
        var newArray = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                $.ajax({
                    url : baseUrl+"car/getCarRentIn",
                    type : "get",
                    dataType : "json",
                    success : function(data){
                        if(window.sessionStorage.getItem("addNews")){
                            var addNews = window.sessionStorage.getItem("addNews");
                            newsData = JSON.parse(addNews).concat(data.data);
                        }else{
                            newsData = data.data;
                        }
                        for(var i=0;i<newsData.length;i++){
                            var newsStr = newsData[i];
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
                            //文章标题
                            if(newsStr.title.indexOf(selectStr) > -1){
                                newsStr["newsName"] = changeStr(newsStr.title);
                            }
                            //发布人
                            if(newsStr.user.indexOf(selectStr) > -1){
                                newsStr["newsAuthor"] = changeStr(newsStr.user);
                            }
                            //审核状态
                            if(newsStr.pay.indexOf(selectStr) > -1){
                                newsStr["newsStatus"] = changeStr(newsStr.pay);
                            }
                            //浏览权限
                            if(newsStr.newsLook.indexOf(selectStr) > -1){
                                newsStr["newsLook"] = changeStr(newsStr.newsLook);
                            }
                            //发布时间
                            if(newsStr.newsTime.indexOf(selectStr) > -1){
                                newsStr["newsTime"] = changeStr(newsStr.newsTime);
                            }
                            if(newsStr.newsName.indexOf(selectStr)>-1 || newsStr.newsAuthor.indexOf(selectStr)>-1 || newsStr.newsStatus.indexOf(selectStr)>-1 || newsStr.newsLook.indexOf(selectStr)>-1 || newsStr.newsTime.indexOf(selectStr)>-1){
                                newArray.push(newsStr);
                            }
                        }
                        newsData = newArray;
                        newsList(newsData);
                    }
                })

                layer.close(index);
            },2000);
        }else{
            layer.msg("请输入需要查询的内容");
        }
    })

    //添加文章
    $(".newsAdd_btn").click(function(){
        var index = layui.layer.open({
            title : "添加文章",
            type : 2,
            content : "addCarRentIn.jsp",
            success : function(layero, index){
                layui.layer.tips('点击此处返回文章列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function(){
            layui.layer.full(index);
        })
        layui.layer.full(index);
    })

    //推荐文章
    $(".recommend").click(function(){
        var $checkbox = $(".news_list").find('tbody input[type="checkbox"]:not([name="show"])');
        if($checkbox.is(":checked")){
            var index = layer.msg('推荐中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                layer.close(index);
                layer.msg("推荐成功");
            },2000);
        }else{
            layer.msg("请选择需要推荐的文章");
        }
    })

    //审核文章
    $(".audit_btn").click(function(){
        var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
        var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
        if($checkbox.is(":checked")){
            var index = layer.msg('审核中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                for(var j=0;j<$checked.length;j++){
                    for(var i=0;i<newsData.length;i++){
                        if(newsData[i].newsId == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")){
                            //修改列表中的文字
                            $checked.eq(j).parents("tr").find("td:eq(3)").text("审核通过").removeAttr("style");
                            //将选中状态删除
                            $checked.eq(j).parents("tr").find('input[type="checkbox"][name="checked"]').prop("checked",false);
                            form.render();
                        }
                    }
                }
                layer.close(index);
                layer.msg("审核成功");
            },2000);
        }else{
            layer.msg("请选择需要审核的文章");
        }
    })

    //批量删除
    $(".batchDel").click(function(){
        var $checkbox = $('.news_list tbody input[type="checkbox"][name="checked"]');
        var $checked = $('.news_list tbody input[type="checkbox"][name="checked"]:checked');
        if($checkbox.is(":checked")){
            layer.confirm('确定删除选中的信息？',{icon:3, title:'提示信息'},function(index){
                var index = layer.msg('删除中，请稍候',{icon: 16,time:false,shade:0.8});
                setTimeout(function(){
                    //删除数据
                    for(var j=0;j<$checked.length;j++){
                        for(var i=0;i<newsData.length;i++){
                            if(newsData[i].newsId == $checked.eq(j).parents("tr").find(".news_del").attr("data-id")){
                                newsData.splice(i,1);
                                newsList(newsData);
                            }
                        }
                    }
                    $('.news_list thead input[type="checkbox"]').prop("checked",false);
                    form.render();
                    layer.close(index);
                    layer.msg("删除成功");
                },2000);
            })
        }else{
            layer.msg("请选择需要删除的文章");
        }
    })

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
        if($(this).text().indexOf("已审核") > 0){
            layer.msg("取消审核成功！");
            $(this).html("<i class='layui-icon'>&#xe600;</i> 审核");
        }else{
            var _this = $(this);

            layer.confirm('审核此信息？',{icon:3, title:'提示信息'},function(index){

                //_this.parents("tr").remove();
                for(var i=0;i<newsData.length;i++){
                    if(newsData[i].id == _this.attr("data-id")){
                        $.ajax({
                            url : baseUrl+"car/passCarRentIn",
                            type : "get",
                            data:{id:newsData[i].id  },
                            dataType : "json",
                            success : function(data){
                                linksData = data.data;
                                //alert(data);
                                if(linksData==1){
                                    layer.msg("审核成功");
                                }else{
                                    layer.msg("审核失败");
                                }
                            },
                            error:function () {
                                layer.msg("审核失败>_<");
                            }
                        });
                        // newsData.splice(i,1);
                        newsList(newsData);
                    }
                }
                layer.close(index);
                $(this).html("<i class='iconfont icon-star'></i> 已审核");
            });

        }



    });

    $("body").on("click",".news_del",function(){  //删除
        var _this = $(this);

        layer.confirm('确定删除此信息？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].id == _this.attr("data-id")){
                    $.ajax({
                        url : baseUrl+"car/deleteCarRentIn",
                        type : "get",
                        data:{id:newsData[i].id  },
                        dataType : "json",
                        success : function(data){
                            linksData = data.data;
                            //alert(data);
                            if(linksData==1){
                                alert("删除成功");
                            }else{
                                alert("删除失败");
                            }
                        },
                        error:function () {
                            alert("删除失败>_<");
                        }
                    });
                    newsData.splice(i,1);
                    newsList(newsData);
                }
            }
            layer.close(index);
        });
    })

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
                        +'<td align="left">'+currData[i].title+'</td>'
                        +'<td>'+currData[i].user+'</td>';
                    if(currData[i].status == "待审核"){
                        dataHtml += '<td style="color:#f00">'+currData[i].status+'</td>';
                    }else{
                        dataHtml += '<td>'+currData[i].status+'</td>';
                    }
                    dataHtml += '<td>'+currData[i].pay+'</td>'
                        +'<td>'+currData[i].date+'</td>'
                        +'<td>'
                        +  '<a class="layui-btn layui-btn-mini news_edit"><i class="iconfont icon-edit"></i> 编辑</a>'
                        +  '<a class="layui-btn layui-btn-normal layui-btn-mini news_collect" data-id="'+data[i].id+'"><i class="layui-icon">&#xe600;</i> 审核</a>'
                        +  '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+data[i].id+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                        +'</td>'
                        +'</tr>';
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