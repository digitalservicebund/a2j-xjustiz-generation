package de.digitalservice.web;

import de.digitalservice.api.FlightClaimRequest;
import de.digitalservice.service.flightClaims.FlightClaimApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

@RestController
@RequestMapping("/claims")
@Tag(name = "Flight Claims")
public class ClaimController {

    private final FlightClaimApplicationService flightClaimApplicationService;

    public ClaimController(FlightClaimApplicationService flightClaimApplicationService) {
        this.flightClaimApplicationService = flightClaimApplicationService;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    @Operation(summary = "Generate XJustiz XML for a flight claim")
    public ResponseEntity<String> createClaim(
            @RequestBody FlightClaimRequest request)
            throws javax.xml.datatype.DatatypeConfigurationException, IOException, SAXException {

        String xml = flightClaimApplicationService.generateClaimXml(request);
        return ResponseEntity.ok(xml);
    }
}