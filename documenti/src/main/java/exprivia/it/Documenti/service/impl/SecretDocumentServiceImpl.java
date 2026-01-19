package exprivia.it.Documenti.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import exprivia.it.Documenti.exception.DocumentAlreadyExistsException;
import exprivia.it.Documenti.exception.DocumentNotFoundException;
import exprivia.it.Documenti.exception.EmergencyProtocolException;
import exprivia.it.Documenti.mapper.SecretDocumentMapper;
import exprivia.it.Documenti.model.dto.SecretDocumentDTO;
import exprivia.it.Documenti.model.entity.SecretDocument;
import exprivia.it.Documenti.model.enums.PresidentCodeEnum;
import exprivia.it.Documenti.repository.SecretDocumentRepository;
import exprivia.it.Documenti.service.ISecreteDocument;

@Service
public class SecretDocumentServiceImpl implements ISecreteDocument {

    @Autowired
    private SecretDocumentRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SecretDocumentMapper mapper;

    @Override
    public SecretDocumentDTO createSecretDocument(SecretDocumentDTO dto, String presidentCode) {
        
        if (!PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new EmergencyProtocolException("Codice presidente non valido, Documento Riservato Violato!");
        }

         if(repository.existsByProtocolNumber(dto.protocolNumber())){ //repository.findByProtocolNumber(entity.getProtocolNumber()).isPresent()
            throw new DocumentAlreadyExistsException("Document with protocol " + dto.protocolNumber() + " alredy exist");
        }
        
        SecretDocument entity = mapper.toEntity(dto);

        entity.setHashSignature(passwordEncoder.encode(entity.getHashSignature()));
        SecretDocument saved = repository.save(entity);

        return mapper.toDTO(saved);

    }

    @Override
    public SecretDocumentDTO getSecretDocumentByNumProtocollo(String protocolNumber, String presidentCode) {

        SecretDocument documentoTrovato = repository.findByProtocolNumber(protocolNumber)
            .orElseThrow(() -> new DocumentNotFoundException("Nessun documento trovato con protocollo: " + protocolNumber));

        if (!PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
        
            // --- PROTOCOLLO D'EMERGENZA ---
            repository.delete(documentoTrovato);
            throw new EmergencyProtocolException("ALLARME: Codice errato! Protocollo d'Emergenza attivato: il documento " + protocolNumber + " è stato distrutto.");
        }
        return mapper.toDTO(documentoTrovato);
    }

    //Seconda Idea, creare un'altra entità ProtocolliDEmenrgenzaAttivati dove mi salvava il documento su cui era stato attivato il protocollo e il numero sbagliato con cui era stato richiesto

    @Override
    public List<SecretDocumentDTO> getSecretDocuments(String presidentCode) {

        if (!PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new EmergencyProtocolException("Codice presidente non valido, Documenti Riservati Violati!");
        }

        List<SecretDocumentDTO> documents = mapper.toListDTO(repository.findAll());

        return documents;
    }
    
}
