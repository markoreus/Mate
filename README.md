# Mate

### Data Structure for mathematical operations in Java

This will bring you a simple and analithical way for doing complex mathematical operations and seing its string representation.

## Expression interface

All classes implements this interface.

## Operation

This represents all the mathematical operations, such as Sum, Multiplication, etc.

This is how you construct an Operation, for expample:
 Sum s = new Sum(
      new Variable("X"),
      Number.parseNumber("1/2")
 );
 
 s.toString: "X + 1/2"
 
## Simplify method
All expressions have this method.
This is for have a more readable toString expression, for example:
Number.parseNumber(125/25).simplify
this will return a new RNumber(5)

this method is excellent but still there is room for improve.

## Evaluate method
All expressions have this method

This is passed a map<Symbol,Space> as a parameter and substitutes each symbol for the respective class that inherits from Space and evaluates the resulting expression

if all the symbols of the expression are on the map then this method returns a Space, if not, it returns the resulting expression

## Space
This is only for agruping all numerical classes, such as
Number, Vector, Matrix.

## Symbol
This represents Variables and Constants. The only difference between this two is the derivate.

## Pow derivate
If the pow operation has in the exponent the variable by which it is being derived, then the method derivate throws an UnsupportedOperationException. This is due to that the implementation of this algorithm is very complex and i havent been able to implement it.
