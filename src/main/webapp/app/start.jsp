<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body onload="printTable()">
<button type="button" onclick="logout()">Log out</button>
<button type="button" onclick="startGame()">Start Game</button>
<style>
    table {
        margin: auto;
        border: 1px solid crimson;
    }

    th {
        height: 5px;
        text-align: center;
        margin: auto;
        padding: auto;
        background-color: darksalmon;
        color: #000000;
        padding: 15px;

    }

    td {
        padding: 15px;
        height: 3px;
        text-align: center
    }
    div{
       color: crimson;
        text-decoration: underline;
        text-align: center;
    }
</style>
<div><h1>Table of winners with minimal count of moves</h1></div>
<table id="score" border="2">
    <tr>
        <th>GameId</th>
        <th>Username</th>
        <th>Moves</th>
    </tr>

</table>

<script>

    function printTable() {
        fetch("<c:url value='/api/game/wintable'/>", {
            "method": "GET",
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(function (response) {
            return response.json();
        }).then(function (game) {
            console.log(JSON.stringify(game));
            var table = document.getElementById("score");
            game.forEach(function (g) {
                var row = table.insertRow();
                var cell1 = row.insertCell(0);
                var cell2 = row.insertCell(1);
                var cell3 = row.insertCell(2);
                cell1.innerHTML = g.gameId;
                cell2.innerHTML = g.username;
                cell3.innerHTML = g.move;
            })
        });

    }


    function logout() {
        fetch("<c:url value='/api/auth/logout'/>", {"method": "POST"})
            .then(function (response) {
                location.href = "/";
            });
    }

    function startGame() {
        fetch("<c:url value='/api/game'/>", {"method": "POST"})
            .then(function (response) {
                location.href = "/app/placement.jsp";
            });
    }
</script>
</body>
</html>
