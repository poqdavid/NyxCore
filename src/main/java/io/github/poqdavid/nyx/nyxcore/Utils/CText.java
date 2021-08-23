/*
 *     This file is part of NyxCore.
 *
 *     NyxCore is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     NyxCore is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with NyxCore.  If not, see <https://www.gnu.org/licenses/>.
 *
 *     Copyright (c) POQDavid <https://github.com/poqdavid>
 *     Copyright (c) contributors
 */

package io.github.poqdavid.nyx.nyxcore.Utils;

public class CText {
    public static final int NORMAL = 0;
    public static final int BRIGHT = 1;

    public static String get(Colors fc, String text) {
        return "\u001b[0;" + fc.value + "m" + text + "\u001b[m";
    }

    public static String get(Colors fc, Integer colormod, String text) {
        return "\u001b[" + colormod + ";" + fc.value + "m" + text + "\u001b[m";
    }

    public enum Colors {
        BLACK(30),
        RED(31),
        GREEN(32),
        YELLOW(33),
        BLUE(34),
        MAGENTA(35),
        CYAN(36),
        WHITE(37),
        ;

        private final int value;

        Colors(int i) {
            this.value = i;
        }
    }

    public enum BGColors {
        BLACK(40),
        RED(41),
        GREEN(42),
        YELLOW(43),
        BLUE(44),
        MAGENTA(45),
        CYAN(46),
        WHITE(47),
        ;

        private final int value;

        BGColors(int i) {
            this.value = i;
        }
    }
//echo -e "\u001b[0;4m\u001b[0;44m\u001b[31m Blue Background Underline \u001b[0m"
}
