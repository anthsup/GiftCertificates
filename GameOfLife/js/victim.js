class Victim extends GameEntity {
    constructor(x, y) {
        super(x, y);
    }

    move(simulation) {
        let newPositionX = this.generateNeighbourValue(this.x);
        let newPositionY = this.generateNeighbourValue(this.y);

        let newEntity = simulation.gameArray[newPositionX][newPositionY];

        if (newEntity >= simulation.ENTITIES_REPRESENTATION.predator) {
            simulation.predatorEatsVictim(this.x, this.y, newPositionX, newPositionY);
        } else if (newEntity === simulation.ENTITIES_REPRESENTATION.lifeCell) { // victim moves or reproduces
            this.moveOnOrReproduce(simulation, newPositionX, newPositionY, simulation.ENTITIES_REPRESENTATION.victim);
        }
    }
}