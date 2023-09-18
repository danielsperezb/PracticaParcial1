package edu.eci.arsw.moneylaundering;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MoneyLaundering
{
    //private TransactionAnalyzer transactionAnalyzer;
    private TransactionReader transactionReader;
    private int amountOfFilesTotal;
    private static AtomicInteger amountOfFilesProcessed;
    private static List<ProcessThread> hilos;
    private final int numeroHilos = 5;
    private static boolean estado;
    private static  List<File> transactionFiles;

    public MoneyLaundering()
    {
        //transactionAnalyzer = new TransactionAnalyzer();
        transactionReader = new TransactionReader();
        amountOfFilesProcessed = new AtomicInteger();
        hilos = new ArrayList<>();
        estado =true;
        
        transactionFiles = getTransactionFileList();
    }
    
    

    public void processTransactionData()
    {
        
        amountOfFilesProcessed.set(0);
        //List<File> transactionFiles = getTransactionFileList();
        amountOfFilesTotal = transactionFiles.size();
        //amountOfFilesTotal = 22; //PRUEBAS SIN 100,000
        //System.out.println(amountOfFilesTotal);
        
        
        int partesPorHilo = (int) Math.ceil((double) amountOfFilesTotal / numeroHilos); //5 numero de hilso
        
        int puntoIncial = 0;
	int puntoFinal = Math.min(partesPorHilo, amountOfFilesTotal);
        
        
        
         for (int i = 0; i < numeroHilos; i++) {
             
            //System.out.print(puntoIncial + "-" + puntoFinal);
            ProcessThread processThread = new ProcessThread(puntoIncial, puntoFinal, transactionFiles,amountOfFilesProcessed,transactionReader);

            puntoIncial = puntoFinal;
            puntoFinal = Math.min(puntoFinal + partesPorHilo, amountOfFilesTotal);

            hilos.add(processThread);
            processThread.start();
        }
         
         
    }
    
    public static void joinHilos(){
        for(ProcessThread hilo: hilos){
            try {
                hilo.join();
            } catch (InterruptedException ex) {
                Logger.getLogger(MoneyLaundering.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

//**
  

    private List<File> getTransactionFileList()
    {
        List<File> csvFiles = new ArrayList<>();
        try (Stream<Path> csvFilePaths = Files.walk(Paths.get("src/main/resources/")).filter(path -> path.getFileName().toString().endsWith(".csv"))) {
            csvFiles = csvFilePaths.map(Path::toFile).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvFiles;
    }

    public static void main(String[] args)
    {
        MoneyLaundering moneyLaundering = new MoneyLaundering();
        moneyLaundering.processTransactionData();
        //moneyLaundering.joinHilos();
        //Thread joinHilos = new Thread(() -> joinHilos());
        //joinHilos.start();
        System.out.println("AQUI");
        while(amountOfFilesProcessed.get() < transactionFiles.size())
        {
            Scanner scanner = new Scanner(System.in);
            String line = scanner.nextLine();
            if(line.contains("exit")){
                break;
            }
            
            if(line.isEmpty()){
                if(estado){
                    pauseHilos();
                    getReport(moneyLaundering);
                }else{
                    resumeHilos();
                    
                }
                
            }
            
           
        }
        
        //moneyLaundering.joinHilos();
        getReport(moneyLaundering);

    }
    
    public static void resumeHilos(){
         System.out.println("CONTINUA PROGRAMA");
         estado = true;
         for(ProcessThread hilo: hilos){
             hilo.resumeThread();
             
         }
    }
    
    public static void pauseHilos(){
         System.out.println("PUASA PROGRAMA");
        estado = false;
        for(ProcessThread hilo: hilos){
             hilo.pauseThread();
         }
    }
    
    public static  void  getReport(MoneyLaundering moneyLaundering){
          String message = "Processed %d out of %d files.\nFound %d suspect accounts:\n%s";
          Set<String> offendingAccounts = ProcessThread.getListOffendingAccounts();
            
          String suspectAccounts = offendingAccounts.stream().reduce("", (s1, s2)-> s1 + "\n"+s2);
            
          message = String.format(message, moneyLaundering.amountOfFilesProcessed.get(), moneyLaundering.amountOfFilesTotal, offendingAccounts.size(), suspectAccounts);
          System.out.println(message);
   }
            
            


}
