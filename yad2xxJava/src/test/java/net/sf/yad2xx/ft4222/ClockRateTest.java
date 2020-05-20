/*
 * Copyright 2020 Stephen Davies
 *
 * This file is part of yad2xxJava.
 *
 * yad2xxJava is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * yad2xxJava is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with yad2xxJava. If not, see <https://www.gnu.org/licenses/>.
 */

package net.sf.yad2xx.ft4222;

import static org.junit.Assert.assertSame;

import org.junit.Test;

/**
 * Unit test ClockRate enumeration.
 *
 * @author Stephen Davies
 * @since May 2020
 * @since 2.1
 */
public class ClockRateTest {

    @Test
    public void testByOrdinalLowest() {
        assertSame(ClockRate.SYS_CLK_60, ClockRate.byOrdinal(0));
    }

}
