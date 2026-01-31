package art.arcane.thaumcraft.api.capabilities;

public interface IAuraCapability {

    short getBaseVis();

    float getVis();
    void setVis(float value);
    float getFlux();
    void setFlux(float value);

}
