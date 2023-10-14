package com.example.userservice.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)//null인 필드를 노출하지 않는 어노테이션
public class ResponseUser {
    private String email;
    private String userId;
    private String name;

    private List<ResponseOrder> orders;
}
