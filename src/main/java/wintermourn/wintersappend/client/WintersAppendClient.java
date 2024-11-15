package wintermourn.wintersappend.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import wintermourn.wintersappend.block.AppendBlocks;
import wintermourn.wintersappend.block.entity.AppendBlockEntities;
import wintermourn.wintersappend.client.colors.ItemColorProviders;
import wintermourn.wintersappend.client.renderer.block.TonicStandRenderer;
import wintermourn.wintersappend.networking.AppendMessages;
import wintermourn.wintersappend.screen.AppendScreens;
import wintermourn.wintersappend.config.AppendClientConfig;

public class WintersAppendClient implements ClientModInitializer {
    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        AppendClientConfig.HANDLER.load();

        BlockRenderLayerMap.INSTANCE.putBlock(AppendBlocks.TONIC_STAND.getBlock(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AppendBlocks.GYPSOPHILA.getBlock(), RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AppendBlocks.GYPSOPHILA_POTTED, RenderLayer.getCutout());

        AppendScreens.Register();

        BlockEntityRendererRegistry.register(AppendBlockEntities.TONIC_STAND_BLOCK_ENTITY, TonicStandRenderer::new);
//        BlockColorProviders.Register();
        ItemColorProviders.Register();

        AppendMessages.RegisterS2CPackets();
    }
}
