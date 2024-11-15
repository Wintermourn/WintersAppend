package wintermourn.wintersappend.callbacks;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.resource.LifecycledResourceManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.config.AppendServerConfig;
import wintermourn.wintersappend.config.AppendServerUserConfig;

import java.util.Objects;

public class ServerDataReloadFinished implements ServerLifecycleEvents.EndDataPackReload {
    @Override
    public void endDataPackReload(MinecraftServer server, LifecycledResourceManager resourceManager, boolean success) {
        if (success)
        {
            WintersAppend.LOGGER.info("Reloading mod server config...");
            AppendServerUserConfig.HANDLER.load();
            AppendServerConfig.CopyUserConfig();
            WintersAppend.LOGGER.info("Reloading mod fuel config...");
            AppendFuelsConfig.LoadConfig();

            PacketByteBuf buf = AppendServerConfig.CreateConfigBuf();
            PacketByteBuf fuels = AppendFuelsConfig.CreateFuelsPacket();
            for (ServerPlayerEntity player : Objects.requireNonNull(server.getPlayerManager().getPlayerList())) {
                ServerPlayNetworking.send(
                        player,
                        AppendMessages.RECEIVE_SERVER_CONFIG,
                        buf
                );
                ServerPlayNetworking.send(
                    player,
                    AppendMessages.SERVER_FUELS,
                    fuels
                );
            }
        }
    }
}
