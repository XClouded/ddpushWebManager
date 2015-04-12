<?php
session_start();

if( !isset($_SESSION['username']) )
{
	echo "<script>window.parent.location.href='index.php';</script>";
	exit;
}

require 'smarty_config.php';

if( isset( $_POST['submit'] ) )
{
    $gameName = $_POST['game_name'];
    $gameVersion = $_POST['game_version'];
    $gameCompany = $_POST['game_company'];

    $result = $database->insert('games', array('game_name'=>$gameName, 'version'=>$gameVersion, 'company'=>$gameCompany));

    if( $result )
    {
        echo "<script>alert('添加成功');</script>";
    }
    else
    {
        echo "<script>alert('添加失败');</script>";
    }

}

$smarty->display("game_add.html");