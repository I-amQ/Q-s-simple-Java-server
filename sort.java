class sort{
    public static void main(String[] args){
        int[] A = {8,7,2,2,3,4,2312,31,2,31,23,13,12,31,3,123,131,23,1,322,13,2254,54,6,54,65,6,5,436,769,79,876,97,69,67,9,67,9,6,9,967};
        insertionSort(A);
        selectionSort(A);
        printArray(A);
    }

    public static void selectionSort(int[] array){

        for(int i = 0; i < array.length; i++){
            for(int j = i +1; j < array.length; j++){
                if (array[j] < array[i]){
                    int t = array[i];
                    array[i] = array[j];
                    array[j] = t;
                }
            }
        }
    }
    

    public static void insertionSort(int[] array){
        for(int i = 1; i < array.length; i++) {
            int v = array[i];
            int j = i;
            while (v < array[j - 1]) {
                array[j] = array[j - 1];
                j = j - 1;
                if (j == 0) break;
            }
            array[j] = v;
        }
    }

    public static void printArray(int[] array){
        for (int index = 0; index < array.length; index ++){
            System.out.print(array[index]+" ");
        }
    }
}