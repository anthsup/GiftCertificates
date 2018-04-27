class GameGrid {
    constructor() {
        this.environmentField = $('#environment')[0];
        this.ctx = this.environmentField.getContext('2d');
        // number of times field size is to be multiplied to make it more readable
        this.MIN_CANVAS_SIZE = 250;
        this.MAX_CANVAS_SIZE = 1000;
        this.CANVAS_SIZE_MODIFIER = 6;
        this.canvasSize = FIELD_SIZE * this.CANVAS_SIZE_MODIFIER;
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

    validateCanvasSize() {
        if (this.canvasSize < this.MIN_CANVAS_SIZE) {
            this.canvasSize = this.MIN_CANVAS_SIZE;
        } else if (this.canvasSize > this.MAX_CANVAS_SIZE) {
            this.canvasSize = this.MAX_CANVAS_SIZE;
        }
    }

    init() {
        this.validateCanvasSize();
        this.environmentField.width = this.environmentField.height = this.canvasSize;
        this.environmentField.style.visibility = "visible";
        this.ctx.fillRect(0, 0, this.environmentField.width, this.environmentField.height);
    }
}