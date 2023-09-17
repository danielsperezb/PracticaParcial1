/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author dalum
 */
public class ProcessThread {
    
   private int limiteInicial;
   private int limiteFinal;
   private static List<File> transactionFiles;
   private TransactionAnalyzer transactionAnalyzer; 
   private static AtomicInteger amountOfFilesProcessed;
   private TransactionReader transactionReader;
   
   public ProcessThread(int limiteInicial,int limiteFinal, List<File> transactionFiles, AtomicInteger amountOfFilesProcessed, TransactionReader transactionReader)
    {
        transactionAnalyzer = new TransactionAnalyzer();
        this.limiteInicial = limiteInicial;
        this.limiteFinal = limiteFinal;
        this.amountOfFilesProcessed = amountOfFilesProcessed;
        this.transactionReader = transactionReader;
        
    }

   public void run(){
       for(int i = limiteInicial; i < limiteFinal; i++){
           List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(i));
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
       }
   }
   
   /**
   public void processTransactionData()
    {
        //amountOfFilesProcessed.set(0);
        //List<File> transactionFiles = getTransactionFileList();
        //samountOfFilesTotal = transactionFiles.size();
        for(File transactionFile : transactionFiles)
        {            
            List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFile);
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
        }
    }
    * */
   
}
