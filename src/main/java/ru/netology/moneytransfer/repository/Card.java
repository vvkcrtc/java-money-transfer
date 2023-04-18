package ru.netology.moneytransferservice.repository;

public class Card {
    protected String cardNumber;
    protected String cardCVV;



    protected String cardValidTill;
    protected float balance;
    protected String currency;
    public Card(String cardNumber, String cardValidTill, String cardCVV, String currency) {
        this.cardNumber = cardNumber;
        this.cardCVV = cardCVV;
        this.cardValidTill = cardValidTill;
        this.currency = currency;
        balance = 0;
    }
    void addFunds(float count) {
        balance += count;
    }

    boolean payment(float count) {
        if(count > balance) {
            return false;
        } else {
            balance -= count;
            return true;
        }
    }
    public String getCardValidTill() {
        return cardValidTill;
    }
    public String getCardCVV() {
        return cardCVV;
    }

    public float getBalance() {
        return balance;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCardNumber() {
        return cardNumber;
    }
}
