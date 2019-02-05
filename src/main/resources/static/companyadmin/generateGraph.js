function createChart(data, text, suffix, maxValue) {
	let pointsOnChart = [];
	for (var current in data){
	    pointsOnChart.push(makePoint(current, data[current]));
	} 
	var chart = new CanvasJS.Chart("chartContainer", {
		animationEnabled: true,
		exportEnabled: false,
		title:{
			text: text
		},
		axisY:{ 
			title: "Value",
			includeZero: false, 
			interval: maxValue,
			suffix: suffix,
			valueFormatString: "#.0"
		},
		data: [{
			type: "stepLine",
			yValueFormatString: "#0.0" + suffix,
			xValueFormatString: "MMM YYYY",
			markerSize: 5,
			dataPoints: pointsOnChart
		}]
	});
	chart.render();
}

function makePoint(date, amount) {
	let retval = new Object();
	retval.x = new Date(+date);
	retval.y = amount;
	return retval;
}