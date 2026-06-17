package net.easecation.dialogtweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.dialog.DialogControlSet;
import net.minecraft.server.dialog.CommonButtonData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Grows a Dialog button's height to fit a multi-line label so the (nine-slice) background stretches
 * around all lines instead of overflowing a fixed 20px button.
 *
 * <p>Targets {@code DialogControlSet#createDialogButton} (the single factory for every Dialog action /
 * common button) so the taller height is set on the {@link Button.Builder} <em>before</em> the screen's
 * layout runs — the {@code LinearLayout} then reserves the correct slot and siblings don't overlap.
 * Scope is naturally limited to Dialog buttons (the screen ViaBedrock/ViaProxy uses for Bedrock FormUI),
 * and only when the label actually contains a newline; single-line buttons keep the vanilla 20px height.
 *
 * <p>The vanilla button sprites (widget/button*, border 3) are {@code nine_slice}, so stretching the
 * height keeps the 3px borders intact and only tiles/stretches the centre — no distortion, and these
 * buttons carry no inner icon textures.
 */
@Mixin(DialogControlSet.class)
public class DialogControlSetMixin {

    @Inject(method = "createDialogButton", at = @At("RETURN"))
    private static void dialogtweaks$growHeightForMultiline(CommonButtonData data, Button.OnPress onPress, CallbackInfoReturnable<Button.Builder> cir) {
        final Button.Builder builder = cir.getReturnValue();
        if (builder == null) {
            return;
        }
        // Same split as AbstractButtonMixin: huge width => only explicit '\n' splits, no width wrap.
        final int lines = Minecraft.getInstance().font.split(data.label(), Integer.MAX_VALUE).size();
        if (lines > 1) {
            // Vanilla single-line button is 20px (~9px text + 11px padding). Keep that padding and let the
            // nine-slice background stretch to fit N lines. builder already has width = data.width().
            final int height = lines * Minecraft.getInstance().font.lineHeight + 11;
            builder.size(data.width(), height);
        }
    }
}
