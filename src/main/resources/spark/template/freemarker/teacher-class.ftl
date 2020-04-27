<!DOCTYPE html>

  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link href="https://fonts.googleapis.com/css?family=Source+Code+Pro&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="../css/normalize.css">
    <link rel="stylesheet" href="../css/html5bp.css">
    <link rel="stylesheet" href="../css/w3.css">
    <link rel="stylesheet" href="../css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&family=Roboto:wght@300&display=swap" rel="stylesheet">
  </head>
  <body>
  	 <#include "teacher-sidebar.ftl">
  	 <#include "header-student.ftl">
  	 <div class="after-header-spacing"></div>
     <div style="padding-left: 20%">
         <div class="profile">
             <h3 style="font-size:50px">${className}</h3>
             <h3 id="class-code">Code: ${classId}</h3>
         </div>

         <div class="tab">
             <button class="tablinks" id="assignments">&nbsp;
                 <a href="main"><img class="class-logo" src="../img/assignment-icon.png"></a>
                 Assignments&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
             <button class="ghost"></button>
             <button class="tablinks" id="students">&nbsp;
                 <a href="main"><img class="class-logo" src="../img/skin4.png"></a>
                 Students&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
         </div>
         <div id="Assignments" class="tabcontent">
             <button class="assignments" id="create-assignment">+</button>
             <ul id="assignments-list"></ul>
         </div>
         <div id="Students" class="tabcontent"></div>
     </div>

     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="../js/jquery-2.1.1.js"></script>
     <script src="../js/main.js"></script>
     <script src="../js/teacher-class.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
