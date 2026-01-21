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

import exprivia.it.documenti.model.dto.PublicDocumentDTO;
import exprivia.it.documenti.service.IPublicDocument;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/document")
@Tag(name = "Public Documents", description = "API per la gestione dei documenti accessibili a tutti")// aggiunge un titolo e una descrizione 
public class PublicDocumentController {

    @Autowired
    private IPublicDocument service;

    //GET /public
    @Operation(
        summary = "Recupera tutti i documenti pubblici",
        description = "Restituisce una lista completa di documenti pubblici o lista vuota"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documenti Trovati")
    })

    @GetMapping("/public")
    public List<exprivia.it.documenti.model.dto.PublicDocumentDTO> getPublicDocuments() {
        return service.getPublicDocuments();
    }

    //GET /public/{numProtocollo}
    @Operation(
        summary = "Recupera il documento dato il numero di protocollo",
        description = "Restituisce se esistente il Documento"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documenti Trovati"),
        @ApiResponse(responseCode = "404", description = "Nessun documento trovato")
    })
    @GetMapping("/public/{numProtocollo}")
    public ResponseEntity<PublicDocumentDTO> getPublicDocument(@PathVariable String numProtocollo) {
        
        PublicDocumentDTO documento = service.getPublicDocumentByProtocolNumber(numProtocollo);
        
        return ResponseEntity.ok(documento);
    }

    //POST /public
    @Operation(
        summary = "crea un Documento",
        description = "crea il Documento e lo salva nel databse"
    )

    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Documenti Creato con Successo"),
        @ApiResponse(responseCode = "409", description = "Documento già esistente")
    })

    @PostMapping("/public")
    public ResponseEntity<PublicDocumentDTO> createPublicDocument(@Valid @RequestBody PublicDocumentDTO dto) {

        PublicDocumentDTO created = service.createPublicDocument(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
