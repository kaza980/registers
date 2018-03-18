import java.util.ArrayList;

/**
 * Created by Kaza on 13.03.2018.
 */
public class Player {
    private String name;
    private ArrayList<Card> cards;
    private Types type;
    public enum Types{Soloist, Passed, Whister};
    //Diamond,Clubs,Hearts, Spades
    public enum Contract{Pass(0), Miser(13),
        SixSpades(1), SixClubs(2),SixDiamond(3),SixHearts(4),
        SevenSpades(5), SevenClubs(6),SevenDiamond(7),SevenHearts(8),
        EightSpades(9), EightClubs(10),EightDiamond(11),EightHearts(12),
        NineSpades(14), NineClubs(15),NineDiamond(16),NineHearts(17),
        TenSpades(18), TenClubs(19),TenDiamond(20),TenHearts(21);
        int priority;
        Contract(int p) {
            priority = p;
        }
        int getPriority() {
            return priority;
        }
};
    public static Contract findContract(int priority){
        for(Contract contract: Contract.values()){
            if(contract.getPriority()==priority) return contract;
        }
        return null;
    }

    private int countOfTrick;

    private int poolPoints;
    private int whistPointsLeft;
    private int whistPointsRight;
    private int dumpPoints;

    private double resultPoints;

    //todo: add strategy

    public Player(String name){
        this.name=name;
        cards = new ArrayList<>();
        countOfTrick = 0;
        poolPoints = 0;
        whistPointsRight = 0;
        whistPointsLeft = 0;
        dumpPoints = 0;
        resultPoints = 0;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }

    public ArrayList<Card> getCards(){
        return cards;
    }

    public void setCards(ArrayList<Card> cards){
        this.cards = cards;
    }

    public Card getCard(int index){
        return cards.get(index);
    }

    public void addCard(Card card){
        cards.add(card);
    }

    public int getCountOfTrick(){
        return countOfTrick;
    }

    public void setCountOfTrick(int count){
        countOfTrick = count;
    }

    public void addTrick(){
        this.countOfTrick++;
    }

    public int getPoolPoints(){
        return poolPoints;
    }

    public void setPoolPoints(int poolPoints){
        this.poolPoints += poolPoints;
    }

    public int getWhistPointsLeft(){
        return whistPointsLeft;
    }

    public void setWhistPointsLeft(int whistPoints){
        this.whistPointsLeft += whistPoints;
    }

    public int getWhistPointsRight(){
        return whistPointsRight;
    }

    public void setWhistPointsRight(int whistPoints){
        this.whistPointsRight += whistPoints;
    }

    public int getDumpPoints(){
        return dumpPoints;
    }

    public void setDumpPoints(int dumpPoints){
        this.dumpPoints += dumpPoints;
    }

    public double getResultPoints(){
        return resultPoints;
    }

    public void setResultPoints(double resultPoints){
        this.resultPoints = resultPoints;
    }

    public Types getType(){
        return type;
    }

    public void setType(Types type){
        this.type = type;
    }
}
