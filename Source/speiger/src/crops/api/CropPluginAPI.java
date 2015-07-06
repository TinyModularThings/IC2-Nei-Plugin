package speiger.src.crops.api;

import ic2.api.crops.CropCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.FMLLog;

import net.minecraft.item.ItemStack;

/**
 * 
 * @author Speiger
 *
 * registry class
 */
public class CropPluginAPI
{
	private static List<ICropInfo> toProcess = new ArrayList<ICropInfo>();
	private Map<CropCard, List<String>> extraInfos = new HashMap<CropCard, List<String>>();
	private Map<CropCard, ItemStack> displayItems = new HashMap<CropCard, ItemStack>();
	
	public static CropPluginAPI instance = new CropPluginAPI();
	
	public void registerCropInfo(ICropInfo info)
	{
		if(info != null)
		{
			toProcess.add(info);
		}
	}
	
	public ItemStack getDisplayItem(CropCard card)
	{
		return displayItems.get(card);
	}
	
	public List<String> getExtraInfos(CropCard card)
	{
		List<String> info = extraInfos.get(card);
		if(info == null)
		{
			info = new ArrayList<String>();
		}
		return info;
	}
	
	/**
	 * @IMPORTANT: Never call this Function!
	 */
	public void load(Map<CropCard, ItemStack> par1)
	{
		for(ICropInfo target : toProcess)
		{
			List<CropCard> cards = target.getSupportedCrops();
			if(cards == null || cards.isEmpty())
			{
				continue;
			}
			for(CropCard card : cards)
			{
				List<String> data = target.getCropInformation(card);
				if(data != null && !data.isEmpty() && !extraInfos.containsKey(card))
				{
					extraInfos.put(card, data);
				}
				ItemStack item = target.getDisplayItems(card);
				if(item != null && !displayItems.containsKey(card))
				{
					item = item.copy();
					item.setStackDisplayName("Crop "+card.name());
					displayItems.put(card, item);
				}
			}
		}
		toProcess.clear();
		for(CropCard card : par1.keySet())
		{
			if(card instanceof ICropCardInfo)
			{
				ICropCardInfo info = (ICropCardInfo)card;
				List<String> data = info.getCropInformation();
				if(data != null && !data.isEmpty())
				{
					extraInfos.put(card, data);
				}
				ItemStack item = info.getDisplayItem();
				if(item != null)
				{
					item = item.copy();
					item.setStackDisplayName("Crop "+card.name());
					displayItems.put(card, item);
				}
			}
			if(!displayItems.containsKey(card))
			{
				displayItems.put(card, par1.get(card));
			}
		}
	}
}
