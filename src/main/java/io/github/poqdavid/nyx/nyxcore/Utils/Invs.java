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

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.text.Text;

import java.util.Objects;


public class Invs {
    private final Object plugin;

    public Invs(Object plugin) {
        this.plugin = plugin;
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch, Text inventoryTitle) {
        final Inventory inv = Inventory.builder().of(invArch)
                .property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(inventoryTitle))
                .build(plugin);
        return this.Open(src, args, inv);

    }

    public CommandResult Open(CommandSource src, InventoryArchetype invArch, Text inventoryTitle) {
        final Inventory inv = Inventory.builder().of(invArch)
                .property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(inventoryTitle))
                .build(plugin);
        return this.Open(src, inv);
    }

    public CommandResult Open(CommandSource src, Inventory inv, InventoryArchetype invArch, Text inventoryTitle) {
        final Inventory invX = Inventory.builder().from(inv).of(invArch)
                .property(InventoryTitle.PROPERTY_NAME, new InventoryTitle(inventoryTitle))
                .build(plugin);
        return this.Open(src, invX);
    }

    public CommandResult Open(CommandSource src, CommandContext args, String invArch) {
        if (Objects.equals(invArch, "enderchest")) {
            final Player player = Tools.getPlayer(src);
            return this.Open(src, args, player.getEnderChestInventory());
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, CommandContext args, InventoryArchetype invArch) {
        final Inventory inv = org.spongepowered.api.item.inventory.Inventory.builder().of(invArch)
                .build(plugin);
        return this.Open(src, args, inv);
    }

    public CommandResult Open(CommandSource src, CommandContext args, org.spongepowered.api.item.inventory.Inventory i) {
        final Player player = Tools.getPlayer(src);
        if (src.getCommandSource().isPresent() && src.getCommandSource().get() instanceof Player) {
            player.openInventory(i);
            return CommandResult.success();
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, org.spongepowered.api.item.inventory.Inventory i) {
        final Player player = Tools.getPlayer(src);
        if (src.getCommandSource().isPresent() && src.getCommandSource().get() instanceof Player) {
            player.openInventory(i);
            return CommandResult.success();
        }
        return CommandResult.empty();
    }

    public CommandResult Open(CommandSource src, Container openCon, String inventoryTypeIn, String title) {
        final Player player = Tools.getPlayer(src);
        final EntityPlayerMP MPlayer = (EntityPlayerMP) player;
        MPlayer.getNextWindowId();
        MPlayer.connection.sendPacket(new SPacketOpenWindow(MPlayer.currentWindowId, inventoryTypeIn, new TextComponentString(title)));
        MPlayer.openContainer = openCon;
        MPlayer.openContainer.windowId = MPlayer.currentWindowId;
        MPlayer.openContainer.addListener(MPlayer);

        return CommandResult.success();
    }
}
