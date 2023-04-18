package ru.netology.moneytransfer.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.stereotype.Service;
import ru.netology.moneytransfer.repository.Card;
import ru.netology.moneytransfer.repository.CardsRepository;
import ru.netology.moneytransfer.repository.Operation;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class TransferService {
    CardsRepository cards;
    AtomicInteger operationId = new AtomicInteger();
    protected int bankCommPercent = 1;
    Logger logger = new Logger("transfer.log.txt");
    public TransferService(CardsRepository cards) {
        this.operationId.set(0);
        this.cards = cards;
        this.cards.addCard(new Card("0000000000000001","05/28","987","RUR"));
        this.cards.addCard(new Card("0000000000000002","05/28","123","RUR"));
        this.cards.cardOperation("0000000000000001","05/28","987",10000, Operation.ADD);
        this.cards.cardOperation("0000000000000002","05/28","123",10000, Operation.ADD);
    }

    protected String createMessage(String cardFrom, String cardFromValidTill, String cardFromCVV,
                                   String cardTo, float value, float bankComm, Result status) {
        String res = "Ошибка";
        if(status == Result.OK) {
            res ="id : " + operationId.toString() + " Перевод с карты " +
                    cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " на карту " + cardTo + " , сумма " + Float.toString(value) + " коммиссия " + bankComm;
        } else if (status == Result.INPUT_DATA_ERROR) {
            res ="id : " + operationId.toString() + " Ошибка перевода " +
                    cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " не верно указаны данные перевода";
        } else {
            res ="id : " + operationId.toString() + " Ошибка перевода " + cardFrom + " действ. до " + cardFromValidTill + " CVV " +
                    cardFromCVV + " на карту " + " операция не выполнена";
        }

        return res;
    }


    public Result goTransfer(String request) throws JsonSyntaxException {


        operationId.incrementAndGet();
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();

            String cardFrom = jsonObject.get("cardFromNumber").getAsString();
            String cardFromCVV = jsonObject.get("cardFromCVV").getAsString();
            String cardFromValidTill = jsonObject.get("cardFromValidTill").getAsString();
            JsonObject amount = jsonObject.get("amount").getAsJsonObject();
            String currency = amount.get("currency").getAsString();
        //    System.out.println(amount.get("value"));
            int tmpVal = amount.get("value").getAsInt();
            float value = (float) tmpVal / 100;
            float bankComm = (value * bankCommPercent) / 100;
            String cardTo = jsonObject.get("cardToNumber").getAsString();

        if(cards.cardOperation(cardFrom, cardFromValidTill, cardFromCVV,0,Operation.VALID_CHECK) &&
                cards.getCard(cardTo) != null) {
            if(cards.transfer(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm )) {
                logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.OK ));
                return Result.OK;
            } else {
                logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.OPERATION_ERROR ));
                return Result.OPERATION_ERROR;
            }

        } else {
            logger.printLog(createMessage(cardFrom, cardFromValidTill, cardFromCVV, cardTo, value, bankComm,Result.INPUT_DATA_ERROR ));
            return Result.INPUT_DATA_ERROR;
        }
    }

    public int getOperationId() {
        return operationId.get();
    }


}
