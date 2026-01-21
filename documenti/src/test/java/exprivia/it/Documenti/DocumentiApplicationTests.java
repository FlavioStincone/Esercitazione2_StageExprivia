package exprivia.it.documenti;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class DocumentiApplicationTests {

    @Autowired
    private DocumentiApplication application;

    @Test
    void contextLoads() {
        assertThat(application).isNotNull();
    }
}
