package wintermourn.wintersappend.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import wintermourn.wintersappend.WintersAppend;
import wintermourn.wintersappend.client.gui.widget.UntexturedButtonWidget;
import wintermourn.wintersappend.networking.AppendMessages;

public class TonicStandScreen extends HandledScreen<TonicStandScreenHandler> {
    public static final Identifier TEXTURE = new Identifier(WintersAppend.MOD_ID,"textures/gui/container/tonic_stand.png");
    boolean buttonHovered = false;

    UntexturedButtonWidget brewingButtonWidget;

    public TonicStandScreen(TonicStandScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;
        brewingButtonWidget = new UntexturedButtonWidget(
                x + 17, y + 61, 18, 11, this::onBrewingModeToggle);
        this.addDrawableChild(brewingButtonWidget);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1f,1f,1f,1f);
        RenderSystem.setShaderTexture(0, TEXTURE);

        int x = (width - backgroundWidth) / 2;
        int y = (height - backgroundHeight) / 2;

        context.drawTexture(TEXTURE, x, y, 0,0, backgroundWidth, backgroundHeight);

        renderProgressArrow(context, x + 98, y + 16);
        renderFuelBar(context, x + 61, y + 44);
        renderPurityBar(context, x + 52, y + 51);
        renderModeButton(context, x + 17, y + 60, handler.getBrewingMode(), brewingButtonWidget.isHovered());

        //render progress
        //render arrow
    }

    private void renderProgressArrow(DrawContext ctx, int x, int y)
    {
        ctx.drawTexture(TEXTURE, x, y, 176,0, 9, Math.round(28 * handler.getScaledProgress()));
    }

    private void renderFuelBar(DrawContext ctx, int x, int y)
    {
        ctx.drawTexture(TEXTURE, x, y, 176,33, Math.round(18 * handler.getScaledFuel()), 4);
        ctx.drawTexture(TEXTURE, x, y, 176,29, Math.round(18 * handler.getScaledFuelEstimate()), 4);
    }
    private void renderModeButton(DrawContext ctx, int x, int y, int mode, boolean buttonHovered)
    {
        ctx.drawTexture(TEXTURE, x + mode * 7, y, 176 + (buttonHovered ? 11 : 0),45, 11, 11);
        ctx.drawTexture(TEXTURE, x + mode * 7, y, 198 + mode * 11,45, 11, 11);
    }
    private void renderPurityBar(DrawContext ctx, int x, int y)
    {
        ctx.setShaderColor(1,0.6f,0.5f,1);
        ctx.drawTexture(TEXTURE, x, y, 176,37, Math.round(27 * handler.getScaledPurity()), 4);
        ctx.setShaderColor(0.6f,1,0.65f,1);
        ctx.drawTexture(TEXTURE, x, y, 176,37, Math.round(27 * handler.getScaledPurityEstimate()), 4);
        ctx.setShaderColor(1,1,1,1);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }

    public void onBrewingModeToggle(ButtonWidget button)
    {
        handler.setBrewingMode(handler.getBrewingMode() == 1 ? 0 : 1);
        ClientPlayNetworking.send(
                AppendMessages.TOGGLE_BREWING_MODE,
                PacketByteBufs.create().writeBlockPos(handler.blockEntity.getPos()));
    }
}
