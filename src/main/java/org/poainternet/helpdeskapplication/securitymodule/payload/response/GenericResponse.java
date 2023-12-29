package org.poainternet.helpdeskapplication.securitymodule.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> {
    private String apiVersion;
    private String organizationName;
    private String message;
    private Integer status;
    private T data;
}
