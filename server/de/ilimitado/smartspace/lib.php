<?php
/*
 * Created on Jun 26, 2009
 *
 * To change the template for this generated file go to
 * Window - Preferences - PHPeclipse - PHP - Code Templates
 */
 
function isReadable($path, $file)
{
    	return (bool) (file_exists($path.$file));
}

//check precondition with isReadable() before calling!!
function readFromFile($path, $file)
{
  return file_get_contents($path.$file);
}

function writeFile($destination, $contents)
{
  return file_put_contents($destination, $contents, LOCK_EX);
}

function deleteFile($path, $file)
{
  unlink($path.$file);
}
?>
