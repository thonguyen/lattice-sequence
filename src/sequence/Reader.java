package sequence;

import java.io.BufferedReader;
import java.io.IOException;

public interface Reader<T>{
    void read(T t, BufferedReader file) throws IOException;
}
