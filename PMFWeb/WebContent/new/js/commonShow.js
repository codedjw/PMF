function resize() {
	width = $("#graph").width(), height = $("#graph").height();
	svg.attr("width", width).attr("height", height);
	svgg.attr("width", width).attr("height", height);
	force.size([width, height]).resume();
}

function tick() {
//	console.log(status);
	if (status === "AlphaMiner") {
	} else if (status === "HoWMiner") {
//		link.attr("d", linkArc);
	} else {
	}
	
	link.attr("x1", function(d) { return d.source.x; })
    .attr("y1", function(d) { return d.source.y; })
    .attr("x2", function(d) { return d.target.x; })
    .attr("y2", function(d) { return d.target.y; });

	  node.attr("transform", function (d) {
	      return "translate(" + d.x + "," + d.y + ")";
	  });
}

function onZoomChanged() {
    svgg.attr("transform", "translate(" + d3.event.translate + ")" + " scale(" + d3.event.scale + ")");
  }

function checkExtension(filename) {

    // Use a regular expression to trim everything before final dot
    var extension = filename.replace(/^.*\./, '');

    // Iff there is no dot anywhere in filename, we would have extension == filename,
    // so we account for this possibility now
    if (extension == filename) {
        extension = '';
    } else {
        // if there is an extension, we convert to lower case
        // (N.B. this conversion will not effect the value of the extension
        // on the file upload.)
        extension = extension.toLowerCase();
    }

    switch (extension) {
        case 'xes':
        	$("#warning").hide();
        	return true;
        	break;
        default:
        	$("#warning").show();
        	return false;
        	break;
    }
}