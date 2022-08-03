

function drawBars(document) {
    let aCanvas = document.getElementById("myCanvas");
    let aContext = aCanvas.getContext("2d");    

    let ind = 0;
    let kDrawDelay = 80;
    let allGames = getAllGames();

    let myInterval = setInterval(function ()
    {
         console.log("setInterval");

         document.getElementById("currentGame").innerHTML = ind + 1 === allGames.length ? "Done" : ind;
         document.getElementById("date").innerHTML = allGames[ind].date;

         drawGame(aContext, allGames, ind);

         ind++;
         if (ind === allGames.length) {
            clearInterval(myInterval);
            console.log("Done!");
         }
    }, kDrawDelay);


}

function drawGame(aContext, allGames, ind) {
    
}


function sayHi() {
    // console.log(getAllGames());
    // alert(data());
}