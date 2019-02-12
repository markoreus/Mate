/*
 * Copyright (C) 2018 Marcos <your.name at your.org>
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
package com.marko.mate.exp.vectorial;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.operation.binop.Division;
import com.marko.mate.exp.operation.binop.Multiplication;
import com.marko.mate.exp.operation.binop.Sum;
import com.marko.mate.exp.symbol.Symbol;
import com.marko.mate.exp.symbol.Variable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Marcos
 */
public abstract class Number extends Space implements Comparable<Number> {

    protected final BigDecimal value;

    protected final boolean isInteger;

    protected final MathContext mathContext;

    private final static RoundingMode DEFAULT_ROUNDING_MODE = RoundingMode.HALF_UP;

    private final static MathContext DEFALUT_MATH_CONTEXT = new MathContext(7, DEFAULT_ROUNDING_MODE);

    protected final boolean isPair;

    protected Number(double value, MathContext mathContext) {
        this.mathContext = mathContext;
        this.value = new BigDecimal(value + "", mathContext);
        isInteger = (this.value.doubleValue() + "").endsWith(".0");
        if (isInteger) {
            isPair = (this.value.doubleValue() % 2) == 0;
        } else {
            isPair = false;
        }

    }

    public Number(double value) {
        this(value, DEFALUT_MATH_CONTEXT);
    }

    protected abstract Number add(Number exp);

    protected abstract Number divide(Number exp);

    protected abstract Number multiply(Number exp);

    protected abstract Number substract(Number exp);

    public abstract Number pow(Number exp);

    public abstract Number abs();

    @Override
    public abstract RNumber evaluate(Map<Symbol, Space> point);

    @Override
    public abstract Number simplify();

    private BigDecimal pow(int exp) {
        return value.pow(exp, mathContext);
    }

    protected BigDecimal pow(double exp) {
        if ((exp + "").endsWith(".0")) {
            return pow((int) exp);
        }
        return new BigDecimal(Math.pow(value.doubleValue(), exp), mathContext);
    }

    @Override
    public abstract Number inverse();

    @Override
    public Number negate() {
        return (Number) multiply(RNumber.parseRNumber("-1"));
    }

    @Override
    public RNumber derivate(Variable var) {
        return RNumber.ZERO;
    }

    @Override
    public boolean isZero() {
        return value.compareTo(BigDecimal.ZERO) == 0;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Number) {
            return value.equals(((Number) obj).value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + Objects.hashCode(this.value);
        return hash;
    }

    public boolean isInteger() {
        return isInteger;
    }

    public boolean isPair() {
        return isPair;
    }

    public BigDecimal value() {
        return value;
    }

    @Override
    public int compareTo(Number number) {
        return value.compareTo(number.value);
    }

    public static Number parseNumber(String number) {
        if (number.contains("/")) {
            return QNumber.parseQNumber(number);
        }
        return RNumber.parseRNumber(number);
    }

    @Override
    public Expression add(Expression exp) {
        if (exp instanceof Space) {
            return add((Space) exp);
        }

        return new Sum(
                this,
                exp
        ).simplify();
    }

    private Space add(Space space) {

        if (space instanceof Number) {
            return add((Number) space);
        }

        if (space instanceof Vector) {
            return add((Vector) space);
        }

        return add((Matrix) space);
    }

    @Override
    public Expression multiply(Expression exp) {

        if (exp instanceof Space) {
            return multiply((Space) exp);
        }

        return new Multiplication(
                this, exp
        ).simplify();
    }

    private Space multiply(Space space) {
        if (space instanceof Number) {
            return multiply((Number) space);
        }

        if (space instanceof Vector) {
            return multiply((Vector) space);
        }

        return multiply((Matrix) space);
    }

    @Override
    public Expression substract(Expression exp) {

        if (exp instanceof Space) {
            return substract((Space) exp);
        }

        return new Sum(
                this,
                new Multiplication(
                        RNumber.parseRNumber("-1"),
                        exp
                )
        ).simplify();

    }

    private Space substract(Space space) {
        if (space instanceof Number) {
            return substract((Number) space);
        }

        if (space instanceof Vector) {
            return substract((Vector) space);
        }

        return substract((Matrix) space);
    }

    @Override
    public Expression divide(Expression exp) {

        if (exp instanceof Space) {
            return divide((Space) exp);
        }

        return new Division(
                this, exp
        ).simplify();

    }

    private Space divide(Space space) {
        if (space instanceof Number) {
            return divide((Number) space);
        }

        if (space instanceof Vector) {
            return divide((Vector) space);
        }

        return divide((Matrix) space);
    }

    private Matrix divide(Matrix matrix) {
        Expression[][] newData = new Expression[matrix.data.length][matrix.data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : matrix.data) {
            for (var exp : expArr) {
                newData[i][j++] = new Division(
                        this,
                        exp
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Vector divide(Vector vector) {
        Expression[] newData = new Expression[vector.data[0].length];
        int i = 0;

        for (var exp : vector.data[0]) {
            newData[i++] = new Division(
                    this,
                    exp
            ).simplify();
        }

        return new Vector(newData);
    }

    private Matrix multiply(Matrix matrix) {

        Expression[][] newData = new Expression[matrix.data.length][matrix.data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : matrix.data) {
            for (var exp : expArr) {
                newData[i][j++] = new Multiplication(
                        this,
                        exp
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);

    }

    private Vector multiply(Vector vector) {

        Expression[] newData = new Expression[vector.data[0].length];
        int i = 0;

        for (var exp : vector.data[0]) {
            newData[i++] = new Multiplication(
                    this,
                    exp
            ).simplify();
        }

        return new Vector(newData);

    }

    private Matrix add(Matrix matrix) {

        Expression[][] newData = new Expression[matrix.data.length][matrix.data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : matrix.data) {
            for (var exp : expArr) {
                newData[i][j++] = new Sum(
                        this,
                        exp
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Vector add(Vector vector) {

        Expression[] newData = new Expression[vector.data[0].length];
        int i = 0;

        for (var exp : vector.data[0]) {
            newData[i++] = new Sum(
                    this,
                    exp
            ).simplify();
        }

        return new Vector(newData);
    }

    private Matrix substract(Matrix matrix) {
        Expression[][] newData = new Expression[matrix.data.length][matrix.data[0].length];
        int i = 0, j = 0;

        for (Expression[] expArr : matrix.data) {
            for (var exp : expArr) {
                newData[i][j++] = new Sum(
                        this,
                        new Multiplication(
                                RNumber.parseRNumber("-1"),
                                exp
                        )
                ).simplify();
            }
            i++;
            j = 0;
        }

        return new Matrix(newData);
    }

    private Vector substract(Vector vector) {

        Expression[] newData = new Expression[vector.data[0].length];
        int i = 0;

        for (var exp : vector.data[0]) {
            newData[i++] = new Sum(
                    this,
                    new Multiplication(
                            RNumber.parseRNumber("-1"),
                            exp
                    )
            ).simplify();
        }

        return new Vector(newData);

    }

}
