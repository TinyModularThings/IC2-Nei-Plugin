package speiger.src.crops.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ic2.api.crops.CropCard;
import ic2.api.crops.Crops;
import ic2.api.info.IC2Classic.IC2Type;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

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
		return ItemStack.copyItemStack(displayItems.get(card));
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
	public void load(Map<CropCard, ItemStack> par1, IC2Type type)
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
					String name = card.displayName();
					if(type == IC2Type.Experimental)
					{
						name = StatCollector.translateToLocal(name);
					}
					storeCrop(item, card);
					item.setStackDisplayName("Crop "+name);
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
					String name = card.displayName();
					if(type == IC2Type.Experimental)
					{
						name = StatCollector.translateToLocal(name);
					}
					item.setStackDisplayName("Crop "+name);
					storeCrop(item, card);
					displayItems.put(card, item);
				}
			}
			if(!displayItems.containsKey(card))
			{
				ItemStack stack = par1.get(card).copy();
				storeCrop(stack, card);
				displayItems.put(card, stack);
			}
		}
	}
	
	public static void storeCrop(ItemStack stack, CropCard card)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setString("Owner", card.owner());
		nbt.setString("Name", card.name());
		if(!stack.hasTagCompound())
		{
			stack.setTagCompound(new NBTTagCompound());
		}
		stack.getTagCompound().setTag("BREED_INFO", nbt);
	}
	
	public static CropCard getCrop(ItemStack stack)
	{
		CropCard card = Crops.instance.getCropCard(stack);
		if(card == null)
		{
			NBTTagCompound nbt = stack.getTagCompound();
			if(nbt == null || !nbt.hasKey("BREED_INFO"))
			{
				return null;
			}
			NBTTagCompound subTag = nbt.getCompoundTag("BREED_INFO");
			card = Crops.instance.getCropCard(subTag.getString("Owner"), subTag.getString("Name"));
		}
		return card;
	}
}
