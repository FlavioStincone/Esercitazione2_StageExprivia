package exprivia.it.Documenti.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import exprivia.it.Documenti.model.entity.SecretDocument;

public interface SecretDocumentRepository extends JpaRepository<SecretDocument, Long > {

    Optional<SecretDocument> findByProtocolNumber(String protocolNumber);

    Optional<SecretDocument> deleteByProtocolNumber(String protocolNumber);

    boolean existsByProtocolNumber(String protocolNumber);
}
