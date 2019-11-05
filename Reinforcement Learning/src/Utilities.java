import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

public class Utilities {
    public static Object deepCopy(Object object) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream outputStrm = new ObjectOutputStream(outputStream);
            outputStrm.writeObject(object);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
            return objInputStream.readObject();
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static  float[] appendArrays(float[] array1, float[] array2){
        float[] array = new float[array1.length + array2.length];
        for(int i=0;i<array1.length;i++){
            array[i] = array1[i];
        }
        for(int i=array1.length;i<array1.length + array2.length;i++){
            array[i] = array2[i - array1.length];
        }
        return array;
    }
}
