package org.zooway.antdvpro.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zooway
 * @create 2024/1/9
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ActionEntityDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String action;
    private String describe;
}
