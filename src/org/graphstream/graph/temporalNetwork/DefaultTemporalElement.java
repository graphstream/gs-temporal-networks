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
import org.graphstream.graph.TemporalNetwork;
import org.graphstream.graph.implementations.AbstractElement;

/**
 *
 */
public abstract class DefaultTemporalElement<T extends ElementSnapshot> implements TemporalElement {
    protected final String id;
    protected final int index;

    protected final TemporalNetwork network;
    protected TemporalTimeline<T> timeline;
    protected T currentSnapshot;
    protected AttributesTimeline attributes;

    protected DefaultTemporalElement(TemporalNetwork network, String id, int index, double creationDate) {
        this.id = id;
        this.index = index;
        this.network = network;

        attributes = new AttributesTimeline();
        timeline = new TemporalTimeline<>();
        currentSnapshot = createSnapshot(creationDate);

        timeline.startTimeWindow(creationDate, currentSnapshot);
    }

    /**
     * Called for each change in the attribute set. This method must be
     * implemented by sub-elements in order to send events to the graph
     * listeners.
     *
     * @param attribute The attribute name that changed.
     * @param event     The type of event among ADD, CHANGE and REMOVE.
     * @param oldValue  The old value of the attribute, null if the attribute was
     *                  added.
     * @param newValue  The new value of the attribute, null if the attribute is about
     *                  to be removed.
     */
    protected abstract void attributeChanged(AbstractElement.AttributeChangeEvent event,
                                             String attribute, Object oldValue, Object newValue);

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

    protected abstract T createSnapshot(double date);


    @Override
    public String getId() {
        return id;
    }

    @Override
    public AttributesTimeline getAttributesTimeline() {
        return attributes;
    }

    @Override
    public TemporalNetwork getTemporalNetwork() {
        return network;
    }
}
