<?php

$servername = "localhost";
$username = "id17436567_mobile_app";
$password = "uYlV.@ZC5liTc-rm";
$database = "id17436567_golf_zone";

// Create connection
$conn = mysqli_connect($servername, $username, $password, $database);

// Check connection
if (!$conn) {
    die("Connection failed: " . mysqli_connect_error());
}

echo "Connected successfully";
?>