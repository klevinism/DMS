var horizontalVisitsBar;
var horizontalAppointmentBar;
var horizontalRevenueBar;

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
                     backgroundColor: '#d567f98c',
                     borderColor: '#b70cef',
                     data: thisYearCustomerValues
                 }]
             },
    
             // Configuration options go here
             options: tempOpt
		});
	}

	if(document.getElementById('totalVisitsLineGraph') !=null){
		var start = new Date();
        start.setMonth(0);
        var end = new Date();
        end.setMonth(11);
        end.setDate(31);
         
		var ctx = document.getElementById('totalVisitsLineGraph').getContext('2d');
		// For a line chart
				
		var totalVisitslineChart = new Chart(ctx, {
		    type: 'line',
	
	         // The data for our dataset
	         data: {
                 labels: getMonthDateNamesBetween(start, new Date()),
                 datasets: [{
                     label: thisYearVisitsLabel,
                     backgroundColor: '#037a9775',
                     borderColor: '#037a97',
                     data: thisYearVisitsValues
                 }]
             },
    
             // Configuration options go here
             options: lineGraphCardOptions
		});
	}

	if(document.getElementById('totalRevenueLineGraph') !=null){
		var start = new Date();
        start.setMonth(0);
        var end = new Date();
        end.setMonth(11);
        end.setDate(31);
         
		var ctx = document.getElementById('totalRevenueLineGraph').getContext('2d');
		// For a line chart
				
		var totalRevenuelineChart = new Chart(ctx, {
		    type: 'line',
	
	         // The data for our dataset
	         data: {
                 labels: getMonthDateNamesBetween(start, new Date()),
                 datasets: [{
                     label: thisYearRevenueLabel,
                     backgroundColor: '#56ff0080',
                     borderColor: '#4dda05',
                     data: thisYearRevenueValues
                 }]
             },
    
             // Configuration options go here
             options: lineGraphCardOptions
		});
	}

	if(document.getElementById('totalNoShowsLineGraph') !=null){
		var start = new Date();
        start.setMonth(0);
        var end = new Date();
        end.setMonth(11);
        end.setDate(31);
         
		var ctx = document.getElementById('totalNoShowsLineGraph').getContext('2d');
		// For a line chart
				
		var totalNoShowslineChart = new Chart(ctx, {
		    type: 'line',
	
	         // The data for our dataset
	         data: {
                 labels: getMonthDateNamesBetween(start, new Date()),
                 datasets: [{
                     label: noShowName,
                     backgroundColor: '#944c037d',
                     borderColor: '#944c03',
                     data: thisYearNoShowsValues
                 }]
             },
    
             // Configuration options go here
             options: lineGraphCardOptions
		});
	}

	if(document.getElementById('pieCustomerChart') !=null){
		var ctx = document.getElementById('pieCustomerChart').getContext('2d');
		var dataArray = [allCustomer["newcustomers"], allCustomer["oldcustomers"]];	
		var sortedArray = dataArray.sort(function(a, b) {
		  return b-a;
		});
		
		console.log("sortedArray", sortedArray);
		// For a pie chart
		var myPieChart = new Chart(ctx, {
		    type: 'doughnut',
		    data: {
		    	labels: customerLabels,
		    	datasets: [{
		    		backgroundColor: [getRandomColor(),getRandomColor()],
			        data: sortedArray 
			    }],				    
		    },
            options: {
            }
		});
	}
	
	if(document.getElementById('RevenueBarGraph') !=null){
		 var selectedDate = $("#appointmentPicker").val().trim().split("-");
		 var from = selectedDate[0].split("/");
		 var dateFromObject = new Date(""+from[2], from[1] - 1, ""+from[0]);
		 var to = selectedDate[1].split("/");
		 var dateToObject = new Date(""+to[2], to[1] - 1, ""+to[0]);  
	
		 var lblNames = getAllChartLabels(dateFromObject, dateToObject);
	  	 var ctx = document.getElementById('RevenueBarGraph').getContext('2d');
	  	 
	  	 var color1 = getRandomColor();
	  	 
	  	 var totalRevenue = 0;
	  	 personnelRevenue.forEach(function(e){
	  	 	totalRevenue += Number.parseInt(e);
	  	 });

		 $("#revenueBarTotalCount").find("div").css("background-color", color1);
		 $("#revenueBarTotalCount").find("h4").text(totalRevenue + " " + currencyAll);
		 
	  	 horizontalRevenueBar = new Chart(ctx, {
	         // The type of chart we want to create
	         type: 'horizontalBar',
	
	         // The data for our dataset
	         data: {
	            labels: lblNames,
	            datasets: [{
	            	label: revenueName,
		    		backgroundColor: color1,
			        data: personnelRevenue
			    }],
	         },
		
	         // Configuration options go here
             options: lineGraphCardOptions
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
	  	 
	  	 var color1 = getRandomColor();
		 var color2 = getRandomColor();
	  	 
	  	 var totalVisits = 0;
	  	 personnelRecordsList.forEach(function(e){
	  	 	totalVisits += Number.parseInt(e);
	  	 });
	  	 
	  	 var totalAppointments = 0;
	  	 personnelAppointmentList.forEach(function(e){
	  	 	totalAppointments += Number.parseInt(e);
	  	 });

		 $("#visitsBarTotalCount").find("div").css("background-color", color1);
		 $("#appointmentsBarTotalCount").find("div").css("background-color", color2);
		 $("#visitsBarTotalCount").find("h4").text(totalVisits);
		 $("#appointmentsBarTotalCount").find("h4").text(totalAppointments);
		 
	  	 horizontalAppointmentBar = new Chart(ctx, {
	         // The type of chart we want to create
	         type: 'horizontalBar',
	
	         // The data for our dataset
	         data: {
	            labels: lblNames,
	            datasets: buildDataset([visitsName], personnelRecordsList, color1).concat(buildDataset([appointmentsName], personnelAppointmentList, color2))
	         },
		
	         // Configuration options go here
             options: lineGraphCardOptions
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

function refreshHorizontalRevenueBarChart(data, date){
	var selectedDates = date;
	var selectedData = data;
	
	var from = selectedDates[0].split("/");
	var dateFromObject = new Date(""+from[2], from[1] - 1, ""+from[0]);
	var to = selectedDates[1].split("/");
	var dateToObject = new Date(""+to[2], to[1] - 1, ""+to[0]);  
		
	var allLabels = getAllChartLabels(dateFromObject, dateToObject);
	removeData(horizontalRevenueBar)
	if(horizontalRevenueBar != null){
		addData(horizontalRevenueBar, allLabels, reloadRevenueDataset(selectedData));
	}
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
}