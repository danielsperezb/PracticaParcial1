package edu.eci.arsw.exams.moneylaunderingapi.service;

import edu.eci.arsw.exams.moneylaunderingapi.model.SuspectAccount;

import java.util.List;
import org.springframework.stereotype.Service;

//@Service
public interface MoneyLaunderingService {
    void updateAccountStatus(SuspectAccount suspectAccount);
    SuspectAccount getAccountStatus(String accountId);
    List<SuspectAccount> getSuspectAccounts();
    void postOffendingAccounts(SuspectAccount suspectAccount);
}
