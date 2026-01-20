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

import exprivia.it.documenti.model.dto.SecretDocumentDTO;
import exprivia.it.documenti.service.ISecreteDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/document")
public class SecretDocumentController {

    @Autowired
    private ISecreteDocument service;

    //GET /secret/{presidentCode}
    @Operation(
        summary = "Recupera tutti i documenti segreti dando il codice presidente",
        description = "Restituisce una lista completa di documenti segreti o lista vuota"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Trovati"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido")
    })

    @GetMapping("/secret/{presidentCode}")
    public List<SecretDocumentDTO> getSecretDocuments(@PathVariable String presidentCode) {
        return service.getSecretDocuments(presidentCode);
    }

    //GET /secret/{numProtocollo}/{presidentCode}
    @Operation(
        summary = "Recupera il documento segreto dato il numero di protocollo e il codice presidente o firma del documento corrente corretto",
        description = "Restituisce se esistente il Documento"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Trovati"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido"),
        @ApiResponse(responseCode = "404", description = "Nessun documento trovato")
    })
    @GetMapping("/secret/{numProtocollo}/{presidentCode}")
    public ResponseEntity<SecretDocumentDTO> getConfidentialDocument(@PathVariable String numProtocollo, @PathVariable String presidentCode) {
        
        SecretDocumentDTO documento = service.getSecretDocumentByNumProtocollo(numProtocollo, presidentCode);
        
        return ResponseEntity.ok(documento);
    }

    //POST /secret/{presidentCode}
    @Operation(
        summary = "crea un documento segreto",
        description = "crea il documento segreto e lo salva nel databse"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Documenti Creato con Successo"),
        @ApiResponse(responseCode = "400", description = "Codice Presidente non valido"),
        @ApiResponse(responseCode = "409", description = "Documento già esistente")
    })

    @PostMapping("/secret/{presidentCode}")
    public ResponseEntity<SecretDocumentDTO> createConfidentialDocument(@Valid @RequestBody SecretDocumentDTO dto, @PathVariable String presidentCode) {

        SecretDocumentDTO created = service.createSecretDocument(dto, presidentCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
}