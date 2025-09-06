package org.example.doc;

public class Test extends Thread{


    static Thread t1 = new Thread();
    static Thread t2 = new Thread();

    String [] a = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
    @Override
    public void run() {
        while (true) {
//            synchronized (t1) {
//                for (int i = 0; i < a.length; i++) {
//                    t1.notify();
//                    System.out.print(a[i].toUpperCase());
//                    try {
//                        t1.wait();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//            }
            synchronized (t2) {

                for (int i = 0; i < a.length; i++) {
                    t2.notify();
                    System.out.print(a[i].toLowerCase());
                    try {
                        t2.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    public static void main(String[] args) {
        Test t1 = new Test();
        t1.start();
//        t1.start();
//        t2.start();
    }
}
