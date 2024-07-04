package com.jhkwak.userservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class OrderListResponseDto {
    private Long orderListId;
    private Character status;
    private LocalDateTime createdAt;
    private List<Map<String, Object>> orderListDetails; // JsonIgnore Annotation 으로 대체
}
