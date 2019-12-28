layui.config({
    base: "js/"
}).use(['form', 'layer', 'jquery', 'laypage'], function () {
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
        if($(".search_input").val() !== ''){
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
                        if(data.status===200){
                            newsData=data.responseMap.result;
                            newsList(newsData);
                        }
                        else if(data.status===500){
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

    // 操作
    // 租用场地
    //跳转到租用场地的专用页面
    $("body").on("click", ".preorder_ground", function () {
        //获取场地ID
        var _this = $(this);
        var ground_id = _this.attr("data-id");
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "预约场馆",
            type : 2,
            content : "preorder.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });

    //跳转到查看评论的专用页面
    $("body").on("click", ".watch_comment", function () {
        var _this = $(this);
        var ground_id = _this.attr("data-id");
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "场馆评论",
            type : 2,
            content : "comment.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });

    //跳转到评论的专用页面
    $("body").on("click", ".make_comment", function () {
        var _this = $(this);
        var ground_id = _this.attr("data-id");
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "对场馆进行评论",
            type : 2,
            content : "make_comment.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });

    //跳转到场地查看页面
    $("body").on("click", ".lookup_ground", function () {
        var _this = $(this);
        var ground_id = _this.attr("data-id");
        $.cookie('groundId', ground_id);
        var index = layui.layer.open({
            title : "场馆详细信息",
            type : 2,
            content : "ground_info.html",
            success : function(layero, index){
                layui.layer.tips('点击此处返回场馆列表', '.layui-layer-setwin .layui-layer-close', {
                    tips: 3
                });
            }
        })
        //改变窗口大小时，重置弹窗的高度，防止超出可视区域（如F12调出debug的操作）
        $(window).resize(function() {
            layui.layer.full(index);
        })
        layui.layer.full(index);
    });

    //渲染数据函数
    function newsList(that) {
        function renderDate(data, curr) {
            var dataHtml = '';
            var currData = data.concat().splice(curr * nums - nums, nums);
            if (currData.length !== 0) {
                for (var i = 0; i < currData.length; i++) {
                    if(currData[i].photo==null){
                        currData[i].photo=""
                    }
                    dataHtml += '<tr>'
                        +  '<td>'
                        +  '<a class="layui-btn layui-btn-normal layui-btn-mini lookup_ground" data-id='+currData[i].groundId+ ' ><i class="layui-icon">&#xe602;</i>'+currData[i].groundId+'</a>'
                        +'</td>'
                        +  '<td>'+currData[i].groundName +'</td>'
                        +  '<td>'+currData[i].description+'</td>'
                        +  '<td>'+currData[i].pricePerHour+'</td>'
                        +  '<td>'+currData[i].address+'</td>'
                        +  '<td >'+'<img width="100%" class="layui-box" id="photo" src='
                        +'"'+         currData[i].photo                  +'"'
                        +  ' alt="未添加图片"></img></td>'
                        + '<td style="text-align:center;">'
                            +  '<a class="layui-btn layui-btn-normal layui-btn-mini preorder_ground" data-id='+currData[i].groundId+ ' ><i class="layui-icon">&#xe605;</i> 我要预约</a><br>'
                            +  '<a class="layui-btn layui-btn-warm layui-btn-mini watch_comment" data-id='+currData[i].groundId+ ' ><i class="layui-icon">&#xe63a;</i> 查看评论</a><br>'
                            +  '<a class="layui-btn layui-btn-danger layui-btn-mini make_comment" data-id='+currData[i].groundId+ ' ><i class="layui-icon">&#xe642;</i> 发布评论</a>'
                        + '</td>'
                        + '</tr>';
                }
            } else {
                dataHtml = '<tr><td colspan="8">暂无数据</td></tr>';
            }
            return dataHtml;
        }

        //分页
        var nums = 8; //每页出现的数据量
        laypage({
            cont: "page",
            pages: Math.ceil(that.length / nums),
            jump: function (obj) {
                $(".grounds_content").html(renderDate(that, obj.curr));
                $('.grounds_list thead input[type="checkbox"]').prop("checked", false);
                form.render();
            }
        })
    }

    //加载页面数据——所有的场地信息
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

