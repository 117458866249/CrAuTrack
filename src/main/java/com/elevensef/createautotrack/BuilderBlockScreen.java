package com.elevensef.createautotrack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BuilderBlockScreen extends AbstractContainerScreen<BuilderBlockMenu> {
    private static final ResourceLocation BACKGROUND = ResourceLocation.fromNamespaceAndPath(CreateAutoTrack.MOD_ID, "textures/gui/background.png");

    private EditBox xTextField;
    private EditBox zTextField;

    public BuilderBlockScreen(BuilderBlockMenu menu, Inventory inv, Component comp) {
        super(menu, inv, comp);
    }

    @Override
    protected void init() {
        super.init();

        Font font = Minecraft.getInstance().font;
        CompoundTag nbtData = menu.blockEntity.serializeNBT();

        xTextField = new EditBox(font, leftPos + 40, topPos + 20, 100, 20, Component.literal("X"));
        xTextField.setMaxLength(50);
        xTextField.setValue(String.valueOf(nbtData.getInt("target_x")));
        xTextField.setResponder(this::onTextChanged1);

        zTextField = new EditBox(font, leftPos + 40, topPos + 50, 100, 20, Component.literal("Y"));
        zTextField.setMaxLength(50);
        zTextField.setValue(String.valueOf(nbtData.getInt("target_z")));
        zTextField.setResponder(this::onTextChanged2);

        addRenderableWidget(xTextField);
        addRenderableWidget(zTextField);

        setInitialFocus(xTextField);
    }

    private void onTextChanged1(String newText) {
        if (xTextField.isFocused()) {
            tryParseAndSend(0, newText);
        }
    }

    private void onTextChanged2(String newText) {
        if (zTextField.isFocused()) {
            tryParseAndSend(1, newText);
        }
    }

    private void tryParseAndSend(int index, String text) {
        String temp = "";
        for (int i = 0;i < text.length();i++){
            if (text.charAt(i) == '-' && i != 0) return;
            temp = temp + text.charAt(i);
        }
        if (text.isEmpty()) {
            sendPacket(index, 0);
            return;
        }
        try {
            int value = Integer.parseInt(temp);
            sendPacket(index, value);
        } catch (NumberFormatException e) {

        }
    }

    private void sendPacket(int index, int value) {
        CreateAutoTrack.CHANNEL.sendToServer(new BuilderMenuNetwork(
                menu.blockEntity.getBlockPos(), index, value));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(BACKGROUND, x, y, 0, -2, imageWidth, imageHeight);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("X"),leftPos+30,topPos+20,0xFFFFFF);
        guiGraphics.drawCenteredString(Minecraft.getInstance().font, Component.literal("Z"),leftPos+30,topPos+50,0xFFFFFF);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (xTextField.isFocused() && xTextField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (zTextField.isFocused() && zTextField.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if ((codePoint>=48 && codePoint<=57)){
            if (xTextField.isFocused() && xTextField.charTyped(codePoint, modifiers)) {
                return true;
            }
            if (zTextField.isFocused() && zTextField.charTyped(codePoint, modifiers)) {
                return true;
            }
            return super.charTyped(codePoint, modifiers);
        }
        else if (codePoint == 45){
            if (xTextField.isFocused() && xTextField.charTyped(codePoint, modifiers)) {
                return true;
            }
            if (zTextField.isFocused() && zTextField.charTyped(codePoint, modifiers)) {
                return true;
            }
            return super.charTyped(codePoint, modifiers);
        }
        return false;
    }
}