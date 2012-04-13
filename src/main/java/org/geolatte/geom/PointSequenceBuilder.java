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

package org.geolatte.geom;

/**
 * A builder for {@code PointSequence}s.
 *
 * {@code PointSequence}s are built by adding points in sequence.
 *
 * @author Karel Maesen, Geovise BVBA, 2011
 */
public interface PointSequenceBuilder {

    /**
     * Adds the specified coordinates to the {@code PointSequence} being built.
     *
     * @param coordinates the coordinates.
     * @return the {@code PointSequenceBuilder}.
     */
    PointSequenceBuilder add(double[] coordinates);

    /**
     * Adds a new 2D point with given coordinates to the {@code PointSequence} being built.
     *
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @return the {@code PointSequenceBuilder}.
     */
    PointSequenceBuilder add(double x, double y);

    PointSequenceBuilder add(double x, double y, double zOrm);

    /**
     * Adds a new measured 3D point with given coordinates to the {@code PointSequence} being built.
     *
     * @param x the x-coordinate of the point.
     * @param y the y-coordinate of the point.
     * @param z the z-coordinate of the point.
     * @param m the measure value of the point.
     * @return the {@code PointSequenceBuilder}.
     */
    PointSequenceBuilder add(double x, double y, double z, double m);

    /**
     * Gets the {@code PointSequence} created with this builder.
     *
     * @return the {@code PointSequence}.
     */
    PointSequence toPointSequence();
}
