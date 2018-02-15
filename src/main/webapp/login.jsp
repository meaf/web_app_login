<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>login test</title>
    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<div class="container">

    <form method="POST" action="j_security_check">

        <input id="requestURI" type="hidden" value="/" name="requestURI"/>

        <h2 class="form-signin-heading" >oh shit not you again</h2>

        <label for="username" class="sr-only">Login</label>
        <input class="form-control" id="username" required="required" type="text" name="j_username"/>

        <label for="password" class="sr-only">Password</label>
        <input class="form-control" id="password" required="required" type="password" name="j_password"/>

        <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
    </form>

</div>


</body>
</html>