package exprivia.it.Documenti.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.Documenti.model.entity.ConfidentialDocument;

/*
    FIXME: Field mapper in exprivia.it.Documenti.service.impl.ConfidentialDocumentServiceImpl required a bean of type 'exprivia.it.Documenti.mapper.ConfidentialDocumentMapper' that could not be found.

    The injection point has the following annotations:
        - @org.springframework.beans.factory.annotation.Autowired(required=true)


    Action:

    Consider defining a bean of type 'exprivia.it.Documenti.mapper.ConfidentialDocumentMapper' in your configuration.
*/

@Mapper(componentModel = "spring")
public interface ConfidentialDocumentMapper {
    //Documento Riservato
    ConfidentialDocumentDTO toDTO(ConfidentialDocument confidentialDocument);

    @Mapping(target = "id", ignore = true)
    ConfidentialDocument toEntity(ConfidentialDocumentDTO confidentialDocumentDTO);

    List<ConfidentialDocumentDTO> toListDTO(List<ConfidentialDocument> confidentialDocuments);

    @Mapping(target = "id", ignore = true)
    List<ConfidentialDocument> toListEntity(List<ConfidentialDocumentDTO> confidentialDocumentDTOs);
    
}
