package airalert.handler;

import airalert.service.AirAlertService;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.HashMap;
import java.util.Map;

public class LambdaFunctionHandler implements RequestHandler<Map<Object, Object>, String> {
    
    private final AirAlertService airAlertService = new AirAlertService();

    @Override
    public String handleRequest(Map<Object, Object> input, Context context) {
        System.out.println("Input: " + input);
        Boolean result = airAlertService.executeService(input);
        return result ? "success":"failed";
    }

    public static void main(String[] args) {
        LambdaFunctionHandler handler = new LambdaFunctionHandler();
        handler.handleRequest(new HashMap<>(), null);
    }
}
