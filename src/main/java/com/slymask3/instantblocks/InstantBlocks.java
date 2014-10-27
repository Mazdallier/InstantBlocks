package com.slymask3.instantblocks;

import net.minecraft.command.ICommandManager;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;

import com.slymask3.instantblocks.command.CommandInstantBlocks;
import com.slymask3.instantblocks.gui.GuiHandler;
import com.slymask3.instantblocks.handler.ClientTickHandler;
import com.slymask3.instantblocks.handler.ConfigurationHandler;
import com.slymask3.instantblocks.handler.ConnectionHandler;
import com.slymask3.instantblocks.init.Loot;
import com.slymask3.instantblocks.init.ModBlocks;
import com.slymask3.instantblocks.init.ModItems;
import com.slymask3.instantblocks.init.Recipes;
import com.slymask3.instantblocks.proxy.IProxy;
import com.slymask3.instantblocks.reference.Reference;
import com.slymask3.instantblocks.tileentity.TileEntityColor;
import com.slymask3.instantblocks.tileentity.TileEntityInstantStatue;
import com.slymask3.instantblocks.utility.InstantBlocksFunctions;
import com.slymask3.instantblocks.utility.LogHelper;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Reference.MOD_ID, name=Reference.MOD_NAME, version=Reference.VERSION, guiFactory=Reference.GUI_FACTORY_CLASS)
//@NetworkMod(clientSideRequired = true, serverSideRequired = false)
public class InstantBlocks {
	@Instance(Reference.MOD_ID)
	public static InstantBlocks instance = new InstantBlocks();
	
	public static InstantBlocksFunctions ibf = new InstantBlocksFunctions();
	public static ConfigurationHandler config = new ConfigurationHandler();
	
	@SidedProxy(clientSide=Reference.CLIENT_PROXY_CLASS, serverSide=Reference.SERVER_PROXY_CLASS)
	public static IProxy proxy;
	
    //public static Block.SoundType soundWaterFootstep = new SoundType("random.splash", 1.0F, 1.0F);
    //public static Block.SoundType soundLavaFootstep = new SoundType("random.fizz", 1.0F, 1.0F);
	
	//public static String ver = "v1.4.8";
	
	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		ConfigurationHandler.init(event.getSuggestedConfigurationFile());
		FMLCommonHandler.instance().bus().register(new ConfigurationHandler());
		FMLCommonHandler.instance().bus().register(new ConnectionHandler());
		FMLCommonHandler.instance().bus().register(new ClientTickHandler());

		TileEntity.addMapping(TileEntityColor.class, "TileEntityColor");
		TileEntity.addMapping(TileEntityInstantStatue.class, "TileEntityInstantStatue");
		
		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());
		
		ModItems.init();
		ModBlocks.init();
		//ModSounds.init();
		
		LogHelper.info("Pre Initialization Complete!");
	}
	
	@Mod.EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.registerInformation();
		
		Recipes.init();
		Loot.init();
		
		LogHelper.info("Initialization Complete!");
	}

	@Mod.EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		LogHelper.info("Post Initialization Complete!");
		
		/*Color color1 = new Color(122, 255, 240);
		Color color2 = new Color(196, 207, 81);
		Color color3 = new Color(48, 156, 50);
		
		LogHelper.info("color1 (lightblue?): " + ColorHelper.getWoolColor(color1));		
		LogHelper.info("color2 (yellow?): " + ColorHelper.getWoolColor(color2));		
		LogHelper.info("color3 (green?): " + ColorHelper.getWoolColor(color3));*/
	}
	
	@Mod.EventHandler
	public void serverStart(FMLServerStartingEvent event) {
		MinecraftServer server = MinecraftServer.getServer(); //Gets current server
		ICommandManager command = server.getCommandManager(); //Gets the command manager to use for server
		ServerCommandManager serverCommand = ((ServerCommandManager) command); //Turns it into another form to use
		
		serverCommand.registerCommand(new CommandInstantBlocks());
	}
}