package oreo.fabricmod.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CatEntityModel;
import oreo.fabricmod.entities.TemplateCat;

public class TemplateEntityModel <T extends TemplateCat> extends CatEntityModel<T> {
    public TemplateEntityModel(ModelPart modelPart) {
        super(modelPart);
    }
}
