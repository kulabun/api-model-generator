package test;

import java.lang.Integer;
import java.lang.String;

public class AccountModel {
    private Integer amount;

    private CurrencyModel currency;

    private PersonModel person;

    public Integer getAmount() {
        return this.amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public CurrencyModel getCurrency() {
        return this.currency;
    }

    public void setCurrency(CurrencyModel currency) {
        this.currency = currency;
    }

    public PersonModel getPerson() {
        return this.person;
    }

    public void setPerson(PersonModel person) {
        this.person = person;
    }

    public static class CurrencyModel {
        private String code;

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public static class PersonModel {
        private String id;

        public String getId() {
            return this.id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}

