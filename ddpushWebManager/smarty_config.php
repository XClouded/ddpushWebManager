<?php

require_once 'smarty/libs/Smarty.class.php';
require_once 'libs/medoo/defaultDb.php';

date_default_timezone_set("Asia/Shanghai");

$smarty = new Smarty;


//$smarty->force_compile = true;
//$smarty->debugging = true;
$smarty->caching = false;
$smarty->cache_lifetime = 0;

$smarty->left_delimiter = "{#";
$smarty->right_delimiter = "#}";
$smarty->setTemplateDir("./smarty/templates");
$smarty->setCacheDir("./smarty/cache");
$smarty->setConfigDir("./smarty/configs");
$smarty->setCompileDir("./smarty/templates_c");
$smarty->setPluginsDir("./smarty/plugins");


$base_path = "smarty/templates/";

$smarty->assign("base_path", $base_path);

