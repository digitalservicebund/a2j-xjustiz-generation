package de.digitalservice.codes;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Generic loader for Genericode XML code lists.
 */
public class CodeListLoader {

    /**
     * Loads a Genericode XML from resources and returns list of code/value pairs.
     */
    public static List<CodeListEntry> loadFromXml(String resourcePath) throws Exception {
        List<CodeListEntry> entries = new ArrayList<>();

        InputStream xml = CodeListLoader.class.getClassLoader().getResourceAsStream(resourcePath);
        if (xml == null)
            throw new RuntimeException("Resource not found: " + resourcePath);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xml);
        NodeList rows = doc.getElementsByTagName("Row");

        for (int i = 0; i < rows.getLength(); i++) {
            Element row = (Element) rows.item(i);

            NodeList values = row.getElementsByTagName("SimpleValue");
            if (values.getLength() < 2)
                continue;

            String code = values.item(0).getTextContent();
            String value = values.item(1).getTextContent();

            entries.add(new CodeListEntry(code, value));
        }

        return entries;
    }
}
