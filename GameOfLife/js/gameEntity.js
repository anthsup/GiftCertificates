class GameEntity {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    // generates value between MIN and MAX range and returns its absolute value in case it's negative
    generateNeighbourValue(currentValue) {
        const MIN = currentValue - 1;
        const MAX = currentValue + 1;

        let newCoordinate = Math.abs(Math.floor(Math.random() * (MAX - MIN + 1)) + MIN);

        // if it's out of game array — just decrement it
        if (newCoordinate >= FIELD_SIZE) {
            newCoordinate--;
        }

        return newCoordinate;
    }

    moveOnOrReproduce(simulation, newPositionX, newPositionY, entityRepresentation) {
        if (simulation.iterationCounter % simulation.REPRODUCTION_PERIOD === 0 && simulation.iterationCounter) {
            this.reproduce(simulation, newPositionX, newPositionY, entityRepresentation);
        } else {
            this.moveOn(simulation, newPositionX, newPositionY, entityRepresentation);
        }
    }

    moveOn(simulation, newPositionX, newPositionY, entityRepresentation) {
        simulation.gameArray[newPositionX][newPositionY] = simulation.gameArray[this.x][this.y];
        simulation.entityMovesOn(this.x, this.y, newPositionX, newPositionY, entityRepresentation);
        this.x = newPositionX;
        this.y = newPositionY;
    }

    reproduce(simulation, newPositionX, newPositionY, entityRepresentation) {
        simulation.gameArray[newPositionX][newPositionY] = entityRepresentation;
        simulation.entityReproduces(newPositionX, newPositionY, entityRepresentation);
    }
}