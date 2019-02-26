package speiger.src.crops.api;

import ic2.api.crops.CropCard;

import java.util.List;

import net.minecraft.item.ItemStack;

/**
 * 
 * @author Speiger
 * Class to add External Informations to Crop Cards
 * Internal CropCard Information have Priority!
 *
 */
public interface ICropInfo
{
	/**
	 * @return the CropCards that he checks for.
	 * a null or a empty list means that it get not loaded..
	 */
	public List<CropCard> getSupportedCrops();
	
	
	/**
	 * @return a list of "Important" Information,
	 * which are needed to grow the Crop.
	 * Like: a Special Light Level or something.
	 * @param card: the CropCard which is asking for it.
	 * you can also return null or a empty list if you only
	 * want to add DisplayItems.
	 */
	public List<String> getCropInformation(CropCard card);
	
	/**
	 * @return the DisplayItem for the CropCard he is checking.
	 * null means it get not implemented. So you can return null
	 */
	public ItemStack getDisplayItems(CropCard card);

}
