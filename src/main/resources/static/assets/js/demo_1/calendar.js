// default keys and styles
var themeConfig = {
	'common.border': '1px solid #e5e5e5',
	'common.backgroundColor': 'white',
	'common.holiday.color': '#ff4040',
	'common.saturday.color': '#333',
	'common.dayname.color': '#333',
	'common.today.color': '#333',
	
	// creation guide style
	'common.creationGuide.backgroundColor': 'rgba(81, 92, 230, 0.05)',
	'common.creationGuide.border': '1px solid #515ce6',
	
	// month header 'dayname'
	'month.dayname.height': '31px',
	'month.dayname.borderLeft': '1px solid #e5e5e5',
	'month.dayname.paddingLeft': '10px',
	'month.dayname.paddingRight': '10px',
	'month.dayname.backgroundColor': 'inherit',
	'month.dayname.fontSize': '12px',
	'month.dayname.fontWeight': 'normal',
	'month.dayname.textAlign': 'left',
	
	// month day grid cell 'day'
	'month.holidayExceptThisMonth.color': 'rgba(255, 64, 64, 0.4)',
	'month.dayExceptThisMonth.color': 'rgba(51, 51, 51, 0.4)',
	'month.weekend.backgroundColor': 'inherit',
	'month.day.fontSize': '14px',
	
	// month schedule style
	'month.schedule.borderRadius': '2px',
	'month.schedule.height': '24px',
	'month.schedule.marginTop': '2px',
	'month.schedule.marginLeft': '8px',
	'month.schedule.marginRight': '8px',
	
	// month more view
	'month.moreView.border': '1px solid #d5d5d5',
	'month.moreView.boxShadow': '0 2px 6px 0 rgba(0, 0, 0, 0.1)',
	'month.moreView.backgroundColor': 'white',
	'month.moreView.paddingBottom': '17px',
	'month.moreViewTitle.height': '44px',
	'month.moreViewTitle.marginBottom': '12px',
	'month.moreViewTitle.backgroundColor': 'inherit',
	'month.moreViewTitle.borderBottom': 'none',
	'month.moreViewTitle.padding': '12px 17px 0 17px',
	'month.moreViewList.padding': '0 17px',
	
	// week header 'dayname'
	'week.dayname.height': '42px',
	'week.dayname.borderTop': '1px solid #e5e5e5',
	'week.dayname.borderBottom': '1px solid #e5e5e5',
	'week.dayname.borderLeft': 'inherit',
	'week.dayname.paddingLeft': '0',
	'week.dayname.backgroundColor': 'inherit',
	'week.dayname.textAlign': 'left',
	'week.today.color': '#333',
	'week.pastDay.color': '#bbb',
	
	// week vertical panel 'vpanel'
	'week.vpanelSplitter.border': '1px solid #e5e5e5',
	'week.vpanelSplitter.height': '3px',
	
	// week daygrid 'daygrid'
	'week.daygrid.borderRight': '1px solid #e5e5e5',
	'week.daygrid.backgroundColor': 'inherit',
	
	'week.daygridLeft.width': '72px',
	'week.daygridLeft.backgroundColor': 'inherit',
	'week.daygridLeft.paddingRight': '8px',
	'week.daygridLeft.borderRight': '1px solid #e5e5e5',
	
	'week.today.backgroundColor': 'rgba(81, 92, 230, 0.05)',
	'week.weekend.backgroundColor': 'inherit',
	
	// week timegrid 'timegrid'
	'week.timegridLeft.width': '72px',
	'week.timegridLeft.backgroundColor': 'inherit',
	'week.timegridLeft.borderRight': '1px solid #e5e5e5',
	'week.timegridLeft.fontSize': '11px',
	'week.timegridLeftTimezoneLabel.height': '40px',
	'week.timegridLeftAdditionalTimezone.backgroundColor': 'white',
	
	'week.timegridOneHour.height': '52px',
	'week.timegridHalfHour.height': '26px',
	'week.timegridHalfHour.borderBottom': 'none',
	'week.timegridHorizontalLine.borderBottom': '1px solid #e5e5e5',
	
	'week.timegrid.paddingRight': '8px',
	'week.timegrid.borderRight': '1px solid #e5e5e5',
	'week.timegridSchedule.borderRadius': '2px',
	'week.timegridSchedule.paddingLeft': '2px',
	
	'week.currentTime.color': '#515ce6',
	'week.currentTime.fontSize': '11px',
	'week.currentTime.fontWeight': 'normal',
	
	'week.pastTime.color': '#bbb',
	'week.pastTime.fontWeight': 'normal',
	
	'week.futureTime.color': '#333',
	'week.futureTime.fontWeight': 'normal',
	
	'week.currentTimeLinePast.border': '1px dashed #515ce6',
	'week.currentTimeLineBullet.backgroundColor': '#515ce6',
	'week.currentTimeLineToday.border': '1px solid #515ce6',
	'week.currentTimeLineFuture.border': 'none',
	
	// week creation guide style
	'week.creationGuide.color': '#515ce6',
	'week.creationGuide.fontSize': '11px',
	'week.creationGuide.fontWeight': 'bold',
	
	// week daygrid schedule style
	'week.dayGridSchedule.borderRadius': '2px',
	'week.dayGridSchedule.height': '24px',
	'week.dayGridSchedule.marginTop': '2px',
	'week.dayGridSchedule.marginLeft': '8px',
	'week.dayGridSchedule.marginRight': '8px'
};



