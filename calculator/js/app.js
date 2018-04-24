class Calculator {
    constructor() {
        this.operators = ['-', '+', 'x', '÷'];
        this.decimalAdded = false;
        this.calculatorKeyCodes = {
            48: "0", 49: "1", 50: "2", 51: "3", 52: "4", 53: "5", 54: "6",
            55: "7", 56: "8", 57: "9", 42: "x", 43: "+", 45: "-", 46: ".",
            47: "÷", 13: "=", 61: "=", 8: "CE", 99: "C", 67: "C"
        };
    }

    evaluate(inputValue) {
        let evaluation = inputValue;
        const lastChar = evaluation[evaluation.length - 1];
        evaluation = evaluation.replace(/x/g, '*').replace(/÷/g, '/');

        if (this.operators.indexOf(lastChar) > -1 || lastChar === '.') {
            evaluation.replace(/.$/, '');
        }

        if (evaluation) {
            this.decimalAdded = false;
            return eval(evaluation);
        }
    }

    dot(inputScreen, btnVal) {
        if (!this.decimalAdded) {
            inputScreen.text(inputScreen.text() + btnVal);
            this.decimalAdded = true;
        }

    }

    clear() {
        this.decimalAdded = false;
        return '';
    }

    cancelLastEntry(inputValue) {
        if (inputValue.slice(-1) === '.') {
            this.decimalAdded = false;
        }

        const result = inputValue.slice(0, inputValue.length - 1);
        if (result.slice(-1) === '.') {
            this.decimalAdded = true;
        }
        return result;
    }

    appendOperator(inputScreen, inputValue, btnVal) {
        const lastChar = inputValue[inputValue.length - 1];
        if (inputValue !== '' && this.operators.indexOf(lastChar) === -1) {
            inputScreen.text(inputScreen.text() + btnVal);
        } else if (inputValue === '' && btnVal === '-') {
            inputScreen.text(inputScreen.text() + btnVal);
        }

        if (this.operators.indexOf(lastChar) > -1 && inputValue.length > 1) {
            inputScreen.text(inputValue.replace(/.$/, btnVal));
        }
        this.decimalAdded = false;
    }
}

$(function () {
    let calculator = new Calculator();

    $('section a').click(function(e) {
        let input = $(this).text();
        parseInput(e, calculator, input);
    });

    $(document).keypress(function(event){
        console.log(event.which);
        parseKeyboardKeyCode(calculator, event, event.which);
    });
});

function parseInput(e, calc, btnVal) {
    const inputScreen = $('.screen');
    let inputValue = inputScreen.text();

    if (btnVal === 'C') {
        inputScreen.text(calc.clear());
    } else if (btnVal === 'CE') {
        inputScreen.text(calc.cancelLastEntry(inputValue));
    } else if (btnVal === '=') {
        inputScreen.text(calc.evaluate(inputValue));
    } else if (calc.operators.includes(btnVal)) {
        calc.appendOperator(inputScreen, inputValue, btnVal);
    } else if (btnVal === '.') {
        calc.dot(inputScreen, btnVal);
    } else {
        inputValue += btnVal;
        inputScreen.text(inputValue);
    }
    e.preventDefault();
}

function parseKeyboardKeyCode(calculator, event, key) {
    if (calculator.calculatorKeyCodes[key]) {
        parseInput(event, calculator, calculator.calculatorKeyCodes[key]);
    }
}