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

package io.github.poqdavid.nyx.nyxcore;

import com.google.inject.Inject;
import io.github.poqdavid.nyx.nyxcore.Permissions.BackpackPermission;
import io.github.poqdavid.nyx.nyxcore.Permissions.ToolsPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.CText;
import io.github.poqdavid.nyx.nyxcore.Utils.NCLogger;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools.Settings;
import org.bstats.sponge.Metrics;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandManager;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.ProviderRegistration;
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Plugin(id = PluginData.id, name = PluginData.name, version = PluginData.version, description = PluginData.description, url = PluginData.url, authors = {PluginData.author1})
public class NyxCore {

    public static NyxCore nyxcore;
    private final Path configDirPath;
    private final Path configFullPath;
    private final Path dataDir;
    private final Path backpackDir;
    private final Path toolsDir;
    private final Path backpacksDir;
    private final PluginContainer pluginContainer;
    private final Metrics metrics;
    public NCLogger logger;
    public PermissionService permService;
    public PermissionDescription.Builder permDescBuilder;
    public Path recordsDir;
    public Settings nytSettings;
    @Inject
    private Game game;
    private CommandManager cmdManager;

    @Inject
    public NyxCore(Metrics.Factory metricsFactory, @ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        nyxcore = this;
        this.dataDir = Sponge.getGame().getSavesDirectory().resolve(PluginData.id);
        this.pluginContainer = container;
        this.logger = new NCLogger();
        this.nytSettings = new Settings();
        this.configDirPath = path.resolve(PluginData.shortName);
        this.configFullPath = Paths.get(this.getConfigPath().toString(), "config.json");
        this.backpackDir = Paths.get(this.getConfigPath().toString(), "NyxBackpack");
        this.toolsDir = Paths.get(this.getConfigPath().toString(), "NyxTools");
        this.backpacksDir = Paths.get(this.backpackDir.toString(), "backpacks");


        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "NyxCore") + CText.get(CText.Colors.YELLOW, 0, " v" + PluginData.version));
        this.logger.info("Starting...");
        this.logger.info(" ");

        metrics = metricsFactory.make(12556);
    }


    @Nonnull
    public static NyxCore getInstance() {
        return nyxcore;
    }

    @Nonnull
    public Path getConfigPath() {
        return this.configDirPath;
    }

    @Nonnull
    public Path getBackpackPath() {
        return this.backpackDir;
    }

    @Nonnull
    public Path getToolsPath() {
        return this.toolsDir;
    }

    @Nonnull
    public Path getBackpacksPath() {
        return this.backpacksDir;
    }

    @Nonnull
    public PluginContainer getPluginContainer() {
        return this.pluginContainer;
    }

    @Nonnull
    public String getVersion() {
        return PluginData.version;
    }

    @Nonnull
    public Settings getToolsSettings() {
        return this.nytSettings;
    }

    @Nonnull
    public NCLogger getLogger(String name) {
        if (name == null || name.isEmpty()) {
            return this.logger;
        } else {
            return new NCLogger(this.logger.LoggerName + CText.get(CText.Colors.YELLOW, 0, " - ") + CText.get(CText.Colors.BLUE, 1, name));
        }
    }

    @Nonnull
    public Game getGame() {
        return game;
    }

    @Inject
    public void setGame(Game game) {
        this.game = game;
    }

    @Listener
    public void onGamePreInit(@Nullable final GamePreInitializationEvent event) {
        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "NyxCore") + CText.get(CText.Colors.YELLOW, 0, " v" + PluginData.version));
        this.logger.info("Initializing...");
        this.logger.info(" ");
        nyxcore = this;
    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        Optional<ProviderRegistration<PermissionService>> pServiceReg = Sponge.getServiceManager().getRegistration(PermissionService.class);
        if (pServiceReg.isPresent()) {
            if (pServiceReg.get().getPlugin().getId().equalsIgnoreCase("sponge")) {
                this.logger.error("Unable to initialize plugin. NyxCore requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
                return;
            }
        } else {
            this.logger.error("Unable to initialize plugin. NyxCore requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
            return;
        }


        if (this.permDescBuilder != null) {
            this.permDescBuilder = this.permService.newDescriptionBuilder(this.getPluginContainer());

            //Backpack commands
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_HELP)
                    .description(Text.of("Allows the use of /bp help"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_MAIN)
                    .description(Text.of("Allows the use of /backpack, /bp"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACKLOCK)
                    .description(Text.of("Allows the use of /backpacklock, /bplock"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_ADMIN_READ)
                    .description(Text.of("Allows to read content of other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_ADMIN_MODIFY)
                    .description(Text.of("Allows to modify other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            //Backpack sizes
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_ONE)
                    .description(Text.of("Sets users backpack size to 1 row"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_TWO)
                    .description(Text.of("Sets users backpack size to 2 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_THREE)
                    .description(Text.of("Sets users backpack size to 3 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR)
                    .description(Text.of("Sets users backpack size to 4 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE)
                    .description(Text.of("Sets users backpack size to 5 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_SIX)
                    .description(Text.of("Sets users backpack size to 6 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            //Backpack commands
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_MAIN)
                    .description(Text.of("Allows the use of /nyxtools, /nyt"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_HELP)
                    .description(Text.of("Allows the use of help command"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ANVIL)
                    .description(Text.of("Allows the use of /anvil, /av"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE)
                    .description(Text.of("Allows the use of /enchantingtable, /et"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENDERCHEST)
                    .description(Text.of("Allows the use of /enderchest, /ec"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_WORKBENCH)
                    .description(Text.of("Allows the use of /workbench, /wb"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

            //Enchantment powers
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_0)
                    .description(Text.of("Sets enchanting table's power to 0"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_1)
                    .description(Text.of("Sets enchanting table's power to 1"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_2)
                    .description(Text.of("Sets enchanting table's power to 2"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_3)
                    .description(Text.of("Sets enchanting table's power to 3"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_4)
                    .description(Text.of("Sets enchanting table's power to 4"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_5)
                    .description(Text.of("Sets enchanting table's power to 5"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_6)
                    .description(Text.of("Sets enchanting table's power to 6"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_7)
                    .description(Text.of("Sets enchanting table's power to 7"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_8)
                    .description(Text.of("Sets enchanting table's power to 8"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_9)
                    .description(Text.of("Sets enchanting table's power to 9"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_10)
                    .description(Text.of("Sets enchanting table's power to 10"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_11)
                    .description(Text.of("Sets enchanting table's power to 11"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_12)
                    .description(Text.of("Sets enchanting table's power to 12"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_13)
                    .description(Text.of("Sets enchanting table's power to 13"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_14)
                    .description(Text.of("Sets enchanting table's power to 14"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permDescBuilder
                    .id(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_15)
                    .description(Text.of("Sets enchanting table's power to 15"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
        }

        this.logger.info("Plugin Initialized successfully!");
    }

}