var calendarOptions = {
  	defaultView: 'week', // set 'week' or 'day',
  	taskView: false,
  	scheduleView: ['time'],  // e.g. true, false, or ['allday', 'time'])
  	useCreationPopup: false,
  	useDetailPopup: false,
  	theme: themeConfig,
    month: {
        daynames: dayNames,
        narrowWeekend: true,
        startDayOfWeek: 0 // monday
    },
    week: {
        daynames: dayNames,
        narrowWeekend: true,
        startDayOfWeek: 0 // monday
    },
  	calendars: [{
		id: '1',
		name: 'Full Calendar',
		color: '#ffffff',
		bgColor: '#2196f3',
		dragBgColor: '#a9acf9',
		borderColor: '#a9acf9'
    }],
}

var calElement$ = $("#calendar");

var cal = new tui.Calendar("#calendar", calendarOptions);

cal.on({
    'beforeCreateSchedule': onBeforeCreateSchedule,
    'beforeUpdateSchedule': onBeforeUpdateSchedule,
    'beforeDeleteSchedule': onBeforeDeleteSchedule,
    'clickSchedule': onClickEventSchedule
});


function onBeforeCreateSchedule(e){
    console.log('beforeCreateSchedule', e);
    
    // Use guideEl$'s left, top to locate your schedule creation popup
    var guideEl$ = e.guide.guideElement ?
    	e.guide.guideElement : e.guide.guideElements[Object.keys(e.guide.guideElements)[0]];
    	
    renderEditPopover(guideEl$, e);
      	
    // open a creation popup
    // If you dont' want to show any popup, just use `e.guide.clearGuideElement()`
    // then close guide element(blue box from dragging or clicking days)
}

function onBeforeDeleteSchedule(e){
	console.log('beforeDeleteSchedule', e);
	deleteAppointment(schedule, function(data, status){
 		if(data.error != "error"){
    		cal.deleteSchedule(e.schedule.id, e.schedule.calendarId);
 		}
    });
}

function onClickEventSchedule(event){
    console.log("clickEventSchedule",event);
    var schedule = event.schedule;
    var elementMain = cal.getElement(schedule.id, schedule.calendarId);

    renderDetailPopover(elementMain, schedule);
}

