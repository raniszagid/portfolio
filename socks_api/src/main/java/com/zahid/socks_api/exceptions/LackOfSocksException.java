package com.zahid.socks_api.exceptions;

public class LackOfSocksException extends RuntimeException {
    public LackOfSocksException(int availableQuantity, int requestedQuantity) {
        super("Lack of products for this operation. There are " + availableQuantity +
                " pairs of socks available, while you request " + requestedQuantity);
    }
}
