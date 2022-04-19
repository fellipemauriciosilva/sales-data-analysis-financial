package br.com.agibank.sale.exceptions;

public class FileSaleDataException extends Exception {

    public FileSaleDataException(){
        super("Data line of file is invalid.");
    }

    public FileSaleDataException(String message){
        super(message);
    }

}