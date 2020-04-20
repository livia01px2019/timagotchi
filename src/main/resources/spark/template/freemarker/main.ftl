<!DOCTYPE html>
  <head>
    <meta charset="utf-8">
    <title>${title}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- In real-world webapps, css is usually minified and
         concatenated. Here, separate normalize from our code, and
         avoid minification for clarity. -->
    <link rel="stylesheet" href="css/normalize.css">
    <link rel="stylesheet" href="css/html5bp.css">
    <link rel="stylesheet" href="css/main.css">
    <link href="https://fonts.googleapis.com/css2?family=Press+Start+2P&family=Roboto:wght@300&display=swap" rel="stylesheet">
  </head>
  <body>
  	 <div class="header" id="header">
  	 	<img class="logo" src="img/main-skin-right.png" alt="timagotchi logo right">
  	 	<h1> TIMAGOTCHI </h1>
  	 	<img class="logo" src="img/main-skin-left.png" alt="timagotchi logo left">
  	 </div>
     ${content}
     <!-- Again, we're serving up the unminified source for clarity. -->
     <script src="js/jquery-2.1.1.js"></script>
     <script src="js/main.js"></script>
  </body>
  <!-- See http://html5boilerplate.com/ for a good place to start
       dealing with real world issues like old browsers.  -->
</html>
