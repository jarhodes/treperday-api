package app.treperday.api.domain.user;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomPasswordGenerator {

	private final SecureRandom secureRandom = new SecureRandom();
	
	private List<Character> charList;
	
	private String randomPassword;
	
	public RandomPasswordGenerator() {
		
		charList = new ArrayList<>();
		
		// Adding ASCII values between 33 and 126
        for (int i = 33; i < 127; i++) {
            charList.add((char) i);
        }
        
        Collections.rotate(charList, 5);
        
        StringBuilder stringBuilder = new StringBuilder();
        
        for (int length = 0; length < 32; length++) {
        	char passwordChar = charList.get(secureRandom.nextInt(charList.size()));
        	stringBuilder.append(passwordChar);
        }
        
        randomPassword = stringBuilder.toString();
	}

	public String getRandomPassword() {
		return randomPassword;
	}
	
	
	
}
