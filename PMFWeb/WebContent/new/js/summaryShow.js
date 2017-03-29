var hasHChart = false;

function show3DChart(divId, titleText, xCat, yTitleText, seriesName, seriesData, unit) {
	hasHChart = true;
	var chart;
	chart = new Highcharts.Chart({
        chart: {
        	renderTo: divId,
            type: 'column',
//            margin: 75,
            options3d: {
                enabled: true,
                alpha: 10,
                beta: 25,
                depth: 70
            }
        },
        title: {
            text: titleText
        },
        subtitle: {
            text: 'Source: www.pmf.org'
        },
        plotOptions: {
            column: {
                depth: 25
            }
        },
        xAxis: {
            categories: xCat
        },
        yAxis: {
            title: {
                text: yTitleText
            }
        },
        tooltip: {
			formatter: function() {
	                return '<b>'+ this.series.name +'</b><br/>'+
					this.x +': '+ this.y + unit;
			}
		},
		credits: {
    		enabled: false // remove high chart logo hyper-link
    	},
        series: [{
            name: seriesName,
            data: seriesData
        }]
    });
}

function destroyHChart(divId) {
	if (hasHChart === true) {
		var chart = $('#'+divId).highcharts(); //access from id
		chart.destroy();
	}
}

function showChart (divId, titleText, xCat, yTitleText, seriesName, seriesData, unit) {
	hasHChart = true;
	var chart;
	chart = new Highcharts.Chart({
		chart: {
			renderTo: divId,
			defaultSeriesType: 'line'
//			marginRight: 130,
//			marginBottom: 25
		},
		title: {
			text: titleText,
			x: -20 //center
		},
		subtitle: {
			text: 'Source: www.pmf.org',
			x: -20
		},
		xAxis: {
			categories: xCat
		},
		yAxis: {
			title: {
				text: yTitleText
			},
			plotLines: [{
				value: 0,
				width: 1,
				color: '#808080'
			}]
		},
		tooltip: {
			formatter: function() {
	                return '<b>'+ this.series.name +'</b><br/>'+
					this.x +': '+ this.y + unit;
			}
		},
		legend: {
			layout: 'vertical',
			align: 'right',
			verticalAlign: 'top',
			x: -10,
			y: 100,
			borderWidth: 0
		},
		credits: {
    		enabled: false // remove high chart logo hyper-link
    	},
		series: [{
			name: seriesName,
			data: seriesData
		}]
	});
}

function showResultInTbl(classifier) {
	if (classifier == null || classifier == undefined) classifier = "";
	var loghtml = "<table class='table table-striped'>";
	loghtml += ("<tr>"+"<th>Event Classes"+classifier+"</th>"+"<th>Frequencies</th>"+"</tr>");
	$.each(logResult, function(idx, logitem){
		loghtml += "<tr>";
		loghtml += ("<td>"+logitem.EventClass+"</td>");
		loghtml += ("<td>"+logitem.Frequency+"</td>");
		loghtml += "</tr>";
		});
	loghtml += "</table>";
	$('#logsummary').html(loghtml);
}

function showResultIn3DC(classifier) {
	if (classifier == null || classifier == undefined) classifier = "";
//	destroyHChart('line-chart');
	var eventClasses = [], frequencies = [];
	$.each(logResult, function(idx, logitem){
		eventClasses.push(logitem.EventClass);
		frequencies.push(logitem.Frequency);
	});
	show3DChart('3d-chart', 'Frequencies of Event Classes'+classifier, eventClasses, 'Frequency', 'Event Class', frequencies, '');
}

function showResultInLC(classifier) {
	if (classifier == null || classifier == undefined) classifier = "";
//	destroyHChart('3d-chart');
	var eventClasses = [], frequencies = [];
	$.each(logResult, function(idx, logitem){
		eventClasses.push(logitem.EventClass);
		frequencies.push(logitem.Frequency);
	});
	showChart('line-chart', 'Frequencies of Event Classes'+classifier, eventClasses, 'Frequency', 'Event Class', frequencies, '');
}