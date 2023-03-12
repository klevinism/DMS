function dismissPopover(){
    if(cal.getViewName() == "month" && currentCalendarGuide){currentCalendarGuide.clearGuideElement();}

	if($(elemPopover).popover()){
		$(elemPopover).popover('dispose');
	    elemPopover = null;
	}
	if($(detailElemPopover).popover()){
	    $(detailElemPopover).popover('dispose');
	    detailElemPopover = null;
	}
}

function renderEditPopover(elem, i){
  dismissPopover();
  
  
  var popUpElem$ = $("<div class='form-group'>"+
  			"<h4>&nbsp;<button type='button' class='close text-primary' onClick='dismissPopover()'>&times;</button></h4>"+
  			"</div>");
  
  var alertEl$ = $(alertElem$).clone();
  $(wrapperElem$).appendTo($(popUpElem$)).append(dateRangeElem$).append(alertEl$).append(saveEditElem$);
  
  setInputFields(popUpElem$, i);
  setDateToDateTimePickers(i);
  var popupcontainer = i.container ? $(i.container).parent().parent() : $(calElement$);

  $(elem).popover({
        sanitize: false,
      	container: $(popupcontainer),
        html: true,
        content: function () {
            return popUpElem$;
        }
   });
   
   $(elem).popover("show");
   $(alertEl$).hide();
   
   initEventOfSaveEdit(popUpElem$, i);
  	
   elemPopover = elem;
}

function setInputFields(element, schedule){
    //Update fields to selected schedule
    var serviceElem = $(element).find("#"+$(serviceElem$).attr("id"));
  	var dentistElem = $(element).find("#"+$(dentistElem$).attr("id"));
  	var customerElem = $(element).find("#"+$(customerElem$).attr("id"));
  	var timeRangeElem = $(element).find("#"+$(dateRangeElem$).attr("id"));
    var popupFooterButtons = $(element).find('#'+$(saveEditElem$).attr('id'));
    
    $(popupFooterButtons).show();
        
    if(schedule.id != null){
      if(schedule.isReadOnly){
        $(popupFooterButtons).hide();
      }
      
      $(serviceElem).find("select").val(schedule.raw.serviceId);
      $(dentistElem).find("select").val(schedule.raw.personnelId != null && schedule.raw.personnelId != '' ? schedule.raw.personnelId : accId);
      $(customerElem).find("select").val(schedule.raw.customerId);
      
      $(popupFooterButtons).find("button").html(editIconElem$);
      $(element).find("#popoverId").val(schedule.id);
    }else{
      if(new Date() > schedule.start){
        $(popupFooterButtons).hide();  
      }
      
      $(dentistElem).find("select").val(accId);
      $(popupFooterButtons).find("button").html(saveIconElem$);
      $(element).find("#popoverId").val(null);
    }    
}

function setDateToDateTimePickers(i){
	fromRangePicker.data("DateTimePicker").destroy();
	toRangePicker.data("DateTimePicker").destroy();

	rangePickerOptions.date = new Date(i.start);
	rangePickerOptions.minDate= new Date(rangePickerOptions.date).setHours(0);
	rangePickerOptions.maxDate= new Date(rangePickerOptions.date).setHours(24);
	
    fromRangePicker = fromRangePicker.datetimepicker(rangePickerOptions);
    fromRangePicker.data("DateTimePicker").date(new Date(i.start));
    
    rangePickerOptions.date = new Date(i.end);
    toRangePicker = toRangePicker.datetimepicker(rangePickerOptions);
    toRangePicker.data("DateTimePicker").date(new Date(i.end));

}

function initEventOfSaveEdit(popUpElem$, i){
    var saveEditButton = $(popUpElem$).find("#saveEditElem").find("button");
    
    var scheduleId = $(popUpElem$).find("#popoverId").val();
    var isEdit = (scheduleId != null && scheduleId.length > 0);

    $(saveEditButton).on('click', function(e){        
      	e.stopPropagation();

	  	console.log($(saveEditButton).data("subscription") === "expired");

		// Missing or invalid subscription
		if($(saveEditButton).data("subscription") === "expired"){

			subscriptionExpiredToastBanner();
			return;
		}

      	var newSchedule = getNewScheduleValues(popUpElem$);
		if(newSchedule){
		    if(isEdit){
		        console.log("Updating Schedule with id: '"+scheduleId+"'", newSchedule);
				toggleLoadButton(e.target);
				newSchedule.id = scheduleId;

			  	updateAppointment(newSchedule, function(data, status){

					if(data.error != "error"){
						var result = data.result[0];
						var calendarId = data.result[0].personnel.id + '';
			    		cal.updateSchedule(result.id+'', calendarId+'', buildNewSchedule(result, calendarId+''));
			    		refreshScheduleVisibility();
			    		dismissPopover();
					}else{
						$(popUpElem$).find("#alert").hide();
						$(popUpElem$).find("#alert").find("strong").html(data.message);
						$(popUpElem$).find("#alert").show();
					}
					toggleLoadButton(e.target);
					hideSchedulesBasedOnFilters();
				});
			  	
			}else{
				newSchedule.id = newSchedule.start;
				toggleLoadButton(e.target);
				createAppointment(newSchedule, function(data, status){
					if(data.error != "error"){
						newSchedule.id = data.result[0].id+'';
						newSchedule.calendarId = data.result[0].personnel.id + '';
						newSchedule.raw.calendarCategory = 'appointment';
						
						if(newSchedule.calendarId == accId){
							newSchedule.isReadOnly = false;
						}else{
							newSchedule.isReadOnly = true;
						}
						
					    cal.createSchedules([newSchedule]);
			    		cal.render();
			    		dismissPopover();
					}else{
						$(popUpElem$).find("#alert").hide();
						$(popUpElem$).find("#alert").find("strong").html(data.message);
						$(popUpElem$).find("#alert").show();
					}
					toggleLoadButton(e.target);
					hideSchedulesBasedOnFilters();
				});
			}
		}									
  	});
}

