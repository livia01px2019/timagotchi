<#assign content>
<div id="grid"></div>

<h1> Quiz Results </h1>
    <div class="quizzes">
        <table style="width:50%">
            <tr>
                <th align="left">Question</th>
                <th align="left">Answer</th>
            </tr>
            ${quizresult}
        </table>
    </div>

</#assign>
<#include "student-main.ftl">