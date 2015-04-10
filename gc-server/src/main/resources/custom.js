var ctx = document.getElementById("linechart").getContext("2d");


var labels = [];
var data = [];
for (var i = 1; i <= 60; i++) {
    labels.push(i);
    data.push(0);
}

var data = {
    labels: labels,
    datasets: [
        {
            label: "Committed",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: data
        },
        {
            label: "Init",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: data
        },
        {
            label: "Max",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: data
        },
        {
            label: "Used",
            fillColor: "rgba(220,220,220,0.2)",
            strokeColor: "rgba(220,220,220,1)",
            pointColor: "rgba(220,220,220,1)",
            pointStrokeColor: "#fff",
            pointHighlightFill: "#fff",
            pointHighlightStroke: "rgba(220,220,220,1)",
            data: data
        }
    ]
};

var options = {};
var myLineChart = new Chart(ctx).Line(data, options);

setInterval(function() {
    $.get("/data", function(data) {
        for (var i = 0; i < data.length; i++) {
            var obj = data[i];
            myLineChart.datasets[0].points[i].value = obj.committed;
            myLineChart.datasets[1].points[i].value = obj.init;
            myLineChart.datasets[2].points[i].value = obj.max;
            myLineChart.datasets[3].points[i].value = obj.used;
        }
        myLineChart.update();
    });
}, 1000);