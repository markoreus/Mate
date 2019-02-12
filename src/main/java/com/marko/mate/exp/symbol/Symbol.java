/*
 * Copyright (C) 2019 Marcos
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
package com.marko.mate.exp.symbol;

import com.marko.mate.exp.Expression;
import com.marko.mate.exp.vectorial.Space;
import java.util.Map;
import java.util.Objects;

/**
 *
 * @author Marcos
 */
public abstract class Symbol implements Expression {

    protected final String id;

    protected Symbol(String symbol) {
        this.id = symbol;
    }

    @Override
    public abstract Expression derivate(Variable var);

    @Override
    public Space evaluate(Map<Symbol, Space> point) {
            return point.get(this);
    }

    @Override
    public boolean isZero() {
        return false;
    }

    @Override
    public Expression simplify() {
        return this;
    }

    @Override
    public String toString() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Symbol) {
            return id.equals(((Symbol) obj).id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

}
