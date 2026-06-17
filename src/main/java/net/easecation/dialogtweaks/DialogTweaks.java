package net.easecation.dialogtweaks;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DialogTweaks — client-side render tweaks for the vanilla Java Dialog screen
 * ({@code net.minecraft.client.gui.screens.dialog.DialogScreen}), which ViaBedrock/ViaProxy
 * uses to display Bedrock FormUI (ModalForm / ActionForm / CustomForm).
 *
 * <p>All behaviour lives in Mixins; this class only exists so NeoForge recognises the mod.
 */
@Mod(value = DialogTweaks.MOD_ID, dist = Dist.CLIENT)
public class DialogTweaks {

    public static final String MOD_ID = "dialogtweaks";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public DialogTweaks(IEventBus modEventBus) {
        LOGGER.info("[DialogTweaks] Initialized (Dialog/FormUI render tweaks).");
    }
}
