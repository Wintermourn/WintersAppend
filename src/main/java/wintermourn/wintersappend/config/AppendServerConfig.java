package wintermourn.wintersappend.config;

import dev.isxander.yacl3.config.v2.api.SerialEntry;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;

@SuppressWarnings("FinalStaticMethod")
public class AppendServerConfig {

    @SerialEntry(comment = "Multiplier on how much more heat fuel must be spent after running out of Purity.")
    public static float impureFuelPenalty = 2f;
    @SerialEntry(comment = "How much longer brewing should take if the Tonic Stand has run out of Purity.")
    public static float impureTimePenalty = 2.5f;

    @SerialEntry(comment = "Force commitment by actively spending fuel when a valid recipe is in the stand.")
    public static boolean spendFuelDuringBrew = false;
    @SerialEntry(comment = "How many effects a standard tonic can have, max.")
    public static int defaultTonicEffects = 3;
    @SerialEntry(comment = "How long the standard tonic should last in ticks (20 = 1 second).")
    public static int defaultTonicLength = 24000;
    @SerialEntry(comment = "How long the standard tonic should last at minimum (20 = 1 second).")
    public static int minimumTonicLength = 1200;

    @SuppressWarnings({"SillyAssignment", "DataFlowIssue"})
    public static final void CopyUserConfig()
    {
        impureFuelPenalty = AppendServerUserConfig.impureFuelPenalty;
        impureTimePenalty = AppendServerUserConfig.impureTimePenalty;

        spendFuelDuringBrew = AppendServerUserConfig.spendFuelDuringBrew;
        defaultTonicEffects = AppendServerUserConfig.defaultTonicEffects;
        defaultTonicLength = AppendServerUserConfig.defaultTonicLength;
        minimumTonicLength = AppendServerUserConfig.minimumTonicLength;
    }

    public static final PacketByteBuf CreateConfigBuf()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeFloat(impureFuelPenalty)
                .writeFloat(impureTimePenalty)

                .writeBoolean(spendFuelDuringBrew)
                .writeByte(defaultTonicEffects)
                .writeInt(defaultTonicLength)
                .writeInt(minimumTonicLength);
        return buf;
    }

}
