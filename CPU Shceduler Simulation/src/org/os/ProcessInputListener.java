package org.os;

import java.lang.Process;
import java.util.ArrayList;

public interface ProcessInputListener {
    void onInputComplete(ArrayList<org.os.Process> processes);
}