package exprivia.it.documenti.model.enums;

public enum PresidentCodeEnum {

    PresidentTrump("PRT45"),
    PresidentBiden("PRB46"),
    PresidentObama("PRO44"),
    PresidentLincoln("PRL16"),
    PresidentWashington("PRW01");
    
    private final String code;
    
    PresidentCodeEnum(String description) {
        this.code = description;
    }
    
    public static boolean existsCode(String code) {
        for (PresidentCodeEnum prCode : PresidentCodeEnum.values()) {
            if (prCode.code.equals(code)) {
                return true;
            }
        }
        return false;
    }

    
    public String getCode() {
        return code;
    }
}
