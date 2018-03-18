public class Main {
        public static void main(String[] args) {
            Game game = new Game();
            //todo add ability to select condition of game end
            for(int i=0;i<12;i++){
                game.distribution();
                game.bidding();
                game.writePoints();
                game.numberOfEldestHand++;
                if(game.numberOfEldestHand==game.getPlayers().length) game.numberOfEldestHand=0;
            }
            game.getResultPoints();

            game.gameInformation.getDistributionInfo(4);
            game.gameInformation.getBiddingInfo(10);
            game.gameInformation.getContractInfo(8);
            game.gameInformation.getGameProcessInfo(1);
            game.gameInformation.getResultPointsInfo(7);
            game.gameInformation.getTotalPointsInfo(3);
            game.gameInformation.getResultPointsInfo(0, game.getPlayers()[1].getName());
    }
}
