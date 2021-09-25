<?php
/**
 * DB_Connect
 * Opens Database Connection
 * @author Bill Glinton <tom.omuom@strathmore.edu>
 */
class DB_Connect {

    private $connection;
    
    public function connect() {
        
        $response = array("error" => FALSE); // json response array

        require_once "DB_Config.php"; // Database Configuration file
        
        $this->connection = new mysqli($servername, $username, $password, $database);
        
         return $this->connection;
        
    }
}
?>