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

//        SoapJaxbDataFormat soapDF = new SoapJaxbDataFormat("com.example.camelmulticastservice.model",
//                new ServiceInterfaceStrategy(ResponseMulticast.class, true));

        // convert pojo to json and multicast to
        // direct for call rest and direct for convert json to xml
        from("direct:multi-cast")
                .to("direct:send-soap");
//                .marshal().json(JsonLibrary.Jackson, RequestMulticast.class)
//                .log("log: json-format --> ${body}")
//                .multicast()
//                    .to("direct:json-to-xml")
//                    .to("direct:send-rest");

        // convert json to xml and send to direct for call soap service
//        from("direct:json-to-xml")
//                .unmarshal().json(JsonLibrary.Jackson, Map.class).marshal().xstream()
////                .to("direct:send-soap")
//                .log("log: xml-format --> ${body}");

        // call soap service
        from("direct:send-soap")
                .process(this::processRequestSoap)
//                .convertBodyTo(ItemRequest.class)
//                .setBody(constant("{$body}"))
                .setHeader(CxfConstants.OPERATION_NAME,
                        constant("Item"))
                .setHeader(CxfConstants.OPERATION_NAMESPACE,
                        constant("http://example-project.com/"))
                .to(getCxfUri() + "&defaultOperationName=sendErrorReport")
                .process(this::processResponseSoap)
//                .unmarshal().json(JsonLibrary.Jackson, ResponseMulticast.class)
//                .log("The title is: ${body}")
                .log("soap request ---> ${body}")
                .to("direct:send-to-broker");
//                .process(exchange -> exchange.getIn().setBody(exchange.getIn().getBody()));

        // call rest service
//        from("direct:send-rest")
//                .setHeader(Exchange.HTTP_METHOD, constant("POST"))
//                .setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
//                .setHeader("Accept",constant("application/json"))
//                .log("send request -->  ${body}")
//                .toD("http:localhost:8090/api/" +
//                        "receive-from-multiCastService?bridgeEndpoint=true&throwExceptionOnFailure=false")
//                .unmarshal().json(JsonLibrary.Jackson, ResponseMulticast.class)
//                .log("response  -->  ${body}");

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
        System.out.println(responseMulticast.toString());

    }

    public void processRequestSoap(Exchange exchange) throws Exception {
        RequestMulticast requestMulticast = exchange.getIn().getBody(RequestMulticast.class);
        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestMulticast.getId());
        itemRequest.setName(requestMulticast.getName());
        itemRequest.setAccount(requestMulticast.getAccount());
//        ItemRequest itemRequest = exchange.getContext().getTypeConverter()
//                .tryConvertTo(ItemRequest.class, exchange, requestMulticast);
        System.out.println("body: " + requestMulticast.toString());
//        System.out.println("body2: " + itemRequest.toString());
        System.out.println("body3: " + itemRequest.toString());
        exchange.getIn().setBody(itemRequest);
        String body = exchange.getIn().getBody(String.class);
        System.out.println(body);
        // change the message to say Hello
//        exchange.getOut().setBody("Hello " + body);
        // copy headers from IN to OUT to propagate them
//        exchange.getOut().setHeaders(exchange.getIn().getHeaders());
    }
}
