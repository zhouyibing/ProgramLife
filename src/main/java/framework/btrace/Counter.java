package framework.btrace;

public  class Counter{
        private static int totalCount = 0;
        public int add(int num) throws InterruptedException {
            totalCount+=num;
            sleep();
            return totalCount;
        }

        public void sleep() throws InterruptedException {
            Thread.sleep(1000);
        }
    }