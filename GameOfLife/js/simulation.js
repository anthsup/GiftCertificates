class Simulation {
    constructor() {
        this.LIFE_CELLS_DENSITY = Number(document.getElementById('lifeCellsDensity').value);
        this.PREDATORS_DENSITY = Number(document.getElementById('predatorsDensity').value);
        this.PREDATOR_LIFETIME_PERIOD = Number(document.getElementById('predatorLifetimePeriod').value);
        this.REPRODUCTION_PERIOD = Number(document.getElementById('reproductionPeriod').value);
        this.ITERATIONS = Number(document.getElementById('iterations').value);
        this.UPDATE_INTERVAL_MILLISECONDS = Number(document.getElementById('updateInterval').value);
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

    updateState() {
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

    generateKey(x, y) {
        return x + ',' + y;
    }
}

let FIELD_SIZE;
let simulation;
let gameGrid;
let gameRunner;

$(".start").click(function () {
    FIELD_SIZE = Number(document.getElementById('fieldSize').value);
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
    if (simulation.updateState()) {
        return;
    }
    gameRunner = setTimeout(runSimulation, simulation.UPDATE_INTERVAL_MILLISECONDS, gameGrid.ctx);
}

