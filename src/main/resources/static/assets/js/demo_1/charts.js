var horizontalVisitsBar;
var horizontalAppointmentBar;
function initCharts(){
	if(document.getElementById('lineCustomerChart') !=null){
		var start = new Date();
        start.setMonth(0);
        var end = new Date();
        end.setMonth(11);
        end.setDate(31);
         
		var ctx = document.getElementById('lineCustomerChart').getContext('2d');
		// For a line chart
		var tempOpt = lineGraphCardOptions;
		tempOpt.scales.yAxes[0].display = true;
		
		var mylineChart = new Chart(ctx, {
		    type: 'line',
	
	         // The data for our dataset
	         data: {
                 labels: getMonthDateNamesBetween(start, new Date()),
                 datasets: [{
                     label: thisYearCustomerLabel,
                     backgroundColor: 'red',
                     borderColor: 'red',
                     data: thisYearCustomerValues
                 }]
             },
    
             // Configuration options go here
             options: tempOpt
		});
	}


	if(document.getElementById('pieCustomerChart') !=null){
		var ctx = document.getElementById('pieCustomerChart').getContext('2d');
		// For a pie chart
		var myPieChart = new Chart(ctx, {
		    type: 'pie',
		    data: {
		    	labels: customerLabels,
		    	datasets: [{
		    		backgroundColor: [getRandomColor(),getRandomColor()],
			        data: [allCustomer["newcustomers"], allCustomer["oldcustomers"]]
			    }],
						    
		    },
            options: {
            }
		});
	}
	
	if(document.getElementById('horizontalAppointmentsBar') !=null){
		 var selectedDate = $("#appointmentPicker").val().trim().split("-");
		 var from = selectedDate[0].split("/");
		 var dateFromObject = new Date(""+from[2], from[1] - 1, ""+from[0]);
		 var to = selectedDate[1].split("/");
		 var dateToObject = new Date(""+to[2], to[1] - 1, ""+to[0]);  
	
		 var lblNames = getAllChartLabels(dateFromObject, dateToObject);
	  	 var ctx = document.getElementById('horizontalAppointmentsBar').getContext('2d');
	  	 horizontalAppointmentBar = new Chart(ctx, {
	         // The type of chart we want to create
	         type: 'horizontalBar',
	
	         // The data for our dataset
	         data: {
	            labels: lblNames,
	            datasets: buildDataset([visitsName], personnelRecordsList).concat(buildDataset([appointmentsName], personnelAppointmentList))
	         },
		
	         // Configuration options go here
             options: {
             	scales: {
			        xAxes: [{
			            ticks: {
			                beginAtZero: true,
			                precision:0
			            }
			        }]
			    }
             }
          });
	}
	
	
}

function addData(chart, label, data) {
	chart.data.labels = label;
    chart.data.datasets.push(data[0]);
    chart.update();
}

function removeData(chart) {
    chart.data.labels = [];
    chart.data.datasets = [];
    chart.update();
}

function refreshHorizontalAppointmentBarChart(data, date){
	var selectedDates = date;
	var selectedData = data;
	
	var from = selectedDates[0].split("/");
	var dateFromObject = new Date(""+from[2], from[1] - 1, ""+from[0]);
	var to = selectedDates[1].split("/");
	var dateToObject = new Date(""+to[2], to[1] - 1, ""+to[0]);  
		
	var allLabels = getAllChartLabels(dateFromObject, dateToObject);
	
	if(horizontalAppointmentBar != null){
		addData(horizontalAppointmentBar, allLabels, reloadDataset(selectedData));
	}
	
	return horizontalVisitsBar;
}