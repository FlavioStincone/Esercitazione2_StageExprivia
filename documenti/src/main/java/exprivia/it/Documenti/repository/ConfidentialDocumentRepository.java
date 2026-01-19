package exprivia.it.Documenti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import exprivia.it.Documenti.model.entity.ConfidentialDocument;

public interface ConfidentialDocumentRepository extends JpaRepository<ConfidentialDocument, Long> {

    //findByProtocolNumber
    Optional<ConfidentialDocument> findByProtocolNumber(String protocolNumber);

    boolean existsByProtocolNumber(String protocolNumber);   
}
