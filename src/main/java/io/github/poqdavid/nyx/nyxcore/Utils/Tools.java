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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools.Settings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.api.Server;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotPos;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

public class Tools {
    public static boolean WriteFile(File file, String content) {
        FileWriter filew = null;

        if (file.getParentFile().mkdirs()) {
            NyxCore.getInstance().getLogger(null).error("Created missing directories");
        }

        if (file.exists()) {
            try {
                filew = new FileWriter(file.toString(), false);
                filew.write(content);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            } finally {
                if (filew != null) {
                    try {
                        filew.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        NyxCore.getInstance().getLogger(null).error(e.getMessage(), e);
                        return false;
                    }
                }
            }
        } else {
            try {
                if (file.createNewFile()) {
                    if (!content.equals("lock")) {
                        NyxCore.getInstance().getLogger(null).info("Created new file: " + file.getName());
                    }
                    WriteFile(file, content);
                }
            } catch (IOException e) {
                e.printStackTrace();
                NyxCore.getInstance().getLogger(null).error(e.getMessage(), e);
                return false;
            }
        }

        return true;
    }

    public static SlotPos indexToSP(Integer index) {
        switch (index) {
            case 0:
                return SlotPos.of(0, 0);
            case 1:
                return SlotPos.of(1, 0);
            case 2:
                return SlotPos.of(2, 0);
            case 3:
                return SlotPos.of(3, 0);
            case 4:
                return SlotPos.of(4, 0);
            case 5:
                return SlotPos.of(5, 0);
            case 6:
                return SlotPos.of(6, 0);
            case 7:
                return SlotPos.of(7, 0);
            case 8:
                return SlotPos.of(8, 0);

            case 9:
                return SlotPos.of(0, 1);
            case 10:
                return SlotPos.of(1, 1);
            case 11:
                return SlotPos.of(2, 1);
            case 12:
                return SlotPos.of(3, 1);
            case 13:
                return SlotPos.of(4, 1);
            case 14:
                return SlotPos.of(5, 1);
            case 15:
                return SlotPos.of(6, 1);
            case 16:
                return SlotPos.of(7, 1);
            case 17:
                return SlotPos.of(8, 1);

            case 18:
                return SlotPos.of(0, 2);
            case 19:
                return SlotPos.of(1, 2);
            case 20:
                return SlotPos.of(2, 2);
            case 21:
                return SlotPos.of(3, 2);
            case 22:
                return SlotPos.of(4, 2);
            case 23:
                return SlotPos.of(5, 2);
            case 24:
                return SlotPos.of(6, 2);
            case 25:
                return SlotPos.of(7, 2);
            case 26:
                return SlotPos.of(8, 2);

            case 27:
                return SlotPos.of(0, 3);
            case 28:
                return SlotPos.of(1, 3);
            case 29:
                return SlotPos.of(2, 3);
            case 30:
                return SlotPos.of(3, 3);
            case 31:
                return SlotPos.of(4, 3);
            case 32:
                return SlotPos.of(5, 3);
            case 33:
                return SlotPos.of(6, 3);
            case 34:
                return SlotPos.of(7, 3);
            case 35:
                return SlotPos.of(8, 3);

            case 36:
                return SlotPos.of(0, 4);
            case 37:
                return SlotPos.of(1, 4);
            case 38:
                return SlotPos.of(2, 4);
            case 39:
                return SlotPos.of(3, 4);
            case 40:
                return SlotPos.of(4, 4);
            case 41:
                return SlotPos.of(5, 4);
            case 42:
                return SlotPos.of(6, 4);
            case 43:
                return SlotPos.of(7, 4);
            case 44:
                return SlotPos.of(8, 4);

            case 45:
                return SlotPos.of(0, 5);
            case 46:
                return SlotPos.of(1, 5);
            case 47:
                return SlotPos.of(2, 5);
            case 48:
                return SlotPos.of(3, 5);
            case 49:
                return SlotPos.of(4, 5);
            case 50:
                return SlotPos.of(5, 5);
            case 51:
                return SlotPos.of(6, 5);
            case 52:
                return SlotPos.of(7, 5);
            case 53:
                return SlotPos.of(8, 5);

            default:
                return null;
        }
    }

    public static void MakeNewBP(Player player) {
        final Path filePath = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + player.getUniqueId() + ".backpack");
        final File file = filePath.toFile();

