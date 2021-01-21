function getDateNamesBetween(from, to) {
    var monthNames = [ "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December" ];
        
    var arr = [];
    var datFrom = new Date(from);
    var datTo = new Date(to);
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();

    for (var i = datFrom.getMonth(); i <= diffYear; i++) {
        arr.push(monthNames[i%12]);
    }        
    
    return arr;
}

$(document).ready(function(){
	
	if(list.length > 0){
		 var ctx = document.getElementById('myChart').getContext('2d');
	     var chart = new Chart(ctx, {
	         // The type of chart we want to create
	         type: 'line',
	
	         // The data for our dataset
	         data: {
	             labels: getDateNamesBetween(new Date().setMonth(0), new Date()),
	             datasets: [{
	                 label: visitsName,
	                 backgroundColor: 'red',
	                 borderColor: 'red',
	                 data: list
	             }]
	         },
	
	         // Configuration options go here
	         options: {}
	     });
	 }
});