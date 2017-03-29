function capm(models, index, type) {
	if (type == 2) {
		return capm2(models, index);
	} else if (type == 3) {
		return capm3(models, index);
	}
	return capm1(models, index);
}

var onlyChainSuccession = false;

function capm1(models, index) {
	var ret_value = {};
//	console.log(models.length);
	var targetModel;
	$.each(models, function(i, model){
		var idx = model["index"];
//		console.log(idx);
		if (idx == index) {
			targetModel = model;
			return false;
		}
		
	});
	var constraints = targetModel["model"];
	var tree = {};
	var templates = [];
	$.each(constraints, function(i, constraint){
		var first = constraint.first;
		var second = constraint.second;
		var cs = constraint.constraint.name;
		if (onlyChainSuccession && cs != "Chain_Succession") {
			return true;
		}
		if (templates.indexOf(cs) == -1) {
			templates.push(cs);
		}
//		console.log(first+" "+second+" "+cs);
		var cs_array;
		if (tree[cs]) {
			cs_array = tree[cs];
		} else {
			cs_array = [];
		}
		var pair = first + " --> " + second;
		cs_array.push(pair);
		tree[cs] = cs_array;
	});
//	console.log(tree);
	ret_value["name"] = "constraints";
	var ret_templates = [];
	$.each(templates, function(i, template){
		var ret_template = {};
		ret_template["name"] = template;
		var pairs = tree[template];
		var ret_pairs = [];
		$.each(pairs, function(i, pair){
			ret_pairs.push({name:pair});
		});
		ret_template["children"]=ret_pairs;
		ret_templates.push(ret_template);
	});
	ret_value["children"] = ret_templates;
	return ret_value;
}

function capm2(models, index) {
//	console.log(models.length);
	var targetModel;
	$.each(models, function(i, model){
		var idx = model["index"];
//		console.log(idx);
		if (idx == index) {
			targetModel = model;
			return false;
		}
	});
	var constraints = targetModel["model"];
	var links = [];
	$.each(constraints, function(i, constraint){
		var first = constraint.first;
		var second = constraint.second;
		var cs = constraint.constraint.name;
		if (onlyChainSuccession && cs != "Chain_Succession") {
			return true;
		}
		if (cs == "Alternate_Succession") {
			cs = "licensing";
		}
		if (cs == "Chain_Succession") {
			cs = "suit";
		}
		var item = {source:first, target:second, type:cs};
		if (links.indexOf(item) == -1) {
			links.push(item);
		}
	});
	return links;
}

function capm3(models, index) {
	var ret_value = {};
//	console.log(models.length);
	var targetModel;
	$.each(models, function(i, model){
		var idx = model["index"];
//		console.log(idx);
		if (idx == index) {
			targetModel = model;
			return false;
		}
		
	});
	var constraints = targetModel["model"];
	var sources = {};
	$.each(constraints, function(i, constraint){
		var first = constraint.first;
		var second = constraint.second;
		var cs = constraint.constraint.name;
		if (onlyChainSuccession && cs != "Chain_Succession") {
			return true;
		}
		var targets = {};
		if (sources[first]) {
			targets = sources[first];
		}
		var target_cs = [];
		if (targets[second]) {
			target_cs = targets[second];
		}
		if (target_cs.indexOf(cs) == -1) {
			target_cs.push(cs);
		}
		targets[second] = target_cs;
		sources[first] = targets;
	});
//	console.log(tree);
	ret_value["name"] = "activities";
	var ret_sources = [];
	$.each(sources, function(source, targets){
		var ret_source = {};
		ret_source["name"] = source;
		var ret_targets = [];
		$.each(targets, function(target, constraints){
			var ret_target = {};
			ret_target["name"] = target;
			var ret_constraints = [];
			$.each(constraints, function(i, cs){
				ret_constraints.push({name:cs});
			})
			ret_target["children"] = ret_constraints;
			ret_targets.push(ret_target);
		});
		ret_source["children"]=ret_targets;
		ret_sources.push(ret_source);
	});
	ret_value["children"] = ret_sources;
	console.log(ret_value);
	return ret_value;
}