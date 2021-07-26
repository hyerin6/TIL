package com.java.oop1;

public class Main {

    public static Order makeOrder() {
        Order order = new Order();
        order.addOrder(Flavor.CREAM, 1);
        order.addOrder(Flavor.RED_BEAN, 1);
        order.addOrder(Flavor.SPICY, 1);
        return order;
    }

    public static void main(String[] args) {
        Customer customer = new Customer(50000L);
        Order order = makeOrder();
        Seller seller = new Seller(100000L);
        seller.sellTo(customer, order);
        BreadBox breadBox = seller.makeBread(order);
        System.out.println("[구매자] 붕어빵 구매 후 남은 금액 = " + customer.getAmount());
        System.out.println("[판매자] 붕어빵 판매 후 남은 금액 = " + seller.getStrongbox().getAmount());
        System.out.println("[붕어빵] 구매자가 받은 붕어빵 = " + breadBox.getBreadBox().toString());
    }

}
