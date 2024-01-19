package org.zooway.antdvpro.model.base;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@AllArgsConstructor
@Data
public class PageResp<T> {
    public static final int MAX_PAGE_SIZE = 1000;

    private int pageNum;
    private int pageSize;
    private long total;
    private List<T> content;
}
