package ru.zahid.springcourse.models;

public class Book {
    private int number;
    private String bookName;
    private String author;
    private int publicationYear;
    private int ownerId;

    public Book() {}

    public Book(int number, String bookName, String author, int publicationYear) {
        this.number = number;
        this.bookName = bookName;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }
}
