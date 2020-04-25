package models;

import java.util.List;

public class Credential {
    
    private String userName;
    private String secretKey;
    private int validationCode;
    private List<Integer> scratchCodes;

    public Credential(String userName, String secretKey, int validationCode, List<Integer> scratchCodes) {
        this.userName = userName;
        this.secretKey = secretKey;
        this.validationCode = validationCode;
        this.scratchCodes = scratchCodes;
    }
    
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public List<Integer> getScratchCodes() {
        return scratchCodes;
    }

    public void setScratchCodes(List<Integer> scratchCodes) {
        this.scratchCodes = scratchCodes;
    }

}