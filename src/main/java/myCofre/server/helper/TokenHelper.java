package myCofre.server.helper;

import java.security.SecureRandom;

public class TokenHelper {

    private static final String charList = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom random = new SecureRandom();

    public static String generateHumanToken(int length) {
        StringBuilder code = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(charList.length());
            code.append(charList.charAt(index));
        }
        return code.toString();
    }

}
