
public class TempTest {
    public static void main(String[] args) {
        int b = 0;
        int[] a = {1,2,10,4,1,4,3,3};
        for (int num : a) {
            b^=num;
        }
        System.out.println(b);
        b&=-b;
        System.out.println(b);
        System.out.println(b&24);
    }
}
