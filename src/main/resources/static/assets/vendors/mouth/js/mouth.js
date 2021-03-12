function onClickTooth(element){
	var selectedToothId = $(element).attr("id");
	var multiple = $("#multipleTeeth").is(':checked');
	selectSingleTooth(element, !multiple);

	if($('#toothSelect').find('option[id = "'+selectedToothId+'"]') != null){
		var option = $("option[id = "+selectedToothId+"]");
		$(option).attr("selected", true);
		$('#toothSelect').trigger("change");
		
		addHiddenInput($(option).val());		
		
		$("#dentalRecordForm").show();
	}
		
}

function selectSingleTooth(element, clearAll){
	if(clearAll){
		clearAllTeethSelection();
	}
	
	element.setAttribute("fill-opacity","0.6");
}

function onRightClickTooth(element, clearAll){
	element.setAttribute("fill-opacity","0");
	
	var id = $(element).attr("id");
	var option = $("#toothSelect").find("#"+id); 
	
	$(option).removeAttr("selected");
	$("#toothSelect").trigger("change");
	
	removeHiddenInput($(option).val());
	
	if(clearAll || $("#toothSelect").select2('data').length == 0 ){
		clearAllTeethSelection();
		$("#dentalRecordForm").hide();
	}
}

function clearAllTeethSelection(){
	$("[name='tooth']").each(function(index, tooth){
		tooth.setAttribute("fill-opacity", 0);
	});

	$("option[name='selectedToothOption']").each(function(index, toothOption){
		$(toothOption).removeAttr("selected");
	});
	
	$('#toothSelect').trigger("change");
}

function select(type){
	var multiple = $("#multipleTeeth").is(':checked');

	resetHiddenInput();

	if(type == 'all'){
		var elem = $(".dentalFace path");
		elem.each(function(el){
			selectSingleTooth(elem[el], false);
			$("#dentalRecordForm").show();
		});
		if(!multiple){
			 $("#multipleTeeth").click();
		}else{
			refreshSelect2();
		}
	}else if(type == 'none'){
		clearAllTeethSelection();
		$("#dentalRecordForm").hide();
		if(multiple){
			 $("#multipleTeeth").click();
		}else{
			refreshSelect2();
		}
	}
		
}

function refreshSelect2(){
	var val= $("#toothSelect").find(":selected");
	var val = val === Array ? val[0]:val; 
	$("[name='tooth']").each(function(index, tooth){
		if($(tooth).attr("fill-opacity") > 0 && $(tooth).attr("id") != val.attr("id")){
		  $("#toothSelect").find("#"+$(tooth).attr("id")).attr("selected",true);
		  $("#toothSelect").trigger("change");
		  addHiddenInput($("#toothSelect").find("#"+$(tooth).attr("id")).val());
		}
	});
}

var elems= document.getElementsByName("tooth");
for(var x=0; x<elems.length; x++){
   elems[x].addEventListener('contextmenu', function(e) {
   	//here you draw your right click menu
   	   var multiple = $("#multipleTeeth").is(':checked');
	   
	   onRightClickTooth(this, !multiple);
	   
	   e.preventDefault();
   }, false);
}
$(document).ready(function() {
	$('#toothSelect').select2();
	var val= $("#toothSelect").find(":selected");
	if(val.val() != null){
		$("#hiddenSelectedTeeth").find("input:first").val(val.val());
	}
});


function addHiddenInput(value){
	var multiple = $("#multipleTeeth").is(':checked');
	var firstInput = $("#hiddenSelectedTeeth").find("input:first");
	var lastInput = $("#hiddenSelectedTeeth").find("input:last");
	var incr = 0;
	
	if(!multiple){
		lastInput = firstInput;
		$("#hiddenSelectedTeeth").html('');
	}else{
		incr = 1;
	}
	
	var lastIName = $(lastInput).attr("name");
	var lastIId = $(lastInput).attr("id");
	
	var name = lastIName.substring(0, lastIName.indexOf('['));
	
	var currIndex = lastIName.substring(lastIName.indexOf('[')+1, lastIName.indexOf(']'));
	
	var currIndex = parseInt(currIndex) + incr;
	
	var input = '<input type="hidden" name="'+name+'['+currIndex+'].name" id="'+name+''+currIndex+'.name" value="'+value+'" >';
	
	$("#hiddenSelectedTeeth").append(input);
}

function removeHiddenInput(value){
	var selectedElem = $("#hiddenSelectedTeeth").find("input[value='"+value+"']");
	
	if($("#hiddenSelectedTeeth").find("input").length > 1){
		$(selectedElem).remove();
	}else{
		$(selectedElem).val("");
	}
}

function refreshHiddenInput(value){
	var firstInput = $("#hiddenSelectedTeeth").find("input:first");
	$("#hiddenSelectedTeeth").html("");
	$(firstInput).val(value);
	$("#hiddenSelectedTeeth").append(firstInput);
}

function resetHiddenInput(){
	var firstInput = $("#hiddenSelectedTeeth").find("input:first");
	$("#hiddenSelectedTeeth").html("");
	$("#hiddenSelectedTeeth").append(firstInput);
}