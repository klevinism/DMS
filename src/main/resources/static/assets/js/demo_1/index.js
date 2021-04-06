

function getBarLabels(){
	var currentDate = new Date();
	var oneWeekBefore = getOneWeekBefore(currentDate);
	return getDayDateNamesBetween(oneWeekBefore, currentDate);
}

function buildDataset(labels, datas){
	
	var dataset = new Array();
	
	for(var cnt=0; cnt<labels.length; cnt++){
		var set =  {
			"backgroundColor": getRandomColor()+"",
			"label": labels[cnt],
			"data": datas
		};
		dataset.push(set);
	}
	return dataset;
}


function reloadDataset(datas){
	var dataset = new Array();
	
	for (var key in datas) {
	    if (datas.hasOwnProperty(key)) {
	    	dataset.push({
	    		backgroundColor: getRandomColor()+"",
	    		label: key,
	    		data: datas[key]
	    	});
	    }
	}
	return dataset;
}

function getPersonnelNames(){
	var personnelids = $('#multipleSelectPersonnel').val();
	var arrayNames = new Array();
	for(cnt=0; cnt<personnelids; cnt++){
		arrayNames.push($('#multipleSelectPersonnel option[value="'+personnelids[cnt]+'"]').html());
	}
	return arrayNames;
}

function getPersonnelAndDateRange(){
	var personnelids = $('#multipleSelectPersonnel').val();
	var dates = $("#appointmentPicker").val().split("-");
	var set = {
		date : [dates[0].trim(),dates[1].trim()],
		personnelsIds: [personnelids]
	}
	
	return set;
}

function fireDateChange(start, end){
	var dates = $("#appointmentPicker").val().split("-");
	var personnelAndDateRange = getPersonnelAndDateRange();
	
	$("#appointmentPickerSpinner").show();
	
	//Visits
	removeData(horizontalAppointmentBar);
	refreshStatistics(dates[0].trim(), dates[1].trim(), personnelAndDateRange["personnelsIds"], function(data){
		if(data.error != "error"){
			refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
		}
	});
	
	//Appointment
	refreshAppointmentStatistics(dates[0].trim(), dates[1].trim(), personnelAndDateRange["personnelsIds"], function(data){
		$("#appointmentPickerSpinner").hide();
		if(data.error != "error"){
			refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
		}
	});
	
}

function fireMultiselectChange(element, checked){
	var personnelAndDateRange = getPersonnelAndDateRange();
	
	if(personnelAndDateRange["personnelsIds"] != null && personnelAndDateRange["personnelsIds"].length > 0){
		$("#appointmentPickerSpinner").show();
		
		//Visits
		removeData(horizontalAppointmentBar);
		
		refreshStatistics(personnelAndDateRange["date"][0], personnelAndDateRange["date"][1], personnelAndDateRange["personnelsIds"], function(data){
			if(data.error != "error"){
				refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
			}
		});
		
		//Appointment
		refreshAppointmentStatistics(personnelAndDateRange["date"][0], personnelAndDateRange["date"][1], personnelAndDateRange["personnelsIds"], function(data){
			$("#appointmentPickerSpinner").hide();
		
			if(data.error != "error"){
				refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
			}
		});
	}
}


function getRandomColor() {
  var letters = '0123456789ABCDEF';
  var color = '#';
  for (var i = 0; i < 6; i++) {
    color += letters[Math.floor(Math.random() * 16)];
  }
  return color;
}

function getAllChartLabels(startDate, endDate){
	var start = startDate;
	var end = endDate;
    
	var lblNames = "";			
	var datePeriod = getPeriod(startDate, endDate);
	console.log(datePeriod);
	if(datePeriod<=7){
		lblNames = getDayDateNamesBetween(start, end);
	}else if(datePeriod>7 && datePeriod<=31){
		lblNames = getWeekDateNamesBetween(start, end);
	}else if(datePeriod>31 && datePeriod<=365 ){
		lblNames = getMonthDateNamesBetween(start, end);
	}else if(datePeriod>365 ){
		lblNames = getYearDateNamesBetween(start, end);
	}
	return lblNames;
}

$(document).ready(function(){
	initCharts();
	if( typeof(multiselectPersonnel) != "undefined"){
		multiselectPersonnel.multiselect('destroy');
		multiselectPersonnel.multiselect({
			onChange: fireMultiselectChange,
			numberDisplayed: 1
	 	});
	 	multiselectPersonnel.multiselect('select', $('#multipleSelectPersonnel').find("option")[0].value)
	}
});