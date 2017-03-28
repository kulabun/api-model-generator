package test;

import com.labunco.requestentity.annotation.RequestEntity;
import com.labunco.requestentity.annotation.RequestField;

import java.util.concurrent.locks.Lock;

/**
 * @author kulabun
 */
@RequestEntity
public class Generic<X, Y extends Lock> {
    @RequestField
    private X x;
    @RequestField
    private Y y;
    @RequestField
    private Generic<X, Y> generic;

    public X getX() {
        return x;
    }

    public void setX(X x) {
        this.x = x;
    }

    public Y getY() {
        return y;
    }

    public void setY(Y y) {
        this.y = y;
    }

    public Generic<X, Y> getGeneric() {
        return generic;
    }

    public void setGeneric(Generic<X, Y> generic) {
        this.generic = generic;
    }
}
