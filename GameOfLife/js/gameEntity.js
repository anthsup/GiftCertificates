class GameEntity {
    constructor(x, y) {
        this.x = x;
        this.y = y;
    }

    generateNeighbourValue(currentValue) {
        let newCoordinate = Math.abs(currentValue + Math.ceil(Math.random() * 3) - 2);

        if (newCoordinate >= FIELD_SIZE) {
            newCoordinate = 2 * FIELD_SIZE - newCoordinate - 1;
        }

        return newCoordinate;
    }

    predatorEatsVictim(simulation, victimX, victimY, predatorX, predatorY) {
        simulation.gameArray[victimX][victimY] = simulation.ENTITIES_REPRESENTATION.lifeCell;
        simulation.gameArray[predatorX][predatorY] = simulation.ENTITIES_REPRESENTATION.predator; // reset life time
        --simulation.victimsNumber;
        delete simulation.entities[simulation.generateKey(victimX, victimY)];
    }

    moveOnOrReproduce(simulation, newPositionX, newPositionY, entityRepresentation) {
        if (simulation.iterationCounter && simulation.iterationCounter % simulation.REPRODUCTION_PERIOD === 0) {
            this.moveOn(simulation, newPositionX, newPositionY, entityRepresentation);
        } else {
            this.reproduce(simulation, newPositionX, newPositionY);
        }
    }

    moveOn(simulation, newPositionX, newPositionY, entityRepresentation) {
        simulation.gameArray[newPositionX][newPositionY] = entityRepresentation;
        if (entityRepresentation === -1) {
            simulation.victimsNumber++;
            simulation.entities[simulation.generateKey(newPositionX, newPositionY)] = new Victim(newPositionX, newPositionY);
        } else {
            simulation.predatorsNumber++;
            simulation.entities[simulation.generateKey(newPositionX, newPositionY)] = new Predator(newPositionX, newPositionY);
        }
    }

    reproduce(simulation, newPositionX, newPositionY) {
        simulation.gameArray[newPositionX][newPositionY] = simulation.gameArray[this.x][this.y];
        simulation.gameArray[this.x][this.y] = simulation.ENTITIES_REPRESENTATION.lifeCell;
        delete simulation.entities[simulation.generateKey(this.x, this.y)];
        this.x = newPositionX;
        this.y = newPositionY;
        simulation.entities[simulation.generateKey(newPositionX, newPositionY)] = this;
    }
}