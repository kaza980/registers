import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import sun.util.resources.cldr.sah.CalendarData_sah_RU;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Kaza on 14.03.2018.
 */
public class Game {

    public enum TypesOfGame{AllPass, Misere, TrickTaking }
    private Player[]players = new Player[3];

    private Card[]deck;
    private ArrayList<Card>talon = new ArrayList<>();
    private Cards.Suits trump;
    public int numberOfEldestHand;
    private Player soloist;
    private TypesOfGame typeOfGame;
    //todo: add progression to cost of trick for all-pass game
    private int costOfTrick=1;
    private int sizeOfPool=20;
    private Player.Contract contract;
    private int necessaryTricks;
    public GameInformation gameInformation;

    PrintWriter fw;

    public Game(){
        players[0] = new Player("Trus");
        players[1] = new Player("Balbes");
        players[2] = new Player("Bivaliy");
        numberOfEldestHand = 0;
        gameInformation = new GameInformation(new String[]{players[0].getName(),players[1].getName(),players[2].getName()});

        try{
           fw = new PrintWriter(new File("log.txt"),"Cp1251");
        } catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    public Player[] getPlayers(){
        return players;
    }

    public void distribution(){

        gameInformation.addRound();

        //обнуляем взятки игроков перед каждым раундом
        for(Player player:players)
            player.setCountOfTrick(0);

        deck=Cards.getDeck();

        players[0].addCard(deck[0]);
        players[0].addCard(deck[1]);

        talon.add(deck[2]);
        talon.add(deck[3]);

        int j=1;
        for(int i=4;i<deck.length;){
            players[j].addCard(deck[i++]);
            players[j].addCard(deck[i++]);
            j++;
            if(j==3) j=0;
        }


        gameInformation.setTalon(talon);
        for(int i=0;i<players.length;i++)
           gameInformation.setCards(players[i].getCards(),i);
        gameInformation.setNumberOfEldestHand(numberOfEldestHand);

        try {
            fw.append("\n\n===========================================\nНовый раунд\n");
            fw.append("В прикупе лежат карты: ");
            for (Card card : talon) {
                fw.append(card.toString() + " ");
            }
            fw.append("\n");

            for (Player player : players) {
                fw.append("Игрок " + player.getName() + " получил карты: ");
                for (Card card : player.getCards()) {
                    fw.append(card.toString() + " ");
                }
                fw.append("\n");
            }
            fw.flush();
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    public void bidding(){
        int maxContract = 0;
        int[] contracts = new int[players.length];
        boolean[] passed = {false, false,false};
        boolean[]allPassed = {true,true,true};
        Random random = new Random();
//todo end bidding if 2 players was passed
        for (int i = numberOfEldestHand; i<players.length;){
            //игрок, который уже пасовал, больше не участвует в торговле
            if(passed[i]) {
                i++;
                if(i==players.length) i=0;
                continue;
            }
             int ran = random.nextInt(15);
             //заявка определяется рандомно - числу от 0 до 8 соответствует пас,
            // от 9 до 12 - повышение текущей заявки, 14 - мизер
             if(ran < 9) {
                 passed[i]=true;
                 gameInformation.addBidInfo("Player "+players[i].getName() +" chose Pass");
             }
             else if (ran > 13) {
                 if(maxContract>Player.Contract.Miser.getPriority()) continue;
                 contracts[i] = Player.Contract.Miser.getPriority();
                 maxContract = Player.Contract.Miser.getPriority();
                 gameInformation.addBidInfo("Player "+players[i].getName() +" chose Miser");
             }
             else {
                 //заявка всегда повышается на 1 шаг
                 maxContract++;
                 contracts[i] = maxContract;
                 gameInformation.addBidInfo("Player "+players[i].getName() +" chose "+Player.findContract(maxContract));
             }
                //если все игроки спасовали, то есть больше не будут повышать заявку,
            //определяем того, у кого была самая большая заявка
                 if(passed[0]&&passed[1]&&passed[2]) {
                     int max = 0;
                     for (int j = 0; j < players.length; j++) {
                         if (contracts[j] > max) {
                             max = contracts[i];
                             soloist = players[i];
                         }
                     }
                     if (max == 0) typeOfGame = TypesOfGame.AllPass;
                     else if (max == Player.Contract.Miser.getPriority()) typeOfGame = TypesOfGame.Misere;
                     else typeOfGame = TypesOfGame.TrickTaking;

                     break;
                 }

             i++;
             if(i==players.length) i=0;
        }
            try {
                fw.append("\nРезультаты торгов: \n");
                fw.append("Тип игры: "+typeOfGame.name());
                if(typeOfGame!=TypesOfGame.AllPass)
                    fw.append("\nИграющий: "+soloist.getName());
                fw.append("\nХод игры: ");
                fw.flush();
            }catch (Exception ioe){
                System.out.println(ioe.getMessage());
            }


        if (typeOfGame==TypesOfGame.TrickTaking){
            addTalon(soloist);
            discard(soloist);
            trickTakingGame(maxContract);
        }
        else if(typeOfGame==TypesOfGame.Misere){
            addTalon(soloist);
            discard(soloist);
            misereGame();
        }
        else if(typeOfGame==TypesOfGame.AllPass){
            allPassGame();
        }

        try {
            fw.append("\n\nПолучено взяток: ");
            for(Player player:players){
                fw.append("\nИгрок "+player.getName()+" - "+player.getCountOfTrick());
            }
            fw.flush();
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }

        gameInformation.setContract(contract, new Player.Types[]{players[0].getType(),players[1].getType(),players[2].getType()});
    }

    private void addTalon(Player player){
        player.addCard(talon.get(0));
        player.addCard(talon.get(1));
        talon.clear();
    }

    private void discard(Player player){
        //todo make it no random
        Random random = new Random();
        try {
            fw.append("\nСброшены карты: ");
            for(int i=0;i<2;i++){
                int randomCard = random.nextInt(player.getCards().size());
                fw.append(player.getCards().get(randomCard).toString()+" ");
                player.getCards().remove(randomCard);
            }
            fw.flush();
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    //todo: add playing normally and playing in the light
    //todo: add greedy whist and gentlemanly whist
    private void trickTakingGame(int maxContract){

        int[] newTricks = new int[players.length];

        soloist.setType(Player.Types.Soloist);
        //todo add contract selection
        //контракт не меняется, берется последняя заявка в торгах
        contract = Player.findContract(maxContract);

        //определить козырь
        if(contract.name().indexOf(Cards.Suits.Clubs.name())>-1)
            trump = Cards.Suits.Clubs;
        else if(contract.name().indexOf(Cards.Suits.Spades.name())>-1)
            trump = Cards.Suits.Spades;
        else if(contract.name().indexOf(Cards.Suits.Diamond.name())>-1)
            trump = Cards.Suits.Diamond;
        else trump = Cards.Suits.Hearts;

        //определить количество взяток
        if(contract.name().indexOf("Six")>-1)
            necessaryTricks = 6;
        else if(contract.name().indexOf("Seven")>-1)
            necessaryTricks = 7;
        else if(contract.name().indexOf("Eight")>-1)
            necessaryTricks = 8;
        else if(contract.name().indexOf("Nine")>-1)
            necessaryTricks = 9;
        else necessaryTricks = 10;

        try {

            fw.append("\nКонтракт: "+contract.name());

        //todo add ability to do wist
        for(Player player:players)
            if(!player.equals(soloist)) {
                player.setType(Player.Types.Passed);
                fw.append("\nИгрок "+player.getName()+" выбрал "+player.getType().name());
            }
        int numberOfGoer = numberOfEldestHand;
        Card[] moves = new Card[players.length];
        Random random = new Random();


            for (int i = 0; i < 10; i++) {
                //первый кидает рандомную карту
                int randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                players[numberOfGoer].getCards().remove(randomCard);

                Cards.Suits goerSuit = moves[numberOfGoer].getSuit();

                //следующие кидают ту же масть
                for (int p = 0; p < players.length - 1; p++) {
                    numberOfGoer++;
                    if (numberOfGoer == players.length) numberOfGoer = 0;
                    for (int j = 0; j < players[numberOfGoer].getCards().size(); j++) {
                        if (players[numberOfGoer].getCards().get(j).getSuit() == goerSuit) {
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(j);
                            players[numberOfGoer].getCards().remove(j);
                            break;
                        } else if (j == players[numberOfGoer].getCards().size() - 1) {
                            //если нет карты такой масти, ищем козырь
                            for (int k = 0; k < players[numberOfGoer].getCards().size(); k++) {
                                if (players[numberOfGoer].getCards().get(j).getSuit() == trump) {
                                    moves[numberOfGoer] = players[numberOfGoer].getCards().get(k);
                                    players[numberOfGoer].getCards().remove(k);
                                    break;
                                } else if (k == players[numberOfGoer].getCards().size() - 1) {
                                    //если нет козыря, кидаем любую
                                    randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                                    moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                                    players[numberOfGoer].getCards().remove(randomCard);
                                }
                            }
                        }
                    }
                }

                //определяем, кто берет взятку, он ходит первым
                numberOfGoer = determineTaker(moves, goerSuit, trump);
                players[numberOfGoer].addTrick();
                newTricks[numberOfGoer]++;

                gameInformation.addGameProcess(moves, players[numberOfGoer].getName());

                fw.append("\nХод №" + i);
                fw.append("\nВыброшены карты: ");
                for (Card card : moves)
                    fw.append(card.toString() + " ");
                fw.append("\nВзятка ушла игроку " + players[numberOfGoer].getName());
                fw.flush();
            }

            gameInformation.setNewTricks(newTricks);
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    private void misereGame(){
        int[] newTricks = new int[players.length];

        soloist.setType(Player.Types.Soloist);
        contract = Player.Contract.Miser;

        for(int i =0;i<players.length;i++)
           if(!players[i].equals(soloist)) players[i].setType(Player.Types.Passed);

        int numberOfGoer = numberOfEldestHand;
        Card[] moves = new Card[players.length];
        Random random = new Random();

        try {
            for (int i = 0; i < 10; i++) {
                //первый кидает рандомную карту
                int randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                players[numberOfGoer].getCards().remove(randomCard);

                Cards.Suits goerSuit = moves[numberOfGoer].getSuit();

                //следующие кидают ту же масть
                for (int p = 0; p < players.length - 1; p++) {
                    numberOfGoer++;
                    if (numberOfGoer == players.length) numberOfGoer = 0;
                    for (int j = 0; j < players[numberOfGoer].getCards().size(); j++) {
                        if (players[numberOfGoer].getCards().get(j).getSuit() == goerSuit) {
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(j);
                            players[numberOfGoer].getCards().remove(j);
                            break;
                        } else if (j == players[numberOfGoer].getCards().size() - 1) {
                            randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                            players[numberOfGoer].getCards().remove(randomCard);
                        }
                    }
                }
                numberOfGoer = determineTaker(moves, goerSuit);
                players[numberOfGoer].addTrick();
                newTricks[numberOfGoer]++;

                gameInformation.addGameProcess(moves, players[numberOfGoer].getName());

                fw.append("\nХод №" + i);
                fw.append("\nВыброшены карты: ");
                for (Card card : moves)
                    fw.append(card.toString() + " ");
                fw.append("\nВзятка ушла игроку " + players[numberOfGoer].getName());
                fw.flush();
            }
            gameInformation.setNewTricks(newTricks);
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    private void allPassGame(){
        int[] newTricks = new int[players.length];

        for(int i =0;i<players.length;i++)
            players[i].setType(Player.Types.Passed);

        contract = Player.Contract.Pass;

        int numberOfGoer = numberOfEldestHand;
        Card[] moves = new Card[players.length];
        Random random = new Random();
        try {
            //разыгрываем прикуп
            for (int i = 0; i < 2; i++) {
                for (int p = 0; p < players.length; p++) {
                    for (int j = 0; j < players[numberOfGoer].getCards().size(); j++) {
                        if (players[numberOfGoer].getCards().get(j).getSuit() == talon.get(0).getSuit()) {
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(j);
                            players[numberOfGoer].getCards().remove(j);
                            break;
                        } else if (j == players[numberOfGoer].getCards().size() - 1) {
                            int randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                            players[numberOfGoer].getCards().remove(randomCard);
                        }
                    }
                    numberOfGoer++;
                    if (numberOfGoer == players.length) numberOfGoer = 0;
                }
                int taker = determineTaker(moves, talon.get(0).getSuit());
                players[taker].addTrick();
                newTricks[taker]++;
                if (i == 1) {
                    numberOfGoer = taker;
                }
                talon.remove(talon.get(0));

                gameInformation.addGameProcess(moves, players[taker].getName());

                fw.append("\nХод №"+i);
                fw.append("\nВыброшены карты: ");
                for(Card card:moves)
                    fw.append(card.toString()+" ");
                fw.append("\nВзятка ушла игроку "+players[numberOfGoer].getName());
            }

            for (int i = 0; i < 8; i++) {
                //первый кидает рандомную карту
                int randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                players[numberOfGoer].getCards().remove(randomCard);

                Cards.Suits goerSuit = moves[numberOfGoer].getSuit();

                //следующие кидают ту же масть
                for (int p = 0; p < players.length - 1; p++) {
                    numberOfGoer++;
                    if (numberOfGoer == players.length) numberOfGoer = 0;
                    for (int j = 0; j < players[numberOfGoer].getCards().size(); j++) {
                        if (players[numberOfGoer].getCards().get(j).getSuit() == goerSuit) {
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(j);
                            players[numberOfGoer].getCards().remove(j);
                            break;
                        }
                        //если масть не найдена, кидаем любую
                        else if (j == players[numberOfGoer].getCards().size() - 1) {
                            randomCard = random.nextInt(players[numberOfGoer].getCards().size());
                            moves[numberOfGoer] = players[numberOfGoer].getCards().get(randomCard);
                            players[numberOfGoer].getCards().remove(randomCard);
                        }
                    }

                }

                numberOfGoer = determineTaker(moves, goerSuit);
                players[numberOfGoer].addTrick();
                newTricks[numberOfGoer]++;

                gameInformation.addGameProcess(moves, players[numberOfGoer].getName());

                fw.append("\nХод №"+(i+2));
                fw.append("\nВыброшены карты: ");
                for(Card card:moves)
                    fw.append(card.toString()+" ");
                fw.append("\nВзятка ушла игроку "+players[numberOfGoer].getName());
                fw.flush();
            }
            gameInformation.setNewTricks(newTricks);
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }

    private int determineTaker(Card[] moves, Cards.Suits goerSuit){
        int taker=0;
        int max = 0;
        for(int i=0;i<moves.length;i++){
            if(moves[i].getSuit()!=goerSuit) continue;
            else if(moves[i].getValue().getPriority()>max){
                max = moves[i].getValue().getPriority();
                taker = i;
            }
        }
        return taker;
    }

    private int determineTaker(Card[] moves, Cards.Suits goerSuit, Cards.Suits trumpSuit){
        int taker = 0;
        int maxGoer = 0;
        int maxTrump = 0;
        for(int i=0;i<moves.length;i++){
            if(moves[i].getSuit()!=goerSuit&&moves[i].getSuit()!=trumpSuit) continue;
            else if(moves[i].getSuit()==trumpSuit)
                if(moves[i].getValue().getPriority()>maxTrump){
                    maxTrump=moves[i].getValue().getPriority();
                    taker=i;
                }
            else if(moves[i].getSuit()==goerSuit)
                if(maxTrump==0&&moves[i].getValue().getPriority()>maxGoer){
                    maxGoer=moves[i].getValue().getPriority();
                    taker=i;
                }
        }
        return taker;
    }

    public void writePoints(){
        Integer[] newPool = new Integer[players.length];
        Integer[] newWhistLeft = new Integer[players.length];
        Integer[] newWhistRight = new Integer[players.length];
        Integer[] newDump = new Integer[players.length];
        Integer[] newTricks;

        if(typeOfGame==TypesOfGame.TrickTaking){
            //todo add countOfPoints for wisters
            if(soloist.getCountOfTrick()>=necessaryTricks) {
                //эта формула соответсвует числу очков за контракт:
                //6-2, 7-4, 8-6, 9-8, 10-10
                soloist.setPoolPoints((necessaryTricks - 5) * 2);

                for(int i=0;i<players.length;i++)
                    if(players[i].getType()== Player.Types.Soloist)
                        newPool[i]=(necessaryTricks - 5) * 2;

            }
            else {
                soloist.setDumpPoints((necessaryTricks-soloist.getCountOfTrick())*costOfTrick);

                for(int i=0;i<players.length;i++)
                    if(players[i].getType()== Player.Types.Soloist)
                        newPool[i]=(necessaryTricks-soloist.getCountOfTrick())*costOfTrick;
            }
           // for(int )
        }
        else if(typeOfGame==TypesOfGame.AllPass){
            int minimalTrick=players[0].getCountOfTrick();
            for(int i=1;i<players.length;i++)
                if(players[i].getCountOfTrick()<minimalTrick)
                    minimalTrick=players[i].getCountOfTrick();
            for(int i=0;i<players.length;i++){
                players[i].setCountOfTrick(players[i].getCountOfTrick()-minimalTrick);
                players[i].setDumpPoints(players[i].getCountOfTrick()*costOfTrick);
            }
        }
        else if(typeOfGame==TypesOfGame.Misere){
            if(soloist.getCountOfTrick()==0)
                soloist.setPoolPoints(10);
            else soloist.setDumpPoints(soloist.getCountOfTrick()*10);
        }

        gameInformation.setTotalPoints(new Integer[]{players[0].getPoolPoints(),players[1].getPoolPoints(),players[2].getPoolPoints()},
                new Integer[]{players[0].getWhistPointsLeft(),players[1].getWhistPointsLeft(),players[2].getWhistPointsLeft()},
                        new Integer[]{players[0].getWhistPointsRight(),players[1].getWhistPointsRight(),players[2].getWhistPointsRight()},
                                new Integer[]{players[0].getDumpPoints(),players[1].getDumpPoints(),players[2].getDumpPoints()},
                                        new Integer[]{players[0].getCountOfTrick(),players[1].getCountOfTrick(),players[2].getCountOfTrick()});

        gameInformation.setResultPoint(countResultPoints());


        try {
            fw.append("\n\nЗаписанные очки: ");
            for(Player player:players){
                fw.append("\nИгрок "+player.getName()+" В горке "+player.getDumpPoints()+" В пуле "+player.getPoolPoints()+" В вистах "+player.getWhistPointsLeft()+", "+player.getWhistPointsRight());
            }
            fw.flush();
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }


    public void getResultPoints(){

        Double[] result = countResultPoints();
        for(int i=0;i<players.length;i++)
            players[i].setResultPoints(result[i]);

        try {
            fw.append("\n\nИтоговые очки: ");
            for(Player player:players){
                fw.append("\nИгрок "+player.getName()+" получил "+player.getResultPoints());
            }
            fw.flush();
        }catch (Exception ioe){
            System.out.println(ioe.getMessage());
        }
    }


    private Double[] countResultPoints(){
        Double[] result = new Double[players.length];
        int[] tempDumpPoint = new int[players.length];

        //пересчитать разность пули и перевести в гору
        int minimalDump=sizeOfPool;
        for(int i=0;i<players.length;i++) {
            if (players[i].getPoolPoints() > sizeOfPool) {
                tempDumpPoint[i] = players[i].getDumpPoints() - (players[i].getPoolPoints() - sizeOfPool);
                if(tempDumpPoint[i]<0)
                    tempDumpPoint[i] = 0;
            } else tempDumpPoint[i]=players[i].getDumpPoints() + (sizeOfPool - players[i].getPoolPoints());
            if(tempDumpPoint[i]<minimalDump)
                minimalDump=tempDumpPoint[i];
        }
        //уменьшаем горы относительно самой маленькой горы
        for(int i=0;i<players.length;i++) {
            tempDumpPoint[i]-=minimalDump;
    }

        //сумма всех гор
        double sumDump=0;
        for (int i=0;i<players.length;i++){
            sumDump+=tempDumpPoint[i];
        }
        //очки относительно суммы очков всех гор
        //возможно итоговое расхождение количества положительных и отрицательынх очков на 1 ед, связанное с округлением
        int averageDump = (int)(sumDump/players.length*10);
        for (int i=0;i<players.length;i++){
            result[i]=(double)averageDump-tempDumpPoint[i]*10;
        }

        //итоговый результат через расчет взаимных вистов
        for(int i=0;i<players.length;i++){
            if(i==0){
                result[i] = result[i]+players[i].getWhistPointsLeft()-players[i+1].getWhistPointsRight();
                result[i] = result[i]+players[i].getWhistPointsRight()-players[players.length-1].getWhistPointsLeft();
            }
            else if(i==players.length-1){
                result[i] = result[i]+players[i].getWhistPointsLeft()-players[0].getWhistPointsRight();
                result[i] = result[i]+players[i].getWhistPointsRight()-players[i-1].getWhistPointsLeft();
            }
            else {
                result[i] = result[i]+players[i].getWhistPointsLeft()-players[i+1].getWhistPointsRight();
                result[i] = result[i]+players[i].getWhistPointsRight()-players[i-1].getWhistPointsLeft();
            }
        }

        return result;
    }
}
