<!DOCTYPE HTML>
<html>
<head>
    <meta charset="utf-8">
    <title>Create</title>
    <style>
        body {
            color: #70AB9A;
        }
        textarea {
            width: 40vw;
            height: 25vh;
            border-width: 4px;
        }
        input {
            border-width: 2px;
        }
        button {
            border-width: 2px;
        }
    </style>
</head>
<body>
<h2 class="greetings">Post your code snippet</h2>
<p><textarea id="code_snippet">//write your code here</textarea></p>
<p>Time restriction: <input id="time_restriction" type="text"></p>
<p>Views restriction: <input id="views_restriction" type="text"></p>
<p><button id="send_snippet" type="submit" onclick="send()">Submit</button></p>
<script>
    function send() {
        let object = {
            "code": document.getElementById("code_snippet").value,
            "time": document.getElementById("time_restriction").value,
            "views": document.getElementById("views_restriction").value
        };
        let json = JSON.stringify(object);
        let xhr = new XMLHttpRequest();
        xhr.open("POST", '/api/code/new', false);
        xhr.setRequestHeader('Content-type', 'application/json; charset=utf-8');
        xhr.send(json);
        if (xhr.status == 200) { alert(xhr.response) };
    }
</script>
</body>
</html>