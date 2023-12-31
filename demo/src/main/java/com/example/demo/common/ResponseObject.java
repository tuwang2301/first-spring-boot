package com.example.demo.common;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseObject<T> {
    private String status;
    private String message;
    @Nullable
    private T data;
}
