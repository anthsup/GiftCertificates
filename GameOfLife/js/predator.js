class Predator extends GameEntity {
    constructor(x, y) {
        super(x, y);
    }

    move(simulation) {
        let newPositionX = this.generateNeighbourValue(this.x);
        let newPositionY = this.generateNeighbourValue(this.y);
        let newEntity = simulation.gameArray[newPositionX][newPositionY];

        if (newEntity === simulation.ENTITIES_REPRESENTATION.victim) {
            simulation.predatorEatsVictim(newPositionX, newPositionY, this.x, this.y);
        } else {
            ++simulation.gameArray[this.x][this.y];
            if (!this.isDead(simulation) && newEntity === simulation.ENTITIES_REPRESENTATION.lifeCell) {
                // predator didn't eat victim, didn't die, so he reproduces or moves on
                this.moveOnOrReproduce(simulation, newPositionX, newPositionY, simulation.ENTITIES_REPRESENTATION.predator);
            }
        }
    }

    isDead(simulation) {
        if (simulation.gameArray[this.x][this.y] >= simulation.PREDATOR_LIFETIME_PERIOD) {
            simulation.predatorDies(this.x, this.y);
            return true;
        }
        return false;
    }
}