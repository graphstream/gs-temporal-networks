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

import org.graphstream.graph.temporalNetwork.AttributesTimeline;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Guilhelm Savin
 * @since 20/01/16.
 */
public class TestAttributesTimeline {
    @Test
    public void testSetAttributeAt() {
        AttributesTimeline atl = new AttributesTimeline();
        atl.setAttributeAt("test", 1.0, 13.37);

        Assert.assertNull(atl.getAttributeAt("test", 0.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 1.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 2.0));

        Assert.assertEquals(atl.getAttributeAt("test", 1.0), 13.37);
        Assert.assertEquals(atl.getAttributeAt("test", 2.0), 13.37);

        atl.setAttributeAt("test", 3.0, 4.2);

        Assert.assertNull(atl.getAttributeAt("test", 0.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 1.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 2.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 3.0));
        Assert.assertNotNull(atl.getAttributeAt("test", 4.0));

        Assert.assertEquals(atl.getAttributeAt("test", 1.0), 13.37);
        Assert.assertEquals(atl.getAttributeAt("test", 2.0), 13.37);
        Assert.assertEquals(atl.getAttributeAt("test", 3.0), 4.2);
        Assert.assertEquals(atl.getAttributeAt("test", 4.0), 4.2);
    }

    @Test
    public void testRemoveAttributeAt() {
        AttributesTimeline atl = new AttributesTimeline();
        atl.setAttributeAt("test", 1.0, 13.37);
        atl.setAttributeAt("test", 20.0, 4.2);
        atl.removeAttributeAt("test", 10.0);

        for (double i = 1; i < 10; i++) {
            Assert.assertNotNull(atl.getAttributeAt("test", i));
            Assert.assertEquals(atl.getAttributeAt("test", i), 13.37);
        }

        for (double i = 10; i < 20; i++) {
            Assert.assertNull(atl.getAttributeAt("test", i));
        }

        for (double i = 20; i < 30; i++) {
            Assert.assertNotNull(atl.getAttributeAt("test", i));
            Assert.assertEquals(atl.getAttributeAt("test", i), 4.2);
        }
    }

    @Test
    public void testClearAttributesAt() {
        AttributesTimeline atl = new AttributesTimeline();
        atl.setAttributeAt("test", 1.0, 13.37);
        atl.setAttributeAt("test", 20.0, 4.2);
        atl.clearAttributesAt(10.0);

        for (double i = 1; i < 10; i++) {
            Assert.assertNotNull(atl.getAttributeAt("test", i));
            Assert.assertEquals(atl.getAttributeAt("test", i), 13.37);
        }

        for (double i = 10; i < 20; i++) {
            Assert.assertNull(atl.getAttributeAt("test", i));
        }

        for (double i = 20; i < 30; i++) {
            Assert.assertNotNull(atl.getAttributeAt("test", i));
            Assert.assertEquals(atl.getAttributeAt("test", i), 4.2);
        }
    }

    @Test
    public void testAttributesCountAt() {
        AttributesTimeline atl = new AttributesTimeline();
        atl.setAttributeAt("test", 1.0, 13.37);
        atl.setAttributeAt("test", 20.0, 4.2);
        atl.setAttributeAt("tset", 5.0, 12.34);
        atl.clearAttributesAt(10.0);

        Assert.assertEquals(atl.getAttributesCountAt(0.0), 0);
        Assert.assertEquals(atl.getAttributesCountAt(1.0), 1);
        Assert.assertEquals(atl.getAttributesCountAt(5.0), 2);
        Assert.assertEquals(atl.getAttributesCountAt(10.0), 0);
        Assert.assertEquals(atl.getAttributesCountAt(20.0), 1);
    }
}
