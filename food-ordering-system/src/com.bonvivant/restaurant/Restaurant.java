package restaurant;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import enums.OrderStatus;
import enums.RestaurantStatus;
import exceptions.BonVivantException;
import helper.Utils;
import order.Order;

public class Restaurant {
    private UUID id;
    private String name;
    private Menu menu;
    private RestaurantStatus status;
    private Integer capacity;
    private Deque<Order> orderProcessingQueue;
    private HashMap<UUID, Order> orderHistory;

    public Restaurant(String name,Integer capacity) {
        this.name = name;
        this.capacity = capacity;
        this.id = Utils.generateUUID();
        this.menu = new Menu();
        this.orderHistory = new HashMap<>();
        this.orderProcessingQueue = new LinkedList<>();
    }
    public Restaurant(String name,Integer capacity, Menu restaurantMenu) {
        this.name = name;
        this.capacity = capacity;
        this.id = UUID.randomUUID();
        this.menu = restaurantMenu;
        this.orderHistory = new HashMap<>();
        this.orderProcessingQueue = new LinkedList<>();
    }

    /**
     * @return the menu
     */
    public Menu getMenu() {
        return menu;
    }

    /**
     * add food in the menu
     * @return void
     */
    public void addFoodItem(FoodItem foodItem) {
        if(menu.findFoodItem(foodItem) == null){
            menu.addFoodItem(foodItem);
        }
    }

    /**
     * remove food in the menu
     * @return void
     */
    public void removeFoodItem(FoodItem foodItem) {
        if(menu.findFoodItem(foodItem.getName()) != null){
            try {
                menu.removeItem(foodItem.getName());
            } catch (BonVivantException e) {
                e.printStackTrace();
            }
        }
    }

    public void updateFoodItem(FoodItem foodItem) {
        menu.updateFoodItem(foodItem);
    }

    /**
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the status
     */
    public RestaurantStatus getStatus() {
        return status;
    }

    /**
     * @param capacity the capacity to set
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * @param menu the menu to set
     */
    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Boolean canAcceptOrder(){
        if(orderProcessingQueue.size() < capacity){
            return true;
        }
        return false;
    }

    public void addOrders(Order order) throws BonVivantException {
        if(!canAcceptOrder()){
            throw new BonVivantException("Running at full capacity, Restaurant can not accept order. Try after some time.");
        }
        this.orderHistory.put(order.getId(), order);
        orderProcessingQueue.addLast(order);
    }

    public OrderStatus getOrderStatus(Order order) throws BonVivantException {
        if(!orderHistory.containsKey(order.getId())){
            throw new BonVivantException("Order is not present. Check order id.");
        }
        return orderHistory.get(order.getId()).getStatus();
    }

    private Order orderCompleted() {
        if(orderProcessingQueue.size() == 0){
            return null;
        }
        return orderProcessingQueue.pollFirst();
    }

    private Order updateOrderStatus(Order order, OrderStatus status){
        order.setStatus(status);
        return order;
    }


}
