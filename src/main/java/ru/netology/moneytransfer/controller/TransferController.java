package ru.netology.moneytransfer.controller;

//import org.springframework.boot.configurationprocessor.json.JSONException;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.netology.moneytransfer.service.Result;
import ru.netology.moneytransfer.service.TransferService;


@CrossOrigin
@RestController

public class TransferController {
    TransferService service;

   public TransferController(TransferService service) {
        this.service = service;
   }


    @RequestMapping("/transfer")
    @PostMapping
    public ResponseEntity<String> getResponse(@RequestBody String request)  {
        System.out.println(request);
        //{"cardFromNumber":"1234123412341234","cardToNumber":"5678567856785678","cardFromCVV":"123","cardFromValidTill":"06/26","amount":{"currency":"RUR","value":50000}}

        Result res = service.goTransfer(request);
        int operationId = service.getOperationId();
        switch (res) {
            case OK:
                return new ResponseEntity<>(String.format("{\"description\":\"Success transfer\"," +
                        " \"operationId\":%s}", Integer.toString(operationId)), HttpStatus.OK);
            case INPUT_DATA_ERROR:
                return new ResponseEntity<>(String.format("{\"description\":\"Error input data\"," +
                        "\"message\":\"Error customer message : INPUT_DATA_ERROR\", " +
                        "\"id\":%s}", Integer.toString(operationId)), HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity<>(String.format("{\"description\":\"Error transfer\"," +
                        "\"message\":\"Error customer message\", " +
                        "\"id\":%s}", Integer.toString(operationId)), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @RequestMapping("/confirmOperation")
    @PostMapping
    public ResponseEntity<String> getConfirmResponse(@RequestBody String request) throws JsonSyntaxException {
        System.out.println(request);
        //{"code":"0000","operationId":12345}
        //JSONObject jsonObject = new JSONObject(request);
        JsonObject jsonObject = new JsonParser().parse(request).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();

        int operationId = service.getOperationId();

        return new ResponseEntity<>(String.format("{\"description\":\"Success confirmation\" ,\"operationId\":%s}",Integer.toString(operationId)), HttpStatus.OK);
    }

}
