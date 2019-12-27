layui.config({
    base : "js/"
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
    $(".search_btn").click(function(){
        var newArray1 = [];
        if($(".search_input").val() != ''){
            var index = layer.msg('查询中，请稍候',{icon: 16,time:false,shade:0.8});
            var matchStr = $(".search_input").val();
            setTimeout(function(){
                $.ajax({
                    url :  baseUrl+"ground/match",
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
            refreshNewsLists();
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
                        url: baseUrl + "ground/deleteByBatch",
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

    //添加场地信息
    $(".newsAdd_btn").click(function(){
        var index = layui.layer.open({
            title : "添加场地",
            type : 2,
            content : "admin_add_ground.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场地列表', '.layui-layer-setwin .layui-layer-close', {
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
        layer.confirm('删除此场地？',{icon:3, title:'提示信息'},function(index){
            for(var i=0;i<newsData.length;i++){
                if(newsData[i].groundId == _this.attr("data-id")){
                    $.ajax({
                        url : baseUrl+"ground/"+newsData[i].groundId,
                        type : "delete",
                        async:false,
                        success : function(data){
                            if(data.status=200)
                            {
                                layer.msg("删除场地成功");
                                newsData.splice(i,1);
                                newsList(newsData);
                            }
                            else{
                                layer.msg("删除失败");
                            }
                        },
                        error:function () {
                            layer.msg("检查一下网络把");
                        }
                    });
                }
            }
            layer.close(index);
        });
    });

    function newsList(data_param){
        //根据data源和offset渲染数据
        function renderDate(data,curr){
            var dataHtml = '';
            var currData = data.concat().splice(curr*nums-nums, nums);
            if(currData.length != 0){
                for(var i=0;i<currData.length;i++){
                    if(currData[i].photo==null){
                        currData[i].photo=""
                    }
                    dataHtml += '<tr>'
                        +  '<td><input type="checkbox" name="checked" lay-skin="primary" lay-filter="choose"></td>'
                        +  '<td>'+currData[i].groundId+'</td>'
                        +  '<td>'+currData[i].groundName+'</td>'
                        +  '<td>'+currData[i].description+'</td>'
                        +  '<td>'+currData[i].pricePerHour+'</td>'
                        +  '<td>'+currData[i].address+'</td>'
                        +  '<td >'+'<img width="100%" class="layui-box" id="photo" src='
                         +'"'+         currData[i].photo                  +'"'
                        +  ' alt="未添加图片"></img></td>'
                        +  '<td>'
                        +    '<a class="layui-btn layui-btn-danger layui-btn-mini news_del" data-id="'+currData[i].groundId+'"><i class="layui-icon">&#xe640;</i> 删除</a>'
                        +  '</td>'
                        +'</tr>';
                }
            }
            else{
                dataHtml = '<tr><td colspan="5">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页渲染
        var nums = 8; //每页出现的数据量
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
            url: baseUrl + "ground",
            type: "get",
            dataType: "json",
            success: function (data) {
                if (data.status === 200) {
                    newsData=data.responseMap.result;
                    newsList(newsData);
                }
                else{
                    top.layer.msg("无场地");
                    newsData=[];
                    newsList(newsData);
                }
            },
            error: function () {
                layer.msg("检查一下网络吧");
                $(window).attr('location', baseUrl);
            }
        });
    }
});
