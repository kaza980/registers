import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Created by Kaza on 18.03.2018.
 */
public class GameInformation {

    //todo: try using GameDTO
    private String[] players;

    private ArrayList<RoundInformation> roundsInfo = new ArrayList<>();
    private PrintWriter infoPW;

    public GameInformation(String[]playersName){
        this.players = playersName;
        try{
            infoPW = new PrintWriter(new File("roundsInfo.txt"),"Cp1251");
        } catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    public void addRound(){
        roundsInfo.add(new RoundInformation());
    }

    public void setTalon(ArrayList<Card> cards){
        roundsInfo.get(roundsInfo.size()-1).setTalon(cards);
    }

    public void setCards(ArrayList<Card> cards, int numnerOfPlayers){
        roundsInfo.get(roundsInfo.size()-1).setCards(cards,numnerOfPlayers);
    }

    public void setNumberOfEldestHand(int numberOfEldestHand){
        roundsInfo.get(roundsInfo.size()-1).setNumberOfEldestHand(numberOfEldestHand);
    }

    public void addBidInfo(String info){
        roundsInfo.get(roundsInfo.size()-1).addBidInfo(info);
    }

    public void setContract(Player.Contract contract, Player.Types[]types){
        roundsInfo.get(roundsInfo.size()-1).setContract(contract, types);
    }

    public void addGameProcess(Card[] moves, String taker) {
        roundsInfo.get(roundsInfo.size()-1).addGameProcess(moves, taker);
    }

    public void setTotalPoints(Integer[] pool, Integer[] whistLeft, Integer[] whistRight, Integer[] dump, Integer[] tricks) {
        roundsInfo.get(roundsInfo.size()-1).setTotalPoint(pool, whistLeft,whistRight, dump, tricks);
    }

    public void setResultPoint(Double[] resultPoint) {
        roundsInfo.get(roundsInfo.size()-1).setResultPoints(resultPoint);
    }

    public void setNewTricks(int[] newTricks) {
        roundsInfo.get(roundsInfo.size()-1).setNewTricks(newTricks);
    }

    //API1
    public void getDistributionInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("DistributionInfo, round "+round+":");
            infoPW.append("\nTalon: ");
            for (String str: roundsInfo.get(round).getTalon())
                infoPW.append(str+" ");
            infoPW.append("\nCards: ");
            for(int i=0;i<players.length;i++) {
                infoPW.append("\n"+players[i]+": ");
                for (String str : roundsInfo.get(round).getCards(i))
                    infoPW.append(str + " ");
            }
            infoPW.append("\nEldest hand: "+players[roundsInfo.get(round).getNumberOfEldestHand()]);
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }

