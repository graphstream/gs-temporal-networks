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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Guilhelm Savin
 * @since 20/01/16.
 */
public class AttributesTimeline {
    private static final Logger LOGGER = Logger.getLogger(AttributesTimeline.class.getName());

    protected Map<String, TemporalTimeline<Object>> attributes;

    public AttributesTimeline() {
        attributes = new HashMap<>();
    }

    public boolean hasAttributeAt(String key, double date) {
        TemporalTimeline<?> ttl = attributes.get(key);
        return ttl == null ? false : ttl.getValueAt(date) != null;
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttributeAt(String key, double date) {
        TemporalTimeline<?> ttl = attributes.get(key);

        if (ttl == null)
            LOGGER.info("no timeline for attribute \"" + key + "\"");

        return ttl == null ? null : (T) ttl.getValueAt(date);
    }

    public void setAttributeAt(String key, double date, Object... values) {
        TemporalTimeline<Object> ttl = attributes.get(key);
        Object value = values == null ? Boolean.TRUE : (values.length == 1 ? values[0] : values);

        if (ttl == null) {
            ttl = new TemporalTimeline<>();
            attributes.put(key, ttl);
        }

        TimeWindow before = ttl.getFloorTimeWindow(date);
        TimeWindow after = ttl.getCeilingTimeWindow(date);

        if (before != null && (!before.isEnded() || before.getEndDate() > date)) {
            before.setEndDate(date);
        }

        if (after == null || after == before) {
            ttl.startTimeWindow(date, value);
        } else {
            if (after.getStartDate() <= date) {
                ttl.removeTimeWindow(after);
            }

            ttl.insertTimeWindow(date, after.getStartDate(), value);
        }
    }

    public void removeAttributeAt(String key, double date) {
        TemporalTimeline<Object> ttl = attributes.get(key);

        if (ttl == null) {
            return;
        }

        TimeWindow timeWindow = ttl.getFloorTimeWindow(date);

        if (timeWindow != null && timeWindow.contains(date)) {
            timeWindow.setEndDate(date);
        }
    }

    public void clearAttributesAt(double date) {
        for (String key : attributes.keySet()) {
            removeAttributeAt(key, date);
        }
    }

    public int getAttributesCountAt(double date) {
        int c = 0;

        for (TemporalTimeline<?> ttl : attributes.values()) {
            if (ttl.existsAt(date)) {
                c++;
            }
        }

        return c;
    }

    public Iterator<String> getKeyIteratorAt(double date) {
        return new KeyIteratorAt(date);
    }

    protected class KeyIteratorAt implements Iterator<String> {
        protected final double date;
        Iterator<Map.Entry<String, TemporalTimeline<Object>>> it;

        String prev, next;

        KeyIteratorAt(double date) {
            this.date = date;
            it = attributes.entrySet().iterator();
            prev = null;

            findNext();
        }

        void findNext() {
            next = null;

            while (next == null && it.hasNext()) {
                Map.Entry<String, TemporalTimeline<Object>> e = it.next();
                TemporalTimeline<Object> ttl = e.getValue();

                if (ttl.existsAt(date)) {
                    next = e.getKey();
                }
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public String next() {
            prev = next;
            findNext();

            return prev;
        }

        @Override
        public void remove() {
            if (prev != null) {
                removeAttributeAt(prev, date);
            }
        }
    }
}
