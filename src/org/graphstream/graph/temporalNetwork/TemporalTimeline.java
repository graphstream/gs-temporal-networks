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

import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Build a timeline using a auto-balancing binary search tree. This will store an ordered list of time-windows, with a value associated with each of these time-window.
 */
public class TemporalTimeline<T> implements Iterable<TimeWindow> {
    private static final Logger LOGGER = Logger.getLogger(TemporalTimeline.class.getName());

    /**
     * The binary search tree that will handle time-windows and their associated values.
     */
    protected NavigableMap<TimeWindow, T> mapping;

    public TemporalTimeline() {
        mapping = new TreeMap<>();
    }

    /*
     * @see java.lang.Iterable#iterator()
     */
    @Override
    public Iterator<TimeWindow> iterator() {
        return mapping.keySet().iterator();
    }

    /**
     * Return the value associated with the time-window containing the given date.
     * If there is no such time-window, null is returned.
     *
     * @param date the date we want to get the value associated with
     * @return the value associated with the time-window containing date
     */
    public T getValueAt(double date) {
        return mapping.get(TimeWindow.wrapDate(date));
    }

    /**
     * Same as {@link #getValueAt(double)} but using a time-window rather than a date.
     *
     * @param timeWindow
     * @return
     */
    public T getValueAt(TimeWindow timeWindow) {
        return mapping.get(timeWindow);
    }

    public T removeTimeWindow(TimeWindow timeWindow) {
        return mapping.remove(timeWindow);
    }

    /**
     * Check if this timeline contains a time-window containing `date`.
     *
     * @param date
     * @return true if there is a time-window containing date, else false
     */
    public boolean existsAt(double date) {
        return mapping.containsKey(TimeWindow.wrapDate(date));
    }

    /**
     * Insert a new time-window, with its associated value, into the range tree.
     *
     * @param start           start-date of the time-window
     * @param end             end-date of the time-window
     * @param associatedValue value associated with the new time-window
     */
    public void insertTimeWindow(double start, double end, T associatedValue) {
        TimeWindow timeWindow = new HookedTimeWindow(start, end);
        mapping.put(timeWindow, associatedValue);
    }

    /**
     * Start a new time-window inside this timeline.
     * If the last time-window is not ended, the timeline will end it for you. The start-date of the new time-window should be greater or equal to the end-date of the last time-window.
     *
     * @param start           start-date of the new time-window that will be created
     * @param associatedValue the value associated with the new time-window
     */
    public void startTimeWindow(double start, T associatedValue) {
        if (mapping.size() > 0) {
            if (!mapping.lastKey().isEnded()) {
                endTimeWindow(start);
            } else if (start < mapping.lastKey().getEndDate()) {
                throw new InvalidTimeWindowException("When starting a new time-window, start-date should be greater than the end-date of the last existing windows.");
            }
        }

        TimeWindow timeWindow = new HookedTimeWindow(start);
        mapping.put(timeWindow, associatedValue);
    }

    /**
     * End the last time-window of this timeline.
     * A call to {@link #startTimeWindow(double, Object)} should have be done before calling this method.
     * If last time-window is already ended, a {@link org.graphstream.graph.temporalNetwork.InvalidTimeWindowException} will be thrown.
     *
     * @param end end-date of the last time-window
     */
    public void endTimeWindow(double end) {
        Map.Entry<TimeWindow, T> entry = mapping.lastEntry();

        if (entry == null) {
            throw new InvalidTimeWindowException("try to end last time window but timeline is empty.");
        } else if (entry.getKey().isEnded()) {
            throw new InvalidTimeWindowException("try to end last time window but last window is already ended.");
        } else {
            TimeWindow timeWindow = entry.getKey();
            timeWindow.setEndDate(end);
        }
    }

    /**
     * Get the closest time-window containing or greater than the given date.
     *
     * @param date
     * @return
     */
    public TimeWindow getCeilingTimeWindow(double date) {
        return mapping.ceilingKey(TimeWindow.wrapDate(date));
    }

    /**
     * Get the closest time-window containing or less than the given date.
     *
     * @param date
     * @return
     */
    public TimeWindow getFloorTimeWindow(double date) {
        return mapping.floorKey(TimeWindow.wrapDate(date));
    }

    protected class HookedTimeWindow extends TimeWindow {
        HookedTimeWindow() {
            super();
        }

        HookedTimeWindow(double start) {
            super(start);
        }

        HookedTimeWindow(double start, double end) {
            super(start, end);
        }

        @Override
        public void setStartDate(double date) {
            Map.Entry<TimeWindow, T> entry = TemporalTimeline.this.mapping.floorEntry(this);

            assert date < endDate;

            if (entry.getKey() == this) {
                T value = entry.getValue();
                TemporalTimeline.this.mapping.remove(this);
                super.setStartDate(date);
                TemporalTimeline.this.mapping.put(this, value);
            } else {
                super.setStartDate(date);
            }
        }

        @Override
        public void setEndDate(double date) {
            Map.Entry<TimeWindow, T> entry = TemporalTimeline.this.mapping.floorEntry(this);

            assert date > startDate;

            if (entry.getKey() == this) {
                T value = entry.getValue();
                TemporalTimeline.this.mapping.remove(this);
                super.setEndDate(date);
                TemporalTimeline.this.mapping.put(this, value);
            } else {
                super.setEndDate(date);
            }
        }
    }
}
