<#assign content>
<div id="grid"></div>

<h1> STUDENT: ASSIGNMENTS </h1>
  <table align="center" style="width: 100%"><tr><td>
        <h2> Quizzes </h2>

        <div class="quizzes">
          <table style="width:100%">
            <tr>
              <th align="left">Quiz</th>
              <th align="left">Status</th>
            </tr>
            ${quizlist}
          </table>
        </div>
      </td><td style="padding-left:10%; padding-right:5%">
        <h2> Checkoff Work </h2>

        <div class="checkoff">
          <table style="width:100%">
            <tr>
              <th align="left">Assignment</th>
              <th align="left">Status</th>
            ${checkofflist}
          </table>
        </div>
      </td></tr>
  </table>
</#assign>
<#include "student-main.ftl">