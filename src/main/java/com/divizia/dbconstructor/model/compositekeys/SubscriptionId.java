package com.divizia.dbconstructor.model.compositekeys;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionId implements Serializable {

    private String user;
    private String customTable;

}
