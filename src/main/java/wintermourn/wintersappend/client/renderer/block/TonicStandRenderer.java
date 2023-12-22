package wintermourn.wintersappend.client.renderer.block;

import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.fluid.Fluids;
import wintermourn.wintersappend.block.entity.TonicStandBlockEntity;

public class TonicStandRenderer implements BlockEntityRenderer<TonicStandBlockEntity> {
    float PADDING = 4 / 16f;
    float VERTIPOS = 6 / 16f;

    public TonicStandRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(TonicStandBlockEntity entity, float tickDelta, MatrixStack matrices,
                       VertexConsumerProvider vertexConsumers, int light, int overlay) {
        VertexConsumer consumer = vertexConsumers.getBuffer(RenderLayer.getSolid());
        MatrixStack.Entry worldMatrix = matrices.peek();

        FluidRenderHandler renderHandler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER);
        Sprite[] sprites = renderHandler.getFluidSprites(null, null, Fluids.WATER.getDefaultState());
        Sprite waterSprite = sprites[0];
        int[] color = entity.getFluidColorRGB();

        matrices.push();

        float texSizeU = waterSprite.getMaxU() - waterSprite.getMinU();
        float texSizeV = waterSprite.getMaxV() - waterSprite.getMinV();
        float texPaddingU = PADDING * texSizeU;
        float texPaddingV = PADDING * texSizeV;

        if (entity.hasOutput() && color != null)
        {
            consumer
                    .vertex(worldMatrix.getPositionMatrix(), PADDING, VERTIPOS, 1f-PADDING)
                    .color(color[0],color[1],color[2],0xaa)
                    .texture(waterSprite.getMinU() + texPaddingU, waterSprite.getMinV() + texPaddingV)
                    .light(light)
                    .normal(0f,1f,0f)
                    .next();
            consumer
                    .vertex(worldMatrix.getPositionMatrix(), 1f-PADDING, VERTIPOS, 1f-PADDING)
                    .color(color[0],color[1],color[2],0xaa)
                    .texture(waterSprite.getMaxU() - texPaddingU, waterSprite.getMinV() + texPaddingV)
                    .light(light)
                    .normal(0f,1f,0f)
                    .next();
            consumer
                    .vertex(worldMatrix.getPositionMatrix(), 1f-PADDING, VERTIPOS, PADDING)
                    .color(color[0],color[1],color[2],0xaa)
                    .texture(waterSprite.getMaxU() - texPaddingU, waterSprite.getMaxV() - texPaddingV)
                    .light(light)
                    .normal(0f,1f,0f)
                    .next();
            consumer
                    .vertex(worldMatrix.getPositionMatrix(), PADDING, VERTIPOS, PADDING)
                    .color(color[0],color[1],color[2],0xaa)
                    .texture(waterSprite.getMinU() + texPaddingU, waterSprite.getMaxV() - texPaddingV)
                    .light(light)
                    .normal(0f,1f,0f)
                    .next();

        }

        matrices.pop();

    }
}
