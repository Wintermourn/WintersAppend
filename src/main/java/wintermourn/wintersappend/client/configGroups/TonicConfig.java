package wintermourn.wintersappend.client.configGroups;

import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.StringHelper;
import wintermourn.wintersappend.config.AppendClientConfig;
import wintermourn.wintersappend.config.AppendServerConfig;

import java.util.List;

import static wintermourn.wintersappend.client.AppendConfigUI.*;

public class TonicConfig {
    public static OptionGroup Tooltip = OptionGroup.createBuilder()
            .name(Text.translatable("group.winters_append.tonics.tooltip"))
            .description(OptionDescription.of(
                    LABEL_CLIENT,
                    Text.translatable("group.winters_append.tonics.tooltip.desc")
            ))
            .options(List.of(
                    CreateOption(
                            "option.winters_append.tonics.show_duration_penalty",
                            false,
                            ()-> AppendClientConfig.displayPenalty,
                            (v)-> AppendClientConfig.displayPenalty = v,
                            opt -> TickBoxControllerBuilder.create(opt).build()
                    )
            ))
            .build();

    public static OptionGroup Brewing = OptionGroup.createBuilder()
            .name(Text.translatable("group.winters_append.tonics.brewing"))
            .description(OptionDescription.of(
                    LABEL_SERVER,
                    Text.translatable("group.winters_append.tonics.brewing.desc")
            ))
            .options(List.of(
                    CreateOption(
                            "option.winters_append.tonics.force_commitment",
                            false,
                            ()-> AppendServerConfig.spendFuelDuringBrew,
                            (v)-> AppendServerConfig.spendFuelDuringBrew = v,
                            opt -> TickBoxControllerBuilder.create(opt).build()
                    ),
                    CreateOption(
                            "option.winters_append.tonics.impure_fuel_penalty",
                            2f,
                            ()-> AppendServerConfig.impureFuelPenalty,
                            (v)-> AppendServerConfig.impureFuelPenalty = v,
                            opt -> CreateFloatSlider(opt,
                                    0.5f,10f,0.05f,
                                    v -> Text.literal(Math.round(v * 1000)/10f + "%")
                            )
                    ),
                    CreateOption(
                            "option.winters_append.tonics.impure_time_penalty",
                            2.5f,
                            ()-> AppendServerConfig.impureTimePenalty,
                            (v)-> AppendServerConfig.impureTimePenalty = v,
                            opt -> CreateFloatSlider(opt,
                                    0.5f,10f,0.05f,
                                    v -> Text.literal(Math.round(v * 1000)/10f + "%")
                            )
                    )
            ))
            .build();

    public static OptionGroup Effects = OptionGroup.createBuilder()
            .name(Text.translatable("group.winters_append.tonics.effects"))
            .description(OptionDescription.of(
                    Text.translatable("config.winters_append.label_server").formatted(Formatting.AQUA,Formatting.BOLD,Formatting.ITALIC),
                    Text.translatable("group.winters_append.tonics.effects.desc")
            ))
            .options(List.of(
                    CreateOption(
                            "option.winters_append.tonics.default_effect_limit",
                            3,
                            ()-> AppendServerConfig.defaultTonicEffects,
                            (v)-> AppendServerConfig.defaultTonicEffects = (int)v,
                            opt -> CreateIntSlider(opt,
                                    1,9,1,
                                    v -> Text.literal(v.toString()))
                    ),
                    CreateOption(
                            "option.winters_append.tonics.default_length",
                            24000,
                            ()-> AppendServerConfig.defaultTonicLength,
                            (v)-> AppendServerConfig.defaultTonicLength = (int)v,
                            opt -> CreateIntSlider(opt,
                                    1200,48000,200,
                                    v -> Text.literal(StringHelper.formatTicks(v)))
                    ), CreateOption(
                            "option.winters_append.tonics.minimum_length",
                            1200,
                            ()-> AppendServerConfig.minimumTonicLength,
                            (v)-> AppendServerConfig.minimumTonicLength = (int)v,
                            opt -> CreateIntSlider(opt,
                                    0,1200,20,
                                    v -> Text.literal(StringHelper.formatTicks(v)))
                    ))
            )
            .build();
}
