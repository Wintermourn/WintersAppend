package wintermourn.wintersappend.mixin.common;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.config.AppendServerConfig;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
    @Inject(method = "onPlayerConnect", at = @At("HEAD"))
    void buh(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        if (connection.isLocal()) return;

        WintersAppend.LOGGER.info("Sending config to {}", player.getName().getString());
        ServerPlayNetworking.send(
            player,
            AppendMessages.RECEIVE_SERVER_CONFIG,
            AppendServerConfig.CreateConfigBuf());
    }
}
