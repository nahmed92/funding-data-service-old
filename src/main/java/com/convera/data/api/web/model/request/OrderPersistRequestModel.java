package com.convera.data.api.web.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@ApiModel
@Data
@Builder
public class OrderPersistRequestModel {

    @NotNull
    @JsonProperty
    private String orderId;

    @NotNull
    @JsonProperty
    private String orderStatus;

    @NotNull
    @JsonProperty
    private String fundingStatus;

    List<ContractSaveRequestModel> contractList;

}
