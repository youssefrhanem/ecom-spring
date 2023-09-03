package com.rhanem.config;

import com.rhanem.entity.Country;
import com.rhanem.entity.Product;
import com.rhanem.entity.ProductCategory;
import com.rhanem.entity.State;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.mapping.ExposureConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Configuration
public class MyDataRestConfig implements RepositoryRestConfigurer {


    private EntityManager entityManager;

    public MyDataRestConfig(EntityManager theEntityManager){
        entityManager = theEntityManager;
    }

    /**
     * Override this method to add additional configuration.
     *
     * @param config Main configuration bean.
     * @param cors   CORS configuration.
     * @since 3.4
     */
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        HttpMethod [] theUnsupportedActions = {HttpMethod.DELETE, HttpMethod.POST, HttpMethod.PUT};

        disableHttp(Product.class, config, theUnsupportedActions);
        disableHttp(ProductCategory.class, config, theUnsupportedActions);
        disableHttp(Country.class, config, theUnsupportedActions);
        disableHttp(State.class, config, theUnsupportedActions);


        // call an internal helper methode to help us expose the id
        exposeIds(config);



    }

    private static void disableHttp(Class theClass ,RepositoryRestConfiguration config, HttpMethod[] theUnsupportedActions) {
        config.getExposureConfiguration()
                .forDomainType(theClass)
                .withItemExposure(((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions)))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedActions));
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        // expose the id
        //get a list of all entity classes from the entity manager
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();

        // create an array of the entity types
        List<Class> entityClasses = new ArrayList<>();
        // get all the entitiy types for the entities
        for (EntityType tempEntityType: entities) {
            entityClasses.add(tempEntityType.getJavaType());
        }

        // expose th entity ids for the array of entities/domain types

        Class[] domainType = entityClasses.toArray(new Class[0]);
        config.exposeIdsFor(domainType);

    }
}
