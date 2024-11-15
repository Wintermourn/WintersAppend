package wintermourn.wintersappend.callbacks;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import wintermourn.wintersappend.config.AppendFuelsConfig;
import wintermourn.wintersappend.config.AppendServerConfig;
import wintermourn.wintersappend.networking.AppendMessages;

public class ServerPlayerJoined implements ServerPlayConnectionEvents.Join {
    @Override
    public void onPlayReady(ServerPlayNetworkHandler handler, PacketSender sender, MinecraftServer server) {
        sender.sendPacket(AppendMessages.SERVER_FUELS, AppendFuelsConfig.CreateFuelsPacket());
        sender.sendPacket(AppendMessages.RECEIVE_SERVER_CONFIG, AppendServerConfig.CreateConfigBuf());
    }
}
