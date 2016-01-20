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

import org.graphstream.graph.Element;
import org.graphstream.graph.TemporalElement;

import java.util.*;

/**
 *
 */
public class DefaultTemporalElement implements TemporalElement {
    protected final String id;
    protected TemporalTimeline<ElementSnapshot> timeline;
    protected ElementSnapshot currentSnapshot;
    protected AttributesTimeline attributes;

    protected DefaultTemporalElement(String id, double creationDate) {
        this.id = id;

        attributes = new AttributesTimeline();
        timeline = new TemporalTimeline<>();
        currentSnapshot = createSnapshot(creationDate);

        timeline.startTimeWindow(creationDate, currentSnapshot);
    }

    /*
     * @see org.graphstream.graph.TemporalElement#getElementTimeline()
     */
    @Override
    public TemporalTimeline<? extends Element> getElementTimeline() {
        return timeline;
    }


    /*
     * @see org.graphstream.graph.TemporalElement#existsAt(double)
     */
    @Override
    public boolean existsAt(double date) {
        return timeline.existsAt(date);
    }


    /*
     * @see org.graphstream.graph.TemporalElement#getElementAt(double)
     */
    @Override
    public Element getElementAt(double date) {
        return timeline.getValueAt(date);
    }

    @Override
    public void addAt(double date) {
        timeline.startTimeWindow(date, createSnapshot(date));
    }

    @Override
    public void removeAt(double date) {
        timeline.endTimeWindow(date);
    }

    protected ElementSnapshot createSnapshot(double date) {
        return new ElementSnapshot(date);
    }

    /*
     * @see org.graphstream.graph.Element#getId()
     */
    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getIndex() {
        return currentSnapshot.getIndex();
    }

    @Override
    public <T> T getAttribute(String key) {
        return currentSnapshot.getAttribute(key);
    }

    @Override
    public <T> T getFirstAttributeOf(String... keys) {
        return currentSnapshot.getFirstAttributeOf(keys);
    }

    @Override
    public <T> T getAttribute(String key, Class<T> clazz) {
        return currentSnapshot.getAttribute(key, clazz);
    }

    @Override
    public <T> T getFirstAttributeOf(Class<T> clazz, String... keys) {
        return currentSnapshot.getFirstAttributeOf(clazz, keys);
    }

    @Override
    public CharSequence getLabel(String key) {
        return currentSnapshot.getLabel(key);
    }

    @Override
    public double getNumber(String key) {
        return currentSnapshot.getNumber(key);
    }

    @Override
    public ArrayList<? extends Number> getVector(String key) {
        return currentSnapshot.getVector(key);
    }

    @Override
    public Object[] getArray(String key) {
        return currentSnapshot.getArray(key);
    }

    @Override
    public HashMap<?, ?> getHash(String key) {
        return currentSnapshot.getHash(key);
    }

    @Override
    public boolean hasAttribute(String key) {
        return currentSnapshot.hasAttribute(key);
    }

    @Override
    public boolean hasAttribute(String key, Class<?> clazz) {
        return currentSnapshot.hasAttribute(key, clazz);
    }

    @Override
    public boolean hasLabel(String key) {
        return currentSnapshot.hasLabel(key);
    }

    @Override
    public boolean hasNumber(String key) {
        return currentSnapshot.hasNumber(key);
    }

    @Override
    public boolean hasVector(String key) {
        return currentSnapshot.hasVector(key);
    }

    @Override
    public boolean hasArray(String key) {
        return currentSnapshot.hasArray(key);
    }

    @Override
    public boolean hasHash(String key) {
        return currentSnapshot.hasHash(key);
    }

    @Override
    public Iterator<String> getAttributeKeyIterator() {
        return currentSnapshot.getAttributeKeyIterator();
    }

    @Override
    public Iterable<String> getEachAttributeKey() {
        return currentSnapshot.getEachAttributeKey();
    }

    @Override
    public Collection<String> getAttributeKeySet() {
        return currentSnapshot.getAttributeKeySet();
    }

    @Override
    public void clearAttributes() {
        currentSnapshot.clearAttributes();
    }

    @Override
    public void addAttribute(String attribute, Object... values) {
        currentSnapshot.addAttribute(attribute, values);
    }

    @Override
    public void changeAttribute(String attribute, Object... values) {
        currentSnapshot.changeAttribute(attribute, values);
    }

    @Override
    public void setAttribute(String attribute, Object... values) {
        currentSnapshot.setAttribute(attribute, values);
    }

    @Override
    public void addAttributes(Map<String, Object> attributes) {
        currentSnapshot.addAttributes(attributes);
    }

    @Override
    public void removeAttribute(String attribute) {
        currentSnapshot.removeAttribute(attribute);
    }

    @Override
    public int getAttributeCount() {
        return currentSnapshot.getAttributeCount();
    }

    protected class ElementSnapshot implements Element {
        protected double snapshotDate;

        protected ElementSnapshot(double date) {
            snapshotDate = date;
        }

        @Override
        public String getId() {
            return DefaultTemporalElement.this.id;
        }

        @Override
        public int getIndex() {
            return -1;
        }

        @Override
        public <T> T getAttribute(String key) {
            return null;
        }

        @Override
        public <T> T getFirstAttributeOf(String... keys) {
            return null;
        }

        @Override
        public <T> T getAttribute(String key, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> T getFirstAttributeOf(Class<T> clazz, String... keys) {
            return null;
        }

        @Override
        public CharSequence getLabel(String key) {
            return null;
        }

        @Override
        public double getNumber(String key) {
            return 0;
        }

        @Override
        public ArrayList<? extends Number> getVector(String key) {
            return null;
        }

        @Override
        public Object[] getArray(String key) {
            return new Object[0];
        }

        @Override
        public HashMap<?, ?> getHash(String key) {
            return null;
        }

        @Override
        public boolean hasAttribute(String key) {
            return false;
        }

        @Override
        public boolean hasAttribute(String key, Class<?> clazz) {
            return false;
        }

        @Override
        public boolean hasLabel(String key) {
            return false;
        }

        @Override
        public boolean hasNumber(String key) {
            return false;
        }

        @Override
        public boolean hasVector(String key) {
            return false;
        }

        @Override
        public boolean hasArray(String key) {
            return false;
        }

        @Override
        public boolean hasHash(String key) {
            return false;
        }

        @Override
        public Iterator<String> getAttributeKeyIterator() {
            return null;
        }

        @Override
        public Iterable<String> getEachAttributeKey() {
            return null;
        }

        @Override
        public Collection<String> getAttributeKeySet() {
            return null;
        }

        @Override
        public void clearAttributes() {

        }

        @Override
        public void addAttribute(String attribute, Object... values) {

        }

        @Override
        public void changeAttribute(String attribute, Object... values) {

        }

        @Override
        public void setAttribute(String attribute, Object... values) {

        }

        @Override
        public void addAttributes(Map<String, Object> attributes) {

        }

        @Override
        public void removeAttribute(String attribute) {

        }

        @Override
        public int getAttributeCount() {
            return 0;
        }
    }
}
