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

package org.geolatte.geom.jts;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import org.geolatte.geom.crs.CartesianCoordinateSystem;
import org.geolatte.geom.*;

/**
 * @author Karel Maesen, Geovise BVBA, 2011
 */
public class JTS {

    private static final GeometryFactory jtsGeometryFactory;

    static {
        jtsGeometryFactory = new GeometryFactory(new PointSequenceFactory());
    }

    public static GeometryFactory geometryFactory() {
        return jtsGeometryFactory;
    }

    public static org.geolatte.geom.Geometry from(com.vividsolutions.jts.geom.Geometry jtsGeometry) {
        return from(jtsGeometry, jtsGeometry.getSRID());
    }

    public static org.geolatte.geom.Geometry from(com.vividsolutions.jts.geom.Geometry jtsGeometry, int SRID) {
        if (jtsGeometry instanceof Point) {
            return from((Point) jtsGeometry, SRID);
        }

        if (jtsGeometry instanceof LineString) {
            return from((LineString) jtsGeometry, SRID);
        }

        if (jtsGeometry instanceof Polygon) {
            return from((Polygon) jtsGeometry, SRID);
        }
        if (jtsGeometry instanceof MultiPoint) {
            return from((MultiPoint) jtsGeometry, SRID);
        }

        if (jtsGeometry instanceof MultiLineString) {
            return from((MultiLineString) jtsGeometry, SRID);
        }

        if (jtsGeometry instanceof GeometryCollection) {
            return from((GeometryCollection) jtsGeometry);
        }

        throw new JTSConversionException();
    }

    private static org.geolatte.geom.Polygon from(Polygon jtsGeometry, int SRID) {
        org.geolatte.geom.LinearRing[] linestrings = new org.geolatte.geom.LinearRing[jtsGeometry.getNumInteriorRing() + 1];
        linestrings[0] = (org.geolatte.geom.LinearRing) from((LineString) jtsGeometry.getExteriorRing(), SRID);
        for (int i = 1; i < linestrings.length; i++) {
            linestrings[i] = (org.geolatte.geom.LinearRing) from((LineString) jtsGeometry.getInteriorRingN(i), SRID);
        }
        return org.geolatte.geom.Polygon.create(linestrings, jtsGeometry.getSRID());
    }

    private static org.geolatte.geom.MultiLineString from(MultiLineString jtsGeometry, int SRID) {
        org.geolatte.geom.LineString[] linestrings = new org.geolatte.geom.LineString[jtsGeometry.getNumGeometries()];
        for (int i = 0; i < linestrings.length; i++) {
            linestrings[i] = from((LineString) jtsGeometry.getGeometryN(i), SRID);
        }
        return org.geolatte.geom.MultiLineString.create(linestrings, jtsGeometry.getSRID());
    }

    private static org.geolatte.geom.GeometryCollection from(GeometryCollection jtsGeometry) {
        throw new UnsupportedOperationException();
    }

    private static org.geolatte.geom.LineString from(LineString jtsLineString, int SRID) {
        CoordinateSequence cs = jtsLineString.getCoordinateSequence();
        return org.geolatte.geom.LineString.create(toPointSequence(cs), SRID);

    }

    private static PointSequence toPointSequence(CoordinateSequence cs) {
        if (cs instanceof PointSequence) return (PointSequence) cs;
        FixedSizePointSequenceBuilder builder = new FixedSizePointSequenceBuilder(cs.size(), CartesianCoordinateSystem.XYZ);
        double[] coord = new double[3];
        for (int i = 0; i < cs.size(); i++) {
            for (int ci = 0; ci < coord.length; ci++) {
                coord[ci] = cs.getOrdinate(i, ci);
            }
            builder.add(coord);
        }
        return builder.toPointSequence();
    }

    private static org.geolatte.geom.MultiPoint from(MultiPoint jtsMultiPoint, int SRID) {
        if (jtsMultiPoint == null || jtsMultiPoint.getNumGeometries() == 0)
            return org.geolatte.geom.MultiPoint.createEmpty();
        org.geolatte.geom.Point[] points = new org.geolatte.geom.Point[jtsMultiPoint.getNumGeometries()];
        for (int i = 0; i < points.length; i++) {
            points[i] = from((Point) jtsMultiPoint.getGeometryN(i), SRID);
        }
        return org.geolatte.geom.MultiPoint.create(points, SRID);
    }

    private static org.geolatte.geom.Point from(com.vividsolutions.jts.geom.Point jtsPoint, int SRID) {
        CoordinateSequence cs = jtsPoint.getCoordinateSequence();
        return org.geolatte.geom.Point.create(toPointSequence(cs), SRID);
    }

    protected static CoordinateSequence sequenceOf(org.geolatte.geom.Geometry geometry) {
        //TODO - when not Geometry instances, create a new PointSequence from the Geometry's Points.
        if (!(geometry != null)) {
            throw new JTSConversionException("Can't convert null geometries.");
        }
        return (CoordinateSequence) geometry.getPoints();
    }

    public static com.vividsolutions.jts.geom.Geometry to(org.geolatte.geom.Geometry geometry) {
        if (geometry instanceof org.geolatte.geom.Point)
            return to((org.geolatte.geom.Point) geometry);

        if (geometry instanceof org.geolatte.geom.LineString)
            return to((org.geolatte.geom.LineString) geometry);

        if (geometry instanceof org.geolatte.geom.MultiPoint)
            return to((org.geolatte.geom.MultiPoint) geometry);

        if (geometry instanceof org.geolatte.geom.Polygon)
            return to((org.geolatte.geom.Polygon) geometry);

        throw new JTSConversionException();

    }

    public static Polygon to(org.geolatte.geom.Polygon polygon) {
        LinearRing shell = to(polygon.getExteriorRing());
        LinearRing[] holes = new LinearRing[polygon.getNumInteriorRing()];
        for (int i = 0; i < holes.length; i++) {
            holes[i] = to(polygon.getInteriorRingN(i));
        }
        Polygon pg = geometryFactory().createPolygon(shell, holes);
        pg.setSRID(polygon.getSRID());
        return pg;
    }

    public static Point to(org.geolatte.geom.Point point) {
        Point pnt = geometryFactory().createPoint(sequenceOf(point));
        pnt.setSRID(point.getSRID());
        return pnt;
    }

    public static LineString to(org.geolatte.geom.LineString lineString) {
        LineString ls = geometryFactory().createLineString(sequenceOf(lineString));
        ls.setSRID(lineString.getSRID());
        return ls;
    }

    public static LinearRing to(org.geolatte.geom.LinearRing linearRing) {
        LinearRing lr = geometryFactory().createLinearRing(sequenceOf(linearRing));
        lr.setSRID(linearRing.getSRID());
        return lr;
    }


    public static MultiPoint to(org.geolatte.geom.MultiPoint multiPoint) {
        MultiPoint mp = geometryFactory().createMultiPoint(sequenceOf(multiPoint));
        mp.setSRID(multiPoint.getSRID());
        return mp;
    }


}

