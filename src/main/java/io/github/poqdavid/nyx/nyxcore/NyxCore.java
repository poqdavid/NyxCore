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

package io.github.poqdavid.nyx.nyxcore;

import com.google.inject.Inject;
import io.github.poqdavid.nyx.nyxcore.Permissions.BackpackPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.CText;
import io.github.poqdavid.nyx.nyxcore.Utils.NCLogger;
import org.bstats.sponge.Metrics2;
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
import org.spongepowered.api.service.permission.PermissionDescription;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.text.Text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.nio.file.Path;
import java.nio.file.Paths;

@Plugin(id = PluginData.id, name = PluginData.name, version = PluginData.version, description = PluginData.description, url = PluginData.url, authors = {PluginData.author1})
public class NyxCore {

    public static NyxCore nyxcore;
    private final Path configdirpath;
    private final Path configfullpath;
    private final Path dataDir;
    private final Path backpackDir;
    private final Path toolsDir;
    private final Path backpacksDir;
    private final PluginContainer pluginContainer;
    private final Metrics2 metrics;
    public NCLogger logger;
    public PermissionService permservice;
    public PermissionDescription.Builder permdescbuilder;
    public Path recordsDir;
    @Inject
    private Game game;
    private CommandManager cmdManager;

    @Inject
    public NyxCore(Metrics2.Factory metricsFactory, @ConfigDir(sharedRoot = true) Path path, Logger logger, PluginContainer container) {
        nyxcore = this;
        this.dataDir = Sponge.getGame().getSavesDirectory().resolve(PluginData.id);
        this.pluginContainer = container;
        this.logger = new NCLogger();
        this.configdirpath = path.resolve(PluginData.shortName);
        this.configfullpath = Paths.get(this.getConfigPath().toString(), "config.json");
        this.backpackDir = Paths.get(this.getConfigPath().toString(), "NyxBackpack");
        this.toolsDir = Paths.get(this.getConfigPath().toString(), "NyxTools");
        this.backpacksDir = Paths.get(this.backpackDir.toString(), "backpacks");

        int pluginId = 12556;
        metrics = metricsFactory.make(pluginId);
    }


    @Nonnull
    public static NyxCore getInstance() {
        return nyxcore;
    }

    @Nonnull
    public Path getConfigPath() {
        return this.configdirpath;
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

    }

    @Listener
    public void onGameInit(@Nullable final GameInitializationEvent event) {
        if (Sponge.getServiceManager().getRegistration(PermissionService.class).get().getPlugin().getId().equalsIgnoreCase("sponge")) {
            this.logger.error("Unable to initialize plugin. VirtualTool requires a PermissionService like  LuckPerms, PEX, PermissionsManager.");
            return;
        }

        if (this.permdescbuilder != null) {
            this.permdescbuilder = this.permservice.newDescriptionBuilder(this.getPluginContainer());

            //Backpack commands
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_HELP)
                    .description(Text.of("Allows the use of /vt help"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_MAIN)
                    .description(Text.of("Allows the use of /backpack, /bp"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACKLOCK)
                    .description(Text.of("Allows the use of /backpacklock, /bplock"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_ADMIN_READ)
                    .description(Text.of("Allows to read content of other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_ADMIN_MODIFY)
                    .description(Text.of("Allows to modify other backpacks"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();

            //Backpack sizes
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_ONE)
                    .description(Text.of("Sets users backpack size to 1 row"))
                    .assign(PermissionDescription.ROLE_USER, true)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_TWO)
                    .description(Text.of("Sets users backpack size to 2 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_THREE)
                    .description(Text.of("Sets users backpack size to 3 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR)
                    .description(Text.of("Sets users backpack size to 4 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE)
                    .description(Text.of("Sets users backpack size to 5 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, false)
                    .assign(PermissionDescription.ROLE_ADMIN, false)
                    .register();
            this.permdescbuilder
                    .id(BackpackPermission.COMMAND_BACKPACK_SIZE_SIX)
                    .description(Text.of("Sets users backpack size to 6 row"))
                    .assign(PermissionDescription.ROLE_USER, false)
                    .assign(PermissionDescription.ROLE_STAFF, true)
                    .assign(PermissionDescription.ROLE_ADMIN, true)
                    .register();

        }

        //this.settings.Load(this.configfullpath, this);

        this.logger.info("Plugin Initialized successfully!");
    }

}
