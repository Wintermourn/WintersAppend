package wintermourn.wintersappend.networking.S2C;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import wintermourn.wintersappend.config.AppendFuelsConfig;

public class ServerFuelsPacket {
    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        AppendFuelsConfig.TransferFuelsFromServer(buf);
    }
}
