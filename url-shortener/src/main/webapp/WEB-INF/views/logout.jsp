<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Logging Out...</title>
    <script>
        window.onload = function () {
            const form = document.createElement("form");
            form.method = "POST";
            form.action = "/logout";
            document.body.appendChild(form);
            form.submit();
        };
    </script>
</head>
<body>
    <h2>Logging out, please wait...</h2>
</body>
</html>
