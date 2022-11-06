package org.minefortress;


import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.EntitySleepEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.GameMode;
import org.minefortress.commands.CommandsManager;
import org.minefortress.fortress.resources.gui.craft.FortressCraftingScreenHandler;
import org.minefortress.fortress.resources.gui.smelt.FortressFurnaceScreenHandler;
import org.minefortress.network.*;
import org.minefortress.network.helpers.FortressChannelNames;
import org.minefortress.network.helpers.FortressServerNetworkHelper;
import org.minefortress.registries.FortressBlocks;
import org.minefortress.registries.FortressEntities;
import org.minefortress.registries.FortressItems;
import org.minefortress.utils.ModUtils;

public class MineFortressMod implements ModInitializer {

    public static final GameMode FORTRESS = ClassTinkerers.getEnum(GameMode.class, "FORTRESS");
    public static final String BLUEPRINTS_FOLDER_NAME = "minefortress-blueprints";
    public static final String BLUEPRINTS_EXTENSION = ".blueprints";
    public static final String MOD_ID = "minefortress";

    private static final Identifier FORTRESS_CRAFTING_SCREEN_HANDLER_ID = new Identifier(MOD_ID, "fortress_crafting_handler");
    private static final Identifier FORTRESS_FURNACE_SCREEN_HANDLER_ID = new Identifier(MOD_ID, "fortress_furnace_handler");
    public static final ScreenHandlerType<FortressCraftingScreenHandler> FORTRESS_CRAFTING_SCREEN_HANDLER = ScreenHandlerRegistry.registerSimple(FORTRESS_CRAFTING_SCREEN_HANDLER_ID, FortressCraftingScreenHandler::new);
    public static final ScreenHandlerType<FortressFurnaceScreenHandler> FORTRESS_FURNACE_SCREEN_HANDLER =  ScreenHandlerRegistry.registerSimple(FORTRESS_FURNACE_SCREEN_HANDLER_ID, FortressFurnaceScreenHandler::new);

    @Override
    public void onInitialize() {
        FortressBlocks.register();
        FortressEntities.register();
        FortressItems.register();

        CommandsManager.registerCommands();
        registerEvents();

        // networking
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.NEW_SELECTION_TASK, ServerboundSimpleSelectionTaskPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.NEW_BLUEPRINT_TASK, ServerboundBlueprintTaskPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.CANCEL_TASK, ServerboundCancelTaskPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_SET_CENTER, ServerboundFortressCenterSetPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_EDIT_BLUEPRINT, ServerboundEditBlueprintPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_SAVE_EDIT_BLUEPRINT, ServerboundFinishEditBlueprintPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_CUT_TREES_TASK, ServerboundCutTreesTaskPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_ROADS_TASK, ServerboundRoadsTaskPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_PROFESSION_STATE_CHANGE, ServerboundChangeProfessionStatePacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_SET_GAMEMODE, ServerboundSetGamemodePacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_OPEN_CRAFTING_TABLE, ServerboundOpenCraftingScreenPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.SCROLL_CURRENT_SCREEN, ServerboundScrollCurrentScreenPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_SLEEP, ServerboundSleepPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_CHANGE_MAX_COLONISTS_COUNT, ServerboundChangeMaxColonistsCountPacket::new);
        FortressServerNetworkHelper.registerReceiver(FortressChannelNames.FORTRESS_BLUEPRINTS_IMPORT_EXPORT, ServerboundBlueprintsImportExportPacket::new);
    }

    public static void registerEvents() {
        EntitySleepEvents.ALLOW_BED.register((entity, sleepingPos, state, vanillaResult) -> {
            if(ModUtils.isFortressGamemode(entity)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });

        EntitySleepEvents.MODIFY_SLEEPING_DIRECTION.register((entity, pos, dir) -> {
            if(ModUtils.isFortressGamemode(entity)) {
                final var rotationVector = entity.getRotationVector();
                return Direction.getFacing(rotationVector.x, rotationVector.y, rotationVector.z);
            }
            return dir;
        });

        EntitySleepEvents.ALLOW_NEARBY_MONSTERS.register((player, pos, vanilla) -> {
            if(ModUtils.isFortressGamemode(player)) {
                return ActionResult.SUCCESS;
            }
            return ActionResult.PASS;
        });
    }

}
