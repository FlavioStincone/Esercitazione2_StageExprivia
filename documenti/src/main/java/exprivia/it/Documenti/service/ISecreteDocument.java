package exprivia.it.Documenti.service;

import java.util.List;

import exprivia.it.Documenti.model.dto.SecretDocumentDTO;

public interface ISecreteDocument {

    SecretDocumentDTO createSecretDocument(SecretDocumentDTO secretDocumentDTO, String presidentCode);

    SecretDocumentDTO getSecretDocumentByNumProtocollo(String protocolNumber, String presidentCode);

    List<SecretDocumentDTO> getSecretDocuments(String presidentCode);
    
}
