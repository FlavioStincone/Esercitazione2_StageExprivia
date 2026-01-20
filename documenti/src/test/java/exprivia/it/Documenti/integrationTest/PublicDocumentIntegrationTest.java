package exprivia.it.documenti.integrationTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import exprivia.it.documenti.model.dto.PublicDocumentDTO;
import exprivia.it.documenti.model.entity.PublicDocument;
import exprivia.it.documenti.repository.PublicDocumentRepository;

@SpringBootTest //si carica TUTTO il contesto Spring (Controller, Service, Repo, DB)
@AutoConfigureMockMvc //si configura MockMvc per fare chiamate HTTP fittizie
@ActiveProfiles("test") //si attiva il profilo "test"
@Transactional //dopo ogni test si torna all'origine "rollback" per lasciare pulito
class PublicDocumentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PublicDocumentRepository repository;

    @Test
    void should_CreateAndRetrieveDocument_FullFlow() throws Exception {

        String protocolNumber = "DOC001";
        PublicDocumentDTO newDoc = new PublicDocumentDTO(protocolNumber, "title", "content", "auth", "clearPass");
        
        mockMvc.perform(post("/document/public")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newDoc)))
                .andExpect(status().isCreated());

        PublicDocument documentInDb = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        
        assertEquals("title", documentInDb.getTitle());
        assert(!documentInDb.getHashSignature().equals("clearPass"));

        mockMvc.perform(get("/document/public/{numProtocollo}",protocolNumber)) // ooppure "/document/public/DOC001"
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.protocolNumber").value("DOC001"));
                
        var documentAfterView = repository.findByProtocolNumber(protocolNumber).orElseThrow();
        assertEquals(1, documentAfterView.getViewsNumber());
    }
}