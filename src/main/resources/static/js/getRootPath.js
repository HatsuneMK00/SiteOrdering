function getRootPath_web() {
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
    return(localhostPath);
}