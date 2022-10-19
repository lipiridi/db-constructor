package com.divizia.dbconstructor.model.compositeKeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisiteId implements Serializable {

    private String id;
    private String customTable;

}
