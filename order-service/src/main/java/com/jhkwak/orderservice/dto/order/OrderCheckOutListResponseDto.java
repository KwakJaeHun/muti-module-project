package com.jhkwak.orderservice.dto.order;

import com.jhkwak.orderservice.dto.user.UserResponseDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@NoArgsConstructor
public class OrderCheckOutListResponseDto {
    private UserResponseDto userResponseDto;
    private List<OrderProductResponseDto> orderProductResponseDto;

    public OrderCheckOutListResponseDto(UserResponseDto userResponseDto, List<OrderProductResponseDto> orderProductResponseDto){
        this.userResponseDto = userResponseDto;
        this.orderProductResponseDto = orderProductResponseDto;
    }
}
