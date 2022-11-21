package bridge;

import java.util.ArrayList;
import java.util.List;

public class Application {

    static InputView inputView = new InputView();
    
    static BridgeGame bridgeGame = new BridgeGame();
    static OutputView outputView = new OutputView();

    public static void main(String[] args) {
        

        System.out.println("다리 건너기 게임을 시작합니다");

        int bridgeSize = inputView.readBridgeSize();

        BridgeMaker bridgeMaker = new BridgeMaker(new BridgeRandomNumberGenerator());

        List<String> bridge = bridgeMaker.makeBridge(bridgeSize);

        int gameCount = 0;

        while (true) {
            gameCount++;
            Bridge userBridge = new Bridge(new ArrayList<>(), new ArrayList<>());
            PlayerStatus playerStatus = null;

            for (String s : bridge) {

                playerStatus = isMatchingWithBridge(userBridge, s);

                playerMove(userBridge, playerStatus);

                if (!playerStatus.isMatchingFlag()) {
                    break;
                }
            }

            if (finishGamePlayerWin(gameCount, userBridge, playerStatus)) {
                break;
            }

            if (finishGamePlayerLose(gameCount, userBridge, playerStatus)) {
                break;
            }
        }
    }

    private static void playerMove(Bridge userBridge, PlayerStatus playerStatus) {
        bridgeGame.move(userBridge, playerStatus);
        outputView.printMap(userBridge);
    }

    private static boolean finishGamePlayerWin(int gameCount, Bridge userBridge, PlayerStatus playerStatus) {
        if (!playerStatus.isMatchingFlag()) {
            outputView.printResult(userBridge, gameCount, playerStatus);
            return true;
        }
        return false;
    }

    private static boolean finishGamePlayerLose(int gameCount, Bridge userBridge, PlayerStatus playerStatus) {
        String retryCommand = inputView.readGameCommand();

        if (bridgeGame.retry(retryCommand)) {
            outputView.printResult(userBridge, gameCount, playerStatus);
            return true;
        }
        return false;
    }

    private static PlayerStatus isMatchingWithBridge(Bridge bridge, String currentStep) {
        String nextStep = inputView.readMoving();

        if (currentStep.equals(nextStep)) {
            return new PlayerStatus(nextStep, true);
        }

        return new PlayerStatus(nextStep, false);
    }
}
