var horizontalVisitsBar;
var horizontalAppointmentBar;
var horizontalRevenueBar;
var lineGraphCardOptions = { 
        responsive: true,
        elements: {
            point: {
                radius: 1.5
            },
            line: {
                tension: 0
            }
        },
        scales: {
            xAxes: [{
                display: false
            }],
            yAxes: [{
                display: false
            }]
        },
        legend: false,
        tooltips: {
            // Disable the on-canvas tooltip
            enabled: false,
            intersect: false,

            custom: function(tooltipModel) {
                // Tooltip Element
                var tooltipEl = document.getElementById('chartjs-tooltip');

                // Create element on first render
                if (!tooltipEl) {
                    tooltipEl = document.createElement('div');
                    tooltipEl.id = 'chartjs-tooltip';
                    tooltipEl.innerHTML = '<table></table>';
                    document.body.appendChild(tooltipEl);
                }

                // Hide if no tooltip
                if (tooltipModel.opacity === 0) {
                    tooltipEl.style.opacity = 0;
                    return;
                }

                // Set caret Position
                tooltipEl.classList.remove('above', 'below', 'no-transform');
                if (tooltipModel.yAlign) {
                    tooltipEl.classList.add(tooltipModel.yAlign);
                } else {
                    tooltipEl.classList.add('no-transform');
                }

                function getBody(bodyItem) {
                    return bodyItem.lines;
                }

                // Set Text
                if (tooltipModel.body) {
                    var titleLines = tooltipModel.title || [];
                    var bodyLines = tooltipModel.body.map(getBody);

                    var innerHtml = '<thead>';

                    titleLines.forEach(function(title) {
                        innerHtml += '<tr><th>' + title + '</th></tr>';
                    });
                    innerHtml += '</thead><tbody>';

                    bodyLines.forEach(function(body, i) {
                        var text = body[0].split(":");
                        var body = text[1].length > 3 ? text[0]+ " " +text[1].replace(/\B(?=(\d{3})+(?!\d))/g, ",") : text.join(" "); 
                        
                        var colors = tooltipModel.labelColors[i];
                        var style = 'background:' + colors.backgroundColor;
                        style += '; border-color:' + colors.borderColor;
                        style += '; border-width: 2px';
                        var span = '<span style="' + style + '"></span>';
                        innerHtml += '<tr><td>' + span + body + '</td></tr>';
                    });
                    innerHtml += '</tbody>';

                    var tableRoot = tooltipEl.querySelector('table');
                    tableRoot.innerHTML = innerHtml;
                }

                // `this` will be the overall tooltip
                var position = this._chart.canvas.getBoundingClientRect();
                var width= Math.max( document.body.scrollWidth, document.body.offsetWidth);
                
                // Display, position, and set styles for font
                tooltipEl.style.opacity = 1;
                tooltipEl.style.position = 'absolute';
                if((position.right + window.pageXOffset + tooltipModel.caretX)+50 >= window.innerWidth){
                    tooltipEl.style.left = (position.left + window.pageXOffset + tooltipModel.caretX) - (this._view.width)  + 'px';    
                }else{
                    tooltipEl.style.left = position.left + window.pageXOffset + tooltipModel.caretX + 'px';
                }
                
                tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px';
                tooltipEl.style.fontFamily = tooltipModel._bodyFontFamily;
                tooltipEl.style.fontSize = tooltipModel.bodyFontSize + 'px';
                tooltipEl.style.fontStyle = tooltipModel._bodyFontStyle;
                tooltipEl.style.padding = tooltipModel.yPadding + 'px ' + tooltipModel.xPadding + 'px';
                tooltipEl.style.pointerEvents = 'none';
            }
    },
    layout: {
        yAlign: 'bottom',
        padding: {
            left: 0,
            right: 0,
            top: 2,
            bottom: 0
        },
	}
};
   		
   		

function initCharts(){
var ctx = document.getElementById('visitsYesterdayGraph').getContext('2d');
		// For a line chart
		var mylineChart = new Chart(ctx, {
		    type: 'line',
	         // The data for our dataset
	         data: {
              labels: lastWorkingDates,
              datasets: [{
                  label: visitsName,
                  backgroundColor: '#90ceff80',
                  borderColor: '#2196f3',
                  data: nrOfVisitsForWorkingDays
              }]
          },
          options: lineGraphCardOptions
		});
		
     	var ctx = document.getElementById('revenueYesterdayGraph').getContext('2d');
		// For a line chart
		
		var mylineChart = new Chart(ctx, {
		    type: 'line',
	         // The data for our dataset
	         data: {
              labels: lastWorkingDates,
              datasets: [{
                  label: revenueName,
                  backgroundColor: '#76e0bb82',
                  borderColor: '#1bca8c',
                  data: nrOfRevenueForWorkingDays
              }]
          },
          options: lineGraphCardOptions
		});
		
		
     	var ctx = document.getElementById('newPatientsYesterdayGraph').getContext('2d');
		// For a line chart
		var mylineChart = new Chart(ctx, {
		    type: 'line',
	         // The data for our dataset
	         data: {
              labels: lastWorkingDates,
              datasets: [{
                  label: thisYearCustomerLabel,
                  backgroundColor: '#f5d79585',
                  borderColor: '#ffaf00',
                  data: nrOfNewCustomersForWorkingDays
              }]
          },
          options: lineGraphCardOptions
		});
		
     	var ctx = document.getElementById('noShowsYesterdayGraph').getContext('2d');
		// For a line chart
		var mylineChart = new Chart(ctx, {
		    type: 'line',
	         // The data for our dataset
	         data: {
              labels: lastWorkingDates,
              datasets: [{
                  label: noShowName,
                  backgroundColor: '#f5918a87',
                  borderColor: '#ff6258',
                  data: nrOfAppointmentsNoShows
              }]
          },
          options: lineGraphCardOptions
		});

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
	
	if(document.getElementById('myChart') !=null){
		var start = new Date();
        start.setMonth(0);
        var end = new Date();
        end.setMonth(11);
        end.setDate(31);
         
		var ctx = document.getElementById('myChart').getContext('2d');
		// For a line chart
		var tempOpt = lineGraphCardOptions;
		tempOpt.scales.yAxes[0].display = true;
				
		var totalVisitslineChart = new Chart(ctx, {
		    type: 'line',
	
	         // The data for our dataset
	         data: {
                 labels: getMonthDateNamesBetween(start, new Date()),
                 datasets: [{
                     label: visitsName,
                     backgroundColor: '#037a9775',
                     borderColor: '#037a97',
                     data: list
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