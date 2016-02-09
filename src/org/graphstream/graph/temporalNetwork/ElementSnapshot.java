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
 * @since 08/02/16.
 */
public abstract class ElementSnapshot<T extends TemporalElement> implements Element {
    protected final T element;
    protected double snapshotDate;

    protected ElementSnapshot(T element, double date) {
        this.element = element;
        snapshotDate = date;
    }

    @Override
    public String getId() {
        return element.getId();
    }

    @Override
    public int getIndex() {
        return -1;
    }

    @Override
    public <T> T getAttribute(String key) {
        return element.getAttributesTimeline().getAttributeAt(key, snapshotDate);
    }

    @Override
    public <T> T getFirstAttributeOf(String... keys) {
        for (String key : keys) {
            T a = getAttribute(key);

            if (a != null)
                return a;
        }

        return null;
    }

    @Override
    public <T> T getAttribute(String key, Class<T> clazz) {
        Object v = getAttribute(key);

        if (v == null) {
            return null;
        }

        if (clazz.isAssignableFrom(v.getClass())) {
            return (T) v;
        }

        return null;
    }

    @Override
    public <T> T getFirstAttributeOf(Class<T> clazz, String... keys) {
        return null;
    }

    @Override
    public CharSequence getLabel(String key) {
        return getAttribute(key, CharSequence.class);
    }

    @Override
    public double getNumber(String key) {
        Number n = (Number) getAttribute(key, Number.class);
        return n == null ? Double.NaN : n.doubleValue();
    }

    @Override
    public ArrayList<? extends Number> getVector(String key) {
        return getAttribute(key, ArrayList.class);
    }

    @Override
    public Object[] getArray(String key) {
        return getAttribute(key, Object[].class);
    }

    @Override
    public HashMap<?, ?> getHash(String key) {
        return getAttribute(key, HashMap.class);
    }

    @Override
    public boolean hasAttribute(String key) {
        return element.getAttributesTimeline().hasAttributeAt(key, snapshotDate);
    }

    @Override
    public boolean hasAttribute(String key, Class<?> clazz) {
        return getAttribute(key, clazz) != null;
    }

    @Override
    public boolean hasLabel(String key) {
        return hasAttribute(key, CharSequence.class);
    }

    @Override
    public boolean hasNumber(String key) {
        return hasAttribute(key, Number.class);
    }

    @Override
    public boolean hasVector(String key) {
        return hasAttribute(key, ArrayList.class);
    }

    @Override
    public boolean hasArray(String key) {
        return hasAttribute(key, Object[].class);
    }

    @Override
    public boolean hasHash(String key) {
        return hasAttribute(key, HashMap.class);
    }

    @Override
    public Iterator<String> getAttributeKeyIterator() {
        return element.getAttributesTimeline().getKeyIteratorAt(snapshotDate);
    }

    @Override
    public Iterable<String> getEachAttributeKey() {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return getAttributeKeyIterator();
            }
        };
    }

    @Deprecated
    @Override
    public Collection<String> getAttributeKeySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clearAttributes() {
        element.getAttributesTimeline().clearAttributesAt(snapshotDate);
    }

    @Override
    public void addAttribute(String attribute, Object... values) {
        element.getAttributesTimeline().setAttributeAt(attribute, snapshotDate, values);
    }

    @Override
    public void changeAttribute(String attribute, Object... values) {
        element.getAttributesTimeline().setAttributeAt(attribute, snapshotDate, values);
    }

    @Override
    public void setAttribute(String attribute, Object... values) {
        element.getAttributesTimeline().setAttributeAt(attribute, snapshotDate, values);
    }

    @Override
    public void addAttributes(Map<String, Object> attributes) {
        if (attributes != null) {
            for (Map.Entry<String, Object> e : attributes.entrySet()) {
                element.getAttributesTimeline().setAttributeAt(e.getKey(), snapshotDate, e.getValue());
            }
        }
    }

    @Override
    public void removeAttribute(String attribute) {
        element.getAttributesTimeline().removeAttributeAt(attribute, snapshotDate);
    }

    @Override
    public int getAttributeCount() {
        return element.getAttributesTimeline().getAttributesCountAt(snapshotDate);
    }
}
