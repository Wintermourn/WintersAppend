package wintermourn.wintersappend.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.networking.C2S.TonicStandModePacket;
import wintermourn.wintersappend.networking.S2C.BrewingFinishedPacket;
import wintermourn.wintersappend.networking.S2C.ServerConfigPacket;
import wintermourn.wintersappend.networking.S2C.ServerFuelsPacket;

public class AppendMessages {
    public static final Identifier TOGGLE_BREWING_MODE = new Identifier(WintersAppend.MOD_ID, "tonic/toggle_mode");

    public static final Identifier BREWING_FINISHED = new Identifier(WintersAppend.MOD_ID, "tonic/brewing_finished");
    public static final Identifier RECEIVE_SERVER_CONFIG = new Identifier(WintersAppend.MOD_ID, "server_config");
    public static final Identifier SERVER_FUELS = new Identifier(WintersAppend.MOD_ID, "server_fuels");

    public static void RegisterC2SPackets()
    {
        ServerPlayNetworking.registerGlobalReceiver(TOGGLE_BREWING_MODE, TonicStandModePacket::onChannelRegister);
    }

    public static void RegisterS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(BREWING_FINISHED, BrewingFinishedPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(RECEIVE_SERVER_CONFIG, ServerConfigPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(SERVER_FUELS, ServerFuelsPacket::receive);
    }
}
