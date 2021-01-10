
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