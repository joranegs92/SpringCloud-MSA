package com.example.catalogservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.Date;

/**
 * vo는 클라이언트로 보내는 데이터,
 * 그래서 null값은 보내지않는다.
 * @JsonInclude
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseCatalog {
    private String productId;
    private String productName;
    private Integer unitPrice;
    private Integer totalPrice;
    private Integer stock;
    private Date createdAt;
}
