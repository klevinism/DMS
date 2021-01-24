function getOneWeekBefore(currentDate){
	var date = new Date(currentDate);
	date.setDate(date.getDate() - 7)
	return date;
}

function getOneMonthBefore(currentDate){
	var date = new Date(currentDate);
	date.setMonth(date.getMonth() - 1);
	return date;
}

function addDay(currentDate){
	var temp = new Date(currentDate);
	temp.setDay(temp.getDay() + 1);
	return temp;
}

function getPeriod(from, to){
	var fromDate = Date.parse(new Date(from));
	var toDate = Date.parse(new Date(to));
	var diffTime = Math.abs(toDate - fromDate);
	
	return Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
}

function getMonthDateNamesBetween(from, to) {
    var monthNames = [ "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December" ];
        
    var arr = [];
    var datFrom = new Date(from);
    var datTo = new Date(to);
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();
    var today = new Date();
      console.log(diffYear);
	for (var i = datFrom.getMonth(); i <= diffYear; i++) {
	    arr.push(monthNames[i%12] + " " + datFrom.getFullYear());
	    datFrom.setMonth(datFrom.getMonth()+1)
	}

    return arr;
}

function getYearDateNamesBetween(from, to) {
    
    var arr = [];
    var datFrom = new Date(from);
    var datTo = new Date(to);
    var fromYear =  datFrom.getFullYear();
    var toYear =  datTo.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + datTo.getMonth();
    var today = new Date();
	for (var i = datFrom.getFullYear(); i <= datTo.getFullYear(); i++) {
	    arr.push(datFrom.getFullYear());
	    datFrom.setYear(datFrom.getFullYear()+1)
	}

    return arr;
}

function getDayDateNamesBetween(from, to){
	var arrayOfWeekdays = ["Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
	var fromDate = new Date(from);
	var toDate = new Date(to);	
	var arr = [];
	
	for (var i = fromDate.getDate(); i <= toDate.getDate(); i++) {
		var temp = new Date(fromDate);
		temp.setDate(i);
        arr.push(arrayOfWeekdays[temp.getDay()]+", "+temp.getDate());
    }        
    
    return arr;
}
function getWeekDateNamesBetween(from, to){
	var monthNames=['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
	var fromDate = new Date(from);
	var toDate = new Date(to);	
	var fromYear =  fromDate.getFullYear();
    var toYear =  toDate.getFullYear();
    var diffYear = (12 * (toYear - fromYear)) + toDate.getMonth();
    var arr = [];
	var period = getPeriod(fromDate,toDate);
	console.log(period);
	for(var cnt=0; cnt < period; cnt+=6){
		var tmp = new Date(fromDate);
		if(cnt < period){
			if(cnt+6 <= period){
				fromDate.setDate(fromDate.getDate()+6);
				arr.push(tmp.getDate() + " - " + (fromDate.getDate()) + " " + monthNames[(fromDate.getMonth())]);
			}
		}else{
			console.log(fromDate.getDate() + " BREAK " + toDate.getDate());
			break;
		}
		
		fromDate.setDate(fromDate.getDate()+1);
	}
	
    
    return arr;
}