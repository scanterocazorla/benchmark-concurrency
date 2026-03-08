
public class Main {

    public static void main(String[] args) {
        int nTasks = 200;
        int sizeTask = 200000;

        //VERSIÓN SECUENCIAL
        long t = System.nanoTime();
        for (int i = 0; i<nTasks; i++){
            Task(sizeTask);
        }
        t = System.nanoTime() - t;
        double tsecuencial = (double) t / 1e9;
        System.out.println("Tiempo secuencial: " + tsecuencial);

        //VERSIÓN PARALELA
        for(int numThreads = 2; numThreads<=32; numThreads = numThreads * 2){
            final int threadsNow = numThreads;
            t = System.nanoTime();
            Thread[] threads = new Thread[threadsNow];
            for (int i = 0; i<threadsNow ; i++){
                final int id = i;
                threads[i] = new Thread(() -> {
                    for (int j = id; j<nTasks ; j+=threadsNow){
                        Task(sizeTask);
                    }
                });
                threads[i].start();
            }

            for (int i = 0; i<threadsNow; i++){
                try{
                    threads[i].join();
                }
                catch (InterruptedException e){
                    System.out.println("err");
                }
            }
            t = System.nanoTime() - t;
            double tparalelo = (double) t / 1e9;
            System.out.println("Tiempo paralelo para " + numThreads + " hilos es: " + tparalelo);
            System.out.println("Speedup: " + (tsecuencial/tparalelo));
        }
    }
        

    public static void Task(int n) {
        int count = 0;
        for (int i = 2; i < n; i++) {
            if (isPrime(i)) {
                count++;
            }
        }
    }

    public static boolean isPrime(int num) {
        double limit = Math.sqrt(num);
        for (int i = 2; i <= limit; i++) {
            if (num % i == 0) {
                return false;
            }
        }
        return true;
    }
}
