import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Kaza on 13.03.2018.
 */
public class Cards {
    public enum Suits {Diamond,Clubs,Hearts, Spades};
    public enum Values {Seven(1), Eight(2), Nine(3), Ten(4), Jack(5), Queen(6), King(7), Ace(8);
        int priority;
        Values(int p) {
            priority = p;
        }
        int getPriority() {
            return priority;
        } };

    private static Card[] deck = new Card[]{
            new Card(Suits.Spades,Values.Seven),new Card(Suits.Spades,Values.Eight),new Card(Suits.Spades,Values.Nine),new Card(Suits.Spades,Values.Ten),new Card(Suits.Spades,Values.Jack),new Card(Suits.Spades,Values.Queen),new Card(Suits.Spades,Values.King),new Card(Suits.Spades,Values.Ace),
            new Card(Suits.Clubs,Values.Seven),new Card(Suits.Clubs,Values.Eight),new Card(Suits.Clubs,Values.Nine),new Card(Suits.Clubs,Values.Ten),new Card(Suits.Clubs,Values.Jack),new Card(Suits.Clubs,Values.Queen),new Card(Suits.Clubs,Values.King),new Card(Suits.Clubs,Values.Ace),
            new Card(Suits.Diamond,Values.Seven),new Card(Suits.Diamond,Values.Eight),new Card(Suits.Diamond,Values.Nine),new Card(Suits.Diamond,Values.Ten),new Card(Suits.Diamond,Values.Jack),new Card(Suits.Diamond,Values.Queen),new Card(Suits.Diamond,Values.King),new Card(Suits.Diamond,Values.Ace),
            new Card(Suits.Hearts,Values.Seven),new Card(Suits.Hearts,Values.Eight),new Card(Suits.Hearts,Values.Nine),new Card(Suits.Hearts,Values.Ten),new Card(Suits.Hearts,Values.Jack),new Card(Suits.Hearts,Values.Queen),new Card(Suits.Hearts,Values.King),new Card(Suits.Hearts,Values.Ace)
    };

    public static Card[] getDeck(){
        shuffleArray(deck);
        return deck;
    }

    private static void shuffleArray(Card[] d) {
        int n = d.length;
        Random random = new Random();
        random.nextInt();
        for (int i = 0; i < n; i++) {
            int change = i + random.nextInt(n - i);
            swap(d, i, change);
        }
    }

    private static void swap(Card[] d, int i, int change) {
        Card temp = d[i];
        d[i] = d[change];
        d[change] = temp;
    }
}
