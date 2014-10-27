package com.slymask3.instantblocks.gui;

import java.awt.Color;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

import org.lwjgl.input.Keyboard;

import com.slymask3.instantblocks.block.BlockInstantStatue;
import com.slymask3.instantblocks.handler.ConfigurationHandler;
import com.slymask3.instantblocks.tileentity.TileEntityInstantStatue;
import com.slymask3.instantblocks.utility.InstantBlocksFunctions;
import com.slymask3.instantblocks.utility.LogHelper;

import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiCheckBox;
import cpw.mods.fml.client.config.GuiSlider;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiInstantStatue extends GuiScreen {
	private static InstantBlocksFunctions ibf = new InstantBlocksFunctions();
	private static ConfigurationHandler config = new ConfigurationHandler();
	
	private EntityPlayer player;
	private TileEntityInstantStatue tileEntity;
	
    private GuiButtonExt doneBtn;
    private GuiButtonExt cancelBtn;
	private GuiTextField input;
	
	private GuiCheckBox head;
	private GuiCheckBox body;
	private GuiCheckBox armLeft;
	private GuiCheckBox armRight;
	private GuiCheckBox legLeft;
	private GuiCheckBox legRight;
	
	private GuiCheckBox rgbMode;

	public GuiInstantStatue(EntityPlayer player, TileEntityInstantStatue entity/*, World world, int x, int y, int z*/) {
		this.player = player;
		this.tileEntity = entity;
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
        this.buttonList.add(this.doneBtn = new GuiButtonExt(0, this.width / 2 - 4 - 150, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.done", new Object[0])));
        this.buttonList.add(this.cancelBtn = new GuiButtonExt(1, this.width / 2 + 4, this.height / 4 + 120 + 12, 150, 20, I18n.format("gui.cancel", new Object[0])));
        
        this.buttonList.add(this.head = new GuiCheckBox(2, this.width / 2 - 4 - 150, this.height / 4 + 20 + 12, "Head", BlockInstantStatue.head));
        this.buttonList.add(this.body = new GuiCheckBox(3, this.width / 2 - 4 - 150, this.height / 4 + 30 + 12, "Body", BlockInstantStatue.body));
        this.buttonList.add(this.armLeft = new GuiCheckBox(4, this.width / 2 - 4 - 150, this.height / 4 + 40 + 12, "Arm (Left)", BlockInstantStatue.armLeft));
        this.buttonList.add(this.armRight = new GuiCheckBox(5, this.width / 2 + 4, this.height / 4 + 40 + 12, "Arm (Right)", BlockInstantStatue.armRight));
        this.buttonList.add(this.legLeft = new GuiCheckBox(6, this.width / 2 - 4 - 150, this.height / 4 + 50 + 12, "Leg (Left)", BlockInstantStatue.legLeft));
        this.buttonList.add(this.legRight = new GuiCheckBox(7, this.width / 2 + 4, this.height / 4 + 50 + 12, "Leg (Right)", BlockInstantStatue.legRight));
        
        this.buttonList.add(this.rgbMode = new GuiCheckBox(8, this.width / 2 - 4 - 150, this.height / 4 + 70 + 12, "RGB Mode", config.rgbMode));
		
		this.input = new GuiTextField(this.fontRendererObj, this.width / 2 - 4 - 150, 50, 300+8, 20);
		this.input.setText("");
		this.input.setFocused(true);
		
		this.doneBtn.enabled = this.input.getText().trim().length() > 3;
	}

	@Override
	public void updateScreen() {
		this.input.updateCursorCounter();
	}

	@Override
	protected void actionPerformed(final GuiButton btn) {
		if (btn.enabled) {
			if (btn.id == doneBtn.id) {
				close();
			} else if (btn.id == cancelBtn.id) {
				Keyboard.enableRepeatEvents(false);
				mc.displayGuiScreen(null);
			}
		}
	}

	@Override
	protected void keyTyped(final char par1, final int par2) {
		
		if (("0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_".indexOf(par1) >= 0) || (par2 == 14)) {
			input.textboxKeyTyped(par1, par2);
		} else if (par2 == doneBtn.id) {
			actionPerformed(doneBtn);
		} else if (par2 == cancelBtn.id) {
			actionPerformed(cancelBtn);
		}

        this.doneBtn.enabled = this.input.getText().trim().length() > 3;
	}

	@Override
	protected void mouseClicked(int x, int y, int click) {
        super.mouseClicked(x, y, click);
        this.input.mouseClicked(x, y, click);
    }
	
	@Override
	public void drawScreen(final int par1, final int par2, final float par3) {
		this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Instant Statue Block", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "Enter a Minecraft Username:", this.width / 2 - 4 - 150, 37, 10526880);
        this.drawString(this.fontRendererObj, "Select parts to generate:", this.width / 2 - 3 - 150, this.height / 4 + 8 + 12, 10526880);
        this.drawString(this.fontRendererObj, "Unchecking RGB Mode will use vanilla minecraft wool colorssss.", this.width / 2 - 3 - 150, this.height / 4 + 83 + 12, 10526880);
        
        //this.drawRect(this.width / 2, this.height / 2, this.width / 2 + 50, this.height / 2 + 50, 1);
        
		this.input.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}
	
	public void close() {

		LogHelper.info("Gui(): "+head.isChecked() + " " + body.isChecked() + " " + armLeft.isChecked()+" "+armRight.isChecked()+" "+legLeft.isChecked()+" "+legRight.isChecked());
		
		//tileEntity.setParts(head.isChecked(), body.isChecked(), armLeft.isChecked(), armRight.isChecked(), legLeft.isChecked(), legRight.isChecked());
		
		BlockInstantStatue.head = head.isChecked();
		BlockInstantStatue.body = body.isChecked();
		BlockInstantStatue.armLeft = armLeft.isChecked();
		BlockInstantStatue.armRight = armRight.isChecked();
		BlockInstantStatue.legLeft = legLeft.isChecked();
		BlockInstantStatue.legRight = legRight.isChecked();
		
		
		BlockInstantStatue.username = input.getText();
		BlockInstantStatue.player = this.player;
		//TileEntityInstantStatue.world = this.world;
		//TileEntityInstantStatue.x = this.x;
		//TileEntityInstantStatue.y = this.y;
		//TileEntityInstantStatue.z = this.z;
		//TileEntityInstantStatue.tile = this.tileEntity;
		
		config.rgbMode = rgbMode.isChecked();
		
		
		Keyboard.enableRepeatEvents(false);
		mc.displayGuiScreen(null);
		
		LogHelper.info(input.getText());
	}
	
}