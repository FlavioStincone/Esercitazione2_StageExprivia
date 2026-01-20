package exprivia.it.documenti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exprivia.it.documenti.exception.DocumentAlreadyExistsException;
import exprivia.it.documenti.exception.DocumentNotFoundException;
import exprivia.it.documenti.exception.InvalidPresidentCodeException;
import exprivia.it.documenti.mapper.ConfidentialDocumentMapper;
import exprivia.it.documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.documenti.model.entity.ConfidentialDocument;
import exprivia.it.documenti.model.enums.PresidentCodeEnum;
import exprivia.it.documenti.repository.ConfidentialDocumentRepository;
import exprivia.it.documenti.service.IConfidentialDocument;


@Service
public class ConfidentialDocumentServiceImpl implements IConfidentialDocument {

    @Autowired
    private ConfidentialDocumentRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ConfidentialDocumentMapper mapper;
    
   @Override
    public ConfidentialDocumentDTO createConfidentialDocument(ConfidentialDocumentDTO dto, String presidentCode) {
        if (!PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new InvalidPresidentCodeException("Codice presidente non valido, Documento Riservato Violato!");
        }

        if(repository.existsByProtocolNumber(dto.protocolNumber())){ //repository.findByProtocolNumber(entity.getProtocolNumber()).isPresent()
            throw new DocumentAlreadyExistsException("Document with protocol " + dto.protocolNumber() + " alredy exist");
        }
        
        ConfidentialDocument entity = mapper.toEntity(dto);
        
        entity.setHashSignature(passwordEncoder.encode(entity.getHashSignature()));
        ConfidentialDocument saved = repository.save(entity);

        return mapper.toDTO(saved);
    }


   @Override
    public ConfidentialDocumentDTO getDocumentByProtocolNumber(String protocolNumber, String codeOrsignature) {
        
        ConfidentialDocument documentoTrovato = repository.findByProtocolNumber(protocolNumber)
             .orElseThrow(() -> new DocumentNotFoundException("Document with protocol " + protocolNumber + " not found"));

        if(passwordEncoder.matches(codeOrsignature, documentoTrovato.getHashSignature()) || PresidentCodeEnum.existsCode(codeOrsignature.toUpperCase())){
            return mapper.toDTO(documentoTrovato);
        } else {
            throw new DocumentNotFoundException("Firma o codice Errato, Documento Riservato Violato!");
        }
    }

    @Override
    public List<ConfidentialDocumentDTO> getConfidentialDocuments(String presidentCode) {
        if (!PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new InvalidPresidentCodeException("Codice presidente non valido, Documento Riservato Violato!");
        }

        List<ConfidentialDocumentDTO> documenti = mapper.toListDTO(repository.findAll());

        return documenti;
    }
    
}
