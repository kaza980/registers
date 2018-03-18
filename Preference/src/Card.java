/**
 * Created by Kaza on 13.03.2018.
 */
public class Card {
    private Cards.Suits suit;
    private Cards.Values value;

    public Card(Cards.Suits suit, Cards.Values value){
        this.suit = suit;
        this.value = value;
    }

    public Cards.Suits getSuit(){
        return suit;
    }

    public Cards.Values getValue(){
        return value;
    }

    @Override
    public String toString() {
        return suit.name()+value.name();
    }
}
