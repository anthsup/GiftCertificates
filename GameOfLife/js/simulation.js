class Simulation {
    constructor() {
        this.LIFE_CELLS_DENSITY = Number($('#lifeCellsDensity').val());
        this.PREDATORS_DENSITY = Number($('#predatorsDensity').val());
        this.PREDATOR_LIFETIME_PERIOD = Number($('#predatorLifetimePeriod').val());
        this.REPRODUCTION_PERIOD = Number($('#reproductionPeriod').val());
        this.ITERATIONS = Number($('#iterations').val());
        this.UPDATE_INTERVAL_MILLISECONDS = Number($('#updateInterval').val());
        this.ENTITIES_REPRESENTATION = {
            lifeCell: 0,
            predator: 1,
            victim: -1
        };

        this.predatorsNumber = 0;
        this.victimsNumber = 0;
        this.iterationCounter = 0;
        this.gameArray = [];
        this.entities = {};
    }

    launchSimulation() {
        if (this.isSimulationOver()) {
            return true;
        }

        for (let i = 0; i < FIELD_SIZE; i++) {
            for (let j = 0; j < FIELD_SIZE; j++) {
                if (!this.gameArray[i][j]) {
                    continue;
                }

                this.entities[this.generateKey(i, j)].move(this);
            }
        }

        this.iterationCounter++;
    }

    predatorDies(predatorX, predatorY) {
        this.gameArray[predatorX][predatorY] = this.ENTITIES_REPRESENTATION.lifeCell; // predator died
        --this.predatorsNumber;
        delete this.entities[this.generateKey(predatorX, predatorY)];
    }

    predatorEatsVictim(victimX, victimY, predatorX, predatorY) {
        this.gameArray[victimX][victimY] = this.ENTITIES_REPRESENTATION.lifeCell;
        this.gameArray[predatorX][predatorY] = this.ENTITIES_REPRESENTATION.predator; // reset life time
        --this.victimsNumber;
        delete this.entities[this.generateKey(victimX, victimY)];
    }

    entityMovesOn(oldPositionX, oldPositionY, newPositionX, newPositionY, entityRepresentation) {
        this.gameArray[oldPositionX][oldPositionY] = this.ENTITIES_REPRESENTATION.lifeCell;
        const currentEntity = this.entities[this.generateKey(oldPositionX, oldPositionY)];
        if (entityRepresentation === this.ENTITIES_REPRESENTATION.victim) {
            this.entities[this.generateKey(newPositionX, newPositionY)] = currentEntity;
        } else {
            this.entities[this.generateKey(newPositionX, newPositionY)] = currentEntity;
        }
        delete this.entities[this.generateKey(oldPositionX, oldPositionY)];
    }

    entityReproduces(newPositionX, newPositionY, entityRepresentation) {
        if (entityRepresentation === this.ENTITIES_REPRESENTATION.victim) {
            this.victimsNumber++;
            this.entities[this.generateKey(newPositionX, newPositionY)] = new Victim(newPositionX, newPositionY);
        } else {
            this.predatorsNumber++;
            this.entities[this.generateKey(newPositionX, newPositionY)] = new Predator(newPositionX, newPositionY);
        }
    }

    createEntity(j, k) {
        const chances = Math.random();

        if (chances <= this.LIFE_CELLS_DENSITY) {
            return this.ENTITIES_REPRESENTATION.lifeCell;
        }

        if (chances <= (1 - this.LIFE_CELLS_DENSITY) * this.PREDATORS_DENSITY + this.LIFE_CELLS_DENSITY) {
            ++this.predatorsNumber;
            this.entities[this.generateKey(j, k)] = new Predator(j, k);
            return this.ENTITIES_REPRESENTATION.predator;
        }

        ++this.victimsNumber;
        this.entities[this.generateKey(j, k)] = new Victim(j, k);
        return this.ENTITIES_REPRESENTATION.victim;
    }

    create2DArray() { // creates a 2 dimensional array of required height
        let arr = [];
        for (let i = 0; i < FIELD_SIZE; i++) {
            arr[i] = [];
        }
        return arr;
    }

    fillArrayRandomly() { // fill the game array randomly
        for (let j = 0; j < FIELD_SIZE; j++) {
            for (let k = 0; k < FIELD_SIZE; k++) {
                this.gameArray[j][k] = this.createEntity(j, k);
            }
        }
    }

    init() {
        this.gameArray = this.create2DArray();
        this.fillArrayRandomly(); // create the starting state for the grid by filling game array with random cells
    }

    isSimulationOver() {
        if (this.iterationCounter === this.ITERATIONS) {
            alert("Game is over.\n" +
                "Predators left: " + this.predatorsNumber + "\n" +
                "Victims left: " + this.victimsNumber);
            return true;
        } else if ((this.victimsNumber + this.predatorsNumber) === (FIELD_SIZE * FIELD_SIZE)) {
            alert("Game is over. No life cells left.");
            return true;
        }
        return false;
    }

    // generates unique key for entities object based on entity's coordinates
    generateKey(x, y) {
        return x + ',' + y;
    }
}

let FIELD_SIZE;
let simulation;
let gameGrid;
let gameRunner;

$(function () {
    setDefaultParams();

    const validationInterval = setInterval(function() {
        if ($("input[type=number]:invalid").length > 0) {
            $("#startButton").attr("disabled", true);
            clearInterval(validationInterval);
        }
    }, 250);
});

$("#startButton").click(function () {
    FIELD_SIZE = Number($('#fieldSize').val());
    simulation = new Simulation();
    gameGrid = new GameGrid();
    simulation.init();
    gameGrid.init();

    if (gameRunner) {
        clearTimeout(gameRunner);
    }
    runSimulation();
});

function runSimulation() {
    gameGrid.drawGrid(simulation.gameArray, simulation.ENTITIES_REPRESENTATION);
    if (simulation.launchSimulation()) {
        return;
    }
    gameRunner = setTimeout(runSimulation, simulation.UPDATE_INTERVAL_MILLISECONDS, gameGrid.ctx);
}

function setDefaultParams() {
    const DEFAULT_FIELD_SIZE = 100;
    const DEFAULT_ITERATIONS_NUMBER = 100;
    const DEFAULT_LIFE_CELLS_DENSITY = 0.5;
    const DEFAULT_PREDATORS_DENSITY = 0.5;
    const DEFAULT_PREDATOR_LIFETIME = 50;
    const DEFAULT_REPRODUCTION_PERIOD = 50;
    const DEFAULT_UPDATE_INTERVAL = 100;

    $('#lifeCellsDensity').val(DEFAULT_LIFE_CELLS_DENSITY);
    $('#predatorsDensity').val(DEFAULT_PREDATORS_DENSITY);
    $('#predatorLifetimePeriod').val(DEFAULT_PREDATOR_LIFETIME);
    $('#reproductionPeriod').val(DEFAULT_REPRODUCTION_PERIOD);
    $('#iterations').val(DEFAULT_ITERATIONS_NUMBER);
    $('#updateInterval').val(DEFAULT_UPDATE_INTERVAL);
    $('#fieldSize').val(DEFAULT_FIELD_SIZE);
}