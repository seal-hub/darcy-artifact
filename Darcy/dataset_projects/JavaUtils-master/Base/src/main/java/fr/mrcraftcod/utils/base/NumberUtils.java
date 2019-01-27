package fr.mrcraftcod.utils.base;

/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 09/03/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-03-09
 */
public class NumberUtils
{
	private static int mod(int number, int base)
	{
		if(number >= 0)
			return number % base;
		return base + number % base;
	}
}
