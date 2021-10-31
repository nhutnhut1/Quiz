package com.example.englishpractice.model;

public class ProfileDB {

    private String name;
    private String email;
    private Long phone;
    private int bookmarkCount;

    public ProfileDB(String name, String email, Long phone, int bookmarkCount) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bookmarkCount = bookmarkCount;
    }

    public int getBookmarkCount() {
        return bookmarkCount;
    }

    public void setBookmarkCount(int bookmarkCount) {
        this.bookmarkCount = bookmarkCount;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
