package de.digitalservice.service.flightClaims;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.digitalservice.api.FlightClaimRequest;
import de.digitalservice.model.fgrUser.UserData;
import de.digitalservice.service.NachrichtenkopfGenerator;
import de.xjustiz.NachrichtKlaverKlageverfahren3500001;
import jakarta.xml.bind.*;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlightClaimApplicationService {

    private final ObjectMapper mapper;

    public FlightClaimApplicationService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    public String generateClaimXml(FlightClaimRequest request)
            throws DatatypeConfigurationException, IOException, SAXException {

        UserData userData = mapper.convertValue(request, UserData.class);

        var nachricht = new NachrichtKlaverKlageverfahren3500001();
        var generator = new FgrClaimGenerator();

        nachricht.setNachrichtenkopf(
                new NachrichtenkopfGenerator()
                        .createNachrichtenkopf(userData, "DigitalService GmbH",
                                "FGR claim", "DigitalService GmbH", "1.0.0"));

        nachricht.setGrunddaten(generator.generatePlaintiffAndCedentGrunddaten(userData));
        nachricht.setGrunddaten(generator.generateDefendantGrunddaten(userData));
        nachricht.setInhaltsdaten(generator.createClaim(userData));

        System.out.println("Generated XML: \n" + marshal(nachricht));

        return marshal(nachricht);
    }

    private String marshal(Object object) throws IOException, SAXException {
        try {
            JAXBContext context = JAXBContext.newInstance(object.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            marshaller.setSchema(loadSchema(context)); // attach schema for validation

            StringWriter writer = new StringWriter();
            marshaller.marshal(object, writer);
            return writer.toString();

        } catch (JAXBException e) {
            throw new RuntimeException("XML generation failed", e);
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