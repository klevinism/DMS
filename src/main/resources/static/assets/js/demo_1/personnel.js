
var url = new URL(window.location);
var c = url.searchParams.get("modal");

if(c != null){
	document.getElementById("myModal").style.display = "block";
	console.log("MODAL IS HERE");
}else{
	console.log("NO MODAL");
}


function submitEditPersonnel(id){
	console.log(id);
	document.getElementById("editPersonnel"+id).submit();
}

function selectPersonnel(id){
	clearSelected();
	document.getElementById('accountRadioCheck' + id).checked  = true;
	console.log(document.getElementById('actionButton' + id));
	document.getElementById('actionButton' + id).style.display = 'block';
}

function clearSelected(){
	var elements = document.getElementsByName("actionButton");

    for(var i = 0; i < elements.length; i++){
      elements[i].style.display = "none";
    }
}