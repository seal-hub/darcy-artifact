package by.jprof.app;

import by.jprof.api.GreetingsService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.ServiceLoader;

public class Main {
	public static void main(String[] args) throws Exception {
		final Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		final Element greetings = document.createElement("greetings");

		for (GreetingsService greetingsService : ServiceLoader.load(GreetingsService.class)) {
			final Element greeting = document.createElement("greeting");

			greeting.setTextContent(greetingsService.greet("world"));
			greetings.appendChild(greeting);
		}

		document.appendChild(greetings);

		final Transformer transformer = TransformerFactory.newInstance().newTransformer();

		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(document), new StreamResult(System.out));
	}
}
