package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

/**
 * @author kulabun
 * @since 3/25/17
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
