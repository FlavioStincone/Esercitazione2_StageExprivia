package exprivia.it.documenti.service.impl;

import java.util.List;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConfidentialDocumentServiceImpl implements IConfidentialDocument {

    private final ConfidentialDocumentRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final ConfidentialDocumentMapper mapper;

    @Override
    public ConfidentialDocumentDTO createConfidentialDocument( ConfidentialDocumentDTO dto,String presidentCode) {

        if (presidentCode == null || presidentCode.isBlank() || !PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new InvalidPresidentCodeException("Codice presidente non valido, Documento Riservato Violato!");
        }

        if (repository.existsByProtocolNumber(dto.protocolNumber())) {
            throw new DocumentAlreadyExistsException("Document with protocol " + dto.protocolNumber() + " already exist");
        }

        ConfidentialDocument entity = mapper.toEntity(dto);
        entity.setHashSignature(passwordEncoder.encode(entity.getHashSignature()));

        ConfidentialDocument saved = repository.save(entity);
        return mapper.toDTO(saved);
    }

    @Override
    public ConfidentialDocumentDTO getDocumentByProtocolNumber( String protocolNumber,String codeOrSignature) {

        ConfidentialDocument document = repository.findByProtocolNumber(protocolNumber)
            .orElseThrow(() -> new DocumentNotFoundException("Document with protocol " + protocolNumber + " not found"));

        boolean validSignature = passwordEncoder.matches(codeOrSignature,document.getHashSignature());

        boolean validPresidentCode = PresidentCodeEnum.existsCode(codeOrSignature.toUpperCase());

        if (validSignature || validPresidentCode) {
            return mapper.toDTO(document);
        }

        throw new DocumentNotFoundException("Firma o codice Errato, Documento Riservato Violato!");
    }

    @Override
    public List<ConfidentialDocumentDTO> getConfidentialDocuments(
            String presidentCode) {

        if (presidentCode == null || presidentCode.isBlank() || !PresidentCodeEnum.existsCode(presidentCode.toUpperCase())) {
            throw new InvalidPresidentCodeException("Codice presidente non valido, Documento Riservato Violato!");
        }

        return mapper.toListDTO(repository.findAll());
    }
}
