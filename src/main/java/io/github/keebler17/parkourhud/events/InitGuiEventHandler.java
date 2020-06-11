package io.github.keebler17.parkourhud.events;

import io.github.keebler17.parkourhud.Buttons;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiOptions;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InitGuiEventHandler {

	@SubscribeEvent
	public void onInitGuiEvent(InitGuiEvent.Post e) {
		if (e.gui instanceof GuiOptions) {
			int x = 0;
			int y = 0;
			for (Object button : e.buttonList) {
				if (button instanceof GuiButton) {
					if (((GuiButton) button).id == 200) { // button 200 is done button on options menu
						x = ((GuiButton) button).xPosition;
						y = ((GuiButton) button).yPosition;
					}
				}
			}
			e.buttonList.add(new GuiButton(Buttons.PKHUD.getValue(), x, y - 27, 200, 20, "Parkour HUD Options..."));
		}
	}

}
