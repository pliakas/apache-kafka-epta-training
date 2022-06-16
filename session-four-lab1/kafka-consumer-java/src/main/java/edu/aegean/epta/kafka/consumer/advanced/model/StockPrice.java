package edu.aegean.epta.kafka.consumer.advanced.model;

import io.advantageous.boon.json.JsonFactory;

public class StockPrice {

    private final int euros;
    private final int cents;
    private final String name;

    public StockPrice(final String json) {
        this(JsonFactory.fromJson(json, StockPrice.class));
    }

//
//    public StockPrice() {
//        euros = 0;
//        cents = 0;
//        name ="";
//    }

    public StockPrice(final String name, final int dollars, final int cents) {
        this.euros = dollars;
        this.cents = cents;
        this.name = name;
    }



    public StockPrice(final StockPrice stockPrice) {
        this.cents = stockPrice.cents;
        this.euros = stockPrice.euros;
        this.name = stockPrice.name;
    }


    public String toJson() {
        return "{" +
                "\"euros\": " + euros +
                ", \"cents\": " + cents +
                ", \"name\": \"" + name + '\"' +
                '}';
    }


    public int getEuros() {
        return euros;
    }


    public int getCents() {
        return cents;
    }


    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "StockPrice{" +
                "euros=" + euros +
                ", cents=" + cents +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StockPrice that = (StockPrice) o;

        if (euros != that.euros) return false;
        if (cents != that.cents) return false;
        return name != null ? name.equals(that.name) : that.name == null;
    }

    @Override
    public int hashCode() {
        int result = euros;
        result = 31 * result + cents;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}