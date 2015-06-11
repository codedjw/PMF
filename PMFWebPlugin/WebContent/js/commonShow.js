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