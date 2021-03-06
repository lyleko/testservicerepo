package edu.bigdata.training.testservice.dao;

import edu.bigdata.training.testservice.model.PersonEntity;
import org.apache.ignite.Ignite;
import org.apache.ignite.configuration.CacheConfiguration;

import javax.cache.Cache;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TestServiceRepository {
    private Ignite ignite;
    private CacheConfiguration<UUID, PersonEntity> personCacheConfiguration;


    public TestServiceRepository(Ignite ignite, CacheConfiguration<UUID, PersonEntity> personCacheConfiguration) {
        this.ignite = ignite;
        this.personCacheConfiguration = personCacheConfiguration;
    }

    public void update(UUID id, PersonEntity personEntity){
        ignite.getOrCreateCache(personCacheConfiguration).replace(id, personEntity);
    }

    public void save(PersonEntity personEntity){
        ignite.getOrCreateCache(personCacheConfiguration).put(personEntity.getId(), personEntity);
    }

    public void delete(UUID id){
        ignite.getOrCreateCache(personCacheConfiguration).remove(id);
    }

    public PersonEntity get(UUID id){
        return ignite.getOrCreateCache(personCacheConfiguration).get(id);
    }

    public List<PersonEntity> getAll(){
        Iterable<Cache.Entry<UUID,PersonEntity>> iterable = () -> ignite.getOrCreateCache(personCacheConfiguration).iterator();


        return StreamSupport
                .stream(iterable.spliterator(), false)
                .map(Cache.Entry::getValue)
                .collect(Collectors.toList());

    }
}
