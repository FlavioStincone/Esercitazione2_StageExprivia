package exprivia.it.Documenti.integrationTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import exprivia.it.Documenti.model.dto.ConfidentialDocumentDTO;
import exprivia.it.Documenti.model.entity.SecretDocument;
import exprivia.it.Documenti.repository.SecretDocumentRepository;

@SpringBootTest //si carica TUTTO il contesto Spring (Controller, Service, Repo, DB)
@AutoConfigureMockMvc //si configura MockMvc per fare chiamate HTTP fittizie
@ActiveProfiles("test") //si attiva il profilo "test"
@Transactional //dopo ogni test si torna all'origine "rollback" per lasciare pulito
public class SecretDocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecretDocumentRepository repository;

    @Test
    void should_CreateAndRetrieveDocument_FullFlow() throws Exception {

        String protocolNumber = "DOC001";
        String presidentCode = "PRT45";

        ConfidentialDocumentDTO newDoc = new ConfidentialDocumentDTO(protocolNumber, "confidentialityReason","title", "content", "auth", "hashSignature");
        
        mockMvc.perform(post("/document/secret/{presidentCode}", presidentCode)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoc)))
                .andExpect(status().isCreated());

        SecretDocument documentInDb = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        
        assertEquals("title", documentInDb.getTitle());
        assert(!documentInDb.getHashSignature().equals("hashSignature"));

        mockMvc.perform(get("/document/secret/{numProtocollo}/{presidentCode}",protocolNumber,presidentCode)) // ooppure "/document/secret/DOC001/PRT45"
                .andExpect(status().isOk())
                .andExpect(jsonPath(("$.title")).value(("title")))
                .andExpect(jsonPath(("$.protocolNumber")).value(("DOC001")));
                
        var secretDocument = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        assertEquals(secretDocument.getProtocolNumber(), protocolNumber);
    }
    
}
