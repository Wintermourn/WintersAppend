package wintermourn.wintersappend.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.SerialEntry;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

public class AppendClientConfig {
    public static ConfigClassHandler<AppendClientConfig> HANDLER = ConfigClassHandler.createBuilder(AppendClientConfig.class)
            .id(new Identifier(WintersAppend.MOD_ID, "client"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("wintermourn/winters_append/client.json5"))
                    .setJson5(true)
                    .build()
            ).build();

    @SerialEntry(comment = "Determines whether the duration penalty should be shown in a Tonic's tooltip, if applicable.")
    public static boolean displayPenalty = false;

}
