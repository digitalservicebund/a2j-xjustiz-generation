package de.digitalservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.digitalservice.model.fgrUser.UserData;
import de.digitalservice.service.NachrichtenkopfGenerator;
import de.digitalservice.service.claims.FgrClaimGenerator;
import de.xjustiz.NachrichtKlaverKlageverfahren3500001;
import jakarta.xml.bind.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class App implements ApplicationRunner {

    private final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws DatatypeConfigurationException {

        UserData userData = loadUserData("/userData/userDataMock.json");
        if (userData == null) {
            System.err.println("User data is missing or invalid.");
            return;
        }

        var nachricht = new NachrichtKlaverKlageverfahren3500001();
        nachricht.setNachrichtenkopf(
                new NachrichtenkopfGenerator().createNachrichtenkopf(userData, "DigitalService GmbH", "FGR claim",
                        "DigitalService GmbH", "1.0.0"));

        var fgrClaimGenerator = new FgrClaimGenerator();
        nachricht.setGrunddaten(fgrClaimGenerator.generatePlaintiffAndCedentGrunddaten(userData));
        nachricht.setGrunddaten(fgrClaimGenerator.generateDefendantGrunddaten(userData));
        nachricht.setInhaltsdaten(fgrClaimGenerator.createClaim(userData));

        try {
            JAXBContext jc = JAXBContext.newInstance(NachrichtKlaverKlageverfahren3500001.class);
            Marshaller marshaller = jc.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // optional schema validation
            if (args.containsOption("validate")) {
                marshaller.setSchema(loadSchema(jc));
            }

            String xml = marshalToString(marshaller, nachricht);
            System.out.println(xml);

        } catch (JAXBException | IOException | SAXException ex) {
            System.err.println("Error generating XML: " + ex);
        }
    }

    private UserData loadUserData(String resourcePath) {
        try (InputStream is = App.class.getResourceAsStream(resourcePath)) {
            if (is == null) {
                System.err.println("Resource not found: " + resourcePath);
                return null;
            }
            return mapper.readValue(is, UserData.class);

        } catch (IOException e) {
            System.err.println("Failed to parse user data: " + e.getMessage());
            return null;
        }
    }

    private Schema loadSchema(JAXBContext jc) throws IOException, SAXException {
        List<DOMResult> domResults = generateJaxbSchemas(jc);
        List<DOMSource> sources = domResults.stream()
                .map(result -> new DOMSource(result.getNode()))
                .toList();

        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return sf.newSchema(sources.toArray(DOMSource[]::new));
    }

    private String marshalToString(Marshaller marshaller, Object object) throws JAXBException {
        StringWriter writer = new StringWriter();
        marshaller.marshal(object, writer);
        return writer.toString();
    }

    private List<DOMResult> generateJaxbSchemas(JAXBContext context) throws IOException {
        List<DOMResult> results = new ArrayList<>();

        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(String namespaceUri, String suggestedFilename) {
                DOMResult result = new DOMResult();
                result.setSystemId(suggestedFilename);
                results.add(result);
                return result;
            }
        });

        return results;
    }
}
