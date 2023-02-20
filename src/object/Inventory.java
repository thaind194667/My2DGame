//package object;
//
//public class Inventory {
//
//    private final int sizeX;
//    private final int sizeY;
//    private final int MaxSize = 50;
//    private final int MaxQuantity = 64;
//    private int size;
//    private SuperObject[] obj;
//
//    private int UsedObj[];
//    private int quantity[];
//    private int currentSize;
//
//    public Inventory() {
//        sizeX = 5;
//        sizeY = 4;
//        size = 0;
//
//        obj = new SuperObject[50];
//        quantity = new int[50];
//        UsedObj = new int[50];
//    }
//
//    public Inventory(int x, int y) {
//        sizeX = x;
//        sizeY = y;
//        size = 0;
//
//        obj = new SuperObject[50];
//        quantity = new int[50];
//    }
//
//    public void setQuantity(int i, int quantity) {
//        this.quantity[i] = quantity;
//    }
//    public void setObj(int i, SuperObject obj) {
//        this.obj[i] = obj;
//    }
//    public void setCurrentSize(int currentSize) {
//        this.currentSize = currentSize;
//    }
//
//    public SuperObject[] getObj() {
//        return obj;
//    }
//    public int[] getQuantity() {
//        return quantity;
//    }
//    public int getCurrentSize() {
//        return currentSize;
//    }
//
//    public void expand(int add) {
//        setCurrentSize(this.getCurrentSize() + add);
//    }
//
//    public void addObj(SuperObject newObj) {
//        boolean set = false;
//        int index = newObj.getId();
//        for (SuperObject now : obj) {
//            if(now == null) break;
//            int currentIndex = now.getId();
//            if (currentIndex == index) {
//                quantity[currentIndex]++;
//                set = true;
//                break;
//            }
//        }
//        if(!set) {
//            obj[size] = newObj;
//            quantity[size] = 1;
//            size++;
//        }
//    }
//
//}
