package com.sber;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class Calc {

    public static void main(String[] args) {
        calculateFactorial("C:\\Users\\averg\\IdeaProjects\\sber\\lab10\\numbers.txt");
    }

    private static void calculateFactorial(String path){
        String line;
        try(BufferedReader br = new BufferedReader(new FileReader(path))){
            while ((line = br.readLine()) != null){
                int number = Integer.parseInt(line.trim());

                CompletableFuture<Void> result = CompletableFuture.runAsync(() -> {
                    long fact = getFactorial(number);
                    System.out.println(number + "! = " + fact);
                });

                CompletableFuture.allOf(result).join();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e){
            System.out.println("No numbers found");
        }
    }

    private static long getFactorial(int number){
        long result = 1;

        for (int i = 1; i <= number; i++){
            result *= i;
        }

        return result;
    }
}