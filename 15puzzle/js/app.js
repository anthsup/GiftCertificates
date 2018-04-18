const solvability = {
    emptyTileRow: null,

    checkSolvability: function () {
        let inversions = 0;
        const listToCheck = solvability.convertListOrder(); // flattened order with values 1-15 (without empty tile)

        for (let i = 0; i < listToCheck.length; i++) {
            for (let j = i + 1; j < listToCheck.length; j++) {
                if ((listToCheck[i].flattenedValue > listToCheck[j].flattenedValue)) {
                    inversions++;
                }
            }
        }

        return solvability.checkInversion(inversions);
    },

    checkInversion: function (inversions) {
        if (app.rowSize % 2 === 1) { // odd numbered row (3x3, 5x5..)
            return (inversions % 2 === 0);
        } else { // even numbered row (4x4...)
            console.log("odd inversion + odd distance/ even inversion - even distance : solvability :" +
                ((inversions + app.rowSize - solvability.emptyTileRow) % 2 === 0));
            console.log("inversion: " + inversions + " empty tile : " + solvability.emptyTileRow +
                ", row distance between empty and bottom: " + (app.rowSize - solvability.emptyTileRow));

            return ((inversions + app.rowSize - solvability.emptyTileRow) % 2 === 0);
        }
    },

    convertListOrder: function () {
        const flattenedOrder = [];
        // index written out in a flattened order (0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15)
        for (let j = 0; j < app.rowSize; j++) {
            for (let k = 0; k < app.rowSize; k++) {
                flattenedOrder.push(app.tileArray[(k * app.rowSize) + j]);
            }
        }

        $.each(flattenedOrder, function (i) {
            if (this.tileId === (app.rowSize - 1) * 11) {
                solvability.emptyTileRow = Math.floor(i / 4) + 1;
            }
        });

        return $.grep(flattenedOrder, function (n) {
            return n.flattenedValue !== (app.rowSize * app.rowSize);
        });
    }
};

const app = {
    rowSize: null,
    tileArray: [], // tiles array with key(1-16 flattened value) and value (tileId)
    tileIndex: [],
    totalMoves: 0,
    time: 0,
    timer: null,

    init: function () {
        app.getRowSize();
        app.setListenerToTile();
        app.updateView();
    },

    getRowSize: function () {
        app.rowSize = $('.container > .row').length;
    },

    setListenerToTile: function () {
        $('.tile').on('click', function () {
            app.swap($("#" + this.id));
        });
    },

    swap: function (clickedTile) {
        if (!clickedTile.hasClass('.empty')) {
            const emptyTileIndex = $(".empty").attr('id');
            const id = clickedTile.attr('id').split('');
            const x = parseInt(id[0]);
            const y = parseInt(id[1]);

            const emptyTile = $("#" + emptyTileIndex);

            const dx = [-1, 0, 1, 0];
            const dy = [0, 1, 0, -1];

            for (let i = 0; i < app.rowSize; i++) {
                const xToCheck = (x + dx[i]).toString();
                const yToCheck = (y + dy[i]).toString();

                if (emptyTileIndex.toString() === xToCheck + yToCheck) {
                    emptyTile.text(clickedTile.text()).removeClass('empty');
                    clickedTile.text('').addClass('empty');

                    $('.moves').text(++app.totalMoves);
                    app.checkWin();
                }
            }
        }
    },

    updateView: function () {
        if (app.tileIndex.length !== 0 || app.tileArray.length !== 0) {
            app.tileIndex.length = 0;
            app.tileArray.length = 0;
        }

        for (let i = 0; i < app.rowSize; i++) {
            for (let j = 0; j < app.rowSize; j++) {
                const flattenedValue = (i + 1) + (j * app.rowSize);

                app.tileArray.push({
                    "flattenedValue": flattenedValue,
                    "tileId": i.toString() + j.toString()
                });

                app.tileIndex.push(i.toString() + j.toString());
            }
        }

        // shuffle until it is solvable.
        do {
            app.shuffleList(app.tileArray);
        } while (!solvability.checkSolvability());

        app.drawTiles();
    },

    shuffleList: function (array) {
        //Fisher-Yates Shuffle
        let m = array.length,
            t, i;

        while (m) {
            i = Math.floor(Math.random() * m--);

            t = array[m];
            array[m] = array[i];
            array[i] = t;
        }
    },

    drawTiles: function() {
        $.each(app.tileIndex, function (i) {
            const cell = $("#" + this).text(app.tileArray[i].flattenedValue);

            if (app.tileArray[i].flattenedValue === (app.rowSize * app.rowSize)) {
                cell.addClass('empty').text('');
            } else if (cell.hasClass('empty') && cell.text() != (app.rowSize * app.rowSize)) {
                cell.removeClass('empty');
            }

            $('.moves').text(app.totalMoves = 0);

            if (app.timer != null) {
                app.time = 0;
                clearInterval(app.timer);
            }
            app.startTimer();
        });
    },

    startTimer: function() {
        app.timer = setInterval(function () {
            $('.timer').text(++app.time);
        }, 1000);
    },

    checkWin: function () {
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
            clearInterval(app.timer);
            alert('You\'ve won in ' + app.totalMoves + ' moves!');
        }
    },
};

$(app.init);