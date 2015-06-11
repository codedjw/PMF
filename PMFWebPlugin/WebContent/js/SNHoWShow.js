function displaySNHoWResults(root) {
	
	force.gravity(1)
		.linkDistance(150)
		.charge(-3000);
	
	var r = 6, s = 50;

	svgg.selectAll("*").remove();

//	svgg.append("svg:defs").append("marker")
//	.attr("id", "arrowhead")
//	.attr("refX", 17 + 3) //must be smarter way to calculate shift
//	.attr("refY", 2)
//	.attr("markerWidth", 6)
//	.attr("markerHeight", 4)
//	.attr("orient", "auto")
//	.append("path")
//	.attr("d", "M 0,0 V 4 L6,2 Z"); //this is actual shape for arrowhead
	
	var markerStyle = [
	                   {id: 0, name: "arrow_normal", color: "#BBBBBB"}, 
	                   {id: 1, name:"arrow_red", color:"red"}
	                   ];
	
	svgg.append("svg:defs").selectAll("marker")
	.data(markerStyle)
	.enter()
	.append("svg:marker")
	.attr("id", function(d){ return 'marker_' + d.name})
    .attr("viewBox","0 -5 10 10")
    .attr("refX","24")
    .attr("refY","0")
    .attr("markerUnits","strokeWidth")
    .attr("markerWidth","9")
    .attr("markerHeight","5")
    .attr("orient","auto")
    .append("svg:path")
    .attr("d","M 0 -5 L 10 0 L 0 5 z")
    .attr("fill", function(d){return d.color});
	
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
						d3.select(this).selectAll(".snnode").classed("fixed", d.fixed);
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

	var linkedByIndex = {};
	var neighbors = {};
	var nodessize = root.nodes.length;
	var max = 0;
	for (var i=0; i<nodessize; i++) {
		neighbors[i] = 0;
		}
	var lmax = 0;
    root.links.forEach(function(d) {
        linkedByIndex[d.source.index + "," + d.target.index] = 1;
        neighbors[d.source.index]++;
        neighbors[d.target.index]++;
        var val = d.detail*1;
        lmax = (lmax < val ? val : lmax);
    });
    for (var i=0; i<nodessize; i++) {
		max = (max < neighbors[i] ? neighbors[i] : max);
		}
    

	d3.selectAll(".transition").append("rect")
	  	.attr("x", function(d) { return -s/2; })
    	.attr("y", function(d) { return -s/2; })
    	.attr("width", s)
    	.attr("height", s)
    	.attr("class", "snnode type"+0);

	d3.selectAll(".circle").append("circle")
	  	.attr("r", function(d) {
				if (max == 0) return r/2;
				else return (r/2+(neighbors[d.index]/max*r/2));
		  	})
  		.attr("class", "snnode type"+1);

	// Enter any new links.
	link.enter().append("line")
	    .attr("class", "snlink")
	    .attr("marker-end", "url(#marker_arrow_normal)")
	    .style("stroke-width", function(d){
		var val = d.detail*1;
		if (lmax == 0) return 0.8;
		else return (0.8+(val/lmax*1));
    })

	node.append("svg:text")
	    .attr("class", "snnode text")
	    .attr("x", 8)
	    .attr("y", ".31em")
	    .text(function (d) {
	    return d.label;
		});

    //标记鼠标悬停的标签
	node.append("title")
	    .text(function(d) {
			  return d.detail; 
	 });
	/* link.append("title")
    .text(function(d) {
    	  var val = d.detail*1;
		  return val.toFixed(5)+""; 
 	}); */

	node.on("mouseover", fade(.2,"red"))
    .on("mouseout", fade(1));

	/* link.on("mouseover", linkfade(.2,"red"))
    .on("mouseout", linkfade(1)); */

    function isConnected(a, b) {
        return linkedByIndex[a.index + "," + b.index] || /* linkedByIndex[b.index + "," + a.index] || */ a.index == b.index;
    }

    function linkfade(opacity, color) {
			return function(d) {
				 link.style("stroke-opacity", function(o){ return o === d ? 1 : opacity;})
	                 .style("stroke", function(o){ return o === d ? color : "#c0c0c0";});
				 node.style("stroke-opacity", function(o){ 
					 var opa = (d.source === o || d.target === o ? 1 : opacity);
					 this.setAttribute('fill-opacity', opa);
					 return opa;
					 });
				}
        }

        function fade(opacity,color) {
            return function(d) {
        
         node.style("stroke-opacity", function(o) {
            thisOpacity = isConnected(d, o) ? 1 : opacity;
            this.setAttribute('fill-opacity', thisOpacity);
            return thisOpacity;
        });

                link.style("opacity", function(o) {
                    return o.source === d /* || o.target === d */ ? 1 : opacity;
                })
                
                .style("stroke", function(o) {
                    return o.source === d /* || o.target === d */ ? color : "#c0c0c0" ;
                })
                .attr("marker-end", function(o){
                	return color === "red" && (o.source === d /* || o.target === d */) ? "url(#marker_arrow_red)" : "url(#marker_arrow_normal)" ;
                });
            };
            
            }

	d3.select(window).on("resize", resize);
}

function linkArc(d) {
	var dx = d.target.x - d.source.x,
    dy = d.target.y - d.source.y,
    dr = Math.sqrt(dx * dx + dy * dy);
	return "M" + 
    	d.source.x + "," + 
    	d.source.y + "A" + 
    	dr + "," + dr + " 0 0,1 " + 
    	d.target.x + "," + 
    	d.target.y;
}