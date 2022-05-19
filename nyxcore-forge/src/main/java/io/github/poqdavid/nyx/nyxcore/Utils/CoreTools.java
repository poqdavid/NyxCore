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

import com.flowpowered.math.imaginary.Quaterniond;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Permissions.BackpackPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxMarket.NMSettings;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools.NTSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.server.permission.PermissionAPI;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class CoreTools {
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

    public static void MakeNewBP(EntityPlayerMP player) {
        final Path filePath = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + player.getUniqueID() + ".backpack");
        final File file = filePath.toFile();

        if (!file.exists()) {
            if (!CoreTools.WriteFile(file, "{}")) {
                NyxCore.getInstance().getLogger(null).error("Failed to create backpack file on player join for " + player.getName());
            }
        }
    }

    public static EntityPlayerMP getPlayer(EntityPlayer entityHuman) {
        return ((EntityPlayerMP) entityHuman);
    }

    public static boolean lockBackpack(EntityPlayer user, boolean log) {
        if (WriteFile(Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueID() + ".lock").toFile(), "lock")) {
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

    public static boolean unlockBackpack(EntityPlayer user, boolean log) {
        Path file = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueID() + ".lock");
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

    public static Boolean backpackCheckLock(EntityPlayer user) throws Exception {
        final Path file = Paths.get(NyxCore.getInstance().getBackpacksPath() + File.separator + user.getUniqueID() + ".lock");
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

    public static void saveToJson(Path file, NTSettings jsonob) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (jsonob == null) {
            WriteFile(file.toFile(), "{}");
        } else {
            WriteFile(file.toFile(), gson.toJson(jsonob, jsonob.getClass()));
        }
    }

    public static void saveToJson(Path file, NMSettings jsonob) {
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

        if (jsonob == null) {
            WriteFile(file.toFile(), "{}");
        } else {
            WriteFile(file.toFile(), gson.toJson(jsonob, jsonob.getClass()));
        }
    }

    public static NTSettings loadFromJson(Path file, NTSettings defob) {

        if (!Files.exists(file)) {
            try {
                saveToJson(file, defob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        NTSettings out = null;
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

    public static NMSettings loadFromJson(Path file, NMSettings defob) {

        if (!Files.exists(file)) {
            try {
                saveToJson(file, defob);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Gson gson = new Gson();
        NMSettings out = null;
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

    public static String getDirection(EntityPlayerMP player) {
        String playerDirection;
        double yaw = player.getRotationYawHead();
        //World w = player.getWorld();
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();

        if (yaw < 0) {
            yaw = yaw + 360;
        }

        if ((yaw >= 315) && (yaw <= 360)) {
            playerDirection = "south";
        } else if ((yaw >= 0) && (yaw <= 45)) {
            playerDirection = "south";
        } else if ((yaw >= 45) && (yaw <= 135)) {
            playerDirection = "west";
        } else if ((yaw >= 135) && (yaw <= 180)) {
            playerDirection = "north";
        } else if ((yaw >= 180) && (yaw <= 225)) {
            playerDirection = "north";
        } else if ((yaw >= 225) && (yaw <= 315)) {
            playerDirection = "east";
        } else {
            playerDirection = "east";
        }

        return playerDirection;
    }

    public static Vec3d getBL(EntityPlayerMP player) {

        double x = player.getPosition().getX();
        double y = player.getPosition().getY();
        double z = player.getPosition().getZ();
        final String playerDirection = CoreTools.getDirection(player);

        if (playerDirection == "north") {
            z = z + 1;
        } else if (playerDirection == "east") {
            x = x - 1;
        } else if (playerDirection == "south") {
            z = z - 1;
        } else if (playerDirection == "west") {
            x = x + 1;
        }

        return new Vec3d(x, y, z);
    }

    public static BlockPos GetLocation(EntityPlayerMP player, Quaterniond Qu, Vec3d distance) {
        final Vec3d dir = new Vec3d(Qu.getDirection().getX(), Qu.getDirection().getY(), Qu.getDirection().getZ());
        final Vec3d diff = dir.add(distance);
        return player.getPosition().add(new Vec3i(diff.x, diff.y, diff.z));
    }

    public static BlockPos GetLocation(EntityPlayerMP player, Vec3d distance) {
        final Vec3d rotation = player.getLookVec();
        return GetLocation(player, Quaterniond.fromAxesAnglesDeg(rotation.x, -rotation.y, rotation.z), distance);
    }

    public static Vec3d rotateAroundAxisY(Vec3d v, double cos, double sin) {
        double x = v.x * cos + v.z * sin;
        double z = v.x * -sin + v.z * cos;
        return new Vec3d(x, v.y, z);
    }

    public static NBTTagCompound SNBTtoNBT(String json) throws NBTException {
        return JsonToNBT.getTagFromJson(json);
    }

    public static String NBTtoSNBT(NBTTagCompound nbt) {
        return nbt.toString();
    }

    public static JsonObject NBTtoJSON(NBTTagCompound nbt) {
        return NBTtoJSON(nbt).getAsJsonObject();
    }

    public static String NBTToBase64(NBTTagCompound compound) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            CompressedStreamTools.writeCompressed(compound, out);
            return Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String ItemStackToBase64(ItemStack itemStack) {
        NBTTagCompound nbt = new NBTTagCompound();
        itemStack.writeToNBT(nbt);
        return NBTToBase64(nbt);
    }

    public static NBTTagCompound Base64ToNBT(String base64) {
        return Base64ToNBT(Base64.getDecoder().decode(base64));
    }

    public static NBTTagCompound Base64ToNBT(byte[] base64) {
        try (ByteArrayInputStream in = new ByteArrayInputStream(base64)) {
            return CompressedStreamTools.readCompressed(in);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack Base64ToItemStack(byte[] base64) {
        NBTTagCompound nbt = Base64ToNBT(base64);
        if (nbt != null) {
            return new ItemStack(nbt);
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static String getItemName(ItemStack itemStack) {
        return itemStack.getDisplayName();
    }

    public static int getBackpackSize(EntityPlayer user) {
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_SIX))
            return 6;
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE))
            return 5;
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR))
            return 4;
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_THREE))
            return 3;
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_TWO))
            return 2;
        if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_ONE))
            return 1;
        return 1;
    }

    public static int getBackpackSize(EntityPlayer user, Integer size) {
        if (size == 0) {
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_SIX))
                return 6;
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE))
                return 5;
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR))
                return 4;
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_THREE))
                return 3;
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_TWO))
                return 2;
            if (PermissionAPI.hasPermission(user, BackpackPermission.COMMAND_BACKPACK_SIZE_ONE))
                return 1;
            return 1;
        } else {
            if (size < 6) {
                return 6;
            } else {
                return size;
            }
        }

    }

    public static ModContainer getForgeMod(String modid) {
        for (ModContainer mod : Loader.instance().getActiveModList()) {
            if (Loader.instance().getIndexedModList().containsKey(modid)) {
                return Loader.instance().getIndexedModList().get(modid);
            }
        }
        return null;
    }
}
