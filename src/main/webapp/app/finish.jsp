<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <title>Result</title>
</head>
<body onload="checkWinner()">
<h1>GAME OVER</h1>
<div id="winner" class="w3-hide">
    <h3>You are winner!</h3>
</div>
<div id="loser" class="w3-hide">
    <h3>You are looser :(</h3>
</div>
<button id="go-back" onclick="goBack()">Go to the main page</button>
<script>
    function checkWinner(){
        fetch("<c:url value='/api/game/status'/>", {
            "method": "GET",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            return response.json();
        }).then(function (game) {
            console.log(JSON.stringify(game));
            if( game.status==="FINISHED" &&game.playerActive){
                document.getElementById("winner").classList.add("w3-hide");
                document.getElementById("loser").classList.remove("w3-hide");

            }else if( game.status==="FINISHED" && !game.playerActive) {
                    document.getElementById("winner").classList.remove("w3-hide");
                    document.getElementById("loser").classList.add("w3-hide");

            }
        });


    }

   function goBack(){
       location.href = "<c:url value='/app/start.jsp'/>";
   }
</script>
</body>
</html>