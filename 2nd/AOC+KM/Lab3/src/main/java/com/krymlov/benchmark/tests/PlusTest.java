package com.krymlov.benchmark.tests;

public class PlusTest implements IOpTest {

    public PlusTest() {
    }

    //returns number of operations per 1 sec
    @Override
    public long testByte(){
        long operations = 0;
        byte number = Byte.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Byte.MAX_VALUE-10){
                number = Byte.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testShort(){
        long operations = 0;
        short number = Short.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Short.MAX_VALUE-10){
                number = Short.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testInt(){
        long operations = 0;
        int number = Integer.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Integer.MAX_VALUE-10){
                number = Integer.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testLong(){
        long operations = 0;
        long number = Long.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Long.MAX_VALUE-10){
                number = Long.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testChar(){
        long operations = 0;
        char number = Character.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Character.MAX_VALUE-10){
                number = Character.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testFloat(){
        long operations = 0;
        float number = Float.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Float.MAX_VALUE-10){
                number = Float.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long testDouble(){
        long operations = 0;
        double number = Double.MIN_VALUE;
        long time = System.currentTimeMillis()+1000;
        while (System.currentTimeMillis() < time){
            if (number >= Double.MAX_VALUE-10){
                number = Double.MIN_VALUE;
            }
            number += (1 + (Math.random() * 3));
            operations++;
        }
        return operations;
    }

    @Override
    public long getOperations(String type) {
        long result = 0;
        if (type.equals("byte")){
            result = testByte();
            System.out.println("byte");
        }else if(type.equals("short")){
            result = testShort();
            System.out.println("short");
        }else if(type.equals("int")){
            result = testInt();
            System.out.println("int");
        }else if(type.equals("long")){
            result = testLong();
            System.out.println("long");
        }else if(type.equals("char")){
            result = testChar();
            System.out.println("char");
        }else if(type.equals("float")){
            result = testFloat();
            System.out.println("float");
        }else if(type.equals("double")){
            result = testDouble();
            System.out.println("double");
        }
        return result;
    }
}
