package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.entity.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.GenerateCategory;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

public abstract class AbstractCategoryExtension implements BeforeEachCallback, AfterEachCallback {

    public static final ExtensionContext.Namespace NAMESPACE
            = ExtensionContext.Namespace.create(AbstractCategoryExtension.class);

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(
                context.getRequiredTestMethod(),
                GenerateCategory.class
        ).ifPresent(
                cat -> {

                    CategoryJson json = new CategoryJson(
                            null,
                            cat.category(),
                            cat.username());

                    context.getStore(NAMESPACE).put(context.getUniqueId(), createCategory(json));
                }
        );
    }

    @Override
    public void afterEach(ExtensionContext context) {

        CategoryJson categoryJson = context.getStore(NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        removeCategory(categoryJson);
    }

    protected abstract CategoryJson createCategory(CategoryJson spend);

    protected abstract void removeCategory(CategoryJson spend);

    
}
