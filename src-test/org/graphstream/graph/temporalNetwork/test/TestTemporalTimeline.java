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

import org.graphstream.graph.temporalNetwork.InvalidTimeWindowException;
import org.graphstream.graph.temporalNetwork.TemporalTimeline;
import org.graphstream.graph.temporalNetwork.TimeWindow;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 */
public class TestTemporalTimeline {
    @Test
    public void testExistsAt() {
        TemporalTimeline<Boolean> ttl = new TemporalTimeline<>();

        ttl.insertTimeWindow(0, 10, true);
        ttl.insertTimeWindow(20, 30, true);

        Assert.assertTrue(ttl.existsAt(0));
        Assert.assertTrue(ttl.existsAt(5));
        Assert.assertFalse(ttl.existsAt(10));
        Assert.assertFalse(ttl.existsAt(15));
        Assert.assertTrue(ttl.existsAt(20));
        Assert.assertTrue(ttl.existsAt(25));
        Assert.assertFalse(ttl.existsAt(30));
        Assert.assertFalse(ttl.existsAt(35));
    }

    @Test
    public void testInsert() {
        TemporalTimeline<Double> ttl = new TemporalTimeline<>();
        double[][] dates = {{0, 10, 123.45}, {20, 30, 678.90}};

        for (double[] d : dates) {
            ttl.insertTimeWindow(d[0], d[1], d[2]);
        }

        int k = 0;

        for (TimeWindow tw : ttl) {
            Assert.assertTrue(tw.getStartDate() == dates[k][0]);
            Assert.assertTrue(tw.getEndDate() == dates[k][1]);
            Assert.assertTrue(ttl.getValueAt(tw) == dates[k][2]);

            k++;
        }
    }

    @Test
    public void testBeginEnd() {
        TemporalTimeline<Double> ttl = new TemporalTimeline<>();

        try {
            ttl.endTimeWindow(1);
            Assert.fail();
        } catch (InvalidTimeWindowException e) {
            Assert.assertNotNull(e);
        }

        ttl.startTimeWindow(1.0, 13.37);

        for (TimeWindow timeWindow : ttl) {
            Assert.assertTrue(timeWindow.getStartDate() == 1.0);
            Assert.assertFalse(timeWindow.isEnded());
            Assert.assertTrue(ttl.getValueAt(timeWindow) == 13.37);
        }

        ttl.endTimeWindow(5.0);

        for (TimeWindow timeWindow : ttl) {
            Assert.assertTrue(timeWindow.getStartDate() == 1.0);
            Assert.assertTrue(timeWindow.isEnded());
            Assert.assertTrue(timeWindow.getEndDate() == 5.0);
            Assert.assertTrue(ttl.getValueAt(timeWindow) == 13.37);
        }
    }

    @Test
    public void testHookedTimeWindow() {
        TemporalTimeline<Double> ttl = new TemporalTimeline<>();
        TimeWindow timeWindow = null;

        ttl.insertTimeWindow(10, 20, 13.37);
        ttl.insertTimeWindow(20, 30, 73.31);

        for (TimeWindow tw : ttl) {
            if (timeWindow == null)
                timeWindow = tw;
        }

        if (timeWindow != null)
            timeWindow.setStartDate(15);

        timeWindow = null;

        for (TimeWindow tw : ttl) {
            if (timeWindow == null) {
                Assert.assertTrue(tw.getStartDate() == 15.0);
                Assert.assertTrue(tw.getEndDate() == 20.0);

                timeWindow = tw;
            }
        }
    }
}
