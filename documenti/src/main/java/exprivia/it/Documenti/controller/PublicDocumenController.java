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

import exprivia.it.Documenti.model.dto.PublicDocumentDTO;
import exprivia.it.Documenti.service.IPublicDocument;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/document")
public class PublicDocumenController {

    @Autowired
    private IPublicDocument service;

    //GET /public
    @GetMapping("/public")
    public List<exprivia.it.Documenti.model.dto.PublicDocumentDTO> getPublicDocuments() {
        return service.getPublicDocuments();
    }

    //GET /public/{numProtocollo}
    @GetMapping("/public/{numProtocollo}")
    public ResponseEntity<PublicDocumentDTO> getPublicDocument(@PathVariable String numProtocollo) {
        
        PublicDocumentDTO documento = service.getPublicDocumentByProtocolNumber(numProtocollo);
        
        return ResponseEntity.ok(documento);
    }

    //POST /public
    @PostMapping("/public")
    public ResponseEntity<PublicDocumentDTO> createPublicDocument(@Valid @RequestBody PublicDocumentDTO dto) {

        PublicDocumentDTO created = service.createPublicDocument(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

}
