<?php
session_start();
include '../phpfunction.php';

if( $_SESSION['user'] == 0) {
	header("Location: ../index.php");
}

echo "<html>";
echo make_header('../');
?>

<style>
.clr {
	clear: both;
	font-size: 0;
	height: 0;
}

#filelist {
	width: 50%;
	float: left;
	height: 500px;
	overflow-y: scroll;
}

#preview-wrapper {
	width: 50%;
	float: left;
	height: 500px;
}

#preview {
	width: 100%;
	height: 100%;
}

.filewrapper {
	padding: 3px;
	padding-bottom: 0px;
	margin: 3px;
	border: solid 1px gray;
	border-radius: 0.4em;
}

.mediafile {
	padding: 3px;
	cursor: pointer;
}

.mediafile {
	float: left;
	width: 90%;
}

.previewfile {
	max-width: 95%;
	max-height: 85%;
	width: auto;
	height: auto;
}

.filestats {
	width: 13%;
	float: right;
}

.delete-form {
	float: right;
	margin: 0;
}

#fileuploader {
	padding: 3em;
	border: solid 1px gray;
}
</style>

<script>
function do_preview(url) {
	var regex = /(?:\.([^.]+))?$/;
	var ext = regex.exec(url)[1];
	var vid_exts = ['mp4', 'ogg'];
	var img_exts = ['jpg', 'png'];
	
	//if its an image file
	if ($.inArray(ext, img_exts) > -1) {
		$('#preview').html("<img class='previewfile' alt='Preview' src='"+url+"'><br>");
	}
	//else if its a video file
	else if ($.inArray(ext, vid_exts) > -1) {
		$('#preview').html("<video alt='preview' src='"+url+"' controls></video>");
	}
	//else what do?
	else {
		$('#preview').html("No preview available for this file");
	}
	
}

$(document).ready(function() {
	$('.mediafile').click(function(){
		var url = $(this).data('fileurl');
		do_preview(url);
	});
	
	$('.delete-form').click(function(event){
		if ( !confirm("Are you sure? Make sure this file is no longer used on the tour!")) {
			event.preventDefault();
			event.stopPropagation();
		}
	});
	
	//if the file api is supported, add warnings to files that are too large!
	if (window.File) {
		$('#fileToUpload').change(function(){
			filesize = this.files[0].size;
			if (filesize > $(this).data('uploadlimit')) {
				$('#file-warning').html("<h4>This file exceeds your upload limit, which is "+$(this).data("uploadlimit")/(1024*1024)+" MB</h4>");
			}
			else {
				$('#file-warning').html("");
			}
		});
	}
	else {
		//no upload size checking, bummer.
	}
});
</script>

<div id='filelist'>
<h3>Files on Server:</h3>

<?php
if (isset($_POST['filters'])) {
	$filters = explode(',', $_POST['filters']);
}
else {
	$filters = NULL;
}

$url = $_SERVER['SERVER_NAME'].$_SERVER['REQUEST_URI'];
$basepath = dirname($url);
$files = scandir('./');
$filter_out = array('htaccess', 'php', 'js', 'css', 'html');
$image_extns = array('jpg', 'png');
$vid_extns = array('mp4', 'ogg');

foreach ($files as $file) {
	if ( !is_file($file)) {
		continue;
	}

	$extn = pathinfo($file, PATHINFO_EXTENSION);
	$filesize = filesize($file);
	if ( $filesize < 1024 ) {
		$filesize = sprintf("%d Bytes", $filesize);
	}
	else if ( $filesize < 1024 * 1024) {
		$filesize = sprintf("%d KB", $filesize/1024);
	}
	else {
		$filesize = sprintf("%.1f MB", $filesize/(1024*1024));
	}
	
	if ( !in_array($extn, $filter_out)) {
		$new_match = array(
			'filename'=>$file,
			'url'=>"http://{$basepath}/{$file}",
			'size'=>$filesize,
			'extn'=>$extn,
			'type'=>'Image (jpg)',
			);
			
		if ( in_array($extn, $image_extns)) {
			$new_match['type'] = "Image ($extn)";
		}
		else if ( in_array($extn, $vid_extns)) {
			$new_match['type'] = "Video ($extn)";
		}
		else {
			$new_match['type'] = ".$extn file";
		}
			
		$matches[] = $new_match;
	}
}

foreach ($matches as $match) {
?>
	
	<div class='filewrapper'>
		<div class='mediafile' data-fileurl='<?php echo $match['url'];?>'>
			<span class='filename'><?php echo $match['filename'];?></span>
			<span class='filestats'><?php echo $match['type'];?></span>
			<span class='filestats'><?php echo $match['size'];?></span>
		</div>
		<form class='delete-form' method='post' action='deletefile.php'>
			<input type='hidden' name='filename' value='<?php echo $match['filename'];?>'/>
			<input type='submit' value='Delete File'/>
		</form>
		<br class='clr'>
	</div>
	
<?php
}
?>
</div>

<div id='preview-wrapper'>
	<h3>File Preview</h3>
	<div id='preview'>
		
	</div>
</div>
<br class='clr'>

<?php
function get_size_limit($fieldname) {
	$val = trim(ini_get($fieldname));
	$postfix = strtolower($val[strlen($val)-1]);
	switch ($postfix) {
		case 'k':
			$val = intval($val)*1024;
			break;
		case 'm':
			$val = intval($val)*1024*1024;
			break;
		case 'g':
			$val = intval($val)*1024*1024*1024;
			break;
	}
	return $val;
}

//find the maximum file upload size
$max_upload = get_size_limit('upload_max_filesize');
$max_post = get_size_limit('post_max_size');
$memory_limit = get_size_limit('memory_limit');
$upload_limit = min($max_upload, $max_post, $memory_limit);
/*echo "Size limits:<br>";
echo $max_upload."<br>";
echo $max_post."<br>";
echo $memory_limit."<br>";
echo $upload_limit."<br>";*/
?>

<div id='fileuploader'>
	<form action="upload.php" method="post" enctype="multipart/form-data">
		<h2>Upload New File</h2>
		<input type="file" name="fileToUpload" id="fileToUpload" size="70" data-uploadlimit="<?php echo $upload_limit;?>">
		<div id='file-warning'></div>
		<input type="submit" value="Upload File" name="submit">
	</form>
</div>

<?php
$more = "<a href='../admin.php'>Administrative Functions</a>
			</br>";
echo footer($more);
?>
