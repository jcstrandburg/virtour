<?php
session_start();
include 'phpfunction.php';

$stopid = $_GET['eid'];

$sent = $_POST['sent'];
if ( isset($_POST['sent'])) {
	$stopid = $_POST['sent'];
	$stopname = $_POST['stopname'];
	$stoporder = $_POST['stoporder'];
	$stopcontent = stripslashes( $_POST['stopcontent']);
	$stopx = $_POST['stopx'];
	$stopy = $_POST['stopy'];
	$mapid = $_POST['mapid'];
}
	
$more = "<a href='stops.php'>List Of Stops</a>
			</br>";

if($_SESSION['user'] != 0) {

	if(empty($sent)) {
		$stmt = $link->prepare("select StopName, StopOrder, StopContent, StopX, StopY, MapID from Stops where StopID=?");
        $stmt->bind_param("i", $stopid);
        $stmt->execute();
        $stmt->bind_result($stopname, $stoporder, $stopcontent, $stopx, $stopy, $mapid);
        $stmt->fetch();
        $stmt->close();
	}
	else {
        $stmt = $writedb->prepare("update Stops set `StopName`=?, `StopOrder`=?, `StopContent`=?, `StopX`=?, `StopY`=?, `MapID`=?  where `StopID`=?");
        $stmt->bind_param("sisddii", $stopname, $stoporder, $stopcontent, $stopx, $stopy, $mapid, $stopid);
        $success = $stmt->execute();
    	header("Location:stops.php");
	}
}
else {
	header("Location:index.php");
}
?>

<?php echo $header?>

<script type="text/javascript">
function set_position(x, y) {
	$('input[name=stopx]').val(x);
	$('input[name=stopy]').val(y);
	
	css_top = ""+y*100+"%";
	css_left = ""+x*100+"%";
	$('#pin').css({'top': css_top, 'left': css_left});
}

function renderJSON() {
    json = framework.renderJSON();
    $("input[name=stopcontent]").val( json);
	document.theform.submit();
}

$(document).ready( function() {
	framework = new ComponentFramework( "compContainer");
	framework.loadFromJSON('<?php echo $stopcontent;?>');
    
    set_position( <?php echo "$stopx, $stopy";?>);


	$('#clickymap').click(
		function(event){ 
			var offset = $(this).offset();
			offx = event.pageX - offset.left;
			offy = event.pageY - offset.top;
			set_position(offx/$(this).width(), offy/$(this).height());
		}			
	);
	
	$('#map-selector').change(
		function(event) {
			var url = $(this).val();
            
            var id = $(this).val();
            var url = "maps/"+$(this).find(":selected").data("url");
			$('#clickymap').attr('src', url);
			$('input[name=mapid]').val(id);
		}
	);
});
</script>


<form method="post" name="theform">
	<h1>Content for Stop Name: <input type="text" name="stopname" value="<?php echo $stopname?>"></h1>
	<h3>Stop Order: <input type="text" name="stoporder" value="<?php echo $stoporder?>"></h3>

	<div class='mapSelector'>
		<h3>Select Map</h3>
        <?php
		$maps = get_map_list();?>
		<select id='map-selector'>
			<?php
			foreach ($maps as $map) {
                if ( $map['id'] == $mapid) {
                    $selectedString = "selected";
                }
                else {
                    $selectedString = "";
                }
				echo "<option value='{$map['id']}' data-url='{$map['url']}' $selectedString>{$map['desc']}</option>";
			}
			?>
		</select>
	</div>
	
	<h3>Click On The Map For Stop Location</h3>
	<div class='mapWrapperWrapper'>
		<div class='mapWrapper'>
			<img id='pin' src='pin.gif'/>
			<img id="clickymap" src="maps/<?php echo $maps[$mapid]['url'];?>">
		</div>
	</div>
	<hr>	
	
	<input type="hidden" name="stopcontent">
	<input type="hidden" name="stopx" value="0.0">
	<input type="hidden" name="stopy" value="0.0">
	<input type="hidden" name="mapid" value="<?php echo $mapid;?>">

	<input type="hidden" name="sent" value="<?php echo $stopid?>">
</form>
<button onClick='framework.addComponent(TextComponent)'>Add Text</button>
<button onClick='framework.addComponent(ImageComponent)'>Add Image</button>
<button onClick='framework.addComponent(VideoComponent)'>Add Video</button>
<div name='compContainer' style='border:1px solid;'></div>

<br>

<button onClick='renderJSON()'>Save Changes</button></br></br>

<?php echo footer($more)?>
