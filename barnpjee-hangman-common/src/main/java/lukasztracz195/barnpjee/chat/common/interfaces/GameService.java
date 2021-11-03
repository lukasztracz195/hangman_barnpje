package lukasztracz195.barnpjee.chat.common.interfaces;


public interface GameService {

    String sendAnswer(String sendAnswerRequestAsJson);

    String exitFromGame(String exitFromGameRequestASJson);

    String getStateOfGame(String getStateOfGameRequestAsJson);

    String eliminateCategory(String eliminateCategoryRequestAsJson);

    boolean isYourMoveInGame(String isYourMoveInGameRequestAsJson);

    String markThatUserIsReadyToGame(String informThatUserIsReadyToGameRequestAsJson);

    String sendAnswerAndGetGameState(String answerRequestAsJson);

    String getPasswordWithAnyGapsFilled(String getPasswordWithAnyGapsFilledRequestASJson);
}
