<!DOCTYPE HTML>
<html>
<head>
    <title>Code</title>
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
    <style>
        pre {
            width: 40vw;
            height: 25vh;
            border-width: 4px;
        }
    </style>
    <meta charset="UTF-8">
</head>
<body>
<h2 class="greetings">Code snippet by direct link</h2>
<div>
    <p><span id="load_date" class="time">${date}</span></p>
    <#if (views > 0)>
        <p><span id="views_restriction">${views} more views allowed</span></p>
    <#elseif views == 0 && time == 0>
        <p><span id="views_restriction">0 more views allowed</span></p>
    </#if>
    <#if (time > 0)>
        <p><span id="time_restriction">The code will be available for ${time} seconds</span></p>
    </#if>
</div>
<div>
    <form id="codeForm">
        <pre id="code_snippet"><code>${code}</code></pre>
    </form>
</div>
</body>
</html>