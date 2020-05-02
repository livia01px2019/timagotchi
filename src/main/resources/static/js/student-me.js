// BEGIN REDACT
/**
 * Front end logic for student home page.
 */

$(document).ready(() => {
    document.getElementById("lbutton").onclick = lbutton;
    function lbutton() {
        const pet = document.getElementById("pet");
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
            setTimeout(function(){
                pet.style.transform = "rotate(-6deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(-10deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(-6deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(-3deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(0deg)";
            }, 100);
        }
    }

    document.getElementById("rbutton").onclick = rbutton;
    function rbutton() {
        const pet = document.getElementById("pet");
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
            setTimeout(function(){
                pet.style.transform = "rotate(6deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(10deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(6deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(3deg)";
            }, 100);
            setTimeout(function(){
                pet.style.transform = "rotate(0deg)";
            }, 100);
        }
    }

    document.getElementById("mbutton").onclick = mbutton;
    function mbutton() {
        const pet = document.getElementById("pet");
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
            setTimeout(function(){
                pet.style.top = "342px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "338px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "342px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "346px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "350px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "354px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "358px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "354px";
            }, 100);
            setTimeout(function(){
                pet.style.top = "350px";
            }, 100);

        }
    }
});