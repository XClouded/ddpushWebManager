<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require 'smarty_config.php';


$user = $_SESSION['username']['user'];


$smarty->assign('user_count', count($user));
$smarty->assign('user_list', $user);

$smarty->display("artice-list.html");