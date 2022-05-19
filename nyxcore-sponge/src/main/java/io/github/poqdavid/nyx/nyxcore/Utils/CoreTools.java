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
import com.flowpowered.math.vector.Vector3d;
import com.google.common.base.Charsets;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.poqdavid.nyx.nyxcore.NyxCore;
import io.github.poqdavid.nyx.nyxcore.Permissions.BackpackPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxMarket.NMSettings;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools.NTSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.persistence.DataFormats;
import org.spongepowered.api.data.persistence.DataTranslators;
import org.spongepowered.api.effect.particle.ParticleType;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Optional;

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
            if (!CoreTools.WriteFile(file, "{}")) {
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

    public static String serializeToJSON(BlockState items) {


        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.setPrettyPrinting();

        //gsonBuilder.registerTypeAdapter(ParticleDataList.class, new InterfaceAdapter<ParticleDataList>());

        Gson gson = gsonBuilder.create();
        Type type = new TypeToken<BlockState>() {
        }.getType();
        return gson.toJson(items, type);
    }

    public static String serialize(DataContainer container) throws IOException {
        try (StringWriter writer = new StringWriter()) {
            HoconConfigurationLoader loader = HoconConfigurationLoader.builder().setSink(() -> new BufferedWriter(writer)).build();
            loader.save(DataTranslators.CONFIGURATION_NODE.translate(container));
            return Base64.getEncoder().encodeToString(writer.toString().getBytes(Charsets.UTF_8));
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static DataContainer deSerialize(String json) {
        try (StringReader reader = new StringReader(new String(Base64.getDecoder().decode(json), Charsets.UTF_8))) {
            return DataTranslators.CONFIGURATION_NODE.translate(HoconConfigurationLoader.builder().setSource(() -> new BufferedReader(reader)).build().load()).getContainer();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ParticleType GetParticleType(String id) {
        ParticleType pt = ParticleTypes.FIRE_SMOKE;
        Optional<ParticleType> optpt = Sponge.getRegistry().getType(ParticleType.class, id);

        if (optpt.isPresent()) {
            pt = optpt.get();
        }

        return pt;
    }

    public static BlockState GetBlock(String id) {
        return Sponge.getRegistry().getType(BlockState.class, id).orElse(BlockTypes.AIR.getDefaultState());
    }


    public static String getDirection(Player player) {
        String playerDirection;
        double yaw = player.getRotation().getY();
        //World w = player.getWorld();
        int x = player.getLocation().getBlockX();
        int y = player.getLocation().getBlockY();
        int z = player.getLocation().getBlockZ();

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

    public static Vector3d getBL(Player player) {

        double x = player.getLocation().getX();
        double y = player.getLocation().getY();
        double z = player.getLocation().getZ();
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


        return new Vector3d(x, y, z);
    }

    public static Location<World> GetLocation(Player player, Quaterniond Qu, Vector3d distance) {
        final Vector3d dir = Qu.getDirection();
        final Vector3d diff = dir.add(distance);
        return player.getLocation().add(diff);
    }

    public static Location<World> GetLocation(Player player, Vector3d distance) {
        final Vector3d rotation = player.getRotation();
        return GetLocation(player, Quaterniond.fromAxesAnglesDeg(rotation.getX(), -rotation.getY(), rotation.getZ()), distance);
    }

    public static Vector3d rotateAroundAxisY(Vector3d v, double cos, double sin) {
        double x = v.getX() * cos + v.getZ() * sin;
        double z = v.getX() * -sin + v.getZ() * cos;
        return new Vector3d(x, v.getY(), z);
    }

    public static ItemStack Base64ToItemStack(byte[] base64) {
        return ItemStack.builder().fromContainer(Base64ToContainer(base64)).build();
    }

    public static Text getItemName(ItemStack itemStack) {
        return itemStack.get(Keys.DISPLAY_NAME).orElse(Text.of(itemStack.getTranslation().get()));
    }

    public static Text getItemName(ItemStackSnapshot itemStack) {
        return itemStack.get(Keys.DISPLAY_NAME).orElse(Text.of(itemStack.getTranslation().get()));
    }

    public static int getBackpackSize(User user) {
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_SIX))
            return 6;
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE))
            return 5;
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR))
            return 4;
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_THREE))
            return 3;
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_TWO))
            return 2;
        if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_ONE))
            return 1;
        return 1;
    }

    public static int getBackpackSize(User user, Integer size) {
        if (size == 0) {
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_SIX))
                return 6;
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE))
                return 5;
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR))
                return 4;
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_THREE))
                return 3;
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_TWO))
                return 2;
            if (user.hasPermission(BackpackPermission.COMMAND_BACKPACK_SIZE_ONE))
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
}
