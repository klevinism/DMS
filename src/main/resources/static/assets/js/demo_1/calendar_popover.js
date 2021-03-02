function dismissPopover(){
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
  
  var popUpElem$ = "<div class='form-group'>"+
  			"<h4>&nbsp;<button type='button' class='close text-primary' onClick='dismissPopover()'>&times;</button></h4>"+
  			"</div>";
  
  var alertEl$ = $(alertElem$).clone();
  popUpElem$ = $(popUpElem$).append($(wrapperElem$).html()).append(dateRangeElem$).append(alertEl$).append(saveEditElem$);
  
  setInputFields(popUpElem$, i);
  setDateToDateTimePickers(i);

  $(elem).popover({
        sanitize: false,
      	container: calElement$,
        html: true,
        content: function () {
            return popUpElem$;
        }
   });
    
   $(elem).popover("show");
   $(popUpElem$).find("select").selectpicker('refresh');
   $(alertEl$).hide();
   
   initEventOfSaveEdit(popUpElem$);
  	
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
      
      $(serviceElem).find("select").selectpicker("val", schedule.raw.serviceId);
      $(dentistElem).find("select").selectpicker("val", schedule.raw.personnelId != null && schedule.raw.personnelId != '' ? schedule.raw.personnelId : accId);
      $(customerElem).find("select").selectpicker("val", schedule.raw.customerId);
      $(popupFooterButtons).find("button").html(editIconElem$);
      $(element).find("#popoverId").val(schedule.id);
    }else{
      if(new Date() > schedule.start){
        $(popupFooterButtons).hide();  
      }
      
      $(dentistElem).find("select").selectpicker("val", accId);
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

function initEventOfSaveEdit(popUpElem$){
    var saveEditButton = $(popUpElem$).find("#saveEditElem").find("button");
    
    var scheduleId = $(popUpElem$).find("#popoverId").val();
    var isEdit = (scheduleId != null && scheduleId.length > 0);

    $(saveEditButton).on('click', function(e){        
      	e.stopPropagation();

      	var newSchedule = getNewScheduleValues(popUpElem$);
		
		if(newSchedule){
		    if(isEdit){
		        console.log("Updating Schedule with id: '"+scheduleId+"'", newSchedule);
				toggleLoadButton(e.target);
				newSchedule.id = scheduleId;
				var calendarId = newSchedule.calendarId;
			  	updateAppointment(newSchedule, function(data, status){
					if(data.error != "error"){
						var result = data.result[0];
			    		cal.updateSchedule(result.id+'', calendarId+'', buildNewSchedule(result, calendarId));
			    		refreshScheduleVisibility();
			    		dismissPopover();
					}else{
						$(popUpElem$).find("#alert").hide();
						$(popUpElem$).find("#alert").find("strong").html(data.message);
						$(popUpElem$).find("#alert").show();
					}
					toggleLoadButton(e.target);
				});
			  	
			}else{
				newSchedule.id = newSchedule.start;
				toggleLoadButton(e.target);
				console.log("newSchedule",newSchedule);
				createAppointment(newSchedule, function(data, status){
					if(data.error != "error"){
						newSchedule.id = data.result[0].id+'';
					    cal.createSchedules([newSchedule]);
			    		cal.render();
			    		dismissPopover();
					}else{
						$(popUpElem$).find("#alert").hide();
						$(popUpElem$).find("#alert").find("strong").html(data.message);
						$(popUpElem$).find("#alert").show();
					}
					toggleLoadButton(e.target);
				});
			} 
		}									
  	});
}

function buildNewSchedule(data, calendarId){
	var calendarOptions = (options = getCalendarOptions(calendarId)) ? options : cal.getOptions().calendars[0];
	
	var dentistName = data.personnel.account.name+", "+data.personnel.account.surname;
	var customerName = data.customer.account.name+", "+data.customer.account.surname;
	var serviceType = data.serviceType;
	
	var title = moment(new Date(data.appointmentDate)).format("HH:mm") + ' - ' + moment(new Date(data.appointmentEndDate)).format("HH:mm") + ' | ' +
    	(serviceType != null? data.serviceType.name : serviceDefaultName) + ' | '+ customerName;
    	
	return {
		id : data.id+'',
		calendarId: calendarOptions.id,
        title: title,
        category: 'time',
        start: new Date(data.appointmentDate).toISOString(),
        end: new Date(data.appointmentEndDate).toISOString(),
        attendees: [dentistName, customerName],
        raw : {
        	serviceId: data.serviceType != null? data.serviceType.id : null,
        	personnelId: data.personnel.id,
        	customerId: data.customer.id,
        	appointmentDate: new Date(data.appointmentDate).toISOString(),
        	appointmentEndDate: new Date(data.appointmentEndDate).toISOString(),
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
	var popUpElem$ = "<div class='form-group'>"+"</div>";
	
	var serviceEl$ = $(detailBodyElem$).find("#serviceType");
	var serviceText = $(serviceElem$).find("select").find('option[value="'+i.raw.serviceId+'"]').text();	
	serviceText = serviceText != null && serviceText != '' ? serviceText : serviceDefaultName;
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
   	
    // open detail view
    $(elem).parent().popover({
        sanitize: false,
      	container: $(elem).parent().parent().parent().parent(),
        html: true,
        title: "<div class='modal-header' style='border:0 none;'><h5 class='modal-title text-white'> "+(title.charAt(0).toUpperCase() + title.slice(1))+"</h5>  <button type='button' class='close text-white' onClick='dismissPopover()'>&times;</button></div>",
        content: function () {
            return popUpElem$;
        }
    });
    
 	$(elem).parent().popover("show");
    
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
		});
		
	});
	detailElemPopover = $(elem).parent();
}

function toggleLoadButton(elem){
	var iconEl$ = $(elem).find("i") != null? $(elem).find("i") : $(elem);
	  
	$(iconEl$).toggleClass(" fa-spin fa-spinner");
	$(elem).prop("disabled", (_, val) => !val);
}

