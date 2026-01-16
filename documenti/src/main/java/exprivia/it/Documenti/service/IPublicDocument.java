package exprivia.it.Documenti.service;

import java.util.List;

import exprivia.it.Documenti.model.dto.PublicDocumentDTO;

public interface IPublicDocument {
    
    PublicDocumentDTO createPublicDocument(PublicDocumentDTO publicDocumentDTO);

    PublicDocumentDTO getPublicDocumentByProtocolNumber(String protocolNumber);

    List<PublicDocumentDTO> getPublicDocuments();
}
