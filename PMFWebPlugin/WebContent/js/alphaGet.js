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
				showResultIn3DC();
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