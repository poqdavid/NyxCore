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

package io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxMarket;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Utils.CoreTools;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.nio.file.Path;

public class NMSettings implements Serializable {

    private static long serialVersionUID = 3330320144420690910L;
    @SerializedName("ItemExpiryTime")
    @Expose
    private Integer itemExpiryTime;

    /**
     * No args constructor for use in serialization
     */
    public NMSettings() {
        this.setItemExpiryTime(5760);
    }

    /**
     * @param itemExpiryTime
     */
    public NMSettings(Integer itemExpiryTime) {
        super();
        this.itemExpiryTime = itemExpiryTime;
    }

    public NMSettings(Path file) {
        this.Load(file);
    }

    public void Load(Path file) {
        try {
            NMSettings sets = CoreTools.loadFromJson(file, new NMSettings());
            NyxCore.getInstance().getLogger(null).info("Loading file: " + file.toString());
            this.setItemExpiryTime(sets.getItemExpiryTime());
        } catch (Exception e) {
            this.Save(file);
            NyxCore.getInstance().getLogger(null).error(e.getMessage(), e);
        }
    }

    public void Save(Path file) {
        CoreTools.saveToJson(file, this);
    }

    public Integer getItemExpiryTime() {
        return itemExpiryTime;
    }

    public void setItemExpiryTime(Integer itemExpiryTime) {
        this.itemExpiryTime = itemExpiryTime;
    }

    public NMSettings withItemExpiryTime(Integer itemExpiryTime) {
        this.itemExpiryTime = itemExpiryTime;
        return this;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("itemExpiryTime", itemExpiryTime).toString();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(itemExpiryTime).toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof NMSettings) == false) {
            return false;
        }
        NMSettings rhs = ((NMSettings) other);
        return new EqualsBuilder().append(itemExpiryTime, rhs.itemExpiryTime).isEquals();
    }
}