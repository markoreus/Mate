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
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

/**
 *
 * @author Marcos
 */
public class RNumber extends Number {

    public final static RNumber ZERO = new RNumber(BigDecimal.ZERO.doubleValue());

    public final static RNumber ONE = new RNumber(BigDecimal.ONE.doubleValue());

    public final static RNumber TEN = new RNumber(BigDecimal.TEN.doubleValue());

    public RNumber(double value) {
        super(value);
    }

    public RNumber(double value, MathContext mathContext) {
        super(value, mathContext);
    }

    public RNumber scale(int scale, RoundingMode mode) {
        return new RNumber(value.setScale(scale, mode).doubleValue());
    }

    @Override
    public Number abs() {
        return new RNumber(value.abs().doubleValue(), mathContext);
    }

    @Override
    protected Number multiply(Number number) {

        if (number instanceof RNumber) {
            return new RNumber(this.value.multiply(number.value).doubleValue());
        }
        return new QNumber(this).multiply(number);
    }

    @Override
    protected Number add(Number number) {

        if (number instanceof RNumber) {
            return new RNumber(value.add(number.value).doubleValue());
        }
        return new QNumber(this).add(number);
    }

    @Override
    protected Number substract(Number number) {

        if (number instanceof RNumber) {
            return new RNumber(value.subtract(number.value).doubleValue());
        }
        return new QNumber(this).substract(number);

    }

    @Override
    protected Number divide(Number exp) {
        if (exp instanceof RNumber) {
            return new RNumber(value.divide(((RNumber) exp).value, mathContext).doubleValue());
        }
        return new QNumber(this).divide((QNumber) exp);
    }

    @Override
    public Number pow(Number exp) {
        return new RNumber(pow(((Number) exp).value.doubleValue()).doubleValue());
    }

    @Override
    public String toString() {
        return value.doubleValue() + "";
    }

    @Override
    public RNumber simplify() {
        return this;
    }

    protected static RNumber parseRNumber(String number) {
        return new RNumber(new BigDecimal(number).doubleValue());
    }

    @Override
    public RNumber evaluate(Map<Symbol, Space> point) {
        return this;
    }

    @Override
    public Number inverse() {
        
        if (this.compareTo(ZERO) == 0) {
            throw new IllegalStateException("Zero doesn't have inverse");
        }
        if (this.compareTo(ONE) == 0) {
            return this;
        }
        return new QNumber(
                RNumber.ONE,
                this
        );
    }

}
