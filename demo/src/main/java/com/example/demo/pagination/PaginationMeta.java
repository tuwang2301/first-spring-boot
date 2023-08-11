package com.example.demo.pagination;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginationMeta {
    private int currentPage;
    private int pageSize;
    private int totalItems;
    private int totalPages;
}
