<html>
<body>
<h1>字符串属性</h1>
<p>${userName!""}</p>
<h1>日期属性</h1>
<p>${(date?string('yyyy-MM-dd hh:mm:ss'))!'日期为null'}</p>
<h1>循环</h1>
<div>
<#list list as item>
    <p>第${item_index+1}个用户</p>
    <p>用户名：${item.name}</p>
    <p>id：${item.id}</p>
</#list>
</div>
<h1>判断1</h1>
<#if userName =='刘志强'>
    <P>存在刘志强</p>
<#elseif userName =='王妍'>
    <P>存在王妍</P>
<#else>
    <P>不存在刘志强和王妍</P>
</#if>
<h1>判断2</h1>
<#assign foo = (userName =='王妍')>
${foo?then('存在王妍', '不存在王妍')}
</html>