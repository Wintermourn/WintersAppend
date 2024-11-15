package wintermourn.wintersappend.client;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import wintermourn.wintersappend.client.configGroups.TonicConfig;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.config.AppendClientConfig;
import wintermourn.wintersappend.config.AppendServerConfig;
import wintermourn.wintersappend.config.AppendServerUserConfig;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class AppendConfigUI {
    public static Text LABEL_SERVER = Text.translatable("config.winters_append.label_server").formatted(Formatting.AQUA, Formatting.BOLD, Formatting.ITALIC);
    public static Text LABEL_CLIENT = Text.translatable("config.winters_append.label_client").formatted(Formatting.GOLD, Formatting.BOLD, Formatting.ITALIC);

    public static Screen CreateScreen(Screen parent)
    {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable("winters_append.title"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.translatable("config.winters_append.tonics.title"))
                        .tooltip(Text.translatable("config.winters_append.tonics.desc"))
                        .groups(List.of(TonicConfig.Tooltip, TonicConfig.Brewing, TonicConfig.Effects))
                        .build()
                )
                .save(()-> {
                    AppendServerUserConfig.HANDLER.save();

                    if (MinecraftClient.getInstance().isConnectedToLocalServer())
                    {
                        AppendServerConfig.CopyUserConfig();

                        PacketByteBuf buf = AppendServerConfig.CreateConfigBuf();
                        for (ServerPlayerEntity player : Objects.requireNonNull(MinecraftClient.getInstance().getServer()).getPlayerManager().getPlayerList()) {
                            ServerPlayNetworking.send(
                                    player,
                                    AppendMessages.RECEIVE_SERVER_CONFIG,
                                    buf);
                        }
                    }

                    AppendClientConfig.HANDLER.save();
                })
                .build()
                .generateScreen(parent);
    }

    public static Controller<Integer> CreateIntSlider(Option<Integer> opt, int min, int max, int step, ValueFormatter<Integer> formatter)
    {
        return IntegerSliderControllerBuilder.create(opt).range(min, max).step(step)
                .formatValue(formatter).build();
    }

    public static Controller<Float> CreateFloatSlider(Option<Float> opt, float min, float max, float step, ValueFormatter<Float> formatter)
    {
        return FloatSliderControllerBuilder.create(opt).range(min, max).step(step)
                .formatValue(formatter).build();
    }


    public static <T> Option<T> CreateOption(String name, T def, Supplier<T> supplier, Consumer<T> consumer, Function<Option<T>, Controller<T>> controller)
    {
        return Option.<T>createBuilder()
                .name(Text.translatable(name +".name"))
                .description(OptionDescription.of(Text.translatable(name +".desc")))
                .binding(def, supplier, consumer)
                .customController(controller)
                .build();
    }

}
