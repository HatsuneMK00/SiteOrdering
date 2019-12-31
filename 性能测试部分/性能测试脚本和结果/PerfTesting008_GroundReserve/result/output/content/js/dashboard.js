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

    var data = {"OkPercent": 99.4821497912824, "KoPercent": 0.5178502087176002};
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
    createTable($("#apdexTable"), {"supportsControllersDiscrimination": true, "overall": {"data": [0.38819581092681493, 500, 1500, "Total"], "isController": false}, "titles": ["Apdex", "T (Toleration threshold)", "F (Frustration threshold)", "Label"], "items": [{"data": [0.34854783131871137, 500, 1500, "order_pass"], "isController": false}, {"data": [0.3603454411080393, 500, 1500, "order_my_delete"], "isController": false}, {"data": [0.46617398187575065, 500, 1500, "reserve_page"], "isController": false}, {"data": [0.3666338985827804, 500, 1500, "reserve_page_getById"], "isController": false}, {"data": [0.35432033000054636, 500, 1500, "order_my_page"], "isController": false}, {"data": [0.8436697097250314, 500, 1500, "order_manage_page"], "isController": false}, {"data": [0.057312226156079164, 500, 1500, "ground_page"], "isController": true}, {"data": [0.344121911849197, 500, 1500, "order_my_page_get"], "isController": false}, {"data": [0.32216424434488034, 500, 1500, "reserve_req"], "isController": false}]}, function(index, item){
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
    createTable($("#statisticsTable"), {"supportsControllersDiscrimination": true, "overall": {"data": ["Total", 329632, 1707, 0.5178502087176002, 1877.5341168333016, 6, 100502, 1502.0, 3078.9500000000007, 10201.920000000173, 81.34082866314668, 438.2494079453435, 38.82461922728348], "isController": false}, "titles": ["Label", "#Samples", "KO", "Error %", "Average", "Min", "Max", "90th pct", "95th pct", "99th pct", "Transactions\/s", "Received", "Sent"], "items": [{"data": ["order_pass", 73132, 812, 1.110321063282831, 1619.9145927911277, 6, 15528, 3554.0, 4594.0, 6710.94000000001, 18.063403207209948, 3.800430973159302, 9.542283521726372], "isController": false}, {"data": ["order_my_delete", 73066, 812, 1.1113240084307339, 1586.6616757452043, 6, 13703, 3556.0, 4450.950000000001, 6565.0, 18.04707910052603, 3.621561895089034, 9.163553051425012], "isController": false}, {"data": ["reserve_page", 73272, 84, 0.11464133639043564, 2232.7232094115525, 8, 57425, 5966.0, 7386.9000000000015, 12504.450000000088, 18.097888950116445, 70.0588071554857, 16.202190031477166], "isController": false}, {"data": ["reserve_page_getById", 36621, 0, 0.0, 1575.3149832063616, 7, 13850, 3729.0, 4733.950000000001, 6861.990000000002, 9.045295389248297, 2.528196443254548, 3.9300519718242546], "isController": false}, {"data": ["order_my_page", 73212, 0, 0.0, 1622.9298475659837, 8, 17525, 3528.9000000000015, 4531.950000000001, 6680.950000000008, 18.08274314709679, 7.292606729898348, 8.18487372566269], "isController": false}, {"data": ["order_manage_page", 73172, 52, 0.07106543486579565, 554.5383343355413, 8, 57002, 1472.0, 3134.0, 6491.990000000002, 18.073345594319438, 54.45245814370053, 8.060204368704944], "isController": false}, {"data": ["ground_page", 73719, 840, 1.1394620111504496, 5801.670668348641, 52, 100502, 10922.700000000004, 17049.20000000001, 44516.80000000003, 18.19024360994871, 723.4777981157223, 7.586566605398371], "isController": true}, {"data": ["order_my_page_get", 36551, 0, 0.0, 1684.2460397800453, 10, 17525, 4016.9000000000015, 5079.9000000000015, 7281.950000000008, 9.028005564359644, 3.9678360764515306, 4.099631433034409], "isController": false}, {"data": ["reserve_req", 73208, 812, 1.109168396896514, 1819.7038984810408, 9, 15778, 4128.0, 5223.800000000003, 7355.890000000018, 18.082152667332405, 7.3914198902135615, 10.938423922963628], "isController": false}]}, function(index, item){
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
    createTable($("#errorsTable"), {"supportsControllersDiscrimination": false, "titles": ["Type of error", "Number of errors", "% in errors", "% in all samples"], "items": [{"data": ["500", 406, 23.784417106033978, 0.12316765362586157], "isController": false}, {"data": ["405", 812, 47.568834212067955, 0.24633530725172315], "isController": false}, {"data": ["Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 489, 28.646748681898067, 0.14834724784001554], "isController": false}]}, function(index, item){
        switch(index){
            case 2:
            case 3:
                item = item.toFixed(2) + '%';
                break;
        }
        return item;
    }, [[1, 1]]);

        // Create top5 errors by sampler
    createTable($("#top5ErrorsBySamplerTable"), {"supportsControllersDiscrimination": false, "overall": {"data": ["Total", 329632, 1707, "405", 812, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 489, "500", 406, null, null, null, null], "isController": false}, "titles": ["Sample", "#Samples", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors", "Error", "#Errors"], "items": [{"data": ["order_pass", 36566, 406, "405", 406, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["order_my_delete", 36533, 406, "405", 406, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["reserve_page", 36651, 43, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 43, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": [], "isController": false}, {"data": ["order_manage_page", 36586, 26, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 26, null, null, null, null, null, null, null, null], "isController": false}, {"data": ["ground_page", 36859, 420, "Non HTTP response code: java.net.SocketTimeoutException\/Non HTTP response message: Read timed out", 420, null, null, null, null, null, null, null, null], "isController": false}, {"data": [], "isController": false}, {"data": ["reserve_req", 36604, 406, "500", 406, null, null, null, null, null, null, null, null], "isController": false}]}, function(index, item){
        return item;
    }, [[0, 0]], 0);

});
