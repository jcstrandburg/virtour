<?php
session_start();
include 'phpfunction.php';

$sent = $_POST['sent'];
$stopname = $_POST['stopname'];
$stoporder = $_POST['stoporder'];
$stopcontent = $_POST['stopcontent'];
$stopx = $_POST['stopx'];
$stopy = $_POST['stopy'];

$more = "<a href='stops.php'>List Of Stops</a>
			</br>";

if($_SESSION['user'] == 1 || $_SESSION['user'] == 2) {
	if(!empty($sent)) {
		$query = "insert into Stops (StopName, StopContent, StopOrder, StopX, StopY) values 
			('" . $stopname . "', '" . $stopcontent. "', '" . $stoporder . "', '" . $stopx . "', '" . $stopy . "')";
		mysqli_query($writedb, $query);
		
		mysqli_query($writedb, "update Stops set StopQRIdentifier = StopID where StopName = '" . $stopname . "'");
		header("Location:stops.php");
	}
}
else {
	header("Location:index.php");
}
?>

<? echo $header ?>

<script>
	function add(x, y) {
		var cont = $("form[name=theform]");
		$('<input type="hidden" name="stopx" value="'+x+'">').appendTo(cont);
		$('<input type="hidden" name="stopy" value="'+y+'">').appendTo(cont);
	}

	function renderJSON() {
		json = framework.renderJSON();
		$("input[name=stopcontent]").val( json);
		document.theform.submit();
	}

	$(document).ready( function() {
		framework = new ComponentFramework( 'compContainer');
	});
</script>



<h1>Add a New Stop</h1>

<form method="POST" name="theform">
	<h2>Stop Name: <input type="text" name="stopname"></h2>
	<h3>Stop Order: <input type="text" name="stoporder"></h3>
	<br>

	<h3>Click On The Map For Stop Location</h3>
	<hr>
	<img id="fl1" src="cf1.png" width="50%" height="50%"><img id="fl4" src="cf4.png" width="50%" height="50%">

	
	<script>
	//get click position
	$("#fl1").click(
		function(ele){ 
			var offset = $(this).offset();
			offx = ele.clientX - offset.left;
			offy = ele.clientY - offset.top;
		
			add(offx/$(this).width(), offy/$(this).height());
		}
	
	);

	$("#fl4").click(
		function(ele){ 
			var offset = $(this).offset();
			offx = ele.clientX - offset.left;
			offy = ele.clientY - offset.top;
		
			add(offx/$(this).width(), offy/$(this).height());
		}
	
	);

	</script>

	<h3>Stop Content</h3>
	<input type="hidden" name="stopcontent">
	<input type="hidden" name="sent" value="sent">
</form>

<button onClick='framework.addComponent(TextComponent)'>Add Text</button>
<button onClick='framework.addComponent(ImageComponent)'>Add Image</button>
<button onClick='framework.addComponent(VideoComponent)'>Add Video</button>

<div name='compContainer' style='border:1px solid;'></div>

<br>

<button onClick='renderJSON()'>Add New Stop</button></br></br>

<?echo footer($more)?>

