package printf1;

public class Printf1Test {
    public static void main(String[] args) {
        double[] d = new double[args.length];
        int doubleCount = -1;
        for (String arg: args){
            doubleCount++;
            try {
                d[doubleCount] = Double.parseDouble(arg);
            } catch (Exception ignored) {
                doubleCount--;
            }
        }
        int count = 0;
        if (doubleCount == -1) {
            count += Printf1.print(8, 4, 3.14);
            count += Printf1.print(8, 4, count);
        } else {
            for (int i = 0; i <= doubleCount; i++) {
                count += Printf1.print(8, 4, d[i]);
            }
        }
        System.out.println();
        for (int i = 0; i < count; i++)
            System.out.print("-");
        System.out.println();
    }
}
