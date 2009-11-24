<?php
/*
 * Created on Jun 26, 2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
require_once("../lib.php");
echo '<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN">'.
		 '<html>' .
		 '<head>' .
  		 	'<title>Smart Space Framework</title>' .
  		 	'<link rel="stylesheet" href="../style.css" type="text/css" media="screen" />' .
		 '</head>' .
		 '<body>';



$destDir = "../uploads/smartspace/";
$destFile = "positions.igp";
$dest = $destDir.$destFile;

writeFile($dest, "Test");

echo assert((bool) (file_exists($dest) 
			&& file_get_contents($dest) == 'Test')) 
			? '<p class="testCase pass">writeFile(): pass</p>' 
			: '<p class="testCase fail">writeFile(): fail</p>';
			
unlink($dest);





writeFile($dest, "Test2");
readFromFile($destDir, $destFile);

echo assert((bool) file_get_contents($dest) == 'Test') 
			? '<p class="testCase pass">readFromFile(): pass</p>' 
			: '<p class="testCase fail">readFromFile(): fail</p>';
			
unlink($dest);



writeFile($dest, "Test3");
echo assert((bool) isReadable($destDir, $destFile)) 
			? '<p class="testCase pass">isReadable(): pass</p>' 
			: '<p class="testCase fail">isReadable(): fail</p>';
			
unlink($dest);



writeFile($dest, "Test4");
deleteFile($destDir, $destFile);
echo assert((bool) !file_exists($dest)) 
			? '<p class="testCase pass">deleteFile(): pass</p>' 
			: '<p class="testCase fail">deleteFile(): fail</p>';
			


echo 	'</body>' .
	'</html>';

?>