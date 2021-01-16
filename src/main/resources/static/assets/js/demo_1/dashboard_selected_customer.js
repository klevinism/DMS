function hoverRecord(element){
	if(element != null){
		$("path").each(function(index, val){
			if($(val).attr("data-tooth")==element){
				$(val).attr("fill","#4e4e4e");
				$(val).attr("fill-opacity","0.6");
			}
		});   
	}
}

function clearRecordSelection(){
	$("path").each(function(e, elem){
		$(elem).attr("fill-opacity","0");
	});	
}