package exprivia.it.Documenti.model.entity;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ConfidentialDocument extends Document {

    private String confidentialityReason;

    public ConfidentialDocument(){
        super();
    }

    public ConfidentialDocument(String protocolNumber, String confidentialityReason,  String title, String content, String author, String hashSignature){
        super(protocolNumber, title,content,author,hashSignature);
        this.confidentialityReason = confidentialityReason;
    }
}