function buildNewSchedule(data, calendarId){
	
	var dentistName = data.personnel.account.name+", "+data.personnel.account.surname;
	var customerName = data.customer.account.name+", "+data.customer.account.surname;
	var serviceType = data.serviceType;
	
	var isReadOnly = data.personnel.id == accId ? false : true;
	var title =  
		moment(new Date(data.appointmentDate)).format("HH:mm") + ' - ' + moment(new Date(data.appointmentEndDate)).format("HH:mm") + ' | ' +
    	(serviceType != null? data.serviceType.name : serviceDefaultName) + ' | '+ customerName;
    
    
	return {
		id : data.id+'',
		calendarId: calendarId+'',
        title: title,
        category: 'time',
        start: new Date(data.appointmentDate).toISOString(),
        end: new Date(data.appointmentEndDate).toISOString(),
        attendees: [dentistName, customerName],
        isReadOnly: isReadOnly,
        raw : {
        	serviceId: data.serviceType != null? data.serviceType.id : null,
        	personnelId: data.personnel.id,
        	customerId: data.customer.id,
        	appointmentDate: new Date(data.appointmentDate).toISOString(),
        	appointmentEndDate: new Date(data.appointmentEndDate).toISOString(),
        	calendarCategory: 'appointment'
        }
	}
}

function buildNewVisitSchedule(data, calendarId){
	var dentistName = data.personnel.account.name+", "+data.personnel.account.surname;
	var customerName = data.history.customer.account.name+", "+data.history.customer.account.surname;
	var serviceType = data.serviceType;
	
	var isReadOnly = data.personnel.id == accId ? false : true;
	var title =  
		moment(new Date(data.servicedate)).format("HH:mm") + ' - ' + moment(new Date(data.servicedate).addMinutes(30)).format("HH:mm") + ' | ' +
    	(serviceType != null? data.serviceType.name : serviceDefaultName) + ' | '+ customerName;

	return {
		id : data.id+'',
		calendarId: calendarId+'',
        title: title,
        category: 'time',
        start: new Date(data.servicedate).toISOString(),
        end: new Date(data.servicedate).addMinutes(30).toISOString(),
        attendees: [dentistName, customerName],
        isReadOnly: isReadOnly,
        raw : {
        	serviceId: data.serviceType != null? data.serviceType.id : null,
        	personnelId: data.personnel.id,
        	customerId: data.history.customer.id,
        	appointmentDate: new Date(data.servicedate).toISOString(),
        	appointmentEndDate: new Date(data.servicedate).addMinutes(30).toISOString(),
        	calendarCategory: 'visit'
        }
	}
}

function getNewScheduleValues(elem){
    var serviceEl$ = $(elem).find("#"+$(serviceElem$).attr('id')).find("option:selected");
	var serviceType = $(serviceEl$).text();
	var dentistEl$ = $(elem).find("#"+$(dentistElem$).attr('id')).find("option:selected");
	var dentist = $(dentistEl$).text();
	var customerEl$ = $(elem).find("#"+$(customerElem$).attr('id')).find("option:selected");
	var customer = $(customerEl$).text();
	
	var startTime = fromRangePicker.data("DateTimePicker").date();
	var endTime =  toRangePicker.data("DateTimePicker").date();
	
	$(elem).find("#alert").hide();

    if(serviceType === null || serviceType === ""){
      	serviceType = $(serviceEl$).parent().attr("title");
    } 
    
    if(dentist === null || dentist === ""){
      	$(elem).find("#alert").find("strong").html(alertPersonnel);
      	$(elem).find("#alert").show();
      	return null;
    }else if(customer === null || customer === ""){
      	$(elem).find("#alert").find("strong").html(alertPatient);
      	$(elem).find("#alert").show();
      	return null;
    }else if(!isValidTimeRange(startTime, endTime)){
      	$(elem).find("#alert").find("strong").html(alertDate);
      	$(elem).find("#alert").show();
	    return null;
	}
    
    var calId = cal._options.calendars[0].id;
    
    var title = moment(new Date(startTime)).format("HH:mm") + ' - ' + moment(new Date(endTime)).format("HH:mm") + ' | ' +
    	serviceType + ' | '+ customer;
    	
    return {
        calendarId: calId,
        title: title,
        category: 'time',
        start: new Date(startTime).toISOString(),
        end: new Date(endTime).toISOString(),
        attendees: [dentist , customer],
        raw : {
        	serviceId: ($(serviceEl$).val() != null && $(serviceEl$) != "" )? $(serviceEl$).val() : null,
        	personnelId: $(dentistEl$).val(),
        	customerId: $(customerEl$).val(),
        	appointmentDate: new Date(startTime),
        	appointmentEndDate: new Date(endTime)
        }
    }
}


