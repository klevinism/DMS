function refreshStatistics(startDate, endDate, personnelIds, callback){
	$.post("/api/personnel/statistics", {"startDate" : startDate, "endDate" : endDate,  "personnelIds" : personnelIds}, callback);
}
function refreshAppointmentStatistics(startDate, endDate, personnelIds, callback){
	$.post("/api/personnel/appointment/statistics", {"startDate" : startDate, "endDate" : endDate,  "personnelIds" : personnelIds}, callback);
}

