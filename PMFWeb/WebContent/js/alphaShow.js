function displayAlphaResults(root) {
	
	force.gravity(.05)
		.linkDistance(80)
		.charge(-800);

	var r = 30, s = 50;
	
	svgg.selectAll("*").remove();

	svgg.append("svg:defs").append("marker")
	.attr("id", "arrowhead")
	.attr("refX", 17 + 3) //must be smarter way to calculate shift
	.attr("refY", 2)
	.attr("markerWidth", 6)
	.attr("markerHeight", 4)
	.attr("orient", "auto")
	.append("path")
	.attr("d", "M 0,0 V 4 L6,2 Z"); //this is actual shape for arrowhead

	// Markers
    /* svgg.append("svg:defs").selectAll("marker")
        .data(['regular'])
      .enter().append("svg:marker")
        .attr("id", String)
        .attr("viewBox", "0 -5 10 10")
        .attr("refX", 15)
        .attr("refY", -1.5)
        .attr("markerWidth", 6)
        .attr("markerHeight", 6)
        .attr("orient", "auto")
      .append("svg:path")
        .attr("d", "M0,-5L10,0L0,5"); */
	
	link = svgg.append('svg:g').selectAll(".link");
	node = svgg.append('svg:g').selectAll(".node");
	
	var nodes = root.nodes;
	var links = root.links;

	// Restart the force layout
	force.nodes(nodes)
		.links(links)
		.start();

	// Update the links…
	link = link.data(links);

	// Exit any old links.
	link.exit().remove();

	// Enter any new links.
	link.enter().insert("line", ".node")
	    .attr("class", "link")
	    .attr("x1", function(d) { return d.source.x; })
	    .attr("y1", function(d) { return d.source.y; })
	    .attr("x2", function(d) { return d.target.x; })
	    .attr("y2", function(d) { return d.target.y; })
	    .attr("marker-end", function (d) {
    		if (d.type == 1) {
        		return "url(#arrowhead)"
    		} else {
        		return " "
    	   	};
	    });

	// Update the nodes…
    node = node.data(nodes);

	// Exit any old nodes.
	node.exit().remove();

	var drag = force.drag()
	  				.on("dragstart", function(d,i){
	  					//拖拽开始后设定被拖拽对象为固定
	  					// solve zoom and drag conflict
	  					d3.event.sourceEvent.stopPropagation();
		  				d.fixed = true;
		  			})
		  			.on("dragend", function(d,i){
						d.fixed = ($('#dragchoice').val() == 0) ? false : true;
						d3.select(this).selectAll(".node").classed("fixed", d.fixed);
			  			});
	  
	node.enter().append("svg:g")
	  	.attr("class", function(d){
			if (d.type == "PN_TRANSITION") {
				return "transition node";
			} else {
				return "circle node";
			}
	     })
		 .call(drag);

	d3.selectAll(".transition").append("rect")
	  	.attr("x", function(d) { return -s/2; })
    	.attr("y", function(d) { return -s/2; })
    	.attr("width", s)
    	.attr("height", s)
    	.attr("class", "node type"+0);

	d3.selectAll(".circle").append("circle")
	  	.attr("r", r)
  		.attr("class", "node type"+1);

	node.append("svg:text")
	    .attr("class", "node text")
	    .attr("dx", 0)
	    .attr("dy", ".50em")
	    .attr("text-anchor", "middle")
	    .text(function (d) {
	    return d.label;
		});

    //标记鼠标悬停的标签
	node.append("title")
	    .text(function(d) {
			  return d.detail; 
	 });

	d3.select(window).on("resize", resize);
}