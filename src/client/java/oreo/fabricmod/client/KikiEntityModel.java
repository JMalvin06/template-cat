package oreo.fabricmod.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CatEntityModel;
import oreo.fabricmod.entities.KikiEntity;

public class KikiEntityModel <T extends KikiEntity> extends CatEntityModel<T> {
    public KikiEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}
