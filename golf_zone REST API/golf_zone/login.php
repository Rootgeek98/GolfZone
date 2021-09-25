<?php

require_once 'include/DB_Functions.php'; // DB_Functions.php file

$database = new DB_Functions(); // Create a new DB_Functions Object

$response = array("error" => FALSE);

if (isset($_POST['email']) && isset($_POST['password'])) { // Checks if the Login Form was sent using POST
    
    // Receive the POST params

    $email = $_POST['email']; // Email

    $password = $_POST['password']; // Password

    $user = $database->authenticateUser($email, $password); // Authenticate User

    if ($user) { // User is found
    
        $response["error"] = FALSE;

        $response["user"]["unique_user_id"] = $user["unique_user_id"]; // Unique User ID

        $response["user"]["firstname"] = $user["firstname"]; // Firstname
        
        $response["user"]["lastname"] = $user["lastname"]; // Username

        $response["user"]["email"] = $user["email"]; // Email

        $response["user"]["created_at"] = $user["created_at"]; // Created at

        $response["user"]["updated_at"] = $user["updated_at"]; // Updated at

        echo json_encode($response);
        
        
    } else {
        
        $response["error"] = TRUE;
        
        $response["error_message"] = "Login Failed. Please check your user credentials";
        
        echo json_encode($response);
        
    }

} else {

    $response["error"] = TRUE;

    $response["error_message"] = "Required parameters email or password is missing!";

    echo json_encode($response);
}

?>