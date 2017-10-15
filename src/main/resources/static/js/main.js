'use strict';

var usernamePage = document.querySelector('#username-page');
var chatPage = document.querySelector('#number-page');
var winPage = document.querySelector('#win-page');
var lostPage = document.querySelector('#lost-page');
var playAgain = document.querySelector('#play-again');
var usernameButton = document.querySelector('#usernameButton');
var moveButton = document.querySelector('#moveButton');
var plusOneButton = document.querySelector('#btnPlus');
var minusOneButton = document.querySelector('#btnMinus');
var replayButton = document.querySelector('#replay');
var simulate = document.querySelector('#simulate');

var stompClient = null;
var username = null;
var message = 'Ready?';
var userId = null;
var gameId = null;
var newNumber = Number(0);
var oldNumber = Number(0);
var min = Number(0);
var max = Number(0);
var one = Number(1);
var tpid = null;
var finished = false;
var subscribedGameChannel = false;
var jsonBody = null;


var cnnct = function connect(event) {

    username = $('#username').val();

    if (username) {

        usernamePage.classList.add('hidden');
        chatPage.classList.remove('hidden');

        var socket = new SockJS('/dws');
        stompClient = Stomp.over(socket);

        stompClient.connect({}, onConnected, onError);
    }
    event.preventDefault();
};

function onConnected() {
    console.log('connected dws');
    joinGame();

}

function joinGame() {
    // Subscribe to the Private User Channel
    stompClient.subscribe('/ws/channel/game/player/' + username, onMessageReceived);

    stompClient.send("/ws/divider/game/join/" + username);

}

var mv = function move() {
    tpid = jsonBody.turnPlayerId;
    if (tpid === userId) {
        var number = Number(document.getElementById("inp-number").value);
        if (!(number === oldNumber || number + one === oldNumber || number - one === oldNumber)) {
            document.getElementById("inp-number").value = oldNumber;
            alert("check number, plus 1 or minus 1");
        } else if (number % 3 !== 0) {
            alert("number must be divisible by three");
        } else {
            stompClient.send("/ws/divider/game/" + gameId + "/player/" + userId + "/move/" + number);
        }
    }
};

function setMessage() {
    if (jsonBody.gameStateId == 1) {
        if (jsonBody.turnPlayerId === userId) {
            $('#messageBox').val("Your turn !");
        } else {
            $('#messageBox').val("Opponent's turn!");
        }
    } else {
        $('#messageBox').val(jsonBody.message);
    }
}

function onMessageReceived(payload) {
    jsonBody = JSON.parse(payload.body);
    gameId = jsonBody.gameId;
    if (!subscribedGameChannel && gameId != null) {
        stompClient.subscribe('/ws/channel/game/' + gameId, onMessageReceived);
        subscribedGameChannel = true;
        console.log(gameId);
        if (jsonBody.game.playerTwoId) {
            userId = jsonBody.game.playerTwoId;
        } else {
            userId = jsonBody.game.playerOneId;
        }
    }

    console.log("jsonBody.game.gameStateId-->", jsonBody.gameStateId);
    setMessage();
    setNumber();
    if (jsonBody.gameStateId === 0) {
    } else if (jsonBody.gameStateId == 2) {
        finishGame();
    } else if (jsonBody.gameStateId == 1) {
        setTurnAndSimulate();
    } else if (jsonBody.gameStateId == 3) {
        finishGame();
    } else {
        // terminateGame();
    }
}


var plsN = function plusOne() {
    console.log('plusOne');
    console.log(max);
    var numberNumb = Number(document.getElementById("inp-number").value);
    if (numberNumb + one > max) {
        alert('plus one minus one');
    } else {
        document.getElementById("inp-number").value = numberNumb + one;
    }
};

var mnsN = function minusOne() {
    console.log('minusOne');
    console.log(min);
    var numberNumb = Number(document.getElementById("inp-number").value);
    if (numberNumb - one < min) {
        alert('plus one minus one');
    } else {
        document.getElementById("inp-number").value = numberNumb - one;
    }
};


function onError(error) {
    console.log('not connected dws ' + error);
    stompClient.disconnect();
}

function terminateGame() {
    stompClient.send("/ws/divider/game/" + gameId + "/player/" + userId + "/termin");
}

function replay() {
    location.reload();
}


function setNumber() {
    if (jsonBody.game.lastMove) {
        document.getElementById("inp-number").value = jsonBody.game.lastMove.number;
        newNumber = Number(jsonBody.game.lastMove.number);
        oldNumber = Number(jsonBody.game.lastMove.number);
        min = newNumber - 1;
        max = newNumber + 1;
    }
}

function finishGame() {
    enableDisableButtons(true);
    finished = true;
    chatPage.classList.add('hidden');
    playAgain.classList.remove('hidden');
    if (jsonBody.winnerPlayerId === userId) {
        winPage.classList.remove('hidden');
    } else {
        lostPage.classList.remove('hidden');
    }
}

function enableDisableButtons(b) {
    $("#moveButton").prop("disabled", b).off('click');
    $("#btnPlus").prop("disabled", b).off('click');
    $("#btnMinus").prop("disabled", b).off('click');
}

function checkSimulate() {

    if ($("#simulate").is(":checked")) {
        document.getElementById("inp-number").value = (newNumber + one) % 3 === 0 ?
            newNumber + one : (newNumber - one) % 3 === 0 ? newNumber - one : newNumber;
        mv();
    }
}

function setTurnAndSimulate() {
    tpid = jsonBody.turnPlayerId;
    console.log("jsonBody.turnPlayerId-->", jsonBody.turnPlayerId);
    if (tpid === userId) {
        enableDisableButtons(false);
        if (!finished) {
            if (simulate.checked) {
                document.getElementById("inp-number").value = (newNumber + one) % 3 === 0 ?
                    newNumber + one : (newNumber - one) % 3 === 0 ? newNumber - one : newNumber;
                mv();
            }
        }
    } else {
        enableDisableButtons(true);
    }
}


usernameButton.addEventListener('click', cnnct, true);
moveButton.addEventListener('click', mv, true);
plusOneButton.addEventListener('click', plsN, true);
minusOneButton.addEventListener('click', mnsN, true);
replayButton.addEventListener('click', replay, true);
simulate.addEventListener('click', checkSimulate, true);
$(window).bind('beforeunload', function () {
    return 'Are you sure you want to leave?';
});
$(window).bind('unload', terminateGame);