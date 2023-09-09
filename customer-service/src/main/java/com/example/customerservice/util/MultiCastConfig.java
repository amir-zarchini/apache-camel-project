package com.example.customerservice.util;

import com.example.customerservice.model.RequestMulticast;
import com.example.customerservice.model.ResponseMulticast;
import com.example_project.Item;
import com.example_project.ItemRequest;
import com.example_project.ItemResponse;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.DataFormat;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MultiCastConfig extends RouteBuilder {

    @Override
    public void configure() {

        //multicast request to soap and rest service
        from("direct:multi-cast")
                .multicast()
                    .to("direct:send-rest")
                    .to("direct:send-soap");

        // call soap service
        from("direct:send-soap")
                .process(this::processRequestSoap)
                .setHeader(CxfConstants.OPERATION_NAME,
                        constant("Item"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE,
                        constant("http://example-project.com/"))
                .to(getCxfUri() + "&defaultOperationName=sendErrorReport")
                .process(this::processResponseSoap)
                .log("soap request ---> ${body}")
                .to("direct:send-to-broker");

        // call rest service
        from("direct:send-rest")
                .marshal().json(JsonLibrary.Jackson, RequestMulticast.class)
                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
                .setHeader("Accept",constant("application/json"))
                .log("send request -->  ${body}")
                .toD("http:localhost:8090/api/" +
                        "receive-from-multiCastService?bridgeEndpoint=true&throwExceptionOnFailure=false")
                .unmarshal().json(JsonLibrary.Jackson, ResponseMulticast.class)
                .log("response  -->  ${body}");

        from("direct:send-to-broker")
                .log(LoggingLevel.ERROR, " log camel ---> ${body}")
                .marshal().json(JsonLibrary.Jackson, ResponseMulticast.class)
                .log(LoggingLevel.ERROR, " log after marshal ---> ${body}")
                .to("kafka:topic-customer-service")
                .unmarshal().json(JsonLibrary.Jackson, ResponseMulticast.class)
                .log(LoggingLevel.ERROR, " log after unmarshal ---> ${body}");
    }

    static String getCxfUri() {
        return "cxf://http://localhost:8095/ws"
                + "?wsdlURL=classpath:item.wsdl"
                + "&serviceClass=" + Item.class.getName()
                + "&dataFormat=" + DataFormat.POJO;
    }

    public void processResponseSoap(Exchange exchange) throws Exception {
        List<Object> itemResponse = (List<Object>) exchange.getIn().getBody();
        ItemResponse result = (ItemResponse) itemResponse.get(0);
        ResponseMulticast responseMulticast = new ResponseMulticast();
        responseMulticast.setId(result.getId());
        responseMulticast.setResponseStatus(result.getCategory());
        exchange.getIn().setBody(responseMulticast);
    }

    public void processRequestSoap(Exchange exchange) throws Exception {
        RequestMulticast requestMulticast = exchange.getIn().getBody(RequestMulticast.class);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestMulticast.getId());
        itemRequest.setName(requestMulticast.getName());
        itemRequest.setAccount(requestMulticast.getAccount());
        exchange.getIn().setBody(itemRequest);
    }
}
