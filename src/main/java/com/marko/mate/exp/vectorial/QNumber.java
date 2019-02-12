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
package com.marko.mate.exp.vectorial;

import com.marko.mate.exp.symbol.Symbol;
import java.math.BigDecimal;
import java.util.Map;
import com.marko.mate.Util;

/**
 *
 * @author Marcos
 */
public class QNumber extends Number {

    private final Number numerator;

    private final Number denominator;

    public QNumber(Number numerator, Number denominator) {
        super(numerator.divide(denominator).value.doubleValue());
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public QNumber(RNumber numerator) {
        this(numerator, RNumber.ONE);
    }

    @Override
    protected Number divide(Number exp) {
        if (exp instanceof QNumber) {
            return divide((QNumber) exp);
        }
        return divide(new QNumber(((RNumber) exp)));
    }

    private Number divide(QNumber number) {
        return new QNumber(
                numerator.divide(number.numerator),
                denominator.divide(number.denominator)
        ).simplify();
    }

    @Override
    public Number pow(Number exp) {

        return new QNumber(
                new RNumber(numerator.pow(((Number) exp)).value.doubleValue()),
                new RNumber(denominator.pow(((Number) exp)).value.doubleValue())
        ).simplify();

    }

    @Override
    public QNumber abs(){
        return new QNumber(
                numerator.abs(),
                denominator.abs()
        );
    }
    
    @Override
    public Number simplify() {

        if (numerator.isZero()) {
            return RNumber.ZERO;
        }

        if (numerator.value == denominator.value) {
            return RNumber.ONE;
        }

        if (numerator instanceof QNumber) {
            return new QNumber(
                    ((QNumber) numerator).numerator,
                    (Number) ((QNumber) numerator).denominator.multiply(denominator)
            ).simplify();
        }

        if (denominator instanceof QNumber) {
            return new QNumber(
                    (Number) numerator.multiply(((QNumber) denominator)),
                    ((QNumber) denominator).numerator
            ).simplify();
        }

        RNumber n = new RNumber(numerator.divide(denominator).value.doubleValue());

        if (n.isInteger) {
            return n;
        }

        if (numerator.isInteger && denominator.isInteger) {

            long gcd = Util.gcd(
                    Math.abs((long) numerator.value.doubleValue()),
                    Math.abs((long) denominator.value.doubleValue())
            );
            if (gcd == denominator.value.doubleValue()) {
                return new RNumber(numerator.value.divide(new BigDecimal(
                        gcd + "")).doubleValue());
            }

            if (gcd != 1) {
                return new QNumber(
                        new RNumber(numerator.value.divide(new BigDecimal(
                                gcd + "")).doubleValue()),
                        new RNumber(denominator.value.divide(new BigDecimal(
                                gcd + "")).doubleValue())
                );
            }
        }

        return this;
    }

    @Override
    public String toString() {
        if (denominator.value.doubleValue() == 1) {
            return numerator.value.doubleValue() + "";
        }
        if (numerator.value.doubleValue() == 0) {
            return 0.0 + "";
        }
        return "(" + numerator
                + "/" + denominator + ")";

    }

    @Override
    protected Number multiply(Number exp) {

        if (exp instanceof QNumber) {
            return multiply((QNumber) exp);
        }
        return multiply(new QNumber(((RNumber) exp)));
    }

    private Number multiply(QNumber number) {
        return new QNumber(
                ((Number) numerator.multiply(number.numerator)),
                (Number) denominator.multiply(number.denominator)
        ).simplify();
    }

    @Override
    protected Number add(Number exp) {
        if (exp instanceof QNumber) {
            return add((QNumber) exp);
        }
        return add(new QNumber((RNumber) exp));
    }

    private Number add(QNumber number) {
        Number den = (Number) number.denominator.multiply(denominator);
        Number num = (Number) (den.divide(denominator).multiply(numerator))
                .add(den.divide(number.denominator).multiply(number.numerator));

        return new QNumber(num, den).simplify();
    }

    @Override
    protected Number substract(Number exp) {
        if (exp instanceof QNumber) {
            return substract((QNumber) exp);
        }
        return substract(new QNumber(((RNumber) exp)));
    }

    private Number substract(QNumber number) {
        Number den = (Number) number.denominator.multiply(denominator);
        Number num = (Number) (den.divide(denominator).multiply(numerator))
                .substract(den.divide(number.denominator).multiply(number.numerator));

        return new QNumber(num, den).simplify();
    }

    protected static QNumber parseQNumber(String number) {
        if (number.contains("/")) {

            int index;
            String numerator = "",
                    denominator = "";

            for (index = 0; index < number.length(); index++) {
                if (number.charAt(index) == '/') {
                    break;
                }
                numerator += number.charAt(index);
            }

            for (int i = index + 1; i < number.length(); i++) {
                denominator += number.charAt(i);
            }
            return new QNumber(
                    RNumber.parseRNumber(numerator),
                    parseNumber(denominator)
            );

        }
        throw new IllegalStateException("Is not a QNumber");
    }

    @Override
    public RNumber evaluate(Map<Symbol, Space> point) {
        return new RNumber(value.doubleValue());
    }

    @Override
    public Number inverse() {
        return new QNumber(
                denominator,
                numerator
        ).simplify();
    }

}
