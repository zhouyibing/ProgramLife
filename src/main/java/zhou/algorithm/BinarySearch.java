package zhou.algorithm;

public class BinarySearch<T extends Comparable>{

    public int search(T[] arr,T s){
        int low=0,high=arr.length;
        int mid=0;
        while (low<=high){
            mid=(low+high)/2;
            int cmp = arr[mid].compareTo(s);
            if (cmp == 0) return mid;
            if (cmp > 0) {
                high=mid-1;
            } else {
                low=mid+1;
            }
        }
        return -1;
    }
    public static void main(String[] args){
        BinarySearch<Integer> binarySearch = new BinarySearch<>();
        System.out.println(binarySearch.search(new Integer[]{12},12));
        System.out.println(binarySearch.search(new Integer[]{12,32},12));
        System.out.println(binarySearch.search(new Integer[]{12,32,435},12));
        System.out.println(binarySearch.search(new Integer[]{12,32,435,3453},12));
        System.out.println(binarySearch.search(new Integer[]{12,42,46,67,78,98,102,135,167,236,265,765,453,327,764,4642},102));
    }
}
