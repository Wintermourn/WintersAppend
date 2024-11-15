package wintermourn.wintersappend.config;

import dev.isxander.yacl3.config.v2.api.ConfigClassHandler;
import dev.isxander.yacl3.config.v2.api.serializer.GsonConfigSerializerBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

public class AppendServerUserConfig extends AppendServerConfig {
    public static ConfigClassHandler<AppendServerConfig> HANDLER = ConfigClassHandler.createBuilder(AppendServerConfig.class)
            .id(new Identifier(WintersAppend.MOD_ID, "server"))
            .serializer(config -> GsonConfigSerializerBuilder.create(config)
                    .setPath(FabricLoader.getInstance().getConfigDir().resolve("wintermourn/winters_append/server.json5"))
                    .setJson5(true)
                    .build()
            ).build();
}
