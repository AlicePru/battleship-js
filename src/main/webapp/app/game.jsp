<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Game started</title>
</head>
<body onload="checkStatus()">
<div id="wait-your-turn" class="w3-hide">
    <h1>Wait your turn to fire</h1>
</div>
<div id="enemy-field" class="w3-hide">
    <table border="2">
        <tr>
            <td>&nbsp;</td>
            <c:forTokens items="A,B,C,D,E,F,G,H,I,J" delims="," var="col">
                <td><c:out value="${col}"/></td>
            </c:forTokens>
        </tr>
        <c:forTokens items="1,2,3,4,5,6,7,8,9,10" delims="," var="row">
            <tr>
                <td><c:out value="${row}"/></td>
                <c:forTokens items="A,B,C,D,E,F,G,H,I,J" delims="," var="col">
                    <td><input type="checkbox" id="${col}${row}" onchange="cellClicked('${col}${row}')"/></td>
                </c:forTokens>
            </tr>
        </c:forTokens>
    </table>
    <button type="button" onclick="fire()">Fire!</button>
</div>

<script>

function fire() {
    console.log(JSON.stringify(data));
    fetch("<c:url value='/api/game/fire'/>", {
        "method": "POST",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(data)
    }).then(function (response) {
        console.log("DONE");
        checkStatus();
    });
}

function checkStatus() {
    console.log("checking status");
    fetch("<c:url value='/api/game/status'/>", {
        "method": "GET",
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
        }
    }).then(function (response) {
        return response.json();
    }).then(function (game) {
        console.log(JSON.stringify(game))
        if (game.status === "STARTED" && game.playerActive) {
            document.getElementById("enemy-field").classList.remove("w3-hide");
            document.getElementById("wait-your-turn").classList.add("w3-hide");
        } else if(game.status === "STARTED" && !game.playerActive){
            document.getElementById("enemy-field").classList.add("w3-hide");
            document.getElementById("wait-your-turn").classList.remove("w3-hide");

        }
            window.setTimeout(function () {
                checkStatus();
            }, 1000);

    });
}
</script>
</body>
</html>
