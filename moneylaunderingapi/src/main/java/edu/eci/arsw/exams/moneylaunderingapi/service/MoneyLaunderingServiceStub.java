package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;
import java.util.ArrayList;

import java.util.List;
import org.springframework.stereotype.Service;

//@Service ("MoneyLaunderingServiceStub")
@Service
public class MoneyLaunderingServiceStub implements MoneyLaunderingService {
    
    List<SuspectAccount> suspectAccounts = new ArrayList<SuspectAccount>();
    
    public MoneyLaunderingServiceStub(){
        suspectAccounts.add(new SuspectAccount("1", 100));
        suspectAccounts.add(new SuspectAccount("2", 200));
    }
        
    @Override
    public void updateAccountStatus(SuspectAccount suspectAccount) {
        //TODO
    }

    @Override
    public SuspectAccount getAccountStatus(String accountId) {
        //TODO
        for(SuspectAccount suspect: suspectAccounts){
            if(suspect.getAccountId().equals(accountId)){
                return suspect;
            }
        }
        return null;
        
    }

    @Override
    public List<SuspectAccount> getSuspectAccounts() {
        //TODO
        return suspectAccounts;
    }
    
     @Override
    public void postOffendingAccounts(SuspectAccount suspectAccount) {
        //TODO
        suspectAccounts.add(suspectAccount);
    }
    
}
