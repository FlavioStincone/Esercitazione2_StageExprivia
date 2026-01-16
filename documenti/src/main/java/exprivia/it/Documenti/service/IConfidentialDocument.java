package exprivia.it.Documenti.service;

import java.util.List;

import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;

public interface IConfidentialDocument {

    ConfidentialDocumentDTO createConfidentialDocument(ConfidentialDocumentDTO confidentialDocumentDTO, String presidentCode);

    ConfidentialDocumentDTO getDocumentByProtocolNumber(String protocolNumber, String firmaHash);

    List<ConfidentialDocumentDTO> getConfidentialDocuments(String presidentCode);
    
}
