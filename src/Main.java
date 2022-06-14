public class Main {
    public static void main(String[] args)  {
        final Restaurant restaurant = new Restaurant();

        new Thread(restaurant::takeOrder).start();
        new Thread(restaurant::makeOrder).start();
        new Thread(restaurant::makeOrder).start();
        new Thread(restaurant::makeOrder).start();
        new Thread(restaurant::makeOrder).start();
        new Thread(restaurant::takeOrder).start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        restaurant.setDayOver(true);
    }
}

