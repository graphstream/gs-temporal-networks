/*
 * Copyright 2006 - 2016
 *     Stefan Balev     <stefan.balev@graphstream-project.org>
 *     Julien Baudry    <julien.baudry@graphstream-project.org>
 *     Antoine Dutot    <antoine.dutot@graphstream-project.org>
 *     Yoann Pigné      <yoann.pigne@graphstream-project.org>
 *     Guilhelm Savin   <guilhelm.savin@graphstream-project.org>
 *
 * This file is part of GraphStream <http://graphstream-project.org>.
 *
 * GraphStream is a library whose purpose is to handle static or dynamic
 * graph, create them from scratch, file or any source and display them.
 *
 * This program is free software distributed under the terms of two licenses, the
 * CeCILL-C license that fits European law, and the GNU Lesser General Public
 * License. You can  use, modify and/ or redistribute the software under the terms
 * of the CeCILL-C license as circulated by CEA, CNRS and INRIA at the following
 * URL <http://www.cecill.info> or under the terms of the GNU LGPL as published by
 * the Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C and LGPL licenses and that you accept their terms.
 */
package org.graphstream.graph.temporalNetwork;

/**
 * Model a time window.
 * <p/>
 * Such objects are identified by two dates : one gives the date of when  the window starts,
 * and a second one gives the date of when the window ends. The start-date is inclusive, whereas
 * the end-date is exclusive.
 */
public class TimeWindow implements Comparable<TimeWindow> {
    public static TimeWindow wrapDate(double date) {
        return new TimeWindow(date, Double.NaN);
    }

    /**
     * Date of the start of the time-window. This date is inclusive.
     */
    protected double startDate;
    /**
     * Date of the end of the time-window. This date is exclusive.
     */
    protected double endDate;

    public TimeWindow() {
        this(0);
    }

    public TimeWindow(double startDate) {
        this(startDate, Double.POSITIVE_INFINITY);
    }

    public TimeWindow(double startDate, double endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public double getStartDate() {
        return startDate;
    }

    public double getEndDate() {
        return endDate;
    }

    public void setStartDate(double startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(double endDate) {
        this.endDate = endDate;
    }

    public boolean isEnded() {
        return !Double.isInfinite(endDate);
    }

    public boolean isDate() {
        return Double.isNaN(endDate);
    }

    public boolean isOverlapping(TimeWindow timeWindow) {
        if (Double.isInfinite(endDate) && Double.isInfinite(timeWindow.endDate)) {
            return true;
        } else if (Double.isInfinite(endDate)) {
            return timeWindow.endDate > startDate;
        } else if (Double.isInfinite(timeWindow.endDate)) {
            return endDate > timeWindow.startDate;
        } else {
            return (timeWindow.startDate >= startDate && timeWindow.startDate < endDate) ||
                    (timeWindow.endDate <= endDate && timeWindow.endDate > startDate);
        }
    }

    public boolean contains(double date) {
        return date >= startDate && (Double.isInfinite(endDate) || date < endDate);
    }

    public boolean contains(TimeWindow timeWindow) {
        return timeWindow.startDate >= startDate && timeWindow.endDate <= endDate;
    }

    @Override
    public int compareTo(TimeWindow timeWindow) {
        if (timeWindow.isDate()) {
            return timeWindow.startDate < startDate ? 1 : (timeWindow.startDate >= endDate ? -1 : 0);
        } else if (isDate()) {
            return startDate < timeWindow.startDate ? -1 : (startDate >= timeWindow.endDate ? 1 : 0);
        }

        return Double.compare(startDate, timeWindow.startDate);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof TimeWindow) {
            TimeWindow otw = (TimeWindow) o;
            return startDate == otw.startDate && (Double.isInfinite(endDate) && Double.isInfinite(otw.endDate) || endDate == endDate);
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format("[%f;%s[", startDate, Double.isInfinite(endDate) ? "-" : Double.toString(endDate));
    }
}
