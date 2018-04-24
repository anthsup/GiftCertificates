// https://www.cs.bham.ac.uk/~mdr/teaching/modules04/java2/TilesSolvability.html
class SolvabilityUtil {
    constructor() {
        this.emptyTileRow = null;
    }

    checkSolvability(rowSize, tileArray) {
        let inversions = 0;
        const listToCheck = this.convertListOrder(rowSize, tileArray); // flattened order with values 1-15 (without empty tile)

        for (let i = 0; i < listToCheck.length; i++) {
            for (let j = i + 1; j < listToCheck.length; j++) {
                if ((listToCheck[i].flattenedValue > listToCheck[j].flattenedValue)) {
                    inversions++;
                }
            }
        }

        return this.checkInversion(inversions, rowSize, tileArray);
    }

    checkInversion(inversions, rowSize, tileArray) {
        if (rowSize % 2 === 1) { // odd numbered row (3x3, 5x5..)
            return (inversions % 2 === 0);
        } else { // even numbered row (4x4...)
            console.log("odd inversion + odd distance/ even inversion - even distance : solvability :" +
                ((inversions + rowSize - this.emptyTileRow) % 2 === 0));
            console.log("inversion: " + inversions + " empty tile : " + this.emptyTileRow +
                ", row distance between empty and bottom: " + (rowSize - this.emptyTileRow));

            return ((inversions + rowSize - this.emptyTileRow) % 2 === 0);
        }
    }

    convertListOrder(rowSize, tileArray) {
        const flattenedOrder = [];
        const self = this;

        // index written out in a flattened order (0, 4, 8, 12, 1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15)
        for (let j = 0; j < rowSize; j++) {
            for (let k = 0; k < rowSize; k++) {
                flattenedOrder.push(tileArray[(k * rowSize) + j]);
            }
        }

        $.each(flattenedOrder, function (i) {
            if (this.tileId === (rowSize - 1) * 11) {
                self.emptyTileRow = Math.floor(i / 4) + 1;
            }
        });

        // check the flattened order using 1-15 index AND return the list to check inversions
        return $.grep(flattenedOrder, function (n) {
            return n.flattenedValue !== (rowSize * rowSize);
        });
    }
}