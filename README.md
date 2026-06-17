# DialogTweaks (NeoForge)

Client-side, Mixin-only NeoForge mod that improves the vanilla Java **Dialog** screen
(`net.minecraft.client.gui.screens.dialog.DialogScreen`) — the screen ViaBedrock / ViaProxy uses to
display Bedrock **FormUI** (ModalForm / ActionForm / CustomForm) to Java clients.

## Features

- **Multi-line button labels.** Bedrock form buttons frequently embed `\n` to show two-line labels.
  When such a label reaches the Java client it is a single `Component`; vanilla
  `AbstractWidget.renderScrollingString` renders it as one line (the `\n` is a zero-width glyph), so
  the lines overlap. This mod splits the label on explicit `\n` and draws each line centred — but only
  for buttons inside a `DialogScreen`, and only when the label actually contains a newline. Every other
  button (and overlong single-line scrolling) is left untouched.

## Scope / design

- Pure render-layer change via one Mixin on `AbstractButton#renderString`; no layout/size changes.
- Gated on `Minecraft.getInstance().screen instanceof DialogScreen<?>` so vanilla and other-mod GUIs are
  unaffected.
- Splits only on explicit `\n` (`Font#split` with `Integer.MAX_VALUE` width) — no automatic
  width-based word wrap.

## Build

- Standalone: `./gradlew assemble` → `build/libs/dialogtweaks-<version>.jar`.
- As part of the EaseCation NeoForge workspace it is an `includeBuild` submodule; the workspace root
  `assemble` builds it and `copy-mods.ps1` deploys it.

Requires Minecraft 1.21.8 / NeoForge 21.8.x, Java 21. Client-side only.
