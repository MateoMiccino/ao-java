package ar.com.tamborindeguy.network.movement;

import ar.com.tamborindeguy.network.interfaces.IRequest;
import ar.com.tamborindeguy.network.interfaces.IRequestProcessor;
import physics.AOPhysics;
import position.WorldPos;

import java.util.Objects;

public class MovementRequest implements IRequest {

    public boolean valid;
    public int requestNumber;
    public WorldPos predicted;
    public AOPhysics.Movement movement;

    public MovementRequest(){}

    public MovementRequest(int requestNumber, WorldPos predicted, AOPhysics.Movement movement, boolean valid) {
        this.requestNumber = requestNumber;
        this.predicted = predicted;
        this.movement = movement;
        this.valid = valid;
    }

    @Override
    public void accept(IRequestProcessor processor, int connectionId) {
        processor.processRequest(this, connectionId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementRequest that = (MovementRequest) o;
        return Objects.equals(predicted, that.predicted) &&
                movement == that.movement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(predicted, movement);
    }
}
