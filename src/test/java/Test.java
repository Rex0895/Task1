public class Test{
    public static void main(String[] args) {
        String str = "1.643";
        String con = "^([0-9]+([.][0-9]*)?)$";//для любого вещ числа [+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)
        if(!str.matches(con)) System.out.println("Ошибка");
        else System.out.println(str);

    }
}