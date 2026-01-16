package exprivia.it.Documenti.controller;

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

import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.Documenti.service.IConfidentialDocument;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/document")
public class ConfidentialDocumentController {
    
    @Autowired
    private IConfidentialDocument service;

    // GET /confidential/{presidentCode}
    @GetMapping("/confidential/{presidentCode}")
    public List<ConfidentialDocumentDTO> getConfidentialDocuments(@PathVariable String presidentCode) {
        return service.getConfidentialDocuments(presidentCode);
    }

    // GET /confidential/{protocolNumber}/{codeOrsignature}
    @GetMapping("/confidential/{protocolNumber}/{codeOrsignature}")
    public ResponseEntity<ConfidentialDocumentDTO> getConfidentialDocument(@PathVariable String protocolNumber, @PathVariable String codeOrsignature) {
        
        ConfidentialDocumentDTO documento = service.getDocumentByProtocolNumber(protocolNumber,codeOrsignature);
        
        return ResponseEntity.ok(documento);
    }

    // POST /confidential/{presidentCode}
    @PostMapping("/confidential/{presidentCode}")
    public ResponseEntity<ConfidentialDocumentDTO> createConfidentialDocument(@Valid @RequestBody ConfidentialDocumentDTO dto, @PathVariable String presidentCode) {

        ConfidentialDocumentDTO created = service.createConfidentialDocument(dto, presidentCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}