        if (!file.exists()) {
            if (!Tools.WriteFile(file, "{}")) {
                NyxCore.getInstance().getLogger(null).error("Failed to create backpack file on player join for " + player.getName());
            }
        }
    }

    public static String ContainerToBase64(DataContainer container) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            DataFormats.NBT.writeTo(out, container);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ItemStackToBase64(ItemStack itemStack) {
        return ContainerToBase64(itemStack.toContainer());
    }

    public static DataContainer Base64ToContainer(String base64) {
        return Base64ToContainer(Base64.getDecoder().decode(base64));
    }

    public static ItemStack Base64ToItemStack(String base64) {
        return ItemStack.builder().fromContainer(Base64ToContainer(base64)).build();
    }

    public static DataContainer Base64ToContainer(byte[] base64) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64)) {
            return DataFormats.NBT.readFrom(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Player getPlayer(CommandSource src, Server server) {
        return server.getPlayer(((Player) src).getUniqueId()).orElse(null);
    }

    public static Player getPlayer(CommandSource src) {
        return ((Player) src);
    }

    public static Player getPlayer(EntityPlayer entityHuman) {
        return ((Player) entityHuman);
    }

    public static EntityPlayerMP getPlayerE(CommandSource src, Server server) {
        return (EntityPlayerMP) server.getPlayer(((Player) src).getUniqueId()).orElse(null);
    }

    public static EntityPlayerMP getPlayerE(CommandSource src) {
        final Server server = NyxCore.getInstance().getGame().getServer();
        return (EntityPlayerMP) server.getPlayer(((Player) src).getUniqueId()).orElse(null);
    }

    public static Optional<Player> getPlayer(Cause cause) {
        return cause.first(Player.class);
    }

    public static boolean lockBackpack(User user, boolean log) {
        if (WriteFile(Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueId() + ".lock").toFile(), "lock")) {
            if (log) {
                NyxCore.getInstance().getLogger(null).info(user.getName() + " backpack's locked");
            }
            return true;
        } else {
            if (log) {
                NyxCore.getInstance().getLogger(null).error(user.getName() + " backpack's is locked or failed getting locked");
            }
            return false;
        }
    }

    public static boolean unlockBackpack(User user, boolean log) {
        Path file = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueId() + ".lock");
        File f = new File(file.toString());
        if (f.isFile()) {
            if (f.exists()) {
                if (f.delete()) {
                    if (log) {
                        NyxCore.getInstance().getLogger(null).info("Removed lock: " + f.getName());
                    }
                    return true;
                } else {
                    if (log) {
                        NyxCore.getInstance().getLogger(null).error("Failed to remove lock: " + f.getName());
                    }
                    return false;
                }
            } else {
                if (log) {
                    NyxCore.getInstance().getLogger(null).error("Lock Doesn't exists failed to remove lock: " + f.getName());
                }
                return false;
            }
        } else {
            if (log) {
                NyxCore.getInstance().getLogger(null).error("Failed to remove lock: " + f.getName() + "!!");
            }
            return false;
        }
    }

    public static Boolean backpackCheckLock(User user) throws CommandException {
        final Path file = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueId() + ".lock");
        return Files.exists(file);
    }

    public static void backpackUnlockAll() {
        try {
            File[] files = new File(NyxCore.getInstance().getBackpacksPath().toString()).listFiles((dir, name) -> name.endsWith(".lock"));
            if (files != null) {
                for (File file : files) {
                    if (file.isFile()) {
                        if (file.exists()) {
                            if (file.delete()) {
                                NyxCore.getInstance().getLogger(null).info("Removed lock: " + file.getName());
                            } else {
                                NyxCore.getInstance().getLogger(null).error("Failed to remove lock: " + file.getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveToJson(Path file, Settings jsonob) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (jsonob == null) {
            WriteFile(file.toFile(), "{}");
        } else {
            WriteFile(file.toFile(), gson.toJson(jsonob, jsonob.getClass()));
        }
    }

    public static Settings loadFromJson(Path file, Settings defob) {

        if (!Files.exists(file)) {
            try {
                saveToJson(file, defob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        Settings out = null;
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(file.toString()));
            out = gson.fromJson(br, defob.getClass());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return out;
    }
}
