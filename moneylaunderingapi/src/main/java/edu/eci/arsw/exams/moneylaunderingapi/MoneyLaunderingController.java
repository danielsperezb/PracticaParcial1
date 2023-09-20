package edu.eci.arsw.exams.moneylaunderingapi;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import edu.eci.arsw.exams.moneylaunderingapi.service.MoneyLaunderingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;

@RestController
@RequestMapping(value = "/fraud-bank-accounts")
public class MoneyLaunderingController {

    @Autowired
    //@Qualifier ("MoneyLaunderingServiceStub")
    MoneyLaunderingService moneyLaunderingService;

    @RequestMapping(method = RequestMethod.GET)
    public List<SuspectAccount> offendingAccounts() {
        return moneyLaunderingService.getSuspectAccounts();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> postOffendingAccounts(@RequestBody SuspectAccount suspectAccount) {
        moneyLaunderingService.postOffendingAccounts(suspectAccount);
        return new ResponseEntity<>("Objeto creado", HttpStatus.CREATED);
    }
    
    
    @RequestMapping(value = "/{accountId}", method=RequestMethod.GET)
    public ResponseEntity<?> getAccountId(@PathVariable String accountId){
        SuspectAccount suspect = moneyLaunderingService.getAccountStatus(accountId);
        if(suspect == null){
            return new ResponseEntity<>("NO ENCOTRADO", HttpStatus.NOT_FOUND);
        }else{
             return new ResponseEntity<>(suspect, HttpStatus.OK);
        }
    } 
    
    

    //TODO
}
