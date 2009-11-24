<?php
/*
 * Created on Jun 26, 2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
require_once("config.php");
require_once("lib.php");

if(isReadable($destDir, $destFile)) {
	
	$jsonString = readFromFile($destDir, $destFile);
	$iGeoPoint = json_decode($jsonString);

	echo '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">'.
		 '<html>' .
		 '<head>' .
  		 	'<title>Smart Space Framework</title>' .
			'<meta http-equiv="refresh" content="2; URL=http://localhost/SmartSpaceServer/de/ilimitado/smartspace/smartSpacePositionProvider.php">' .
  		 	'<link rel="stylesheet" href="style.css" type="text/css" media="screen" />' .
			'<style type="text/css">' .
  				'#mainPanel img.marker1 {
										position: relative;' .
										'left: '. $iGeoPoint->{'IGP_POS_X'}.'px;'.
										'top: '. $iGeoPoint->{'IGP_POS_Y'}.'px;' .
				'}
  			</style>' .
		 '</head>' .
		 '<body>' .	
			'<div id="mainPanel">' .
				'<img class="marker1" src="marker1.png"/>' .
			'</div>' .
		'</body>' .
	'</html>';
}
else
	echo "Error";
?>

