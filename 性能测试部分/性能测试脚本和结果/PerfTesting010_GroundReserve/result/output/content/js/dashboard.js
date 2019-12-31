/*
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
var showControllersOnly = false;
var seriesFilter = "";
var filtersOnlySampleSeries = true;

/*
 * Add header in statistics table to group metrics by category
 * format
 *
 */
function summaryTableHeader(header) {
    var newRow = header.insertRow(-1);
    newRow.className = "tablesorter-no-sort";
    var cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Requests";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 3;
    cell.innerHTML = "Executions";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 6;
    cell.innerHTML = "Response Times (ms)";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 1;
    cell.innerHTML = "Throughput";
    newRow.appendChild(cell);

    cell = document.createElement('th');
    cell.setAttribute("data-sorter", false);
    cell.colSpan = 2;
    cell.innerHTML = "Network (KB/sec)";
    newRow.appendChild(cell);
}

/*
 * Populates the table identified by id parameter with the specified data and
 * format
 *
 */
function createTable(table, info, formatter, defaultSorts, seriesIndex, headerCreator) {
    var tableRef = table[0];

    // Create header and populate it with data.titles array
    var header = tableRef.createTHead();

    // Call callback is available
    if(headerCreator) {
        headerCreator(header);
    }

    var newRow = header.insertRow(-1);
    for (var index = 0; index < info.titles.length; index++) {
        var cell = document.createElement('th');
        cell.innerHTML = info.titles[index];
        newRow.appendChild(cell);
    }

    var tBody;

    // Create overall body if defined
    if(info.overall){
        tBody = document.createElement('tbody');
        tBody.className = "tablesorter-no-sort";
        tableRef.appendChild(tBody);
        var newRow = tBody.insertRow(-1);
        var data = info.overall.data;
        for(var index=0;index < data.length; index++){
            var cell = newRow.insertCell(-1);
            cell.innerHTML = formatter ? formatter(index, data[index]): data[index];
        }
    }

    // Create regular body
    tBody = document.createElement('tbody');
    tableRef.appendChild(tBody);

    var regexp;
    if(seriesFilter) {
        regexp = new RegExp(seriesFilter, 'i');
    }
    // Populate body with data.items array
    for(var index=0; index < info.items.length; index++){
        var item = info.items[index];
        if((!regexp || filtersOnlySampleSeries && !info.supportsControllersDiscrimination || regexp.test(item.data[seriesIndex]))
                &&
                (!showControllersOnly || !info.supportsControllersDiscrimination || item.isController)){
            if(item.data.length > 0) {
                var newRow = tBody.insertRow(-1);
                for(var col=0; col < item.data.length; col++){
                    var cell = newRow.insertCell(-1);
                    cell.innerHTML = formatter ? formatter(col, item.data[col]) : item.data[col];
                }
            }
        }
    }

    // Add support of columns sort
    table.tablesorter({sortList : defaultSorts});
}

