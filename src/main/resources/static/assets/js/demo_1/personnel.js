
var url = new URL(window.location);
var isModal = url.searchParams.get("modal");
var action = url.searchParams.get("view");

$(document).ready(function(){
	if(isModal){
		$('#'+action.toLowerCase()+'Modal').modal('show');
	}
	
	var selectedPersonnel = $('input[name="accountRadio"]:checked');
	if(selectedPersonnel.val() != null)
		selectPersonnel( selectedPersonnel.val());
		
});

function submitEditPersonnel(id){
	document.getElementById("editPersonnel"+id).submit();
}

function selectPersonnel(id){
	clearSelected();
	document.getElementById('accountRadioCheck' + id).checked  = true;
	document.getElementById('actionButton' + id).style.display = 'block';
}

function clearSelected(){
	var elements = document.getElementsByName("actionButton");

    for(var i = 0; i < elements.length; i++){
      elements[i].style.display = "none";
    }
}

function resendConfirmation(){
	var selectedPersonnel = $('input[name="accountRadio"]:checked');
	var personnelId;
	if(selectedPersonnel != undefined || selectedPersonnel.val() != null){
		personnelId = selectedPersonnel.val();
	}
	if(personnelId != undefined || personnelId != null){
		reConfirm(personnelId,  function (data){
			if(data.error != "error"){
				$("#success").html(data.message);
				$("#success").show();
				setTimeout(function(){ $("#success").hide(); }, 5000);
			}else if(data.error === "error"){
				$("#error").html(data.message);
				$("#error").show();
				setTimeout(function(){ $("#error").hide(); }, 5000);
			}
		});
	}	
}

function reConfirm(personnelId, callback){
	$.post("/api/personnel/sendConfirmation", {"id" : personnelId}, callback);
}