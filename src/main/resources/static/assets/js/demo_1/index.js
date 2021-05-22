

function getBarLabels(){
	var currentDate = new Date();
	var oneWeekBefore = getOneWeekBefore(currentDate);
	return getDayDateNamesBetween(oneWeekBefore, currentDate);
}

function buildDataset(labels, datas, color){
	
	var dataset = new Array();
	
	for(var cnt=0; cnt<labels.length; cnt++){
		var set =  {
			"backgroundColor": color+"",
			"label": labels[cnt],
			"data": datas
		};
		dataset.push(set);
	}
	
	return dataset;
}

function reloadRevenueDataset(datas){
	var dataset = new Array();
		var totalHtml='<div class="d-flex flex-column justify-content-center border-bottom mb-2" > '+
          '<div class="d-flex align-items-center" id="visitsBarTotalCount">'+
            '<div class="dot-indicator mt-1 mr-2"></div>'+
            '<h4 class="mb-0">0</h4>'+
          '</div>'+
          '<small class="text-muted ml-3"></small>'+
        '</div>';	
	for (var key in datas) {

		var color = getRandomColor();
		
		var text = $(totalHtml).clone();
		$(text).find(".dot-indicator").css("background-color", color);
		var total = 0;
		datas[key].forEach(function(n){
			if(n != null){
				total+=Number.parseInt(n);
			}
		})
		
		$(text).find("h4").text(numberWithCommas(total) + " " +currencyAll);
		$(text).find("small").text(key);
		
		$("#RevenueBarsTotal").append($(text).clone());	
		
	    if (datas.hasOwnProperty(key)) {
	    	dataset.push({
	    		backgroundColor: color+"",
	    		label: key,
	    		data: datas[key]
	    	});
	    }
	}
	
	return dataset;
}


function reloadDataset(datas){
	var dataset = new Array();
		var totalHtml='<div class="d-flex flex-column justify-content-center border-bottom mb-2" > '+
          '<div class="d-flex align-items-center" id="visitsBarTotalCount">'+
            '<div class="dot-indicator mt-1 mr-2"></div>'+
            '<h4 class="mb-0">0</h4>'+
          '</div>'+
          '<small class="text-muted ml-3"></small>'+
        '</div>';	
	for (var key in datas) {

		var color = getRandomColor();
		
		var text = $(totalHtml).clone();
		$(text).find(".dot-indicator").css("background-color", color);
		var total = 0;
		datas[key].forEach(function(n){
			total+=Number.parseInt(n);
		})
		
		$(text).find("h4").text(total);
		$(text).find("small").text(key);
		
		$("#BarsTotal").append($(text).clone());	
		
	    if (datas.hasOwnProperty(key)) {
	    	dataset.push({
	    		backgroundColor: color+"",
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
	
	removeData(horizontalAppointmentBar);
	$("#BarsTotal").html("");
	removeData(horizontalRevenueBar);
	$("#RevenueBarsTotal").html("");
	
	//Visits
	refreshStatistics(dates[0].trim(), dates[1].trim(), personnelAndDateRange["personnelsIds"], function(data){
		if(data.error != "error"){
			refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
		}
	});
	
	//Appointment
	refreshAppointmentStatistics(dates[0].trim(), dates[1].trim(), personnelAndDateRange["personnelsIds"], function(data){
		if(data.error != "error"){
			refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
		}
	});
	
	//Revenue
	refreshRevenueStatistics(dates[0].trim(), dates[1].trim(), personnelAndDateRange["personnelsIds"][0], function(data){
		$("#appointmentPickerSpinner").hide();
		if(data.error != "error"){
			refreshHorizontalRevenueBarChart(data.result[0], personnelAndDateRange["date"]);		
		}
	});
}

function fireMultiselectChange(element, checked){
	var personnelAndDateRange = getPersonnelAndDateRange();
	
	if(personnelAndDateRange["personnelsIds"] != null && personnelAndDateRange["personnelsIds"].length > 0){
		$("#appointmentPickerSpinner").show();
		
		removeData(horizontalAppointmentBar);
		$("#BarsTotal").html("");
		removeData(horizontalRevenueBar);
		$("#RevenueBarsTotal").html("");
		
		//Visits
		refreshStatistics(personnelAndDateRange["date"][0], personnelAndDateRange["date"][1], personnelAndDateRange["personnelsIds"], function(data){
			if(data.error != "error"){
				refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
			}
		});
		
		//Appointment
		refreshAppointmentStatistics(personnelAndDateRange["date"][0], personnelAndDateRange["date"][1], personnelAndDateRange["personnelsIds"], function(data){		
			if(data.error != "error"){
				refreshHorizontalAppointmentBarChart(data.result[0], personnelAndDateRange["date"]);		
			}
		});
		
		//Revenue
		refreshRevenueStatistics(personnelAndDateRange["date"][0], personnelAndDateRange["date"][1], personnelAndDateRange["personnelsIds"][0], function(data){
			$("#appointmentPickerSpinner").hide();
			if(data.error != "error"){
				refreshHorizontalRevenueBarChart(data.result[0], personnelAndDateRange["date"]);		
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

function numberWithCommas(x) {
    return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
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