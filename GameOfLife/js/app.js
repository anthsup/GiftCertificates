let canvasSize;
let environmentField;
let ctx;
let fieldSize;
let predatorLifetimePeriod;
let reproductionPeriod;
let lifeCellsDensity;
let predatorsDensity;
let iterations;
let predatorImage;
let victimImage;
let millisecondsSpeed;
let predatorsNumber;
let victimsNumber;
let entities;
let stopper;
let gameGrid;
let counter;

function setParameters() {
    environmentField = document.getElementById('environment');
    ctx = environmentField.getContext('2d');
    fieldSize = Number(document.getElementById('fieldSize').value);
    canvasSize = fieldSize * 6;
    predatorLifetimePeriod = Number(document.getElementById('predatorLifetimePeriod').value);
    reproductionPeriod = Number(document.getElementById('reproductionPeriod').value);
    lifeCellsDensity = Number(document.getElementById('lifeCellsDensity').value);
    predatorsDensity = Number(document.getElementById('predatorsDensity').value);
    iterations = Number(document.getElementById('iterations').value);
    predatorImage = new Image();
    victimImage = new Image();
    predatorImage.src = 'img/red.png';
    victimImage.src = 'img/blue.png';
    millisecondsSpeed = Number(document.getElementById('updateInterval').value);
    predatorsNumber = 0;
    victimsNumber = 0;
    entities = {
        lifeCell: 0,
        predator: 1,
        victim: -1
    };
    counter = 0;
}

function start() {
    if (stopper != undefined) {
        clearTimeout(stopper);
    }

    setParameters();

    environmentField.width = environmentField.height = canvasSize;
    environmentField.style.visibility = "visible";
    ctx.fillRect(0, 0, environmentField.width, environmentField.height);
    gameGrid = createArray(fieldSize);
    fillRandom(); // create the starting state for the grid by filling it with random cells
    updateGrid(ctx);
}

function createArray(rows) { // creates a 2 dimensional array of required height
    let arr = [];
    for (let i = 0; i < rows; i++) {
        arr[i] = [];
    }
    return arr;
}

function fillRandom() { // fill the grid randomly
    for (let j = 0; j < fieldSize; j++) {
        for (let k = 0; k < fieldSize; k++) {
            gameGrid[j][k] = createEntity();
        }
    }
}

function createEntity() {
    const chances = Math.random();

    if (chances <= lifeCellsDensity) {
        return entities.lifeCell;
    }

    if (chances <= (1 - lifeCellsDensity) * predatorsDensity + lifeCellsDensity) {
        ++predatorsNumber;
        return entities.predator;
    }

    ++victimsNumber;
    return entities.victim;
}

function drawGrid(ctx) {
    ctx.clearRect(0, 0, canvasSize, canvasSize); //this should clear the canvas ahead of each redraw
    let box = canvasSize / fieldSize;
    for (let i = 0; i < fieldSize; i++) {
        for (let j = 0; j < fieldSize; j++) {
            let entity = gameGrid[i][j];
            if (entity !== entities.lifeCell) {
                ctx.drawImage((entity >= entities.predator ? predatorImage : victimImage), box * i, box * j, box, box);
            }
        }
    }
    ctx.save();
}

function updateGrid(ctx) {
    drawGrid(ctx);

    if (isSimulationOver()) {
        return;
    }

    for (let j = 0; j < fieldSize; j++) {
        for (let k = 0; k < fieldSize; k++) {
            let entity = gameGrid[j][k];
            if (entity === entities.lifeCell) {
                continue;
            }

            let newPositionX = generateNewPositionX(j);
            let newPositionY = generateNewPositionY(k);

            let newEntity = gameGrid[newPositionX][newPositionY];

            if (entity >= entities.predator) {
                makePredatorsMove(newEntity, j, k, newPositionX, newPositionY);
            } else if (entity === entities.victim) {
                makeVictimsMove(newEntity, j, k, newPositionX, newPositionY);
            }
        }
    }

    counter++;
    stopper = setTimeout(updateGrid, millisecondsSpeed, ctx);
}

function generateNewPositionX(j) {
    let newPositionX = Math.abs(j + Math.ceil(Math.random() * 3) - 2);

    if (newPositionX >= fieldSize) {
        newPositionX = 2 * fieldSize - newPositionX - 1;
    }

    return newPositionX;
}

function generateNewPositionY(k) {
    let newPositionY = Math.abs(k + Math.ceil(Math.random() * 3) - 2);

    if (newPositionY >= fieldSize) {
        newPositionY = 2 * fieldSize - newPositionY - 1;
    }

    return newPositionY;
}

function makePredatorsMove(newEntity, j, k, newPositionX, newPositionY) {
    if (newEntity === entities.victim) {
        gameGrid[j][k] = entities.predator; // reset life time
        gameGrid[newPositionX][newPositionY] = entities.lifeCell;
        --victimsNumber;
    } else {
        gameGrid[j][k] = gameGrid[j][k] + 1;
        if (gameGrid[j][k] >= predatorLifetimePeriod) {
            gameGrid[j][k] = entities.lifeCell; // predator died
            --predatorsNumber;
        } else if (newEntity === entities.lifeCell) { // predator didn't eat victim, didn't die, so he moves on
            if (counter % reproductionPeriod === 0) {
                predatorsNumber++;
                gameGrid[newPositionX][newPositionY] = entities.predator;
            } else {
                gameGrid[newPositionX][newPositionY] = gameGrid[j][k];
                gameGrid[j][k] = entities.lifeCell;
            }
        }
    }
}

function makeVictimsMove(newEntity, j, k, newPositionX, newPositionY) {
    if (newEntity >= entities.predator) {
        gameGrid[newPositionX][newPositionY] = entities.predator;
        gameGrid[j][k] = entities.lifeCell; // predator ate victim
        --victimsNumber;
    } else if (newEntity === entities.lifeCell) { // victim moves
        if (counter % reproductionPeriod === 0) {
            gameGrid[newPositionX][newPositionY] = entities.victim;
            victimsNumber++;
        } else {
            gameGrid[j][k] = entities.lifeCell;
            gameGrid[newPositionX][newPositionY] = entities.victim;
        }
    }
}

function isSimulationOver() {
    if (counter === iterations) {
        alert("Game is over.\n" +
            "Predators left: " + predatorsNumber + "\n" +
            "Victims left: " + victimsNumber);
        return true;
    } else if ((victimsNumber + predatorsNumber) === (fieldSize * fieldSize)) {
        alert("Game is over. No life cells left.");
        return true;
    }
    return false;
}