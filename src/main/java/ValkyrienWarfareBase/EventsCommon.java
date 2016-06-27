package ValkyrienWarfareBase;

import ValkyrienWarfareBase.Interaction.CustomNetHandlerPlayServer;
import ValkyrienWarfareBase.PhysicsManagement.PhysicsWrapperEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.HarvestCheck;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.event.world.ChunkDataEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class EventsCommon {

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerTickEvent(PlayerTickEvent event){
		if(!event.player.worldObj.isRemote){
			EntityPlayerMP player = (EntityPlayerMP) event.player;
			if(!(player.playerNetServerHandler instanceof CustomNetHandlerPlayServer)){
				player.playerNetServerHandler = new CustomNetHandlerPlayServer(player.playerNetServerHandler);
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onWorldLoad(WorldEvent.Load event){
		ValkyrienWarfareMod.chunkManager.initWorld(event.getWorld());
		ValkyrienWarfareMod.physicsManager.initWorld(event.getWorld());
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onWorldUnload(WorldEvent.Unload event){
		if(!event.getWorld().isRemote){
			ValkyrienWarfareMod.chunkManager.removeWorld(event.getWorld());
		}
		ValkyrienWarfareMod.physicsManager.removeWorld(event.getWorld());
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onChunkNBTLoad(ChunkDataEvent.Load event){
		NBTTagCompound data = event.getData();
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onChunkNBTUnload(ChunkDataEvent.Save event){
		NBTTagCompound data = event.getData();
		
	}

	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onEntityUntrack(PlayerEvent.StopTracking event){
		if(!event.getEntityPlayer().worldObj.isRemote){
			Entity ent = event.getTarget();
			if(ent instanceof PhysicsWrapperEntity){
				((PhysicsWrapperEntity)ent).wrapping.onPlayerUntracking(event.getEntityPlayer());
			}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onPlayerOpenContainerEvent(PlayerOpenContainerEvent event){
		event.setResult(Result.ALLOW);
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onBreakEvent(BreakEvent event){

	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onHarvestDropsEvent(HarvestDropsEvent event){
		
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	public void onHarvestCheck(HarvestCheck event){

	}

}
