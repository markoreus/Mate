/*
 * Copyright (C) 2018 Marcos
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.marko.mate.exp;

import com.marko.mate.exp.operation.binop.Division;
import com.marko.mate.exp.operation.binop.Multiplication;
import com.marko.mate.exp.operation.binop.Pow;
import com.marko.mate.exp.operation.binop.Sum;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import com.marko.mate.exp.vectorial.Matrix;
import com.marko.mate.exp.vectorial.RNumber;
import com.marko.mate.exp.vectorial.Space;
import com.marko.mate.exp.vectorial.Vector;
import java.util.Map;

/**
 * <p>
 * {@code Expression} is the interface used to represents mathematics
 * operations, variables or numbers.
 *
 * @author Marcos
 */
public interface Expression {

    /**
     * Finds the derivative of an expression in an analitical way.
     *
     * @param var
     * @return the derivative of a mathematical expression with respect to the
     * variable var
     */
    public Expression derivate(Variable var);

    /**
     *
     * @return true if zero, false oterwise
     */
    public boolean isZero();

    /**
     * It simplifies the expression to be more readable.
     *
     * @return {@code Expression}
     */
    public Expression simplify();

    /**
     * It evaluates the expression in a point.
     * <p>
     * If the param point contains all the symbols in the expression this method
     * will return the numerical value of this expression evaluated or an
     * {@link Space} depending of the second argument of the map point, if not
     * it will return an expression evaluated only on the symbols that contains
     * the map.
     *
     * @param point the point to be evaluated
     * @return {@link Expression}
     */
    public Expression evaluate(Map<Symbol, Space> point);

    @Override
    public boolean equals(Object obj);

    /**
     * <p>
     * The String representation of a mathematical operation, for example:
     * <pre>{@code
     * new Sum(
     *  new Variable("x"),
     *  new RNumber(2)
     * ).toString();
     * }</pre> it will return
     * <pre>
     * {@code x + 2}
     * </pre>
     *
     * @return the String representation of the {@link Expression}
     */
    @Override
    public String toString();

    /**
     * <p>
     * All expressions can be added to another expression. This represents the
     * arithmetic operation of addition. In case of adding a new class
     * implementing {@link Expression} this method will return a {@link Sum}
     * simplified by default.
     *
     * @param exp the expression to be added
     * @return {@link Expression}
     */
    public default Expression add(Expression exp) {
        return new Sum(
                this,
                exp
        ).simplify();
    }

    /**
     * <p>
     * All expressions can be substracted to another expression. This represents
     * the arithmetic operation of substraction. In case of adding a new class
     * implementing {@link Expression} this method will return a {@link Sum}
     * simplified by default
     *
     * @param exp the expression to be substracted
     * @return {@link Expression}
     */
    public default Expression substract(Expression exp) {
        return new Sum(
                this,
                new Multiplication(
                        RNumber.parseNumber("-1"),
                        exp
                )
        ).simplify();
    }

    /**
     * <p>
     * All expressions can be multiplied to another expression. This represents
     * the arithmetic operation of multiplication. In case of adding a new class
     * implementing {@link Expression} this method will return a
     * {@link Multiplication} simplified by default
     *
     * @param exp the expression to be multiplied
     * @return {@link Expression}
     */
    public default Expression multiply(Expression exp) {
        return new Multiplication(
                this, exp
        ).simplify();
    }

    /**
     * <p>
     * All expressions can be divided to another expression. This represents the
     * arithmetic operation of Division. In case of adding a new class
     * implementing {@link Expression} this method will return a
     * {@link Division} simplified by default.
     *
     * if the expression to be divided is zero, this metho will throw an
     * ArithmeticException.
     *
     * @param exp the expression to be divided
     * @return {@link Expression}
     */
    public default Expression divide(Expression exp) throws ArithmeticException {
        if (exp.isZero()) {
            throw new ArithmeticException("Division by zero");
        }

        return new Division(
                this,
                exp
        ).simplify();
    }

    /**
     * <p>
     * This method will elevate this method will elevate this expression to the
     * expression passed by params. All expression can be elevated to each other
     * with the exceptions of {@link Matrix} and {@link Vector}
     *
     * @param exp
     * @return {@link Expression}
     */
    public default Expression pow(Expression exp) {
        return new Pow(
                this, exp
        ).simplify();
    }

    /**
     * <p>
     * All expressions have the property of being negated. By default this this
     * will only multiply this by minus one.
     *
     * @return {@link Expression}
     */
    public default Expression negate() {
        return new Multiplication(
                RNumber.parseNumber("-1"),
                this
        ).simplify();
    }

    /**
     * <p>
     * All expressions have inverse. By default this this
     * will only divide one by this.
     *
     * @return {@link Expression}
     */
    public default Expression inverse() {
        return new Division(
                RNumber.parseNumber("1"),
                this
        ).simplify();
    }

}
