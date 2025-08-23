package com.status.dto;

import com.status.entities.SwitchEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SwitchStatus {
    private SwitchEntity switchEntity;
    private Integer status;
}
