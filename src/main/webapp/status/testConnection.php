<?php

try {


    echo "Test PDO-> start";
    $db = "";
    $user = "";
    $pass = "";
    $host = "";

    $dbh = new PDO('mysql:host='.$host.';dbname='.$db, $user, $pass);
    echo "Test PDO-> " . var_dump($dbh);
    foreach ($dbh->query('SELECT * from tables') as $row) {
        print_r($row);
    }
    $dbh = null;
    echo "Test PDO-> end";
} catch (PDOException $e) {
    print "Error!: " . $e->getMessage() . "<br/>";
    die();
}
?>
