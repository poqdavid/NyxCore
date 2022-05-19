package io.github.poqdavid.nyx.nyxcore;

import io.github.poqdavid.nyx.nyxcore.Permissions.BackpackPermission;
import io.github.poqdavid.nyx.nyxcore.Permissions.MarketPermission;
import io.github.poqdavid.nyx.nyxcore.Permissions.ToolsPermission;
import io.github.poqdavid.nyx.nyxcore.Utils.CText;
import io.github.poqdavid.nyx.nyxcore.Utils.NCLogger;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxMarket.NMSettings;
import io.github.poqdavid.nyx.nyxcore.Utils.Setting.NyxTools.NTSettings;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ModContainer;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.server.permission.DefaultPermissionLevel;
import net.minecraftforge.server.permission.PermissionAPI;

import javax.annotation.Nonnull;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.poqdavid.nyx.nyxcore.Utils.CoreTools.getForgeMod;

@Mod(
        modid = "@id@",
        name = "@name@",
        version = "@version@"
)
public class NyxCore {
    public static final String MODID = "nyxcore";

    @Mod.Instance(value = MODID)
    public static NyxCore instance;

    public static NyxCore nyxcore;
    private Path configDirPath;
    private Path configFullPath;

    private Path dataDir;
    private Path backpackDir;
    private Path toolsDir;
    private Path backpacksDir;
    private Path effectDir;
    private Path marketDir;

    private ModContainer modContainer;
    public NCLogger logger;

    public Path recordsDir;
   //public NTSettings nytSettings;
    //public NMSettings nmSettings;

    //private Game game;

    public NyxCore(){
        nyxcore = this;
        modContainer = getForgeMod(MODID);

        this.dataDir = Paths.get("");//Sponge.getGame().getSavesDirectory().resolve(this.getPluginContainer().getId());

        this.logger = new NCLogger();
        //this.nytSettings = new NTSettings();
        //this.nmSettings = new NMSettings();
        this.configDirPath = Paths.get("test");//path.resolve("Nyx");
        this.configFullPath = Paths.get(this.getConfigPath().toString(), "config.json");
        this.backpackDir = Paths.get(this.getConfigPath().toString(), "NyxBackpack");
        this.toolsDir = Paths.get(this.getConfigPath().toString(), "NyxTools");
        this.backpacksDir = Paths.get(this.backpackDir.toString(), "backpacks");
        this.effectDir = Paths.get(this.getConfigPath().toString(), "NyxEffect");
        this.marketDir = Paths.get(this.getConfigPath().toString(), "NyxMarket");

        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "@name@") + CText.get(CText.Colors.YELLOW, 0, " v" + this.getVersion()));
        this.logger.info("Starting...");
        this.logger.info(" ");

    }

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        this.logger.info(" ");
        this.logger.info(CText.get(CText.Colors.MAGENTA, 0, "@name@") + CText.get(CText.Colors.YELLOW, 0, " v" + this.getVersion()));
        this.logger.info("Initializing...");
        this.logger.info(" ");
        nyxcore = this;
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
    public Path getEffectPath() {
        return this.effectDir;
    }

    @Nonnull
    public Path getMarketPath() {
        return this.marketDir;
    }

    @Nonnull
    public ModContainer getModContainer() {
        return this.modContainer;
    }

    @Nonnull
    public String getVersion() {
       if (this.getModContainer().getVersion() != null) {
            return this.getModContainer().getVersion();
        } else {
            return "@version@";
        }
    }

/*    @Nonnull
    public NTSettings getToolsSettings() {
        return this.nytSettings;
    }

    @Nonnull
    public NMSettings getMarketSettings() {
        return this.nmSettings;
    }*/

    @Nonnull
    public NCLogger getLogger(String name) {
        if (name == null || name.isEmpty()) {
            return this.logger;
        } else {
            return new NCLogger(this.logger.LoggerName + CText.get(CText.Colors.YELLOW, 0, " - ") + CText.get(CText.Colors.BLUE, 1, name));
        }
    }

