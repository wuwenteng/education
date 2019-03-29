package com.example.education.user;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ASUS
 */
@Data
public class Result<T> {
    private String code;
    private String message;
    private User user;
    private List<T> data = new ArrayList<>();

    public Result() {

    }

    public Result(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
