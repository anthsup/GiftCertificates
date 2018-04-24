describe("clear", function() {

    it("returns empty value", function() {
        assert.equal(new Calculator().clear(), '');
    });
});

describe("evaluate simple expression (2 + 3)", function() {

    it("evaluates simple expression and returns result", function() {
        let calc = new Calculator();
        let input = "2+3";
        let expected = '5';
        assert.equal(calc.evaluate(input), expected);
    });
});

describe("evaluate complex expression (2 + 3 * 2 - 5)", function() {

    it("evaluates complex expression and return result", function() {
        let calc = new Calculator();
        let input = "2+3*2-5";
        let expected = '3';
        assert.equal(calc.evaluate(input), expected);
    });
});

describe("evaluate simple expression with decimals (2.01 + 2.05)", function() {

    it("evaluates complex expression and return result", function() {
        let calc = new Calculator();
        let input = "2.01+2.05";
        let expected = '4.06';
        assert.equal(calc.evaluate(input), expected);
    });
});

describe("evaluate complex expression with decimals (2 + 2.05 * 2)", function() {

    it("evaluates complex expression and return result", function() {
        let calc = new Calculator();
        let input = "2+2.05*2";
        let expected = '6.1';
        assert.equal(calc.evaluate(input), expected);
    });
});

describe("evaluate expression that has dots at the end of values (2.+3.)", function() {

    it("evaluates complex expression and return result", function() {
        let calc = new Calculator();
        let input = "2.+3.";
        let expected = '5';
        assert.equal(calc.evaluate(input), expected);
    });
});