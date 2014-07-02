
package net.specialattack.forge.discotek.sync;

import net.specialattack.forge.core.sync.SFloat;
import net.specialattack.forge.discotek.client.ClientProxy;
import net.specialattack.forge.discotek.tileentity.TileEntityLight;

public class SVariableFloat extends SFloat {

    private final TileEntityLight owner;

    public SVariableFloat(TileEntityLight owner, float value) {
        super(owner, value);
        this.owner = owner;
    }

    public SVariableFloat(TileEntityLight owner) {
        super(owner);
        this.owner = owner;
    }

    @Override
    public float getValue() {
        if (owner != null && owner.getBoolean("beat", 1.0F)) {
            return super.getValue() * (ClientProxy.beat ? 1.0F : 0.5F);
        }
        else {
            return super.getValue();
        }
    }

    public float getValueDirect() {
        return super.getValue();
    }

    @Override
    public String toString() {
        return "VariableFloat: " + this.getValueDirect();
    }

}
