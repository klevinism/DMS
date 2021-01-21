function onClickTooth(element){
	var selectedToothId = $(element).attr('id');
	
	selectSingleTooth(element);

	if($('option[id = "'+selectedToothId+'"]') != null){
		var option = $("option[id = "+selectedToothId+"]");
		$(option).attr("selected", "selected");
		var selected_option = $('#toothSelect option:selected');
		
		$("#selectedToothInput").val("");
		$("#selectedToothInput").val($('#toothSelect option:selected').val());
		$("#dentalRecordForm").show();

	}
		
}

function selectSingleTooth(element){
	clearAllTeethSelection();
	element.setAttribute("fill-opacity","0.6");
}

function onRightClickTooth(element){
	clearAllTeethSelection();
	$("#dentalRecordForm").hide();
}

function clearAllTeethSelection(){
	$("[name='tooth']").each(function(index, tooth){
		tooth.setAttribute("fill-opacity", 0);
	});

	$("option[name='selectedToothOption']").each(function(index, toothOption){
		$(toothOption).removeAttr("selected");
	});
	
}

var elems= document.getElementsByName("tooth");
for(var x=0; x<elems.length; x++){
   elems[x].addEventListener('contextmenu', function(e) {
   	//here you draw your right click menu
	   onRightClickTooth(this);
	   e.preventDefault();
   }, false);
}