function renderDetailPopover(elem, i){
    dismissPopover();
	var title = i.title;
	var start = i.start;
	var end = i.end;
	var dentist = i.attendees[0];
	var patient = i.attendees[1];
	var raw = i.raw;
	var serviceId = raw.serviceId != null ? raw.serviceId : serviceDefaultName;
	var popUpElem$ = "<div class='form-group'>"+"</div>";
	var currentCalendar = findCalendarById(i.calendarId);
	
	var serviceEl$ = $(detailBodyElem$).find("#serviceType");
	var serviceTextEl$ = $(serviceElem$).find("select").find('option[value="'+i.raw.serviceId+'"]');
	var serviceText = serviceTextEl$ != null && serviceTextEl$.text() != '' ? serviceTextEl$.text() : serviceDefaultName;	
	
	var icon = $(serviceEl$).find("i");
	$(serviceEl$).html("");
	$(serviceEl$).append(icon).append(serviceText);
	
	var dentistEl$ = $(detailBodyElem$).find("#detailPersonnel");
	var icon = $(dentistEl$).find("i");
	$(dentistEl$).html("");
	$(dentistEl$).append(icon).append(" "+dentist);
	
	var patientEl$ = $(detailBodyElem$).find("#detailCustomer");
	var icon = $(patientEl$).find("i");
	$(patientEl$).html("");
	$(patientEl$).append(icon).append(" "+patient);
	
	var timeRangeEl$ = $(detailBodyElem$).find("#detailTimeRange");
	var icon = $(timeRangeEl$).find("i");
	$(timeRangeEl$).html("");
	$(timeRangeEl$).append(icon).append(" "+renderPeriodToElement([], new Date(start), new Date(end), true).join(''));
	
   	popUpElem$ = $(popUpElem$).append(detailBodyElem$);
   	
   	if(!i.isReadOnly && new Date() <= new Date(start)){
   		popUpElem$ = $(popUpElem$).append(detailEditDeleteElem$);    
   	}

   	var readOnlyIcon = '<i class="fa fa-lock mr-1" title="'+lockedDefaultName+'" style="color:'+currentCalendar.color+'"></i> ';
   	
   	var categoryIcon = '<i class="fa fa-calendar ml-1 mr-1" title="'+appointmentDefaultName+'" style="color:'+currentCalendar.color+'"></i> ';
   	if(raw.calendarCategory == 'visit'){
   		categoryIcon = '<i class="fa fa-stethoscope ml-1 mr-1" title="'+visitsDefaultName+'" style="color:'+currentCalendar.color+'"></i> ';
   	}
   	var popoverContainerEl$ = cal.getViewName() == 'month' ? $(elem).parent().parent().parent() : $(elem).parent().parent().parent().parent();
   	
   	
    // open detail view
    $(elem).parent().popover({
        sanitize: false,
      	container: popoverContainerEl$,
        html: true,
        title: "<div class='modal-header p-1' style='border:0 none;'><h5 class='modal-title' style='color:"+currentCalendar.color+"'> "+ readOnlyIcon + categoryIcon+
        		(title.charAt(0).toUpperCase() + title.slice(1))+"</h5>  <button type='button m-1' class='close' style='color:"+currentCalendar.color+"' onClick='dismissPopover()'>&times;</button></div>",
        content: function () {
            return popUpElem$;
        }
    });
    
 	var popupElm = $(elem).parent().popover("show");
 	$(popoverContainerEl$).find(".popover-header").css("background-color", currentCalendar.bgColor)
    
	$(popUpElem$).find("#editDetailBtn").on("click", function(e){
		console.log("Edit Schedule event", e);
		renderEditPopover($(elem), i);
	});
	
	$(popUpElem$).find("#deleteDetailBtn").on("click", function(e){
		console.log("Delete Schedule event", e);
		toggleLoadButton(e.target);
		deleteAppointment(i, function(data, status){
			if(data.error != "error"){
			    cal.deleteSchedule(i.id, i.calendarId);
			    dismissPopover();
			}else{
				$(popUpElem$).find("#alert").html(data.message);
				$(popUpElem$).find("#alert").show();
			}
			toggleLoadButton(e.target);
			hideSchedulesBasedOnFilters();
		});
		
	});
	detailElemPopover = $(elem).parent();
}

function toggleLoadButton(elem){
	var iconEl$ = $(elem).find("i") != null? $(elem).find("i") : $(elem);
	  
	$(iconEl$).toggleClass("fa-spin fa-spinner");
	$(elem).prop("disabled", (_, val) => !val);
}

