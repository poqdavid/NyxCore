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

package io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Utils.Tools;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.Valid;
import java.io.Serializable;
import java.nio.file.Path;

public class Settings implements Serializable {

    private final static long serialVersionUID = 4280887333225310204L;
    @SerializedName("commands")
    @Expose
    @Valid
    private Commands commands;

    /**
     * No args constructor for use in serialization
     */
    public Settings() {
        this.setCommands(new Commands(true, true, true, true));
    }

    public Settings(Commands commands) {
        super();
        this.commands = commands;
    }


    public Settings(Path file) {
        this.Load(file);
    }

    public void Load(Path file) {
        try {
            Settings sets = Tools.loadFromJson(file, new Settings());
            NyxCore.getInstance().getLogger(null).info("Loading file: " + file.toString());
            this.setCommands(sets.getCommands());
        } catch (Exception e) {
            this.Save(file);
            NyxCore.getInstance().getLogger(null).error(e.getMessage(), e);
        }
    }

    public void Save(Path file) {
        Tools.saveToJson(file, this);
    }

    public Commands getCommands() {
        return commands;
    }

    public void setCommands(Commands commands) {
        this.commands = commands;
    }

    public Settings withCommands(Commands commands) {
        this.commands = commands;
        return this;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(commands).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Settings)) {
            return false;
        }
        Settings rhs = ((Settings) other);
        return new EqualsBuilder().append(commands, rhs.commands).isEquals();
    }

    public class Commands implements Serializable {

        private final static long serialVersionUID = 6630490888395483134L;
        @SerializedName("enable_enderchest")
        @Expose
        private boolean enableEnderchest;
        @SerializedName("enable_anvil")
        @Expose
        private boolean enableAnvil;
        @SerializedName("enable_workbench")
        @Expose
        private boolean enableWorkbench;
        @SerializedName("enable_enchantingtable")
        @Expose
        private boolean enableEnchantingTable;


        /**
         * No args constructor for use in serialization
         */
        public Commands() {
        }

        public Commands(boolean enableEnderchest, boolean enableAnvil, boolean enableWorkbench, boolean enableEnchantingTable) {
            super();
            this.enableEnderchest = enableEnderchest;
            this.enableAnvil = enableAnvil;
            this.enableWorkbench = enableWorkbench;
            this.enableEnchantingTable = enableEnchantingTable;
        }

        public boolean isEnderchestEnabled() {
            return enableEnderchest;
        }

        public void setEnderchest(boolean enableEnderchest) {
            this.enableEnderchest = enableEnderchest;
        }

        public Commands withEnderchest(boolean enableEnderchest) {
            this.enableEnderchest = enableEnderchest;
            return this;
        }

        public boolean isAnvilEnabled() {
            return enableAnvil;
        }

        public void setAnvil(boolean enableAnvil) {
            this.enableAnvil = enableAnvil;
        }

        public Commands withAnvil(boolean enableAnvil) {
            this.enableAnvil = enableAnvil;
            return this;
        }

        public boolean isWorkbenchEnabled() {
            return enableWorkbench;
        }

        public void setWorkbench(boolean enableWorkbench) {
            this.enableWorkbench = enableWorkbench;
        }

        public Commands withWorkbench(boolean enableWorkbench) {
            this.enableWorkbench = enableWorkbench;
            return this;
        }

        public boolean isEnchantingTableEnabled() {
            return enableEnchantingTable;
        }

        public void setEnchantingTable(boolean enableEnchantingTable) {
            this.enableEnchantingTable = enableEnchantingTable;
        }

        public Commands withEnchantingTable(boolean enableEnchantingTable) {
            this.enableEnchantingTable = enableEnchantingTable;
            return this;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(enableEnderchest).append(enableAnvil).append(enableWorkbench).append(enableEnchantingTable).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if (!(other instanceof Commands)) {
                return false;
            }
            Commands rhs = ((Commands) other);
            return new EqualsBuilder().append(enableEnderchest, rhs.enableEnderchest).append(enableAnvil, rhs.enableAnvil).append(enableWorkbench, rhs.enableWorkbench).append(enableEnchantingTable, rhs.enableEnchantingTable).isEquals();
        }

    }
}