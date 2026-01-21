package exprivia.it.documenti.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import exprivia.it.documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.documenti.model.entity.ConfidentialDocument;

@Mapper(componentModel = "spring")
public interface ConfidentialDocumentMapper {

    ConfidentialDocumentDTO toDTO(ConfidentialDocument confidentialDocument);

    @Mapping(target = "id", ignore = true)
    ConfidentialDocument toEntity(ConfidentialDocumentDTO confidentialDocumentDTO);

    List<ConfidentialDocumentDTO> toListDTO(List<ConfidentialDocument> confidentialDocuments);

    List<ConfidentialDocument> toListEntity(List<ConfidentialDocumentDTO> confidentialDocumentDTOs);
}
