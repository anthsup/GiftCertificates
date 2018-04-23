class GameGrid {
    constructor() {
        this.environmentField = document.getElementById('environment');
        this.ctx = this.environmentField.getContext('2d');
        this.canvasSize = FIELD_SIZE * 6;
        this.predatorImage = new Image();
        this.victimImage = new Image();
        this.predatorImage.src = 'img/red.png';
        this.victimImage.src = 'img/blue.png';
    }

    drawGrid(gameArray, entities) {
        this.ctx.clearRect(0, 0, this.canvasSize, this.canvasSize); // this should clear the canvas ahead of each redraw
        let box = this.canvasSize / FIELD_SIZE;
        for (let i = 0; i < FIELD_SIZE; i++) {
            for (let j = 0; j < FIELD_SIZE; j++) {
                let entity = gameArray[i][j];
                if (entity !== entities.lifeCell) {
                    this.ctx.drawImage((entity >= entities.predator ? this.predatorImage : this.victimImage),
                        box * i, box * j, box, box);
                }
            }
        }
        this.ctx.save();
    }

    init() {
        this.environmentField.width = this.environmentField.height = this.canvasSize;
        this.environmentField.style.visibility = "visible";
        this.ctx.fillRect(0, 0, this.environmentField.width, this.environmentField.height);
    }
}