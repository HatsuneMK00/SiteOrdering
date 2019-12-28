$(".logout").click(
    function() {
        closeAllTabs();
        $.cookie('userId', null);
        $.cookie('username', null);
        var curWwwPath = window.document.location.href;
        var pathName = window.document.location.pathname;
        var pos;
        if(pathName.length>1){pos = curWwwPath.indexOf(pathName);}
        else{pos = -1;}
        var localhostPath;
        if(pos>0){localhostPath = curWwwPath.substring(0, pos)+"/";}
        else{localhostPath=curWwwPath;}
        $(window).attr('location',localhostPath);
    }
)