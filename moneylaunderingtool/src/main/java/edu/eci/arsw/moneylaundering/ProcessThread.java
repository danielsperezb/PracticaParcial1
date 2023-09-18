/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dalum
 */
public class ProcessThread extends Thread{
    
   private int limiteInicial;
   private int limiteFinal;
   private static List<File> transactionFiles;
   private TransactionAnalyzer transactionAnalyzer; 
   private static AtomicInteger amountOfFilesProcessed;
   private TransactionReader transactionReader;
   private boolean activo;
   
   private static Set<String> listOffendingAccounts = new HashSet<>();
   
   public ProcessThread(int limiteInicial,int limiteFinal, List<File> transactionFiles, AtomicInteger amountOfFilesProcessed, TransactionReader transactionReader)
    {
        transactionAnalyzer = new TransactionAnalyzer();
        this.limiteInicial = limiteInicial;
        this.limiteFinal = limiteFinal;
        this.amountOfFilesProcessed = amountOfFilesProcessed;
        this.transactionReader = transactionReader;
        this.transactionFiles = transactionFiles;
        this.activo = true;

    }

   public void run(){
       //System.out.println(limiteInicial + " - " + limiteFinal);
       for(int i = limiteInicial; i < limiteFinal; i++){  
           if(!activo){
               try {
                   synchronized(this){
                        wait();
                   }
               } catch (InterruptedException ex) {
                   Logger.getLogger(ProcessThread.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
           List<Transaction> transactions = transactionReader.readTransactionsFromFile(transactionFiles.get(i));
            for(Transaction transaction : transactions)
            {
                transactionAnalyzer.addTransaction(transaction);
            }
            amountOfFilesProcessed.incrementAndGet();
       }
       System.out.println("Termine");
   }

   
   public void pauseThread(){ 
       this.activo = false;
       synchronized(listOffendingAccounts){
           listOffendingAccounts.addAll(transactionAnalyzer.listOffendingAccounts());
       }
   }
   
   public static Set<String> getListOffendingAccounts(){
       return listOffendingAccounts;
   }
   
   public void resumeThread(){
       this.activo = true;
       synchronized(this){
           notifyAll(); 
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


