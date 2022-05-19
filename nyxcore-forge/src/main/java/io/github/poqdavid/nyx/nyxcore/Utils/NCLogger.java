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
 *     Copyright (c) POQDavid <https://github.com/poqdavid/NyxCore>
 *     Copyright (c) contributors
 */

package io.github.poqdavid.nyx.nyxcore.Utils;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class NCLogger {
    public String LoggerName = CText.get(CText.Colors.BLUE, 1, "Nyx") + CText.get(CText.Colors.MAGENTA, 0, "Core");
    private final Logger logger;

    public NCLogger() {
        this.logger = LogManager.getLogger(LoggerName);
    }

    public NCLogger(String name) {
        this.logger = LogManager.getLogger(name);
        this.LoggerName = this.logger.getName();
    }

    public void info(String msg) {
        this.logger.info(CText.get(CText.Colors.YELLOW, 0, msg));
    }

    public void error(String msg) {
        this.logger.error(CText.get(CText.Colors.RED, 0, msg));
    }

    public void error(String msg, Throwable tw) {
        this.logger.error(CText.get(CText.Colors.RED, 0, msg), tw);
    }
}
