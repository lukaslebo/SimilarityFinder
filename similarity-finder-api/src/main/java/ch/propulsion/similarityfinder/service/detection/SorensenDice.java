package ch.propulsion.similarityfinder.service.detection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public final class SorensenDice {
	
	private static final int DEFAULT_K = 2;
	private static final boolean REMOVE_SPACES = true;
	private static final Pattern SPACE_REG = Pattern.compile("\\s+");

    private final int k;
    private final String spaceTo;
    
    public SorensenDice(int k, boolean remove_spaces) {
		this.k = k;
		if (remove_spaces) {
			spaceTo = "";
		}
		else {
			spaceTo = " ";
		}
    }
    
    public SorensenDice() {
    		this(DEFAULT_K, REMOVE_SPACES);
    }
    
    public SorensenDice(int k) {
		this(k, REMOVE_SPACES);
    }
    
    public SorensenDice(boolean remove_spaces) {
		this(DEFAULT_K, remove_spaces);
    }
    
    public int getK() {
    		return k;
    }
 
	public final Map<String, Integer> getProfile(final String string) {
        HashMap<String, Integer> profile = new HashMap<String, Integer>();

        String clean_string = SPACE_REG.matcher(string).replaceAll(spaceTo);
        
        for (int i = 0; i < (clean_string.length() - k + 1); i++) {
            String kGram = clean_string.substring(i, i + k);
            Integer old = profile.get(kGram);
            if (old != null) {
                profile.put(kGram, old + 1);
            } else {
                profile.put(kGram, 1);
            }
        }

        return profile;
    }
	
	public final double similarity(final String s1, final String s2) {
        if (s1 == null) {
            throw new NullPointerException("s1 must not be null");
        }

        if (s2 == null) {
            throw new NullPointerException("s2 must not be null");
        }

        if (s1.equals(s2)) {
            return 1;
        }

        Map<String, Integer> profile1 = getProfile(s1);
        Map<String, Integer> profile2 = getProfile(s2);

        Set<String> union = new HashSet<String>();
        union.addAll(profile1.keySet());
        union.addAll(profile2.keySet());

        int intersection = 0;

        for (String key : union) {
            if (profile1.containsKey(key) && profile2.containsKey(key)) {
                intersection++;
            }
        }

        return 2.0 * intersection / (profile1.size() + profile2.size());
    }
	
}
