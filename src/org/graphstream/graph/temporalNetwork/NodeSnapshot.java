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

import org.graphstream.graph.*;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @since 08/02/16.
 */
public class NodeSnapshot extends ElementSnapshot<TemporalNode> implements Node {
    protected final List<TemporalEdge> edges;

    public NodeSnapshot(TemporalNode node, List<TemporalEdge> edges, double date) {
        super(node, date);
        this.edges = edges;
    }

    @Override
    public Graph getGraph() {
        return element.getTemporalNetwork().getGraphAt(snapshotDate);
    }

    @Override
    public int getDegree() {
        int d = 0;

        for (TemporalEdge e : edges) {
            if (e.existsAt(snapshotDate))
                d++;
        }

        return d;
    }

    @Override
    public int getOutDegree() {
        int d = 0;

        for (TemporalEdge e : edges) {
            if (e.getSourceNode() == element && e.existsAt(snapshotDate))
                d++;
        }

        return d;
    }

    @Override
    public int getInDegree() {
        int d = 0;

        for (TemporalEdge e : edges) {
            if (e.getSourceNode() == element && e.existsAt(snapshotDate))
                d++;
        }

        return d;
    }

    protected TemporalEdge getTemporalEdgeBetween(TemporalNode source, TemporalNode target, boolean directed) {
        for (TemporalEdge e : edges) {
            if (directed) {
                if (e.getSourceNode() == source && e.getTargetNode() == target && e.existsAt(snapshotDate))
                    return e;
            } else {
                if ((e.getSourceNode() == source || e.getTargetNode() == target) && e.existsAt(snapshotDate))
                    return e;
            }
        }

        return null;
    }

    @Override
    public boolean hasEdgeToward(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        return getTemporalEdgeBetween(element, other, true) != null;
    }

    @Override
    public boolean hasEdgeFrom(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        return getTemporalEdgeBetween(other, element, true) != null;
    }

    @Override
    public boolean hasEdgeBetween(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        return getTemporalEdgeBetween(other, other, false) != null;
    }

    @Override
    public <T extends Edge> T getEdgeToward(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        TemporalEdge e = getTemporalEdgeBetween(element, other, true);

        return e == null ? null : (T) e.getEdgeAt(snapshotDate);
    }

    @Override
    public <T extends Edge> T getEdgeFrom(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        TemporalEdge e = getTemporalEdgeBetween(other, element, true);

        return e == null ? null : (T) e.getEdgeAt(snapshotDate);
    }

    @Override
    public <T extends Edge> T getEdgeBetween(String id) {
        TemporalNode other = element.getTemporalNetwork().getTemporalNode(id);
        TemporalEdge e = getTemporalEdgeBetween(other, other, false);

        return e == null ? null : (T) e.getEdgeAt(snapshotDate);
    }

    @Override
    public <T extends Edge> Iterator<T> getEdgeIterator() {
        return new EdgeIterator<T>();
    }

    @Override
    public <T extends Edge> Iterator<T> getEnteringEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> Iterator<T> getLeavingEdgeIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getNeighborNodeIterator() {
        return null;
    }

    @Override
    public <T extends Edge> T getEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getEnteringEdge(int i) {
        return null;
    }

    @Override
    public <T extends Edge> T getLeavingEdge(int i) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getBreadthFirstIterator(boolean directed) {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator() {
        return null;
    }

    @Override
    public <T extends Node> Iterator<T> getDepthFirstIterator(boolean directed) {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachLeavingEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Iterable<T> getEachEnteringEdge() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getEdgeSet() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getLeavingEdgeSet() {
        return null;
    }

    @Override
    public <T extends Edge> Collection<T> getEnteringEdgeSet() {
        return null;
    }

    @Override
    public boolean hasEdgeToward(Node node) {
        return hasEdgeToward(node.getId());
    }

    @Override
    public boolean hasEdgeToward(int index) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeFrom(Node node) {
        return hasEdgeFrom(node.getId());
    }

    @Override
    public boolean hasEdgeFrom(int index) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public boolean hasEdgeBetween(Node node) {
        return hasEdgeBetween(node.getId());
    }

    @Override
    public boolean hasEdgeBetween(int index) throws IndexOutOfBoundsException {
        return false;
    }

    @Override
    public <T extends Edge> T getEdgeToward(Node node) {
        return getEdgeToward(node.getId());
    }

    @Override
    public <T extends Edge> T getEdgeToward(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeFrom(Node node) {
        return getEdgeFrom(node.getId());
    }

    @Override
    public <T extends Edge> T getEdgeFrom(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public <T extends Edge> T getEdgeBetween(Node node) {
        return getEdgeBetween(node.getId());
    }

    @Override
    public <T extends Edge> T getEdgeBetween(int index) throws IndexOutOfBoundsException {
        return null;
    }

    @Override
    public Iterator<Edge> iterator() {
        return getEdgeIterator();
    }

    protected class EdgeIterator<T extends Edge> implements Iterator<T> {
        protected final Iterator<TemporalEdge> it;
        protected boolean hasNext;
        protected TemporalEdge prev, next;

        protected EdgeIterator() {
            it = edges.iterator();
            prev = next = null;
            findNext();
        }

        protected void findNext() {
            prev = next;

            while (it.hasNext()) {
                next = it.next();

                if (checkDirection() && next.existsAt(snapshotDate)) {
                    hasNext = true;
                    return;
                }
            }

            next = null;
            hasNext = false;
        }

        @Override
        public boolean hasNext() {
            return hasNext;
        }

        @Override
        public T next() {
            if (next == null)
                return null;

            Edge e = next.getEdgeAt(snapshotDate);

            findNext();
            return (T) e;
        }

        @Override
        public void remove() {
            if (prev != null) {
                prev.removeAt(snapshotDate);
            }
        }

        protected boolean checkDirection() {
            return true;
        }
    }
}
