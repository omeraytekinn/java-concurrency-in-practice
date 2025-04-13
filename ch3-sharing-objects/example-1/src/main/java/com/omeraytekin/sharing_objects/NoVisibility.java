package com.omeraytekin.sharing_objects;

public class NoVisibility {
    static boolean ready;
    static int number;

    static class ReaderThread extends Thread {
        public void run() {
            while (!ready) {
                // Yoğun işlem simülasyonu
                for (int i = 0; i < 1_000_000; i++) {
                    Math.sin(i);
                }
            }
            System.out.println(number);
        }
    }

    public static void main(String[] args) {
        new ReaderThread().start();
        // Yazma sırasında bir gecikme ekliyoruz
        try {
            Thread.sleep(100); // 100ms bekleme
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        number = 42;
        ready = true;
    }
}
