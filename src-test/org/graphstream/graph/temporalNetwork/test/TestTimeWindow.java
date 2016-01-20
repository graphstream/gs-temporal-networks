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
package org.graphstream.graph.temporalNetwork.test;

import org.graphstream.graph.temporalNetwork.TimeWindow;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TestTimeWindow {
    @Test
    public void testCreation() {
        TimeWindow timeWindow = new TimeWindow(13, 37);

        Assert.assertEquals(timeWindow.getStartDate(), 13, 0.0);
        Assert.assertEquals(timeWindow.getEndDate(), 37, 0.0);
        Assert.assertTrue(timeWindow.isEnded());

        timeWindow = new TimeWindow(1337.0);

        Assert.assertEquals(timeWindow.getStartDate(), 1337.0, 0.0);
        Assert.assertTrue(Double.isInfinite(timeWindow.getEndDate()));
        Assert.assertFalse(timeWindow.isEnded());
    }

    @Test
    public void testOverlapping() {
        TimeWindow tw1 = new TimeWindow(0, 10);
        TimeWindow tw2 = new TimeWindow(10, 11);
        TimeWindow tw3 = new TimeWindow(9, 10);
        TimeWindow tw4 = new TimeWindow(11, 13);
        TimeWindow tw5 = new TimeWindow(8);
        TimeWindow tw6 = new TimeWindow(10);

        Assert.assertFalse(tw1.isOverlapping(tw2));
        Assert.assertFalse(tw2.isOverlapping(tw1));

        Assert.assertFalse(tw1.isOverlapping(tw4));
        Assert.assertFalse(tw4.isOverlapping(tw1));

        Assert.assertTrue(tw1.isOverlapping(tw3));
        Assert.assertTrue(tw3.isOverlapping(tw1));

        Assert.assertFalse(tw2.isOverlapping(tw3));
        Assert.assertFalse(tw3.isOverlapping(tw2));

        Assert.assertFalse(tw2.isOverlapping(tw3));
        Assert.assertFalse(tw3.isOverlapping(tw2));

        Assert.assertFalse(tw1.isOverlapping(tw6));
        Assert.assertFalse(tw6.isOverlapping(tw1));

        Assert.assertTrue(tw1.isOverlapping(tw5));
        Assert.assertTrue(tw5.isOverlapping(tw1));

        Assert.assertTrue(tw5.isOverlapping(tw6));
        Assert.assertTrue(tw6.isOverlapping(tw5));
    }

    @Test
    public void testEquals() {
        TimeWindow tw1 = new TimeWindow(0, 10);
        TimeWindow tw2 = new TimeWindow(0, 10);

        Assert.assertTrue(tw1.equals(tw2));

        tw1 = new TimeWindow(8);
        tw2 = new TimeWindow(8);

        Assert.assertTrue(tw1.equals(tw2));
    }

    @Test
    public void testCompare() {
        TimeWindow tw1 = new TimeWindow(0, 10);
        TimeWindow tw2 = new TimeWindow(20, 30);
        TimeWindow tw3 = new TimeWindow(40, 50);
        TimeWindow tw4 = new TimeWindow(0, 10);

        Assert.assertTrue(tw1.compareTo(tw2) < 0);
        Assert.assertTrue(tw2.compareTo(tw3) < 0);
        Assert.assertTrue(tw1.compareTo(tw3) < 0);

        Assert.assertTrue(tw2.compareTo(tw1) > 0);
        Assert.assertTrue(tw3.compareTo(tw1) > 0);

        Assert.assertTrue(tw1.compareTo(tw4) == 0);
    }

    @Test
    public void testContains() {
        TimeWindow tw1 = new TimeWindow(10, 20);
        TimeWindow tw2 = new TimeWindow(10, 15);

        Assert.assertTrue(tw1.contains(tw2));
        Assert.assertFalse(tw2.contains(tw1));
        Assert.assertTrue(tw1.contains(10));
        Assert.assertTrue(tw1.contains(11));
        Assert.assertFalse(tw1.contains(20));
    }
}
