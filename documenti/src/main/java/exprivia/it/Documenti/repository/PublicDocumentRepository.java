package exprivia.it.Documenti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import exprivia.it.Documenti.model.entity.PublicDocument;

public interface PublicDocumentRepository extends JpaRepository<PublicDocument, Long> {

    //findByProtocolNumber
    Optional<PublicDocument> findByProtocolNumber(String protocolNumber);
}
