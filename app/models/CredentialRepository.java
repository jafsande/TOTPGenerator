package models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.warrenstrange.googleauth.ICredentialRepository;

public class CredentialRepository implements ICredentialRepository{
    
    private Map<String, Credential> credentials = new HashMap<String, Credential>();
    /**
     * This method retrieves the Base32-encoded private key of the given user.
     *
     * @param userName the user whose private key shall be retrieved.
     * @return the private key of the specified user.
     */
    public String getSecretKey(String userName){
        if (credentials.containsKey(userName)) {
            Credential cred = credentials.get(userName);
            return cred.getSecretKey();
        }
        else {
            return null;
        }
    }

    /**
     * This method saves the user credentials.
     *
     * @param userName       the user whose data shall be saved.
     * @param secretKey      the generated key.
     * @param validationCode the validation code.
     * @param scratchCodes   the list of scratch codes.
     */
    public void saveUserCredentials(String userName,
                             String secretKey,
                             int validationCode,
                             List<Integer> scratchCodes){
        Credential cred = new Credential(userName, secretKey, validationCode, scratchCodes);
        this.credentials.put(userName,cred);
    }

}