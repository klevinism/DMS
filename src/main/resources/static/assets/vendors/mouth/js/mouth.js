function onClickTooth(element){
	element.setAttribute("fill-opacity","1");
	$("#context-menu").removeClass("show").hide();
	
}

function onRightClickTooth(element){
	element.setAttribute("fill-opacity","0");
}

var elems= document.getElementsByName("tooth");
for(var x=0; x<elems.length; x++){
   elems[x].addEventListener('contextmenu', function(e) {
   	//here you draw your right click menu
	   onRightClickTooth(this);
	   e.preventDefault();
   }, false);
}
