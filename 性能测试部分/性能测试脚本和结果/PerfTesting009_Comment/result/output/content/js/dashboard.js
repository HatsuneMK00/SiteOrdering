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

    var data = {"OkPercent": 99.59887774250753, "KoPercent": 0.4011222574924697};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.27820450239871325, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.19103024354967846, 500, 1500, "comment_pub_page_get"], "isController": false}, {"data": [0.18707447905921187, 500, 1500, "user_getByName"], "isController": false}, {"data": [0.5025820444628056, 500, 1500, "comment_pub_page"], "isController": false}, {"data": [0.18999857824193153, 500, 1500, "comment_pub_req"], "isController": false}, {"data": [0.04833929877227418, 500, 1500, "ground_page"], "isController": true}, {"data": [0.9314455650374968, 500, 1500, "comment_pub_page_pubed"], "isController": false}, {"data": [0.18178055972272517, 500, 1500, "comment_del_req"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 271239, 1088, 0.4011222574924697, 2267.1994034780955, 6, 103844, 2587.800000000003, 3923.800000000003, 10718.530000000075, 66.99341844785823, 460.67433948911525, 31.858978130871424], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["comment_pub_page_get", 38719, 0, 0.0, 2307.269299310401, 7, 35267, 5008.9000000000015, 5773.9000000000015, 8428.900000000016, 9.563382758593436, 6.690763788178568, 4.191824687746377], "isController": false}, {"data": ["user_getByName", 77552, 0, 0.0, 2333.273971013008, 8, 16386, 4841.600000000006, 5712.0, 8168.980000000003, 19.15491343585579, 7.033444777228297, 8.64215821031775], "isController": false}, {"data": ["comment_pub_page", 77458, 45, 0.05809600041312711, 1655.1376875209696, 9, 57904, 3981.0, 5688.850000000002, 9238.970000000005, 19.131648703628144, 82.95123089109016, 12.930493809454504], "isController": false}, {"data": ["comment_pub_req", 77369, 701, 0.9060476418203673, 2304.502152024718, 7, 60939, 4192.800000000003, 5416.0, 8255.340000000106, 19.109642652226437, 30.360033094972227, 15.746756716340771], "isController": false}, {"data": ["ground_page", 77949, 728, 0.9339439890184608, 5831.5673453155405, 52, 103844, 10304.500000000007, 16100.700000000019, 34849.400000000416, 19.252119056927974, 767.2369515051547, 8.046132910975034], "isController": true}, {"data": ["comment_pub_page_pubed", 38670, 5, 0.012929919834497027, 224.73638479441505, 7, 56521, 661.0, 762.0, 3204.0, 9.551289461126474, 23.215951923107795, 4.5138929411475575], "isController": false}, {"data": ["comment_del_req", 77324, 696, 0.9001086338006311, 2451.514006000706, 6, 16012, 5267.0, 6007.9000000000015, 8865.890000000018, 19.098631722490392, 3.829483799853137, 9.641079281464739], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["400", 348, 31.985294117647058, 0.12830013383031202], "isController": false}, {"data": ["405", 348, 31.985294117647058, 0.12830013383031202], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 392, 36.029411764705884, 0.14452198983184572], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 271239, 1088, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 392, "400", 348, "405", 348, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["comment_pub_page", 38739, 23, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 23, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["comment_pub_req", 38699, 348, "400", 348, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["ground_page", 38974, 364, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 364, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["comment_pub_page_pubed", 38670, 5, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 5, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["comment_del_req", 38662, 348, "405", 348, null, null, null, null, null, null, null, null], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