    //API2
    public void getBiddingInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("BiddingInfo, round "+round+":\n");
            for (String str: roundsInfo.get(round).getBidInfo())
                infoPW.append(str+"\n");
            infoPW.append("Talon: ");
            for (String str: roundsInfo.get(round).getTalon())
                infoPW.append(str+" ");
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }

    //API3
    public void getContractInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("ContractInfo, round "+round+":");
            if(roundsInfo.get(round).getContract()!= Player.Contract.Pass){
                infoPW.append("\nContract: "+roundsInfo.get(round).getContract().name());
                for(int i=0;i<players.length;i++)
                    infoPW.append("\nPlayer "+players[i]+" plays like "+roundsInfo.get(round).getTypeofPlayer(i));
            }
            else infoPW.append("\nAll players passed");

            infoPW.append("\n\n");
            infoPW.flush();
        }
    }

    //API4
    public void getGameProcessInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("GameProccessInfo, round "+round+":");
            for(int i=0;i<roundsInfo.get(round).getMoves().size();i++){
                infoPW.append("\nMove №"+i+": ");
                for(int j=0;j<roundsInfo.get(round).getMoves().get(i).length;j++)
                    infoPW.append(roundsInfo.get(round).getMoves().get(i)[j]+" ");
                infoPW.append("\nTaker: "+roundsInfo.get(round).getTaker(i));
            }
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }


    //API7
    public void getTotalPointsInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("TotalPointsInfo, round "+round+":");
            for(int i=0;i<players.length;i++)
                infoPW.append("\nPlayer "+players[i]+ " Pool: "+roundsInfo.get(round).getTotalPoolPoints(i)+
                " Whist left: "+roundsInfo.get(round).getTotalWhistPointsLeft(i)+" Whist right: "+roundsInfo.get(round).getTotalWhistPointsRight(i)+
                " Dump: "+roundsInfo.get(round).getTotalDumpPoints(i)+" Tricks: "+roundsInfo.get(round).getTotalTricks(i));
            
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }

    //API8
    public void getResultPointsInfo(int round, String name) {
        if(round<roundsInfo.size()){
            infoPW.append("ResultPointInfo, round "+round+", player "+name+":");
            int id=0;
            for(int i=0;i<players.length;i++)
                if(players[i].equals(name))
                    id=i;
            infoPW.append("\nPoints: "+roundsInfo.get(round).getResultPoints(id));
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }

    //API10
    public void getResultPointsInfo(int round) {
        if(round<roundsInfo.size()){
            infoPW.append("AllResultPointInfo, round "+round+":");
            for(int i=0;i<players.length;i++)
                infoPW.append("\nPoints of player "+players[i]+": "+roundsInfo.get(round).getResultPoints()[i]);
            infoPW.append("\n\n");
            infoPW.flush();
        }
    }




    /*данные хранятся в виде строк, а не объектов
* этого достаточно, если цель методов - лишь хранить описание хода игры*/
    private class RoundInformation {

        //раздача
        private ArrayList<String>[] cards = new ArrayList[GameInformation.this.players.length];
        private ArrayList<String> talon = new ArrayList<>();
        private Integer numberOfEldestHand;

        //торговля
        private ArrayList<String> bid = new ArrayList<>();

        //заявка игрока
        private Player.Contract contract;
        private Player.Types[] typeOfPlayer;

        //процесс розыгрыша
        private ArrayList<String[]> moves = new ArrayList<>();
        private ArrayList<String> takers = new ArrayList<>();

        //результат розыгрыша
        private Integer[] newTricks;
        private Integer[] newPoolPoints;
        private Integer[] newWhistPointsLeft;
        private Integer[] newWhistPointsRight;
        private Integer[] newDumpPoints;

        //текущее состояние очков
        private Integer[] totalTricks;
        private Integer[] totalPoolPoints;
        private Integer[] totalWhistPointsLeft;
        private Integer[] totalWhistPointsRight;
        private Integer[] totalDumpPoints;
        private Double[] resultPoints;

        //статистика по игроку
        private String[] typeOfGame;
        private Integer[] successfulMisere;
        private Integer[] successfulAllPass;
        private Integer[] successfulContract;

        public void setTalon(ArrayList<Card> cards){
            for(Card card: cards)
                talon.add(card.toString());
        }

        public ArrayList<String> getTalon(){
            return talon;
        }

        public void setCards(ArrayList<Card> cards, int numberOfPlayer){
            this.cards[numberOfPlayer] = new ArrayList<>();
            for(Card card: cards)
                this.cards[numberOfPlayer].add(card.toString());
        }

        public ArrayList<String> getCards(int numberOfPlayer){
            return cards[numberOfPlayer];
        }

        public void setNumberOfEldestHand(int numberOfEldestHand){
            this.numberOfEldestHand = numberOfEldestHand;
        }

        public int getNumberOfEldestHand(){
            return numberOfEldestHand;
        }

        public void addBidInfo(String info){
            bid.add(info);
        }

        public ArrayList<String> getBidInfo(){
            return bid;
        }

        public void setContract(Player.Contract contract, Player.Types[] types) {
            this.contract = contract;
            typeOfPlayer = new Player.Types[types.length];
            for(int i = 0;i<types.length;i++)
                typeOfPlayer[i]  = types[i];
        }

        public Player.Contract getContract(){
            return contract;
        }

        public Player.Types getTypeofPlayer(int numberOfPlayer){
            return typeOfPlayer[numberOfPlayer];
        }

        public void addGameProcess(Card[] moves, String taker) {
            String[] m = new String[moves.length];
            for(int i=0;i<moves.length;i++)
                m[i]=moves[i].toString();
            this.moves.add(m);
            this.takers.add(taker);
        }

        public ArrayList<String[]> getMoves() {
            return moves;
        }

        public String getTaker(int i) {
            return takers.get(i);
        }

        public void setTotalPoint(Integer[] pool, Integer[] whistLeft, Integer[] whistRight, Integer[] dump, Integer[] tricks) {
            totalPoolPoints = new Integer[pool.length];
            totalWhistPointsLeft = new Integer[pool.length];
            totalWhistPointsRight = new Integer[pool.length];
            totalDumpPoints = new Integer[pool.length];
            totalTricks = new Integer[pool.length];
            for(int i=0;i<pool.length;i++){
                totalPoolPoints[i] = pool[i];
                totalWhistPointsLeft[i] = whistLeft[i];
                totalWhistPointsRight[i] = whistRight[i];
                totalDumpPoints[i] = dump[i];
                totalTricks[i] = tricks[i];
            }
        }

        public Integer getTotalPoolPoints(int i) {
            return totalPoolPoints[i];
        }

        public Integer getTotalWhistPointsLeft(int i) {
            return totalWhistPointsLeft[i];
        }

        public Integer getTotalWhistPointsRight(int i) {
            return totalWhistPointsRight[i];
        }

        public Integer getTotalDumpPoints(int i) {
            return totalDumpPoints[i];
        }

        public Integer getTotalTricks(int i) {
            return totalTricks[i];
        }

        public void setResultPoints(Double[] resultPoints) {
            this.resultPoints = new Double[resultPoints.length];
            for(int i=0;i<resultPoints.length;i++)
                this.resultPoints[i]=resultPoints[i];
        }

        public Double getResultPoints(int id) {
            return resultPoints[id];
        }

        public Double[] getResultPoints() {
            return resultPoints;
        }

        public void setNewTricks(int[] newTricks) {
            this.newTricks = new Integer[newTricks.length];
            for(int i=0;i<newTricks.length;i++)
                this.newTricks[i] = newTricks [i];
        }
    }
}
