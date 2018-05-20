package com.yefei.happy_code_generator.model;

import lombok.Data;

import java.util.List;

@Data
public class DictionaryGroupDTO {
    private String agencyPk;

    private String name;

    private String changeName;

    private String describe;

    private List<DictionaryInfoDTO> dictionaryInfoDtoList;
}
