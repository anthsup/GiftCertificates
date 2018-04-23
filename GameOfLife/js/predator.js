class Predator extends GameEntity {
    constructor(x, y) {
        super(x, y);
    }

    move(simulation) {
        let newPositionX = this.generateNeighbourValue(this.x);
        let newPositionY = this.generateNeighbourValue(this.y);
        let newEntity = simulation.gameArray[newPositionX][newPositionY];

        if (newEntity === simulation.ENTITIES_REPRESENTATION.victim) {
            this.predatorEatsVictim(simulation, newPositionX, newPositionY, this.x, this.y);
        } else {
            ++simulation.gameArray[this.x][this.y];
            if (!this.predatorDies(simulation) && newEntity === simulation.ENTITIES_REPRESENTATION.lifeCell) {
                // predator didn't eat victim, didn't die, so he reproduces or moves on
                this.moveOnOrReproduce(simulation, newPositionX, newPositionY, simulation.ENTITIES_REPRESENTATION.predator);
            }
        }
    }

    predatorDies(simulation) {
        if (simulation.gameArray[this.x][this.y] >= simulation.PREDATOR_LIFETIME_PERIOD) {
            simulation.gameArray[this.x][this.y] = simulation.ENTITIES_REPRESENTATION.lifeCell; // predator died
            --simulation.predatorsNumber;
            delete simulation.entities[simulation.generateKey(this.x, this.y)];
            return true;
        }
        return false;
    }
}