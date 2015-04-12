<?php
session_start();

if( isset($_GET['exit']) )
{
	unset($_SESSION['username']);
}

require 'smarty_config.php';

$smarty->display('login.html');


