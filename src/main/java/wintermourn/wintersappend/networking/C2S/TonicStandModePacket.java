package wintermourn.wintersappend.networking.C2S;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;

public class TonicStandModePacket {
    public static void onChannelRegister(
            MinecraftServer minecraftServer,
            ServerPlayerEntity serverPlayerEntity,
            ServerPlayNetworkHandler serverPlayNetworkHandler,
            PacketByteBuf packetByteBuf, PacketSender packetSender) {
        WintersAppend.LOGGER.info("guh");

        BlockPos position = packetByteBuf.readBlockPos();

        BlockEntity ent = serverPlayerEntity.getServerWorld().getWorldChunk(position).getBlockEntity(position);

        if (!(ent instanceof TonicStandBlockEntity stand)) return;

        stand.setBrewingMode(stand.getBrewingMode() == 1 ? 0 : 1);
    }
}
