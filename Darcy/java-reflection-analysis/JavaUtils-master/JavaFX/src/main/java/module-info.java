/**
 * Created by Thomas Couchoud (MrCraftCod - zerderr@gmail.com) on 22/09/2017.
 *
 * @author Thomas Couchoud
 * @since 2017-09-22
 */
module fr.mrcraftcod.utils.javafx {
	requires transitive javafx.graphics;
	requires transitive javafx.swing;
	requires transitive javafx.controls;
	requires transitive fr.mrcraftcod.utils.base;
	
	exports fr.mrcraftcod.utils.javafx;
	exports fr.mrcraftcod.utils.javafx.components;
}