function doAlpha() {
	var json = {logid : $("#logchoice").val()};
	getAlphaResults(json);
}
var logResult;
function getAlphaResults(json) {
	var jsonstr = JSON.stringify(json);
	$.ajax({
		url : "AlphaTestServlet",
		data : {
			"logJson" : jsonstr
			},
		type : "POST",
		dataType : "json",
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		success : function(json) {
			// send back a json object
			if (json.status == "OK") {
				status = "AlphaMiner";
				$('#dragchoice').val("0");
				displayAlphaResults(json.result);
				logResult = json.log;
				showResultInTbl();
				showResultInLC();
				showResultIn3DC();
				// fix dimensions of chart that was in a hidden element
				jQuery(document).on( 'shown.bs.tab', 'a[data-toggle="tab"]', function (e) { // on tab selection event
				    jQuery( ".contains-chart" ).each(function() { // target each element with the .contains-chart class
				        var chart = jQuery(this).highcharts(); // target the chart itself
				        chart.reflow() // reflow that chart
				    });
				})
			}
 		},

		// code to run if the request fails;
		// the raw request and status codes are passed to the function
		error : function(xhr, status, errThrown) {
			alert("Sorry, there was a problem!");
			console.log("Error: " + errThrowm);
			console.log("Status: " + status);
			console.dir(xhr); 
		},

		// code to run regardless of success or failure
		complete :function(xhr, status) {
			//alert("The request is complete!");
		}
	}); 
}