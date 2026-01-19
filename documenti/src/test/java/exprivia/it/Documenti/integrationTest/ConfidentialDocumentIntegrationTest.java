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
import exprivia.it.Documenti.model.entity.ConfidentialDocument;
import exprivia.it.Documenti.repository.ConfidentialDocumentRepository;

@SpringBootTest //si carica TUTTO il contesto Spring (Controller, Service, Repo, DB)
@AutoConfigureMockMvc //si configura MockMvc per fare chiamate HTTP fittizie
@ActiveProfiles("test") //si attiva il profilo "test"
@Transactional //dopo ogni test si torna all'origine "rollback" per lasciare pulito
public class ConfidentialDocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ConfidentialDocumentRepository repository;

    @Test
    void should_CreateAndRetrieveDocument_FullFlow() throws Exception {

        String protocolNumber = "DOC001";
        String hashSignature = "Pippo";
        ConfidentialDocumentDTO newDoc = new ConfidentialDocumentDTO(protocolNumber, "confidentialityReason","title", "content", "auth", hashSignature);
        
        mockMvc.perform(post("/document/confidential/{presidentCode}", "PRT45")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoc)))
                .andExpect(status().isCreated());

        ConfidentialDocument documentInDb = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        
        assertEquals("title", documentInDb.getTitle());
        assert(!documentInDb.getHashSignature().equals(hashSignature));

        mockMvc.perform(get("/document/confidential/{numProtocollo}/{codeOrsignature}",protocolNumber,hashSignature)) // ooppure "/document/confidential/DOC001"
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath(("$.protocolNumber")).value(("DOC001")));
                
        var confidentialDOcument = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        assertEquals(confidentialDOcument.getProtocolNumber(), protocolNumber);
    }
    
}
