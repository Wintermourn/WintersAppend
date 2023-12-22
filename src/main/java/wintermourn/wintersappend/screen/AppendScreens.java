package wintermourn.wintersappend.screen;

import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class AppendScreens {
    public static void Register()
    {
        HandledScreens.register(AppendScreenHandlers.TONIC_STAND_SCREEN_HANDLER, TonicStandScreen::new);
    }
}
