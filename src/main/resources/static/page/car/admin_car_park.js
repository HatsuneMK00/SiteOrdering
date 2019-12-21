/**
 * Created by 蛟川小盆友 on 2017/12/19.
 */
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
    $.get(baseUrl+"park/parkTraversal", function(data){
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
                newArray.push(newsData[i]);
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
    });

    //查询
    $(".search_btn").click(function(){
        var newArray1 = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            setTimeout(function(){
                $.ajax({
                    url :  baseUrl+"park/parkTraversal",
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
                            if(newsStr.name.indexOf(selectStr) > -1){
                                newsStr["name"] = changeStr(newsStr.name);
                            }
                            if(newsStr.name.indexOf(selectStr)>-1 ){
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

    //添加停车信息
    $(".newsAdd_btn").click(function(){
        var index = layui.layer.open({
            title : "添加停车信息",
            type : 2,
            content : "admin_add_park.jsp",
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




    //删除
    $("body").on("click",".news_del",function(){
        var _this = $(this);

        layer.confirm('删除此信息？',{icon:3, title:'提示信息'},function(index){

            //_this.parents("tr").remove();
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].id == _this.attr("data-id")){
                    $.ajax({
                        url : baseUrl+"park//parkDelete",
                        type : "get",
                        data:{id:newsData[i].id  },
                        dataType : "json",
                        success : function(data){
                            linksData = data.data;
                            //alert(data);
                            if(data.code=="200"){
                                layer.msg("删除成功");
                                window.location.reload();
                            }else if(data.code=="202"){
                                layer.msg("删除失败，此停车位正在使用");
                            }else{
                                layer.msg("删除失败");
                            }
                        },
                        error:function () {
                            layer.msg("删除失败>_<检查一下网络吧");
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
                        +'<td align="left">'+currData[i].id+'</td>'
                        +'<td>'+currData[i].name+'</td>'
                        +'<td>'+currData[i].price_per_day+'</td>';


                    if(currData[i].status == "0"){
                        dataHtml += '<td >'+'未租出'+'</td>';
                    }else {
                        dataHtml += '<td>'+'已租出'+'</td>';
                    }
                        dataHtml += '<td>'
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
});
