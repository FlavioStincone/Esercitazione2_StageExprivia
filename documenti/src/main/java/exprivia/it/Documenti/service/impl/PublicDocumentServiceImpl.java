package exprivia.it.Documenti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exprivia.it.Documenti.exception.DocumentAlreadyExistsException;
import exprivia.it.Documenti.exception.DocumentNotFoundException;
import exprivia.it.Documenti.mapper.PublicDocumentMapper;
import exprivia.it.Documenti.model.dto.PublicDocumentDTO;
import exprivia.it.Documenti.model.entity.PublicDocument;
import exprivia.it.Documenti.repository.PublicDocumentRepository;
import exprivia.it.Documenti.service.IPublicDocument;

@Service
public class PublicDocumentServiceImpl implements IPublicDocument {

    @Autowired
    private PublicDocumentRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PublicDocumentMapper mapper;

    @Override
    public PublicDocumentDTO createPublicDocument(PublicDocumentDTO dto) {

        if(repository.existsByProtocolNumber(dto.protocolNumber())){ //repository.findByProtocolNumber(entity.getProtocolNumber()).isPresent()
            throw new DocumentAlreadyExistsException("Document with protocol " + dto.protocolNumber() + " alredy exist");
        }

        PublicDocument entity = mapper.toEntity(dto);

        entity.setHashSignature(passwordEncoder.encode(entity.getHashSignature()));

        PublicDocument saved = repository.save(entity);

        return mapper.toDTO(saved);
    }


    @Override
    public PublicDocumentDTO getPublicDocumentByProtocolNumber(String protocolNumber) {

        PublicDocument documentoTrovato = repository.findByProtocolNumber(protocolNumber).orElseThrow(() ->new DocumentNotFoundException( "Document with protocol " + protocolNumber + " not found"));
        documentoTrovato.documentoVisualizzato();
        repository.save(documentoTrovato);
        return mapper.toDTO(documentoTrovato);
    }

    @Override
    public List<PublicDocumentDTO> getPublicDocuments(){

        List<PublicDocumentDTO> documenti = mapper.toListDTO(repository.findAll());

        return documenti;

    }

    
}
