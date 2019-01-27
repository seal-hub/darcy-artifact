import de.thatsich.map.MapConverterService;
import de.thatsich.map.URLEncoderConverterService;

module map.converter.service {
	requires java.desktop;

	exports de.thatsich.map;

	provides MapConverterService with URLEncoderConverterService;
}
