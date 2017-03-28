package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * @author kulabun
 */
@RequestEntity
public class Recursive<T extends Recursive<T>> {
    @RequestField
    private Recursive<T> recursive;

    public Recursive<T> getRecursive() {
        return recursive;
    }

    public void setRecursive(Recursive<T> recursive) {
        this.recursive = recursive;
    }
}
