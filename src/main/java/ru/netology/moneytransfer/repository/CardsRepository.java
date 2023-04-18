package ru.netology.moneytransferservice.repository;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;


@Repository
public class CardsRepository {

    protected List<Card> cards = new ArrayList<>();
    public CardsRepository(){}


    public void addCard(Card card) {
        cards.add(card);
    }

    public Card getCard(String cardNumber) {
        for(Card card : cards) {
            if(cardNumber.equals(card.getCardNumber())) {
                return card;
            }
        }
        return null;
    }
    public boolean cardOperation(String cardNumber, String cardValidTill, String cardCVV, float amount, Operation operation) {
        Card card = getCard(cardNumber);
        if(card == null) {
            return false;
        } else {
            switch (operation) {
                case ADD:
                    card.addFunds(amount);
                    break;
                case DEC:
                    if(cardValidTill.equals(card.getCardValidTill()) && cardCVV.equals(card.getCardCVV())) {
                        if(!card.payment(amount)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                    break;
                case VALID_CHECK:
                    if(!cardValidTill.equals(card.getCardValidTill()) || !cardCVV.equals(card.getCardCVV()))
                        return false;
                default: break;
            }
            return true;
        }
    }

    public boolean transfer(String numCardFrom, String cardValidTill, String cardCVV, String numCardTo, float amount, float bankComm) {
        if(cardOperation(numCardFrom, cardValidTill, cardCVV, amount+bankComm, Operation.DEC)) {
            cardOperation(numCardTo, "", "", amount, Operation.ADD);

            return true;
        }
        return false;
    }


}
