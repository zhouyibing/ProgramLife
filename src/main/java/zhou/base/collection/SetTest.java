package zhou.base.collection;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Zhou Yibing on 2016/1/19.
 */
public class SetTest {

    public static void main(String[] args){
       Point point = new Point(12,34);
       Point point2 = new Point(13,34);
       Point point3 = new Point(13,34);
        Set<Point> set = new HashSet<Point>();
        set.add(point);
        set.add(point2);
        set.add(point3);
        point3.setX(14);
        set.remove(point3);
        System.out.println(set.size());
    }

    public static class Point{
        private int x;
        private int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
}
