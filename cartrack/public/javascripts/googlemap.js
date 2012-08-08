var markersArray = [];
var polysArray = [];
var map;
var shipImage;
var commonImage;

function initGoogleMap() {
	var mapOptions = {
		center : new google.maps.LatLng(-34.397, 150.644),
		zoom : 8,
		mapTypeId : google.maps.MapTypeId.ROADMAP
	};
	map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
	shipImage = new google.maps.MarkerImage(
			'/assets/images/marker-current.png', new google.maps.Size(30, 19),
			new google.maps.Point(0, 0), new google.maps.Point(15, 9));

	commonImage = new google.maps.MarkerImage('/assets/images/marker.png',
	// This marker is 20 pixels wide by 32 pixels tall.
	new google.maps.Size(7, 7),
	// The origin for this image is 0,0.
	new google.maps.Point(0, 0),
	// The anchor for this image is the base of the flagpole at 0,32.
	new google.maps.Point(4, 4));
}

function getlocationFromJson(json) {
	var latlng = new google.maps.LatLng(json.longitude, json.latitude);
	return latlng;
}

function addMarker(obj,targetId, name, formerLocation, startLocation) {

	var location = getlocationFromJson(obj);
	var marker = new google.maps.Marker({
		position : location,
		icon : commonImage,
	});

	// could change to google map api distance matrix
	var distanceToLast = 0;
	if (formerLocation) {
		distanceToLast = calDistance(formerLocation.lat(),
				formerLocation.lng(), location.lat(), location.lng());

	}
	var distanceToStart = 0;
	if (startLocation) {
		distanceToStart = calDistance(startLocation.lat(), startLocation
				.lng(), location.lat(), location.lng());
	}

	var contentString = '<div class="container-fluid">'
			+ '<div class="row-fluid"><div class="span8">{0}</div><div class="span4"><a id="gettargetinfo" onclick="window.open(\'/target/getinfo/{1}\');" href="#">信息</a></div></div>'
			+ '<div class="row-fluid"><div class="span12">{2}</div></div>'
			+ '<div class="row-fluid"><div class="span4"></div><div class="span4">负责人:{3}</div></div>'
			+ '<div class="row-fluid"><div class="span4"></div><div class="span4">产品:{4}</div></div>'
			+ '<div class="row-fluid"><div class="span6">距离前点:{5}km</div><div class="span6">距离起点:{6}km</div></div>'
			+ '</div>';
	contentString = jQuery.validator.format(contentString, name, targetId,
			new Date(obj.pointRecordTime).toLocaleString(), obj.mananger,
			obj.product, distanceToLast, distanceToStart);

	google.maps.event.addListener(marker, 'click', function() {

		var infowindow = new google.maps.InfoWindow({
			content : contentString
		});
		infowindow.open(map, marker);
	});

	markersArray.push(marker);
}

function setLastMarkerToShip() {
	var marker = markersArray.pop();
	if (marker) {
		marker.setIcon(shipImage);
	}
	markersArray.push(marker);
}

// Removes the overlays from the map, but keeps them in the array
function clearOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
	}
	if (polysArray) {
		for (i in polysArray) {
			polysArray[i].setMap(null);
		}
	}

}

function showOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(map);
		}
	}
	if (polysArray) {
		for (i in polysArray) {
			polysArray[i].setMap(map);
		}
	}
}

// Deletes all markers in the array by removing references to them
function deleteOverlays() {
	if (markersArray) {
		for (i in markersArray) {
			markersArray[i].setMap(null);
		}
		markersArray.length = 0;
	}

	if (polysArray) {
		for (i in polysArray) {
			polysArray[i].setMap(null);
		}
		polysArray.length = 0;
	}
}

function setCenter(location) {
	map.setCenter(location);
}

function getRandomColor() {
	var letter = [ '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C',
			'D', 'E' ];
	var color = "#";
	for (i = 0; i < 6; i++) {
		var vNum = Math.random();
		vNum = Math.round(vNum * 13);
		color = color + letter[vNum];
	}
	return color;
}

function addPoly(locationArray) {

	var polyOptions = {
		path : locationArray,
		strokeColor : getRandomColor(),
		strokeOpacity : 1.0,
		strokeWeight : 3
	}

	poly = new google.maps.Polyline(polyOptions);
	polysArray.push(poly);

}

function getDistanceFromLastPoint(current, last) {

}

function dec2deg(dec) {
	var dec = Math.abs(dec) + "";
	dec = dec.split(".");

	var deg = dec[0];

	dec[1] = "0." + dec[1];
	var min_sec = dec[1] * 3600;
	var min = Math.floor(min_sec / 60);
	var sec = (min_sec - (min * 60));

	return [ deg, min, sec ];
}

function deg2dec(deg, min, sec) {
	var deg = Math.abs(deg);
	var min = Math.abs(min);
	var sec = Math.abs(sec);
	return deg * 1 + (sec * 1 + min * 60) / 3600;
}

// 弧度转换
function rad(d) {
	return d * Math.PI / 180.0;
}

// 计算距离，结果的单位为千米（km）
function calDistance(lat1, lng1, lat2, lng2) {
	if ((Math.abs(lat1) > 90) || (Math.abs(lat2) > 90))
		return false;

	if ((Math.abs(lng1) > 180) || (Math.abs(lng2) > 180))
		return false;

	var radLat1 = rad(lat1);
	var radLat2 = rad(lat2);
	var a = radLat1 - radLat2;
	var b = rad(lng1) - rad(lng2);
	var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
			+ Math.cos(radLat1) * Math.cos(radLat2)
			* Math.pow(Math.sin(b / 2), 2)));
	s = s * 6378.137; // 地球半径 6378.137
	s = Math.round(s * 10000) / 10000;
	return s;
}
