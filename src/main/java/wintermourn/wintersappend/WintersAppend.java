package wintermourn.wintersappend;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wintermourn.wintersappend.attributes.PercentAttributes;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.block.entity.AppendBlockEntities;
import wintermourn.wintersappend.callbacks.ServerDataReloadFinished;
import wintermourn.wintersappend.callbacks.ServerPlayerJoined;
import wintermourn.wintersappend.category.AppendCreativeCategory;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.effects.ImmunizationEffects;
import wintermourn.wintersappend.effects.MajorEffects;
import wintermourn.wintersappend.effects.MinorEffects;
import wintermourn.wintersappend.effects.VanillaEffects;
import wintermourn.wintersappend.item.AppendItems;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.recipe.AppendRecipes;
import wintermourn.wintersappend.screen.AppendScreenHandlers;
import wintermourn.wintersappend.config.AppendServerConfig;
import wintermourn.wintersappend.config.AppendServerUserConfig;
import wintermourn.wintersappend.world.gen.AppendWorldGen;

import java.util.Random;

public class WintersAppend implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("winters_append");

	public static final String MOD_ID = "winters_append";
	public static final Random RANDOM = new Random();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Hello Fabric world!");

		AppendServerUserConfig.HANDLER.load();
		AppendServerConfig.CopyUserConfig();
		AppendFuelsConfig.LoadConfig();
		ServerLifecycleEvents.END_DATA_PACK_RELOAD.register(new ServerDataReloadFinished());
		ServerPlayConnectionEvents.JOIN.register(new ServerPlayerJoined());

		PercentAttributes.Register();

		MajorEffects.Register();
		MinorEffects.Register();
		ImmunizationEffects.Register();
		VanillaEffects.Register();

		AppendItems.Register();
		AppendBlocks.Register();

		AppendRecipes.Register();

		AppendCreativeCategory.Register();

		AppendBlockEntities.Register();

		AppendScreenHandlers.Register();

		AppendMessages.RegisterC2SPackets();


		AppendWorldGen.RegisterWorldGen();
	}
}