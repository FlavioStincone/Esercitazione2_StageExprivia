package exprivia.it.documenti.service;

import java.util.List;

import exprivia.it.documenti.model.dto.SecretDocumentDTO;

public interface ISecreteDocument {

    SecretDocumentDTO createSecretDocument(SecretDocumentDTO secretDocumentDTO, String presidentCode);

    SecretDocumentDTO getSecretDocumentByNumProtocollo(String protocolNumber, String presidentCode);

    List<SecretDocumentDTO> getSecretDocuments(String presidentCode);
    
}
