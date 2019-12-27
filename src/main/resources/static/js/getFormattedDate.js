function fromDateToString(t){
    function pad(num, n) {
        var ary=Array(n>num?(n-(''+num).length+1):0).join(0)+num;
        return ary;
    }

    // time formatting to "yyyy-MM-dd HH:mm:ss.0"
    var tt=t.getFullYear()+'-'+pad((t.getMonth()+1),2)+'-'+pad(t.getDate(),2)+' '+
        pad(t.getHours(),2)+':'+pad(t.getMinutes(),2)+':'+pad(t.getSeconds(),2)+'.0'
    return tt;
}

function fromDateToChineseString(t){
    function pad(num, n) {
        var ary=Array(n>num?(n-(''+num).length+1):0).join(0)+num;
        return ary;
    }

    // time formatting to "yyyy-MM-dd HH:mm:ss.0"
    var tt=t.getFullYear()+'年'+pad((t.getMonth()+1),2)+'月'+pad(t.getDate(),2)+'日 '+
        pad(t.getHours(),2)+'时'+pad(t.getMinutes(),2)+'分'+pad(t.getSeconds(),2)+'秒'
    return tt;
}