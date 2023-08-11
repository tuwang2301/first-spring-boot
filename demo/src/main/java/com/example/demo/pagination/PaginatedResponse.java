package com.example.demo.pagination;

import com.example.demo.view.Views;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginatedResponse<T> {
    private PaginationMeta meta;
    private T data;
    @JsonView(Views.StudentWithoutClass.class)
    public T getData() {
        return data;
    }
}
