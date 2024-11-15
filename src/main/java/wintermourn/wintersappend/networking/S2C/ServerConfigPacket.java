package wintermourn.wintersappend.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import wintermourn.wintersappend.config.AppendServerConfig;

public class ServerConfigPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        AppendServerConfig.impureFuelPenalty = buf.readFloat();
        AppendServerConfig.impureTimePenalty = buf.readFloat();

        AppendServerConfig.spendFuelDuringBrew = buf.readBoolean();
        AppendServerConfig.defaultTonicEffects = buf.readByte();
        AppendServerConfig.defaultTonicLength = buf.readInt();
        AppendServerConfig.minimumTonicLength = buf.readInt();
    }
}
