package exprivia.it.documenti.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import exprivia.it.documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.documenti.service.IConfidentialDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/document")
public class ConfidentialDocumentController {
    
    @Autowired
    private IConfidentialDocument service;

    // GET /confidential/{presidentCode}
    @Operation(
        summary = "Recupera tutti i documenti pubblici dando il codice presidente",
        description = "Restituisce una lista completa di documenti confidenziali o lista vuota"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Trovati"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido")
    })

    @GetMapping("/confidential/{presidentCode}")
    public List<ConfidentialDocumentDTO> getConfidentialDocuments(@PathVariable String presidentCode) {
        return service.getConfidentialDocuments(presidentCode);
    }

    // GET /confidential/{protocolNumber}/{codeOrsignature}
    @Operation(
        summary = "Recupera il documento confidenziale dato il numero di protocollo e il codice presidente o firma del documento corrente corretto",
        description = "Restituisce se esistente il Documento"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Trovati"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido"),
        @ApiResponse(responseCode = "404", description = "Nessun documento trovato")
    })

    @GetMapping("/confidential/{protocolNumber}/{codeOrsignature}")
    public ResponseEntity<ConfidentialDocumentDTO> getConfidentialDocument(@PathVariable String protocolNumber, @PathVariable String codeOrsignature) {
        
        ConfidentialDocumentDTO documento = service.getDocumentByProtocolNumber(protocolNumber,codeOrsignature);
        
        return ResponseEntity.ok(documento);
    }

    // POST /confidential/{presidentCode}
    @Operation(
        summary = "crea un documento confidenziale",
        description = "crea il documento confidenziale e lo salva nel databse"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Creato con Successo"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido"),
        @ApiResponse(responseCode = "409", description = "Documento già esistente")
    })

    @PostMapping("/confidential/{presidentCode}")
    public ResponseEntity<ConfidentialDocumentDTO> createConfidentialDocument(@Valid @RequestBody ConfidentialDocumentDTO dto, @PathVariable String presidentCode) {

        ConfidentialDocumentDTO created = service.createConfidentialDocument(dto, presidentCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}