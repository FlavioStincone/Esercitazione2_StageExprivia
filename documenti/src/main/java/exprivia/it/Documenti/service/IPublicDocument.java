package exprivia.it.documenti.service;

import java.util.List;

import exprivia.it.documenti.model.dto.PublicDocumentDTO;

public interface IPublicDocument {
    
    PublicDocumentDTO createPublicDocument(PublicDocumentDTO publicDocumentDTO);

    PublicDocumentDTO getPublicDocumentByProtocolNumber(String protocolNumber);

    List<PublicDocumentDTO> getPublicDocuments();
}
