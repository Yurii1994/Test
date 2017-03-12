<?php 
$mysql_host = "localhost";
$mysql_user = "technicalplan";
$mysql_password = "11121994";
$mysql_database = "technicalplan";

mysql_connect($mysql_host, $mysql_user, $mysql_password);
mysql_select_db($mysql_database);
mysql_set_charset('utf8');
header('Content-type: text/html; charset=utf-8');


if (isset($_GET["action"])) { 
    $action = $_GET['action'];
}

if($action == select)
{
	$q=mysql_query("SELECT text, url_image FROM test");
	while($e=mysql_fetch_assoc($q))
    $output[]=$e;
	print(json_encode($output, JSON_UNESCAPED_UNICODE));
}

mysql_close();
?>