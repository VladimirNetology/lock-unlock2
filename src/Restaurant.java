import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant {
    public static final int MILLIS_WAITER = 5000;
    public static final int MILLIS_CUSTOMER = 2000;
    public static final int MILLIS_CUSTOMER_EATING = 4000;
    public List<Order> orders = new ArrayList<>();
    ReentrantLock lock = new ReentrantLock();
    Condition conditionWaiter = lock.newCondition();
    Condition conditionCustomer = lock.newCondition();
    boolean isDayOver = false;

    public void setDayOver(boolean dayOver) {
        isDayOver = dayOver;
    }

    public void takeOrder() {
        while (!isDayOver) {
            try {
                System.out.println("Waiter: Ready to work " + Thread.currentThread().getName());
                lock.lock();
                while (orders.size() == 0) {
                    System.out.println("Waiter: Waiting... " + Thread.currentThread().getName());
                    conditionWaiter.await();
                }
                System.out.println("Waiter: Take the order " + Thread.currentThread().getName());
                Thread.sleep(MILLIS_WAITER);
                orders.remove(0);
                System.out.println("Waiter: The order is completed " + Thread.currentThread().getName());
                conditionCustomer.signal();
                System.out.println("Waiter: Is ready to work again " + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock.unlock();
            }
        }
        System.out.println("Restaurant: Day is over");
    }

    public void makeOrder() {
        try {
            lock.lock();
            System.out.println("Customer: Ready to order " + Thread.currentThread().getName());
            Thread.sleep(MILLIS_CUSTOMER);
            orders.add(new Order());
            System.out.println("Customer: Ordered " + Thread.currentThread().getName());
            conditionWaiter.signal();
            System.out.println("Customer: Waiting... " + Thread.currentThread().getName());
            conditionCustomer.await();
            System.out.println("Customer: Eating " + Thread.currentThread().getName());
            Thread.sleep(MILLIS_CUSTOMER_EATING);
            System.out.println("Customer: Finish " + Thread.currentThread().getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
