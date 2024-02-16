import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> CharDataMap;
    
    // The window length used in this model.
    int windowLength;
    
    // The random number generator used by this model. 
	private Random randomGenerator;

    /** Constructs a language model with the given window length and a given
     *  seed value. Generating texts from this model multiple times with the 
     *  same seed value will produce the same random texts. Good for debugging. */
    public LanguageModel(int windowLength, int seed) {
        this.windowLength = windowLength;
        randomGenerator = new Random(seed);
        CharDataMap = new HashMap<String, List>();
    }

    /** Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production. */
    public LanguageModel(int windowLength) {
        this.windowLength = windowLength;
        randomGenerator = new Random();
        CharDataMap = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
	public void train(String fileName) {
		String window = ""; 
        char c; 
        
        In in = new In(fileName);
        for(int i = 1 ; i <= 4 ; i++){
            if(!in.isEmpty()){
                window = window + in.readChar();
            }

        while(!in.isEmpty()){

            c = in.readChar();
            if (CharDataMap.containsKey(window) == true){
            List probs = CharDataMap.get(window);
            probs.update(c);
            }
            else{
            List probs = new List();
            CharDataMap.put(window, probs);
            probs.update(c);
            }
            

            window = window.substring(1);
            window = window + c;
            }

        for( List probs : CharDataMap.values()){
            calculateProbabilities(probs);
        }
        }

	}

    // Computes and sets the probabilities (p and cp fields) of all the
	// characters in the given list. */
	public static void calculateProbabilities(List probs) {				
		double count = 0;
        for(int i = 0 ; i < probs.getSize() ; i++){
            CharData ch = probs.get(i);
            if (ch != null){
                count = count + ch.count;
            }
        }

        double totalCp = 0;
        for(int i = 0 ; i < probs.getSize() ; i++){
            CharData ch = probs.get(i);
            if(ch != null){
            ch.p = (double) (ch.count / count);
            ch.cp = (double) (totalCp + ch.p);
            totalCp = ch.cp;
            }
        }          
        }
	

    // Returns a random character from the given probabilities list.
	public static char getRandomChar(List probs) {
		    double r = (double)(Math.random() * 1) ;
            CharData ch = probs.get(0);
            char toreturn = ch.chr;
            if(ch.cp > r){
                return ch.chr;
            }
            for(int i = 1 ; i < probs.getSize() ; i++){
                ch = probs.get(i);
                if (ch != null){
                    if(ch.cp > r){
                        toreturn = ch.chr;
                        break;
                    }
                }
            }
            return toreturn;
	}

    /**
	 * Generates a random text, based on the probabilities that were learned during training. 
	 * @param initialText - text to start with. If initialText's last substring of size numberOfLetters
	 * doesn't appear as a key in Map, we generate no text and return only the initial text. 
	 * @param numberOfLetters - the size of text to generate
	 * @return the generated text
	 */
	public String generate(String initialText, int textLength) {
		return "";
	}

    /** Returns a string representing the map of this language model. */
	public String toString() {
		StringBuilder str = new StringBuilder();
		for (String key : CharDataMap.keySet()) {
			List keyProbs = CharDataMap.get(key);
			str.append(key + " : " + keyProbs + "\n");
		}
		return str.toString();
	}

    public static void main(String[] args) {
        List newl = new List();
        newl.addFirst(' ');
        newl.addFirst('e');
        newl.addFirst('t');
        newl.addFirst('m');
        newl.addFirst('o');
        newl.addFirst('c');
        newl.update('m');
        newl.update('e');
        calculateProbabilities(newl);
        System.out.println(newl.toString());

		for(int i = 0 ; i < 10 ; i++){
            System.out.println(getRandomChar(newl));
        }
          
    }
}
