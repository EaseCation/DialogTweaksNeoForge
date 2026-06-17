package net.easecation.dialogtweaks.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.screens.dialog.DialogScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Renders a button's label across multiple lines when it contains explicit {@code '\n'} line breaks,
 * but only for buttons inside a {@link DialogScreen} (the screen ViaBedrock/ViaProxy uses to display
 * Bedrock FormUI).
 *
 * <p>Vanilla {@code AbstractWidget.renderScrollingString} treats the whole {@link Component} as a single
 * line — the {@code '\n'} is a zero-width glyph, so two-line Bedrock button labels collapse onto one
 * overlapping line. We split on the newline and draw each line centred.
 *
 * <p>Scope is deliberately narrow: only {@code DialogScreen} buttons, and only when the label actually
 * contains a newline (otherwise vanilla rendering — including its horizontal scrolling for overlong
 * single-line text — is left completely untouched).
 */
@Mixin(AbstractButton.class)
public class AbstractButtonMixin {

    @Inject(method = "renderString", at = @At("HEAD"), cancellable = true)
    private void dialogtweaks$wrapMultilineLabel(GuiGraphics guiGraphics, Font font, int color, CallbackInfo ci) {
        if (!(Minecraft.getInstance().screen instanceof DialogScreen<?>)) {
            return;
        }

        final AbstractButton self = (AbstractButton) (Object) this;
        // Huge width => Font#split only breaks on explicit '\n', never on width (no auto word-wrap).
        // The returned FormattedCharSequences preserve the original styling (§ colour/format codes).
        final List<FormattedCharSequence> lines = font.split(self.getMessage(), Integer.MAX_VALUE);
        if (lines.size() <= 1) {
            return; // no newline -> leave vanilla single-line behaviour intact
        }

        final int lineHeight = font.lineHeight;
        final int centerX = self.getX() + self.getWidth() / 2;
        final int startY = self.getY() + (self.getHeight() - lines.size() * lineHeight) / 2;
        for (int i = 0; i < lines.size(); i++) {
            final FormattedCharSequence line = lines.get(i);
            final int x = centerX - font.width(line) / 2;
            guiGraphics.drawString(font, line, x, startY + i * lineHeight, color, true);
        }
        ci.cancel(); // replace vanilla single-line draw entirely
    }
}
