<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Title</title>
</head>
<body onload="printTable()">
<button type="button" onclick="logout()">Log out</button>
<button type="button" onclick="startGame()">Start Game</button>

<table id="score">
    <%--<tr>--%>
        <%--<td>--%>
            <%--<c:forTokens items="gameId,userId,move" delims="," var="col">--%>
        <%--<td>--%>
            <%--<c:out value="${col}"/></td>--%>
        <%--</c:forTokens>--%>
        <%--</td>--%>
    <%--</tr>--%>
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
        }).then(function (users) {
            console.log(JSON.stringify(users));
            var table = document.getElementById("score");
            users.forEach(function (u) {
                var row = table.insertRow();
               // var cell1=row.insertCell(0);
               var cell2=row.insertCell(1);
               var cell3=row.insertCell(2);
               // cell1.innerHTML(u.gameId);
               cell2.innerHTML(u.userID);
               cell3.innerHTML(u.move);
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
