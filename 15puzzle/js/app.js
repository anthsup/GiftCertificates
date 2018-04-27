class App {
    constructor() {
        this.rowSize = null;
        this.tileArray = []; // tiles array with key(1-16 flattened value) and value (tileId)
        this.tileIndexes = [];
        this.totalMoves = 0;
        this.time = 0;
        this.timer = null;
    }

    init(solvabilityUtilInstance) {
        this.getRowSize();
        this.setListenerToTile();
        this.updateView(solvabilityUtilInstance);
    }

    getRowSize() {
        this.rowSize = $('.container > .row').length;
    }

    setListenerToTile() {
        const self = this;
        $('.tile').on('click', function () {
            self.swapTiles($("#" + this.id));
        });
    }

    swapTiles(clickedTile) {
        if (!clickedTile.hasClass('empty')) {
            const emptyTileIndex = $(".empty").attr('id');
            const id = clickedTile.attr('id').split('');
            const x = parseInt(id[0]);
            const y = parseInt(id[1]);

            const emptyTile = $("#" + emptyTileIndex);

            const dx = [-1, 0, 1, 0];
            const dy = [0, 1, 0, -1];

            for (let i = 0; i < this.rowSize; i++) {
                const xToCheck = (x + dx[i]).toString();
                const yToCheck = (y + dy[i]).toString();

                if (emptyTileIndex.toString() === xToCheck + yToCheck) {
                    emptyTile.text(clickedTile.text()).removeClass('empty');
                    clickedTile.text('').addClass('empty');

                    $('#moves').text(++this.totalMoves);
                    this.checkWin();
                }
            }
        }
    }

    updateView(solvabilityUtilInstance) {
        if (this.tileIndexes.length !== 0 || this.tileArray.length !== 0) {
            this.tileIndexes.length = 0;
            this.tileArray.length = 0;
        }

        for (let i = 0; i < this.rowSize; i++) {
            for (let j = 0; j < this.rowSize; j++) {
                const flattenedValue = (i + 1) + (j * this.rowSize);

                this.tileArray.push({
                    "flattenedValue": flattenedValue,
                    "tileId": i.toString() + j.toString()
                });

                this.tileIndexes.push(i.toString() + j.toString());
            }
        }

        // shuffle until it is solvable.
        do {
            this.shuffleList(this.tileArray);
        } while (!solvabilityUtilInstance.checkSolvability(this.rowSize, this.tileArray));

        this.drawTiles();
    }

    // Fisher-Yates Shuffle
    // https://bost.ocks.org/mike/shuffle/
    shuffleList(array) {
        let m = array.length,
            t, i;

        while (m) {
            i = Math.floor(Math.random() * m--);

            t = array[m];
            array[m] = array[i];
            array[i] = t;
        }
    }

    drawTiles() {
        const self = this;
        $.each(this.tileIndexes, function (i) {
            const cell = $("#" + this).text(self.tileArray[i].flattenedValue);

            if (self.tileArray[i].flattenedValue === (self.rowSize * self.rowSize)) {
                cell.addClass('empty').text('');
            } else if (cell.hasClass('empty') && cell.text() != (self.rowSize * self.rowSize)) {
                cell.removeClass('empty');
            }

            $('#moves').text(self.totalMoves = 0);

            if (self.timer != null) {
                self.time = 0;
                clearInterval(self.timer);
            }
            $('#timer').text(self.time);
            self.startTimer();
        });
    }

    startTimer() {
        const self = this;
        this.timer = setInterval(function () {
            $('#timer').text(++self.time);
        }, 1000);
    }

    checkWin() {
        let condition = true;
        let num = 0;

        $('.tile').each(function(i, tile) {
            if (i > 14) { return; }

            if (Number(num) > Number($(tile).text()) || $(tile).text() === '') {
                condition = false;
            }

            num = $(tile).text();
        });

        if (condition) {
            clearInterval(this.timer);
            alert('You\'ve won in ' + this.totalMoves + ' moves!');
        }
    }
}

let app;
let solvabilityUtilInstance;

$(function () {
    app = new App();
    solvabilityUtilInstance = new SolvabilityUtil();
    app.init(solvabilityUtilInstance);
});

$('#newGame').click(() => {
    app.updateView(solvabilityUtilInstance);
});

$('#restoreGame').click(() => {
    app.drawTiles();
});