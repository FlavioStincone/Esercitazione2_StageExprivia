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

import exprivia.it.Documenti.model.dto.SecretDocumentDTO;
import exprivia.it.Documenti.service.ISecreteDocument;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/document")
public class SecretDocumentController {

    @Autowired
    private ISecreteDocument service;

    //GET /secret/{presidentCode}
    @GetMapping("/secret/{presidentCode}")
    public List<SecretDocumentDTO> getSecretDocuments(@PathVariable String presidentCode) {
        return service.getSecretDocuments(presidentCode);
    }

    //GET /secret/{numProtocollo}/{presidentCode}
    @GetMapping("/secret/{numProtocollo}/{presidentCode}")
    public ResponseEntity<SecretDocumentDTO> getConfidentialDocument(@PathVariable String numProtocollo, @PathVariable String presidentCode) {
        
        SecretDocumentDTO documento = service.getSecretDocumentByNumProtocollo(numProtocollo, presidentCode);
        
        return ResponseEntity.ok(documento);
    }

    //POST /secret/{presidentCode}
    @PostMapping("/secret/{presidentCode}")
    public ResponseEntity<SecretDocumentDTO> createConfidentialDocument(@Valid @RequestBody SecretDocumentDTO dto, @PathVariable String presidentCode) {

        SecretDocumentDTO created = service.createSecretDocument(dto, presidentCode);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
}