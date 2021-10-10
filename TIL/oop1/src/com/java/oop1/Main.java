package com.java.oop1;

public class Main {

    public static void main(String[] args) {
        Customer customer = new Customer(50000L);
        Order order = new Order();
        BreadSeller breadSeller = new BreadSeller(100000L);

        breadSeller.sellTo(customer, order);
        BreadBox breadBox = breadSeller.makeBread(order);
        customer.receive(breadBox);

        System.out.println("[구매자] 붕어빵 구매 후 남은 금액 = " + customer.getBag().getAmount());
        System.out.println("[판매자] 붕어빵 판매 후 남은 금액 = " + breadSeller.getStrongbox().getAmount());
        System.out.println("[붕어빵] 구매자가 받은 붕어빵 = " + customer.getBag().getBreadBox().getBreadBox().toString());
    }

}
