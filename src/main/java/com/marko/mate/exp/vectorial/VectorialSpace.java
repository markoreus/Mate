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
package com.marko.mate.exp.vectorial;

import com.marko.mate.exp.Expression;
import java.util.Arrays;

/**
 *
 * @author Marcos
 */
public abstract class VectorialSpace extends Space{

    protected final Expression[][] data;

    protected VectorialSpace(Expression[][] data) {
        this.data = data;
    }

    protected VectorialSpace(Expression[] data) {
        this.data = new Expression[1][data.length];
        int i = 0;
        
        for (var exp : data) {
            this.data[0][i++] = exp;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VectorialSpace) {
            VectorialSpace vs = (VectorialSpace) obj;
            for (int i = 0; i < data.length; i++) {
                for (int j = 0; j < data[0].length; j++) {
                    if (!data[i][j].equals(vs.data[i][j])) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }    

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Arrays.deepHashCode(this.data);
        return hash;
    }
    
    @Override
    public abstract Space inverse();
    
    public abstract Matrix transpose();

}
