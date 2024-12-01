public class Day6 extends AbstractDay {

    public Day6() {
        super("day6");
    }


    record Record(long time, long distance) {
        public long nbBetter() {
            long k = 1;
            while ((time - k) * k <= distance) {
                k++;
            }
            long k2 = time - 1;
            while ((time - k) * k <= distance) {
                k--;
            }
            return time - k - k2;
        }
    }

    public static void main(String[] args) {
        new Day6().solution();
    }

    @Override
    public void solution() {
        var r1 = new Record(52, 426);
        var r2 = new Record(94, 1374);
        var r3 = new Record(75, 1279);
        var r4 = new Record(94, 1216);
        System.out.println(r1.nbBetter() * r2.nbBetter() * r3.nbBetter() * r4.nbBetter());
        long t0 = System.currentTimeMillis();

        var r5 = new Record(52947594, 426137412791216L);
        var nb = r5.nbBetter();
        long t1 = System.currentTimeMillis();
        System.out.println("time :" + (t1 - t0));
        System.out.println(nb);
    }

}
