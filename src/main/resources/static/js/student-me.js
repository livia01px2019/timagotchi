// BEGIN REDACT
/**
 * Front end logic for student home page.
 * Sounds from https://www.youtube.com/watch?v=0brnh65myJU and https://www.youtube.com/watch?v=h5g9fbuA7Hc
 */

$(document).ready(() => {
    document.getElementById("lbutton").onclick = lbutton;
    function lbutton() {
        const pet = document.getElementById("pet");
		const petContainer = document.getElementById("skin-container");
        if (pet.src.includes("stage1") || pet.src.includes("stage2") || pet.src.includes("stage3")) {
            if (pet.src.includes("stage1")) {
                const audio = new Audio("../audio/stage1.mp3");
                audio.play();
            } else if (pet.src.includes("stage2")) {
                const audio = new Audio("../audio/stage2.mp3");
                audio.play();
            } else if (pet.src.includes("stage3")) {
                const audio = new Audio("../audio/stage3.mp3");
                audio.play();
            }

            pet.style.transform = "rotate(-3deg)";
            setTimeout(function(){pet.style.transform = "rotate(-6deg)";}, 20);
            setTimeout(function(){pet.style.transform = "rotate(-10deg)";}, 40);
            setTimeout(function(){pet.style.transform = "rotate(-6deg)";}, 60);
            setTimeout(function(){pet.style.transform = "rotate(-3deg)";}, 80);
            setTimeout(function(){pet.style.transform = "rotate(0deg)";}, 100);
        } else {
            const audio = new Audio("../audio/stage4.mp3");
            audio.play();

            pet.style.top = "345px";
            pet.style.left = "517px";
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "514px";}, 55);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "511px";}, 110);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "508px";}, 165);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "505px";}, 220);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "502px";}, 275);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "499px";}, 330);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "496px";}, 385);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "499px";}, 450);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "502px";}, 505);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "505px";}, 560);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "508px";}, 615);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "511px";}, 670);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "514px";}, 725);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "517px";}, 780);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "520px";}, 835);
        }
    }

    document.getElementById("rbutton").onclick = rbutton;
    function rbutton() {
        const pet = document.getElementById("pet");
		const petContainer = document.getElementById("skin-container");
        if (pet.src.includes("stage1") || pet.src.includes("stage2") || pet.src.includes("stage3")) {
            if (pet.src.includes("stage1")) {
                const audio = new Audio("../audio/stage1.mp3");
                audio.play();
            } else if (pet.src.includes("stage2")) {
                const audio = new Audio("../audio/stage2.mp3");
                audio.play();
            } else if (pet.src.includes("stage3")) {
                const audio = new Audio("../audio/stage3.mp3");
                audio.play();
            }

            pet.style.transform = "rotate(3deg)";
            setTimeout(function(){pet.style.transform = "rotate(6deg)";}, 20);
            setTimeout(function(){pet.style.transform = "rotate(10deg)";}, 40);
            setTimeout(function(){pet.style.transform = "rotate(6deg)";}, 60);
            setTimeout(function(){pet.style.transform = "rotate(3deg)";}, 80);
            setTimeout(function(){pet.style.transform = "rotate(0deg)";}, 100);
        } else {
            const audio = new Audio("../audio/stage4.mp3");
            audio.play();

            pet.style.top = "345px";
            pet.style.left = "523px";
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "526px";}, 55);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "529px";}, 110);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "532px";}, 165);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "535px";}, 220);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "538px";}, 275);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "541px";}, 330);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "544px";}, 385);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "541px";}, 450);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "538px";}, 505);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "535px";}, 560);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "532px";}, 615);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "529px";}, 670);
            setTimeout(function(){petContainer.style.top = "340px";petContainer.style.left = "526px";}, 725);
            setTimeout(function(){petContainer.style.top = "345px";petContainer.style.left = "523px";}, 780);
            setTimeout(function(){petContainer.style.top = "350px";petContainer.style.left = "520px";}, 835);
        }
    }
    
    let eggArray = [
    	"Wonder what's inside? It needs more time, though.",
    	"What will hatch from this? It will take some time.",
    	"What Timagotchi will hatch from this Egg?",
    	"It moves occasionally. It should hatch soon.",
    	"It appears to move occasionally. It may be close to hatching."
    ];
    
    let eggCrackingArray = [
    	"It's making sounds inside. It's going to hatch soon!",
    	"It's making sounds! It's about to hatch!",
    	"Sounds can be heard coming from inside! It will hatch soon!",
    	"Sounds can be heard coming from inside! This Egg will hatch soon!"
    ];
    
    let babyArray = [
    	"\"Waah!\"",
    	"\"Dadda?\"",
		"\"Momma?\"",
    	"\"Bwubba!?\"",
    	"It's hungry! Help it grow!"
    ];
    
    function typeWriter() {
	  if (i < txt.length) {
	  	eventsEnabled = false;
		document.getElementById("demo").innerHTML += txt.charAt(i);
		i++;
		setTimeout(typeWriter, speed);
	  }
	  if (i >= txt.length) {
	  	eventsEnabled = true;
	  }
	}
    
	let i = 0;
	let txt = "";
	let speed = 50;
	let eventsEnabled = true;
	
    document.getElementById("mbutton").onclick = mbutton;
    function mbutton() {
    	if (eventsEnabled == true) {
    		document.getElementById("demo").innerHTML = "";
    		i = 0;
		    const pet = document.getElementById("pet");
			const petContainer = document.getElementById("skin-container");
		    if (pet.src.includes("stage1") || pet.src.includes("stage2") || pet.src.includes("stage3")) {
		        if (pet.src.includes("stage1")) {
		            const audio = new Audio("../audio/stage1.mp3");
		            audio.play();
		        } else if (pet.src.includes("stage2")) {
		            const audio = new Audio("../audio/stage2.mp3");
		            audio.play();
		        } else if (pet.src.includes("stage3")) {
		            const audio = new Audio("../audio/stage3.mp3");
		            audio.play();
		        }

		        pet.style.top = "346px";
		        setTimeout(function(){petContainer.style.top = "342px";}, 20);
		        setTimeout(function(){petContainer.style.top = "338px";}, 40);
		        setTimeout(function(){petContainer.style.top = "342px";}, 60);
		        setTimeout(function(){petContainer.style.top = "346px";}, 80);
		        setTimeout(function(){petContainer.style.top = "350px";}, 100);
		        setTimeout(function(){petContainer.style.top = "354px";}, 120);
		        setTimeout(function(){petContainer.style.top = "358px";}, 140);
		        setTimeout(function(){petContainer.style.top = "354px";}, 160);
		        setTimeout(function(){petContainer.style.top = "350px";}, 180);
		    } else {
		        const audio = new Audio("../audio/stage5.mp3");
		        audio.volume = 0.7;
		        audio.play();

		        pet.style.transform = "rotate(20deg)";
		        setTimeout(function(){pet.style.transform = "rotate(40deg)";}, 60);
		        setTimeout(function(){pet.style.transform = "rotate(60deg)";}, 120);
		        setTimeout(function(){pet.style.transform = "rotate(80deg)";}, 180);
		        setTimeout(function(){pet.style.transform = "rotate(100deg)";}, 240);
		        setTimeout(function(){pet.style.transform = "rotate(120deg)";}, 300);
		        setTimeout(function(){pet.style.transform = "rotate(140deg)";}, 360);
		        setTimeout(function(){pet.style.transform = "rotate(160deg)";}, 420);
		        setTimeout(function(){pet.style.transform = "rotate(180deg)";}, 480);
		        setTimeout(function(){pet.style.transform = "rotate(200deg)";}, 540);
		        setTimeout(function(){pet.style.transform = "rotate(220deg)";}, 600);
		        setTimeout(function(){pet.style.transform = "rotate(240deg)";}, 660);
		        setTimeout(function(){pet.style.transform = "rotate(260deg)";}, 720);
		        setTimeout(function(){pet.style.transform = "rotate(280deg)";}, 780);
		        setTimeout(function(){pet.style.transform = "rotate(300deg)";}, 840);
		        setTimeout(function(){pet.style.transform = "rotate(320deg)";}, 900);
		        setTimeout(function(){pet.style.transform = "rotate(340deg)";}, 960);
		        setTimeout(function(){pet.style.transform = "rotate(360deg)";}, 1020);
		        setTimeout(function(){pet.style.transform = "rotate(380deg)";}, 1080);
		        setTimeout(function(){pet.style.transform = "rotate(400deg)";}, 1140);
		        setTimeout(function(){pet.style.transform = "rotate(420deg)";}, 1200);
		        setTimeout(function(){pet.style.transform = "rotate(440deg)";}, 1260);
		        setTimeout(function(){pet.style.transform = "rotate(460deg)";}, 1320);
		        setTimeout(function(){pet.style.transform = "rotate(480deg)";}, 1380);
		        setTimeout(function(){pet.style.transform = "rotate(500deg)";}, 1440);
		        setTimeout(function(){pet.style.transform = "rotate(520deg)";}, 1500);
		        setTimeout(function(){pet.style.transform = "rotate(540deg)";}, 1560);
		        setTimeout(function(){pet.style.transform = "rotate(560deg)";}, 1620);
		        setTimeout(function(){pet.style.transform = "rotate(580deg)";}, 1680);
		        setTimeout(function(){pet.style.transform = "rotate(600deg)";}, 1740);
		        setTimeout(function(){pet.style.transform = "rotate(620deg)";}, 1800);
		        setTimeout(function(){pet.style.transform = "rotate(640deg)";}, 1860);
		        setTimeout(function(){pet.style.transform = "rotate(660deg)";}, 1920);
		        setTimeout(function(){pet.style.transform = "rotate(680deg)";}, 1980);
		        setTimeout(function(){pet.style.transform = "rotate(700deg)";}, 2040);
		        setTimeout(function(){pet.style.transform = "rotate(720deg)";}, 2100);
		    }
		    if (pet.src.includes("stage1")) {
		    	txt = eggArray[Math.floor(Math.random()*eggArray.length)];
		    	typeWriter();
		    }
		    if (pet.src.includes("stage2")) {
		    	txt = eggCrackingArray[Math.floor(Math.random()*eggCrackingArray.length)];
		    	typeWriter();
		    }
		    if (pet.src.includes("stage3")) {
		    	txt = babyArray[Math.floor(Math.random()*babyArray.length)];
		    	typeWriter();
		    }
    	}
    }
});
