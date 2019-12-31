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

    var data = {"OkPercent": 99.94321408290745, "KoPercent": 0.05678591709256105};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.7663500868452855, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.6332369942196532, 500, 1500, "news_get"], "isController": false}, {"data": [0.9596529534268687, 500, 1500, "new_pubPage"], "isController": true}, {"data": [0.7679749913164293, 500, 1500, "news_del-53"], "isController": false}, {"data": [0.6352746888206193, 500, 1500, "news_getAll"], "isController": false}, {"data": [0.7865590659836865, 500, 1500, "news_page"], "isController": false}, {"data": [0.683097576098398, 500, 1500, "new_pub_req"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 415596, 236, 0.05678591709256105, 922.620624356336, 7, 97793, 3776.0, 5253.950000000001, 10275.670000000053, 89.2050925522475, 422.2478026917188, 42.75313467272031], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["news_get", 69200, 110, 0.15895953757225434, 1769.679393063585, 8, 97793, 8786.700000000004, 11450.0, 21888.88000000002, 14.853916454018114, 172.46151890498948, 6.299982969378394], "isController": false}, {"data": ["new_pubPage", 138771, 2, 0.0014412233103458215, 144.19438499398396, 11, 30007, 286.0, 701.0, 1562.9900000000016, 29.785736132947697, 66.20360978340193, 14.078211442699304], "isController": true}, {"data": ["news_del-53", 138192, 2, 0.001447261780710895, 925.1843883871653, 13, 30007, 7469.700000000004, 8547.95, 12878.990000000002, 29.663140727123874, 9.589351137041984, 15.09206313653605], "isController": false}, {"data": ["news_getAll", 69333, 122, 0.17596238443454054, 1753.2284049442553, 8, 92536, 8485.700000000004, 11258.95, 22167.3700000001, 14.882088218867345, 172.6146044637196, 6.325364243096216], "isController": false}, {"data": ["news_page", 138413, 114, 0.08236220586216612, 992.0120147673932, 7, 98088, 7778.800000000003, 10054.95, 18082.730000000043, 29.710553231953448, 241.3987288139004, 19.733160097862285], "isController": false}, {"data": ["new_pub_req", 138702, 122, 0.0879583567648628, 1712.6501708699254, 11, 115500, 12239.900000000001, 15009.0, 22538.81000000003, 29.771520291778156, 182.2357927690592, 23.973049204052604], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 236, 100.0, 0.05678591709256105], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 415596, 236, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 236, null, null, null, null, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["news_get", 69200, 110, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 110, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["new_pubPage", 69385, 1, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 1, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["news_del-53", 69096, 1, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 1, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["news_getAll", 69333, 122, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 122, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["news_page", 69213, 2, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 2, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
