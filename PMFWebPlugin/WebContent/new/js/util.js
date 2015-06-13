function checkExtension(filename, type, idwarning) {

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
        case type:
        	$(idwarning).hide();
        	return true;
        	break;
        default:
        	$(idwarning).show();
        	return false;
        	break;
    }
}

function getCatPluginHtml() {
	$.ajax({
		url : "PluginMgmServlet?op=4",
		type : "POST",
		dataType : "json",
		contentType: "application/x-www-form-urlencoded; charset=utf-8", 
		success : function(json) {
			// send back a json object
			if (json.status == "OK") {
				updateCateTree(json);
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

function updateCateTree(json) {
	if (json.cateTree == null || json.cateTree == undefined) {
		$("#main-sidebar .cate-tree").remove();
	} else {
		$.each(json.cateTree, function(idx, cate){
			$.each(cate, function(catName, plugins) {
				$li_cat = $("<li class=\"treeview cate-tree\">"+
            				"<a href=\"javascript:void(0)\">"+
            				"<i class=\"fa fa-folder\"></i>"+
            				"<span>"+catName+"</span>"+
            				"<i class=\"fa fa-angle-left pull-right\"></i>"+
        					"</a>"+
        					"</li>");
				$ul_cat = $("<ul class=\"treeview-menu\">"+"</ul>");
				$.each(plugins, function(iidx, plugin){
					$li_plugin = $("<li><a href=\""+plugin.pageName+"\"><i class=\"fa fa-angle-double-right\"></i>"+plugin.pluginName+"</a></li>");
					$ul_cat.append($li_plugin);
				});
				$li_cat.append($ul_cat);
				$("#main-sidebar").append($li_cat);
			});
		});
	}
	/* Sidebar tree view VIP!!! */
    $("#main-sidebar .treeview").tree();
}