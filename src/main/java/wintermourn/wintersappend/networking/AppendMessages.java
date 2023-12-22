package wintermourn.wintersappend.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.networking.S2C.BrewingFinishedPacket;

public class AppendMessages {
    public static final Identifier BREWING_FINISHED = new Identifier(WintersAppend.MOD_ID, "tonic/brewing_finished");

    public static void RegisterC2SPackets()
    {

    }

    public static void RegisterS2CPackets()
    {
        ClientPlayNetworking.registerGlobalReceiver(BREWING_FINISHED, BrewingFinishedPacket::receive);
    }
}
