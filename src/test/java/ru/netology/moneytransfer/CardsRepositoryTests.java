package ru.netology.moneytransfer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.moneytransfer.repository.Card;
import ru.netology.moneytransfer.repository.CardsRepository;
import ru.netology.moneytransfer.repository.Operation;

public class CardsRepositoryTests {
    static CardsRepository cards = new CardsRepository();
    @BeforeAll
    public static void init() {
        cards.addCard(new Card("1234567812340001","05/28","987","RUB"));
        cards.addCard(new Card("1234567812340002","08/26","789","RUB"));
        cards.addCard(new Card("1234567812340003","07/18","111","RUB"));
        cards.addCard(new Card("1234567812340004","01/23","987","RUB"));
    }

    @Test
    public void cardOperationTest() {
        boolean[] expect = new boolean[] {false, false, false, false, true, true,false};
        boolean[] result = new boolean[7];
        result[0] = cards.cardOperation("1234567812340001","05/28","987", 100, Operation.DEC );
        result[1] = cards.cardOperation("1234567812340001","05/28","000", 100, Operation.DEC );
        result[2] = cards.cardOperation("1234567812340001","05/21","987", 100, Operation.DEC );
        result[3] = cards.cardOperation("0000567812340001","05/28","987", 100, Operation.ADD );
        result[4] = cards.cardOperation("1234567812340001","05/28","987", 100, Operation.ADD );
        result[5] = cards.cardOperation("1234567812340001","05/28","987", 100, Operation.VALID_CHECK );
        result[6] = cards.cardOperation("1234567812340001","05/28","000", 100, Operation.VALID_CHECK );
        boolean actual = true;
        for(int i = 0; i < expect.length; i++) {
            if(expect[i] != result[i]) {
                actual = false;
            }
        }

        Assertions.assertEquals(true,actual);

    }
    @Test
    public void transferTest() {
        cards.cardOperation("1234567812340003","","", 1000, Operation.ADD );
        cards.transfer("1234567812340003","07/18","111","1234567812340002",500 ,0);
        Assertions.assertEquals(true, (cards.getCard("1234567812340003").getBalance() == 500 && cards.getCard("1234567812340002").getBalance() == 500));

    }

}
