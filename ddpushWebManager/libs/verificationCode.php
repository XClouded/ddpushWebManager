<?php
session_start();
//session_destroy();

if( isset($_GET['check']) )
{
    echo $_SESSION['author_code'];
}
else
{
    require "./ValidateCode.php";

    $vcode = new ValidateCode();
    $vcode->doimg();
    $_SESSION['author_code'] = $vcode->getCode();
}