/*    @Nonnull
    public Game getGame() {
        return game;
    }

    @Inject
    public void setGame(Game game) {
        this.game = game;
    }*/

    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance("@id@")
    public static NyxCore INSTANCE;

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_HELP, DefaultPermissionLevel.NONE, "Allows the use of /bp help");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_MAIN, DefaultPermissionLevel.NONE, "Allows the use of /backpack, /bp");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACKLOCK, DefaultPermissionLevel.NONE, "Allows the use of /backpacklock, /bplock");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_ADMIN_READ, DefaultPermissionLevel.NONE, "Allows to read content of other backpacks");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_ADMIN_MODIFY, DefaultPermissionLevel.NONE, "Allows to modify other backpacks");

        //Backpack sizes
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_ONE, DefaultPermissionLevel.NONE, "Sets users backpack size to 1 row");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_TWO, DefaultPermissionLevel.NONE, "Sets users backpack size to 2 row");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_THREE, DefaultPermissionLevel.NONE, "Sets users backpack size to 3 row");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_FOUR, DefaultPermissionLevel.NONE, "Sets users backpack size to 4 row");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_FIVE, DefaultPermissionLevel.NONE, "Sets users backpack size to 5 row");
        PermissionAPI.registerNode(BackpackPermission.COMMAND_BACKPACK_SIZE_SIX, DefaultPermissionLevel.NONE, "Sets users backpack size to 6 row");

        //Backpack commands
        PermissionAPI.registerNode(ToolsPermission.COMMAND_MAIN, DefaultPermissionLevel.NONE, "Allows the use of /nyxtools, /nyt");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_HELP, DefaultPermissionLevel.NONE, "Allows the use of help command");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ANVIL, DefaultPermissionLevel.NONE, "Allows the use of /anvil, /av");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE, DefaultPermissionLevel.NONE, "Allows the use of /enchantingtable, /et");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENDERCHEST, DefaultPermissionLevel.NONE, "Allows the use of /enderchest, /ec");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_WORKBENCH, DefaultPermissionLevel.NONE, "Allows the use of /workbench, /wb");

        //Enchantment powers
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_0, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 0");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_1, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 1");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_2, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 2");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_3, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 3");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_4, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 4");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_5, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 5");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_6, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 6");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_7, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 7");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_8, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 8");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_9, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 9");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_10, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 10");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_11, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 11");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_12, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 12");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_13, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 13");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_14, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 14");
        PermissionAPI.registerNode(ToolsPermission.COMMAND_ENCHANTINGTABLE_POWER_15, DefaultPermissionLevel.NONE, "Sets enchanting table's power to 15");

        //Market
        PermissionAPI.registerNode(MarketPermission.COMMAND_HELP, DefaultPermissionLevel.NONE, "Allows the use of help command");
        PermissionAPI.registerNode(MarketPermission.COMMAND_MAIN, DefaultPermissionLevel.NONE, "Allows the use of /sm, /NyxMarket");
        PermissionAPI.registerNode(MarketPermission.COMMAND_SELL, DefaultPermissionLevel.NONE, "Allows the use of /market sell");
        PermissionAPI.registerNode(MarketPermission.COMMAND_BUY, DefaultPermissionLevel.NONE, "Allows the use of /market buy");
        PermissionAPI.registerNode(MarketPermission.COMMAND_HISTORY, DefaultPermissionLevel.NONE, "Allows the use of /market history");
        PermissionAPI.registerNode(MarketPermission.COMMAND_MAIL, DefaultPermissionLevel.NONE, "Allows the use of /market mail");
        PermissionAPI.registerNode(MarketPermission.COMMAND_PRICECHECK, DefaultPermissionLevel.NONE, "Allows the use of /market price");
        PermissionAPI.registerNode(MarketPermission.COMMAND_PRICELIMIT, DefaultPermissionLevel.NONE, "Allows the use of /market pricelimit");
        PermissionAPI.registerNode(MarketPermission.COMMAND_RELOAD, DefaultPermissionLevel.NONE, "Allows the use of /market reload");
        PermissionAPI.registerNode(MarketPermission.COMMAND_SEND, DefaultPermissionLevel.NONE, "Allows the use of /market send");

        PermissionAPI.registerNode(MarketPermission.ADMIN_CANCEL, DefaultPermissionLevel.NONE, "Allows the the admin to cancel market items");
        PermissionAPI.registerNode(MarketPermission.COMMAND_SETTING, DefaultPermissionLevel.NONE, "Allows the the admin to change settings");
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder("@id@")
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder("@id@")
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {
        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
