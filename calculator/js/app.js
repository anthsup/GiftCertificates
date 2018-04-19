class Calculator {
    constructor() {
        this.operators = ['-', '+', 'x', '÷'];
        this.decimalAdded = false;
        this.calculatorKeyCodes = {
            48: "0", 49: "1", 50: "2", 51: "3", 52: "4", 53: "5", 54: "6",
            55: "7", 56: "8", 57: "9", 42: "x", 43: "+", 45: "-", 46: ".",
            47: "÷", 13: "=", 61: "=", 8: "C"
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
            return eval(evaluation);
        }
    }

    dot(inputScreen, btnVal) {
        if (!this.decimalAdded) {
            inputScreen.text(inputScreen.text() + btnVal);
            this.decimalAdded = true;
        }

    }

    clear(calc) {
        calc.isEvaluated = false;
        this.decimalAdded = false;
        return '';
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

function init(calc) {
    $('section a').each(function() {
        $(this).on("click", function(e) {
            let input = $(this).text();
            parseInput(e, calc, input);
        });
    })
}

function parseInput(e, calc, btnVal) {
    const inputScreen = $('.screen');
    let inputValue = inputScreen.text();

    if (calc.isEvaluated) {
        inputValue = calc.clear(calc);
    }
    if (btnVal === 'C') {
        inputScreen.text(calc.clear(calc));
    } else if (btnVal === '=') {
        inputScreen.text(calc.evaluate(inputValue));
        calc.isEvaluated = true;
    } else if (calc.operators.indexOf(btnVal) > -1) {
        calc.appendOperator(inputScreen, inputValue, btnVal);
    } else if (btnVal === '.') {
        calc.dot(inputScreen, btnVal);
    } else {
        inputValue += btnVal;
        inputScreen.text(inputValue);
    }
    e.preventDefault();
}

let calculator = new Calculator();

function parseKeyboardKeyCode(event, key) {
    if (calculator.calculatorKeyCodes[key] !== undefined) {
        parseInput(event, calculator, calculator.calculatorKeyCodes[key]);
    }
}

window.onload = function () {
    init(calculator);
};

$(document).keypress(function(event){
    console.log(event.which);
    parseKeyboardKeyCode(event, event.which);
});