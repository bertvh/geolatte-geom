/*
 * This file is part of the GeoLatte project.
 *
 *     GeoLatte is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     GeoLatte is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public License
 *     along with GeoLatte.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Copyright (C) 2010 - 2011 and Ownership of code is shared by:
 * Qmino bvba - Romeinsestraat 18 - 3001 Heverlee  (http://www.qmino.com)
 * Geovise bvba - Generaal Eisenhowerlei 9 - 2140 Antwerpen (http://www.geovise.com)
 */

package org.geolatte.geom.crs;

import java.util.Arrays;

/**
 * A coordinate system.
 *
 * <p>A coordinate system is characterized by its {@link CoordinateSystemAxis CoordinateSystemAxes} (in order).</p>
 *
 * @author Karel Maesen, Geovise BVBA
 *         creation-date: 4/29/11
 */
public class CoordinateSystem {

    private final CoordinateSystemAxis[] axes;

    /**
     * Constructs an {@code CoordinateSystem}.
     *
     * <p>{@code CoordinateSystem}s are characterized by their {@link CoordinateSystemAxis CoordinateSystemAxes}. </p>
     *
     * @param axes the sequence (at least two) of its {@code CoordinateSystem}s.
     * @throws IllegalArgumentException when less than two axes are specified.
     */
    public CoordinateSystem(CoordinateSystemAxis... axes) {
        if (axes == null || axes.length < 2) {
            throw new IllegalArgumentException("Requires at least 2 axes");
        }
        this.axes = axes;
    }

    /**
     * Returns the {@link CoordinateSystemAxis CoordinateSystemAxes} of this {@code CoordinateSystem} (in order).
     *
     * @return an ordered array of {@code CoordinateSystemAxis CoordinateSystemAxes}.
     */
    public CoordinateSystemAxis[] getAxes() {
        return Arrays.copyOf(axes, axes.length);
    }

    /**
     * Gets the coordinate dimension, i.e. the number of axes in this coordinate system.
     *
     * @return the coordinate dimension.
     */
    public int getCoordinateDimension() {
        return this.axes.length;
    }

    /**
     * Returns the position of the specified {@link CoordinateSystemAxis} in this {@code CoordinateSystem}.
     *
     * @param axis One of the axes of this {@code CoordinateSystem}.
     * @return the position of the given axis if present, -1 if the given axis is not part of this
     * {@code CoordinateSystem}
     */
    public int getAxisIndex(CoordinateSystemAxis axis) {
        int i = 0;
        for (CoordinateSystemAxis a : axes) {
            if (a == axis) return i;
            i++;
        }
        return -1;
    }

    /**
     * Returns the {@link CoordinateSystemAxis} at the specified position.
     *
     * @param index the position of the requested axis.
     * @return the axis at the given position.
     */
    public CoordinateSystemAxis getAxis(int index) {
        return this.axes[index];
    }

    /**
     * Returns the {@code Unit} of the axis at the specified index.
     *
     * @param index the position of the requested axis.
     * @return the unit of the axis at the given position.
     */
    public Unit getAxisUnit(int index){
        return this.axes[index].getUnit();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoordinateSystem that = (CoordinateSystem) o;

        if (!Arrays.equals(axes, that.axes)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(axes);
    }
}
