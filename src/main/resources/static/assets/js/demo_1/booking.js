
function showHourTable(date, excludedDates){
	var beginDate = new Date(date);
	beginDate.setHours(8);
	beginDate.setMinutes(0);
	var innerDiv = "";
	
	var endDate = new Date(beginDate);
	endDate.setDate(endDate.getDate() + 1);
	while(beginDate < endDate && beginDate.getHours() < 18){
		var show = true;
	
		for(var cnt=0; cnt<excludedDates.length; cnt++){

			if(beginDate.getMonth() == excludedDates[cnt].getMonth() && 
					beginDate.getDay() == excludedDates[cnt].getDay() && 
					beginDate.getYear() == excludedDates[cnt].getYear()){
				
				var before = new Date(excludedDates[cnt]);
				before.setMinutes(before.getMinutes() - 30);
				
				var after = new Date(excludedDates[cnt]);
				after.setMinutes(after.getMinutes() + 30)
				
				if((beginDate.getHours() == before.getHours() && beginDate.getMinutes() == before.getMinutes())){
					beginDate.setHours(beginDate.getHours() + 1);
					beginDate.setMinutes(beginDate.getMinutes() + 30);
					show = false;
					break;
				}else if((beginDate.getHours() == excludedDates[cnt].getHours() && beginDate.getMinutes() == excludedDates[cnt].getMinutes())){
					beginDate.setHours(beginDate.getHours() + 1);
					show = false;
					break;
				}
			}
		}
		
		if(show){
			var ending = new Date(beginDate);
			ending.setHours(ending.getHours() + 1);
		
			innerDiv += "<div class='col-md-3 col-4 my-1 px-2' ><div class='cell py-1' >"+ beginDate.getHours() +" : "+(beginDate.getMinutes()<10?'0':'') + beginDate.getMinutes() + " - "+ ending.getHours() +" : "+(ending.getMinutes()<10?'0':'') + ending.getMinutes()+"</div></div>";
			
			beginDate.setMinutes(beginDate.getMinutes() + 30);
		}
	}
	var outterDiv = "<div class='row text-center mx-0'>" + innerDiv + "</div>";
	$("#hours").html(outterDiv);
	
	$(".px-2").click(function(){
		var selectedAppointmentTimes = $(this).find(".py-1").html().trim().split("-");
		var appointmentDate = new Date($("#id_4").find("input").val());
		
		var appointmentTimes = selectedAppointmentTimes[0].split(" : ");
		appointmentDate.setHours(appointmentTimes[0]);
		appointmentDate.setMinutes(appointmentTimes[1]);
		
		$("#appointmentDate").val(appointmentDate);
		$(".px-2").removeClass("active");
		$(this).addClass("active");
		$("#bookSubmit").removeAttr("disabled");
	});
}

function book(){
	 $("#bookSubmit").attr("disabled","");
	 var customerId = $("#customerId").val();
	 var personnelId = $("#availablePersonnel").val();
	 var appointmentDate = new Date($("#appointmentDate").val());
	 $("#loader").show();
	 $("#hours").html("");
	 $.post("/api/book", {"customerId" : customerId,"personnelId" : personnelId, "appointmentDate":appointmentDate}, function(data, status){
		$("#loader").hide();
		if(data.error != "error"){
			$("#alert").addClass('alert-success');
			$("#alert").html("<strong>"+ data.error+"</strong>"+ data.message);
			$("#alert").show();
			
			setTimeout(function(){ $("#alert").hide(); }, 5000);
			
			$("#hours").html("");
		}else{
			console.log(data);
		}
		
		$("#id_4").show();
		
		$('#appointmentModal').modal('hide')
	 });
}
 
 
function prettyDate2(time) {
  var date = new Date(time);
  return date.toLocaleTimeString(navigator.language, {
    hour: '2-digit'
  });
}