$(document).ready(function() {

    // Customize table sorter default options
    $.extend( $.tablesorter.defaults, {
        theme: 'blue',
        cssInfoBlock: "tablesorter-no-sort",
        widthFixed: true,
        widgets: ['zebra']
    });

    var data = {"OkPercent": 99.67564155762184, "KoPercent": 0.324358442378161};
    var dataset = [
        {
            "label" : "KO",
            "data" : data.KoPercent,
            "color" : "#FF6347"
        },
        {
            "label" : "OK",
            "data" : data.OkPercent,
            "color" : "#9ACD32"
        }];
    $.plot($("#flot-requests-summary"), dataset, {
        series : {
            pie : {
                show : true,
                radius : 1,
                label : {
                    show : true,
                    radius : 3 / 4,
                    formatter : function(label, series) {
                        return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'
                            + label
                            + '<br/>'
                            + Math.round10(series.percent, -2)
                            + '%</div>';
                    },
                    background : {
                        opacity : 0.5,
                        color : '#000'
                    }
                }
            }
        },
        legend : {
            show : true
        }
    });

    // Creates APDEX table
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.28440080368564186, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.2438953641685565, 500, 1500, "order_pass"], "isController": false}, {"data": [0.2648630064529779, 500, 1500, "order_my_delete"], "isController": false}, {"data": [0.35205756453688386, 500, 1500, "reserve_page"], "isController": false}, {"data": [0.2816982817508276, 500, 1500, "reserve_page_getById"], "isController": false}, {"data": [0.2669078947368421, 500, 1500, "order_my_page"], "isController": false}, {"data": [0.6089328063241106, 500, 1500, "order_manage_page"], "isController": false}, {"data": [0.07758688114294653, 500, 1500, "ground_page"], "isController": true}, {"data": [0.23206739911261356, 500, 1500, "order_my_page_get"], "isController": false}, {"data": [0.20607112794612795, 500, 1500, "reserve_req"], "isController": false}]}, function(index, item){
        switch(index){
            case 0:
                item = item.toFixed(3);
                break;
            case 1:
            case 2:
                item = formatDuration(item);
                break;
        }
        return item;
    }, [[0, 0]], 3);

    // Create statistics table
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 171107, 555, 0.324358442378161, 2899.47555623097, 7, 96508, 1734.0, 2817.650000000005, 7658.700000000048, 81.5254109694123, 441.6097411175302, 38.92831221326122], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["order_pass", 37922, 260, 0.6856178471599599, 3113.2026528136585, 7, 30000, 6520.0, 8339.900000000001, 14124.680000000051, 18.06902269467726, 3.7940178777760836, 9.545127998539593], "isController": false}, {"data": ["order_my_delete", 37812, 258, 0.682323072040622, 2901.1111022955647, 7, 26015, 6223.9000000000015, 8147.7000000000335, 12541.980000000003, 18.018498839652896, 3.6057112397605917, 9.149418627681069], "isController": false}, {"data": ["reserve_page", 38079, 29, 0.0761574621182279, 3714.2485621996557, 9, 60769, 9357.600000000006, 12248.450000000008, 18287.910000000014, 18.143873065789943, 70.25303448745667, 16.24634124959797], "isController": false}, {"data": ["reserve_page_getById", 19031, 1, 0.005254584625085387, 2843.159003730774, 7, 30001, 6588.799999999999, 8518.399999999976, 13001.560000000005, 9.06803335984192, 2.53585058347084, 3.9398325960979967], "isController": false}, {"data": ["order_my_page", 38000, 0, 0.0, 2939.8942894736706, 8, 29301, 6301.9000000000015, 8307.900000000001, 12819.960000000006, 18.10641233828115, 7.317439547662509, 8.195532144480593], "isController": false}, {"data": ["order_manage_page", 37950, 26, 0.06851119894598155, 956.1025032938018, 8, 43875, 1529.9000000000015, 2278.0, 6868.8600000001825, 18.082579495450766, 54.48122975686197, 8.064528557491405], "isController": false}, {"data": ["ground_page", 38357, 276, 0.7195557525353912, 5926.352347680982, 52, 96508, 11005.30000000001, 15377.550000000021, 33927.86000000002, 18.275019581909554, 729.8088283324026, 7.654297764613059], "isController": true}, {"data": ["order_my_page_get", 18932, 0, 0.0, 3216.107120219747, 10, 29301, 7026.4000000000015, 9013.699999999997, 13761.379999999976, 9.021798231380627, 3.9817564478730487, 4.09681267342968], "isController": false}, {"data": ["reserve_req", 38016, 260, 0.6839225589225589, 3486.4693023990058, 10, 29163, 7002.800000000003, 9132.400000000009, 14190.700000000048, 18.113846209557607, 7.413643815938812, 10.957832647639567], "isController": false}]}, function(index, item){
        switch(index){
            // Errors pct
            case 3:
                item = item.toFixed(2) + '%';
                break;
            // Mean
            case 4:
            // Mean
            case 7:
            // Percentile 1
            case 8:
            // Percentile 2
            case 9:
            // Percentile 3
            case 10:
            // Throughput
            case 11:
            // Kbytes/s
            case 12:
            // Sent Kbytes/s
                item = item.toFixed(2);
                break;
        }
        return item;
    }, [[0, 0]], 0, summaryTableHeader);

    // Create error table
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 130, 23.423423423423422, 0.07597585136785753], "isController": false}, {"data": ["405", 258, 46.486486486486484, 0.1507828434839019], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 167, 30.09009009009009, 0.0975997475264016], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 171107, 555, "405", 258, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 167, "500", 130, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["order_pass", 18961, 130, "405", 129, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 1, null, null, null, null, null, null], "isController": false}, {"data": ["order_my_delete", 18906, 129, "405", 129, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["reserve_page", 19048, 14, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 14, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["reserve_page_getById", 19031, 1, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 1, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": ["order_manage_page", 18975, 13, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 13, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["ground_page", 19178, 138, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 138, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": ["reserve_req", 19008, 130, "500", 130, null, null, null, null, null, null, null, null], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
