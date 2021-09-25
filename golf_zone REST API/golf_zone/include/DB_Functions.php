<?php

class DB_Functions {
    
    private $connection;

    function __construct() { // Constructor

        require_once 'DB_Connect.php'; // Database Connection File

        $database = new DB_Connect(); // Create a new Database Connection Object

        $this->connection = $database->connect(); // Obtain Database Connection
    }

    function __destruct() { // Destructor
        
    }

    /**
     * createUser
     * 
     * Creates new user and returns user details
     *
     * @param mixed $username
     * @param mixed $email
     * @param mixed $password
     * @return void
     */
    public function createUser($firstname, $lastname, $email, $password) {
        
        $unique_user_id = uniqid('', true); // Generates Unique User ID

        $encrypted_password = password_hash($password, PASSWORD_DEFAULT); // Hashes the password

        $prepared_statement = $this->connection->prepare("INSERT INTO users_97292 (unique_user_id, firstname, lastname, email, encrypted_password, created_at) VALUES (?, ?, ?, ?, ?, now())");

        $prepared_statement->bind_param("sssss", $unique_user_id, $firstname, $lastname, $email, $encrypted_password);

        $result = $prepared_statement->execute();

        $prepared_statement->close();

        if ($result) { // Check if the user was successfully stored
            
            $prepared_statement = $this->connection->prepare("SELECT * from users_97292 WHERE email = ?");

            $prepared_statement->bind_param('s', $email);

            $prepared_statement->execute();

            $user = $prepared_statement->get_result()->fetch_assoc();

            $prepared_statement->close();

            return $user;

        }

    }

    /**
     * authenticateUser
     * 
     * Authenticates User
     *
     * @param mixed $email
     * @param mixed $password
     * @return void
     */
    public function authenticateUser($email, $password) {
        
        $prepared_statement = $this->connection->prepare("SELECT * from users_97292 WHERE email = ?");

        $prepared_statement->bind_param("s", $email);

        $retrieve = $prepared_statement->execute();

        if ($retrieve) {
            
            $user = $prepared_statement->get_result()->fetch_assoc(); // Fetch Database results
            
            if ($user) {
                
                $check_password = password_verify($password, $user['encrypted_password']);

                if ($check_password) { // Check for password equality
    
                    return $user; // User Authentication Details are correct
    
                }
            }
            
        } else {
            
            return NULL;
        }
        
        $prepared_statement->close();

    }
    
    /**
     * checkUser
     *
     * @param  mixed $email
     * @return void
     */
    public function checkUser($email){

        $prepared_statement = $this->connection->prepare("SELECT email from users_97292 WHERE email = ?");

        $prepared_statement->bind_param("s", $email);

        $prepared_statement->execute();

        $prepared_statement->store_result();

        if ($prepared_statement->num_rows > 0) { // User exists

            $prepared_statement->close();

            return true;

        } else { // User does not exist

            $prepared_statement->close();

            return false;

        }
    }
}
?>