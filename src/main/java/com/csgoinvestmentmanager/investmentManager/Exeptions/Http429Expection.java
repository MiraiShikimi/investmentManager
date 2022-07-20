package com.csgoinvestmentmanager.investmentManager.Exeptions;

public class Http429Expection extends RuntimeException{

    public Http429Expection(String message) {
        super(message);
    }

}
