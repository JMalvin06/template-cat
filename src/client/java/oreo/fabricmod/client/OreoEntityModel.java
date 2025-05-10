package oreo.fabricmod.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CatEntityModel;
import oreo.fabricmod.entities.OreoEntity;

public class OreoEntityModel<T extends OreoEntity> extends CatEntityModel<T> {
    public OreoEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}