function onBeforeUpdateSchedule(e){
    console.log('beforeUpdateSchedule', e);

    var schedule = e.schedule;
    var changes = e.changes;

    if (changes && !changes.isAllDay && schedule.category === 'allday') {
        changes.category = 'time';
    }
	
	var updateData = schedule;
	updateData.title = changes.title != null ? changes.title : schedule.title;
	updateData.raw = changes.raw != null ? changes.raw : schedule.raw;
	updateData.start = changes.start != null ? changes.start : schedule.start;
	updateData.end = changes.end  != null ? changes.end : schedule.end;
	updateData.attendees = changes.attendees  != null ? changes.attendees : schedule.attendees;
	
	updateAppointment(updateData, function(data, status){
 		if(data.error != "error"){
 			var updatedSchedule =  buildNewSchedule(data.result[0], schedule.calendarId);
 			cal.updateSchedule(schedule.id, schedule.calendarId, updatedSchedule);
 		}
    });
}


/* --Calendar Range functions-- */
    
function moveCalendarToRange(elem){
	switch($(elem).attr('data-action')){
		case 'move-prev' : cal.prev();break;
		case 'move-next' : cal.next();break;
		case 'move-today' : cal.today();cal.scrollToNow();break;
		default : return;
	}
	
	var endDate = new Date(cal.getDateRangeEnd());
	endDate = new Date(endDate.setHours(23));
	endDate = new Date(endDate.setMinutes(59));
	endDate = new Date(endDate.setSeconds(59));
	
	var dataObj = {
        start : new Date(cal.getDateRangeStart()),
    	end : endDate,
    	personnelId : accId
    };
	
	toggleLoadButton(elem);    
	lazyLoadSchedules(dataObj);
	toggleLoadButton(elem);
	
	setRenderRangeText();
	dismissPopover();
}

function lazyLoadSchedules(obj){
    getAvailableAppointments(obj, function(data, result){
        if(data.error != "error"){
            renderDataOnCalendar(data.result);
        }
    });
}

function renderDataOnCalendar(data){
	cal.clear();
    var scheduleArr = [];
    data.forEach(function(elem){
        var schedule = buildNewSchedule(elem, cal.getOptions().calendars[0].calendarId);
        scheduleArr.push(schedule);
    });
    cal.createSchedules(scheduleArr);
    refreshScheduleVisibility();
}


function setRenderRangeText() {
  var renderRange = document.getElementById('renderRange');
  var options = cal.getOptions();
  var viewName = cal.getViewName();
  var html = [];
  if (viewName === 'day') {
    html.push(moment(cal.getDate().getTime()).format('DD/MM/YYYY'));
  } else if (viewName === 'month' &&
    (!options.month.visibleWeeksCount || options.month.visibleWeeksCount > 4)) {
    html.push(moment(cal.getDate().getTime()).format('MM/YYYY'));
  } else {
	html.push(renderPeriodToElement(html, cal.getDateRangeStart().getTime(), cal.getDateRangeEnd().getTime(), false));
  }
  renderRange.innerHTML = html.join('');
}

function renderPeriodToElement(element, start, end, isTime){
	var startFormat = "DD";
	var endFormat = startFormat;
	var dateFormat = "MMM/YYYY";
	
	if(isTime){
		startFormat = "HH:mm";
		endFormat = startFormat;
		dateFormat = "MMM/YYYY"  
	}
    element.push(moment(start).format(startFormat));
    element.push(' ~ ');
    element.push(moment(end).format(endFormat));
    element.push(' /');
    element.push(moment(start).format(dateFormat));
    return element;
}

function refreshScheduleVisibility() {
    var calendarElements = Array.prototype.slice.call(document.querySelectorAll('#calendarList input'));

    cal.render(true);
	cal.render();
}

function getCalendarOptions(calendarId){
	cal.getOptions().calendars.forEach(function(calendarObject){
		return calendarObject.id == calendarId ? calendarObject : null;			
	});
}