<!DOCTYPE html>
<html lang="en">
<head>
    <title>Latest</title>
    <link rel="stylesheet" href="/css/main.css">
    <link rel="stylesheet"
          href="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/styles/default.min.css">
    <script src="//cdn.jsdelivr.net/gh/highlightjs/cdn-release@10.2.1/build/highlight.min.js"></script>
    <script>hljs.initHighlightingOnLoad();</script>
</head>
<body>
<#list codeSnippets as snippets>
    <span id="load_date">${snippets.date.format(LOCAL_DATE_TIME)}</span>
    <pre id="code_snippet"><code>${snippets.code}</code></pre>
</#list>
</body>
</html>