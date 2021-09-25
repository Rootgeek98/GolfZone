<?php
/**
 * Registration Endpoint
 * Handles the user registration request
 * @author Bill Glinton <tom.omuom@strathmore.edu>
 */

require_once 'include/DB_Functions.php'; // DB_Functions.php file

$database = new DB_Functions(); // Create a new DB_Functions Object

$response = array("error" => FALSE); // json response array

if (isset($_POST['firstname']) && isset($_POST['lastname']) && isset($_POST['email']) &&  isset($_POST['password']) &&  isset($_POST['confirm_password'])) { // Checks if the Registration Form was sent using POST
    
    // Receive the POST parameters
    
    $firstname = $_POST['firstname']; // Firstname
    
    $lastname = $_POST['lastname']; // Lastname
    
    $email = $_POST['email']; // Email

    $password = $_POST['password']; // Password
    
    $confirm_password = $_POST['confirm_password']; // Password

    if ($_POST['firstname'] == NULL || $_POST['lastname'] == NULL || $_POST['email'] == NULL || $_POST['password'] == NULL || $_POST['confirm_password'] == NULL) {  // Check if the input fields are empty

        $response["error"] = TRUE;

        $response["error_message"] = "One of your input fields is empty";

        echo json_encode($response);

    } else if ($_POST["password"] != $_POST["confirm_password"]) {
        
        $response["error"] = TRUE;

        $response["error_message"] = "Your Passwords do not match!";

        echo json_encode($response);
        
    } elseif (strlen($_POST["password"]) < 8) {
        
        $response["error"] = TRUE;

        $response["error_message"] = "Your password must be at least 8 characters in length!";

        echo json_encode($response);
        
    } else {

        $check_user = $database->checkUser($email); // Check existence of user in the database

        if ($check_user) { // User already exists in the database

            $response["error"] = TRUE;

            $response["error_message"] = "User already exists with " . $email;

            echo json_encode($response);

        } else { // User does not exist in the database

            $user = $database->createUser($firstname, $lastname, $email, $password); // Create new user

            if ($user) { // User Stored Successfully

                $response["error"] = FALSE;

                $response["user"]["unique_user_id"] = $user["unique_user_id"]; // Unique_user_id

                $response["user"]["firstname"] = $user["firstname"]; // Firstname
                
                $response["user"]["lastname"] = $user["lastname"]; // Lastname

                $response["user"]["email"] = $user["email"]; // Email

                $response["user"]["password"] = $user["encrypted_password"]; // Password

                $response["user"]["created_at"] = $user["created_at"]; // Created at

                $response["user"]["updated_at"] = $user["updated_at"]; // Updated at

                echo json_encode($response);

            } else { // User Failed to store

                $response["error"] = TRUE;

                $response["error_message"] = "Unknown error occurred in registration";

                echo json_encode($response);

            }
        }
    }
    
} else {

    $response["error"] = TRUE;

    $response["error_message"] = "Required parameters (firstname, lastname, email or password) is missing!";

    echo json_encode($response);
}


?>