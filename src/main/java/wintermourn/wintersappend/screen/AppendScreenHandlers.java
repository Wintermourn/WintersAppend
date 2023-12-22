package wintermourn.wintersappend.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;

public class AppendScreenHandlers {
    public static final ScreenHandlerType<TonicStandScreenHandler> TONIC_STAND_SCREEN_HANDLER;

    static
    {
        TONIC_STAND_SCREEN_HANDLER =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        new Identifier(WintersAppend.MOD_ID, "tonic_stand"),
                        new ExtendedScreenHandlerType<>(TonicStandScreenHandler::new));
    }

    public static void Register() {}
}
